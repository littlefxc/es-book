package com.examples.esbook.domain.common;

import lombok.Data;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
@Data
public class Result<T> {

    private Integer code;

    private String msg;

    private T data;

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static Result error() {
        return new Result(0, "Failure");
    }

    public static Result ok(Object data) {
        return new Result(200, "Success", data);
    }

    public static Result error(String msg) {
        return new Result(0, msg);
    }

    public static Result ok() {
        return new Result(200, "Success");
    }
}
