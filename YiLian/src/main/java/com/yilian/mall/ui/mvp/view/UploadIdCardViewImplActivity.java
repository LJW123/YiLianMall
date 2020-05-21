package com.yilian.mall.ui.mvp.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.MenuUtil;
import com.yilian.mylibrary.ALiCardBody;
import com.yilian.mylibrary.ALiIdCardDataValue;
import com.yilian.mylibrary.ALiIdCardFaceEntity;
import com.yilian.mylibrary.AliIdCardBackDataValue;
import com.yilian.mylibrary.AliIdCardBackEntity;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class UploadIdCardViewImplActivity extends BaseAppCompatActivity implements IUploadIdCardView, View.OnClickListener {

    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvName;
    private ImageView ivIdCardFront;
    private ImageView ivIdCardBack;
    private TextView tvLookPhotoAsk;
    private Button btnUpload;
    /**
     * 上个界面传递过来的处理过的用户名，用于界面展示
     */
    private String userName;
    private int requestCode;
    /**
     * 身份证正反面 “face" "back"
     */
    private String side;
    /**
     * 阿里api识别的的身份证有效期截止时间
     */
    private String scanEndDate;
    private String backUrl;
    private String
            faceUrl;
    /**
     * 传递过来的用户填写的身份证名称 用来和识别的身份证名称尽心对比，如果相同则身份证可用 并上传身份证
     */
    private String cardName;
    private TimePickerView timePickerView;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_id_card_view_impl);
        userName = getIntent().getStringExtra("userName");
        cardName = getIntent().getStringExtra("cardName");
        initView();
        initListener();
    }

    private void initListener() {
        RxUtil.clicks(ivIdCardFront, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                side = "face";
                camera(ALBUM_CAMERA_FRONT_REQUEST_CODE);
            }
        });
        RxUtil.clicks(ivIdCardBack, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                side = "back";
                camera(ALBUM_CAMERA_BACK_REQUEST_CODE);
            }
        });
        RxUtil.clicks(btnUpload, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (TextUtils.isEmpty(faceUrl) || TextUtils.isEmpty(backUrl)) {
                    showToast("证件信息不完整");
                    return;
                }
                showBirthdayPicker(scanEndDate);
            }
        });
        RxUtil.clicks(tvLookPhotoAsk, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(Constants.SPKEY_URL, Constants.PHOTO_DESCRIPTION);
                startActivity(intent);
            }
        });
        RxUtil.clicks(v3Shop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                MenuUtil.showMenu(UploadIdCardViewImplActivity.this, R.id.v3Shop);
            }
        });

    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("实名认证");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.merchant_v3_more_bottom);
        v3Shop.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        tvName = (TextView) findViewById(R.id.tv_name);
        Spanned spanned = Html.fromHtml("请上传<font color ='#FF1A90F5'>" + userName + "的身份证正反面照片");
        tvName.setText(spanned);
        ivIdCardFront = (ImageView) findViewById(R.id.iv_id_card_front);
        ivIdCardBack = (ImageView) findViewById(R.id.iv_id_card_back);
        tvLookPhotoAsk = (TextView) findViewById(R.id.tv_look_photo_ask);
        btnUpload = (Button) findViewById(R.id.btn_upload);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
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
     * 拍照上传
     *
     * @param requestCode
     */
    public void camera(int requestCode) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限，
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, APPLY_CAMERA_REQUEST_CODE);
            return;
        }

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
            return;
        }
//            有权限了
        openCamera(requestCode);
    }

    public static final int APPLY_CAMERA_REQUEST_CODE = 999;
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 101;//写SD卡的权限

    private static String ImageName;
    public static final int ALBUM_CAMERA_FRONT_REQUEST_CODE = 1;
    public static final int ALBUM_CAMERA_BACK_REQUEST_CODE = 2;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.requestCode = requestCode;
        // TODO Auto-generated method stub
        if (requestCode == ALBUM_CAMERA_FRONT_REQUEST_CODE || requestCode == ALBUM_CAMERA_BACK_REQUEST_CODE) {//从拍照界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                File picture = new File(pathname);
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
                                        checkIdCard(base64Strin, file, side);
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

    /**
     * 根据阿里api检测身份证照片信息
     *
     * @param bitMapBase64Code
     * @param picture
     */
    @SuppressWarnings("unchecked")
    private void checkIdCard(String bitMapBase64Code, File picture, String side) {
        startMyDialog(false);
        showToast("证件识别中");
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .checkIdCard(ALiCardBody.getInstance().getIdCardRequestHeader(), ALiCardBody.getInstance().getIdCardRequestBody(bitMapBase64Code, side))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            showToast("未识别,请重试");
                        } else {
                            showToast(e.getMessage());
                        }
                        stopMyDialog();
                        Logger.i("e.getMessage():" + e.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBody responseBody) {
                        String string = null;
                        try {
                            string = responseBody.string();
                            switch (requestCode) {
                                case ALBUM_CAMERA_FRONT_REQUEST_CODE:
                                    ALiIdCardFaceEntity aLiIdCardEntity = new Gson().fromJson(string, ALiIdCardFaceEntity.class);
                                    List<ALiIdCardFaceEntity.OutputsBean> outputs = aLiIdCardEntity.outputs;
                                    ALiIdCardFaceEntity.OutputsBean outputsBean = outputs.get(0);
                                    ALiIdCardFaceEntity.OutputsBean.OutputValueBean outputValue = outputsBean.outputValue;
                                    String dataValue = outputValue.dataValue;
                                    ALiIdCardDataValue aLiIdCardDataValue = new Gson().fromJson(dataValue, ALiIdCardDataValue.class);
//                            照片身份证号码
                                    String num = aLiIdCardDataValue.num;
                                    if (aLiIdCardDataValue.success) {
                                        if (aLiIdCardDataValue.name.equals(cardName)) {
                                            uploadIm(picture);
                                        } else {
                                            showToast("识别姓名不符,请重新拍照识别");
                                        }
                                        Logger.i("身份证照片上的号码：" + num);
                                        Logger.i("识别身份证" + string);
                                    } else {
                                        showToast("未识别,请重试");
                                    }

                                    break;
                                case ALBUM_CAMERA_BACK_REQUEST_CODE:
                                    AliIdCardBackEntity aliIdCardBackEntity = new Gson().fromJson(string, AliIdCardBackEntity.class);
                                    List<AliIdCardBackEntity.OutputsBean> outputs1 = aliIdCardBackEntity.outputs;
                                    AliIdCardBackEntity.OutputsBean outputsBean1 = outputs1.get(0);
                                    AliIdCardBackEntity.OutputsBean.OutputValueBean outputValue1 = outputsBean1.outputValue;
                                    AliIdCardBackDataValue dataValue1 = new Gson().fromJson(outputValue1.dataValue, AliIdCardBackDataValue.class);
//                                    20190902/长期
                                    scanEndDate = dataValue1.endDate;
                                    if (dataValue1.success) {
                                        uploadIm(picture);
                                    } else {
                                        showToast("未识别,请重试");
                                    }
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

    private void uploadIm(File picture) {
        startMyDialog(false);
        showToast("证件上传中");
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), picture);
        MultipartBody.Part imgPhoto = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", requestBody);
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .uploadImage("upload_auth_img", imgPhoto)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UploadImageEnity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                        showToast("上传成功");
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(UploadImageEnity o) {
                        String imageUrl = o.filename;
                        switch (requestCode) {
                            case ALBUM_CAMERA_FRONT_REQUEST_CODE:
                                GlideUtil.showImageNoSuffix(mContext, imageUrl, ivIdCardFront);
                                faceUrl = imageUrl;
                                break;
                            case ALBUM_CAMERA_BACK_REQUEST_CODE:
                                GlideUtil.showImageNoSuffix(mContext, imageUrl, ivIdCardBack);
                                backUrl = imageUrl;
                                break;

                        }
                    }
                });
        addSubscription(subscription);
    }

    private String getBitMapBase64Code(String imagePath) {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, baos);
        byte[] bytes = baos.toByteArray();
        byte[] encode = Base64.encode(bytes, Base64.DEFAULT);
        return new String(encode);
    }

    /**
     * 开启照相机
     */
    private void openCamera(int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);
        FileUtils.startActionCapture(this, file, requestCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_upload:

                break;
        }
    }

    private void showBirthdayPicker(String scanEndDate) {
//        开始时间限制1984年1月1日
        Calendar calendarStart = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarStart.set(Calendar.MONTH, 1 - 1);
        calendarStart.set(Calendar.YEAR, 1984);
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
//        默认时间
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar defaultSelectDay = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
//       截止时间限制 当天+50年
        calendarStart.set(Calendar.DAY_OF_MONTH, 1);
        Calendar calendarEnd = Calendar.getInstance(java.util.TimeZone.getTimeZone("GMT+08:00"));
        calendarEnd.add(Calendar.YEAR, 50);
        calendarEnd.set(Calendar.MINUTE, 0);
        //设置默认当前的时间
        String title = "请选择您的证件有效期";

        //选中时间回调
//设置起止时间
        timePickerView = new TimePickerView.Builder(mContext, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中时间回调
                String checkEndDate;
                if (checkBox != null && checkBox.isChecked()) {
                    checkEndDate = "长期";
                } else {
                    Timestamp timestamp = new Timestamp(date.getTime());
                    checkEndDate = DateUtils.timeStampToStr5(timestamp.getTime() / 1000);
                }
                if (checkEndDate.equals(scanEndDate)) {
                    addIdCardInfo();
                } else {
                    showToast("选择时间与识别时间不一致,请核对信息后重新上传");
                }
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)
                .setDividerColor(Color.DKGRAY)
                .setOutSideCancelable(false
                )
                .setContentSize(20)
                .setDate(defaultSelectDay)
                .setRangDate(calendarStart, calendarEnd)//设置起止时间
                .setLayoutRes(R.layout.library_module_pickerview_custom_time, new CustomListener() {
                    @Override
                    public void customLayout(View v) {
                        TextView tvTitle = v.findViewById(R.id.tvTitle);
                        tvTitle.setText(title);
                        Button btnCancel = v.findViewById(R.id.btnCancel);
                        Button btnSubmit = v.findViewById(R.id.btnSubmit);
                        checkBox = v.findViewById(R.id.check_box);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timePickerView.dismiss();
                            }
                        });
                        btnSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                timePickerView.returnData();
                                timePickerView.dismiss();
                            }
                        });

                    }
                })
                .build();
        timePickerView.show();
    }

    @SuppressWarnings("unchecked")
    private void addIdCardInfo() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .addIdCardInfo("userAuth/add_user_info", faceUrl, backUrl, scanEndDate.equals("长期") ? "-1" : scanEndDate)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        startActivity(new Intent(mContext, CertificationResultImplViewActivity.class));
                    }
                });
        addSubscription(subscription);
    }
}
