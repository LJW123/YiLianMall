package com.yilian.mylibrary;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Ray_L_Pain on 2018/1/20 0020.
 */

public class IDCardResult {
    /**
     * 错误消息，为空时，代表验证通过
     */
    private String error;


    public boolean isLegal() {
        //两个变量为默认值,即认为是合法的
        return error == null || error.equals("");
    }

    public String getError() {
        return error;
    }

    public void setError(String message) {
        this.error = message;
    }

    public void show(Context context) {
        if (!isLegal()) {
            Toast.makeText(context, error, Toast.LENGTH_SHORT).show();
        }
    }
}
