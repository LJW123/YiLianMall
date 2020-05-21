package com.yilian.mylibrary;/**
 * Created by  on 2017/3/23 0023.
 */

/**
 * Created by  on 2017/3/23 0023.
 */
public class WebImageUtil {

    private static WebImageUtil webImageUtil;
    private WebImageUtil() {
    }
    public static synchronized WebImageUtil getInstance(){
        if(webImageUtil==null){
            webImageUtil = new WebImageUtil();
        }
        return webImageUtil;
    }

    public  String getWebImageUrlWithSuffix(String localUrl){
        if (localUrl==null) {
            return "";
        }
        if(localUrl.contains("http://")||localUrl.contains("https://")){
            return localUrl;
        }
        return Constants.ImageUrl+localUrl+Constants.ImageSuffix;
    }

    public String getWebImageUrlNOSuffix(String localUrl){
        if (localUrl==null) {
            return "";
        }
        if(localUrl.contains("http://")||localUrl.contains("https://")){
            return localUrl;
        }
        return Constants.ImageUrl+localUrl;
    }
    public String getWebImageUrlSetSuffix(String localUrl, String width, String height) {
        if (localUrl==null) {
            return "";
        }
        if (localUrl.contains("http://") || localUrl.contains("https://")) {
            return localUrl;
        }
        return Constants.ImageUrl + localUrl + "?x-oss-process=image/resize,limit_0,w_" + width + ",h_" + height + ",m_fill";
    }
}
