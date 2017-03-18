package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class NumericValidator {

    private NumericValidator() {

    }

    public static boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.toString().matches("^\\d+$");
    }

}