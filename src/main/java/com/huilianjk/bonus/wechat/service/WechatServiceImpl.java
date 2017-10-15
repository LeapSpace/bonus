package com.huilianjk.bonus.wechat.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huilianjk.bonus.handler.exceptions.DefException;
import com.huilianjk.bonus.util.httpclient.HttpClient4Util;
import com.huilianjk.bonus.util.httpclient.HttpResp;
import com.huilianjk.bonus.wechat.pojo.WechatUser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by space on 15/12/5.
 */
@Service
public class WechatServiceImpl implements WechatService {
    private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    @Value("wechat.appId")
    private String appId;
    @Value("wechat.appSecret")
    private String appSecret;

    private WechatUser getUserInfoByOauthToken(String accessToken, String openId) {
        String api = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openId + "&lang=zh_CN";
        WechatUser wechatUser = new WechatUser();
        try {
            HttpResp resp = HttpClient4Util.getDefault().doGet(api);
            JSONObject jsonObject = JSON.parseObject(resp.getText("UTF-8"));
            wechatUser.setOpenId(openId);
            if (StringUtils.isBlank(jsonObject.getString("errcode"))) {
                String nickname = jsonObject.getString("nickname");
                String headImg = jsonObject.getString("headimgurl");
                wechatUser.setOpenId(openId);
                wechatUser.setNickname(nickname);
                wechatUser.setHeadImg(headImg);
            } else {
                logger.info("wx request:", jsonObject);
                throw new DefException(jsonObject.getString("errcode"));
            }
        } catch (IOException ie) {
            logger.error("get wechat openId error", ie);
            throw new DefException("get wechat openId error");
        }
        return wechatUser;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public WechatUser getOpenIdFromWebOauth(String code) {
        String api = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
        WechatUser wechatUser = new WechatUser();
        try {
            HttpResp resp = HttpClient4Util.getDefault().doGet(api);
            JSONObject jsonObject = JSON.parseObject(resp.getText("UTF-8"));
            if (StringUtils.isBlank(jsonObject.getString("errcode"))) {
                String openId = jsonObject.getString("openid");
                logger.debug(jsonObject.getString("scope"));
                if ("snsapi_userinfo".equals(jsonObject.getString("scope"))) {
                    String accessToken = jsonObject.getString("access_token");
                    wechatUser = getUserInfoByOauthToken(accessToken, openId);
                }
                wechatUser.setOpenId(openId);
                if (jsonObject.get("unionid") != null) {
                    wechatUser.setUnionId(jsonObject.getString("unionid"));
                }
            } else {
                logger.debug("wx request:", jsonObject);
                throw new DefException(jsonObject.getString("errcode"));
            }
        } catch (IOException ie) {
            logger.error("get wechat openId error", ie);
            throw new DefException("get OpenId error");
        }
        return wechatUser;
    }
}
