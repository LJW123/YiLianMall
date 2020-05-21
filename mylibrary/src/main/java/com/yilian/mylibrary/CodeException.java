package com.yilian.mylibrary;

/**
 * @author Created by  on 2018/1/19.
 */

public class CodeException extends RuntimeException {
    public CodeException(String message, int code) {
        super(message);
        this.message = message;
        this.code = code;
    }

    @Override
    public String toString() {
        return "CodeException{" +
                "code=" + code +
                "message=" + message +
                '}';
    }

    private String message;
    public int code;
}
