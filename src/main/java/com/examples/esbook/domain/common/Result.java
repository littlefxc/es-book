package com.examples.esbook.domain.common;

/**
 * @author fengxuechao
 * @version 0.1
 * @date 2019/8/5
 */
public class Result {

    public static Result error() {
        return null;
    }

    public static Result ok(Object data) {
        return null;
    }

    public static Result error(String msg) {
        return null;
    }

    public static Result ok() {
        return null;
    }
}
