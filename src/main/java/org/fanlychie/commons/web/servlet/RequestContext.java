package org.fanlychie.commons.web.servlet;

import org.fanlychie.commons.web.exception.RuntimeCastException;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * 请求上下文环境
 * Created by fanlychie on 2017/2/16.
 */
public final class RequestContext {

    /**
     * KEY
     */
    private static final String REQUEST = "__REQUEST__";

    /**
     * 设置本次请求上下文环境变量
     *
     * @param request HttpServletRequest 对象
     */
    public static void setLocal(ServletRequest request) {
        if (!(request instanceof HttpServletRequest)) {
            throw new IllegalArgumentException("request is not a HttpServletRequest: " + request);
        }
        LocalContext.put(REQUEST, request);
    }

    /**
     * 获取本次请求的 HttpServletRequest 对象
     *
     * @return 返回 HttpServletRequest 对象
     */
    public static HttpServletRequest getRequest() {
        return LocalContext.get(REQUEST);
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
     * 获取请求参数值
     *
     * @param name 参数名称
     * @return 返回请求参数的值
     */
    public static String getRequestParam(String name) {
        return getRequest().getParameter(name);
    }

    /**
     * 设置会话属性值
     *
     * @param name  属性名称
     * @param value 属性的值
     */
    public static void setSessionAttribute(String name, Object value) {
        getSession().setAttribute(name, value);
    }

    /**
     * 获取会话属性的值
     *
     * @param name 属性名称
     * @param <T>  期望类型
     * @return 返回会话属性的值
     */
    public static <T> T getSessionAttribute(String name) {
        return (T) getSession().getAttribute(name);
    }

    /**
     * 获取输入流对象
     *
     * @return 返回请求中的输入流对象
     */
    public static ServletInputStream getInputStream() {
        try {
            return getRequest().getInputStream();
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 获取读对象
     *
     * @return 返回请求中读对象
     */
    public static BufferedReader getReader() {
        try {
            return getRequest().getReader();
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 转发
     *
     * @param path 路径
     */
    public static void forward(String path) {
        HttpServletResponse response = ResponseContext.getResponse();
        if (response == null) {
            throw new NullPointerException();
        }
        try {
            getRequest().getRequestDispatcher(path).forward(getRequest(), response);
        } catch (Exception e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 获取客户端 IP 地址
     *
     * @return 返回客户端 IP 地址
     */
    public static String getClientIPAddress() {
        return ClientInformation.getIPAddress(getRequest());
    }

    /**
     * 获取服务器路径地址
     *
     * @return 返回服务器路径地址
     */
    public static String getServerPath() {
        HttpServletRequest request = getRequest();
        StringBuilder builder = new StringBuilder();
        builder.append(request.getScheme()).append("://").append(request.getServerName());
        int port = request.getServerPort();
        if (port != 80) {
            builder.append(":").append(port);
        }
        builder.append(request.getContextPath()).append("/");
        return builder.toString();
    }

    // 私有化构造器
    private RequestContext() {

    }

}