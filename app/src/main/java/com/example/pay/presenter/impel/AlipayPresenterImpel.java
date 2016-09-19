package com.example.pay.presenter.impel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.pay.View.AliPayView;
import com.example.pay.presenter.AlipayPresenter;
import com.example.pay.utils.ZFB.PayResult;
import com.example.pay.utils.ZFB.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by mzp on 2016/8/9.
 */
public class AlipayPresenterImpel implements AlipayPresenter
{
    private static final int SDK_PAY_FLAG = 1;

    private Context context;
    private AliPayView aliPayView;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler()
    {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case SDK_PAY_FLAG:
                {
                    PayResult payResult = new PayResult((String) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档

                    if (TextUtils.equals(resultStatus, "9000"))
                    {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                    } else
                    {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000"))
                        {
                            Toast.makeText(context, "支付提交中", Toast.LENGTH_SHORT).show();
                        } else
                        {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }
    };

    public AlipayPresenterImpel(Context context, AliPayView aliPayView)
    {
        this.context = context;
        this.aliPayView = aliPayView;
    }

    @Override
    public void alipayRequest()
    {
        String orderInfo = aliPayView.getOrderInfo();

        /**
         * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
         */
        String sign = sign(orderInfo);
        try
        {
            /**
             * 仅需对sign 做URL编码
             */
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

        Runnable payRunnable = new Runnable()
        {

            @Override
            public void run()
            {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity)context);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    private String sign(String content)
    {
        return SignUtils.sign(content, aliPayView.getRSAPrivate());
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    private String getSignType()
    {
        return "sign_type=\"RSA\"";
    }
}
