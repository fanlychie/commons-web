package org.fanlychie.commons.web.spring.validator.validation;

import org.fanlychie.commons.web.spring.validator.constraint.Pattern;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class PatternValidator {

    private PatternValidator() {

    }

    public static boolean isValid(CharSequence charSequence, Pattern pattern) {
        return charSequence != null && charSequence.toString().matches(pattern.value());
    }

}