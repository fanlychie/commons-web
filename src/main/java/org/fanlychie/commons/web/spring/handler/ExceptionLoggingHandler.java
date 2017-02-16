package org.fanlychie.commons.web.spring.handler;

import org.fanlychie.commons.web.servlet.ResponseContext;
import org.springframework.util.CollectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

    /**
     * 异常返回的 JSON 信息映射表
     */
    private Map<Class<?>, String> exceptionJsonMessageMappings;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        logger.error("「捕捉到异常」", ex);
        HandlerMethod method = (HandlerMethod) handler;
        String location = method.getBeanType().getName() + "." + method.getMethod().getName();
        if (defaultExceptionResolver == null || defaultExceptionResolver.equals("view")) {
            if (matchLocation(location, exceptionJsonMappings)) {
                return writeJSONMessage(response, ex);
            }
        } else if (defaultExceptionResolver.equals("json") && !matchLocation(location, exceptionViewMappings)) {
            return writeJSONMessage(response, ex);
        }
        return super.doResolveException(request, response, handler, ex);
    }

    /**
     * 设置默认异常处理方式, 取值 [json/view]
     * <p>
     * json: 表示异常时默认返回 JSON 字符串结果
     * <p>
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
     * <p>
     * 格式: 控制器全路径名称.方法名称
     * <p>
     * 通配符*可用于替换多个字类字符, eg: com.domain.controller.UserController.*
     * <p>
     * 配置此项需关联配置 defaultExceptionJsonMessage 项, 以保证正确返回 JSON 结果到客户端
     *
     * @param exceptionJsonMappings 异常返回 JSON 结果的位置映射集合
     */
    public void setExceptionJsonMappings(String[] exceptionJsonMappings) {
        this.exceptionJsonMappings = convertRegularExpression(exceptionJsonMappings);
    }

    /**
     * 设置异常返回视图的位置映射集合, 路径细化到控制器的方法名称。
     * <p>
     * 格式: 控制器全路径名称.方法名称
     * <p>
     * 通配符*可用于替换多个字类字符, eg: com.domain.controller.UserController.*
     *
     * @param exceptionViewMappings 异常返回视图的位置映射集合
     */
    public void setExceptionViewMappings(String[] exceptionViewMappings) {
        this.exceptionViewMappings = convertRegularExpression(exceptionViewMappings);
    }

    /**
     * 设置异常返回的 JSON 信息映射表, 发生异常时, 若为 JSON 结果返回, 则先在此映射表查找异常映射的消息内容,
     * <p>
     * 若查找不到才使用 defaultExceptionJsonMessage 配置项返回消息
     *
     * @param exceptionJsonMessageMappings 异常返回的 JSON 信息映射表
     */
    public void setExceptionJsonMessageMappings(Map<Class<?>, String> exceptionJsonMessageMappings) {
        this.exceptionJsonMessageMappings = exceptionJsonMessageMappings;
    }

    // 转换为正则表达式表示
    private String[] convertRegularExpression(String[] mappings) {
        for (int i = 0; i < mappings.length; i++) {
            mappings[i] = mappings[i].replace("*", "\\w*");
        }
        return mappings;
    }

    // 写出 JSON 到客户端
    private ModelAndView writeJSONMessage(HttpServletResponse response, Exception e) {
        String jsonString = defaultExceptionJsonMessage;
        if (!CollectionUtils.isEmpty(exceptionJsonMessageMappings)) {
            String mappingValue = exceptionJsonMessageMappings.get(e.getClass());
            if (mappingValue != null) {
                jsonString = mappingValue;
            }
        }
        ResponseContext.setLocal(response);
        ResponseContext.write(jsonString);
        return new ModelAndView();
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