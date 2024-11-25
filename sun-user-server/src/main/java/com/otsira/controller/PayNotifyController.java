package com.otsira.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.otsira.properties.WechatProperties;
import com.otsira.service.OrderService;
import com.wechat.pay.contrib.apache.httpclient.util.AesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 微信支付相关功能的 controller 层, 用于接收微信方发送的支付结果
 * @create: 2024/11/24 20:28
 */
@RestController
@RequestMapping("/user/notify")
@Slf4j
public class PayNotifyController {
    private OrderService orderService;
    private WechatProperties wechatProperties;

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setWechatProperties(WechatProperties wechatProperties) {
        this.wechatProperties = wechatProperties;
    }

    /**
     * 支付成功回调
     * @param request 请求
     * @param response 响应
     * @throws Exception 异常
     */
    @RequestMapping("/paySuccess")
    public void paySuccessNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 1.读取微信第三方发来的响应数据
        String body = readData(request);
        log.info("支付成功回调：{}", body);

        // 2.微信第三方响应体数据解密
        String plainText = decryptData(body);
        log.info("解密后的文本：{}", plainText);

        JSONObject jsonObject = JSON.parseObject(plainText);

        // 商户平台订单号
        String outTradeNo = jsonObject.getString("out_trade_no");
        // 微信支付交易号
        String transactionId = jsonObject.getString("transaction_id");

        log.info("商户平台订单号：{}", outTradeNo);
        log.info("微信支付交易号：{}", transactionId);

        // 3.数据库业务处理: 修改订单状态、来单提醒
        int update = orderService.paySuccess(outTradeNo);
        if (update <= 0) {
            log.error("订单状态修改失败");
        }

        // 4.给微信第三方响应
        responseToWechat(response);
    }

    /**
     * 读取数据
     * @param request 请求
     * @return 数据
     * @throws Exception 异常
     */
    private String readData(HttpServletRequest request) throws Exception {
        BufferedReader reader = request.getReader();
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (!result.isEmpty()) {
                result.append("\n");
            }
            result.append(line);
        }
        return result.toString();
    }

    /**
     * 数据解密
     * @param body 数据
     * @return 解密后的数据
     * @throws Exception 异常
     */
    private String decryptData(String body) throws Exception {
        JSONObject resultObject = JSON.parseObject(body);
        JSONObject resource = resultObject.getJSONObject("resource");

        String associatedData = resource.getString("associated_data");
        String nonce = resource.getString("nonce");
        String ciphertext = resource.getString("ciphertext");

        // ApiV3Key - 证书解密的密钥
        AesUtil aesUtil = new AesUtil(wechatProperties.getApiV3Key().getBytes(StandardCharsets.UTF_8));
        return aesUtil.decryptToString(associatedData.getBytes(StandardCharsets.UTF_8),
                nonce.getBytes(StandardCharsets.UTF_8),
                ciphertext);
    }

    /**
     * 给微信响应
     * @param response 响应
     * @throws Exception 异常
     */
    private void responseToWechat(HttpServletResponse response) throws Exception {
        response.setStatus(200);
        HashMap<Object, Object> map = new HashMap<>();
        map.put("code", "SUCCESS");
        map.put("message", "SUCCESS");
        response.setHeader("Content-type", ContentType.APPLICATION_JSON.toString());
        response.getOutputStream().write(JSONUtils.toJSONString(map).getBytes(StandardCharsets.UTF_8));
        response.flushBuffer();
    }
}
