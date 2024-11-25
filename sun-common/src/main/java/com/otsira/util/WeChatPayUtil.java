package com.otsira.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otsira.properties.WechatProperties;
import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 微信支付的工具类
 * @create: 2024/11/24 17:15
 */
@Component
@Slf4j
public class WeChatPayUtil {
    // 微信支付下单接口地址
    public static final String JSAPI = "https://api.mch.weixin.qq.com/v3/pay/transactions/jsapi";

    // 申请退款接口地址
    public static final String REFUNDS = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

    private WechatProperties weChatProperties;

    @Autowired
    public void setWeChatProperties(WechatProperties weChatProperties) {
        this.weChatProperties = weChatProperties;
    }

    /**
     * 获取调用微信接口的客户端工具对象
     * @return CloseableHttpClient 客户端工具对象
     */
    private CloseableHttpClient getWechatHttpClient() {
        try {
            // 加载商户 API 私钥 (apiclient_key.pem 文件)
            PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new FileInputStream(weChatProperties.getPrivateKeyFilePath()));

            // 加载平台证书文件 (wechatpay_xxxxxxxxx.pem 文件)
            X509Certificate x509Certificate = PemUtil.loadCertificate(new FileInputStream(weChatProperties.getWeChatPayCertFilePath()));

            // wechatPayCertificates 微信支付平台证书列表。你也可以使用后面章节提到的“定时更新平台证书功能”，而不需要关心平台证书的来龙去脉
            List<X509Certificate> wechatPayCertificates = Collections.singletonList(x509Certificate);

            // 通过 WechatPayHttpClientBuilder 构造的 HttpClient，会自动的处理签名和验签
            return WechatPayHttpClientBuilder.create()
                    .withMerchant(weChatProperties.getMchid(), weChatProperties.getMchSerialNo(), merchantPrivateKey)
                    .withWechatPay(wechatPayCertificates)
                    .build();
        } catch (FileNotFoundException e) {
            log.info("获取微信接口客户端工具对象失败: {}", e.getMessage());
            throw new RuntimeException("获取微信接口客户端工具对象失败");
        }
    }

    /**
     * 发送 POST 请求
     * @param url 请求地址
     * @param body 请求体
     * @return 请求结果
     * @throws Exception 异常
     */
    private String post(String url, String body) throws Exception {
        // 通过 WechatPayHttpClientBuilder 构造的 HttpClient，会自动的处理签名和验签
        CloseableHttpClient httpClient = getWechatHttpClient();

        if (httpClient == null) {
            return null;
        }

        // 封装 POST 请求头和请求体
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpPost.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpPost.addHeader("Wechatpay-Serial", weChatProperties.getMchSerialNo());
        httpPost.setEntity(new StringEntity(body, "UTF-8"));

        // 给微信第三方发送 POST 请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        if (response == null) {
            httpClient.close();
            return null;
        }

        httpClient.close();
        response.close();
        return EntityUtils.toString(response.getEntity());
    }

    /**
     * 发送 GET 请求
     * @param url 请求地址
     * @return 请求结果
     * @throws Exception 异常
     */
    private String get(String url) throws Exception {
        CloseableHttpClient httpClient = getWechatHttpClient();

        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.toString());
        httpGet.addHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
        httpGet.addHeader("Wechatpay-Serial", weChatProperties.getMchSerialNo());

        CloseableHttpResponse response = null;
        if (httpClient != null) {
            response = httpClient.execute(httpGet);
        }
        try {
            if (response != null) {
                return EntityUtils.toString(response.getEntity());
            }
        } finally {
            if (httpClient != null) {
                httpClient.close();
            }
            if (response != null) {
                response.close();
            }
        }
        return null;
    }

    /**
     * jsapi下单
     * @param orderNum 商户订单号
     * @param total 金额
     * @param description 商品描述
     * @param openid 微信用户的openid
     * @return 下单结果
     * @throws Exception 异常
     */
    private String jsapi(String orderNum, BigDecimal total, String description, String openid) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("appid", weChatProperties.getAppid());
        jsonObject.put("mchid", weChatProperties.getMchid());
        jsonObject.put("description", description);
        jsonObject.put("out_trade_no", orderNum);
        /*
        notify_url 详解: 微信第三方在用户支付成功后, 给服务器的通知地址。
            需要公网地址: http://www.otsira.org/api/user/notify/paySuccess
            http://www.otsira.org = 用 cpolar 通过指定域名 www.otsira.org 内网穿透到 http://localhost:80
            /api = nginx 反向代理到 localhost:8080 网关
            /user = 网关会路由到用户端微服务 sun-user
            所以, 上述网址相当于: http://localhost:8080/user/notify/paySuccess
            tomcat 服务器: http://localhost:8082/notify/paySuccess
        */
        jsonObject.put("notify_url", weChatProperties.getNotifyUrl());

        JSONObject amount = new JSONObject();
        // 金额
        amount.put("total", total.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue());
        // 币种
        amount.put("currency", "CNY");

        jsonObject.put("amount", amount);

        JSONObject payer = new JSONObject();
        payer.put("openid", openid);

        jsonObject.put("payer", payer);

        String body = jsonObject.toJSONString();

        // 给微信第三方发送 POST 请求, 返回微信第三方的响应体内容
        return post(JSAPI, body);
    }

    /**
     * 小程序支付
     * @param orderNum 商户订单号
     * @param total 支付金额
     * @param description 商品描述
     * @param openid 微信用户的openid
     * @return 给前端制作好的 JSON 数据, 用于通过前端 wx.request() 调起微信支付
     * @throws Exception 异常
     */
    public JSONObject pay(String orderNum, BigDecimal total, String description, String openid) throws Exception {
        // 统一下单，生成预支付交易单
        String wechatResponseBodyAsString = jsapi(orderNum, total, description, openid);

        // 解析返回结果
        JSONObject jsonObject = JSON.parseObject(wechatResponseBodyAsString);
        log.info("微信第三方返回的预支付响应体内容: {}", jsonObject);

        // 拿到了微信第三方的预支付交易单号, 开始给前端制作数据
        // 用于前端通过 wx.request() 调起微信支付
        String prepayId = jsonObject.getString("prepay_id");
        if (prepayId != null) {
            String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
            String nonceStr = RandomStringUtils.randomNumeric(32);
            ArrayList<Object> list = new ArrayList<>();
            list.add(weChatProperties.getAppid());
            list.add(timeStamp);
            list.add(nonceStr);
            list.add("prepay_id=" + prepayId);

            // 二次签名，调起支付需要重新签名
            StringBuilder stringBuilder = new StringBuilder();
            for (Object o : list) {
                stringBuilder.append(o).append("\n");
            }
            String signMessage = stringBuilder.toString();
            byte[] message = signMessage.getBytes();

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(PemUtil.loadPrivateKey(new FileInputStream(weChatProperties.getPrivateKeyFilePath())));
            signature.update(message);
            String packageSign = Base64.getEncoder().encodeToString(signature.sign());

            // 构造数据给微信小程序，用于调起微信支付
            JSONObject jo = new JSONObject();
            jo.put("timeStamp", timeStamp);
            jo.put("nonceStr", nonceStr);
            jo.put("package", "prepay_id=" + prepayId);
            jo.put("signType", "RSA");
            jo.put("paySign", packageSign);

            return jo;
        }
        return jsonObject;
    }

    /**
     * 申请退款
     * @param outTradeNo 订单号
     * @param outRefundNo 退款单号
     * @param refund 退款金额
     * @param total 订单总金额
     * @return 退款结果
     * @throws Exception 异常
     */
    public String refund(String outTradeNo, String outRefundNo, BigDecimal refund, BigDecimal total) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("out_trade_no", outTradeNo);
        jsonObject.put("out_refund_no", outRefundNo);

        JSONObject amount = new JSONObject();
        amount.put("refund", refund.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue());
        amount.put("total", total.multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP).intValue());
        amount.put("currency", "CNY");

        jsonObject.put("amount", amount);
        jsonObject.put("notify_url", weChatProperties.getRefundNotifyUrl());

        String body = jsonObject.toJSONString();

        //调用申请退款接口
        return post(REFUNDS, body);
    }
}
