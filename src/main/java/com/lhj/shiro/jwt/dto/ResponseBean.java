package com.lhj.shiro.jwt.dto;

import lombok.Data;

@Data
public class ResponseBean<T> {

    private int code;

    private String msg;

    private T data;

    private ResponseBean(int code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResponseBean success(){
        return new ResponseBean<T>(200, "success", null);
    }

    public static <T> ResponseBean success(String msg){
        return new ResponseBean<T>(200, msg, null);
    }

    public static <T> ResponseBean success(T data){
        return new ResponseBean<T>(200, "success", data);
    }

    public static <T> ResponseBean success(String msg, T data){
        return new ResponseBean<T>(200, msg, data);
    }

    public static <T> ResponseBean fail(){
        return new ResponseBean<T>(500, "failed", null);
    }

    public static <T> ResponseBean fail(String msg){
        return new ResponseBean<T>(500, msg, null);
    }

    public static <T> ResponseBean fail(T data){
        return new ResponseBean<T>(500, "failed", data);
    }

    public static <T> ResponseBean fail(String msg, T data){
        return new ResponseBean<T>(500, msg, data);
    }

}
