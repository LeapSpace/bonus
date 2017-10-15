package com.huilianjk.bonus.handler.exceptions;


import com.huilianjk.bonus.util.common.ErrorCode;

/**
 * 资源不存在Exception
 *
 * @author
 */

public class NotFoundException extends DefException {
    private ErrorCode errorCode = ErrorCode.NOT_FOUND;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String msg) {
        super(msg);
    }

    public NotFoundException(ErrorCode errorCode, String msg) {
        super(errorCode, msg);
    }
}
