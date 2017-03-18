package org.fanlychie.commons.web.spring.validator.constraint;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 安全的HTML, 仅当标注在 {@link java.lang.CharSequence} 类型的属性时进行值的验证和矫正
 */
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface SafeHtml {

    Strategy value() default Strategy.CLEAR;

    public static enum Strategy { ESCAPE, CLEAR; }

}