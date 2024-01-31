package com.magic.ssh.util;

import java.io.Serializable;

import lombok.Data;

@Data
public class Result implements Serializable {

    private static final long serialVersionUID = -1802122468331526708L;

    private Integer code;// 状态码
    private String msg;// 返回信息
    private Object data;// 返回数据

    //自定义code,msg,data
    private Result(Integer code, String msg, Object data) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    //自定义data
    private Result(Object data) {
        this.data = data;
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMessage();
    }

    //自定义code,msg
    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result() {
        this.code = ResultCode.SUCCESS.getCode();
        this.msg = ResultCode.SUCCESS.getMessage();
    }

    public static Result success() {
        return new Result();
    }

    public static Result success(ResultCode code, Object data) {
        return new Result(code.getCode(), code.getMessage(), data);
    }

    public static Result success(ResultCode code) {
        return new Result(code.getCode(), code.getMessage());
    }

    public static Result success(String msg) {
        return build(ResultCode.SUCCESS, msg, null);
    }

    public static Result success(Object data) {
        return success(ResultCode.SUCCESS, data);
    }

    public static Result build(ResultCode code) {
        return new Result(code.getCode(), code.getMessage());
    }
    public static Result build(ResultCode code,Object data) {
        return new Result(code.getCode(), code.getMessage(),data);
    }

    public static Result build(ResultCode code, String msg, Object data) {
        return new Result(code.getCode(), msg, data);
    }
}
