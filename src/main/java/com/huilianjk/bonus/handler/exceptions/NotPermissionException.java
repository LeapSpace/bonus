package com.huilianjk.bonus.handler.exceptions;


import com.huilianjk.bonus.util.common.ErrorCode;

/**
 * 无权限Exception
 *
 * @author
 */

public class NotPermissionException extends DefException {
    private ErrorCode errorCode = ErrorCode.NOT_FOUND;

    public NotPermissionException() {
        super();
    }

    public NotPermissionException(String errMsg) {
        super(errMsg);
    }

    public NotPermissionException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }

}
