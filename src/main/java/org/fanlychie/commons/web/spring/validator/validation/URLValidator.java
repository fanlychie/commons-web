package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class URLValidator {

    private URLValidator() {

    }

    public static boolean isValid(CharSequence charSequence) {
        return charSequence != null && charSequence.toString().matches("^(https?|ftp|file):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|]$");
    }

}