package com.leshan.ylyj.utils;/**
 * Created by  on 2017/3/3 0003.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.leshan.ylyj.view.activity.bankmodel.JRegionModel;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.networkingmodule.BuildConfig;
import com.yilian.networkingmodule.entity.UploadImageEnity;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rxfamily.entity.JBankModel;

/**
 * Created by  on 2017/3/3 0003.
 */
public class RetrofitUtils {

    private static RetrofitUtils retrofitUtils;
    private Context context;
    private static Retrofit retrofit;
    private static RetrofitService retrofitService;
    private SharedPreferences sp;
    private String deviceIndex;
    private String token;

    private RetrofitUtils() {

    }

    public static RetrofitUtils getInstance(Context context) {
        if (retrofitUtils == null || retrofitService == null) {
            synchronized (RetrofitUtils.class) {
                if (retrofitUtils == null) {
                    HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
                    if (BuildConfig.DEBUG) {
                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                    } else {
                        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
                    }
                    OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//                httpClient.connectTimeout(5, TimeUnit.SECONDS);
// add your other interceptors …
// add logging as last interceptor
                    httpClient.addInterceptor(logging);  // <-- this is the important line!
                    retrofitUtils = new RetrofitUtils();
                    retrofit = new Retrofit.Builder()
                            .baseUrl(Ip.getURL(context))
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(httpClient.build())
                            .build();
                }
                if (retrofitService == null) {
                    retrofitService = retrofit.create(RetrofitService.class);
                }
            }
        }
        return retrofitUtils;
    }

    public RetrofitUtils setContext(Context context) throws Exception {
        if (context == null) {
            throw new Exception("RetrofitUtils mContext is null");
        }
        this.context = context;
        sp = context.getSharedPreferences(Constants.SP_FILE, Context.MODE_PRIVATE);
        deviceIndex = RequestOftenKey.getDeviceIndex(context);
        token = RequestOftenKey.getToken(context);
        return retrofitUtils;
    }

    /**
     * 登录阿里客服
     * 该方法必须在获取token，并将token存储到本地sp后使用
     * 多出登录操作，Callback中执行的操作是相同的，所以Callback不作为参数传递过来，而是直接在该方法中new
     *
     * @throws Exception
     */
//    public void getAliAccountInfo() throws Exception {
//
//
//        if (context == null) {
//            throw new Exception("RetrofitUtils mContext is null");
//        } else {
//            Logger.i("获取百川信息使用的token："+token+"   RequestOftenToken:"+ com.yilian.mylibrary.RequestOftenKey.getToken(context));
//            Call<AliLoginUserInfo> loginAliIMCall = retrofitService.getAliAccountInfo("chat/user_add", deviceIndex, token);
//            loginAliIMCall.enqueue(new Callback<AliLoginUserInfo>() {
//                @Override
//                public void onResponse(Call<AliLoginUserInfo> call, Response<AliLoginUserInfo> response) {
//                    AliLoginUserInfo body = response.body();
////                    Logger.i("Login ali info:" + body.toString());
//                    if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
//                        AliLoginUserInfo.DataBean data = body.data;
//                        String password = data.password;
//                        String userid = data.userid;//ALi userId
//                        Logger.i("登录获取ALIuserid:" + userid + "登录获取ALIpassword:" + password);
////                                将Ali UserId存储为sp值，以便后续使用
//                        sp.edit().putString(Constants.ALI_USER_ID, userid).commit();
//                        //此对象获取到后，保存为全局对象，供APP使用
//                        //此对象跟用户相关，如果切换了用户，需要重新获取
//                        YWIMKit mIMKit = YWAPI.getIMKitInstance(userid, Constants.ALI_APP_KEY);
//                        if (mIMKit != null) {
//                            MyApplication.getInstance().setYWIMKit(mIMKit);
//                            Logger.i("不空");
//                        } else {
//                            Logger.i("空啊空");
//                        }
//                        /**
//                         * 登录阿里
//                         */
//                        IYWLoginService loginService = null;
//                        try {
//                            loginService = MyApplication.getInstance().getYWIMKit().getLoginService();
//                        } catch (Exception e) {
//                            Logger.i("ali loginByPassWord " + e.getMessage());
//                            e.printStackTrace();
//                        }
//                        YWLoginParam loginParam = YWLoginParam.createLoginParam(userid, password);
//                        loginService.login(loginParam, new IWxCallback() {
//                            @Override
//                            public void onSuccess(Object... arg0) {
//                                Logger.i("Ali loginByPassWord success:" + arg0.toString());
//                            }
//
//                            @Override
//                            public void onProgress(int arg0) {
//                                // TODO Auto-generated method stub
//                            }
//
//                            @Override
//                            public void onError(int errCode, String description) {
//                                //如果登录失败，errCode为错误码,description是错误的具体描述信息
//                                Logger.i("Ali loginByPassWord error" + errCode + "   detail info:" + description);
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AliLoginUserInfo> call, Throwable t) {
//                    Logger.i("Login aLi error" + t.getMessage());
//                }
//            });
//        }
//    }

    /**
     * 退出阿里客服账号
     */
//    public void logOutAli() {
//        try {
//            MyApplication.getInstance().getYWIMKit().getLoginService().logout(new IWxCallback() {
//                @Override
//                public void onSuccess(Object... objects) {
//                    Logger.i("ali logout success");
//                }
//
//                @Override
//                public void onError(int i, String s) {
//                    Logger.i("ali logout error + " + s);
//                }
//
//                @Override
//                public void onProgress(int i) {
//
//                }
//            });
//        } catch (Exception e) {
//            Logger.i("ali logout " + e.getMessage());
//            e.printStackTrace();
//        }
//    }



//    public void getBinkInfo(Callback<BankInfoModel> callback) {
//        retrofitService.getBinkInfo("get_bank_info", token, deviceIndex).enqueue(callback);
//    }
//
//    public void bindCard(String bankName, String userName, String cardNum, String cardBranch, String provinceId, String cityId, String countyId,
//                         String phone, String code, Callback<BaseEntity> callback) {
//        retrofitService.bindCard("add_bank_card", token, deviceIndex, bankName, userName, cardNum, cardBranch, provinceId, cityId, countyId, phone, code).enqueue(callback);
//    }

    public void getBankList(Callback<JBankModel> callback) {
        retrofitService.getBankList("get_bank_list").enqueue(callback);
    }

    public void getRegion(Callback<JRegionModel> callback) {
        retrofitService.getRegion("getRegion").enqueue(callback);
    }



    /**
     * 绑定对工卡-上传身份证信息
     */
    public void uploadFilePublic(MultipartBody.Part image, Callback<UploadImageEnity> callback) {
        retrofitService.uploadFilePublic("upload_cert_img", token, deviceIndex, image).enqueue(callback);
    }


}
