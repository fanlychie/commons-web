package org.fanlychie.commons.web.spring.validator.handler;

import com.alibaba.fastjson.JSON;
import org.fanlychie.commons.web.exception.ArgumentVaildException;
import org.fanlychie.commons.web.spring.validator.constraint.Alphabetic;
import org.fanlychie.commons.web.spring.validator.constraint.Alphanumeric;
import org.fanlychie.commons.web.spring.validator.constraint.Length;
import org.fanlychie.commons.web.spring.validator.constraint.NotBlank;
import org.fanlychie.commons.web.spring.validator.constraint.NotEmpty;
import org.fanlychie.commons.web.spring.validator.constraint.NotNull;
import org.fanlychie.commons.web.spring.validator.constraint.Numeric;
import org.fanlychie.commons.web.spring.validator.constraint.Pattern;
import org.fanlychie.commons.web.spring.validator.constraint.SafeHtml;
import org.fanlychie.commons.web.spring.validator.validation.AlphabeticValidator;
import org.fanlychie.commons.web.spring.validator.validation.AlphanumericValidator;
import org.fanlychie.commons.web.spring.validator.validation.LengthValidator;
import org.fanlychie.commons.web.spring.validator.validation.NotBlankValidator;
import org.fanlychie.commons.web.spring.validator.validation.NotEmptyValidator;
import org.fanlychie.commons.web.spring.validator.validation.NotNullValidator;
import org.fanlychie.commons.web.spring.validator.validation.NumericValidator;
import org.fanlychie.commons.web.spring.validator.validation.PatternValidator;
import org.fanlychie.commons.web.spring.validator.validation.SafeHtmlValidator;
import org.fanlychie.jreflect.BeanDescriptor;
import org.fanlychie.jreflect.ConstructorDescriptor;

/**
 * 注解处理执行器
 * Created by fanlychie on 2017/3/18.
 */
public final class AnnotationHandlerExecutor {

    private AnnotationHandlerExecutor() {

    }

    public static void doExecute(Object bean, boolean applicationJsonResponse) {
        executeNotNullAnnotation(bean, applicationJsonResponse);
        executeNotEmptyAnnotation(bean, applicationJsonResponse);
        executeNotBlankAnnotation(bean, applicationJsonResponse);
        executeLengthAnnotation(bean, applicationJsonResponse);
        executeSafeHtmlAnnotation(bean, applicationJsonResponse);
        executePatternAnnotation(bean, applicationJsonResponse);
        executeNumericAnnotation(bean, applicationJsonResponse);
        executeAlphabeticAnnotation(bean, applicationJsonResponse);
        executeAlphanumericAnnotation(bean, applicationJsonResponse);
    }

    private static void executeNotNullAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<NotNull>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, NotNull annotation, Class<?> beanErrorType) {
                if (!NotNullValidator.isValid(value)) {
                    throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                }
            }
        }.start();
    }

    private static void executeNotEmptyAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<NotEmpty>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, NotEmpty annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!NotEmptyValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeNotBlankAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<NotBlank>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, NotBlank annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!NotBlankValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeAlphabeticAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Alphabetic>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Alphabetic annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!AlphabeticValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeAlphanumericAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Alphanumeric>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Alphanumeric annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!AlphanumericValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeLengthAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Length>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Length annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!LengthValidator.isValid((CharSequence) value, annotation)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeNumericAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Numeric>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Numeric annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!NumericValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executePatternAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Pattern>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Pattern annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!PatternValidator.isValid((CharSequence) value, annotation)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeSafeHtmlAnnotation(Object bean, boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<SafeHtml>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, SafeHtml annotation, Class<?> beanErrorType) {
                if (String.class.isAssignableFrom(type)) {
                    String safeHtmlValue = SafeHtmlValidator.validCorrect((String) value, annotation);
                    if (safeHtmlValue != null) {
                        beanDescriptor.setValueByName(name, safeHtmlValue);
                    }
                }
            }
        }.start();
    }

    private static ArgumentVaildException argumentVaildException(String message, Class<?> argErrorType, Class<?> beanErrorType, boolean applicationJsonResponse) {
        if (applicationJsonResponse) {
            Class<?> errorType = null;
            if (beanErrorType != null) {
                errorType = beanErrorType;
            } else {
                errorType = argErrorType;
            }
            Object result = new ConstructorDescriptor(errorType).newInstance(message);
            return new ArgumentVaildException(JSON.toJSONString(result), true);
        } else {
            return new ArgumentVaildException(message, false);
        }
    }

}