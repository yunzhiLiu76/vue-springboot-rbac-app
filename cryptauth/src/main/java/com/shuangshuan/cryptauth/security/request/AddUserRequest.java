package com.shuangshuan.cryptauth.security.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddUserRequest {
    @NotBlank
    @Schema(description = "用户名")
    private String username;  // 用户名
    @NotBlank
    @Schema(description = "密码")
    private String password;  // 密码
   // 手机号

}
