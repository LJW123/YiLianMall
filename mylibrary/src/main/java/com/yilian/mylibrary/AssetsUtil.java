package com.yilian.mylibrary;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author xiaofo on 2018/9/27.
 */

public class AssetsUtil {

    public static String getJson(Context context, String jsonPath) {
        String json = null;
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(jsonPath);

            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            json = new String(buffer, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }
}
