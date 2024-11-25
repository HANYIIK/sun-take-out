package com.otsira.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @program: sun-take-out
 * @author: HANYIIK
 * @description: 后端发给前端的订单支付 VO
 * @create: 2024/11/24 17:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("后端发给前端的订单支付 VO")
public class OrderPaymentVO implements Serializable {
    @ApiModelProperty("时间戳")
    private String timeStamp;

    @ApiModelProperty("随机字符串")
    private String nonceStr;

    @ApiModelProperty("统一下单接口返回的 prepay_id 参数值")
    private String packageStr;

    @ApiModelProperty("签名算法")
    private String signType;

    @ApiModelProperty("签名")
    private String paySign;
}
