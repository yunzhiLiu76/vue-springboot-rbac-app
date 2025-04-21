package com.shuangshuan.scaffold.relationaldataaccess.mysql.mapper;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

}
