package com.shuangshuan.scaffold.relationaldataaccess.mysql.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "user_basic")
public class UserBasic {


    @Schema(description = "UserBasic ID")
    private Long id;
    @Schema(description = "name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserBasic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
