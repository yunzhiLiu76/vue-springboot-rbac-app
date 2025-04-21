package com.shuangshuan.scaffold.nosql.redis;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RedisRequest {
    @NotBlank(message = "redis key不能为空")
    @Schema(description = "redis key")
    private String key;
    @NotBlank(message = "redis value 不能为空")
    @Schema(description = "redis key")
    private String value;

}
