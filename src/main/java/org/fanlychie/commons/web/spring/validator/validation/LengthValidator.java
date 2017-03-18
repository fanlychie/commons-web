package org.fanlychie.commons.web.spring.validator.validation;

import org.fanlychie.commons.web.spring.validator.constraint.Length;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class LengthValidator {

    private LengthValidator() {

    }

    public static boolean isValid(CharSequence charSequence, Length length) {
        if (charSequence != null) {
            int len = charSequence.length();
            return len >= length.min() && len <= length.max();
        }
        return false;
    }

}