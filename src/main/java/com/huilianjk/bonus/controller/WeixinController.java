package com.huilianjk.bonus.controller;

import com.huilianjk.bonus.handler.exceptions.DefException;
import com.huilianjk.bonus.pojo.User;
import com.huilianjk.bonus.service.UserService;
import com.huilianjk.bonus.util.common.Constant;
import com.huilianjk.bonus.util.common.RequestUtil;
import com.huilianjk.bonus.wechat.pojo.WechatUser;
import com.huilianjk.bonus.wechat.service.WechatService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by space on 15/12/4.
 */
@Controller
public class WeixinController {
    private final static Logger logger = LoggerFactory.getLogger(WeixinController.class);
    @Autowired
    private WechatService wechatService;
    @Autowired
    private UserService userService;

    protected String getAuthUrl(HttpServletRequest request, String targetUrl, String scope) {
        String state = RandomStringUtils.randomAlphanumeric(12);
        request.getSession().setAttribute(Constant.WECHAT_OAUTH_STATE, state);
        String redirectUri = RequestUtil.getDomain(request) + "/wx?target=" + targetUrl;
        logger.debug("here1:" + targetUrl);
        return "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wechatService.getAppId() + "&redirect_uri=" + urlEncode(redirectUri) + "&response_type=code&scope=" + scope + "&state=" + state + "#wechat_redirect";
    }

    private String urlEncode(String redirectUri) {
        try {
            return URLEncoder.encode(redirectUri, "utf-8");
        } catch (UnsupportedEncodingException e) {

        }
        return StringUtils.EMPTY;
    }

    protected void handleAuthCode(HttpServletRequest request) {
        WechatUser wechatUser = wechatService.getOpenIdFromWebOauth(request.getParameter("code"));
        User user = userService.getUserByOpenId(wechatUser.getOpenId());
        if (user == null) {
            userService.insert(new User(wechatUser.getOpenId(),wechatUser.getNickname()));
        }
        request.getSession().setAttribute(Constant.WECHAT_SESSION_STR, wechatUser);
    }

    @RequestMapping("/wx")
    public String index(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String targetUrl = request.getParameter("target");
        String scope = request.getParameter("scope");
        scope = (scope!=null && scope.equals("snsapi_base")) ? "snsapi_base" : "snsapi_userinfo";
        if (session.getAttribute(Constant.WECHAT_SESSION_STR) == null) {
            if (StringUtils.isBlank(request.getParameter("code"))) {
                logger.debug("here0:"+request.getRequestURI());
                return "redirect:" + getAuthUrl(request, targetUrl, scope);
            } else {
                String state = (String) request.getSession().getAttribute(Constant.WECHAT_OAUTH_STATE);
                if (StringUtils.isBlank(state) || !(state).equals(request.getParameter("state"))) {
                    logger.debug((String) request.getSession().getAttribute(Constant.WECHAT_OAUTH_STATE));
                    logger.debug(request.getParameter("state"));
                    throw new DefException("授权错误!");
                }
                logger.debug("here2");
                handleAuthCode(request);
            }
        }

        String redirectUrl = RequestUtil.getDomain(request) + targetUrl;
        return "redirect:" + redirectUrl;
    }

    @RequestMapping("/wx/clearSession.json")
    @ResponseBody
    public String clearUserSession(HttpServletRequest request) throws Exception {
        request.logout();
        return "";
    }

}
