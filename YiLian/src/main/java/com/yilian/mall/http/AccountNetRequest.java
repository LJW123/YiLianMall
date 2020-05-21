package com.yilian.mall.http;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonObject;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.entity.AliLoginUserInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.CityList;
import com.yilian.mall.entity.CountyList;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.entity.Location;
import com.yilian.mall.entity.MemberRelationSystemEntity;
import com.yilian.mall.entity.ObtainIntegral;
import com.yilian.mall.entity.ProvinceList;
import com.yilian.mall.entity.RegisterAccount;
import com.yilian.mall.entity.RegisterDevice;
import com.yilian.mall.entity.Start_app;
import com.yilian.mall.entity.Submit_card_info;
import com.yilian.mall.entity.TokenEntity;
import com.yilian.mall.entity.UploadImageData;
import com.yilian.mall.entity.WXBindPhoneEntity;
import com.yilian.mall.utils.PhoneInfor;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.Ip;

public class AccountNetRequest extends BaseNetRequest {

    private  String URL;
    Context context;

    public AccountNetRequest(Context mContext) {
        super(mContext);
        // TODO Auto-generated constructor stub
        this.context = mContext;
        URL = Ip.getURL(context) + "account.php";
        Logger.i("获取的URL：3" + URL);
    }

    /**
     * app每次启动时调用
     *@param appVersionCode  versionCode
     * @param gapTime  本次启动间隔时间（当前时间-上次退出时间,单位：秒）
     *                 osType //设备操作系统类型,0代表安卓，1代表IOS
     *                 token （token+gap_time做数学运算）（没有登录可以没有或值为空字符串）
     * @param pushId   客户端当前的推送id
     * @param callBack
     */
    public void appStart(String appVersionCode,String gapTime, String pushId, Class<Start_app> cls, RequestCallBack<Start_app> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "start_app");
        params.addBodyParameter("token", RequestOftenKey.gettoken(mContext));//此处使用不加盐值的token
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("os_type", "0");
        params.addBodyParameter("push_id", pushId);
        params.addBodyParameter("device_token", "0");
        params.addBodyParameter("gap_time", gapTime);
        params.addBodyParameter("app_version", appVersionCode);

        Logger.i("startApp  参数  token "+ RequestOftenKey.gettoken(mContext)+" device_index  "+RequestOftenKey.getDeviceIndex(mContext)
                +"  ostype :0 "+"   push_id   "+pushId+"  gapTime  "+gapTime+"  appversion  "+appVersionCode);
        postRequest(URL, params, cls, callBack);
    }


    /**
     * 新设备注册,设备首次安装app时调用
     * <p>
     * 请求参数
     * <p>
     * "uuid":"",//设备的唯一ID
     * "os_type":"",//设备操作系统类型,0代表安卓，1代表IOS
     * "os_version":"",//操作系统版本
     * "screen_size":"",//设备屏幕尺寸
     * "screen_dpi":"",//设备分辨率
     * "device_brand":"",//设备品牌
     * "device_model":"",//设备型号
     * "device_cpu":"",//设备cpu型号
     * "device_ram":"",//设备内存型号或大小
     * <p>
     * 返回参数
     * <p>
     * "code":"",//服务器处理结果(1/0/-3)
     * "device_index":""//返回设备编号
     */
    public void registerDevice(int scrWidth, int scrHeight, Class<RegisterDevice> cls, RequestCallBack<RegisterDevice> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "register_new_device");
        params.addBodyParameter("uuid", PhoneInfor.getIMEI(mContext));
        params.addBodyParameter("os_type", "0");
        params.addBodyParameter("os_version", PhoneInfor.getOSVersion());
        params.addBodyParameter("screen_size", "0");
        params.addBodyParameter("screen_dpi", scrWidth + "×" + scrHeight);
        params.addBodyParameter("device_brand", PhoneInfor.getDeviceBrand());
        params.addBodyParameter("device_model", PhoneInfor.getDeviceModel());
        params.addBodyParameter("device_cpu", "0");
        params.addBodyParameter("device_ram", String.valueOf((PhoneInfor.getTotalMemory(mContext) / 1000 / 1000 / 1000 + 1)));
Logger.i("注册设备参数: uuid:"+PhoneInfor.getIMEI(mContext)+" os_version:"+PhoneInfor.getOSVersion()+"  screen_dpi:"+scrWidth + "×" + scrHeight+"  " +
        "device_brand"+PhoneInfor.getDeviceBrand()+" device_model:"+PhoneInfor.getDeviceModel()+"  device_ram:"+String.valueOf((PhoneInfor.getTotalMemory(mContext) / 1000 / 1000 / 1000 + 1)));
        postRequest(URL, params, cls, callBack);
        Logger.i("获取的URL：4" + URL);
    }

    /**
     * 获取手机短信验证码
     *
     * @param phone      用户填写的手机号
     * @param verifyType 请求验证码的类型，0表示注册验证，1表示快捷登录验证，2表示账号操作安全验证，3表示未登录 4表示手机号码以及第三方账号互相绑定
     * @param cls
     * @param callBack   * 	"device_index":""//客户端运行设备编号
     *                   返回参数
     *                   <p>
     *                   "code":"",//服务器处理结果(1/0/-1/-2/-6)
     *                   ，-3表示发送失败（可重试）
     */
    public void getSmsVerifyCodeNew(String phone, int verifyType, String verifyCode, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_SMS_code_v2");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("verify_type", String.valueOf(verifyType));
        params.addBodyParameter("verify_code", verifyCode);
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
        Logger.i("获取验证码参数： phone:" + phone + "  verifyType:" + verifyType + "  verifyCode:" + verifyCode + "  deviceIndex:" + RequestOftenKey.getDeviceIndex(mContext)
                + "token:" + RequestOftenKey.getToken(mContext));
        Logger.i("获取语音验证码" + "params" + params.toString());
    }

    /**
     * 获取手机语音短信验证码
     *
     * @param phone      用户填写的手机号
     * @param verifyType 请求验证码的类型，0表示注册验证，1表示快捷登录验证，2表示账号操作安全验证
     * @param cls
     * @param callBack   * 	"device_index":""//客户端运行设备编号
     *                   返回参数
     *                   <p>
     *                   "code":"",//服务器处理结果(1/0/-1/-2/-6)
     *                   ，-3表示发送失败（可重试）
     */
    public void getSmsVerifyVoiceNew(String phone, int verifyType, String verifyCode, int voice, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_SMS_code_v2");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("verify_type", String.valueOf(verifyType));
        params.addBodyParameter("verify_code", verifyCode);
        params.addBodyParameter("voice", String.valueOf(voice));
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
        Logger.i("获取语音验证码" + "params" + params.toString());
    }

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     *              返回参数
     *              "code":""//服务器返回结果（0/1/-5）
     */
    public void checkSmsVerifyCode(String phone, String code, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "verify_SMS_code");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("SMS_code", code);
        params.addBodyParameter("type", "1");
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 验证短信验证码
     *
     * @param phone 手机号
     * @param code  验证码
     *              返回参数
     *              "code":""//服务器返回结果（0/1/-5）
     */
    public void checkSmsForgetCode(String phone, String code, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "verify_SMS_code");
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("SMS_code", code);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 注册时验证短信码并注册账号
     *
     * @param phone    用户填写的手机号
     * @param code     用户填写的短信验证码
     * @param pwd      用户设置的登录密码(16位md5值+SMS_code的md5值=伪装成32位md5值)
     * @param cls
     * @param callBack "device_index":""//设备编号
     *                 "token":""//登录凭证，注册成功时才有此字段
     *                 返回参数
     *                 "code":"",//服务器处理结果(1/0/-3/-5)
     */
    public void registerAccoiunt(String phone, String code, String pwd, Class<RegisterAccount> cls, RequestCallBack<RegisterAccount> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "register_account");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("SMS_code", code);
        params.addBodyParameter("login_password", pwd);

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 验证手机号是否注册过
     *
     * @param phone
     * @param callBack
     */
    public void getCheckUser(String phone, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "check_user");
        params.addBodyParameter("phone", phone);
        postRequest(URL, params, cls, callBack);
    }


    /**
     * 用户登录
     *
     * @param account  用户要登录的账号（手机号）
     * @param pwd      用户填写的登录密码(16位md5值+盐的16位md5值=伪装成32位md5值)
     * @param cls
     * @param callBack
     */
    public void login(String account, String pwd, Class<RegisterAccount> cls, RequestCallBack<RegisterAccount> callBack) {

        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "login_by_password");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("account", account);
        params.addBodyParameter("login_password", pwd);
        Logger.i("设备ID：" + RequestOftenKey.getDeviceIndex(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 快捷登录（先通过接口4获取短信）
     * device_index":""//设备编号
     *
     * @param phone    用户填写的手机号
     * @param code     用户填写的短信验证码
     * @param cls
     * @param callBack * 返回参数
     *                 "code":"",//服务器处理结果(1/0/-3/-5)
     *                 "token":""//登录凭证，登录成功时才有此字段
     */
    public void quickLogin(String phone, String code, String sign, Class<RegisterAccount> cls, RequestCallBack<RegisterAccount> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "login_by_quick");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("SMS_code", code);
        params.addBodyParameter("sign", sign);


        postRequest(URL, params, cls, callBack);
    }

    /**
     * 重设登录密码（先通过验证短信验证码接口）
     *
     * @param account  要找回密码的账号（手机号）
     * @param newPwd   用户输入的新的登录密码(16位md5值+SMS_code的16位值=32位md5值)
     * @param cls
     * @param callBack
     */
    public void resetLoginPwd(String account, String newPwd, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "reset_login_password_ios");
        params.addBodyParameter("account", account);
        params.addBodyParameter("login_password", newPwd);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 修改登录密码
     * deviceIndex 客户端运行设备的唯一自增编号
     * token 客户端当前登录凭证(token+盐做数学运算)
     *
     * @param oldPwd   旧登录密码（16位md5值）
     * @param newPwd   新登录密码（16位md5值+盐的16位md5)
     * @param cls
     * @param callBack
     */
    public void modifyLoginPwd(String oldPwd, String newPwd, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "amend_login_password");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("old_login_password", oldPwd);
        params.addBodyParameter("new_login_password", newPwd);

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 初次设置(或重设)支付密码(先通过短信验证接口)
     * "device_index":"",//客户端运行设备的唯一自增编号
     * "token":"",//客户端登录凭证(token值+盐做数学运算)
     *
     * @param newPwd   用户输入的新的登录密码(16位md5值+盐的16位md5值)
     * @param cls
     * @param callBack *
     *                 返回参数
     *                 <p>
     *                 "code":"",//服务器处理结果(1、0、-3、-4、-5)
     */
    public void resetPayPwd(String newPwd, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "reset_pay_password_ios");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("new_pay_password", newPwd);

        Logger.i("URL  "+URL+" DEVICEINDEX "+RequestOftenKey.getDeviceIndex(mContext)+" TOKEN "+RequestOftenKey.getToken(mContext)+" NEW PASSWORD  "+newPwd);
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 修改支付密码(通过旧支付密码)
     * "device_index":"",//客户端运行设备的唯一自增编号
     * "token":"",//客户端登录凭证(token值+盐做数学运算)
     *
     * @param oldPwd   旧支付密码(16位md5值+盐的16位md5值)
     * @param newPwd   用户输入的新的登录密码(16位md5值+盐的16位md5值)
     * @param cls
     * @param callBack * 返回参数
     *                 <p>
     *                 "code":"",//服务器处理结果：（1、0、-3、-4、-5）
     */
    public void modifyPayPwd(String oldPwd, String newPwd, Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "amend_pay_password");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("old_pay_password", oldPwd);
        params.addBodyParameter("new_pay_password", newPwd);

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 退出登录
     * * 	"device_index":"",//客户端运行设备的唯一自增编号
     * "token":"",//客户端登录凭证(token值+盐做数学运算)
     *
     * @param cls
     * @param callBack 返回参数
     *                 <p>
     *                 "code":"",//服务器处理结果：（1、0、-3、-4）
     */
    public void LogOut(Class<BaseEntity> cls, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "login_out");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 设置用户资料
     *
     * @param info     用户信息实例(不需要修改或设置的字段可以不存在)
     * @param callBack
     */
    public void setUserInfo(GetUserInfoEntity.UserInfo info, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "set_user_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        JsonObject jsonObject = new JsonObject();

        if (!TextUtils.isEmpty(info.name)) {
            jsonObject.addProperty("name", info.name);
        }

        if (!TextUtils.isEmpty(info.city)) {
            jsonObject.addProperty("province", info.province);
            jsonObject.addProperty("city", info.city);
            jsonObject.addProperty("district", info.district);
        }

        if (!TextUtils.isEmpty(info.sex)) {
            jsonObject.addProperty("sex", info.sex);
        }

        params.addBodyParameter("user_info", jsonObject.toString());

        postRequest(URL, params, BaseEntity.class, callBack);
    }

    /**
     * 获取用户资料
     *
     * @param callBack
     */
    public void getUserInfo(RequestCallBack<GetUserInfoEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_userInfo");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(context));
        params.addBodyParameter("token", RequestOftenKey.getToken(context));
        Logger.i("获取到的用户资料参数： token"+RequestOftenKey.getToken(context)+" deviceIndex: "+RequestOftenKey.getDeviceIndex(context));
        postRequest(URL, params, GetUserInfoEntity.class, callBack);
    }


    /**
     * 上传用户头像(POST上传用户头像)
     *
     * @param deviceIndex
     * @param token
     * @param file        头像图片文件内容
     * @param callBack
     */
    public void uploadUserIcon(String deviceIndex, String token, String file, RequestCallBack<UploadImageData> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_user_info");
        params.addBodyParameter("device_index", deviceIndex);
        params.addBodyParameter("token", token);
        params.addBodyParameter("file", file);

        postRequest(URL, params, UploadImageData.class, callBack);
    }

    /**
     * 获取用户信息(POST上传用户头像)
     *
     * @param callBack
     */
    public void getUserInfoList(RequestCallBack<GetUserInfoEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_user_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));

        postRequest(URL, params, GetUserInfoEntity.class, callBack);
    }


    public void checkIDNumber() {

    }

    /**
     * 提交身份证信息
     *
     * @param cardId       身份证号
     * @param cardName     真实姓名
     * @param cardValidity 有效期（时间戳，单位：秒）
     * @param cardType     身份证类型，0临时，1正式
     * @param cls
     * @param callBack
     */
    public void submitIDCardInfo(String cardId, String cardName, String cardValidity, String cardType, Class<Submit_card_info> cls, RequestCallBack<Submit_card_info> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "submit_card_info");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("card_id", cardId);
        params.addBodyParameter("card_name", cardName);
        params.addBodyParameter("card_validity", cardValidity);
        params.addBodyParameter("card_type", cardType);

        postRequest(URL, params, cls, callBack);
    }

    /**
     * 提交身份证正面照片
     *
     * @param deviceIndex 客户端运行设备的唯一自增编号
     * @param token       客户端登录凭证(token值+盐做数学运算)
     * @param file        头像图片文件内容
     * @param callBack
     */
    public void submitIDCardFrontImage(String deviceIndex, String token, String file, RequestCallBack<UploadImageData> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "upload_card_front_image");
        params.addBodyParameter("device_index", deviceIndex);
        params.addBodyParameter("token", token);
        params.addBodyParameter("file", file);

        postRequest(URL, params, UploadImageData.class, callBack);
    }

    /**
     * 提交身份证反面照片
     *
     * @param deviceIndex 客户端运行设备的唯一自增编号
     * @param token       客户端登录凭证(token值+盐做数学运算)
     * @param file        头像图片文件内容
     * @param callBack
     */
    public void submitIDCardBackImage(String deviceIndex, String token, String file, RequestCallBack<UploadImageData> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "upload_card_reverse_image");
        params.addBodyParameter("device_index", deviceIndex);
        params.addBodyParameter("token", token);
        params.addBodyParameter("file", file);

        postRequest(URL, params, UploadImageData.class, callBack);
    }

    public void getIDCardInfo(String deviceIndex, String token) {

    }

    /**
     * base64方式上传图片
     *
     * @param imgRole  图片的作用（head：头像，card_front：身份证正面，card_reverse：身份证背面）
     * @param imgName  图片名字，包括后缀名
     * @param imgType  图片类型（image/png、image/jpeg、image/gif）
     * @param imgSize  图片数据长度（image_data字段值的长度）
     * @param imgFile   图片格式文件
     * @param callBack
     */
    public void uploadBase64Image(String imgRole, String imgName, String imgType, String imgSize, String imgFile, RequestCallBack<UploadImageData> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "upload_image_data");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("image_data", imgFile);
        params.addBodyParameter("image_role", imgRole);
        params.addBodyParameter("image_name", imgName);
        params.addBodyParameter("image_type", imgType);
        params.addBodyParameter("image_size", imgSize);


        postRequest(URL, params, UploadImageData.class, callBack);
    }

    /**
     * 获取用户积发奖励包
     *
     * @param cls
     * @param callBack
     */
    public void getIntegralBalance(Class<ObtainIntegral> cls, RequestCallBack<ObtainIntegral> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "get_integral_balance");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(URL, params, cls, callBack);
    }

    /**
     * 根据经纬度获取地理位置信息
     *
     * @param lat      经度
     * @param lng      纬度
     * @param callBack
     */
    public void location(String lat, String lng, RequestCallBack<Location> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "location/setLocation");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", "0");
        params.addBodyParameter("lat", lat);
        params.addBodyParameter("lng", lng);
        postRequest(Ip.getURL(context) + "mall.php", params, Location.class, callBack);

    }

    /**
     * 获取省份列表
     *
     * @param callBack
     */
    public void provinceList(RequestCallBack<ProvinceList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "location/provionce_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(Ip.getURL(context) + "mall.php", params, ProvinceList.class, callBack);

    }

    /**
     * 获取省份的城市列表
     *
     * @param province
     * @param callBack
     */
    public void city(String province, RequestCallBack<CityList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "location/city_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("province", province);
        postRequest(Ip.getURL(context) + "mall.php", params, CityList.class, callBack);
    }

    /**
     * 获取城市区县列表
     *
     * @param city
     * @param callBack
     */
    public void county(String city, RequestCallBack<CountyList> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "location/county_list");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("city", city);
        postRequest(Ip.getURL(context) + "mall.php", params, CountyList.class, callBack);
    }

    /**
     * 修改用户手机号
     *
     * @param step     1 老手机验证码验证 2 新手机验证码验证
     * @param oldPhone 老手机号
     * @param newPhone 新手机号
     * @param SMSCode  验证码
     * @param callBack
     */
    public void changePhone(int step, String oldPhone, String newPhone, String SMSCode, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/change_phone");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("step", String.valueOf(step));
        params.addBodyParameter("old_phone", oldPhone);
        params.addBodyParameter("new_phone", newPhone);
        params.addBodyParameter("SMS_code", SMSCode);
        postRequest(Ip.getURL(context) + "mall.php", params, BaseEntity.class, callBack);
    }

    /**
     * 绑定推荐人信息
     *
     * @param referrerPhone
     * @param callBack
     */
    public void bindReferrerPhone(String referrerPhone, RequestCallBack<BaseEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "account/bind_superior");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("sign", referrerPhone);
        postRequest(Ip.getURL(context) + "mall.php", params, BaseEntity.class, callBack);
    }

    /**
     * 检测微信号是否绑定了手机号
     *
     * @param wxOpenId 微信的OpenID
     * @param callBack
     */
    public void checkWhetherWXBindPhone(String wxOpenId, RequestCallBack<TokenEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "login_by_weixin");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("open_id", wxOpenId);
        params.addBodyParameter("type", "0");
        Logger.i("11111检测微信号是否绑定了手机号参数：  device_index" + RequestOftenKey.getDeviceIndex(mContext) + "   open_id:" + wxOpenId + "   type:" + 0);
        postRequest(URL, params, TokenEntity.class, callBack);
    }

    /**
     * 将微信和手机号绑定
     *
     * @param wxOpenId 微信的OpenID
     * @param smsCode  用户填写的短信验证码
     * @param phone    绑定手机号
     * @param sign     推荐者手机号(非必填)
     * @param callBack
     */
    public void BindWXAndPhone(String wxOpenId, String smsCode, String phone, String sign, RequestCallBack<WXBindPhoneEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "login_by_weixin");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("open_id", wxOpenId);
        params.addBodyParameter("type", "1");
        params.addBodyParameter("SMS_code", smsCode);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("sign", sign);
        Logger.i("绑定手机号码与微信参数:  deviceIndex:" + RequestOftenKey.getDeviceIndex(mContext) + "  token:" + RequestOftenKey.getToken(mContext) + "   openId:" + wxOpenId
                + "  type:1    smsCode:" + smsCode + "  phone:" + phone + "  sign:" + sign);
        postRequest(URL, params, WXBindPhoneEntity.class, callBack);

    }

    /**
     * 会员体系判断
     *
     * @param phone
     * @param callBack
     */
    public void getMemberRelationSystem(String phone, RequestCallBack<MemberRelationSystemEntity> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("c", "member_relation_system");
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        params.addBodyParameter("phone", phone);
        postRequest(Ip.getURL(context) + "mall.php", params, MemberRelationSystemEntity.class, callBack);
    }

    public void loginCustomerService(RequestCallBack<AliLoginUserInfo> callBack) {
        RequestParams params = new RequestParams();
        params.addBodyParameter("device_index", RequestOftenKey.getDeviceIndex(mContext));
        params.addBodyParameter("token", RequestOftenKey.getToken(mContext));
        postRequest(Ip.getURL(context) + "mall.php", params, AliLoginUserInfo.class, callBack);
    }

}
