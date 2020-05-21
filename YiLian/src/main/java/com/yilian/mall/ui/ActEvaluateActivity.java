package com.yilian.mall.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
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
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActImageDeleteAdapter;
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.ActRuleEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *  猜价格-碰运气的评价
 * @author Ray_L_Pain
 * @date 2017/12/11 0011
 */

public class ActEvaluateActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.rb_grade)
    RatingBar ratingBar;
    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.rv_view)
    RecyclerView recyclerView;
    @ViewInject(R.id.chk)
    CheckBox checkBox;

    private String goodsId, goodsIcon;
    private ActImageDeleteAdapter adapter;
    BitmapUtils bu;
    private PopupWindow popupWindow;
    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> imgList1 = new ArrayList<>();

    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHOTO_ALBUM = 2;
    private static String ImageName;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_evaluate);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        goodsId = getIntent().getStringExtra("goods_index");
        goodsIcon = getIntent().getStringExtra("goods_icon");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("商品评价");
        tvRight.setText("提交");
        tvRight.setTextColor(getResources().getColor(R.color.color_red));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

        GlideUtil.showImage(mContext, goodsIcon, iv);

        bu = new BitmapUtils(mContext);
        imgList1.add("add");
        adapter = new ActImageDeleteAdapter(mContext, imgList1, imgList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add")) {
                    imgUpload(ActEvaluateActivity.this.getWindow().getDecorView());
                }
            }
        });
    }

    /**
     * 这里的获取数据为评论字数和图片的限制
     */
    int tvLimit, ivLimit;
    private void initData() {
        RetrofitUtils2.getInstance(mContext).galRule("1", new Callback<ActRuleEntity>() {
            @Override
            public void onResponse(Call<ActRuleEntity> call, Response<ActRuleEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        ActRuleEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                Logger.i("2017年12月14日 11:09:39-走了成功");
                                tvLimit = entity.info.word_limit;
                                ivLimit = entity.info.photo_limit;
                                Logger.i("2017年12月14日 11:09:39-" + tvLimit);
                                Logger.i("2017年12月14日 11:09:39-" + ivLimit);
                                break;
                            default:
                                Logger.i("2017年12月14日 11:09:39-走了其他");
                                tvLimit = 10;
                                ivLimit = 1;
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActRuleEntity> call, Throwable t) {
                Logger.i("2017年12月14日 11:09:39-走了失败");
                tvLimit = 10;
                ivLimit = 1;
            }
        });
    }

    /**
     * 发表评论
     */
    public void evaluate() {
        String evaluateStr = et.getText().toString();
        if (TextUtils.isEmpty(evaluateStr)) {
            showToast("讲两句吧!");
            return;
        }
        if (evaluateStr.length() < tvLimit) {
            showToast("评论内容不少于" + tvLimit + "个字哦~~");
            return;
        }
        if (imgList.size() < ivLimit) {
            showToast("图片数量不少于" + ivLimit + "张图片哦~~");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgList.size(); i++) {
            if (i == imgList.size() - 1) {
                sb.append(imgList.get(i));
            } else {
                sb.append(imgList.get(i) + ",");
            }
        }
        startMyDialog();
        RetrofitUtils2.getInstance(mContext).galSetComment(goodsId, "0", String.valueOf(ratingBar.getRating() * 10), evaluateStr, sb.toString(), new Callback<HttpResultBean>() {
            @Override
            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                stopMyDialog();
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                showToast(body.msg);
                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                finish();
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<HttpResultBean> call, Throwable t) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

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

        FileUtils.startActionCapture(this, file, ALBUM_CAMERA_REQUEST_CODE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
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
        startActivityForResult(intent, PHOTO_ALBUM);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
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

    Bitmap photo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == CAMERA_REQUEST_CODE) {//从拍照界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                File picture = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);
                sendImage(picture);
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                }
            }
        } else if (requestCode == PHOTO_ALBUM) {//从图库选择界面返回
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
            sendImage(new File(imagePath));
            c.close();
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
     * 图片上传到服务器
     */
    private void sendImage(File file) {
        startMyDialog();
        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", photoRequestBody);
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext)
                    .uploadFile(photo, new Callback<UploadImageEnity>() {
                        @Override
                        public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                if (com.yilian.mall.utils.CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                    UploadImageEnity body = response.body();
                                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                            switch (body.code) {
                                                case 1:
                                                    showToast("上传成功");
                                                    imgList.add(body.filename);
                                                    imgList1.clear();
                                                    imgList1.addAll(imgList);
                                                    if (imgList.size() < 6) {
                                                        imgList1.add("add");
                                                    }
                                                    recyclerView.setVisibility(View.VISIBLE);
                                                    adapter.notifyDataSetChanged();
                                                    stopMyDialog();
                                                    break;
                                                default:
                                                    break;
                                            }
                                        }
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

}
