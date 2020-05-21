package com.yilian.networkingmodule.retrofitutil;

import java.io.IOException;

/**
 * Created by  on 2017/8/14 0014.
 */

public class ResultException extends IOException {
    public final String errMsg;
    public final int errCode;

    public ResultException(String message, int errCode) {
        this.errMsg = message;
        this.errCode = errCode;
    }
}
