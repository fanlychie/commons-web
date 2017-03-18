package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class NotEmptyValidator {

    private NotEmptyValidator() {

    }

    public static boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.toString().length() > 0;
    }

}