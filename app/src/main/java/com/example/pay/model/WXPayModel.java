package com.example.pay.model;

import com.example.pay.http.HttpConstants;
import com.example.pay.http.XmlHttpRequest;
import com.example.pay.listener.BaseLoadListener;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

/**
 * Created by mzp on 2016/8/5.
 */
public class WXPayModel
{
    public static void weixinOrderHttp(SortedMap<String,String> parameters, BaseLoadListener loadListener)
    {
        Map<String, String> payParams = conversionSortTOHash(parameters);
        new XmlHttpRequest().doHttpPost(HttpConstants.WX_ORDER_API, payParams, loadListener);
    }

    private static Map<String, String> conversionSortTOHash(SortedMap<String,String> parameters)
    {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> entry : parameters.entrySet())
        {
            map.put(entry.getKey(), entry.getValue());
        }

        return map;
    }
}
