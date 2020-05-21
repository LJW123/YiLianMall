package com.yilian.mall.ui;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.leshan.ylyj.view.activity.bankmodel.MyBankCardsActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.ui.mvp.view.BasicInfomationViewImplActivity;
import com.yilian.mall.ui.mvp.view.CertificationResultImplViewActivity;
import com.yilian.mall.ui.mvp.view.CertificationViewImplActivity;
import com.yilian.mall.ui.mvp.view.EditUserNameViewImplActivity;
import com.yilian.mall.utils.DataCleanManager;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Compressor;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.leshan.ylyj.view.activity.bankmodel.MyPurseActivity.CARD_LIST_REQUEST_CODE;
import static com.yilian.mall.ui.mvp.view.CertificationResultImplViewActivity.IS_FROM_USER_INFO_ACTIVITY;


/**
 * 个人信息界面
 * <p>
 * 该页面的拍照权限申请逻辑：
 * <p>
 * <p>
 * ********************************************丨→有—(打开拍照界面)←———————丨*************
 * ********检查拍照权限—有—检查读取存储卡权限丨**********************************丨***************
 * **************丨*****************↑*********丨→没有—申请读取存储卡权限—通过——***************
 * *************没有****************↑*************************丨***********************************
 * **************丨*****************↑************************不通过********************************
 * *********申请拍照权限—通过———→*************************丨***********************************
 * **************丨*******************************************STOP**********************************
 * ************不通过*******************************************************************************
 * **************丨*********************************************************************************
 * *************STOP********************************************************************************
 */
public class UserInfoActivity extends BaseActivity {


    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int PHOTO_ALBUM = 2;//打开相册
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//请求读取SD卡的权限使用请求码
    private static String ImageName;
    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.nickname)
    private TextView nickname;
    @ViewInject(R.id.signature)
    private TextView tvSignature;
    @ViewInject(R.id.user_level)
    private TextView tvLevel;
    @ViewInject(R.id.user_photo)
    private ImageView user_photo;
    @ViewInject(R.id.real_name)
    private TextView tvRealName;
    @ViewInject(R.id.phone)
    private TextView phone;
    @ViewInject(R.id.referrer)
    private TextView referrer;//推荐人手机号码
    @ViewInject(R.id.ll_phone)
    private LinearLayout llPhone;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private AccountNetRequest accountNetRequest;
    private String address;
    private PopupWindow popupWindowSex;
    private PopupWindow popupWindow;
    private String referrerPhone;

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                if (rest == PackageManager.PERMISSION_GRANTED) {
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ViewUtils.inject(this);
        tv_back.setText("个人信息");
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.userinfor_photo)
                .showImageForEmptyUri(R.mipmap.userinfor_photo).showImageOnFail(R.mipmap.userinfor_photo)
                // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        accountNetRequest = new AccountNetRequest(mContext);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RequestOftenKey.getUserInfor(UserInfoActivity.this, sp);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startMyDialog();
                            setData();
                            stopMyDialog();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void setData() {
        if (isCert()) {
            tvRealName.setText("已认证");
        } else {
            tvRealName.setText("未认证");
        }
        String phone = sp.getString("phone", "");
        if (!TextUtils.isEmpty(phone)) {
            this.phone.setText(phone);
            this.phone.setClickable(false);//不可点击绑定
            this.phone.setCompoundDrawables(null, null, null, null);
        } else {
            //无手机号，可以点击绑定手机号
            this.phone.setText("未绑定");
            llPhone.setClickable(true);
            llPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, BindPhoneActivity.class);
                    startActivity(intent);
                }
            });
        }


        nickname.setText(sp.getString(Constants.USER_NAME, "未设置"));
        tvSignature.setText(sp.getString(Constants.STATE_OF_MIND, ""));
        String levelStr = PreferenceUtils.readStrConfig(Constants.USER_LEVEL, mContext, "-1");
        switch (levelStr) {
            case "0":
                levelStr = "普通会员";
                break;
            case "1":
                levelStr = "VIP会员";
                break;
            case "2":
                levelStr = "个体商家";
                break;
            case "3":
                levelStr = "实体商家";
                break;
            case "4":
                levelStr = "城市代理";
                break;
            case "5":
                levelStr = "省份代理";
                break;
            default:
                levelStr = "";
                break;
        }
        tvLevel.setText(levelStr);

        address = sp.getString("cityName", "") + " " + sp.getString("districtName", "");

        Logger.i(address);

//        amendAddress.setText(TextUtils.isEmpty(address.trim()) ? "请设置地区" : address);
//        推荐人
        referrerPhone = sp.getString(Constants.REFERRER_PHONE, "");
        if (!referrerPhone.equals("0")) {
            referrer.setText(referrerPhone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));//推荐人手机号码，中间四位隐藏
            referrer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
        String imageUrl = PreferenceUtils.readStrConfig(Constants.PHOTO, mContext, "");
        GlideUtil.showCirHeadNoSuffix(mContext, imageUrl, user_photo);


    }

    /**
     * 修改昵称
     *
     * @param view
     */
    public void amendName(View view) {
        Intent intent = new Intent(UserInfoActivity.this, EditUserNameViewImplActivity.class);
//        intent.putExtra("key", "name");
//        intent.putExtra("title", "修改昵称");
        startActivity(intent);

    }

    /**
     * 修改性别
     *
     * @param view
     */
    public void amendSex(View view) {
        getSexPopupWindow(R.layout.popupwindow_sex, -2, -2, R.style.AnimationSEX);
        // 这里是位置显示方式,在屏幕的中间
        popupWindowSex.showAtLocation(view, Gravity.CENTER, 0, 0);
        popupWindowSex.setOnDismissListener(new poponDismissListener());
    }

    /***
     * 获取SexPopupWindow实例
     */
    private void getSexPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindowSex) {
            popupWindowSex.dismiss();
            backgroundAlpha(0.5f);
            return;
        } else {
            initSexPopuptWindow(resource, width, height, animationStyle);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    /**
     * 选择性别PopupWindow
     */
    protected void initSexPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null,
                false);
        // 创建PopupWindow实例,200,LayoutParams.MATCH_PARENT分别是宽度和高度
        RadioButton radioGirl = (RadioButton) popupWindow_view.findViewById(R.id.girl);
        RadioButton radioBoy = (RadioButton) popupWindow_view.findViewById(R.id.boy);
        if (sp.getString("sex", "0").equals("1")) {//男
            radioBoy.setChecked(true);
            radioGirl.setChecked(false);
        } else if (sp.getString("sex", "0").equals("2")) {//女
            radioBoy.setChecked(false);
            radioGirl.setChecked(true);
        } else {//未知
            radioBoy.setChecked(false);
            radioGirl.setChecked(false);
        }
        backgroundAlpha(0.5f);
        popupWindowSex = new PopupWindow(popupWindow_view, width, height, true);
        // 点击其他地方消失
        popupWindow_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (popupWindowSex != null && popupWindowSex.isShowing()) {
                    popupWindowSex.dismiss();
                    backgroundAlpha(1f);
                    popupWindowSex = null;
                }
                return false;
            }
        });
    }

    /**
     * 修改地址
     *
     * @param view
     */
    public void amendAddress(View view) {
        startActivityForResult(new Intent(this, CitySelectionActivity.class), 0);
    }

    /**
     * 查看或设置推荐人
     */
    public void setReferrer(View view) {
        if (null == referrerPhone || !referrerPhone.equals("0")) {//根据推荐人手机号码是否为0判断是否有推荐人
            return;
        } else {
            startActivity(new Intent(this, SettingReferrerActivity.class));
        }
    }

    /**
     * 我的二维码
     *
     * @param view
     */
    public void myTwodimensionCode(View view) {
        startActivity(new Intent(UserInfoActivity.this, MyQRCodeActivity.class));
    }

    /**
     * 我的地址
     *
     * @param view
     */
    public void myAddressManage(View view) {
        Intent intent = new Intent(UserInfoActivity.this, AddressManageActivity.class);
        intent.putExtra("FLAG", "UserInfo");//标记从我的收获地址进入
        startActivity(intent);
    }

    /**
     * 账号与安全
     *
     * @param view
     */
    public void accountSecurity(View view) {
        if (TextUtils.isEmpty(sp.getString("phone", ""))) {
            startActivity(new Intent(this, BindPhoneActivity.class));
        } else {
            startActivity(new Intent(this, AccountSecurityActivity.class));
        }
    }

    /**
     * 我的银行卡
     *
     * @param view
     */
    public void myBankCard(View view) {
        if (isCert()) {
            Intent intent = new Intent(mContext, MyBankCardsActivity.class);
            startActivityForResult(intent, CARD_LIST_REQUEST_CODE);
        } else {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.mvp.view.CertificationViewImplActivity"));
            startActivity(intent);
        }
    }

    /**
     * 退出
     *
     * @param view
     */
    public void logout(View view) {
        startMyDialog();
        accountNetRequest.LogOut(BaseEntity.class, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                //刷新个人页面标识
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT_LOADING, true, mContext);
                //刷新购物车页面标识
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_SHOP_FRAGMENT, true, mContext);

                PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, false, mContext);
                PreferenceUtils.writeStrConfig(Constants.SPKEY_TOKEN, "0", mContext);
                PreferenceUtils.writeStrConfig(Constants.AGENT_STATUS, "0", mContext);//重置申请服务中心状态，保证首页服务中心状态正常
                PreferenceUtils.writeStrConfig(Constants.MERCHANT_LEV, "0", mContext);//重置会员等级，保证首页会员等级状态正常
                PreferenceUtils.writeBoolConfig(Constants.ON_OFF_VOICE, false, mContext);//关闭语音播报开关
                com.yilian.mall.utils.PreferenceUtils.writeBoolConfig(Constants.RED_PACKET_SHOW, false, getApplicationContext());
                String userId = PreferenceUtils.readStrConfig(Constants.USER_ID, mContext);
                Logger.i("解绑使用userID：" + userId);
                PushManager.getInstance().unBindAlias(mContext, userId, false);
                com.yilian.mall.utils.PreferenceUtils.writeStrConfig(Constants.USER_ID, "0", mContext);//重置user_id，保证我的-线上商城角标正常
                showToast("您已退出登录状态");
                finish();
                DataCleanManager.cleanSharedPreference(mContext);
                FileUtils.delFile("/com.yilian/userphoto/userphoto.png");

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
                Logger.i("退出登录 Exception" + e + " content " + s);
            }
        });
    }

    /**
     * @param view 点击头像
     * @param view
     */
    public void amendUserPhoto(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1, LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    /***
     * 获取头像PopupWindow实例
     */
    private void getPhotoPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPhotoPopuptWindow(resource, width, height, animationStyle);
        }
    }

    /**
     * 头像PopupWindow
     */
    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        // 点击其他地方消失
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    /**
     * 修改性别 男
     *
     * @param view
     */
    public void man(View view) {
        popupWindowSex.dismiss();
        GetUserInfoEntity info = new GetUserInfoEntity();

        GetUserInfoEntity.UserInfo userInfo = info.new UserInfo();
        userInfo.sex = "1";
        accountNetRequest.setUserInfo(userInfo, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {

                switch (responseInfo.result.code) {
                    case 1:
                        showToast("修改成功！");
                        sp.edit().putString("sex", "1").commit();
//                        sex.setText("男");
                        break;
                    case -3:
                        showToast("系统繁忙！");
                        break;
                    case -4:
                        showToast("登录状态失效，请重新登录！");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 修改性别 女
     *
     * @param view
     */
    public void woman(View view) {
        popupWindowSex.dismiss();
        GetUserInfoEntity info = new GetUserInfoEntity();

        GetUserInfoEntity.UserInfo userInfo = info.new UserInfo();

        userInfo.sex = "2";
        accountNetRequest.setUserInfo(userInfo, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {

                switch (responseInfo.result.code) {
                    case 1:
                        showToast("修改成功！");
                        sp.edit().putString("sex", "1").commit();
//                        sex.setText("女");
                        break;
                    case -3:
                        showToast("系统繁忙！");
                        break;
                    case -4:
                        showToast("登录状态失效，请重新登录！");
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            openCamera();
                        } else {
                            requestWrite_External_Storage();
                        }
                    } else {
                        showToast("拍照权限被拒绝");
                    }
                }
                break;
            case APPLY_WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        showToast("存储被拒绝,无法拍照");
                    }

                }
                break;
            case APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        albumSelect();
                    } else {
                        showToast("请开启读取SD卡的权限");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 开启照相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);

        FileUtils.startActionCapture(UserInfoActivity.this, file, ALBUM_CAMERA_REQUEST_CODE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, ALBUM_CAMERA_REQUEST_CODE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 申请存储卡写权限
     */
    private void requestWrite_External_Storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
    }

    /**
     * 选择相册
     */
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//跳转至相册相册存在的图片url
        startActivityForResult(intent, PHOTO_ALBUM);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        Logger.i("onActivityResult" + "RequestCode" + requestCode);
        String fileSrc;
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    String[] cityInfo = data.getExtras().getString("cityInfo").split(",");

                    //sendUserInfo(cityInfo[3], cityInfo[5]);
//                    amendAddress.setText(cityInfo[2] + " " + cityInfo[4]);
                }
                break;
            case ALBUM_CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (data == null) {
                    Logger.i("resultCode4  " + resultCode + "  requestCode " + requestCode + " intent:" + data);
                    String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                    int i = BitmapUtil.readPictureDegree(pathname);//获取照片被旋转的角度
                    Logger.i("照片旋转角度：" + i);
                    File picFile = new File(pathname);
                    try {
                        File file = compressImage(picFile);
                        if (file != null) {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                            Bitmap bitmap1 = BitmapUtil.rotaingImageView(i, bitmap);//将照片旋转的角度还原过来，解决三星手机拍照自动旋转90度的问题
                            Uri uri = saveBitmap(bitmap1);
                            File file1 = new File(new URI(uri.toString()));
                            upLoadImage(file1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case PHOTO_ALBUM:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (null == data) {
                    return;
                }
                Logger.i("data.getData().getScheme  " + data.getData().getScheme());
                if ("file".equals(data.getData().getScheme())) {
                    //有些低版本返回Uri模式为file
                    fileSrc = data.getData().getPath();
                } else {
                    Uri selectImage = data.getData();
                    Logger.i("data.getData().getScheme33333  " + selectImage);
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectImage, filePathColumns, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    fileSrc = cursor.getString(columnIndex);
                    cursor.close();
                }
                Bitmap smallBitmap = BitmapUtil.getSmallBitmap(fileSrc, 350, 350);
                Logger.i("imagePath  " + fileSrc);
                Logger.e("smallBitmap " + smallBitmap);
                File file = new File(fileSrc);
                try {
                    File compressFile = compressImage(file);
                    if (compressFile != null) {
                        upLoadImage(compressFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void camera(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, APPLY_CAMERA_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }
//            有权限了
        openCamera();


    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }

        albumSelect();
    }

    private File compressImage(File picFile) throws IOException {
        if (picFile == null) {
            return null;
        }
        File file = new Compressor(this)
                .setMaxHeight(150)
                .setMaxWidth(150)
                .setQuality(75)
                .compressToFile(picFile);
        try {
            long fileSize = FileUtils.getFileSize(file);
            if (fileSize > 256000) {
                file = compressImage(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yilian/userphoto/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "user.png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 打开裁剪页面
     *
     * @param uri
     */
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.merchant_camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 210);
        intent.putExtra("outputY", 210);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }


    /**
     * 文件格式的上传头像
     * 256000bit
     */
    private void upLoadImage(File file) {
        startMyDialog(false);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imgPhoto = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", requestBody);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .uploadHeadImage(imgPhoto, new Callback<UploadImageEnity>() {
                    @Override
                    public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        //保存服务器返回的头像链接
                                        String filename = response.body().filename;
                                        com.yilian.mylibrary.PreferenceUtils.writeStrConfig(com.yilian.mylibrary.Constants.PHOTO, filename, mContext);
                                        if (!TextUtils.isEmpty(filename)) {
                                            //刷新个人页面标识
                                            sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                            Logger.i("更新头像URL  " + filename);
                                            GlideUtil.showCirHeadNoSuffix(mContext, filename, user_photo);
                                        } else {
                                            GlideUtil.showCirHeadNoSuffix(mContext, filename, user_photo);
                                        }
                                        showToast("上传成功");
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                        stopMyDialog();

                    }
                });
    }


    private void sendUserInfo(String cityId, String countyId) {

        GetUserInfoEntity.UserInfo userInfo = new GetUserInfoEntity().userInfo;

        userInfo.city = cityId;
        userInfo.district = countyId;

        accountNetRequest.setUserInfo(userInfo, new RequestCallBack<BaseEntity>() {

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> arg0) {
                // TODO Auto-generated method stub

                RequestOftenKey.getUserInfor(mContext, sp);
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 个性签名
     *
     * @param view
     */
    public void signature(View view) {
        Intent intent = new Intent(mContext, EditUserNameViewImplActivity.class);
        intent.putExtra("editType", 1);
        startActivity(intent);
    }

    /**
     * 基本信息
     *
     * @param view
     */
    public void basicInformation(View view) {
        Intent intent = new Intent(mContext, BasicInfomationViewImplActivity.class);
        startActivity(intent);
    }

    /**
     * 实名认证
     *
     * @param view
     */
    public void realName(View view) {
        if (!isCert()) {
            Intent intent = new Intent(mContext, CertificationViewImplActivity.class);
            intent.putExtra(IS_FROM_USER_INFO_ACTIVITY,true);
            startActivity(intent);
        } else {
            Intent intent = new Intent(mContext, CertificationResultImplViewActivity.class);
            intent.putExtra(IS_FROM_USER_INFO_ACTIVITY,true);
            startActivity(intent);
        }
    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

}
