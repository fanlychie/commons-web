package org.fanlychie.commons.web.spring.servlet;

import org.fanlychie.commons.web.servelt.HttpContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Servlet 处理器拦截, 可以通过 HttpContext 获取请求相关的上下文变量
 * Created by fanlychie on 2017/2/15.
 */
public abstract class ServletHandlerInterceptor extends HandlerInterceptorAdapter {

    /**
     * 跳过的 URL
     */
    private List<String> skipUrls;

    /**
     * 拦截的 URL
     */
    private List<String> interceptUrls;

    public abstract boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;

    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpContext.init(request, response);
        String uri = request.getRequestURI();
        if (skipUrl(uri) || !interceptUrl(uri)) {
            return true;
        }
        return handle(request, response, handler);
    }

    /**
     * 设置跳过的 URL 地址列表
     *
     * @param skipUrls 跳过的 URL 地址列表
     */
    public void setSkipUrls(List<String> skipUrls) {
        this.skipUrls = skipUrls;
    }

    /**
     * 设置拦截的 URL 地址列表
     *
     * @param interceptUrls 拦截的 URL 地址列表
     */
    public void setInterceptUrls(List<String> interceptUrls) {
        this.interceptUrls = interceptUrls;
    }

    /**
     * 转发
     *
     * @param path 路径
     * @return 固定返回 false
     * @throws Exception
     */
    protected boolean forward(String path) throws Exception {
        HttpContext.getRequest().getRequestDispatcher(path).forward(HttpContext.getRequest(), HttpContext.getResponse());
        return false;
    }

    /**
     * 重定向
     *
     * @param location 重定向到的位置
     * @return 固定返回 false
     * @throws Exception
     */
    protected boolean sendRedirect(String location) throws Exception {
        HttpContext.getResponse().sendRedirect(location);
        return false;
    }

    /**
     * 匹配 URL 地址
     *
     * @param uri        URI 地址
     * @param expression 表达式, 字符 '*' 被替换成正则表达式 '\\S*'
     * @return 匹配成功返回 true, 否则返回 false
     */
    protected boolean matcheUrl(String uri, String expression) {
        return uri.matches(expression.replace("*", "\\S*"));
    }

    /**
     * 向客户端写出文本消息
     *
     * @param message 消息内容
     * @return 固定返回 false
     * @throws Exception
     */
    protected boolean writeTextMessage(String message) throws Exception {
        HttpServletResponse response = HttpContext.getResponse();
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(message);
        return false;
    }

    // 跳过 URL
    private boolean skipUrl(String uri) {
        if (!CollectionUtils.isEmpty(skipUrls)) {
            for (String skipUrl : skipUrls) {
                if (matcheUrl(uri, skipUrl)) {
                    return true;
                }
            }
        }
        return false;
    }

    // 拦截 URL
    private boolean interceptUrl(String uri) {
        if (!CollectionUtils.isEmpty(interceptUrls)) {
            for (String interceptUrl : interceptUrls) {
                if (matcheUrl(uri, interceptUrl)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

}