package com.huilianjk.bonus.util.common;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by space on 15/12/21.
 */
public class RequestUtil {
    private static final UrlPathHelper urlPathHelper = new UrlPathHelper();

    public static boolean isAjaxRequest(HttpServletRequest request) {
        String requestUri = urlPathHelper.getRequestUri(request);
        if (requestUri.endsWith(".json")) {
            return true;
        }
        return false;
    }

    public static String getDomain(HttpServletRequest request) {
        String protocol = request.getScheme() + "://";
        int end = request.getRequestURL().indexOf("/", protocol.length());
        if (end <= 0) {
            return request.getRequestURL().toString();
        } else {
            return request.getRequestURL().substring(0, end);
        }
    }

    public static String getRequestDomain(HttpServletRequest request) {
        String host = request.getHeader("HOST");
        if (host.indexOf(":") >= 0) {
            return host.substring(0, host.indexOf(":"));
        }
        return host;
    }

    public static String getQueryStringWithoutParams(HttpServletRequest request, String[] params) {
        if (StringUtils.isBlank(request.getQueryString())) {
            return StringUtils.EMPTY;
        }
        String[] parameters = request.getQueryString().split("&");
        StringBuilder builder = new StringBuilder();
        for (String s : parameters) {
            String param = s.split("=")[0];
            if (!Arrays.asList(params).contains(param)) {
                builder.append(s + "&");
            }
        }
        return builder.toString();
    }
}
