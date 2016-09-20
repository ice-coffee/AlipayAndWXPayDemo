## Android端 微信支付与支付宝支付接入

###1. 添加jar包
`alipaySdk-20160516.jar` 和 `libammsdk.jar`
>特别注意：`友盟+`中的微信分享jar包：SocialSDK_WeiXin_1.jar,SocialSDK_WeiXin_2.jar和从微信开放平台下载的jar包：libammsdk.jar有冲突，因为他们都包含有微信登录/分享的功能。友盟自3.3.8版本已经兼容微信支付sdk，SocialSDK_WeiXin_X.jar里包含了微信支付需要的类可以直接使用支付的api

###2. 微信支付集成开发步骤
- 在账号中心进行开发者资质认证, 这是使用微信支付以及微信登录等高级功能的必要步骤, 但是需要1年300的费用.
- 在管理中心创建应用获取APP_ID, 这个一般在7个工作日内.
- 在应用详情页点击申请支付能力, 在接下来的步骤中你需要进行"资料审核", "账户验证", "协议签署"三步, 这个也需要几个工作日的时间.
- 通过查看微信支付流程图并根据[Android接入指南](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=1417751808&token=&lang=zh_CN)编写代码
![](https://pay.weixin.qq.com/wiki/doc/api/img/chapter8_3_1.png)
- 微信统一下单接口

>特别注意:
  - 统一下单的url地址和需要传递的参数可以在微信支付开发文档中找到;
  - 微信的统一下单接口接收的参数是一段xml代码;
  - 把所有需要传递的参数组合成一段xml代码，`要有换行符`，不然微信服务端无法识别;
  - 在调用签名算法时需要用的`KEY`, 它的生成方式可以参考![签名算法](https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3);
  - 统一下单接口的返回值是一段xml代码, 所以你需要对它进行解析.
```
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
```
```
/**
     * 微信支付签名算法sign
     *
     * @param parameters
     * @return
     */
    @SuppressWarnings("unchecked")
    public static String createSign(SortedMap<String, String> parameters)
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
        sb.append("key=" + KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), "UTF-8").toUpperCase();
        return sign;
    }
```
- 微信支付接口
支付接口参看微信支付提供的API文档即可.
