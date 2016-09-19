package com.example.pay.View;

/**
 * Created by mzp on 2016/9/18.
 */
public interface WXPayView
{
    String getAppid();

    String getMchId();

    String getNonceStr();

    String getBody();

    String getOutTradeNo();

    String getTotalFee();

    String getSpbillCreateIp();

    String getNOtifyUrl();

    String getTradeType();

    String getSignKey();
}
