package com.shuangshuan.scaffold.relationaldataaccess.mysql.common;

import lombok.Getter;

@Getter
public enum BusinessResponseCode {

    // REDIS相关操作
    REDIS_CREATE_SUCCESS(1001, "redis创建失败"),
    REDIS_UPDATE_SUCCESS(1002, "redis更新失败"),
    REDIS_DELETE_SUCCESS(1003, "redis删除失败");

    private final int code;
    private final String message;

    BusinessResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
