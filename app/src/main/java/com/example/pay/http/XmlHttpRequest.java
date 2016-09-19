package com.example.pay.http;

import android.os.AsyncTask;

import com.example.pay.listener.BaseLoadListener;
import com.example.pay.presenter.WXPayPresenter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Objects;

/**
 * Created by mzp on 2016/8/5.
 */
public class XmlHttpRequest
{
    private BaseLoadListener loadListener;

    public void doHttpPost(String urlStr, final Map<String, String> map, BaseLoadListener loadListener)
    {
        this.loadListener = loadListener;

        String stringXml = mapToXmlString(map);
        new XmlAsyncTask().execute(urlStr, stringXml);
    }

    private class XmlAsyncTask extends AsyncTask<String, Objects, String>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params)
        {
            try
            {
                URL url = new URL(params[0]);

                byte[] xmlbyte = params[1].getBytes("UTF-8");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setDoOutput(true);// 允许输出
                conn.setDoInput(true);
                conn.setUseCaches(false);// 不使用缓存
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");// 维持长连接
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Length",
                        String.valueOf(xmlbyte.length));
                conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
                conn.setRequestProperty("X-ClientType", "2");//发送自定义的头信息

                conn.getOutputStream().write(xmlbyte);
                conn.getOutputStream().flush();
                conn.getOutputStream().close();


                if (conn.getResponseCode() != 200)
                {
                    loadListener.onError("loadListener.onError(\"网络错误,请确认网络或稍后再试\");");
                }
                else
                {
                    InputStream is = conn.getInputStream();// 获取返回数据

                    loadListener.onSuccess(is);
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s)
        {
            super.onPostExecute(s);
        }
    }

    private static String mapToXmlString(Map<String, String> map)
    {
        StringBuilder xml = new StringBuilder();
        xml.append("<xml>"+"\n");
        xml.append("<appid>"+map.get(WXPayPresenter.APPID)+"</appid>"+"\n");
        xml.append("<mch_id>"+map.get(WXPayPresenter.MCH_ID)+"</mch_id>"+"\n");
        xml.append("<nonce_str>"+map.get(WXPayPresenter.NONCE_STR)+"</nonce_str>"+"\n");
        xml.append("<body>"+map.get(WXPayPresenter.BODY)+"</body>"+"\n");
        xml.append("<out_trade_no>"+map.get(WXPayPresenter.OUT_TRADE_NO)+"</out_trade_no>"+"\n");
        xml.append("<total_fee>"+map.get(WXPayPresenter.TOTAL_FEE)+"</total_fee>"+"\n");
        xml.append("<spbill_create_ip>"+map.get(WXPayPresenter.SPBILL_CREATE_IP)+"</spbill_create_ip>"+"\n");
        xml.append("<notify_url>"+map.get(WXPayPresenter.NOTIFY_URL)+"</notify_url>"+"\n");
        xml.append("<trade_type>"+map.get(WXPayPresenter.TRADE_TYPE)+"</trade_type>"+"\n");
        xml.append("<sign>"+map.get(WXPayPresenter.SIGNN)+"</sign>"+"\n");
        xml.append("</xml>");

        return xml.toString();
    }
}
