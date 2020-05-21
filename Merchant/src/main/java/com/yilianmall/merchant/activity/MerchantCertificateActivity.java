package com.yilianmall.merchant.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.MerchantAuthInfoEntity;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.ImageCompressUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 资料认证
 */
public class MerchantCertificateActivity extends BaseActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {


    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvIdentityAnnouncements;
    private ImageView ivCardFront;
    private ImageView ivCardReverse;
    private TextView tvDataAnnouncements;
    private ImageView icCardPerson;
    private TextView tvEntityAnnouncements;
    private ImageView ivLicense;
    private ImageView ivLogo;
    private LinearLayout llEntityPhoto;
    private CheckBox checkbox;
    private TextView tvAgreement;
    private Button btnSubmit;
    private String ImageName, ImagePrefix;
    private String merchantId;
    private TextView tvPersonageMsg;
    private PopupWindow popupWindow;
    private File picFile;
    private Uri uri;
    private String fileSrc;
    private EditText etIdCrad;
    private EditText etName;
    private String idFrontFileNeam = "", idReverseFileNeam = "", cardHoldFileNeam = "", businessLicenceFileNeam = "", companyLogoFileNeam = "";
    private String merchantType;
    private ScrollView scrollView;
    private LinearLayout llLoadError;
    private TextView tvRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_personal_certificate);
        initView();
        initData();
    }

    /**
     * 处理资料回显
     */
    private void initData() {
        startMyDialog(false);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMerchantAuthInfo(new Callback<MerchantAuthInfoEntity>() {
                    @Override
                    public void onResponse(Call<MerchantAuthInfoEntity> call, Response<MerchantAuthInfoEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            HttpResultBean bean = response.body();
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        llLoadError.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.VISIBLE);
                                        MerchantAuthInfoEntity.DataBean merchantInfo = response.body().data;
                                        etName.setText(merchantInfo.cardName);
                                        etName.setSelection(merchantInfo.cardName.length());
                                        etIdCrad.setText(merchantInfo.cardId);
                                        GlideUtil.showImageWithSuffix(mContext, merchantInfo.cardFront, ivCardFront);
                                        idFrontFileNeam = merchantInfo.cardFront;
                                        GlideUtil.showImageWithSuffix(mContext, merchantInfo.cardReverse, ivCardReverse);
                                        idReverseFileNeam = merchantInfo.cardReverse;
                                        GlideUtil.showImageWithSuffix(mContext, merchantInfo.cardPerson, icCardPerson);
                                        cardHoldFileNeam = merchantInfo.cardPerson;
                                        GlideUtil.showImageWithSuffix(mContext, merchantInfo.license, ivLicense);
                                        businessLicenceFileNeam = merchantInfo.license;
                                        GlideUtil.showImageWithSuffix(mContext, merchantInfo.logo, ivLogo);
                                        companyLogoFileNeam = merchantInfo.logo;
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<MerchantAuthInfoEntity> call, Throwable t) {
                        scrollView.setVisibility(View.GONE);
                        llLoadError.setVisibility(View.VISIBLE);
                        Logger.e("Class  " + getClass().getSimpleName() + "  throwable  " + t);
                        stopMyDialog();
                    }
                });
    }


    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        tvPersonageMsg = (TextView) findViewById(R.id.tv_personage_msg);
        etName = (EditText) findViewById(R.id.et_name);
        etIdCrad = (EditText) findViewById(R.id.et_idcard);
        tvIdentityAnnouncements = (TextView) findViewById(R.id.tv_identity_announcements);
        tvIdentityAnnouncements.setOnClickListener(this);
        ivCardFront = (ImageView) findViewById(R.id.iv_card_front);
        ivCardFront.setOnClickListener(this);
        ivCardReverse = (ImageView) findViewById(R.id.iv_card_reverse);
        ivCardReverse.setOnClickListener(this);
        tvDataAnnouncements = (TextView) findViewById(R.id.tv_data_announcements);
        tvDataAnnouncements.setOnClickListener(this);
        icCardPerson = (ImageView) findViewById(R.id.iv_card_person);
        icCardPerson.setOnClickListener(this);
        tvEntityAnnouncements = (TextView) findViewById(R.id.tv_entity_announcements);
        tvEntityAnnouncements.setOnClickListener(this);
        ivLicense = (ImageView) findViewById(R.id.iv_license);
        ivLicense.setOnClickListener(this);
        ivLogo = (ImageView) findViewById(R.id.iv_logo);
        ivLogo.setOnClickListener(this);
        llEntityPhoto = (LinearLayout) findViewById(R.id.ll_entity_photo);
        llLoadError = (LinearLayout) findViewById(R.id.ll_load_error);
        tvRefresh = (TextView) findViewById(R.id.tv_refresh);

        //当前需提交的是个体还是实体
        merchantType = getIntent().getStringExtra("merchantType");
        merchantId = getIntent().getStringExtra("merchantId");
        Logger.i("merchantType  " + merchantType);
        switch (merchantType) {
            case "0"://个体
                v3Title.setText("个体资料认证");
                tvPersonageMsg.setText("提交个人身份信息");
                llEntityPhoto.setVisibility(View.GONE);
                break;
            case "1"://实体或服务中心
                v3Title.setText("实体资料认证");
                tvPersonageMsg.setText("提交法人身份信息");
                llEntityPhoto.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        checkbox.setChecked(true);
        Spanned spanned1 = Html.fromHtml("我已阅读并接受<font color='#fe5062'><u>益联益家用户协议</u></font>");
        tvAgreement = (TextView) findViewById(R.id.tv_agreement);
        tvAgreement.setText(spanned1);
        tvAgreement.setOnClickListener(this);
        btnSubmit = (Button) findViewById(R.id.btn_submit);

        v3Back.setOnClickListener(this);
        tvAgreement.setOnClickListener(this);//商家协议
        btnSubmit.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        String url=null;
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.btn_submit) {
            //提交
            submit();

        } else if (i == R.id.tv_data_announcements) {//提交认证资料的注意事项
            //广播跳转界面
            url=Constants.PERSONAL_INFO_NOTICE;

        } else if (i == R.id.tv_entity_announcements) {//提交实体资料的注意事项
            //广播跳转界面
            url=Constants.CERTIFICATED_INFORMATION;

        } else if (i == R.id.tv_identity_announcements) {//提交个人资料的注意事项

            url=Constants.PERSONAGE_IDCARD;
        } else if (i == R.id.iv_card_front) {
            ImagePrefix = "idFront";//不需要做图片点击放大
            initImageName(ImagePrefix);
            showPopupVindow(view);
        } else if (i == R.id.iv_card_reverse) {
            ImagePrefix = "idReverse";
            initImageName(ImagePrefix);
            showPopupVindow(view);
        } else if (i == R.id.iv_card_person) {
            ImagePrefix = "cardHold";
            initImageName(ImagePrefix);
            showPopupVindow(view);
        } else if (i == R.id.iv_license) {
            ImagePrefix = "businessLicence";
            initImageName(ImagePrefix);
            showPopupVindow(view);
        } else if (i == R.id.iv_logo) {
            ImagePrefix = "companyLogo";
            initImageName(ImagePrefix);
            showPopupVindow(view);
        } else if (i == R.id.tv_agreement) {
            //跳转网页商家入驻协议
            switch (merchantType) {
                case "0"://个体
                    url= Constants.PERSONAGE_MERCHANT;
                    break;
                case "1":
                    url=Constants.MERCHANT_REGISTE;
                    break;
            }
            JumpToOtherPageUtil.getInstance().jumpToWebViewActivity(mContext,url,false);
        } else if (i == R.id.tv_refresh) {
            initData();
        }
    }

    public String getEtCard() {
        Logger.i("getEtCard  " + etIdCrad.getText().toString().trim());
        return etIdCrad.getText().toString().trim();
    }

    public String getEtName() {
        return etName.getText().toString().trim();
    }

    private void submit() {
        String name = getEtName();
        Logger.i("NAME1111  " + name);
        if (TextUtils.isEmpty(name)) {
            showToast("请输入您的姓名");
            return;
        }
        Logger.i("idCard111  " + getEtCard() + RegExUtils.isIdCard(getEtCard()));
//        取消身份证号格式限制
        if (TextUtils.isEmpty(getEtCard())) {
            showToast("请输入正确的身份证号码");
            return;
        }
        if (TextUtils.isEmpty(idFrontFileNeam)) {
            showToast("请上传身份证正面图片");
            return;
        }
        if (TextUtils.isEmpty(idReverseFileNeam)) {
            showToast("请上传身份证反面图片");
            return;
        }
        if (TextUtils.isEmpty(cardHoldFileNeam)) {
            showToast("请上传本人手持证件照");
            return;
        }
        switch (merchantType) {
            case "1"://实体商家
                if (TextUtils.isEmpty(businessLicenceFileNeam)) {
                    showToast("请上传营业执照扫描件");
                    return;
                }
                if (TextUtils.isEmpty(companyLogoFileNeam)) {
                    showToast("请上传商户logo图片");
                    return;
                }
                break;
            case "0"://防止个体上家的时候返-17
                businessLicenceFileNeam = "";
                companyLogoFileNeam = "";
                break;
        }
        if (!checkbox.isChecked()) {
            showToast("请阅读益联益家网用户协议");
            return;
        }
        startMyDialog();
        Logger.i("cardName  " + name + "  idCard  " + getEtCard() + " merchantId  " + merchantId + " DEVICEINDEX " + RequestOftenKey.getDeviceIndex(mContext));
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .submitMerchantAuth(merchantId, merchantType, idFrontFileNeam, idReverseFileNeam, cardHoldFileNeam, businessLicenceFileNeam,
                        companyLogoFileNeam, getEtCard(), name, new Callback<BaseEntity>() {
                            @Override
                            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                    if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                        switch (response.body().code) {
                                            case 1:
                                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                                showToast(response.body().msg);
                                                stopMyDialog();
                                                finish();
                                                break;
                                            default:
                                                stopMyDialog();
                                                finish();
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseEntity> call, Throwable t) {
                                stopMyDialog();
                                showToast(R.string.network_module_net_work_error);
                            }
                        });

    }


    private void initImageName(String state) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        Date date = new Date(System.currentTimeMillis());
        String name = simpleDateFormat.format(date);
        ImageName = name + state;

    }

    private void showPopupVindow(View view) {//-1MATCH_PATENT
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1,
                LinearLayout.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroudAlpha(0.2f);
    }

    private void getPhotoPopupWindow(int resoure, int width, int height, int animat) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopupWindow(resoure, width, height, animat);
        }

    }

    private void initPopupWindow(int resoure, int width, int height, int animat) {
        View view = getLayoutInflater().inflate(resoure, null, false);
        popupWindow = new PopupWindow(view, width, height, true);
        popupWindow.setAnimationStyle(animat);
        backgroudAlpha(0.2f);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
    }

    private void backgroudAlpha(float alpha) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = alpha;
        getWindow().setAttributes(attributes);
        ColorDrawable colorDrawable = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroudAlpha(1f);//pop消失的时候不透明
            }
        });
    }

    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;

    /**
     * 申请存储卡写权限
     */
    private void requestWrite_External_Storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
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
            //读取SD卡的权限
            case APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        albumeSelect();
                    } else {
                        showToast("请开启读取SD卡的权限");
                    }
                }

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
     * 开启照相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);
//        if (!file.exists()) {
//            file.mkdir();
//        }
        FileUtils.startActionCapture(this, file, GO_CAMERA);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, GO_CAMERA);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroudAlpha(1f);
        }
    }

    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//请求读取SD卡的权限使用请求码
    private static final int GO_CAMERA = 99;//打开相机请求码
    private static final int GO_ALBUM = 97;//打开相册请求码

    /**
     * 相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        ;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //暂无权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestWrite_External_Storage();
            return;
        }
        albumeSelect();
    }

    /**
     * 相册选择
     */
    private void albumeSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//跳转至相册相册存在的图片url
        startActivityForResult(intent, GO_ALBUM);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroudAlpha(1f);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GO_CAMERA:
                Logger.i("resultCode2  " + resultCode + "  requestCode " + requestCode + " intent:" + data);
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (data == null) {
                    Logger.i("resultCode4  " + resultCode + "  requestCode " + requestCode + " intent:" + data);
                    String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                    picFile = new File(pathname);
                    if (null != picFile || !picFile.equals("")) {
                        picFile = ImageCompressUtil.compressImage(picFile, ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext), 50, mContext);
                        picFile = BitmapUtil.restoreFile(pathname, picFile, "environment");//处理图片旋转问题
                        uploadImage(picFile);//上传图片加水印
                    }
                }
                break;
            case GO_ALBUM:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (null == data) {
                    return;
                }
                if ("file".equals(data.getData().getScheme())) {
                    //有些低版本返回Uri模式为file
                    fileSrc = data.getData().getPath();
                } else {
                    Uri selectImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectImage, filePathColumns, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    fileSrc = cursor.getString(columnIndex);
                    cursor.close();
                }
                if (null != fileSrc || !fileSrc.equals("")) {
                    File file = ImageCompressUtil.compressImage(new File(fileSrc), ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext), 50, mContext);
                    file = BitmapUtil.restoreFile(fileSrc, file, "environment");//处理图片旋转问题
                    uploadImage(file);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 上传图片加水印
     *
     * @param picFile
     */
    private void uploadImage(File picFile) {
        startMyDialog(false);
        //还没有处理压缩
        Logger.i("图片文件路径   " + picFile);
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), picFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", photoRequestBody);
        try {
            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                    .AddressuploadFile(photo, new Callback<UploadImageEnity>() {
                        @Override
                        public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                            stopMyDialog();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                switch (response.body().code) {
                                    case 1:
                                        //显示图片
                                        uploadImageView(response.body().filename);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                            showToast(R.string.network_module_net_work_error);
                            Logger.e("throwable  " + t);
                            stopMyDialog();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uploadImageView(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            ivCardFront.setImageResource(R.mipmap.merchant_default_jp);
            ivCardReverse.setImageResource(R.mipmap.merchant_default_jp);
            icCardPerson.setImageResource(R.mipmap.merchant_default_jp);
            ivLicense.setImageResource(R.mipmap.merchant_default_jp);
            ivLogo.setImageResource(R.mipmap.merchant_default_jp);
            return;
        }

        //返回的文件要加域名的前缀
        switch (ImagePrefix) {
            case "idFront":
                GlideUtil.showImageWithSuffix(mContext, imageUrl, ivCardFront);
                idFrontFileNeam = imageUrl.toString();
                break;
            case "idReverse":
                GlideUtil.showImage(mContext, imageUrl, ivCardReverse);
                idReverseFileNeam = imageUrl.toString();
                break;
            case "cardHold":
                GlideUtil.showImage(mContext, imageUrl, icCardPerson);
                cardHoldFileNeam = imageUrl.toString();
                break;
            case "businessLicence":
                GlideUtil.showImage(mContext, imageUrl, ivLicense);
                businessLicenceFileNeam = imageUrl.toString();
                break;
            case "companyLogo":
                companyLogoFileNeam = imageUrl.toString();
                GlideUtil.showImage(mContext, imageUrl, ivLogo);
                break;

        }
    }
}
