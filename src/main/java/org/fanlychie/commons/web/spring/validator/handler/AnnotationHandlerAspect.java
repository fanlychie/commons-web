package org.fanlychie.commons.web.spring.validator.handler;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.fanlychie.commons.web.spring.validator.Valid;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by 范忠云 on 2017/3/17.
 */
@Aspect
public class AnnotationHandlerAspect {

    public AnnotationHandlerAspect() {
        System.out.println("---------- AnnotationHandlerAspect --------------------");
    }

    //    @Before("execution(public * *(..)) && args(org.fanlychie.commons.web.spring.validator.Valid)")

    /*
    @Before(value="execution(public * *(..)) && target(bean) && @annotation(valid)", argNames="bean,valid")
    public void before(Object bean, Valid valid) {
        System.out.println();
    }
    */

    /*
    @Pointcut("@annotation(verify)")
    public void annotationPointCutDefinition(Valid verify) {
        System.out.println();
    }

    @Before("execution(public * *(..)) && annotationPointCutDefinition(verify)")
    public void before(Valid verify) {
        System.out.println();
    }
    */

    @Pointcut("args(valid)")
    public void annotationPointCutDefinition(Valid valid) {
        System.out.println();
    }

    @Before("execution(* *(.., @org.fanlychie.commons.web.spring.validator.Valid (*), ..))")
    public void before(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        for (MethodArgument argument : MethodArgument.of(joinPoint))
            if (argument.hasAnnotation(Valid.class) && argument.getValue() == null)
                throw new NullPointerException(String.format(
                        "%s: argument \"%s\" (at position %d) cannot be null",
                        methodSignature.getMethod(), argument.getName(), argument.getIndex()));
    }

    private static class MethodArgument {

        private final int index;
        private final String name;
        private final List<Annotation> annotations;
        private final Object value;

        private MethodArgument(
                int index,
                String name,
                List<Annotation> annotations,
                Object value) {
            this.index = index;
            this.name = name;
            this.annotations = Collections.unmodifiableList(annotations);
            this.value = value;
        }

        public int getIndex() { return index; }

        public String getName() { return name; }

        public List<Annotation> getAnnotations() { return annotations; }

        public boolean hasAnnotation(Class<? extends Annotation> type) {
            for (Annotation annotation : annotations)
                if (annotation.annotationType().equals(type))
                    return true;
            return false;
        }

        public Object getValue() { return value; }

        public static List<MethodArgument> of(JoinPoint joinPoint) {
            List<MethodArgument> arguments = new ArrayList<>();
            CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
            String[] names = codeSignature.getParameterNames();
            MethodSignature methodSignature =
                    (MethodSignature) joinPoint.getStaticPart().getSignature();
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();
            Object[] values = joinPoint.getArgs();
            for (int i = 0; i < values.length; i++)
                arguments.add(new MethodArgument(
                        i, names[i], Arrays.asList(annotations[i]), values[i]));
            return Collections.unmodifiableList(arguments);
        }

    }

}