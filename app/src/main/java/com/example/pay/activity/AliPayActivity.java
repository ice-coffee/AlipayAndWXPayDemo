package com.example.pay.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.pay.R;
import com.example.pay.View.AliPayView;
import com.example.pay.presenter.AlipayPresenter;
import com.example.pay.presenter.impel.AlipayPresenterImpel;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by mzp on 2016/9/18.
 */
public class AliPayActivity extends Activity implements AliPayView
{
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "";

    @Bind(R.id.partner)
    EditText partner;
    @Bind(R.id.seller_id)
    EditText sellerId;
    @Bind(R.id.out_trade_no)
    EditText outTradeNo;
    @Bind(R.id.subject)
    EditText subject;
    @Bind(R.id.body)
    EditText body;
    @Bind(R.id.total_fee)
    EditText totalFee;
    @Bind(R.id.notify_url)
    EditText notifyUrl;
    @Bind(R.id.rsa_private)
    EditText rsaPrivate;

    private AlipayPresenter alipayPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alipay);
        ButterKnife.bind(this);

        alipayPresenter = new AlipayPresenterImpel(this, this);

        partner.setText("");
        sellerId.setText("");
        outTradeNo.setText(getCurrentTime());
        subject.setText("");
        body.setText("");
        totalFee.setText("1");
        notifyUrl.setText("http://nofify.msp.hk/notify.htm");
        rsaPrivate.setText(RSA_PRIVATE);
    }

    public void aliPayCommit(View view)
    {
        alipayPresenter.alipayRequest();
    }

    @Override
    public String getOrderInfo()
    {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + partner.getText().toString().trim() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + sellerId.getText().toString().trim() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + outTradeNo.getText().toString().trim() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject.getText().toString().trim() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body.getText().toString().trim() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + totalFee.getText().toString().trim() + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notifyUrl.getText().toString().trim() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        //orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        //orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    @Override
    public String getRSAPrivate()
    {
        return rsaPrivate.getText().toString().trim();
    }

    private String getCurrentTime()
    {
        return String.valueOf(System.currentTimeMillis());
    }
}
