package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 长度, 仅当标注在 {@link java.lang.CharSequence} 类型的属性或方法时进行值的验证
 */
@Documented
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface Length {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String errmsg();

    Class<?> errtype() default String.class;

}