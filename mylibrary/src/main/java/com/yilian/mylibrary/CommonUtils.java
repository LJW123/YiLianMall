package com.yilian.mylibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.yilian.mylibrary.Constants.PHONE_REGEX;

public class CommonUtils<T> {


    public static void showInfoDialog(Context context, String message) {
        showInfoDialog(context, message, "提示", "确定", null);
    }

    public static void showInfoDialog(Context context, String message,
                                      String titleStr, String positiveStr,
                                      DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
        localBuilder.setTitle(titleStr);
        localBuilder.setMessage(message);
        if (onClickListener == null)
            onClickListener = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                }
            };
        localBuilder.setPositiveButton(positiveStr, onClickListener);
        localBuilder.show();
    }

    /**
     * 新版返回码处理方式
     *
     * @param context            此处的context使用全局Context 防止内存泄漏
     * @param code
     * @param msg
     * @param loginActivityClass 跳转的页面类名（全类名）
     * @return
     */
    public static boolean serivceReturnCode(Context context, int code, String msg, Class loginActivityClass) {
        switch (code) {
            case 1:
                return true;
            case -5://该情况是密码错误，不同界面的密码错误处理逻辑可能不同，所以返回给页面单独处理
                return true;
            case -4:
            case -23:
                context.startActivity(new Intent(context, loginActivityClass));
                return false;
            case -12:
                return false;
            default:
                Toast.makeText(context,  msg, Toast.LENGTH_SHORT).show();
                return false;
        }
    }


    /**
     * 新版返回码处理方式
     *
     * @param context 此处的context使用全局Context 防止内存泄漏
     * @param code
     * @param msg
     * @return
     */
    public static boolean serivceReturnCode(Context context, int code, String msg) {
        switch (code) {
            case 1:
                return true;
            case -5://该情况是密码错误，不同界面的密码错误处理逻辑可能不同，所以返回给页面单独处理
                Toast.makeText(context, "验证码或密码错误", Toast.LENGTH_SHORT).show();
                return true;
            case -4:
            case -23:
                Toast.makeText(context, "登录状态失效,请重新登录.", Toast.LENGTH_SHORT).show();
                PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE,false,context);
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true,context);
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true,context);
                Intent intent = new Intent();
                intent.setComponent(new ComponentName(context, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                context.startActivity(intent);
                return false;
            case -12:
                return false;
            default:
                if (!TextUtils.isEmpty(msg)) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
                return false;
        }
    }

    /**
     * 服务区返回状态码
     *
     * @param context 上下文
     * @param code    返回码
     */
    public static Boolean httpRequestReturnCode(Context context, int code) {
        switch (code) {
            case 0:
                Toast.makeText(context, "请求参数异常", Toast.LENGTH_SHORT).show();
                return false;
            case 1:
                return true;
            case -1:
                Toast.makeText(context, "操作频繁,请稍后", Toast.LENGTH_SHORT).show();
                return false;
            case -2:
                Toast.makeText(context, "系统限制", Toast.LENGTH_SHORT).show();
                return false;
            case -3:
                Toast.makeText(context, "系统繁忙", Toast.LENGTH_SHORT).show();
                return false;
            case -4:
                Toast.makeText(context, "token失效", Toast.LENGTH_SHORT).show();
                return false;
            case -5:
                Toast.makeText(context, "密码验证失败", Toast.LENGTH_SHORT).show();
                return false;
            case -6:
                Toast.makeText(context, "发送失败", Toast.LENGTH_SHORT).show();
                return false;
            case -7:
                Toast.makeText(context, "文件上传出错", Toast.LENGTH_SHORT).show();
                return false;
            case -8:
                Toast.makeText(context, "不支持的文件类型", Toast.LENGTH_SHORT).show();
                return false;
            case -9:
                Toast.makeText(context, "无效的控制器参数", Toast.LENGTH_SHORT).show();
                return false;
            case -10:
                Toast.makeText(context, "缺少控制器参数", Toast.LENGTH_SHORT).show();
                return false;
            case -11:
                Toast.makeText(context, "数据库内的数据异常", Toast.LENGTH_SHORT).show();
                return false;
            case -12:
                Toast.makeText(context, "数据已存在", Toast.LENGTH_SHORT).show();
                return false;
            case -13:
                Toast.makeText(context, "奖励不足", Toast.LENGTH_SHORT).show();
                return false;
            case -14:
                Toast.makeText(context, "订单失效", Toast.LENGTH_SHORT).show();
                return false;
            case -15:
                Toast.makeText(context, "验证状态超时", Toast.LENGTH_SHORT).show();
                return false;
            case -16:
                Toast.makeText(context, "账号未注册", Toast.LENGTH_SHORT).show();
                return false;
            case -17:
                Toast.makeText(context, "缺少请求参数", Toast.LENGTH_SHORT).show();
                return false;
            case -18:
                Toast.makeText(context, "请求的数据不存在", Toast.LENGTH_SHORT).show();
                return false;
            case -19:
                Toast.makeText(context, "服务器代码出错", Toast.LENGTH_SHORT).show();
                return false;
            case -20:
                Toast.makeText(context, "数据不完整", Toast.LENGTH_SHORT).show();
                return false;
            case -21:
                Toast.makeText(context, "没有获取到数据", Toast.LENGTH_SHORT).show();
                return false;
            case -22:
                Toast.makeText(context, "活动已经结束", Toast.LENGTH_SHORT).show();
                return false;
            case -23:
                Toast.makeText(context, "设备ID验证失败", Toast.LENGTH_SHORT).show();
                return false;
            case -24:
                Toast.makeText(context, "盐值验证失败", Toast.LENGTH_SHORT).show();
                return false;
            case -25:
                Toast.makeText(context, "身份证号已经使用过，需重新提交身份证资料", Toast.LENGTH_SHORT).show();
                return false;
            case -26:
                Toast.makeText(context, "二维码参数无效", Toast.LENGTH_SHORT).show();
                return false;
            case -27:
                Toast.makeText(context, "活动参与次数已达到上限", Toast.LENGTH_SHORT).show();
                return false;
            case -28:
                Toast.makeText(context, "请设置密码", Toast.LENGTH_SHORT).show();
                return false;
            case -29:
                Toast.makeText(context, "请输入大于1的数字", Toast.LENGTH_SHORT).show();
                return false;
            case -30:
                Toast.makeText(context, "商家未提交资料", Toast.LENGTH_SHORT).show();
                return false;
            case -31:
                Toast.makeText(context, "商家资料正在审核中", Toast.LENGTH_SHORT).show();
                return false;
            case -32:
                Toast.makeText(context, "商家未设置合作折扣", Toast.LENGTH_SHORT).show();
                return false;
            case -33:
                Toast.makeText(context, "商家与兑换中心暂停合作，不能使用此功能", Toast.LENGTH_SHORT).show();
                return false;
            case -34:
                Toast.makeText(context, "商家被兑换中心审核拒绝，不能使用此功能", Toast.LENGTH_SHORT).show();
                return false;
            case -35:
                Toast.makeText(context, "超过领奖有效期", Toast.LENGTH_SHORT).show();
                return false;
            case -36:
                Toast.makeText(context, "兑换中心奖励不足", Toast.LENGTH_SHORT).show();
                return false;
            case -37:
                Toast.makeText(context, "该商品已经下架", Toast.LENGTH_SHORT).show();
                return false;
            case -38:
                Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
                return false;
            case -39:
                Toast.makeText(context, "购物车信息错误", Toast.LENGTH_SHORT).show();
                return false;
            case -40:
                Toast.makeText(context, "申请售后类型参数错误", Toast.LENGTH_SHORT).show();
                return false;
            case -41:
                Toast.makeText(context, "售后订单号状态不合理不能 操作！", Toast.LENGTH_SHORT).show();
                return false;
            case -42:
                Toast.makeText(context, "售后订单号不存在！", Toast.LENGTH_SHORT).show();
                return false;
            case -43:
                Toast.makeText(context, "售后订单号状态不合理不能 操作！", Toast.LENGTH_SHORT).show();
                return false;
            case -44:
                Toast.makeText(context, "已出库订单不能取消", Toast.LENGTH_SHORT).show();
                return false;
            case -45:
                Toast.makeText(context, "已经取消过了", Toast.LENGTH_SHORT).show();
                return false;
            case -46:
                Toast.makeText(context, "有商品还未发货不能确认收货，请耐心等待", Toast.LENGTH_SHORT).show();
                return false;
            case -47:
                Toast.makeText(context, "超出活动范围不能参加", Toast.LENGTH_SHORT).show();
                return false;
            case -48:
                Toast.makeText(context, "已经评价过请勿重复操作", Toast.LENGTH_SHORT).show();
                return false;
            case -49:
                Toast.makeText(context, "该用户不属于你的上级或者推荐的会员不能查看", Toast.LENGTH_SHORT).show();
                return false;
            case -50:
                Toast.makeText(context, "该会员和我没有关系", Toast.LENGTH_SHORT).show();
                return false;
            case -51:
                Toast.makeText(context, "该用户还未绑定微信", Toast.LENGTH_SHORT).show();
                return false;
            case -52:
                Toast.makeText(context, "领奖励超额", Toast.LENGTH_SHORT).show();
                return false;
            case -53:
                Toast.makeText(context, "领奖励金额小于100", Toast.LENGTH_SHORT).show();
                return false;
            case -54:
                Toast.makeText(context, "暂无可以参加的每日摇一摇活动", Toast.LENGTH_SHORT).show();
                return false;
            case -55:
                Toast.makeText(context, "已经免费参加过一次了，继续请传递密码", Toast.LENGTH_SHORT).show();
                return false;
            case -56:
                Toast.makeText(context, "转账金额过小", Toast.LENGTH_SHORT).show();
                return false;
            case -57:
                Toast.makeText(context, "转账金额过大", Toast.LENGTH_SHORT).show();
                return false;
            case -58:
                Toast.makeText(context, "老手机号与客户端传递不一致", Toast.LENGTH_SHORT).show();
                return false;
            case -59:
                Toast.makeText(context, "新手机号不能与老手机号一致", Toast.LENGTH_SHORT).show();
                return false;
            case -60:
                Toast.makeText(context, "转赠对象不能是自己", Toast.LENGTH_SHORT).show();
                return false;
        }
        return false;
    }

    /**
     * 服务区返回状态码
     *
     * @param context 上下文
     * @param code    返回码
     */
    public static Boolean NetworkRequestReturnCode(Context context, String code) {

        switch (code) {
            case "1":
                return true;
            case "-1":
                Toast.makeText(context, "30秒内只能发送一次验证码哦，请稍后重试！", Toast.LENGTH_SHORT).show();
                return false;
            case "-2":
                Toast.makeText(context, "系统限制", Toast.LENGTH_SHORT).show();
                return false;
            case "-3":
                Toast.makeText(context, "系统繁忙", Toast.LENGTH_SHORT).show();
                return false;
            case "-4":
            case "-23":
                Toast.makeText(context, "登录状态失效", Toast.LENGTH_SHORT).show();
                context.getSharedPreferences("UserInfor", 0).edit().putBoolean("state", false).commit();
                FileUtils.delFile("/com.yilian/userphoto/userphoto.png");
                return false;
            case "-5":
                Toast.makeText(context, "密码/验证码验证失败", Toast.LENGTH_SHORT).show();
                return false;
            case "-6":
                Toast.makeText(context, "操作失败", Toast.LENGTH_SHORT).show();
                return false;
            case "-12":
                Toast.makeText(context, "数据已存在", Toast.LENGTH_SHORT).show();
                return false;
            case "-13":
                Toast.makeText(context, "奖励不足", Toast.LENGTH_SHORT).show();
                return false;
            case "-14":
                Toast.makeText(context, "订单失效", Toast.LENGTH_SHORT).show();
                return false;
            case "-15":
                Toast.makeText(context, "验证时间过长，请返回上一界面重新获取验证码", Toast.LENGTH_SHORT).show();
                return false;
            case "-16":
                Toast.makeText(context, "手机号未注册", Toast.LENGTH_SHORT).show();
                return false;
            case "-21":
                Toast.makeText(context, "没有数据了", Toast.LENGTH_SHORT).show();
                return false;
            case "-22":
                Toast.makeText(context, "活动已结束", Toast.LENGTH_SHORT).show();
                return false;
            case "-24":
                Toast.makeText(context, "盐验证失败", Toast.LENGTH_SHORT).show();
                return false;
            case "-25":
                Toast.makeText(context, "该身份证信息已认证", Toast.LENGTH_SHORT).show();
                return false;
            case "-26":
                Toast.makeText(context, "二维码已失效", Toast.LENGTH_SHORT).show();
                return false;
        }
        return false;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 数字字母组合
     *
     * @param str
     * @return
     */
    public static boolean passwordVerify(String str) {
        String str1 = "([0-9](?=[0-9]*?[a-zA-Z])\\w{5,17})|([a-zA-Z](?=[a-zA-Z]*?[0-9])\\w{5,17})";
        return str.matches(str1);
    }

    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex.toUpperCase());
        }
        return hexString.toString();
    }

    /**
     * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     *
     * @param context
     * @return 0：无网络 1：WIFI 2：CMWAP 3：CMNET
     */
    public static int isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return 0;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return 1;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo)
                                    || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return 2;
                            }
                            return 3;
                        }
                    }
                }
            }
        }
        return 0;
    }


    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd HH:mm:ss
     */

    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }


    public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
        int picWidth = bitmap.getWidth();
        int picHeight = bitmap.getHeight();
        int picScreenHeight = 0;
        picScreenHeight = (screenWidth * picHeight) / picWidth;
        return picScreenHeight;
    }

    /**
     * @param context
     * @throws Exception
     */
    public static void bindViewSelector(Context context, final View view,
                                        String nornalImageurl, final String pressedImageUrl) {
        final StateListDrawable stateListDrawable = new StateListDrawable();
        final BitmapUtils utils = new BitmapUtils(context);
        utils.display(view, nornalImageurl, new BitmapLoadCallBack<View>() {

            @Override
            public void onLoadCompleted(View container, String uri,
                                        Bitmap bitmap, BitmapDisplayConfig config,
                                        BitmapLoadFrom from) {
                Drawable normalDrawable = new BitmapDrawable(bitmap);
                stateListDrawable.addState(
                        new int[]{android.R.attr.state_active},
                        normalDrawable);
                stateListDrawable.addState(new int[]{
                        android.R.attr.state_focused,
                        android.R.attr.state_enabled}, normalDrawable);
                stateListDrawable.addState(
                        new int[]{android.R.attr.state_enabled},
                        normalDrawable);
                utils.display(container, pressedImageUrl,
                        new BitmapLoadCallBack<View>() {

                            @Override
                            public void onLoadCompleted(View container,
                                                        String uri, Bitmap bitmap,
                                                        BitmapDisplayConfig config,
                                                        BitmapLoadFrom from) {
                                stateListDrawable.addState(new int[]{
                                                android.R.attr.state_pressed,
                                                android.R.attr.state_enabled},
                                        new BitmapDrawable(bitmap));

                                view.setBackgroundDrawable(stateListDrawable);

                            }

                            @Override
                            public void onLoadFailed(View container,
                                                     String uri, Drawable drawable) {
                                // TODO Auto-generated method stub

                            }
                        });
            }

            @Override
            public void onLoadFailed(View container, String uri,
                                     Drawable drawable) {

            }
        });

    }

    public static boolean isPhoneNumer(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        }
        String telRegex = PHONE_REGEX;
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(telRegex);
    }

    public static boolean rightPWD(String passWord) {
//        分开来注释一下：
//        ^ 匹配一行的开头位置
//        (?![0-9]+$) 预测该位置后面不全是数字
//        (?![a-zA-Z]+$) 预测该位置后面不全是字母
//         [0-9A-Za-z] {6,10} 由6-18位数字或字母组成
//        $ 匹配行结尾位置
        String pwdRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,10}$";
        if (TextUtils.isEmpty(passWord)) {
            return false;
        }
        return passWord.matches(pwdRegex);
    }

    //获取MD5的9-25位的小写
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        //16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toLowerCase();
    }

    //    获取MD5值得大写
    public static String getMD5(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString().toUpperCase();
    }

    private static Drawable createDrawable(Drawable d, Paint p) {

        BitmapDrawable bd = (BitmapDrawable) d;
        Bitmap b = bd.getBitmap();
        Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
                bd.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(b, 0, 0, p); // 关键代码，使用新的Paint画原图，

        return new BitmapDrawable(bitmap);
    }

    /**
     * 设置Selector。 本次只增加点击变暗的效果，注释的代码为更多的效果
     */
    public static StateListDrawable createSLD(Context context, Drawable drawable) {
        StateListDrawable bg = new StateListDrawable();
        int brightness = 50 - 127;
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0,
                brightness,// 改变亮度
                0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});

        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

        Drawable normal = drawable;
        Drawable pressed = createDrawable(drawable, paint);
        bg.addState(new int[]{android.R.attr.state_pressed,}, pressed);
        bg.addState(new int[]{android.R.attr.state_focused,}, pressed);
        bg.addState(new int[]{android.R.attr.state_selected}, pressed);
        bg.addState(new int[]{}, normal);
        return bg;
    }

    public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
        Bitmap image = null;
        AssetManager am = ct.getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;

    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    public static String formatDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(new Date());
    }

    public static String getStr(String[] args) {
        String str = "";
        for (int i = 0; i < args.length; i++) {
            str += (String) args[i];
        }
        return str;
    }

    /**
     * 判断 String 是否相同
     *
     * @param str
     * @return 结果
     */
    public static boolean verification(String str) {
        char s = str.charAt(0);
        for (int i = 1; i < str.length() && i < 8; i++) {
            if (s != str.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取状态栏高度
     *
     * @param activity
     * @return
     */
    public static int getStatusHeight(Activity activity) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        activity.getWindow().getDecorView(
        ).getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName(
                        "com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(
                        localClass.getField("status_bar_height").get(
                                localObject).toString());
                statusHeight = activity.getResources(
                ).getDimensionPixelSize(i5);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

    public static int getHeight(Activity activity, String s) {
        DisplayMetrics metric = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
        int height = metric.heightPixels;   // 屏幕高度（像素）
        int w = metric.widthPixels;
        if (s.equals("h")) {
            return height;
        } else {
            return w;
        }

    }

    /**
     * @param context
     * @return String
     * @throws NameNotFoundException
     * @description 获取app versionName
     */
    public static String getAppVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packInfo.versionName;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            return "";
        }
    }

    /**
     * 百度坐标转为火星坐标
     *
     * @param bd_lat
     * @param bd_lon
     * @return double数组:[gcj02_lat,gcj02_lon]
     */
    public static double[] bd2gcj02(double bd_lat, double bd_lon) {
        double x_pi = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double lon = z * Math.cos(theta);
        double lat = z * Math.sin(theta);
        return new double[]{lat, lon};
    }

    /**
     * @param screenWidth 手机屏幕的宽度
     * @param picWidth    原始图片所用分辨率的宽度
     * @param retainValue 保留小数位
     * @return 手机屏幕分辨率与原始图片分辨率的宽度比
     */
    public static double divideWidth(int screenWidth, int picWidth, int retainValue) {
        BigDecimal screenBD = new BigDecimal(Double.toString(screenWidth));
        BigDecimal picBD = new BigDecimal(Double.toString(picWidth));
        return screenBD.divide(picBD, retainValue, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

//    public static void backTop(final PullToRefreshListView lv, final ImageView ivReturn) {
//
//        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                if (firstVisibleItem > 9) {
//                    ivReturn.setVisibility(View.VISIBLE);
//                } else {
//                    ivReturn.setVisibility(View.GONE);
//                }
//
//            }
//        });
//
//        ivReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lv.getRefreshableView().setSelection(0);
//            }
//        });
//
//    }

    public static void backTop(final ListView lv, final ImageView ivReturn) {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem > 9) {
                    ivReturn.setVisibility(View.VISIBLE);
                } else {
                    ivReturn.setVisibility(View.GONE);
                }

            }
        });

        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setSelection(0);
            }
        });

    }

    public static void backTop(final GridView lv, final ImageView ivReturn) {

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem > 3) {
                    ivReturn.setVisibility(View.VISIBLE);
                } else {
                    ivReturn.setVisibility(View.GONE);
                }

            }
        });

        ivReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv.setSelection(0);
            }
        });

    }

//    public static void backTop(final ScrollView lv , final ImageView ivReturn){
//
//        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//                if (firstVisibleItem > 9){
//                    ivReturn.setVisibility(View.VISIBLE);
//                }else {
//                    ivReturn.setVisibility(View.GONE);
//                }
//
//            }
//        });
//
//        ivReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                lv.setSelection(0);
//            }
//        });
//
//    }

}
