package com.huilianjk.bonus.util.common;

/**
 * Created by space on 2017/10/15.
 */
public enum  ErrorCode {
    SUCCESS(0),
    ERROR(1),
    NOT_FOUND(2, "数据不存在或已删除"),
    FORBIDDEN(3, "数据校验出错");
    private final int value;
    private final String msg;

    ErrorCode(int value) {
        this.value = value;
        this.msg = "";
    }

    ErrorCode(int value, String msg) {
        this.value = value;
        this.msg = msg;
    }

    public int intValue() {
        return this.value;
    }

    public String getMsg() {
        return msg;
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
