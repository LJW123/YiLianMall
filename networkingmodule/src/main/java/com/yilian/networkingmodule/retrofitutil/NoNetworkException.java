package com.yilian.networkingmodule.retrofitutil;

import java.io.IOException;

/**
 * Created by  on 2017/8/16 0016.
 */

public class NoNetworkException extends IOException {
    @Override
    public String getMessage() {
        return "网络异常,请检查后重试!!";
    }
}
