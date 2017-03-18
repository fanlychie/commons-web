package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class NotBlankValidator {

    private NotBlankValidator() {

    }

    public static boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.toString().trim().length() > 0;
    }

}