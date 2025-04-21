package com.shuangshuan.cryptauth.security.controller;

import com.shuangshuan.cryptauth.common.BusinessResponseCode;
import com.shuangshuan.cryptauth.common.ResponseResult;
import com.shuangshuan.cryptauth.security.entity.UserAccount;
import com.shuangshuan.cryptauth.security.request.AddUserRequest;
import com.shuangshuan.cryptauth.security.request.ChangePasswordRequest;
import com.shuangshuan.cryptauth.security.response.RoleDetails;
import com.shuangshuan.cryptauth.security.response.UserDetailsResponse;
import com.shuangshuan.cryptauth.security.service.UserAccountServiceImpl;
import com.shuangshuan.cryptauth.security.service.UserService;
import com.shuangshuan.cryptauth.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "UserController")
@RestController
@RequestMapping("/sys")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    private UserService userService;  // 注入 UserService，用于修改密码等业务逻辑

    @Autowired
    private UserAccountServiceImpl userAccountServiceImpl;


    /**
     * 获取当前用户详细信息
     *
     * @return 用户详细信息
     */
    @Operation(summary = "details", description = "details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "get current user successfully")
    })
    @GetMapping("/details")
    public ResponseResult<UserDetailsResponse> getUserDetails() {
        // 获取当前登录用户的用户名
        String username = SecurityUtils.getCurrentUsername();

        // 查找用户并返回详细信息
        UserAccount user = userService.queryUserByUsername(username);

        // 查询这个用户对应的roles
        RoleDetails roles =userAccountServiceImpl.queryUserPermissionDetails(user);

        // 将用户信息转换为 DTO（数据传输对象），返回用户详细信息
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse(
                user.getUsername(),
                roles,
                user.getId()
        );

        return ResponseResult.success(userDetailsResponse);
    }


    /**
     * 修改用户密码
     *
     * @param changePasswordRequest 包含旧密码和新密码
     * @return 操作结果
     */
    @Operation(summary = "updatePass", description = "updatePass")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "updatePass successfully"),
            @ApiResponse(responseCode = "2001", description = "user not find"),
            @ApiResponse(responseCode = "5001", description = "updatePass failed"),
            @ApiResponse(responseCode = "5002", description = "old password incorrect")
    })
    @PutMapping("/user/updatePass")
    public ResponseResult<String> changePassword(@Parameter(description = "changePasswordRequest") @RequestBody ChangePasswordRequest changePasswordRequest,
                                                 BindingResult bindingResult) {
        // 获取当前登录用户的用户名
        String username = SecurityUtils.getCurrentUsername();

        // 调用服务层修改密码
        try {
            boolean isPasswordChanged = userService.changePassword(username, changePasswordRequest);

            if (isPasswordChanged) {
                return ResponseResult.success(null, BusinessResponseCode.PASSWORD_UPDATE_SUCCESS.getMessage());
            } else {
                return ResponseResult.error(BusinessResponseCode.PASSWORD_UPDATE_FAILED);
            }
        } catch (IllegalArgumentException e) {
            // 处理密码不正确的情况
            return ResponseResult.error(BusinessResponseCode.OLD_PASSWORD_INCORRECT);
        }
    }


    /**
     * 添加新用户
     *
     * @param addUserRequest 包含新用户的详细信息
     * @return 操作结果
     */
    @Operation(summary = "addUser", description = "addUser")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "addUser successfully"),
            @ApiResponse(responseCode = "400", description = "Username already exists"),
            @ApiResponse(responseCode = "2002", description = "Error occurred while creating use")
    })
    @PostMapping("/user/add")
    public ResponseResult<UserAccount> addUser(@Parameter(description = "addUserRequest") @RequestBody AddUserRequest addUserRequest,
                                               BindingResult bindingResult) {
        try {

            // 获取当前登录用户的用户名
            String loginUser = SecurityUtils.getCurrentUsername();
            // 调用服务层创建用户
            UserAccount userAccount = userService.addUser(addUserRequest, loginUser);

            // 返回成功响应
            return ResponseResult.success(userAccount, BusinessResponseCode.USER_CREATED_SUCCESS.getMessage());
        } catch (IllegalArgumentException e) {
            // 处理用户名已存在的情况
            return ResponseResult.error(BusinessResponseCode.USERNAME_ALREADY_EXISTS_FAILED);
        } catch (Exception e) {
            // 处理其他未知异常
            return ResponseResult.error(BusinessResponseCode.USER_CREATION_FAILED);
        }
    }

    /**
     * 分页查询用户列表
     *
     * @param page     页码，默认值为0
     * @param pagesize 每页的记录数，默认值为10
     * @return 分页后的用户信息
     */
    @Operation(summary = "Get users with pagination", description = "Fetch Users in a paginated manner")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "users fetched successfully")
    })
    @GetMapping("/getUsersList")
    public ResponseResult<Page<UserAccount>> getUsersPage(
            @Parameter(name = "page", description = "page", required = true)
            @RequestParam(value = "page", defaultValue = "1") int page,
            @Parameter(name = "pagesize", description = "pagesize", required = true)
            @RequestParam(value = "pagesize", defaultValue = "10") int pagesize) {
        logger.info("Fetching users with pagination, page: {}, size: {}", page, pagesize);
        Pageable pageable = PageRequest.of(page - 1, pagesize);
        Page<UserAccount> usersPage = userAccountServiceImpl.findAllUsers(pageable);
        logger.info("Fetched {} users in page {} with size {}", usersPage.getTotalElements(), page, pagesize);
        return ResponseResult.success(usersPage, BusinessResponseCode.USER_LIST_FETCHED_SUCCESS.getMessage());
    }


    /**
     * 删除指定ID的user
     *
     * @param id user的ID
     * @return 删除结果的响应
     */
    @Operation(summary = "Delete user", description = "Delete a user by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "user deleted successfully"),
            @ApiResponse(responseCode = "2001", description = "user not found"),
            @ApiResponse(responseCode = "2004", description = "user deletion failed")
    })
    @DeleteMapping("/deleteUser/{id}")
    public ResponseResult<Void> deleteUser(
            @Parameter(description = "ID of the user to delete") @PathVariable Integer id) {
        logger.info("Deleting user with ID: {}", id);
        if (!userService.existsById(id)) {
            logger.warn("user with ID: {} not found for deletion", id);
            return ResponseResult.error(BusinessResponseCode.USER_NOT_FOUND);
        }
        userAccountServiceImpl.deleteById(id);
        logger.info("Successfully deleted user with ID: {}", id);
        return ResponseResult.success(null, BusinessResponseCode.USER_DELETED_SUCCESS.getMessage());
    }



}

