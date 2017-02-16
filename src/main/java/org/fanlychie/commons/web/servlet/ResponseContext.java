package org.fanlychie.commons.web.servlet;

import org.fanlychie.commons.web.exception.RuntimeCastException;
import org.springframework.http.MediaType;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * 响应上下文环境
 * Created by fanlychie on 2017/2/16.
 */
public final class ResponseContext {

    private static final String RESPONSE = "__RESPONSE__";

    /**
     * 设置本次请求上下文环境变量
     *
     * @param response HttpServletResponse 对象
     */
    public static void setLocal(ServletResponse response) {
        if (!(response instanceof HttpServletResponse)) {
            throw new IllegalArgumentException("response is not a HttpServletResponse: " + response);
        }
        LocalContext.put(RESPONSE, response);
    }

    /**
     * 获取本次请求的 HttpServletResponse 对象
     *
     * @return 返回 HttpServletResponse 对象
     */
    public static HttpServletResponse getResponse() {
        return LocalContext.get(RESPONSE);
    }

    /**
     * 获取输出流
     *
     * @return 返回响应对象中的输出流对象
     */
    public static OutputStream getOutputStream() {
        try {
            return getResponse().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 获取写对象
     *
     * @return 返回响应对象中的写对象
     */
    public static PrintWriter getWriter() {
        try {
            return getResponse().getWriter();
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 向客户端写出响应消息
     *
     * @param text 文本内容
     */
    public static void write(String text) {
        HttpServletResponse response = getResponse();
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        getWriter().write(text);
    }

    /**
     * 重定向
     *
     * @param location 重定向到的位置
     */
    public static void sendRedirect(String location) {
        try {
            getResponse().sendRedirect(location);
        } catch (IOException e) {
            throw new RuntimeCastException(e);
        }
    }

    /**
     * 私有化构造器
     */
    private ResponseContext() {

    }

}