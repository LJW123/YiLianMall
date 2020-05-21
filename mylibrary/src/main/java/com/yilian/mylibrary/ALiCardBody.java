package com.yilian.mylibrary;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * @author Created by  on 2018/1/20.
 */

public class ALiCardBody {

    private static ALiCardBody aLiCardBody;

    private ALiCardBody() {
    }

    public static ALiCardBody getInstance() {
        if (aLiCardBody == null) {
            synchronized (ALiCardBody.class) {
                if (aLiCardBody == null) {
                    aLiCardBody = new ALiCardBody();
                }
            }
        }
        return aLiCardBody;
    }

    public Map<String, String> getIdCardRequestHeader() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "APPCODE " + Constants.OCR_IDCARD_APP_CODE);
        headerMap.put("Content-Type", "application/json; charset=UTF-8");
        return headerMap;
    }
    //驾驶证
    public Map<String, String> getDriverCarRequestHeader() {
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "APPCODE " +  Constants.OCR_DRIVER_LICENSE);
        headerMap.put("Content-Type", "application/json; charset=UTF-8");
        return headerMap;
    }
    public RequestBody getIdCardRequestBody(String base64Str, String side) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("Content-Type, application/json"),
                "{\n" +
                        "    \"inputs\": [\n" +
                        "        {\n" +
                        "            \"image\": {\n" +
                        "                \"dataType\": 50,\n" +
                        "                \"dataValue\": \"" + base64Str + "\"    \n" +
                        "            },\n" +
                        "            \"configure\": {\n" +
                        "                \"dataType\": 50,\n" +
                        "                \"dataValue\": \"{\\\"side\\\":\\\"" + side + "\\\"}\"\n" +
                        "            }\n" +
                        "        }\n" +
                        "    ]\n" +
                        "}");
        return requestBody;
    }
}
