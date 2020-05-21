package com.leshan.ylyj.view.activity.creditmodel;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.presenter.DringLicensePrensent;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxImageTool;
import com.yilian.mylibrary.ALiCardBody;
import com.yilian.mylibrary.ALiIdCardFaceEntity;
import com.yilian.mylibrary.AliIdCardBackEntity;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.ALiCarBackDataValue;
import rxfamily.entity.ALiCarPositiveDataValue;
import rxfamily.entity.ALiCarSuccessDataInfo;
import rxfamily.entity.AuthenticationInfoEntity;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.UploadDriverImgEntity;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 驾驶证识别上传
 */
public class MyCarLicenseActivity extends BaseActivity implements View.OnClickListener {

    Dialog dialog;
    boolean positive, back;
    private RelativeLayout shootCarPositive, shootCarBack;
    ALiCarSuccessDataInfo info;
    private ImageView shootPositiveImg, shootBackImg;
    CreditPresenter presenter;
    /**
     * 驾驶证 正反面 “face" "back"
     */
    private String side, positiveImg, backImg;
    private int requestCode;
    private static String ImageName;
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;
    public static final int ALBUM_CAMERA_BACK_REQUEST_CODE = 2;
    public static final int APPLY_CAMERA_REQUEST_CODE = 999;
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 101;//写SD卡的权限
    private LinearLayout beforeShootPositive, beforeShootBack;
    private TextView shootSuccessPositive, shootSuccessBack, carTitle, car_title;
    private DringLicensePrensent mPresent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_car_license);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        info = new ALiCarSuccessDataInfo();
        shootPositiveImg = (ImageView) findViewById(R.id.shoot_positive_img);
        shootBackImg = (ImageView) findViewById(R.id.shoot_back_img);
        beforeShootPositive = (LinearLayout) findViewById(R.id.before_shoot_positive);
        beforeShootBack = (LinearLayout) findViewById(R.id.before_shoot_back);
        //拍摄成功
        shootSuccessPositive = (TextView) findViewById(R.id.shoot_success_positive);
        shootSuccessBack = (TextView) findViewById(R.id.shoot_success_back);
        carTitle = (TextView) findViewById(R.id.car_title);
        car_title = (TextView) findViewById(R.id.car_title);

    }

    @Override
    protected void initListener() {
        shootPositiveImg.setOnClickListener(this);
        shootBackImg.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        //@net 初始化presenter，并绑定网络回调，
        if (mPresent == null) {
            mPresent = new DringLicensePrensent(mContext, this);
        }
        //@net 初始化presenter，并绑定网络回调，
        if (presenter == null) {
            presenter = new CreditPresenter(mContext, this);
        }
        //获取用户姓名回显
        mPresent.getAuthenticationInfoVisiable(mContext);
    }

    /**
     * 识别失败
     */
    private void DialogWhether() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_identification_failure, null);
            TextView identification_ok = view1.findViewById(R.id.identification_ok);
            identification_ok.setOnClickListener(this);
            builder.setView(view1);
            dialog = builder.create();
        }
        dialog.show();

    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.shoot_positive_img) {
            //证件照主页
            side = "face";
            openCamera(ALBUM_CAMERA_REQUEST_CODE);
        } else if (i == R.id.shoot_back_img) {
            //证件照副页
            side = "back";
            openCamera(ALBUM_CAMERA_BACK_REQUEST_CODE);
        } else if (i == R.id.identification_ok) {
            dialog.dismiss();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera(requestCode);
                    } else {
                        showToast("拍照权限被拒绝");
                    }
                }
                break;
            case APPLY_WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera(requestCode);
                    } else {
                        showToast("存储被拒绝,无法拍照");
                    }

                }
                break;
        }
    }

    /**
     * 开启照相机
     */
    private void openCamera(int requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, APPLY_CAMERA_REQUEST_CODE);
            return;
        }

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
            return;
        }
//            有权限了
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);
        FileUtils.startActionCapture(this, file, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.requestCode = requestCode;
        //从拍照界面返回 证件照主页
        if (requestCode == ALBUM_CAMERA_REQUEST_CODE || requestCode == ALBUM_CAMERA_BACK_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                File picture = new File(pathname);
                if (null != picture || !picture.equals("")) {
                    if (null != picture || !picture.equals("")) {
                        Luban.with(mContext).load(pathname)
                                .setCompressListener(new OnCompressListener() {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(File file) {
                                        try {
                                            InputStream fileInputStream = new FileInputStream(file);
                                            byte[] bytes = new byte[fileInputStream.available()];
                                            int length = fileInputStream.read(bytes);
                                            String base64Strin = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
                                            Logger.i("base64Strin:" + base64Strin.length());
                                            checkDriverCard(base64Strin, file, side);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {

                                    }
                                }).launch();
                    }
                }
            }
        }
    }

    /**
     * 根据阿里api检测驾驶证照片信息
     *
     * @param bitMapBase64Code
     * @param picture
     * @param side
     */
    private void checkDriverCard(String bitMapBase64Code, final File picture, String side) {
        startMyDialog(false);
        showToast("证件识别中");
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .checkDriverCard(ALiCardBody.getInstance().getDriverCarRequestHeader(), ALiCardBody.getInstance().getIdCardRequestBody(bitMapBase64Code, side))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        //识别失败
                        DialogWhether();
                        Logger.i("驾驶证识别Error：" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String string = null;
                        try {
                            string = responseBody.string();
                            switch (requestCode) {
                                case ALBUM_CAMERA_REQUEST_CODE:
                                    ALiIdCardFaceEntity aLiIdCardEntity = new Gson().fromJson(string, ALiIdCardFaceEntity.class);
                                    List<ALiIdCardFaceEntity.OutputsBean> outputs = aLiIdCardEntity.outputs;
                                    ALiIdCardFaceEntity.OutputsBean outputsBean = outputs.get(0);
                                    ALiIdCardFaceEntity.OutputsBean.OutputValueBean outputValue = outputsBean.outputValue;
                                    String dataValue = outputValue.dataValue;
                                    ALiCarPositiveDataValue value = new Gson().fromJson(dataValue, ALiCarPositiveDataValue.class);
                                    Logger.i("驾驶证主页识别：" + string + "/n 返回的Value信息：" + value.toString());
                                    //识别成功
                                    boolean success = value.success;
                                    if (success == false) {
                                        //识别失败
                                        DialogWhether();
                                    } else {
                                        info.setName(value.name);  //姓名
                                        info.setNum(value.num);    //驾驶证号
                                        info.setVehicleType(value.vehicleType); //驾驶证准驾车型
                                        info.setStartDate(value.startDate);    //驾驶证有效期开始时间
                                        info.setEndDate(value.endDate);       //驾驶证有效期时长
                                        Logger.i("识别驾驶证主页成功！" + info.toString());
                                        uploadImg(picture);
                                    }
                                    break;
                                case ALBUM_CAMERA_BACK_REQUEST_CODE:
                                    AliIdCardBackEntity aliIdCardBackEntity = new Gson().fromJson(string, AliIdCardBackEntity.class);
                                    List<AliIdCardBackEntity.OutputsBean> outputs1 = aliIdCardBackEntity.outputs;
                                    AliIdCardBackEntity.OutputsBean outputsBean1 = outputs1.get(0);
                                    AliIdCardBackEntity.OutputsBean.OutputValueBean outputValue1 = outputsBean1.outputValue;
                                    ALiCarBackDataValue value1 = new Gson().fromJson(outputValue1.dataValue, ALiCarBackDataValue.class);
                                    Logger.i("驾驶证副页识别：" + string + "/n 返回的Value信息：" + value1.toString());
                                    //识别成功与否
                                    boolean success1 = value1.success;
                                    if (success1 == false) {
                                        //识别失败
                                        DialogWhether();
                                    } else {
                                        //档案编号
                                        String archiveNo = value1.archiveNo;
                                        info.setArchiveNo(value1.archiveNo);
                                        Logger.i("识别驾驶证副页成功！" + info.toString());
                                    }
                                    uploadImg(picture);
                                    break;
                                default:
                                    break;
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void uploadImg(File picture) {
        startMyDialog(false);
        showToast("证件上传中");
        Logger.i("图片名字" + picture.getName().toString());
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), picture);
        MultipartBody.Part imgPhoto = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment.png", requestBody);
        Logger.i("图片:" + imgPhoto.headers().toString());
        presenter.uploadDriverImg(imgPhoto);

    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.toString());
        ToastUtil.showMessage(this, "上传失败！" + e.getMessage());
        stopMyDialog();
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("获取数据结果");
        if (baseEntity instanceof AuthenticationInfoEntity) {
            //设置用户信息
            AuthenticationInfoEntity entity = (AuthenticationInfoEntity) baseEntity;
            Log.i("TAG", "用户信息回显: " + entity.info.toString());
            Log.i("TAG", "用户姓名回显: " + entity.info.userName);
            String owerName = entity.info.userName;
            if (!TextUtils.isEmpty(owerName)) {
                String anthInfo = "请拍摄" + owerName + "的驾驶证，并录入信息";
                SpannableString spannableString = new SpannableString(anthInfo);
                ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#F39826"));
                spannableString.setSpan(colorSpan, 3, owerName.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                car_title.setText(spannableString);
            }
        }
        if (baseEntity instanceof UploadDriverImgEntity) {
            String imageUrl = ((UploadDriverImgEntity) baseEntity).getData().getPicUrl();
            Logger.i("图片ImageUrl:" + imageUrl);
            switch (requestCode) {
                case ALBUM_CAMERA_REQUEST_CODE:
                    //显示图片
                    GlideUtil.showImageRadius(mContext, imageUrl, shootPositiveImg, RxImageTool.dp2px(6));
                    beforeShootPositive.setVisibility(View.GONE); // 隐藏
                    shootSuccessPositive.setVisibility(View.VISIBLE);//显示
                    positive = true;
                    positiveImg = imageUrl;
                    if (info != null && back == true && positiveImg != null && backImg != null) {
                        SkipUtils.toCheckCarLicense(MyCarLicenseActivity.this, info, positiveImg, backImg);
                    }
                    break;
                case ALBUM_CAMERA_BACK_REQUEST_CODE:
                    GlideUtil.showImageRadius(mContext, imageUrl, shootBackImg, RxImageTool.dp2px(6));
                    beforeShootBack.setVisibility(View.GONE); // 隐藏
                    shootSuccessBack.setVisibility(View.VISIBLE);//显示
                    back = true;
                    backImg = imageUrl;
                    if (info != null && positive == true && positiveImg != null && backImg != null) {
                        SkipUtils.toCheckCarLicense(MyCarLicenseActivity.this, info, positiveImg, backImg);
                    }
                    break;
            }
        }
    }

}
