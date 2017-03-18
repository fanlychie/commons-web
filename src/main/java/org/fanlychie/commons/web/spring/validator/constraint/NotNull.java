package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Not Null
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface NotNull {

    String errmsg();

    Class<?> errtype() default String.class;

}