package com.yilian.mall.utils;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yilian.mall.entity.BaseEntity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;


public class LefenHttpUtils {

    /**
     * HttpUtils一个单例对象，用于外向外界提供访问入口
     */
    private static HttpUtils httpUtils;
    @SuppressWarnings("unused")
    private final String TAG = "LefenHttpUtils";

    /**
     * @return HttpUtils 返回一个HttpUtils单例对象
     * @description: 获取单例HttpUtils对象
     */
    public static synchronized HttpUtils getSingleHttpUtils() {
        if (httpUtils == null) {
            httpUtils = new HttpUtils();
            // 设置超时时间
            httpUtils.configCurrentHttpCacheExpiry(1000 * 10);
        }
        return httpUtils;
    }

//	/**
//	 * 
//	 * @description:获取httpUtils对象
//	 * @return
//	 * HttpUtils
//	 */
//	private static HttpUtils getHttpUtils()
//	{
//		HttpUtils https = new HttpUtils();
//		// 设置超时时间
//		https.configCurrentHttpCacheExpiry(1000 * 10);
//		return https;
//	}

    /**
     * @param url       请求地址
     * @param params    请求参数封装
     * @param icallBack 任务完成后的接口回调
     * @return void
     * @description: 向服务器发送一个post请求
     */
    public static void post(Context context, String url, RequestParams params,
                            final RequestCallBack<String> icallBack) {

        if (!checkNet(context)) {
            icallBack.onFailure(new HttpException(), "当前网络不可用");
            return;
        }
        HttpUtils https = getSingleHttpUtils();//getHttpUtils();

        https.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                icallBack.onSuccess(responseInfo);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                icallBack.onFailure(error, msg);

            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {

            }
        });

    }

    /**
     * @param context
     * @param cls       返回数据类型定义
     * @param url       接口地址     * @param headers   http header参数
     * @param params    接口参数定义
     * @param icallBack void
     * @description:post方式调接口,有header参数
     */
    public static <T> void post(final Context context, final Class<T> cls, String url, Header[] headers, RequestParams params, final RequestCallBack<T> icallBack) {
        if (!checkNet(context)) {
            icallBack.onFailure(new HttpException(), "当前网络不可用。");
            return;
        }
        HttpUtils https = getSingleHttpUtils();//getHttpUtils();
        https.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                icallBack.onStart();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                icallBack.onFailure(error, msg);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                icallBack.onLoading(total, current, isUploading);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseEntity be;
                try {
                    ResponseInfo<T> ri = (ResponseInfo<T>) responseInfo;
                    be = (BaseEntity) cls.newInstance();
                    Logger.i("请求结果:" + responseInfo.result);
                    be = be.parserT(responseInfo.result);
                    ri.result = (T) be;
                    icallBack.onSuccess(ri);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    icallBack.onFailure(new HttpException("JsonSyntaxException"),ex.toString());
                    Logger.i("这里报错了:"+cls.getSimpleName()+"错误信息"+ex.toString());

                }
            }
        });
    }

    /**
     * @param url       请求地址
     * @param params    请求参数封装
     * @param icallBack 任务完成后的接口回调
     * @return void
     * @description: 向服务器发送一个get请求
     */
    public static void get(Context context, String url, RequestParams params,
                           final RequestCallBack<String> icallBack) {

        if (!checkNet(context)) {
            icallBack.onFailure(new HttpException(), "当前网络不可用");
            return;
        }
        HttpUtils https = getSingleHttpUtils();//getHttpUtils();
        https.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                icallBack.onSuccess(responseInfo);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                icallBack.onFailure(error, msg);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {

            }
        });

    }

    /**
     * @param context
     * @param cls       返回数据类型定义
     * @param url       接口地址
     * @param headers   http header参数
     * @param params    接口参数定义
     * @param icallBack void
     * @description:post方式调接口,有header参数
     */
    public static <T> void get(Context context, final Class<T> cls, String url, Header[] headers, RequestParams params, final RequestCallBack<T> icallBack) {
        if (!checkNet(context)) {
            icallBack.onFailure(new HttpException(), "当前网络不可用。");
            return;
        }
        HttpUtils https = getSingleHttpUtils();//getHttpUtils();
        https.send(HttpMethod.GET, url, params, new RequestCallBack<String>() {


            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                icallBack.onStart();
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                icallBack.onFailure(error, msg);
            }

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                icallBack.onLoading(total, current, isUploading);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                BaseEntity be;
                try {
                    ResponseInfo<T> ri = (ResponseInfo<T>) responseInfo;
                    be = (BaseEntity) cls.newInstance();
                    be = be.parserT(responseInfo.result);
                    ri.result = (T) be;
                    icallBack.onSuccess(ri);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

        });
    }

    /**
     * @param context
     * @return boolean
     * @description 检查网络是否可用
     */
    private static boolean checkNet(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();

                if (netInfo != null && netInfo.isAvailable()
                        && netInfo.isConnected()) {
                    return true;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return false;
    }

    /**
     * @param context   上下文
     * @param url       上传地址
     * @param filePath  本地文件地址
     * @param icallBack 连网服务器结果回调 void
     * @description: 文件上传
     */
    public void upload(Context context, String url, String filePath,
                       final RequestCallBack<String> icallBack) {

        RequestParams params = new RequestParams();
        params.addQueryStringParameter("method", "upload");

        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, url, params,
                new RequestCallBack<String>() {

                    @Override
                    public void onStart() {
                        icallBack.onStart();
                    }

                    @Override
                    public void onLoading(long total, long current,
                                          boolean isUploading) {
                        if (isUploading) {
                        } else {
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        icallBack.onSuccess(responseInfo);
                    }

                    @Override
                    public void onFailure(HttpException error, String msg) {
                        icallBack.onFailure(error, msg);
                    }
                });

    }

}
