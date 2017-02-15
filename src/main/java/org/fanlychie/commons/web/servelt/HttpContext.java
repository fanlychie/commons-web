package org.fanlychie.commons.web.servelt;

import org.fanlychie.commons.web.exception.RuntimeCastException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * HTTP 请求上下文
 * Created by fanlychie on 2017/2/15.
 */
public final class HttpContext {

    /**
     * HttpServletRequest 键值
     */
    private static final String HTTP_SERVLET_REQUEST = "HTTP_SERVLET_REQUEST";

    /**
     * HttpServletResponse 键值
     */
    private static final String HTTP_SERVLET_RESPONSE = "HTTP_SERVLET_RESPONSE";

    /**
     * 线程本地变量表
     */
    private static final ThreadLocal<Map<Object, Object>> THREAD_LOCAL = new ThreadLocal<Map<Object, Object>>() {
        @Override
        protected Map<Object, Object> initialValue() {
            return new ConcurrentHashMap<>();
        }
    };

    /**
     * 私有化构造器
     */
    private HttpContext() {

    }

    /**
     * 初始化本次请求上下文环境变量
     *
     * @param request  HttpServletRequest 对象
     * @param response HttpServletResponse 对象
     */
    public static void init(HttpServletRequest request, HttpServletResponse response) {
        put(HTTP_SERVLET_REQUEST, request);
        put(HTTP_SERVLET_RESPONSE, response);
    }

    /**
     * 获取本次请求的 HttpServletRequest 对象
     *
     * @return 返回 HttpServletRequest 对象
     */
    public static HttpServletRequest getRequest() {
        return (HttpServletRequest) THREAD_LOCAL.get().get(HTTP_SERVLET_REQUEST);
    }

    /**
     * 获取本次请求的 HttpServletResponse 对象
     *
     * @return 返回 HttpServletResponse 对象
     */
    public static HttpServletResponse getResponse() {
        return (HttpServletResponse) THREAD_LOCAL.get().get(HTTP_SERVLET_RESPONSE);
    }

    /**
     * 获取 HttpSession 对象
     *
     * @return 返回 HttpSession 对象
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 将键值对放入本次请求本地线程变量表中
     *
     * @param name  名称
     * @param value 值
     */
    public static void put(Object name, Object value) {
        THREAD_LOCAL.get().put(name, value);
    }

    /**
     * 获取本次请求本地线程变量表中的键关联的值
     *
     * @param name 名称
     * @param <T>  期望类型
     * @return 返回键关联的值
     */
    public static <T> T get(Object name) {
        return (T) THREAD_LOCAL.get().get(name);
    }

    /**
     * 获取请求的客户端 IP 地址
     *
     * @return 返回客户端 IP 地址
     */
    public static String getIPAddress() {
        return getIPAddress(getRequest());
    }

    /**
     * 获取请求的客户端 IP 地址
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

    /**
     * 向客户端写出响应消息
     *
     * @param message 消息内容
     */
    public static void writeResponseMessage(String message) {
        writeResponseMessage(getResponse(), message);
    }

    /**
     * 向客户端写出响应消息
     *
     * @param response HttpServletResponse 对象
     * @param message  消息内容
     */
    public static void writeResponseMessage(HttpServletResponse response, String message) {
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html;charset=utf-8");
        try {
            response.getWriter().write(message);
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    private static boolean isFound(String ip) {
        return ip != null && ip.length() > 0 && !"unknown".equalsIgnoreCase(ip);
    }

}