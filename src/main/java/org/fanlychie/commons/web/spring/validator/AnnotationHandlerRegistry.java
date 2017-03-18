package org.fanlychie.commons.web.spring.validator;

import org.fanlychie.commons.web.spring.validator.handler.AnnotationHandlerAspect;
import org.fanlychie.commons.web.spring.validator.handler.AnnotationMappingExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 注解处理器注册
 * Created by fanlychie on 2017/3/15.
 */
@EnableWebMvc
@Configuration
@EnableAspectJAutoProxy
public class AnnotationHandlerRegistry extends WebMvcConfigurerAdapter {

    @Bean
    public AnnotationHandlerAspect registerAnnotationHandlerAspect() {
        return new AnnotationHandlerAspect();
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new AnnotationMappingExceptionResolver());
        // 重排序, 确保 AnnotationMappingExceptionResolver 排在第一位
        // {@see org.springframework.web.servlet.DispatcherServlet#processHandlerException}
        Collections.sort(exceptionResolvers, new Comparator<HandlerExceptionResolver>() {
            @Override
            public int compare(HandlerExceptionResolver o1, HandlerExceptionResolver o2) {
                if (o1 instanceof AnnotationMappingExceptionResolver) {
                    return -1;
                }
                return 0;
            }
        });
    }

}