package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 小数, 仅当标注在 float/Float, double/Double 类型的属性时进行值的验证
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface Decimal {

    double min() default 0;

    double max() default Double.MAX_VALUE;

    String errmsg();

    Class<?> errtype() default String.class;

}