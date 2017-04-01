package org.fanlychie.commons.web.spring.validator.validation;

import org.fanlychie.commons.web.spring.validator.constraint.Int;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class IntValidator {

    private IntValidator() {

    }

    public static boolean isValid(Number number, Int integer) {
        if (number == null) {
            return false;
        }
        int intValue = number.intValue();
        return intValue >= integer.min() && intValue <= integer.max();
    }

}