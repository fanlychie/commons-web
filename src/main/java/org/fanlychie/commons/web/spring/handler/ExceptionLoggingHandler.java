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
     * 默认错误处理
     */
    private String defaultErrorResolver;

    /**
     * JSON 结果消息
     */
    private String jsonErrorMessage;

    /**
     * JSON 结果映射
     */
    private String[] jsonErrorMappings;

    /**
     * 视图结果映射
     */
    private String[] viewErrorMappings;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("「捕捉到异常」", ex);
        HandlerMethod method = (HandlerMethod) handler;
        String location = method.getBeanType().getName() + "." + method.getMethod().getName();
        if (defaultErrorResolver == null || defaultErrorResolver.equals("view")) {
            if (matchLocation(location, jsonErrorMappings)) {
                return writeJSONMessage(response);
            }
        } else if (defaultErrorResolver.equals("json") && !matchLocation(location, viewErrorMappings)) {
            return writeJSONMessage(response);
        }
        return super.doResolveException(request, response, handler, ex);
    }

    /**
     * 设置默认错误处理, 取值 [json/view]
     *
     * @param defaultErrorResolver 默认错误处理
     */
    public void setDefaultErrorResolver(String defaultErrorResolver) {
        this.defaultErrorResolver = defaultErrorResolver;
    }

    /**
     * 设置 JSON 结果消息, 服务器抛出异常时, 以此消息响应客户端请求
     *
     * @param jsonErrorMessage json 字符串消息内容
     */
    public void setJsonErrorMessage(String jsonErrorMessage) {
        this.jsonErrorMessage = jsonErrorMessage;
    }

    /**
     * 设置 JSON 结果返回的路径映射, 路径细化到控制器的方法名称。
     * 通配符*可用于替换多个字类字符, eg: com.domain.controller.UserController.*
     * 配置此项需关联配置 jsonErrorMessage 项, 以保证正确返回 JSON 结果到客户端
     *
     * @param jsonErrorMappings 映射 JSON 结果返回的路径
     */
    public void setJsonErrorMappings(String[] jsonErrorMappings) {
        this.jsonErrorMappings = convertRegularExpression(jsonErrorMappings);
    }

    /**
     * 设置视图结果返回的路径映射, 路径细化到控制器的方法名称。
     * 通配符*可用于替换多个字类字符, eg: com.domain.controller.UserController.*
     *
     * @param viewErrorMappings 映射视图结果返回的路径
     */
    public void setViewErrorMappings(String[] viewErrorMappings) {
        this.viewErrorMappings = convertRegularExpression(viewErrorMappings);
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
        HttpContext.writeResponseMessage(response, jsonErrorMessage);
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