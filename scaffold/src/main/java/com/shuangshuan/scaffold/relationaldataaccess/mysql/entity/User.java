package com.shuangshuan.scaffold.relationaldataaccess.mysql.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "user_info")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "User ID")
    private Long id;


    //姓名
    @Schema(description = "姓名")
    private String name;


    //手机号
    @Schema(description = "手机号")
    private String phone;


    //性别 0 女 1 男
    @Schema(description = "性别 0 女 1 男")
    private String sex;


    //身份证号
    @Schema(description = "身份证号")
    private String idNumber;


    //头像
    @Schema(description = "头像")
    private String avatar;


    //状态 0:禁用，1:正常
    @Schema(description = "状态 0:禁用，1:正常")
    private Integer status;
}
