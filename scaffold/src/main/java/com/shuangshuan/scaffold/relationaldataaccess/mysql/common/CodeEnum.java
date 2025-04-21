package com.shuangshuan.scaffold.relationaldataaccess.mysql.common;

public enum CodeEnum {
    SUCCESS(200,"操作成功!"),
    FAILURE(201,"操作失败");

    private final int code;

    private final String message;

    CodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
