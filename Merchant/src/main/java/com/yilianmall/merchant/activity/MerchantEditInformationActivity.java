package com.yilianmall.merchant.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.adapter.BaseAdapter;
import com.yilian.mylibrary.adapter.ImageDeleteAdapter;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.IndustryEntity;
import com.yilian.networkingmodule.entity.MerchantData;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.ImageCompressUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MerchantEditInformationActivity extends BaseActivity implements View.OnClickListener, ImageDeleteAdapter.RefreshImageList {

    public static final int TAG_CLOSE = 0;
    public static final int TAG_OPEN = 1;
    public static final int APPLY_CAMERA_REQUEST_CODE = 999;
    public static final int MERCHANT_DISCOUNT_CODE = 99;
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 101;//写SD卡的权限
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private FrameLayout v3Layout;
    private ImageView ivMerchantHeadImg;
    private TextView tvChangeHeadImage;
    private EditText etMerchantName;
    private TextView tvMerchantDiscount;
    private TextView tvMerchantChangeCoupon;
    private RecyclerView rvView;
    private EditText etMerchantRecommend;
    private Button btnNextStep;
    private TextView tvOpenTime;
    private TextView tvCloseTime;
    private EditText etContact;
    private EditText etPhone;
    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> imgList1 = new ArrayList<>();
    private ImageDeleteAdapter adapter;
    private TextView tvIndustry;
    private String merchantHeadImage;
    private EditText etKeyword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_edit_information);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getMerchantData();
        getIndustyrList();
    }

    private ArrayList<IndustryEntity.IndustryBean> industry1 = new ArrayList<>();
    private ArrayList<ArrayList<IndustryEntity.IndustryBean.SonIndustryBean>> industry2 = new ArrayList<>();

    private void getIndustyrList() {
        RetrofitUtils.getInstance(mContext).getIndustryList(new Callback<IndustryEntity>() {
            @Override
            public void onResponse(Call<IndustryEntity> call, Response<IndustryEntity> response) {
                IndustryEntity body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                for (int i = 0; i < body.list.size(); i++) {
                                    IndustryEntity.IndustryBean industryBean = body.list.get(i);
                                    industry1.add(industryBean);
                                    industry2.add(industryBean.sonIndustry);
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<IndustryEntity> call, Throwable t) {

            }
        });
    }

    private void getMerchantData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getMerchantData(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), new Callback<MerchantData>() {
                    @Override
                    public void onResponse(Call<MerchantData> call, Response<MerchantData> response) {
                        stopMyDialog();
                        MerchantData body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        if (!TextUtils.isEmpty(body.data.merchantName)) {
                                            tvRight.setVisibility(View.VISIBLE);
                                            v3Shop.setVisibility(View.GONE);
                                        }
                                        setData(body);
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MerchantData> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.network_module_net_work_error);
                    }
                });
    }

    private void setData(MerchantData body) {
        MerchantData.DataBean data = body.data;
        if (TextUtils.isEmpty(data.merchantName)) {
            etMerchantName.setHint("请填写门店名称");
        } else {
            etMerchantName.setText(data.merchantName);
            etMerchantName.setSelection(data.merchantName.length());
        }

        if (TextUtils.isEmpty(data.merchantImage)) {
            tvChangeHeadImage.setText("上传");
        } else {
            merchantHeadImage = data.merchantImage;
            GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(data.merchantImage), ivMerchantHeadImg);
            tvChangeHeadImage.setText("更换");
        }
        if (TextUtils.isEmpty(data.merchantPercent)) {
            tvMerchantDiscount.setHint("请设置让利折扣");
        } else {
            if (data.merchantPercent.equals("10")) {
                tvMerchantDiscount.setHint("请设置让利折扣");

            } else {
                tvMerchantDiscount.setText("当前让利折扣:" + data.merchantPercent + "折");
            }
        }
        if (data.merchantAlbum != null && data.merchantAlbum.size() > 0) {
//            imgList=data.merchantAlbum;
            imgList.addAll(data.merchantAlbum);
            refreshAlbums(data.merchantAlbum);
        }
        if (TextUtils.isEmpty(data.merchantContacts)) {
            etContact.setHint("请输入联系人名称");
        } else {
            etContact.setText(data.merchantContacts);
        }
        if (TextUtils.isEmpty(data.merchantTel)) {
            etPhone.setHint("请输入联系电话");
        } else {
            etPhone.setText(data.merchantTel);
        }
        if (TextUtils.isEmpty(data.openTime)) {
            tvOpenTime.setHint("请选择开门时间");
        } else {
            tvOpenTime.setText(data.openTime);
        }
        if (TextUtils.isEmpty(data.closeTime)) {
            tvCloseTime.setHint("请选择打烊时间");
        } else {
            tvCloseTime.setText(data.closeTime);
        }
        if (TextUtils.isEmpty(data.merchantDesp)) {
            etMerchantRecommend.setHint("请输入门店简介");
        } else {
            etMerchantRecommend.setText(data.merchantDesp);
        }
        if (TextUtils.isEmpty(data.industryParentName) || TextUtils.isEmpty(data.merchantIndustryName)) {
            tvIndustry.setHint("请选择行业");
        } else {
            tvIndustry.setText(data.industryParentName + data.merchantIndustryName);
            industrySonId = data.merchantIndustry;
            industrySupId = data.merchantIndustryParent;
        }
        if (!TextUtils.isEmpty(data.keyWords)) {
            etKeyword.setText(data.keyWords);
        } else {
            etKeyword.setHint("请以空格间隔，例“美食 娱乐”");
        }
    }

    String albums = "";

    public void refreshAlbums(List<String> merchantAlbum) {
        getAlbums(merchantAlbum);
        imgList1.clear();
        imgList1.addAll(merchantAlbum);
        if (imgList.size() < 6) {
            imgList1.add("add");
        }
        rvView.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    private void getAlbums(List<String> merchantAlbum) {
        String newestAlbums = "";
        for (int i = 0; i < merchantAlbum.size(); i++) {
            if (i == merchantAlbum.size() - 1) {
                newestAlbums += (merchantAlbum.get(i));
            } else {
                newestAlbums += (merchantAlbum.get(i) + ",");
            }
        }
        albums = newestAlbums;
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("门店基本资料");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setText("跳过");
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);

        ivMerchantHeadImg = (ImageView) findViewById(R.id.iv_merchant_head_img);
        tvChangeHeadImage = (TextView) findViewById(R.id.tv_change_head_image);
        etMerchantName = (EditText) findViewById(R.id.et_merchant_name);
        tvMerchantDiscount = (TextView) findViewById(R.id.tv_merchant_discount);
        tvMerchantChangeCoupon = (TextView) findViewById(R.id.tv_merchant_change_discount);
        rvView = (RecyclerView) findViewById(R.id.rv_view);
        rvView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        imgList1.add("add");
        adapter = new ImageDeleteAdapter(mContext, imgList1, imgList, this);
        rvView.setAdapter(adapter);

        View includeContact = findViewById(R.id.include_contact);
        ((TextView) includeContact.findViewById(R.id.tv_key)).setText("联系人:");
        etContact = (EditText) includeContact.findViewById(R.id.et_value);
        etContact.setVisibility(View.VISIBLE);
        etContact.setMaxLines(1);

        View includePhone = findViewById(R.id.include_phone);
        ((TextView) includePhone.findViewById(R.id.tv_key)).setText("联系电话:");
        etPhone = (EditText) includePhone.findViewById(R.id.et_value);
        etPhone.setVisibility(View.VISIBLE);
        etPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        InputFilter[] filters = {new InputFilter.LengthFilter(11)};
        etPhone.setFilters(filters);

        View includeOpenTime = findViewById(R.id.include_open_time);
        ((TextView) includeOpenTime.findViewById(R.id.tv_key)).setText("开门时间:");
        tvOpenTime = (TextView) includeOpenTime.findViewById(R.id.tv_value);
        tvOpenTime.setVisibility(View.VISIBLE);
        tvOpenTime.setHint("请选择开门时间");
        tvOpenTime.setTag(TAG_OPEN);

        View includeCloseTime = findViewById(R.id.include_close_time);
        ((TextView) includeCloseTime.findViewById(R.id.tv_key)).setText("打烊时间:");
        tvCloseTime = (TextView) includeCloseTime.findViewById(R.id.tv_value);
        tvCloseTime.setVisibility(View.VISIBLE);
        tvCloseTime.setHint("请选择打烊时间");
        tvCloseTime.setTag(TAG_CLOSE);

        View includeIndustry = findViewById(R.id.include_industry);
        ((TextView) includeIndustry.findViewById(R.id.tv_key)).setText("所属行业:");
        tvIndustry = (TextView) includeIndustry.findViewById(R.id.tv_value);
        tvIndustry.setVisibility(View.VISIBLE);

        View includeKeyword = findViewById(R.id.include_keyword);
        ((TextView) includeKeyword.findViewById(R.id.tv_key)).setText("关键词");
        etKeyword = (EditText) includeKeyword.findViewById(R.id.et_value);
        etKeyword.setVisibility(View.VISIBLE);

        etMerchantRecommend = (EditText) findViewById(R.id.et_merchant_recommend);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);

        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvChangeHeadImage.setOnClickListener(this);
        tvMerchantChangeCoupon.setOnClickListener(this);
        btnNextStep.setOnClickListener(this);
        etKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initListener() {
        tvIndustry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (industry1.size() <= 0 || industry2.size() <= 0) {
                    showToast(R.string.network_module_net_work_error);
                    return;
                }
                showIndustryPickerView();
            }
        });
        tvOpenTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v, "开门时间", 8);
            }
        });
        tvCloseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker(v, "打烊时间", 22);
            }
        });
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                REQUEST_FROM = FROME_ALBUM_CODE;
                if (imgList1.get(position).equals("add"))
                    imgUpload(MerchantEditInformationActivity.this.getWindow().getDecorView());
            }
        });
    }

    private String industrySupId;
    private String industrySonId;

    /**
     * 弹出行业选择器
     */
    private void showIndustryPickerView() {
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                IndustryEntity.IndustryBean industryBean = industry1.get(options1);
                industrySupId = industryBean.industryId;
                IndustryEntity.IndustryBean.SonIndustryBean sonIndustryBean = industry2.get(options1).get(option2);
                industrySonId = sonIndustryBean.industryId;
                tvIndustry.setText(industryBean.getPickerViewText() + sonIndustryBean.getPickerViewText());
            }
        })
                .setTitleText("行业选择")
                .setContentTextSize(20).build();
        pvOptions.setPicker(industry1, industry2);
        pvOptions.show();
    }

    /**
     * 打开选择图片PopupWindow
     *
     * @param view
     */
    public void imgUpload(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
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

    private PopupWindow popupWindow;

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
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
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

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_right) {
            startActivity(new Intent(mContext, MerchantAddressActivity.class));
        } else if (i == R.id.tv_change_head_image) {
            REQUEST_FROM = FROME_HEAD_CODE;
            imgUpload(MerchantEditInformationActivity.this.getWindow().getDecorView());
        } else if (i == R.id.tv_merchant_change_discount) {
            Intent intent = new Intent(mContext, MerchantChangeDiscountActivity.class);
            startActivityForResult(intent, MERCHANT_DISCOUNT_CODE);
        } else if (i == R.id.btn_next_step) {
            submit();
        }
    }

    private void submit() {
        // validate
        if (TextUtils.isEmpty(merchantHeadImage)) {
            showToast("请上传商铺门头照片");
            return;
        }
        String name = etMerchantName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "门店名称不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (imgList1.size() < 2) {
            showToast("至少上传一张环境照片");
            return;
        } else {

        }
        if (etContact.length() <= 0) {
            showToast("请输入联系人");
            return;
        }
        String phone = etPhone.getText().toString().trim();
        if (!CommonUtils.isPhoneNumer(phone)) {
            showToast("请输入联系电话");
            return;
        }
        String openTime = tvOpenTime.getText().toString().trim();
        if (TextUtils.isEmpty(openTime)) {
            showToast("请选择开门时间");
            return;
        }
        String closeTime = tvCloseTime.getText().toString().trim();
        if (TextUtils.isEmpty(closeTime)) {
            showToast("请选择打烊时间");
            return;
        }
        String recommend = etMerchantRecommend.getText().toString().trim();
        if (TextUtils.isEmpty(recommend)) {
            Toast.makeText(this, "请输入门店简介信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(industrySonId) || TextUtils.isEmpty(industrySupId)) {
            showToast("请选择行业");
            return;
        }
        //关键词非必填不需要判断是否为空
        String keyWords = etKeyword.getText().toString().trim();

        // TODO validate success, do something
        setMerchantData(name, phone, openTime, closeTime, recommend, keyWords);
    }

    private void setMerchantData(String name, String phone, String openTime, String closeTime, String recommend, String keyWords) {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .setMerchantData(PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext), merchantHeadImage, name, albums, etContact.getText().toString().trim(),
                        phone, openTime, closeTime, recommend, industrySupId, industrySonId, keyWords, new Callback<BaseEntity>() {
                            @Override
                            public void onResponse(Call<BaseEntity> call, Response<BaseEntity> response) {
                                stopMyDialog();
                                BaseEntity body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        switch (body.code) {
                                            case 1:
                                                showToast("提交成功");
                                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_CENTER, true, mContext);
//                                                finish();
                                                startActivity(new Intent(mContext, MerchantAddressActivity.class));
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


    private void showTimePicker(final View view, String time, int defaultHour) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        calendar.set(Calendar.HOUR_OF_DAY, defaultHour);
        calendar.set(Calendar.MINUTE, 0);
        new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                getTime(date, view);
            }
        })
                .setTitleText(time)
                .setContentSize(20)
                .setType(TimePickerView.Type.HOURS_MINS)
                .setDividerColor(Color.DKGRAY)
                .setDate(calendar)
                .build()
                .show();
    }

    private String getTime(Date date, View v) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String time = format.format(date);
        switch ((int) v.getTag()) {
            case TAG_OPEN:
                tvOpenTime.setText(time);
                break;
            case TAG_CLOSE:
                tvCloseTime.setText(time);
                break;
        }
        return time;
    }

    private static String ImageName;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        showToast("拍照权限被拒绝");
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
            case APPLY_WRITE_EXTERNAL_STORAGE_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
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
     * @param view
     */
    public void camera(View view) {
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
        openCamera();
    }

    /**
     * 开启照相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);

        FileUtils.startActionCapture(this, file, ALBUM_CAMERA_REQUEST_CODE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, ALBUM_CAMERA_REQUEST_CODE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }


    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;
    public static final int ALBUM_PHOTO_ALBUM = 2;
    public static final int FROME_HEAD_CODE = 100;
    public static final int FROME_ALBUM_CODE = 101;
    public int REQUEST_FROM;

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            //暂无权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }
        albumSelect();
    }

    /**
     * 相册选择
     */
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, ALBUM_PHOTO_ALBUM);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == ALBUM_CAMERA_REQUEST_CODE) {//从拍照界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                File picture = new File(pathname);
                if (null != picture || !picture.equals("")) {
                    picture = ImageCompressUtil.compressImage(picture, ScreenUtils.getScreenWidth(mContext), ScreenUtils.getScreenHeight(mContext), 50, mContext);
                    picture = ImageCompressUtil.compressImage(picture, ScreenUtils.getScreenWidth(mContext) / 2, ScreenUtils.getScreenHeight(mContext) / 2, 50, mContext);
                    picture = BitmapUtil.restoreFile(pathname, picture, "environment");//处理图片旋转问题
                    uploadImage(picture);
                }
            }
        } else if (requestCode == ALBUM_PHOTO_ALBUM) {//从图库选择界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                return;
            }
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            File file = new File(imagePath);
            if (null != file || !file.equals("")) {
                file = ImageCompressUtil.compressImage(file, ScreenUtils.getScreenWidth(mContext) / 2, ScreenUtils.getScreenHeight(mContext) / 2, 50, mContext);
                file = BitmapUtil.restoreFile(imagePath, file, "environment");//处理图片旋转问题
                uploadImage(file);
            }
            c.close();
        } else if (requestCode == MERCHANT_DISCOUNT_CODE) {
            if (null != data) {
                String merchantDiscount = data.getStringExtra("merchantDiscount");
                if (!TextUtils.isEmpty(merchantDiscount)) {
                    tvMerchantDiscount.setText("当前让利折扣:" + merchantDiscount + "折");
                }
            }
        }
    }


    /**
     * 图片上传到服务器
     */
    private void uploadImage(File file) {
        startMyDialog();
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", photoRequestBody);
        try {
            RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                    .uploadFile(photo, new Callback<UploadImageEnity>() {
                        @Override
                        public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                            UploadImageEnity body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    switch (body.code) {
                                        case 1:
                                            showToast("上传成功");
                                            switch (REQUEST_FROM) {
                                                case FROME_ALBUM_CODE:

                                                    imgList.add(body.filename);
                                                    refreshAlbums(imgList);
                                                    break;
                                                case FROME_HEAD_CODE:
                                                    merchantHeadImage = body.filename;
                                                    GlideUtil.showImage(mContext, WebImageUtil.getInstance().getWebImageUrlWithSuffix(body.filename), ivMerchantHeadImg);
                                                    break;
                                            }


                                            break;
                                    }
                                }
                            }
                            stopMyDialog();
                        }

                        @Override
                        public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                            stopMyDialog();
                            showToast("上传失败,重新上传");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void refreshImageList(ArrayList<String> list) {
        refreshAlbums(list);
    }
}
