package org.fanlychie.commons.web.spring.validator.handler;

import org.fanlychie.commons.web.spring.validator.constraint.ErrorType;
import org.fanlychie.jreflect.BeanDescriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * 注解执行运行器
 * Created by fanlychie on 2017/3/18.
 */
public abstract class AnnotationHandlerRunner<A extends Annotation> {

    private final Object bean;

    private final Class<A> annotationClass;

    public AnnotationHandlerRunner(Object bean) {
        this.bean = bean;
        this.annotationClass = (Class<A>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public final void start() {
        Class<?> beanErrorType = null;
        ErrorType errorType = bean.getClass().getAnnotation(ErrorType.class);
        if (errorType != null) {
            beanErrorType = errorType.value();
        }
        BeanDescriptor beanDescriptor = new BeanDescriptor(bean);
        Map<Field, A> map = beanDescriptor.getFieldDescriptor().getFieldAnnotationMap(annotationClass);
        for (Field field : map.keySet()) {
            run(beanDescriptor, field.getName(), beanDescriptor.getValueByName(field.getName()), field.getType(), map.get(field), beanErrorType);
        }
    }

    protected abstract void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, A annotation, Class<?> beanErrorType);

}