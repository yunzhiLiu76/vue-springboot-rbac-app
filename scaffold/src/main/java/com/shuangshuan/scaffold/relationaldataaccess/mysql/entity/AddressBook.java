package com.shuangshuan.scaffold.relationaldataaccess.mysql.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "address_book")
public class AddressBook {
    // todo 记录 after-saving-the-identifier-must-not-be-null-error 报错，在这里加上@Id即可
    @Schema(description = "AddressBook ID")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Schema(description = "用户id")
    //用户id
    private Long userId;

    @Schema(description = "收货人 或 雇员名称")
    //收货人 或 雇员名称
    private String consignee;


    //手机号
    @Schema(description = "手机号")
    private String phone;


    //性别 0 女 1 男
    @Schema(description = "性别 0 女 1 男")
    private String sex;


    //省级区划编号
    @Schema(description = "省级区划编号")
    private String provinceCode;


    //省级名称
    @Schema(description = "Username")
    private String provinceName;


    //市级区划编号
    @Schema(description = "省级名称")
    private String cityCode;


    //市级名称
    @Schema(description = "市级名称")
    private String cityName;


    //区级区划编号
    @Schema(description = "区级区划编号")
    private String districtCode;


    //区级名称
    @Schema(description = "区级名称")
    private String districtName;


    //详细地址
    @Schema(description = "详细地址")
    private String detail;


    //标签 比如 公司、家
    @Schema(description = "标签 比如 公司、家")
    private String label;

    // todo 将数据类型从tinyint(1)改为int 或将tinyint(1)改为tinyint(4)  No converter found capable of converting from type [java.lang.Boolean] to type [java.lang.Integer] 先百度，没有找到答案。然后认为是
    // todo 转换器的问题，翻看spring官网没有找到相应的内容 最后还是debug,发现是数据类型的问题 Mysql中，如果使用tinyint来设置字段的数据类型，映射到Java数据类型中，不仅可以使用上面的Boolean类型来接收，也可以使用Java中int类型来接收。在MySQL中存储的tinyint(1)类型数据，不仅可以存储0和1，任意一个一位自然数都可以（0-9）。不过，当这样（tinyint(1)）使用时,0映射为Java中的Boolean类型false，1-9数字都将映射为true
    //是否默认 0 否 1是
    @Schema(description = "是否默认 0 否 1是")
    private Integer isDefault;

    //创建时间 todo 这里没有加入类似于@TableField(fill = FieldFill.INSERT)的代码，以后可以改进
    @Schema(description = "创建时间")
    private LocalDateTime createTime;


    //更新时间
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;


    //创建人
    @Schema(description = "创建人")
    private Long createUser;


    //修改人
    @Schema(description = "修改人")
    private Long updateUser;


    //是否删除
    @Schema(description = "是否删除")
    private Integer isDeleted;

}
