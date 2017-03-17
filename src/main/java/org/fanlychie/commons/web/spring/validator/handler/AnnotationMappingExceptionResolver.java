package org.fanlychie.commons.web.spring.validator.handler;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 注解异常处理器
 * Created by fanlychie on 2017/3/15.
 */
public class AnnotationMappingExceptionResolver extends SimpleMappingExceptionResolver {

    public AnnotationMappingExceptionResolver() {
        System.out.println("---------- AnnotationMappingExceptionResolver --------------------");
    }

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return super.doResolveException(request, response, handler, ex);
    }

}