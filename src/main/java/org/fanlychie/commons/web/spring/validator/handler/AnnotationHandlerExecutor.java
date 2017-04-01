package org.fanlychie.commons.web.spring.validator.handler;

import com.alibaba.fastjson.JSON;
import org.fanlychie.commons.web.exception.ArgumentVaildException;
import org.fanlychie.commons.web.spring.validator.constraint.Alphabetic;
import org.fanlychie.commons.web.spring.validator.constraint.Alphanumeric;
import org.fanlychie.commons.web.spring.validator.constraint.Decimal;
import org.fanlychie.commons.web.spring.validator.constraint.Email;
import org.fanlychie.commons.web.spring.validator.constraint.Int;
import org.fanlychie.commons.web.spring.validator.constraint.Length;
import org.fanlychie.commons.web.spring.validator.constraint.NotBlank;
import org.fanlychie.commons.web.spring.validator.constraint.NotEmpty;
import org.fanlychie.commons.web.spring.validator.constraint.NotNull;
import org.fanlychie.commons.web.spring.validator.constraint.Numeric;
import org.fanlychie.commons.web.spring.validator.constraint.Pattern;
import org.fanlychie.commons.web.spring.validator.constraint.SafeHtml;
import org.fanlychie.commons.web.spring.validator.constraint.URL;
import org.fanlychie.commons.web.spring.validator.validation.AlphabeticValidator;
import org.fanlychie.commons.web.spring.validator.validation.AlphanumericValidator;
import org.fanlychie.commons.web.spring.validator.validation.DecimalValidator;
import org.fanlychie.commons.web.spring.validator.validation.EmailValidator;
import org.fanlychie.commons.web.spring.validator.validation.IntValidator;
import org.fanlychie.commons.web.spring.validator.validation.LengthValidator;
import org.fanlychie.commons.web.spring.validator.validation.NotBlankValidator;
import org.fanlychie.commons.web.spring.validator.validation.NotEmptyValidator;
import org.fanlychie.commons.web.spring.validator.validation.NotNullValidator;
import org.fanlychie.commons.web.spring.validator.validation.NumericValidator;
import org.fanlychie.commons.web.spring.validator.validation.PatternValidator;
import org.fanlychie.commons.web.spring.validator.validation.SafeHtmlValidator;
import org.fanlychie.commons.web.spring.validator.validation.URLValidator;
import org.fanlychie.jreflect.BeanDescriptor;
import org.fanlychie.jreflect.ConstructorDescriptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注解处理执行器
 * Created by fanlychie on 2017/3/18.
 */
public final class AnnotationHandlerExecutor {

    private static final ConcurrentHashMap<Class<?>, Set<Class<? extends Annotation>>> MEMORY_CACHE_MAP = new ConcurrentHashMap<>();

    private AnnotationHandlerExecutor() {

    }

    public static void doExecute(final Object bean, final boolean applicationJsonResponse) {
        preHandle(bean);
        if (hasAnnotation(bean, SafeHtml.class)) {
            executeSafeHtmlAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, NotNull.class)) {
            executeNotNullAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, NotEmpty.class)) {
            executeNotEmptyAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, NotBlank.class)) {
            executeNotBlankAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Length.class)) {
            executeLengthAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Pattern.class)) {
            executePatternAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Numeric.class)) {
            executeNumericAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Alphabetic.class)) {
            executeAlphabeticAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Alphanumeric.class)) {
            executeAlphanumericAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Int.class)) {
            executeIntegerAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Email.class)) {
            executeEmailAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, URL.class)) {
            executeURLAnnotation(bean, applicationJsonResponse);
        }
        if (hasAnnotation(bean, Decimal.class)) {
            executeDecimalhAnnotation(bean, applicationJsonResponse);
        }
    }

    private static void executeNotNullAnnotation(final Object bean, final boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<NotNull>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, NotNull annotation, Class<?> beanErrorType) {
                if (!NotNullValidator.isValid(value)) {
                    throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                }
            }
        }.start();
    }

    private static void executeNotEmptyAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeNotBlankAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeAlphabeticAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeAlphanumericAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeDecimalhAnnotation(final Object bean, final boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Decimal>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Decimal annotation, Class<?> beanErrorType) {
                if (type == float.class || type == double.class || type == Float.class || type == Double.class) {
                    if (value == null || !DecimalValidator.isValid(value.toString(), annotation)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeNumericAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executePatternAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeSafeHtmlAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeIntegerAnnotation(final Object bean, final boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Int>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Int annotation, Class<?> beanErrorType) {
                if (type == byte.class || type == short.class || type == int.class || type == long.class ||
                        type == Byte.class || type == Short.class || type == java.lang.Integer.class || type == Long.class) {
                    if (!IntValidator.isValid((Number) value, annotation)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeLengthAnnotation(final Object bean, final boolean applicationJsonResponse) {
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

    private static void executeEmailAnnotation(final Object bean, final boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<Email>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, Email annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!EmailValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
                    }
                }
            }
        }.start();
    }

    private static void executeURLAnnotation(final Object bean, final boolean applicationJsonResponse) {
        new AnnotationHandlerRunner<URL>(bean) {
            @Override
            protected void run(BeanDescriptor beanDescriptor, String name, Object value, Class<?> type, URL annotation, Class<?> beanErrorType) {
                if (CharSequence.class.isAssignableFrom(type)) {
                    if (!URLValidator.isValid((CharSequence) value)) {
                        throw argumentVaildException(annotation.errmsg(), annotation.errtype(), beanErrorType, applicationJsonResponse);
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

    private static boolean hasAnnotation(Object bean, Class<? extends Annotation> annotationClass) {
        Set<Class<? extends Annotation>> annotations = MEMORY_CACHE_MAP.get(bean.getClass());
        return annotations.contains(annotationClass);
    }

    private static void preHandle(Object bean) {
        if (!MEMORY_CACHE_MAP.containsKey(bean.getClass())) {
            BeanDescriptor beanDescriptor = new BeanDescriptor(bean);
            List<Field> fields = beanDescriptor.getFieldDescriptor().getFields();
            Set<Class<? extends Annotation>> annotations = new HashSet<>();
            for (Field field : fields) {
                for (Annotation annotation : field.getAnnotations()) {
                    annotations.add(annotation.annotationType());
                }
            }
            MEMORY_CACHE_MAP.put(bean.getClass(), annotations);
        }
    }

}