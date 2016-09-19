package com.example.pay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.pay.R;
import com.example.pay.View.WXPayView;
import com.example.pay.presenter.WXPayPresenter;
import com.example.pay.presenter.impel.WXPayPresenterImpel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzp on 2016/9/18.
 */
public class WXPayActivity extends Activity implements WXPayView
{
    private static final String KEY = "";

    @Bind(R.id.appid)
    EditText appid;
    @Bind(R.id.mch_id)
    EditText mchId;
    @Bind(R.id.nonce_str)
    EditText nonceStr;
    @Bind(R.id.body)
    EditText body;
    @Bind(R.id.out_trade_no)
    EditText outTradeNo;
    @Bind(R.id.total_fee)
    EditText totalFee;
    @Bind(R.id.spbill_create_ip)
    EditText spbillCreateIp;
    @Bind(R.id.notify_url)
    EditText notifyUrl;
    @Bind(R.id.trade_type)
    EditText tradeType;
    @Bind(R.id.sign_key)
    EditText signKey;

    private WXPayPresenter wxPayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxpay);
        ButterKnife.bind(this);

        wxPayPresenter = new WXPayPresenterImpel(this, this);

        appid.setText("");
        mchId.setText("");
        nonceStr.setText("");
        body.setText("");
        outTradeNo.setText(getCurrentTime());
        totalFee.setText("1");
        spbillCreateIp.setText("45.32.54.153");
        notifyUrl.setText("http://www.weixin.qq.com/wxpay/pay.php");
        tradeType.setText("APP");
        signKey.setText(KEY);
    }

    public void wxPayCommit(View view)
    {
        wxPayPresenter.weixinRequest();
    }

    private String getCurrentTime()
    {
        return String.valueOf(System.currentTimeMillis());
    }

    @Override
    public String getAppid()
    {
        return appid.getText().toString().trim();
    }

    @Override
    public String getMchId()
    {
        return mchId.getText().toString().trim();
    }

    @Override
    public String getNonceStr()
    {
        return nonceStr.getText().toString().trim();
    }

    @Override
    public String getBody()
    {
        return body.getText().toString().trim();
    }

    @Override
    public String getOutTradeNo()
    {
        return outTradeNo.getText().toString().trim();
    }

    @Override
    public String getTotalFee()
    {
        return totalFee.getText().toString().trim();
    }

    @Override
    public String getSpbillCreateIp()
    {
        return spbillCreateIp.getText().toString().trim();
    }

    @Override
    public String getNOtifyUrl()
    {
        return notifyUrl.getText().toString().trim();
    }

    @Override
    public String getTradeType()
    {
        return tradeType.getText().toString().trim();
    }

    @Override
    public String getSignKey()
    {
        return signKey.getText().toString().trim();
    }
}
