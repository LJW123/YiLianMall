package com.yilian.mall.http;

import android.content.Context;

import com.orhanobut.logger.Logger;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuyuqi on 2017/2/22 0022.
 */
public class IdCardNetRequest {

    private final Context context;

    public IdCardNetRequest(Context mContext) {
        context =mContext;
    }

    //用过阿里云对比身份证号码
    public static void switchIdCardFormAliyun(String imgString, String IDtype){

        String host="https://dm-51.data.aliyun.com";
        String path="/rest/160601/ocr_idcard.json";
        String method="POST";
        Map<String,String> headers=new HashMap<>();

        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE："  "
        headers.put("Authorization", "APPCODE ff9c764da2dd4c46bd4ad3a6019a5227");
        Map<String,String> querys=new HashMap<>();
        String bodys="{\"inputs\":[{\"image\":{\"dataType\":50,\"dataValue\":\"base64_image_string"+imgString+"\"},\"configure\":" +
                "{\"dataType\":50,\"dataValue\":\"{\\\"side\\\":\\\"face"+IDtype+"\\\"}\"}}]}";

        HttpResponse httpResponse = null;
        try {
//            httpResponse = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            Logger.i("response内容 "+httpResponse.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
