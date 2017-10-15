package com.huilianjk.bonus.wechat.service;


import com.huilianjk.bonus.wechat.pojo.WechatUser;

/**
 * Created by space on 15/12/5.
 */
public interface WechatService {

    /**
     * e.g. https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
     */
    String accessTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";

    /**
     * e.g. https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
     */
    String jsApiTicketApi = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi";

    WechatUser getOpenIdFromWebOauth(String code);

    String getAppId();
}
