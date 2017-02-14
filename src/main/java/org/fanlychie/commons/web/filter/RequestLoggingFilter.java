package org.fanlychie.commons.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fanlychie.commons.web.servelt.HttpContext;
import org.fanlychie.commons.web.servelt.RecyclableHttpServletRequest;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 请求记录过滤器, 可以通过 HttpContext 获取请求相关的上下文变量
 * Created by fanlychie on 2017/2/15.
 */
public class RequestLoggingFilter implements Filter {

    private Log logger = LogFactory.getLog(RequestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        if (logger.isInfoEnabled()) {
            HttpServletRequest request = (HttpServletRequest) req;
            switch (request.getMethod().toUpperCase()) {
                case "GET":
                case "DELETE":
                    logger.debug(getRequestMessage(request, getQueryParamString(request)));
                    break;
                case "PUT":
                case "POST":
                    if (isApplicationJsonRequest(request)) {
                        request = new RecyclableHttpServletRequest(request);
                        logger.debug(getRequestMessage(request, null));
                        logger.debug("param: " + getJsonParamString(request));
                    } else if (isUrlencodedFormRequest(request)) {
                        logger.debug(getRequestMessage(request, null));
                        logger.debug("param: " + getFormParamString(request));
                    } else if (isFileUploadRequest(request)) {
                        logger.debug(getRequestMessage(request, null));
                        logger.debug("param: --- 文件上传 ---");
                    }
                    break;
                default:
                    logger.debug(getRequestMessage(request, null));
            }
            chain.doFilter(request, resp);
        }
        HttpContext.init((HttpServletRequest) req, (HttpServletResponse) resp);
    }

    @Override
    public void destroy() {

    }

    /**
     * 是否为普通表单提交
     *
     * @param request HttpServletRequest 对象
     * @return 是返回 true, 否则返回 false
     */
    private boolean isUrlencodedFormRequest(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType == null ? false : contentType.matches("application/x-www-form-urlencoded[\\S\\W]*");
    }

    /**
     * 是否为 JSON 参数提交
     *
     * @param request HttpServletRequest 对象
     * @return 是返回 true, 否则返回 false
     */
    private boolean isApplicationJsonRequest(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType == null ? false : contentType.matches("application/json[\\S\\W]*");
    }

    /**
     * 是否为文件上传请求
     *
     * @param request HttpServletRequest 对象
     * @return 是返回 true, 否则返回 false
     */
    private boolean isFileUploadRequest(HttpServletRequest request) {
        String contentType = request.getHeader("Content-Type");
        return contentType == null ? false : contentType.matches("multipart/form-data[\\S\\W]*");
    }

    /**
     * 获取表单提交的参数字符串
     *
     * @param request HttpServletRequest 对象
     * @return 返回表单提交的参数字符串信息
     */
    private String getFormParamString(HttpServletRequest request) {
        StringBuilder params = new StringBuilder("{");
        Enumeration<String> names = request.getParameterNames();
        while (names.hasMoreElements()) {
            String name = names.nextElement();
            params.append(name).append("=").append(request.getParameter(name)).append(", ");
        }
        int length = params.length();
        if (length > 1) {
            params.replace(length - 2, length, "}");
        } else {
            params.append("}");
        }
        return params.toString();
    }

    /**
     * 获取查询参数的字符串
     *
     * @param request HttpServletRequest 对象
     * @return 返回查询参数的字符串信息
     */
    private String getQueryParamString(HttpServletRequest request) {
        String queryString = request.getQueryString();
        return queryString == null ? "" : "?" + queryString;
    }

    /**
     * 获取 JSON 参数提交的字符串
     *
     * @param request HttpServletRequest 对象
     * @return 返回 JSON 参数提交的字符串信息
     */
    private String getJsonParamString(HttpServletRequest request) {
        return ((RecyclableHttpServletRequest) request).getPayload();
    }

    /**
     * 获取请求体消息
     *
     * @param request     HttpServletRequest 对象
     * @param queryString 查询字符串
     * @return 返回请求体消息
     */
    private String getRequestMessage(HttpServletRequest request, String queryString) {
        StringBuilder message = new StringBuilder();
        message.append(request.getMethod().toUpperCase())
                .append(" ").append(request.getRequestURI());
        if (queryString != null) {
            message.append(queryString);
        }
        message.append(" ").append(request.getProtocol())
                .append(" ").append(HttpContext.getIPAddress(request));
        String contentType = request.getHeader("Content-Type");
        String contentLength = request.getHeader("Content-Length");
        String contentEncoding = request.getHeader("Content-Encoding");
        if (contentType != null || contentEncoding != null || contentLength != null) {
            message.append(" [").append("Content-Type=").append(contentType);
            message.append(", Content-Encoding=").append(contentEncoding);
            message.append(", Content-Length=").append(contentLength).append("]");
        }
        return message.toString();
    }

}