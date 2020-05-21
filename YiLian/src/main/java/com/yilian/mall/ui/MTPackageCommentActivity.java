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
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.adapter.ImageDeleteAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.UploadImageData;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;

import java.io.ByteArrayOutputStream;
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
 * 套餐评论晒单 界面
 */
public class MTPackageCommentActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private RadioButton rbMtTotalCommentScore1;
    private RadioButton rbMtTotalCommentScore2;
    private RadioButton rbMtTotalCommentScore3;
    private RadioButton rbMtTotalCommentScore4;
    private RadioButton rbMtTotalCommentScore5;
    private RadioGroup rgMtTotalComment;
    private ImageView ivStart;
    private TextView tvMtEnvironmentScore;
    private TextView tvMtServiceScore;
    private EditText etTotalComment;
    private RecyclerView rvView;
    private RatingBar ratingBarEnvironment;
    private LinearLayout activityMtpakcageComment;
    private RatingBar ratingBarService;

    private PopupWindow popupWindow;
    MTNetRequest mtNetRequest;
    private static String ImageName;
    //提交订单时所需要的数据
    private String orderId;//订单ID 由上个界面传递过来
    private String totalScore;//总评分
    private final String DEFAULTSCORE = "50";//默认评分50分
    private String environmentScore = DEFAULTSCORE;//环境评分
    private String serviceScore = DEFAULTSCORE;//服务评分
    private String commentContent;//评论内容
    private ArrayList<String> imgUrls;//图片URL

    RatingBar.OnRatingBarChangeListener ratingBarChangeListener = new RatingBar.OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            switch (ratingBar.getId()) {
                case R.id.rating_bar_environment:
                    environmentScore = String.valueOf((int) rating * 10);//获取环境评分
                    Logger.i("environmentScore:" + environmentScore);
                    setSubCommentText(ratingBar, tvMtEnvironmentScore);
                    break;
                case R.id.rating_bar_service:
                    serviceScore = String.valueOf((int) rating * 10);//获取服务评分
                    Logger.i("serviceScore:" + serviceScore);
                    setSubCommentText(ratingBar, tvMtServiceScore);
                    break;
            }
        }
    };
    private ImageDeleteAdapter adapter;
    private ArrayList<String> imgList1 = new ArrayList<>();
    private ArrayList<String> imgList = new ArrayList<>();

    //根据评分 选择显示的评论分数的文本
    private void setSubCommentText(RatingBar ratingBar, TextView textView) {
        switch ((int) ratingBar.getRating()) {
            case 1:
                setSubCommentText1(ratingBar, textView, "环境1分", "服务1分");
                break;
            case 2:
                setSubCommentText1(ratingBar, textView, "环境2分", "服务2分");
                break;
            case 3:
                setSubCommentText1(ratingBar, textView, "环境3分", "服务3分");
                break;
            case 4:
                setSubCommentText1(ratingBar, textView, "环境4分", "服务4分");
                break;
            case 5:
                setSubCommentText1(ratingBar, textView, "环境5分", "服务5分");
                break;
        }
    }

    //将评论分数的文本显示在界面上
    private void setSubCommentText1(RatingBar ratingBar, TextView textView, String string1, String string2) {
        if (ratingBar.getId() == R.id.rating_bar_environment) {
            textView.setText(string1);
        } else {
            textView.setText(string2);
        }
        textView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtpakcage_comment);
        orderId = getIntent().getStringExtra(Constants.SPKEY_MT_PACKAGE_ORDER_ID);
        initView();
        initListener();
        initData();
    }

    private void initView() {
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        rbMtTotalCommentScore1 = (RadioButton) findViewById(R.id.rb_mt_total_comment_score1);
        rbMtTotalCommentScore1.setText("差");
        rbMtTotalCommentScore2 = (RadioButton) findViewById(R.id.rb_mt_total_comment_score2);
        rbMtTotalCommentScore2.setText("一般");
        rbMtTotalCommentScore3 = (RadioButton) findViewById(R.id.rb_mt_total_comment_score3);
        rbMtTotalCommentScore3.setText("满意");
        rbMtTotalCommentScore4 = (RadioButton) findViewById(R.id.rb_mt_total_comment_score4);
        rbMtTotalCommentScore4.setText("很满意");
        rbMtTotalCommentScore5 = (RadioButton) findViewById(R.id.rb_mt_total_comment_score5);
        rbMtTotalCommentScore5.setText("强烈推荐");
//        .........................................................................
//        初始化满意程度
        rgMtTotalComment = (RadioGroup) findViewById(R.id.rg_mt_total_comment);
        rgMtTotalComment.check(R.id.rb_mt_total_comment_score5);
        setDrawableBotton(R.id.rb_mt_total_comment_score5);
//        .........................................................................

        totalScore = "50";
        ivStart = (ImageView) findViewById(R.id.iv_start);
//        环境评分
        tvMtEnvironmentScore = (TextView) findViewById(R.id.tv_mt_environment_score);
        tvMtEnvironmentScore.setText("环境5分");
//        服务评分
        tvMtServiceScore = (TextView) findViewById(R.id.tv_mt_service_score);
        tvMtServiceScore.setText("服务5分");


        etTotalComment = (EditText) findViewById(R.id.et_total_comment);
        rvView = (RecyclerView) findViewById(R.id.rv_view);
        imgList1.add("add");
        adapter = new ImageDeleteAdapter(mContext, imgList1, imgList, new BitmapUtils(mContext));
        rvView.setAdapter(adapter);
        rvView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        activityMtpakcageComment = (LinearLayout) findViewById(R.id.activity_mtpakcage_comment);


        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText("评论晒单");
        tvTitle.setVisibility(View.VISIBLE);
        ivRight2.setVisibility(View.GONE);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setTextColor(getResources().getColor(R.color.color_red));
        tvRight.setText("发布");

        ratingBarEnvironment = (RatingBar) findViewById(R.id.rating_bar_environment);
        ratingBarEnvironment.setRating(5.0f);//初始化默认值
        ratingBarEnvironment.setOnClickListener(this);
        ratingBarService = (RatingBar) findViewById(R.id.rating_bar_service);
        ratingBarService.setRating(5.0f);
        ratingBarService.setOnClickListener(this);
    }

    private void initData() {

    }

    private void initListener() {
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add"))
                    imgUpload(MTPackageCommentActivity.this.getWindow().getDecorView());

            }
        });
        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
//        总评分监听 获取总评分
        rgMtTotalComment.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setDrawableBotton(checkedId);
                switch (checkedId) {
                    case R.id.rb_mt_total_comment_score1:
                        ivStart.setImageResource(R.mipmap.mt_total_comment_start1);
                        totalScore = "10";

                        break;
                    case R.id.rb_mt_total_comment_score2:
                        ivStart.setImageResource(R.mipmap.mt_total_comment_start2);
                        totalScore = "20";
                        break;
                    case R.id.rb_mt_total_comment_score3:
                        ivStart.setImageResource(R.mipmap.mt_total_comment_start3);
                        totalScore = "30";
                        break;
                    case R.id.rb_mt_total_comment_score4:
                        ivStart.setImageResource(R.mipmap.mt_total_comment_start4);
                        totalScore = "40";
                        break;
                    case R.id.rb_mt_total_comment_score5:
                        ivStart.setImageResource(R.mipmap.mt_total_comment_start5);
                        totalScore = "50";
                        break;
                }
            }
        });

        ratingBarService.setOnRatingBarChangeListener(ratingBarChangeListener);
        ratingBarEnvironment.setOnRatingBarChangeListener(ratingBarChangeListener);
    }

    /**
     * 设置radioButton的下方图片
     *
     * @param checkedId
     */
    private void setDrawableBotton(int checkedId) {
        if (checkedId == R.id.rb_mt_total_comment_score1) {
            rbMtTotalCommentScore1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score1_1));
        } else {
            rbMtTotalCommentScore1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score1_0));
        }
        if (checkedId == R.id.rb_mt_total_comment_score2) {
            rbMtTotalCommentScore2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score2_1));
        } else {
            rbMtTotalCommentScore2.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score2_0));
        }
        if (checkedId == R.id.rb_mt_total_comment_score3) {
            rbMtTotalCommentScore3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score3_1));
        } else {
            rbMtTotalCommentScore3.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score3_0));
        }
        if (checkedId == R.id.rb_mt_total_comment_score4) {
            rbMtTotalCommentScore4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score4_1));
        } else {
            rbMtTotalCommentScore4.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score4_0));
        }
        if (checkedId == R.id.rb_mt_total_comment_score5) {
            rbMtTotalCommentScore5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score5_1));
        } else {
            rbMtTotalCommentScore5.setCompoundDrawablesWithIntrinsicBounds(null, null, null, ContextCompat.getDrawable(mContext, R.drawable.mt_comment_score5_0));
        }
    }

    //    获取评论内容
    private void getData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:
                commitComment();
                break;
        }

    }

    //发布评论
    private void commitComment() {
        if (TextUtils.isEmpty(totalScore)) {
            showToast("请选择满意程度");
            return;
        }
        if (TextUtils.isEmpty(environmentScore)) {
            showToast("请选择环境评分");
            return;
        }
        if (TextUtils.isEmpty(serviceScore)) {
            showToast("请选择服务评分");
            return;
        }
        commentContent = etTotalComment.getText().toString().trim();
        if (TextUtils.isEmpty(commentContent)) {
            showToast("请填写评价");
            return;
        }
        if (commentContent.length() > 200) {
            showToast("评论内容太长,请少于200字");
            return;
        }
        if (mtNetRequest == null) {
            mtNetRequest = new MTNetRequest(mContext);
        }
        mtNetRequest.commitPackageComment(orderId, totalScore, environmentScore, serviceScore, commentContent, getImageUrls(), new RequestCallBack<BaseEntity>() {
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
                        startActivity(new Intent(MTPackageCommentActivity.this, MTCommentSuccessActivity.class));
                        finish();
                        break;
                    case -3:
                        showToast(R.string.system_busy);
                        break;
                    case -4:
                        showToast(R.string.login_failure);
                        sp.edit().putBoolean(Constants.SPKEY_STATE, false).commit();//将登陆状态设置为false；
                        startActivity(new Intent(MTPackageCommentActivity.this, LeFenPhoneLoginActivity.class));
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

    /**
     * 跳转到系统裁剪图片界面
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
     * 保存图片到本地磁盘
     *
     * @param bm
     * @return
     */
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
     * 获取评论图片URL地址 多个地址以逗号分隔
     *
     * @return
     */
    public String getImageUrls() {
        StringBuffer sb = new StringBuffer();

        for (String s : imgList) {

        }

        for (int i = 0; i < imgList.size(); i++) {
            if (i == imgList.size() - 1) {
                sb.append(imgList.get(i));
            } else {
                sb.append(imgList.get(i) + ",");
            }
        }
        return sb.toString();
    }

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
                                                    if (imgList.size() < 3) {
                                                        imgList1.add("add");
                                                    }
                                                    rvView.setVisibility(View.VISIBLE);
                                                    adapter.notifyDataSetChanged();
                                                    stopMyDialog();
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

    /**
     * 图片上传到服务器
     *
     * @param bm
     */
    private void sendImage(Bitmap bm) {
        Logger.i("开始上传图片2");
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();
        final String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        Logger.i("开始上传  图片地址是：" + img);
        MallNetRequest accountNetRequest = new MallNetRequest(mContext);
        accountNetRequest.uploadGoodsImg(img, new RequestCallBack<UploadImageData>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<UploadImageData> responseInfo) {
                stopMyDialog();
                if (responseInfo.result.data != null && responseInfo.result.code == 1) {
                    Logger.i("返回图片地址是：" + responseInfo.result.url);
                    Logger.i("返回图片地址是：" + responseInfo.result.data);
                    imgList.add(responseInfo.result.data);
                    imgList1.clear();
                    imgList1.addAll(imgList);
                    if (imgList.size() < 3) {
                        imgList1.add("add");
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    showToast("图片过大，请重试" + responseInfo.result.code);
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("上传失败");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == 1) {//从拍照界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                File picture = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);
                sendImage(picture);
//                Uri uri = Uri.fromFile(picture);
//                startImageZoom(uri);
                //startCropImage(uri.toString());
                return;
            } else {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap bm = extras.getParcelable("data");
                    Uri uri = saveBitmap(bm);
                }
            }
        } else if (requestCode == 2) {//从图库选择界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                return;
            }
//            String path = data.getStringExtra("path");
//            Logger.i("path: " + path);
//            Bitmap smallBitmap = BitmapUtil.getSmallBitmap(path, 100, 100);
//            sendImage(smallBitmap);
//            startCropImage(path);//从相册选取照片后取消裁剪，直接上传

            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            Bitmap smallBitmap = BitmapUtil.getSmallBitmap(imagePath, 100, 100);
            c.close();
            sendImage(new File(imagePath));

        } else if (requestCode == 3) {//从裁剪图片界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                return;
            }
            Bundle extras = data.getExtras();
            if (extras == null) {
                return;
            }
            Bitmap bm = extras.getParcelable("data");

            sendImage(bm);

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
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
                if (grantResults.length>0){
                    if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                        albumSelect();
                    }else {
                        showToast("请开启读取SD卡权限");
                    }
                }
                break;
        }
    }

    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;

    /**
     * 拍照
     *
     * @param view
     */
    public void camera(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
//申请权限，
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
     * 跳转至相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
//        Intent intent = new Intent(this, DialogImageActivity.class);
//        intent.setType("image/*");
//        startActivityForResult(intent, 2);
//        if (popupWindow != null && popupWindow.isShowing()) {
//            popupWindow.dismiss();
//            backgroundAlpha(1f);
//        }
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
        startActivityForResult(intent, 2);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
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

}
