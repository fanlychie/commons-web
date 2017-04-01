package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 整数, 仅当标注在 byte/Byte, short/Short, int/Int, long/Long 类型的属性时进行值的验证
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Int {

    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String errmsg();

    Class<?> errtype() default String.class;

}