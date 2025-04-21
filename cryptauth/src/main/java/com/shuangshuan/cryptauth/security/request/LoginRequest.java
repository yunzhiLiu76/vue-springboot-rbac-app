package com.shuangshuan.cryptauth.security.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String mobile;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;


    // 默认构造方法
    public LoginRequest() {
    }

    // 带参数的构造方法
    public LoginRequest(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

}

