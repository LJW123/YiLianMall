package com.yilian.mall.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ActImageDeleteAdapter;
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.PreferenceUtils;

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
 *  V5版本的套餐评价页
 * @author Ray_L_Pain
 * @date 2017/12/25 0025
 */

public class V5EvaActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;

    @ViewInject(R.id.rg_total_comment)
    RadioGroup radioGroup;
    @ViewInject(R.id.rb_total_comment_score1)
    RadioButton rbScore1;
    @ViewInject(R.id.rb_total_comment_score2)
    RadioButton rbScore2;
    @ViewInject(R.id.rb_total_comment_score3)
    RadioButton rbScore3;
    @ViewInject(R.id.rb_total_comment_score4)
    RadioButton rbScore4;

    @ViewInject(R.id.et)
    EditText et;
    @ViewInject(R.id.recycler_view)
    RecyclerView recyclerView;

    private ActImageDeleteAdapter adapter;
    private ArrayList<String> imgList1 = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();
    private PopupWindow popupWindow;
    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHOTO_ALBUM = 2;
    private static String ImageName;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    MTNetRequest mtNetRequest;

    private String title;
    private int checkedId;
    private String orderId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v5_eva);
        ViewUtils.inject(this);

        initView();
        initListener();
    }

    private void initView() {
        title = getIntent().getStringExtra("title");
        checkedId = getIntent().getIntExtra("checked_id", 0);
        orderId = getIntent().getStringExtra("order_id");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(title);
        tvRight.setText("发布");
        tvRight.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evaluate();
            }
        });

        int position = 4;
        switch (checkedId) {
            case 1:
                position = R.id.rb_total_comment_score1;
                radioGroup.check(R.id.rb_total_comment_score1);
                break;
            case 2:
                position = R.id.rb_total_comment_score2;
                radioGroup.check(R.id.rb_total_comment_score2);
                break;
            case 3:
                position = R.id.rb_total_comment_score3;
                radioGroup.check(R.id.rb_total_comment_score3);
                break;
            case 4:
                position = R.id.rb_total_comment_score4;
                radioGroup.check(R.id.rb_total_comment_score4);
                break;
            default:
                break;
        }
        rbScore1.setText("不满");
        rbScore2.setText("一般");
        rbScore3.setText("满意");
        rbScore4.setText("超赞");
        initRadioButton(position);

        imgList1.add("add");
        adapter = new ActImageDeleteAdapter(mContext, imgList1, imgList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add")) {
                    imgUpload(V5EvaActivity.this.getWindow().getDecorView());
                }
            }
        });
    }

    /**
     * tvLimit为字数最少限制
     * ivLimit为图片最少限制
     * 暂时写死
     */
    int tvLimit = 13;
    int ivLimit = 3;
    private void evaluate() {
        String evaluateStr = et.getText().toString().trim();
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
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.commitPackageComment(orderId, "1", "2", "1", evaluateStr, sb.toString(), new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                BaseEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        //刷新个人页面
                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);

                        showToast("评论成功");
                        startActivity(new Intent(V5EvaActivity.this, MTCommentSuccessActivity.class));
                        AppManager.getInstance().killActivity(V5EvaActivity.class);
                        AppManager.getInstance().killActivity(V5PaySuccessActivity.class);
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -4:
                        showToast(R.string.login_failure);
                        sp.edit().putBoolean(Constants.SPKEY_STATE, false).commit();//将登陆状态设置为false；
                        startActivity(new Intent(V5EvaActivity.this, LeFenPhoneLoginActivity.class));
                        break;

                    default:
                        showToast("" + result.code);
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
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

        FileUtils.startActionCapture(this, file, ALBUM_CAMERA_REQUEST_CODE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 拍照
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


    private void initListener() {
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add")) {
                    imgUpload(V5EvaActivity.this.getWindow().getDecorView());
                }
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                initRadioButton(checkedId);
                switch (checkedId) {
                    case R.id.rb_total_comment_score1:

                        break;
                    case R.id.rb_total_comment_score2:

                        break;
                    case R.id.rb_total_comment_score3:

                        break;
                    case R.id.rb_total_comment_score4:

                        break;
                    default:
                        break;
                }
            }
        });

    }

    private void initRadioButton(int checkedId) {
        Logger.i("v5-" + checkedId);
        if (checkedId == R.id.rb_total_comment_score1) {
            rbScore1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_1));
        } else {
            rbScore1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_off_1));
        }
        if (checkedId == R.id.rb_total_comment_score2) {
            rbScore2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_2));
        } else {
            rbScore2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_off_2));
        }
        if (checkedId == R.id.rb_total_comment_score3) {
            rbScore3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_3));
        } else {
            rbScore3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_off_3));
        }
        if (checkedId == R.id.rb_total_comment_score4) {
            rbScore4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_4));
        } else {
            rbScore4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.v5_eva_state_off_4));
        }
    }


}
