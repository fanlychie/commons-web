package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class AlphabeticValidator {

    private AlphabeticValidator() {

    }

    public static boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.toString().matches("^[a-zA-Z]+$");
    }

}