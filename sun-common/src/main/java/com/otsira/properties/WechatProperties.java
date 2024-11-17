package com.otsira.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 微信登录及微信支付相关的配置项
 * @create: 2024/11/16 12:08
 */
@Component
@ConfigurationProperties(prefix = "sun.wechat")
@Data
public class WechatProperties {
    // 小程序的 appid
    private String appid;

    // 小程序的秘钥
    private String secret;

    // 商户号
    private String mchid;

    // 商户API证书的证书序列号
    private String mchSerialNo;

    // 商户私钥文件路径
    private String privateKeyFilePath;

    // 证书解密的密钥
    private String apiV3Key;

    // 平台证书
    private String weChatPayCertFilePath;

    // 支付成功回调地址
    private String notifyUrl;

    // 退款成功回调地址
    private String refundNotifyUrl;
}
