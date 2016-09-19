package com.example.pay.presenter;

import java.util.Map;

/**
 * Created by mzp on 2016/8/5.
 */
public interface WXPayPresenter
{
    /*order start*/
    String APPID = "appid";
    String MCH_ID = "mch_id";
    String NONCE_STR = "nonce_str";
    String BODY = "body";
    String OUT_TRADE_NO = "out_trade_no";
    String TOTAL_FEE = "total_fee";
    String SPBILL_CREATE_IP = "spbill_create_ip";
    String NOTIFY_URL = "notify_url";
    String TRADE_TYPE = "trade_type";
    String SIGNN = "sign";
    /*order end*/

    /*pay start*/
    String PAY_APPID = "appid";
    String PAY_PARTNERID = "partnerid";
    String PAY_PREPAY_ID = "prepayid";
    String PAY_PACKAGE = "package";
    String PAY_NONCESTR = "noncestr";
    String PAY_TIMESTAMP = "timestamp";
    String PAY_SIGN = "sign";
    /*pay end*/

    void weixinRequest();

    void weixinPay(Map<String, String> payParams);
}
