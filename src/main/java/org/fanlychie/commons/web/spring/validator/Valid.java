package org.fanlychie.commons.web.spring.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 方法的参数验证
 */
@Documented
@Target(PARAMETER)
@Retention(RUNTIME)
public @interface Valid {

}