package com.shuangshuan.scaffold.nosql.redis;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.common.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * redis设置值
     *
     * @param redisRequest  将要设置的Redis额值
     * @return Redis设置成功
     */
    @Operation(summary = "set redis key", description = "set redis key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "set redis key successfully")
    })
    @PostMapping("/set")
    public ResponseResult<String> set(@Parameter(description = "set redis key") @RequestBody @Valid RedisRequest redisRequest, BindingResult bindingResult) {
        redisUtil.set(redisRequest.getKey(), redisRequest.getValue(), 60000);
        return ResponseResult.success(redisRequest.getKey(),"set redis key success");
    }

    /**
     * redis获取值
     *
     * @param key  将要获取的Redis额值
     * @return Redis获取成功
     */
    @Operation(summary = "get redis key", description = "get redis key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get redis key successfully")
    })
    @GetMapping("/get/{key}")
    public ResponseResult<String> get(@Parameter(name = "key", description = "key", required = true) @PathVariable String key) {
        return ResponseResult.success((String) redisUtil.get(key));
    }

    /**
     * redis设置值
     *
     * @param key  将要删除的Redis额值
     * @return Redis删除成功
     */
    @Operation(summary = "delete redis key", description = "delete redis key")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "delete redis key successfully")
    })
    @DeleteMapping("/delete/{key}")
    public ResponseResult<String> delete(@Parameter(name = "key", description = "key", required = true) @PathVariable String key) {
        redisUtil.delete(key);
        return ResponseResult.success(key,"delete redis key success");
    }

    /**
     * 分页获取 Redis 所有 key-value
     * @param pattern 匹配 key 的模式（默认 "*" 查询全部）
     * @param page 页码（从 1 开始）
     * @param pageSize 每页大小
     * @return Map<String, Object> 结果
     */
    @Operation(summary = "get all redis key with value", description = "get all redis key with value")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get all redis key with value")
    })
    @GetMapping("/all")
    public ResponseResult<Map<String, Object>> getAllKeysWithValues(
            @Parameter(name = "pattern", description = "pattern", required = true)
            @RequestParam(defaultValue = "*") String pattern,
            @Parameter(name = "page", description = "page", required = true)
            @RequestParam(defaultValue = "1") int page,
            @Parameter(name = "pageSize", description = "pageSize", required = true)
            @RequestParam(defaultValue = "10") int pageSize) {

        return ResponseResult.success(redisUtil.getAllKeysWithValues(pattern, page, pageSize));
    }

}

