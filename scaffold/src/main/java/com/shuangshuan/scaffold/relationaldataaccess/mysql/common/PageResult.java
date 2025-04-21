package com.shuangshuan.scaffold.relationaldataaccess.mysql.common;

import com.shuangshuan.scaffold.relationaldataaccess.mysql.dto.UserAddressBookDto;
import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult <T>{
    // todo 这里是在百度借鉴的，并没有在重量级的文档上看到，以后会做修改 不清楚它是否要实现序列化接口，所以未实现，并且没有重写equals和hashCode方法
    private Integer total;
    private Integer respCode;
    private String respMsg;
    private List<T> datas;

    public static <T> PageResult<T> success(long total, List<T> list) {
        return new PageResult(Math.toIntExact(total), CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), list);
    }

    public static <T> PageResult<T> success(Integer total, List<T> list) {
        return new PageResult(total, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), list);
    }

    public static <T> PageResult<T> success(Integer total, List<T> list,String msg) {
        return new PageResult(total, CodeEnum.SUCCESS.getCode(), msg, list);
    }

    public static <T> PageResult<T> fail( String msg) {
        return new PageResult(CodeEnum.FAILURE.getCode(), msg);
    }

    public static <T> PageResult<T> fail( ) {
        return new PageResult(CodeEnum.FAILURE.getCode(), CodeEnum.FAILURE.getMessage());
    }



    public Integer getTotal() {
        return this.total;
    }

    public Integer getrespCode() {
        return this.respCode;
    }

    public String getrespMsg() {
        return this.respMsg;
    }

    public List<T> getDatas() {
        return this.datas;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public void setrespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public void setrespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;

    }

    public PageResult() {
    }

    public PageResult(Integer respCode, String respMsg) {
        this.respCode = respCode;
        this.respMsg = respMsg;
    }

    public PageResult(Integer total, Integer respCode, String respMsg, List<T> datas) {
        this.total = total;
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.datas = datas;
    }



}
