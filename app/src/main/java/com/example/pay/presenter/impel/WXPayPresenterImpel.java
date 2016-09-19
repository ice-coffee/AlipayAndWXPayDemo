package com.example.pay.presenter.impel;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import com.example.pay.View.WXPayView;
import com.example.pay.activity.WXPayActivity;
import com.example.pay.common.FoContent;
import com.example.pay.listener.BaseLoadListener;
import com.example.pay.model.WXPayModel;
import com.example.pay.presenter.WXPayPresenter;
import com.example.pay.utils.DomBookParser;
import com.example.pay.utils.MD5Utils;
import com.example.pay.utils.NetUtils;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by mzp on 2016/8/5.
 */
public class WXPayPresenterImpel implements WXPayPresenter
{
    private Context context;

    public WXPayView wxPayView;

    private IWXAPI api;

    public WXPayPresenterImpel(Context context, WXPayView wxPayView)
    {
        api = WXAPIFactory.createWXAPI(context, FoContent.WX_APP_ID);
        //将应用的appid注册到微信
        api.registerApp(FoContent.WX_APP_ID);
        this.context = context;
        this.wxPayView = wxPayView;
    }


    @Override
    public void weixinRequest()
    {
        SortedMap<String, String> parameters = new TreeMap<String, String>();
        //应用ID
        parameters.put(WXPayPresenter.APPID, wxPayView.getAppid());
        //商户号, 这个需要保存在服务端，经接口获取
        parameters.put(WXPayPresenter.MCH_ID, wxPayView.getMchId());
        //随机字符串
        parameters.put(WXPayPresenter.NONCE_STR, wxPayView.getNonceStr());
        //商品描述
        parameters.put(WXPayPresenter.BODY, wxPayView.getBody());
        //商户订单号
        parameters.put(WXPayPresenter.OUT_TRADE_NO, wxPayView.getOutTradeNo());
        //总金额
        parameters.put(WXPayPresenter.TOTAL_FEE, wxPayView.getTotalFee());
        //终端IP
        parameters.put(WXPayPresenter.SPBILL_CREATE_IP, wxPayView.getSpbillCreateIp());
        //通知地址
        parameters.put(WXPayPresenter.NOTIFY_URL, wxPayView.getNOtifyUrl());
        //交易类型
        parameters.put(WXPayPresenter.TRADE_TYPE, wxPayView.getTradeType());

        //签名
        parameters.put(WXPayPresenter.SIGNN, createSign(parameters));

        WXPayModel.weixinOrderHttp(parameters, loadListener);
    }

    @Override
    public void weixinPay(Map<String, String> orderResultParams)
    {
        SortedMap<String, String> payParams = new TreeMap<>();
        payParams.put(WXPayPresenter.PAY_APPID, wxPayView.getAppid());
        payParams.put(WXPayPresenter.PAY_PARTNERID, wxPayView.getMchId());
        payParams.put(WXPayPresenter.PAY_PREPAY_ID, orderResultParams.get(WXPayPresenter.PAY_PREPAY_ID));
        payParams.put(WXPayPresenter.PAY_PACKAGE, "Sign=WXPay"); //固定值
        payParams.put(WXPayPresenter.PAY_NONCESTR, "5K8264ILTKCH16CQ2502SI8ZNMTM67VS");
        payParams.put(WXPayPresenter.PAY_TIMESTAMP, getCurrentTime());

        PayReq req = new PayReq();
        req.appId			= wxPayView.getAppid();
        req.partnerId		= wxPayView.getMchId();
        req.prepayId		= orderResultParams.get(WXPayPresenter.PAY_PREPAY_ID);
        req.packageValue	= "Sign=WXPay";
        req.nonceStr		= "5K8264ILTKCH16CQ2502SI8ZNMTM67VS";
        req.timeStamp		= getCurrentTime();
        req.sign			= createSign(payParams);

        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    /**
     * 微信下单
     */
    private BaseLoadListener loadListener = new BaseLoadListener()
    {
        @Override
        public void onSuccess(Object data)
        {
            try {
                DomBookParser parser = new DomBookParser();  //创建SaxBookParser实例
                Map<String, String> orderResultParams = parser.parse((InputStream)data);
                weixinPay(orderResultParams);
            } catch (Exception e) {
                Log.e("aaa", e.getMessage());
            }
        }

        @Override
        public void onError(String errorMsg)
        {
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 微信支付签名算法sign
     *
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public String createSign(SortedMap<String, String> parameters)
    {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();//所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext())
        {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k))
            {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + wxPayView.getSignKey());
        String sign = MD5Utils.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }

    /**
     * 客户端IP是可以从服务端获得的
     * 具体怎么获得我也不清楚, 我们PHP的小伙伴发给我的ip就可以用, 正常支付
     * @return
     */
    private String getWxIpAddress()
    {
        String ipAddress = null;

        switch (NetUtils.getNetworkType(context))
        {
            case NetUtils.NETTYPE_WIFI:
                ipAddress = getWifiIpAddress();
                break;

            case NetUtils.NETTYPE_CMNET:
                ipAddress = getLocalIpAddress();
                break;
        }

        return ipAddress;
    }

    /**
     * 获取wifi状态下的ip地址
     * @return
     */
    private String getWifiIpAddress()
    {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled())
        {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return intToIp(ipAddress);
    }

    private String intToIp(int i)
    {

        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * 获取移动网络状态下的IP地址
     * @return
     */
    public String getLocalIpAddress()
    {
        try
        {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); )
            {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); )
                {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress())
                    {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex)
        {
            Log.e("IpAddress", ex.toString());
        }
        return null;
    }

    private String getCurrentTime()
    {
        return String.valueOf(System.currentTimeMillis());
    }
}
