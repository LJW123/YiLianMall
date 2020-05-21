package rxfamily.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;


/**
 * @author ASUS
 */
public class NetRequestOftenKey {
    /**
     * sp key- token
     **/
    public static final String SPKEY_TOKEN = "token";
    /**
     * sp key-设备id
     **/
    public static final String SPKEY_DEVICE_INDEX = "device_index";
    /**
     * 共享文件的名称
     */

    private final static String strPreferName = "UserInfor";
    /**
     * sp key-盐值
     **/
    public static final String SPKEY_SERVER_SALT = "server_salt";


    /**
     * 网络请求常用Key
     */

    public static String getDeviceIndex(Context context) {
//        Logger.i("getDeviceIndex  FFFFFF "+PreferenceUtils.readStrConfig(SPKEY_DEVICE_INDEX, context));
        SharedPreferences sharedPreferences = context.getSharedPreferences(strPreferName, 0);
        return sharedPreferences.getString(SPKEY_DEVICE_INDEX, "");
    }

    ;

    /**
     * 加盐值
     *
     * @param context
     * @return
     */
    public static String getToken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(strPreferName, 0);
        String token =sharedPreferences.getString(SPKEY_TOKEN,"");
//        Logger.i("生成加盐token使用的token:"+token);
        if (TextUtils.isEmpty(token)) {//解决异常情况存储token为空字符串，导致转换为long类型错误问题 bugly上报地址：https://bugly.qq.com/v2/crash-reporting/crashes/ca5b40bb7f/4370?pid=1
            token = "0";
        }
        Long aLong = Long.valueOf(token);
//        Logger.i("返回的token1: "+aLong);
        if (aLong == 0L) {//如果不加盐值的token为0时，则返回0，不再增加盐值
            return "0";
        }

        String salt = sharedPreferences.getString(SPKEY_SERVER_SALT, "0");
//        Logger.i("获取token时使用的salt:"+salt);
        String s = String.valueOf(aLong + Long.valueOf(salt));

        return s;
    }

    ;

    /**
     * 不加盐值
     *
     * @param context
     * @return
     */
    public static String gettoken(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(strPreferName, 0);
        return sharedPreferences.getString(SPKEY_TOKEN,"");
    }

    public static String getServerSalt(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(strPreferName, 0);
        return sharedPreferences.getString(SPKEY_SERVER_SALT, "");
    }
}
