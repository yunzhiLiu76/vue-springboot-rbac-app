package com.shuangshuan.scaffold.relationaldataaccess.mysql.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AddressBookDto {


    //用户id
    private Long userId;


    //收货人 或 雇员名称
    private String consignee;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //省级区划编号
    private String provinceCode;


    //省级名称
    private String provinceName;


    //市级区划编号
    private String cityCode;


    //市级名称
    private String cityName;


    //区级区划编号
    private String districtCode;


    //区级名称
    private String districtName;


    //详细地址
    private String detail;


    //标签 比如 公司、家
    private String label;

    //是否默认 0 否 1是
    private Integer isDefault;


    //是否删除

    private Integer isDeleted;
}
