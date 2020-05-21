package com.yilian.mall.utils;/**
 * Created by  on 2017/3/7 0007.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.yilian.mall.R;
import com.yilian.mylibrary.Ip;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by  on 2017/3/7 0007.
 */
public class ImgCodeUtil {
    public static void setImgCode(String phoneNumber, ImageView ivImgCode, Activity context) {
        ivImgCode.setBackgroundColor(ContextCompat.getColor(context,R.color.color_bg));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String url0 = Ip.getURL(context) + "api.php?c=ImageCode&width=82&height=38&count=4&phone=" + phoneNumber;
                    URL url = new URL(url0);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("POST");
                    if (200 == conn.getResponseCode()) {
                        InputStream inputStream = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        context.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                ivImgCode.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                    ivImgCode.setBackground(new BitmapDrawable(bitmap));
                                } else {
                                    ivImgCode.setBackgroundDrawable(new BitmapDrawable(bitmap));
                                }
                            }
                        });

                    } else {
                        ivImgCode.setBackgroundResource(R.mipmap.zhanwei);
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(context, "图片验证码获取失败,点击图片重新获取", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
}
