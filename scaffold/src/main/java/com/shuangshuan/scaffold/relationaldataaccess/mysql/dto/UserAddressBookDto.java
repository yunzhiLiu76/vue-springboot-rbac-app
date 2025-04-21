package com.shuangshuan.scaffold.relationaldataaccess.mysql.dto;




import com.shuangshuan.scaffold.relationaldataaccess.mysql.entity.AddressBook;
import lombok.Data;

import java.util.List;

@Data
public class UserAddressBookDto {

    //姓名
    private String name;


    //手机号
    private String phone;


    //性别 0 女 1 男
    private String sex;


    //身份证号
    private String idNumber;


    //头像
    private String avatar;


    private List<AddressBook> addressBooklist;

}
