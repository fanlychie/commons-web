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
        String ip = null;
        if ((ip = getRequestHeader(request, "X-Real-IP")) != null) {
            return ip;
        }
        if ((ip = getRequestHeader(request, "X-Forwarded-For")) != null) {
            int index = ip.indexOf(",");
            return index == -1 ? ip : ip.substring(0, index);
        }
        if ((ip = getRequestHeader(request, "Proxy-Client-IP")) != null) {
            return ip;
        }
        if ((ip = getRequestHeader(request, "WL-Proxy-Client-IP")) != null) {
            return ip;
        }
        if ((ip = request.getRemoteAddr()) != null && !ip.equals("0:0:0:0:0:0:0:1")) {
            return ip;
        }
        return "127.0.0.1";
    }

    // 私有化构造器
    private ClientInformation() {

    }

    /**
     * 获取请求头参数的值
     *
     * @param request HttpServletRequest
     * @param name    参数名称
     * @return 若值为 null 或忽略大小写与 unknown 字符相同, 则返回 null
     */
    private static String getRequestHeader(HttpServletRequest request, String name) {
        String value = request.getHeader(name);
        return value == null || value.equalsIgnoreCase("unknown") ? null : value;
    }

}