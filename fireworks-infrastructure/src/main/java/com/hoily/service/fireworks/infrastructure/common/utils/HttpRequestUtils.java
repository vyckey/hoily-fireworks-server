package com.hoily.service.fireworks.infrastructure.common.utils;

import jakarta.servlet.http.HttpServletRequest;

/**
 * description is here
 *
 * @author vyckey
 * 2023/3/23 11:57
 */
public abstract class HttpRequestUtils {
    public static String getIpAddress(HttpServletRequest request) {
        final String unknown = "unknown";
        String ip = request.getHeader("X-Forwarded-For");

        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
