package com.shuangshuan.cryptauth.security.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserDetailsResponse {

    @Schema(description = "名字")
    private String username;

    @Schema(description = "用户权限点对象")
    private RoleDetails roles;        // 用户权限点对象

    @Schema(description = "用户id")
    private Integer userId;

    // 构造函数
    public UserDetailsResponse(String username,
                                RoleDetails roles,  Integer userId) {
        this.username = username;
        this.roles = roles;
        this.userId = userId;
    }


}
