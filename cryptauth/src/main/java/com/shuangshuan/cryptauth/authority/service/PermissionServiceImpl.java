package com.shuangshuan.cryptauth.authority.service;

import com.shuangshuan.cryptauth.authority.entity.Permission;
import com.shuangshuan.cryptauth.authority.entity.RolePermission;
import com.shuangshuan.cryptauth.authority.repository.PermissionRepository;
import com.shuangshuan.cryptauth.authority.repository.RolePermissionRepository;
import com.shuangshuan.cryptauth.common.BusinessResponseCode;
import com.shuangshuan.cryptauth.security.util.SecurityUtils;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionServiceImpl implements PermissionService {

    private static final Logger logger = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Autowired
    private RolePermissionRepository rolePermissionsRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    // 获取所有权限点
    public List<Permission> findAllPermissions() {
        return permissionRepository.findAllActivePermissions();
    }

    // 根据 ID 查找权限点
    public Optional<Permission> findPermissionById(Integer id) {
        return permissionRepository.findById(id);
    }

    // 创建新的权限点
    @Transactional
    public Permission save(Permission permission) {
        String userName = SecurityUtils.getCurrentUsername();
        if (permission != null && permission.getId() == null) {
            permission.setCreatedBy(userName);
            permission.setDeleted(0);
        }
        permission.setUpdatedBy(userName);
        return permissionRepository.save(permission);
    }

    // 判断权限是否存在
    @Override
    public boolean existsById(Integer id) {
        return permissionRepository.existsById(id);
    }

    // 删除权限点
    @Transactional
    public void deleteById(Integer id) {
        permissionRepository.softDeleteById(id);
    }

    @Override
    public Optional<Permission> findPermissionByCode(String code) {
        return permissionRepository.findByCode(code);
    }

    /**
     * 为角色分配权限
     *
     * @param roleId  角色ID
     * @param permIds 权限ID列表
     */
    @Transactional
    public Boolean assignPermissionsToRole(Integer roleId, List<Integer> permIds) {
        // 查询/sys/details的permId，并加入permIds
        String path = "/sys/details";
        Optional<Permission> permission = permissionRepository.queryByPathEqualsIgnoreCase(path);
        if (permission.isPresent()) {
            Integer permId = permission.get().getId();
            if (!permIds.contains(permId)) {
                permIds.add(permId);  // 将 permId 加入 permIds 集合
            }
        }
        try {
            // 1. 删除角色已有的权限关联
            rolePermissionsRepository.deleteByRoleId(roleId);
            String userName = SecurityUtils.getCurrentUsername();
            // 2. 如果传递了权限ID列表，插入新的权限关联
            if (permIds != null && !permIds.isEmpty()) {
                List<RolePermission> rolePermissionsList = new ArrayList<>();
                for (Integer permId : permIds) {
                    RolePermission rolePermission = new RolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermId(permId);
                    rolePermission.setCreatedBy(userName);
                    rolePermission.setUpdatedBy(userName);
                    rolePermission.setDeleted(0);
                    rolePermissionsList.add(rolePermission);
                }

                // 批量保存角色与权限的关联
                rolePermissionsRepository.saveAll(rolePermissionsList);
            }

            // 返回 true，表示角色权限分配成功
            return true;
        } catch (Exception e) {
            // 处理异常并返回 false，表示分配权限失败
            logger.error("{}{}", roleId, BusinessResponseCode.PERMISSION_GRANTED_FAILED.getMessage());
            return false;
        }
    }
}
