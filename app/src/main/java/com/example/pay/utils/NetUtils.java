package com.example.pay.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

/**
 * Created by mzp on 2016/8/5.
 */
public class NetUtils
{
    /**
     * 获取当前网络类型
     *
     * @return 0：没有网络   1：WIFI网络   2：3G网络
     */

    public static final int NETTYPE_WIFI = 0x01;
    public static final int NETTYPE_CMNET = 0x02;

    public static int getNetworkType(Context context)
    {
        int netType = 0;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null)
        {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_MOBILE)
        {
            String extraInfo = networkInfo.getExtraInfo();
            if (!TextUtils.isEmpty(extraInfo))
            {
                netType = NETTYPE_CMNET;
            }
        } else if (nType == ConnectivityManager.TYPE_WIFI)
        {
            netType = NETTYPE_WIFI;
        }
        return netType;
    }
}
