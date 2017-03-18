package org.fanlychie.commons.web.exception;

/**
 * 参数校验异常
 * Created by fanlychie on 2017/3/18.
 */
public class ArgumentVaildException extends RuntimeException {

    private boolean applicationJsonResponse;

    public ArgumentVaildException(String message, boolean applicationJsonResponse) {
        super(message);
        this.applicationJsonResponse = applicationJsonResponse;
    }

    public boolean isApplicationJsonResponse() {
        return applicationJsonResponse;
    }

}