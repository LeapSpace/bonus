package com.huilianjk.bonus.security;

import com.huilianjk.bonus.util.common.RequestUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by space on 2017/10/15.
 */
@Component
public class SimpleAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    private static Logger logger = LoggerFactory.getLogger(SimpleAuthenticationEntryPoint.class);

    public SimpleAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
    }


    @Override
    protected String determineUrlToUseForThisRequest(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception){
        String requestURI = request.getRequestURI();
        if (RequestUtil.isAjaxRequest(request)) {
            try {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().print("forbidden");
                response.flushBuffer();
                response.getWriter().close();
            } catch (IOException ie) {
                logger.warn("response error");
            }
        }
        String target = "";
        if (!requestURI.startsWith("/wx")) {
            try {
                target = URLEncoder.encode(requestURI, "UTF-8");
            } catch (UnsupportedEncodingException ue) {
                logger.info("fuuuuuuuuuuuuuuck u!! 屮!!");
            }
        }
        return "/wx?target=" + target;
    }

    /**
     * Performs the redirect (or forward) to the login form URL.
     */
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        String redirectUrl = null;
        if (isUseForward()) {
            if (isForceHttps() && "http".equals(request.getScheme())) {
                // First redirect the current request to HTTPS.
                // When that request is received, the forward to the login page will be used.
                redirectUrl = buildHttpsRedirectUrlForRequest(request);
            }
            if (redirectUrl == null) {
                String loginForm = determineUrlToUseForThisRequest(request, response, authException);

                logger.debug("Server side forward to: " + loginForm);
                RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);
                dispatcher.forward(request, response);
                return;
            }
        } else {
            // redirect to login page. Use https if forceHttps true
            redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);
        }
        if (!response.isCommitted()) {
            redirectStrategy.sendRedirect(request, response, redirectUrl);
        }
    }
}
