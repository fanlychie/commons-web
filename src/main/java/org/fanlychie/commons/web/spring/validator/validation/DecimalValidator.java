package org.fanlychie.commons.web.spring.validator.validation;

import org.fanlychie.commons.web.spring.validator.constraint.Decimal;

import java.math.BigDecimal;

/**
 * Created by fanlychie on 2017/3/18.
 */
public class DecimalValidator {

    private DecimalValidator() {

    }

    public static boolean isValid(String strValue, Decimal decimal) {
        if (strValue == null) {
            return false;
        }
        BigDecimal bd0 = new BigDecimal(strValue);
        BigDecimal bd1 = new BigDecimal(String.valueOf(decimal.min()));
        BigDecimal bd2 = new BigDecimal(String.valueOf(decimal.max()));
        int bd0CompareBd1 = bd0.compareTo(bd1);
        int bd0CompareBd2 = bd0.compareTo(bd2);
        return (bd0CompareBd1 == 0 || bd0CompareBd1 == 1) && (bd0CompareBd2 == 0 || bd0CompareBd2 == -1);
    }

}