package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class AlphanumericValidator {

    private AlphanumericValidator() {

    }

    public static boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.toString().matches("^(?=.*[0-9])(?=.*[a-zA-Z])([a-zA-Z0-9]{2,})$");
    }

}