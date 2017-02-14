package org.fanlychie.commons.web.exception;

/**
 * 解析字符串为日期对象异常
 * Created by fanlychie on 2017/2/15.
 */
public class ParseStringToDateException extends RuntimeException {

    public ParseStringToDateException(String message) {
        super(message);
    }

}