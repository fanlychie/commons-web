package org.fanlychie.commons.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户端信息
 * Created by fanlychie on 2017/2/16.
 */
public final class ClientInformation {

    /**
     * 获取客户端 IP 地址
     *
     * @param request HttpServletRequest 对象
     * @return 返回客户端 IP 地址
     */
    public static String getIPAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (isFound(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (isFound(ip)) {
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (isFound(ip)) {
            return ip;
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (isFound(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    // 私有化构造器
    private ClientInformation() {

    }

    // 查找 IP
    private static boolean isFound(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

}