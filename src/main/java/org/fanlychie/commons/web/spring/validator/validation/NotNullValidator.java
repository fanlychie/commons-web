package org.fanlychie.commons.web.spring.validator.validation;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class NotNullValidator {

    private NotNullValidator() {

    }

    public static boolean isValid(Object object) {
        return object != null;
    }

}