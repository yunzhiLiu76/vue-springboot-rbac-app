package com.shuangshuan.scaffold.relationaldataaccess.mysql.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "book")
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(description = "Book ID")
    private Long id;


    //用户id
    @Schema(description = "用户id")
    private Long userId;


    //收货人 或 雇员名称
    @Schema(description = "收货人 或 雇员名称")
    private String consignee;
}
