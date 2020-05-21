package com.yilian.mall.jd.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.jd.adapter.JDAfterSaleApplyImgAdapter;
import com.yilian.mall.jd.adapter.JDAfterSaleApplyTypeAdapter;
import com.yilian.mall.jd.utils.JDDetailAddressUtil;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.jd.utils.JumpJdActivityUtils;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForDataEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleApplyForSubmitEntity;
import com.yilian.networkingmodule.entity.jd.JDAfterSaleUpdEntity;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;


/**
 * 京东售后申请
 *
 * @author Zg on 2017/5/28.
 */
public class JDAfterSaleApplyForActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHOTO_ALBUM = 2;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    private static String ImageName;
    ArrayList<String> imgList = new ArrayList<>();
    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;
    //服务类型
    private RecyclerView rvServiceType;
    private JDAfterSaleApplyTypeAdapter serviceTypeAdapter;
    //商品信息
    private ImageView ivGoodsImg;
    private TextView tvGoodsName, tvGoodsPrice, tvGoodsNum;
    //问题描述
    private int maxNum = 500;//限制的最大字数
    private EditText etProblemDescription;
    private TextView tvImportLimit;
    private ImageView ivImagePicker;
    private PopupWindow popupWindow;
    private RecyclerView rvImg;
    private JDAfterSaleApplyImgAdapter imgAdapter;
    //商品退回方式
    private RecyclerView rvReturnType;
    private JDAfterSaleApplyTypeAdapter returnTypeAdapter;
    //收货信息
    private EditText etLinkPerson, etLinkPhone;
    private LinearLayout llLinkAddress;
    private TextView tvLinkAddress;
    //提交
    private TextView tvSubmit;
    //提交信息
    private String serviceType, returnType, provinceId;
    /**
     * 京东订单号
     */
    private String jdOrderId,skuId,skuNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_after_sale_apply_for);
        initView();
        initData();
        initListener();
    }

    public void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new RefreshClickListener());
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        //服务类型
        rvServiceType = findViewById(R.id.rv_service_type);
        serviceTypeAdapter = new JDAfterSaleApplyTypeAdapter();
        rvServiceType.setLayoutManager(new GridLayoutManager(context, 3));
        rvServiceType.setAdapter(serviceTypeAdapter);
        //商品信息
        ivGoodsImg = findViewById(R.id.iv_goods_img);
        tvGoodsName = findViewById(R.id.tv_goods_name);
        tvGoodsPrice = findViewById(R.id.tv_goods_price);
        tvGoodsNum = findViewById(R.id.tv_goods_num);
        //问题描述
        etProblemDescription = findViewById(R.id.et_problem_description);
        tvImportLimit = findViewById(R.id.tv_import_limit);
        ivImagePicker = findViewById(R.id.iv_image_picker);
        rvImg = findViewById(R.id.rv_img);
        rvImg.setVisibility(View.GONE);
        rvImg.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        imgAdapter = new JDAfterSaleApplyImgAdapter();
        rvImg.setAdapter(imgAdapter);
        //商品退回方式
        rvReturnType = findViewById(R.id.rv_return_type);
        returnTypeAdapter = new JDAfterSaleApplyTypeAdapter();
        rvReturnType.setLayoutManager(new GridLayoutManager(context, 3));
        rvReturnType.setAdapter(returnTypeAdapter);
        //收货信息
        etLinkPerson = findViewById(R.id.et_link_person);
        etLinkPhone = findViewById(R.id.et_link_phone);
        llLinkAddress = findViewById(R.id.ll_link_address);
        tvLinkAddress = findViewById(R.id.tv_link_address);
        //提交
        tvSubmit = findViewById(R.id.tv_submit);
    }

    public void initData() {
        tvTitle.setText("申请售后服务");
        jdOrderId = getIntent().getStringExtra("jdOrderId");
        skuId = getIntent().getStringExtra("skuId");
        skuNum = getIntent().getStringExtra("skuNum");
        getJDAfterSaleApplyForData();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivImagePicker.setOnClickListener(this);
        llLinkAddress.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        etProblemDescription.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                temp = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvImportLimit.setText(s.length() + "/" + maxNum);
                selectionStart = etProblemDescription.getSelectionStart();
                selectionEnd = etProblemDescription.getSelectionEnd();
                if (temp.length() > maxNum) {
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionStart;
                    etProblemDescription.setText(s);
                    //设置光标在最后
                    etProblemDescription.setSelection(tempSelection);

                }
            }
        });

        serviceTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<JDAfterSaleApplyForDataEntity.TypeBean> serviceTypeList = adapter.getData();
                for (int i = 0; i < serviceTypeList.size(); i++) {
                    if (i == position) {
                        serviceType = serviceTypeList.get(i).getCode();
                        serviceTypeList.get(i).setSelected(true);
                    } else {
                        serviceTypeList.get(i).setSelected(false);
                    }
                }
                serviceTypeAdapter.setNewData(serviceTypeList);
            }
        });

        returnTypeAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                List<JDAfterSaleApplyForDataEntity.TypeBean> returnTypeList = adapter.getData();
                for (int i = 0; i < returnTypeList.size(); i++) {
                    if (i == position) {
                        returnType = returnTypeList.get(i).getCode();
                        returnTypeList.get(i).setSelected(true);
                    } else {
                        returnTypeList.get(i).setSelected(false);
                    }
                }
                returnTypeAdapter.setNewData(returnTypeList);
            }
        });

        imgAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_del:
                        //删除图片
                        imgList.remove(position);
                        imgAdapter.remove(position);
                        if (imgList.size() == 0) {
                            rvImg.setVisibility(View.GONE);
                        }
                        break;
                    default:
                        break;
                }
            }
        });

    }

    /**
     * 获取售后申请填写信息
     */
    private void getJDAfterSaleApplyForData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJDAfterSaleApplyForData("jd_aftersale/js_aftersale_info", jdOrderId,skuId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAfterSaleApplyForDataEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDAfterSaleApplyForDataEntity entity) {
                        JDAfterSaleApplyForDataEntity.DataBean mData = (JDAfterSaleApplyForDataEntity.DataBean) entity.data;
                        if (mData != null) {
                            setData(mData);

                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

    private void setData(JDAfterSaleApplyForDataEntity.DataBean mData) {
        //商品信息
        GlideUtil.showImageWithSuffix(context, JDImageUtil.getJDImageUrl_N3(mData.getGoodsInfo().getSkuPic()), ivGoodsImg);
        tvGoodsName.setText(mData.getGoodsInfo().getSkuName());
        tvGoodsPrice.setText(mData.getGoodsInfo().getSkuJdPrice());
        tvGoodsNum.setText("数量：×" + mData.getGoodsInfo().getSkuNum());
        //省ID  用于提交售后申请用
        provinceId = mData.getProvinceId();
        //服务类型
        List<JDAfterSaleApplyForDataEntity.TypeBean> serviceTypeList = mData.getServiceType();
        if (serviceTypeList != null && serviceTypeList.size() > 0) {
            for (int i = 0; i < serviceTypeList.size(); i++) {
                if (i == 0) {
                    serviceType = serviceTypeList.get(i).getCode();
                    serviceTypeList.get(i).setSelected(true);
                } else {
                    serviceTypeList.get(i).setSelected(false);
                }
            }
        }
        serviceTypeAdapter.setNewData(serviceTypeList);
        //商品退回方式
        List<JDAfterSaleApplyForDataEntity.TypeBean> returnTypeList = mData.getReturnType();
        if (returnTypeList != null && returnTypeList.size() > 0) {
            for (int i = 0; i < returnTypeList.size(); i++) {
                if (i == 0) {
                    returnType = returnTypeList.get(i).getCode();
                    returnTypeList.get(i).setSelected(true);
                } else {
                    returnTypeList.get(i).setSelected(false);
                }
            }
        }
        returnTypeAdapter.setNewData(returnTypeList);
        //收货信息
        etLinkPerson.setText(mData.getName());
        etLinkPhone.setText(mData.getMobile());
        tvLinkAddress.setText(mData.getAddress());

        varyViewUtils.showDataView();
    }

    /**
     * 按钮的点击事件
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //返回
                finish();
                break;
            case R.id.iv_image_picker:
                //图片选择
                if (imgList != null && imgList.size() >= 5) {
                    showToast("最多上传五张图片");
                } else {
                    imgUpload(getWindow().getDecorView());
                }
                break;
            case R.id.ll_link_address:
                //选择地址
                JumpJdActivityUtils.toJDShippingAddressListActivityForReuslt(this);
                break;
            case R.id.tv_submit:
                //提交
                setAftersaleAction();
                break;
            default:
                break;
        }
    }

    public void imgUpload(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);

    }

    /**
     * 京东申请售后
     */
    private void setAftersaleAction() {
        //问题描述
        String problemDescription;
        //图片地址
        String imgStr;
        //收货人
        String linkPerson;
        //手机号码
        String linkPhone;

        problemDescription = etProblemDescription.getText().toString();
        if (TextUtils.isEmpty(problemDescription)) {
            showToast("请描述问题");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgList.size(); i++) {
            if (i < imgList.size() - 1) {
                sb.append(imgList.get(i) + ",");
            } else {
                sb.append(imgList.get(i));
            }
        }
        imgStr = sb.toString();

        linkPerson = etLinkPerson.getText().toString();
        if (TextUtils.isEmpty(linkPerson)) {
            showToast("请输入联系人");
            return;
        }

        linkPhone = etLinkPhone.getText().toString();
        if (TextUtils.isEmpty(linkPhone)) {
            showToast("请输入手机号码");
            return;
        }
        if (TextUtils.isEmpty(serviceType)) {
            showToast("请选择服务类型");
            return;
        }
        if (TextUtils.isEmpty(returnType)) {
            showToast("请选择商品退回方式");
            return;
        }
//        if (TextUtils.isEmpty(provinceId)) {
//            showToast("省份ID为空");
//            return;
//        }

        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                setJdAftersaleAction("jd_aftersale/js_aftersale_action",
                        jdOrderId,skuId,skuNum ,serviceType, problemDescription, imgStr,
                        returnType, tvLinkAddress.getText().toString(),
                        linkPerson, linkPhone, provinceId).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAfterSaleApplyForSubmitEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDAfterSaleApplyForSubmitEntity entity) {
                        //京东更新售后订单
                        getJdAftersaleUpd(entity.id);
                    }
                });
        addSubscription(subscription);
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
     * 京东更新售后订单
     *
     * @param id 京东申请售后(setJdAftersaleAction（）) 成功返回值
     */
    private void getJdAftersaleUpd(String id) {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJdAftersaleUpd("jd_aftersale/js_aftersale_upd", id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JDAfterSaleUpdEntity>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        stopMyDialog();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(JDAfterSaleUpdEntity entity) {
                        // 申请售后结束跳转 售后详情
                        JumpJdActivityUtils.toJDAfterSaleDetails(context, entity.afsServiceId, skuId, skuNum);
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * PopupWindow
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST_CODE) { //拍照回来
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                File picture = new File(pathname);
                Luban.with(context).load(picture)
                        .setCompressListener(new OnCompressListener() {
                                                 @Override
                                                 public void onStart() {
                                                     startMyDialog(false);
                                                 }

                                                 @Override
                                                 public void onSuccess(File file) {
                                                     sendImage(file);
                                                 }

                                                 @Override
                                                 public void onError(Throwable e) {
                                                     showToast(e.getMessage());
                                                     stopMyDialog();
                                                 }
                                             }
                        ).launch();


                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                }
            }
        } else if (requestCode == PHOTO_ALBUM) { //相册选择回来
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
            int columnIndex = 0;
            try {
                columnIndex = c.getColumnIndex(filePathColumns[0]);
                String imagePath = c.getString(columnIndex);
                Luban.with(context).load(imagePath)
                        .setCompressListener(new OnCompressListener() {
                            @Override
                            public void onStart() {
                                startMyDialog(false);
                            }

                            @Override
                            public void onSuccess(File file) {
                                sendImage(file);
                            }

                            @Override
                            public void onError(Throwable e) {
                                showToast(e.getMessage());
                                stopMyDialog();
                            }
                        }).launch();
            } catch (Exception e) {
                e.printStackTrace();
                showToast("文件异常,请更换照片或拍照上传");
            } finally {
                c.close();
            }
        } else if (requestCode == JDEditShippingAddressListActivity.GET_JD_ADDRESS && resultCode == JDEditShippingAddressListActivity.JD_ADDRESS_RESULT) {
            //地址管理页面 回来   选择地址后返回
            JDShippingAddressInfoEntity.DataBean shippingAddressInfo = (JDShippingAddressInfoEntity.DataBean) data.getSerializableExtra(JDEditShippingAddressListActivity.TAG);
            tvLinkAddress.setText(JDDetailAddressUtil.getJDShippingDetailAddressStr(shippingAddressInfo.province,
                    shippingAddressInfo.city, shippingAddressInfo.county, shippingAddressInfo.town, shippingAddressInfo.detailAddress));
            provinceId = shippingAddressInfo.provinceId;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                        showToast("请开启读取SD卡权限");
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

        FileUtils.startActionCapture(this, file, ALBUM_CAMERA_REQUEST_CODE);
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
     * 相册选择
     */
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_ALBUM);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    private void sendImage(File file) {
        //在原来的基础上再对bitmap压缩一次
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", photoRequestBody);
        try {
            RetrofitUtils.getInstance(context).setContext(context)
                    .uploadFile(photo, new Callback<UploadImageEnity>() {
                        @Override
                        public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                            UploadImageEnity body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                                if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                                    switch (body.code) {
                                        case 1:
                                            showToast("上传成功");
                                            imgList.add(body.filename);
                                            imgAdapter.addData(body.filename);
                                            stopMyDialog();
                                            rvImg.setVisibility(View.VISIBLE);
                                            break;
                                    }
                                }
                            }
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

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yilian/userphoto/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "11.png");
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
     * 拍照上传
     *
     * @param view
     */
    public void camera(View view) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限，

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, APPLY_CAMERA_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
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
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }
        albumSelect();
    }

    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getJDAfterSaleApplyForData();
                }
            }, 1000);
        }
    }
}
