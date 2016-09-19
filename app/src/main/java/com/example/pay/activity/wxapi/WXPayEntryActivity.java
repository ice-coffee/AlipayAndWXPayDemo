package com.example.pay.activity.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import com.example.pay.R;
import com.example.pay.common.FoContent;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by mzp on 2016/8/9.
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);

        api = WXAPIFactory.createWXAPI(this, FoContent.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    public void payResultBack(View view)
    {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req)
    {
    }

    @Override
    public void onResp(BaseResp resp)
    {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            int errCode = resp.errCode;
            if (0 != errCode)
            {
                if (-1 == errCode)
                {
                    Toast.makeText(this, getString(R.string.pay_error_1), Toast.LENGTH_SHORT).show();
                }
                else if (-2 == errCode)
                {
                    Toast.makeText(this, getString(R.string.pay_error_2), Toast.LENGTH_SHORT).show();
                }

                finish();
            }
            else
            {
                // 发送本地广播
            }
        }
    }
}
