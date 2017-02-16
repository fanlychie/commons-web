package org.fanlychie.commons.web.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fanlychie.commons.web.servlet.ClientInformation;
import org.fanlychie.commons.web.servlet.RecyclableHttpServletRequest;
import org.fanlychie.commons.web.servlet.RequestContext;
import org.fanlychie.commons.web.servlet.ResponseContext;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;

/**
 * 请求记录过滤器
 * <p>
 * 可以通过 RequestContext 和 ResponseContext 获取请求相关的上下文变量
 * <p>
 * Created by fanlychie on 2017/2/15.
 */
public class RequestLoggingFilter implements Filter {

    private Log logger = LogFactory.getLog(RequestLoggingFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        RequestContext.setLocal(request);
        ResponseContext.setLocal(response);
        if (logger.isInfoEnabled()) {
            switch (((HttpServletRequest) request).getMethod().toUpperCase()) {
                case "GET":
                case "DELETE":
                    logger.info(getRequestMessage(getQueryParamString()));
                    break;
                case "PUT":
                case "POST":
                    if (isApplicationJsonRequest()) {
                        request = new RecyclableHttpServletRequest((HttpServletRequest) request);
                        RequestContext.setLocal(request);
                        logger.info(getRequestMessage());
                        logger.info("param: " + getJsonParamString());
                    } else if (isUrlencodedFormRequest()) {
                        logger.info(getRequestMessage());
                        logger.info("param: " + getFormParamString());
                    } else if (isFileUploadRequest()) {
                        logger.info(getRequestMessage());
                        logger.info("param: --- 文件上传 ---");
                    }
                    break;
                default:
                    logger.info(getRequestMessage());
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }

    /**
     * 是否为普通表单提交
     *
     * @return 是返回 true, 否则返回 false
     */
    private boolean isUrlencodedFormRequest() {
        String contentType = RequestContext.getRequest().getHeader("Content-Type");
        return contentType == null ? false : contentType.matches("application/x-www-form-urlencoded[\\S\\W]*");
    }

    /**
     * 是否为 JSON 参数提交
     *
     * @return 是返回 true, 否则返回 false
     */
    private boolean isApplicationJsonRequest() {
        String contentType = RequestContext.getRequest().getHeader("Content-Type");
        return contentType == null ? false : contentType.matches("application/json[\\S\\W]*");
    }

    /**
     * 是否为文件上传请求
     *
     * @return 是返回 true, 否则返回 false
     */
    private boolean isFileUploadRequest() {
        String contentType = RequestContext.getRequest().getHeader("Content-Type");
        return contentType == null ? false : contentType.matches("multipart/form-data[\\S\\W]*");
    }

    /**
     * 获取表单提交的参数字符串
     *
     * @return 返回表单提交的参数字符串信息
     */
    private String getFormParamString() {
        HttpServletRequest request = RequestContext.getRequest();
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
     * @return 返回查询参数的字符串信息
     */
    private String getQueryParamString() {
        String queryString = RequestContext.getRequest().getQueryString();
        return queryString == null ? "" : "?" + queryString;
    }

    /**
     * 获取 JSON 参数提交的字符串
     *
     * @return 返回 JSON 参数提交的字符串信息
     */
    private String getJsonParamString() {
        HttpServletRequest request = RequestContext.getRequest();
        if (!(request instanceof RecyclableHttpServletRequest)) {
            throw new IllegalArgumentException("request is not a RecyclableHttpServletRequest: " + request);
        }
        return ((RecyclableHttpServletRequest) request).getPayload();
    }

    /**
     * 获取请求体消息
     *
     * @return 返回请求体消息
     */
    private String getRequestMessage() {
        return getRequestMessage(null);
    }

    /**
     * 获取请求体消息
     *
     * @param queryString 查询字符串
     * @return 返回请求体消息
     */
    private String getRequestMessage(String queryString) {
        StringBuilder message = new StringBuilder();
        HttpServletRequest request = RequestContext.getRequest();
        message.append(request.getMethod().toUpperCase()).append(" ")
                .append(request.getRequestURI());
        if (queryString != null) {
            message.append(queryString);
        }
        message.append(" ")
                .append(request.getProtocol()).append(" ")
                .append(ClientInformation.getIPAddress(request));
        String contentType = request.getHeader("Content-Type");
        String contentLength = request.getHeader("Content-Length");
        String contentEncoding = request.getHeader("Content-Encoding");
        if (contentType != null || contentEncoding != null || contentLength != null) {
            message.append(" [")
                    .append("Content-Type=").append(contentType)
                    .append(", Content-Encoding=").append(contentEncoding)
                    .append(", Content-Length=").append(contentLength)
                    .append("]");
        }
        return message.toString();
    }

}