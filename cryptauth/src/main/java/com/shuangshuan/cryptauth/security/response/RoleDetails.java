package com.shuangshuan.cryptauth.security.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class RoleDetails {

    @Schema(description = "页面路由权限点")
    private String[] menus;
    @Schema(description = "按钮权限点")
    private String[] points;
    @Schema(description = "用户的所有角色id")
    private List<Integer> roles;
}
