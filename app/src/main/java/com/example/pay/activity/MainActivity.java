package com.example.pay.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.pay.R;

/**
 * Created by mzp on 2016/9/18.
 */
public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void wxPayApply(View view)
    {
        startActivity(new Intent(this, WXPayActivity.class));
    }

    public void aliPayApply(View view)
    {
        startActivity(new Intent(this, AliPayActivity.class));
    }
}
