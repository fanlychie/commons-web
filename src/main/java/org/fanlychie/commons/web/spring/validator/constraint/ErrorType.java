package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 类注解, 参数值验证失败时的返回值类型
 */
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ErrorType {

    Class<?> value();

}