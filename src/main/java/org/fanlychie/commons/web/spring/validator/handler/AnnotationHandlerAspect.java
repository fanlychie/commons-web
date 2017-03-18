package org.fanlychie.commons.web.spring.validator.handler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.fanlychie.commons.web.spring.validator.Valid;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 注解处理AOP
 * Created by fanlychie on 2017/3/17.
 */
@Aspect
public class AnnotationHandlerAspect {

    @Before("execution(public * *(.., @org.fanlychie.commons.web.spring.validator.Valid (*), ..))")
    public void doResolveAnnotation(JoinPoint joinPoint) {
        boolean applicationJsonResponse = isApplicationJsonResponse(joinPoint);
        List<MethodArgument> arguments = MethodArgument.of(joinPoint);
        for (MethodArgument argument : arguments) {
            List<Annotation> annotations = argument.annotations;
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Valid.class)) {
                    AnnotationHandlerExecutor.doExecute(argument.value, applicationJsonResponse);
                }
            }
        }
    }

    private boolean isApplicationJsonResponse(JoinPoint joinPoint) {
        Annotation restControllerAnnotation = joinPoint.getTarget().getClass().getAnnotation(RestController.class);
        if (restControllerAnnotation != null) {
            return true;
        }
        MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
        Annotation responseBodyAnnotation = methodSignature.getMethod().getAnnotation(ResponseBody.class);
        if (responseBodyAnnotation != null) {
            return true;
        }
        return false;
    }

    private static class MethodArgument {

        /**
         * 方法的参数索引
         */
        private int index;

        /**
         * 方法的参数名称
         */
        private String name;

        /**
         * 方法的参数的值
         */
        private Object value;

        /**
         * 方法的参数注解列表
         */
        private List<Annotation> annotations;

        // 解析方法的参数列表信息
        private static List<MethodArgument> of(JoinPoint joinPoint) {
            List<MethodArgument> arguments = new ArrayList<>();
            String[] names = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
            MethodSignature methodSignature = (MethodSignature) joinPoint.getStaticPart().getSignature();
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
            Object[] values = joinPoint.getArgs();
            for (int i = 0; i < values.length; i++) {
                MethodArgument argument = new MethodArgument();
                argument.index = i;
                argument.name = names[i];
                argument.value = values[i];
                argument.annotations = Arrays.asList(annotations[i]);
                arguments.add(argument);
            }
            return arguments;
        }

    }

}