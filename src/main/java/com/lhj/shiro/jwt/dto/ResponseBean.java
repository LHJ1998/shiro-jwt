package com.lhj.shiro.jwt.dto;

import com.lhj.shiro.jwt.common.CommonResultCode;
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
        return new ResponseBean<T>(CommonResultCode.SUCCESS_CODE, "success", null);
    }

    public static <T> ResponseBean success(String msg){
        return new ResponseBean<T>(CommonResultCode.SUCCESS_CODE, msg, null);
    }

    public static <T> ResponseBean success(T data){
        return new ResponseBean<T>(CommonResultCode.SUCCESS_CODE, "success", data);
    }

    public static <T> ResponseBean success(String msg, T data){
        return new ResponseBean<T>(CommonResultCode.SUCCESS_CODE, msg, data);
    }

    public static <T> ResponseBean fail(){
        return new ResponseBean<T>(CommonResultCode.OPERATING_FAILED, "failed", null);
    }

    public static <T> ResponseBean fail(String msg){
        return new ResponseBean<T>(CommonResultCode.OPERATING_FAILED, msg, null);
    }

    public static <T> ResponseBean fail(T data){
        return new ResponseBean<T>(CommonResultCode.OPERATING_FAILED, "failed", data);
    }

    public static <T> ResponseBean fail(String msg, T data){
        return new ResponseBean<T>(CommonResultCode.OPERATING_FAILED, msg, data);
    }

    public static <T> ResponseBean fail(int code){
        return new ResponseBean<T>(code, "failed", null);
    }

    public static <T> ResponseBean fail(int code, String msg){
        return new ResponseBean<T>(code, msg, null);
    }

    public static <T> ResponseBean fail(int code, T data){
        return new ResponseBean<T>(code, "failed", data);
    }

    public static <T> ResponseBean fail(int code, String msg, T data){
        return new ResponseBean<T>(code, msg, data);
    }

}
