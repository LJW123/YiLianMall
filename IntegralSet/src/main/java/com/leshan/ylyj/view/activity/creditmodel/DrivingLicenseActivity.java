package com.leshan.ylyj.view.activity.creditmodel;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.bean.DrivingLicenseBody;
import com.leshan.ylyj.presenter.DringLicensePrensent;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.vondear.rxtools.RxImageTool;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Subscriber;
import rxfamily.entity.AuthenticationInfoEntity;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.DrivingLicenseEntity;
import rxfamily.entity.DrivingLicenseOutputValueEntity;
import rxfamily.entity.UploadDrivingImgEntity;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 行驶证
 *
 * @author zyh
 */
public class DrivingLicenseActivity extends BaseActivity implements View.OnClickListener {

    private Dialog dialog;
    boolean working = true;
    private RelativeLayout mRlShotHomepage;
    private RelativeLayout mRlShotVicepage;
    private String mHomepagePath;
    private String mVicePagePath;
    private int REQUEST_CAMERA_H = 0;
    private int REQUEST_CAMERA_V = 1;


    private ImageView mIvHomePage;
    private LinearLayout mLlHomepageNotice;
    private ImageView mIvVicePage;
    private LinearLayout mLlVicepageNotice;

    private String currentFilPath;
    private String storagePath;
    private String mHomeBitmapString;
    private String mViceBitmapString;
    private DrivingLicenseBody homeLicenseBody;
    private DrivingLicenseBody viceLicenseBody;
    private DrivingLicenseBody.Inputs homeInputs;
    private DrivingLicenseBody.Inputs viceInputs;

    private UploadDrivingImgEntity mHomeEntity;
    private UploadDrivingImgEntity mViceEntity;
    private DrivingLicenseOutputValueEntity curDrivingValue;
    private DrivingLicenseOutputValueEntity faceValue;
    private DrivingLicenseOutputValueEntity backValue;
    private String back;
    private String positive;
    private TextView tvCameraSuccess2;
    private TextView tvCameraSuccess1;
    private DringLicensePrensent mPresent;
    private TextView tvOwnerDriving;
    public static final int APPLY_CAMERA_REQUEST_CODE = 999;
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 101;//写SD卡的权限

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_license);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        storagePath = Environment.getExternalStorageDirectory().getPath();// 获取SD卡路径
        mHomepagePath = storagePath + "/" + "homepage.png";// 主页指定路径
        mVicePagePath = storagePath + "/" + "vicepage.png";// 副页指定路径
        mRlShotHomepage = (RelativeLayout) findViewById(R.id.rl_shot_homepage);
        mRlShotVicepage = (RelativeLayout) findViewById(R.id.rl_shot_vicepage);

        mIvHomePage = (ImageView) findViewById(R.id.iv_home_page);
        mLlHomepageNotice = (LinearLayout) findViewById(R.id.ll_homepage_notice);
        mIvVicePage = (ImageView) findViewById(R.id.iv_vice_page);
        mLlVicepageNotice = (LinearLayout) findViewById(R.id.ll_vicepage_notice);
        tvCameraSuccess1 = (TextView) findViewById(R.id.tv_camera_success1);
        tvCameraSuccess2 = (TextView) findViewById(R.id.tv_camera_success2);
        tvOwnerDriving = (TextView) findViewById(R.id.tv_ower_drving);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        jumpShotCamre(currentFilPath, requestCode);
                    } else {
                        showToast("拍照权限被拒绝");
                    }
                }
                break;
            case APPLY_WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        jumpShotCamre(currentFilPath, requestCode);
                    } else {
                        showToast("存储被拒绝,无法拍照");
                    }

                }
                break;
        }
    }

    /**
     * 调起相机拍照
     *
     * @param mFilePath
     */
    private void jumpShotCamre(String mFilePath, int code) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, APPLY_CAMERA_REQUEST_CODE);
            return;
        }

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
            return;
        }
        File file = new File(mFilePath);
        FileUtils.startActionCapture(this, file, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) { // 如果返回数据

            if (requestCode == REQUEST_CAMERA_H) {
                //压缩图片
                compressBitmap(currentFilPath, 0);
            } else {
                //压缩图片
                compressBitmap(currentFilPath, 1);
            }
        }
    }

    private File currentFile;

    /**
     * 压缩文件
     *
     * @param pathname
     * @param side
     */

    private void compressBitmap(String pathname, final int side) {
        Luban.with(mContext).load(pathname)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        currentFile = file;
                        try {
                            InputStream fileInputStream = new FileInputStream(file);
                            byte[] bytes = new byte[fileInputStream.available()];
                            int length = fileInputStream.read(bytes);
                            if (side == 0) {
                                mHomeBitmapString = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
                                homeLicenseBody.inputs.get(0).image.dataValue = mHomeBitmapString;
                            } else {
                                mViceBitmapString = Base64.encodeToString(bytes, 0, length, Base64.DEFAULT);
                                viceLicenseBody.inputs.get(0).image.dataValue = mViceBitmapString;
                            }
                            dringLicense(side);
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


    private int side = 0;

    @Override
    protected void initListener() {
        mRlShotHomepage.setOnClickListener(this);
        mRlShotVicepage.setOnClickListener(this);

    }


    @Override
    protected void initData() {
        mPresent = new DringLicensePrensent(mContext, this);
        //主页body类
        homeLicenseBody = new DrivingLicenseBody();
        homeLicenseBody.inputs = new ArrayList<>();
        homeInputs = new DrivingLicenseBody.Inputs();
        homeInputs.image = new DrivingLicenseBody.Inputs.Image();
        homeInputs.image.dataType = 50;
        homeLicenseBody.inputs.add(homeInputs);

        //副页body类
        viceLicenseBody = new DrivingLicenseBody();
        viceLicenseBody.inputs = new ArrayList<>();
        viceInputs = new DrivingLicenseBody.Inputs();
        viceInputs.image = new DrivingLicenseBody.Inputs.Image();
        viceInputs.image.dataType = 50;
        viceLicenseBody.inputs.add(viceInputs);

        //获取用户姓名回显
        mPresent.getAuthenticationInfoVisiable(mContext);
    }


    /**
     * 行驶证认证
     *
     * @param side
     */
    private void dringLicense(final int side) {
        startMyDialog(false);
        RequestBody requestBody = null;
        MediaType json = MediaType.parse("application/json; charset=utf-8");
        Gson gson = new Gson();
        if (side == 0) {
            String jsonData = gson.toJson(homeLicenseBody);
            requestBody = RequestBody.create(json, jsonData);
        } else {
            String jsonData = gson.toJson(viceLicenseBody);
            requestBody = RequestBody.create(json, jsonData);
        }
        mPresent.dringLicense(requestBody, new Subscriber<ResponseBody>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                stopMyDialog();
                DialogWhether();

            }

            @Override
            public void onNext(ResponseBody responseBody) {
                try {
                    String string = responseBody.string();
                    DrivingLicenseEntity drivingLicenseEntity = new Gson().fromJson(string, DrivingLicenseEntity.class);
                    List<DrivingLicenseEntity.Outputs> outputs = drivingLicenseEntity.outputs;
                    DrivingLicenseEntity.Outputs outputs1 = outputs.get(0);
                    DrivingLicenseEntity.Outputs.OutputValue outputValue = outputs1.outputValue;
                    String dataValue = outputValue.dataValue;
                    curDrivingValue = new Gson().fromJson(dataValue, DrivingLicenseOutputValueEntity.class);
                    if (side == 0) {
                        faceValue = curDrivingValue;
                    }
                    if (side == 1) {
                        backValue = curDrivingValue;
                    }
                    if (curDrivingValue.success) {
                        //成功
                        uploadFile();
                    } else {
                        //失败
                        DialogWhether();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 上传图片
     */
    private void uploadFile() {
        startMyDialog(false);
        showToast("证件上传中");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/png"), currentFile);
        MultipartBody.Part imgPhoto = MultipartBody.Part.createFormData("file", currentFile.getName(), requestBody);
        mPresent.uploadDrivingCertificates(imgPhoto);
    }


    /**
     * 识别失败
     */
    private void DialogWhether() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_identification_failure, null);
            TextView identification_ok = (TextView) view1.findViewById(R.id.identification_ok);
            identification_ok.setOnClickListener(this);
            builder.setView(view1);
            dialog = builder.create();
        }
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.returnImg) {
            working = false;
            finish();
        } else if (id == R.id.rl_shot_homepage) {
            //拍摄主页
            currentFilPath = mHomepagePath;
            jumpShotCamre(currentFilPath, REQUEST_CAMERA_H);
            side = 0;
        } else if (id == R.id.rl_shot_vicepage) {
            //拍摄副页
            currentFilPath = mVicePagePath;
            side = 1;
            jumpShotCamre(currentFilPath, REQUEST_CAMERA_V);
        } else if (id == R.id.identification_ok) {
            dialog.dismiss();
        }
    }

    @Override
    public void onCompleted() {
        stopMyDialog();
    }

    @Override
    public void onNext(final BaseEntity baseEntity) {
        if (baseEntity instanceof AuthenticationInfoEntity) {
            //设置用户信息
            AuthenticationInfoEntity entity= (AuthenticationInfoEntity) baseEntity;
            String owerName=entity.info.userName;
           if (!TextUtils.isEmpty(owerName)){
               String anthInfo="请拍摄"+owerName+"的行驶证，并录入信息";
               SpannableString spannableString=new SpannableString(anthInfo);
               ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#F39826"));
               spannableString.setSpan(colorSpan,3,owerName.length()+3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
               tvOwnerDriving.setText(spannableString);
           }
        }

        if (baseEntity instanceof UploadDrivingImgEntity) {

            if (side == 0) {
                mHomeEntity = (UploadDrivingImgEntity) baseEntity;
                GlideUtil.showImageRadius(mContext, mHomeEntity.data.picUrl, mIvHomePage, RxImageTool.dp2px(6));
                positive = mHomeEntity.data.picUrl;
                mLlHomepageNotice.setVisibility(View.GONE);
                tvCameraSuccess1.setVisibility(View.VISIBLE);
            } else {
                mViceEntity = (UploadDrivingImgEntity) baseEntity;
                back = mViceEntity.data.picUrl;
                GlideUtil.showImageRadius(mContext, mViceEntity.data.picUrl, mIvVicePage, RxImageTool.dp2px(6));
                mLlVicepageNotice.setVisibility(View.GONE);
                tvCameraSuccess2.setVisibility(View.VISIBLE);
            }
        }
        mLlVicepageNotice.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mHomeEntity != null && mViceEntity != null) {
                    SkipUtils.toCheckDrivingLicense(mContext, faceValue, backValue, positive, back);
                }
            }
        }, 500);


    }

    @Override
    public void onErrors(int flag, Throwable e) {
        stopMyDialog();
    }

    @Override
    protected void onDestroy() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }
}
