package com.example.pay.listener;


/**
 * Created by mzp on 2016/8/5.
 */
public interface BaseLoadListener
{
    void onSuccess(Object data);

    void onError(String errorMsg);
}
