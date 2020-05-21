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

import com.bigkoo.pickerview.OptionsPickerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.enums.GoodsFeedbackType;
import com.yilian.mall.jd.adapter.JDPickImageAdapter;
import com.yilian.mall.jd.utils.JDImageUtil;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.entity.ImgAlterEntity;
import com.yilian.mylibrary.utils.KeyBordUtils;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.networkingmodule.entity.jd.FeedbackTypeEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
 * 京东商品 问题反馈
 * by Zg  2018.06.20
 */
public class JDGoodsFeedbackActivity extends BaseFragmentActivity implements View.OnClickListener {
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHOTO_ALBUM = 2;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;

    private static String ImageName;
    /**
     * 上传图片数量限制
     */
    private int imgNum = 3;
    private JDPickImageAdapter adapter;
    private PopupWindow popupWindow;

    private VaryViewUtils varyViewUtils;
    //标题栏
    private ImageView ivBack;
    private TextView tvTitle;
    //反馈类型
    private LinearLayout llPickFeedbackType;
    private TextView tvFeedbackType;
    //反馈内容
    private int maxNum = 500;//限制的最大字数
    private EditText etProblemDescription;
    private TextView tvImportLimit;
    //反馈图片
    private RecyclerView rvImg;
    //反馈人手机号
    private EditText etPhone;
    //提交
    private TextView tvSubmit;
    //反馈类型列表
    private List<FeedbackTypeEntity.DataBean> feedbackList;

    //反馈商品所属类型
    private GoodsFeedbackType goodsFeedbackType;
    //商品信息参数
    private String goodsSku, goodsName, goodsImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jd_activity_goods_feedback);
        EventBus.getDefault().register(this);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initView() {
        //初始化——数据展示布局
        varyViewUtils = new VaryViewUtils(context);
        varyViewUtils.setVaryViewHelper(R.id.vary_content, getWindow().getDecorView(), new RefreshClickListener());
        //标题栏
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        //内容
        llPickFeedbackType = findViewById(R.id.ll_pick_feedback_type);
        tvFeedbackType = findViewById(R.id.tv_feedback_type);
        etProblemDescription = findViewById(R.id.et_problem_description);
        tvImportLimit = findViewById(R.id.tv_import_limit);
        tvImportLimit.setText("0/" + maxNum);

        rvImg = findViewById(R.id.rv_img);

        adapter = new JDPickImageAdapter(imgNum);
        rvImg.setAdapter(adapter);
        rvImg.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));


        etPhone = findViewById(R.id.et_phone);
        tvSubmit = findViewById(R.id.tv_submit);
    }

    private void initData() {
        tvTitle.setText("问题反馈");
        goodsSku = getIntent().getStringExtra("goods_sku");
        goodsName = getIntent().getStringExtra("goods_name");
        goodsImg = getIntent().getStringExtra("goods_img");
        goodsFeedbackType = (GoodsFeedbackType) getIntent().getSerializableExtra("goodsFeedbackType");
        //获取反馈类型
        switch (goodsFeedbackType) {
            case GOODS_FEEDBACK_JD:
                //京东商品
                getJDFeedbackType();
                break;
            case GOODS_FEEDBACK_SN:
                //苏宁商品
                getSnFeedbackType();
                break;
            default:
                break;
        }
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        llPickFeedbackType.setOnClickListener(this);
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

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_del:
                        if (adapter.getData() != null) {
                            //删除图片
                            adapter.remove(position);
                        }
                        break;
                    case R.id.iv:
                        //点击图片
                        String str = (String) adapter.getItem(position);
                        if (str.equals(JDPickImageAdapter.ADD)) {
                            KeyBordUtils.closeKeyBord(context);
                            imgUpload(getWindow().getDecorView());
                        } else {
                            List<String> imgList = new ArrayList<>();
                            for (Object o : adapter.getData()) {
                                String img = String.valueOf(o);
                                if (!img.equals(JDPickImageAdapter.ADD)) {
                                    imgList.add(img);
                                }
                            }
                            JumpToOtherPageUtil.getInstance().jumpToImageBrowse(context, imgList, position, true);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取京东政企意见反馈类型
     */
    private void getJDFeedbackType() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getJDFeedbackType("jd_goods/jd_face_back_type").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedbackTypeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(FeedbackTypeEntity entity) {
                        feedbackList = entity.data;
                        if (feedbackList != null && feedbackList.size() > 0) {
                            varyViewUtils.showDataView();
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取苏宁政企意见反馈类型
     */
    private void getSnFeedbackType() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                getSnFeedbackType("suning_goods/suning_face_back_type").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<FeedbackTypeEntity>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        varyViewUtils.showErrorView();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(FeedbackTypeEntity entity) {
                        feedbackList = entity.data;
                        if (feedbackList != null && feedbackList.size() > 0) {
                            varyViewUtils.showDataView();
                        } else {
                            varyViewUtils.showErrorView();
                        }
                    }
                });
        addSubscription(subscription);
    }

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
                                            adapter.addItem(adapter.getData().size() - 1, body.filename);
                                            stopMyDialog();
                                            break;
                                        default:
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_pick_feedback_type:
                KeyBordUtils.closeKeyBord(context);
                showFeedbackTypePickerView();
                break;
            case R.id.tv_submit:
                KeyBordUtils.closeKeyBord(context);
                setFeedback();
                break;
            default:
                break;
        }
    }

    /**
     * 弹出类型选择器
     */
    private void showFeedbackTypePickerView() {
        final OptionsPickerView expressOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                FeedbackTypeEntity.DataBean feedback = feedbackList.get(options1);
                tvFeedbackType.setText(feedback.getName());
            }
        }).setContentTextSize(20)
                .build();
        expressOptions.setPicker(feedbackList);
        expressOptions.show();
    }

    /**
     * 意见反馈 表单验证
     */
    private void setFeedback() {
        String feedbackType = tvFeedbackType.getText().toString();
        if (TextUtils.isEmpty(feedbackType)) {
            showToast("请选择反馈类型");
            return;
        }
        String phone = etPhone.getText().toString();
        if (TextUtils.isEmpty(phone) || !RegExUtils.isPhone(phone)) {
            showToast("请输入正确手机号");
            return;
        }
        List<String> list = adapter.getImgList();
        String img = "";
        for (String item : list) {
            if (img.equals("")) {
                img = img + WebImageUtil.getInstance().getWebImageUrlWithSuffix(item);
            } else {
                img = img + "|" + WebImageUtil.getInstance().getWebImageUrlWithSuffix(item);
            }
        }
        String problemDescription = etProblemDescription.getText().toString();
        if (TextUtils.isEmpty(problemDescription)) {
            problemDescription = "";
        }
        if (img.equals("") && problemDescription.equals("")) {
            showToast("请输入反馈内容或上传相关图片");
            return;
        }

        startMyDialog();

        switch (goodsFeedbackType) {
            case GOODS_FEEDBACK_JD:
                //京东商品
                setJDFeedback(feedbackType, problemDescription, img, phone);
                break;
            case GOODS_FEEDBACK_SN:
                //苏宁商品
                setSnFeedback(feedbackType, problemDescription, img, phone);
                break;
            default:
                break;
        }

    }

    /**
     * 京东政企意见反馈
     *
     * @param feedbackType       反馈类型
     * @param problemDescription 问题描述
     * @param img                上传图片
     * @param phone              手机号
     */
    private void setJDFeedback(String feedbackType, String problemDescription, String img, String phone) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                setJDFeedback("jd_goods/jd_face_back", feedbackType, problemDescription, img, phone, goodsSku, goodsName, JDImageUtil.getJDImageUrl_N0(goodsImg)).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
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
                    public void onNext(HttpResultBean entity) {
                        showToast(entity.msg);
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 苏宁政企意见反馈
     *
     * @param feedbackType       反馈类型
     * @param problemDescription 问题描述
     * @param img                上传图片
     * @param phone              手机号
     */
    private void setSnFeedback(String feedbackType, String problemDescription, String img, String phone) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(context).
                setSnFeedback("suning_goods/suning_face_back", feedbackType, problemDescription, img, phone, goodsSku, goodsName, goodsImg).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
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
                    public void onNext(HttpResultBean entity) {
                        showToast(entity.msg);
                        finish();
                    }
                });
        addSubscription(subscription);
    }


    @Subscribe()
    public void receiveEvent(ImgAlterEntity imgAlterEntity) throws PackageManager.NameNotFoundException {
        if (adapter.getData() != null) {
            for (int i = 0; i < adapter.getData().size(); i++) {
                String imgUrl = adapter.getItem(i);
                if (imgUrl.equals(imgAlterEntity.imgUrl)) {
                    //删除图片
                    adapter.remove(i);
                }
            }


        }


    }

    public class RefreshClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            varyViewUtils.showLoadingView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getJDFeedbackType();
                }
            }, 1000);
        }
    }
}
