package org.fanlychie.commons.web.spring.handler;

import org.fanlychie.commons.web.servelt.HttpContext;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 异常记录日志处理器
 * Created by fanlychie on 2017/2/15.
 */
public class ExceptionLoggingHandler extends SimpleMappingExceptionResolver {

    /**
     * 默认异常处理方式
     */
    private String defaultExceptionResolver;

    /**
     * 默认异常返回的 JSON 信息
     */
    private String defaultExceptionJsonMessage;

    /**
     * 异常返回 JSON 结果的位置映射集合
     */
    private String[] exceptionJsonMappings;

    /**
     * 异常返回视图的位置映射集合
     */
    private String[] exceptionViewMappings;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("「捕捉到异常」", ex);
        HandlerMethod method = (HandlerMethod) handler;
        String location = method.getBeanType().getName() + "." + method.getMethod().getName();
        if (defaultExceptionResolver == null || defaultExceptionResolver.equals("view")) {
            if (matchLocation(location, exceptionJsonMappings)) {
                return writeJSONMessage(response);
            }
        } else if (defaultExceptionResolver.equals("json") && !matchLocation(location, exceptionViewMappings)) {
            return writeJSONMessage(response);
        }
        return super.doResolveException(request, response, handler, ex);
    }

    /**
     * 设置默认异常处理方式, 取值 [json/view]
     * json: 表示异常时默认返回 JSON 字符串结果
     * view: 表示异常时默认返回视图页面的结果
     *
     * @param defaultExceptionResolver 默认异常处理方式
     */
    public void setDefaultExceptionResolver(String defaultExceptionResolver) {
        this.defaultExceptionResolver = defaultExceptionResolver;
    }

    /**
     * 设置异常返回的 JSON 信息, 服务器抛出异常时, 以此消息响应客户端请求
     *
     * @param defaultExceptionJsonMessage json 字符串消息内容
     */
    public void setDefaultExceptionJsonMessage(String defaultExceptionJsonMessage) {
        this.defaultExceptionJsonMessage = defaultExceptionJsonMessage;
    }

    /**
     * 设置异常返回 JSON 结果的位置映射集合, 路径细化到控制器的方法名称。
     * 格式: 控制器全路径名称.方法名称
     * 通配符*可用于替换多个字类字符, eg: com.domain.controller.UserController.*
     * 配置此项需关联配置 defaultExceptionJsonMessage 项, 以保证正确返回 JSON 结果到客户端
     *
     * @param exceptionJsonMappings 异常返回 JSON 结果的位置映射集合
     */
    public void setExceptionJsonMappings(String[] exceptionJsonMappings) {
        this.exceptionJsonMappings = convertRegularExpression(exceptionJsonMappings);
    }

    /**
     * 设置异常返回视图的位置映射集合, 路径细化到控制器的方法名称。
     * 格式: 控制器全路径名称.方法名称
     * 通配符*可用于替换多个字类字符, eg: com.domain.controller.UserController.*
     *
     * @param exceptionViewMappings 异常返回视图的位置映射集合
     */
    public void setExceptionViewMappings(String[] exceptionViewMappings) {
        this.exceptionViewMappings = convertRegularExpression(exceptionViewMappings);
    }

    // 转换为正则表达式表示
    private String[] convertRegularExpression(String[] mappings) {
        for (int i = 0; i < mappings.length; i++) {
            mappings[i] = mappings[i].replace("*", "\\w*");
        }
        return mappings;
    }

    // 写出 JSON 到客户端
    private ModelAndView writeJSONMessage(HttpServletResponse response) {
        HttpContext.writeResponseMessage(response, defaultExceptionJsonMessage);
        return null;
    }

    // 匹配位置
    private boolean matchLocation(String location, String[] expressions) {
        if (expressions != null && expressions.length != 0) {
            for (String expression : expressions) {
                if (location.matches(expression)) {
                    return true;
                }
            }
        }
        return false;
    }

}