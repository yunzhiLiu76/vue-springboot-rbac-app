package com.shuangshuan.cryptauth.security.repository;

import com.shuangshuan.cryptauth.security.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


public interface UserRepository extends JpaRepository<UserAccount, Integer> {

    // 根据 username 查找用户
    Optional<UserAccount> findByUsername(String username);

    // 根据用户名查找用户
    boolean existsByUsername(String username);

    // 判断角色是否存在
    boolean existsById(Integer id);

    @Query("SELECT r FROM UserAccount r WHERE r.deleted = 0")
    Page<UserAccount> findAllActiveUsers(Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE UserAccount p SET p.deleted = 1 WHERE p.id = :id")
    void softDeleteById(@Param("id") Integer id);
}
