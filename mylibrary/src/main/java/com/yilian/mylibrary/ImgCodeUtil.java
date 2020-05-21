package com.yilian.mylibrary;/**
 * Created by  on 2017/3/7 0007.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by  on 2017/3/7 0007.
 */
public class ImgCodeUtil {
    /**
     * 刷新图形验证码
     *
     * @param phoneNumber 对应手机号
     * @param ivImgCode   显示图形验证码的ImageView
     * @param context
     */
    public static void setImgCode(final String phoneNumber, final ImageView ivImgCode, final Activity context) {
        ivImgCode.setBackgroundColor(ContextCompat.getColor(context,R.color.library_module_color_bg));
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //更新图形验证码获取的接口
                    String url0 = Ip.getURL(context) + "account.php?c=ImageCode&width=50&height=20&count=4&phone=" + phoneNumber;
                    Logger.i("uro10  "+url0);
                    URL url = new URL(url0);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setConnectTimeout(3000);
                    conn.setRequestMethod("POST");
                    if (200 == conn.getResponseCode()) {
                        InputStream inputStream = conn.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
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
                        context.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivImgCode.setBackgroundResource(R.mipmap.library_module_default_jp);
                                context.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(context, "图片验证码获取失败,点击图片重新后去", Toast.LENGTH_LONG).show();
                                    }
                                });
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
