package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 非空白(除去首尾的空白部分长度不为0), 仅当标注在 {@link java.lang.CharSequence} 类型的属性或方法时进行值的验证
 */
@Documented
@Target({FIELD, METHOD})
@Retention(RUNTIME)
public @interface NotBlank {

    String errmsg();

    Class<?> errtype() default String.class;

}