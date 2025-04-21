package com.shuangshuan.cryptauth.security.service;

import com.shuangshuan.cryptauth.authority.entity.Permission;
import com.shuangshuan.cryptauth.authority.entity.RolePermission;
import com.shuangshuan.cryptauth.authority.entity.UserRole;
import com.shuangshuan.cryptauth.authority.repository.PermissionRepository;
import com.shuangshuan.cryptauth.authority.repository.RolePermissionRepository;
import com.shuangshuan.cryptauth.authority.repository.UserRoleRepository;
import com.shuangshuan.cryptauth.security.entity.UserAccount;
import com.shuangshuan.cryptauth.security.repository.UserRepository;
import com.shuangshuan.cryptauth.security.response.RoleDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserAccountServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserAccount user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Permission> permissions =getAllPermissionsByUserId(user.getId());

        // 生成权限集合
        Set<GrantedAuthority> authorities = new HashSet<>();
        for (Permission permission : permissions) {
            if (permission.getPath() != null && !permission.getPath().isEmpty()) {
                // 添加权限作为 GrantedAuthority
                authorities.add(new SimpleGrantedAuthority(permission.getPath()));
            }
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }

    private List<Permission> getAllPermissionsByUserId(Integer id) {

        List<Integer> roleIds =getUserRoleIds(id);

        // 遍历这些角色id，查询出这些角色id对应的权限id 并去重
        List<RolePermission> rolePermissionList = rolePermissionRepository.findPermissionIdByRoleIdIn(roleIds);
        Set<Integer> permissionIds = rolePermissionList.stream()
                .map(RolePermission::getPermId) // 提取 id
                .collect(Collectors.toSet()); // 将结果收集到 Set 中，自动去重
        // 根据权限ID查询权限信息
        return permissionRepository.findByIdIn(new ArrayList<>(permissionIds));

    }

    private List<Integer> getUserRoleIds(Integer id) {
        // 查看下这个用户下所有的角色id
        List<UserRole> roles = userRoleRepository.findRoleIdByUserId(id);
        // 提取 UserRole 中的 id，并放入一个新的 List<Integer>
        return roles.stream()
                .map(UserRole::getRoleId)  // 提取每个 UserRole 的 id
                .collect(Collectors.toList());  // 将结果收集到 List 中
    }

    public RoleDetails queryUserPermissionDetails(UserAccount user) {
        List<Integer> roles=getUserRoleIds(user.getId());
        List<Permission> permissions =getAllPermissionsByUserId(user.getId());
        List<String> menuList = new ArrayList<>();
        List<String> pointList = new ArrayList<>();
        // 遍历permissions，如果是1为页面路由权限点, 2为按钮权限点，分别加入menu和points
        for (Permission permission : permissions) {
            if(permission.getType()==1) {
                menuList.add(permission.getCode());
            }
            if(permission.getType()==2) {
                pointList.add(permission.getCode());

            }

        }
        // 转换为数组
        String[] menus = menuList.toArray(new String[0]);
        String[] points = pointList.toArray(new String[0]);

        // 构造 RoleDetails 对象
        RoleDetails roleDetails = new RoleDetails();
        roleDetails.setMenus(menus);
        roleDetails.setPoints(points);
        roleDetails.setRoles(roles);
        return roleDetails;
    }


    public Page<UserAccount> findAllUsers(Pageable pageable) {
        return userRepository.findAllActiveUsers(pageable);
    }

    public void deleteById(Integer id) {
        userRepository.softDeleteById(id);

    }
}