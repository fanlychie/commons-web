package org.fanlychie.commons.web.spring.validator.validation;

import org.fanlychie.commons.web.spring.validator.constraint.Integer;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class IntegerValidator {

    private IntegerValidator() {

    }

    public static boolean isValid(Number number, Integer integer) {
        int intValue = number.intValue();
        return intValue >= integer.min() && intValue <= integer.max();
    }

}