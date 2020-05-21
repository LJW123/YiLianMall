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
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mylibrary.GoodsType;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.adapter.ImageDeleteAdapter;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPCommodityDetail;
import com.yilian.mall.entity.UploadImageEnity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.widgets.CircleImageView1;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;

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
 * 商品评价界面
 */
public class EvaluateActivity extends BaseActivity {

    public static final int CAMERA_REQUEST_CODE = 1;
    public static final int PHOTO_ALBUM = 2;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    private static String ImageName;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.rv_view)
    RecyclerView rvImg;
    BitmapUtils bu;
    MallNetRequest netRequest;
    JPNetRequest jpNetRequest;
    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> imgList1 = new ArrayList<>();
    Bitmap photo;
    @ViewInject(R.id.v3Back)
    private ImageView ivBack;
    @ViewInject(R.id.iv_goods)
    private CircleImageView1 ivGoods;
    @ViewInject(R.id.tv_goods_name)
    private TextView tvGoodsName;
    @ViewInject(R.id.rb_evaluate)
    private RatingBar rbEvaluate;
    @ViewInject(R.id.tv_goods_price)
    private TextView tvGoodsPrice;
    //    @ViewInject(R.id.tv_goods_integral)
//    private TextView tvGoodsIntegral;
//    @ViewInject(R.id.tv_score)
//    private TextView tvFen;
//    @ViewInject(R.id.tv_icon_integral)
//    private TextView tvIconIntegral;
    @ViewInject(R.id.tv_evaluate_count)
    private TextView tvEvaluateCount;
    @ViewInject(R.id.tv_shopping_count)
    private TextView tvShoppingCount;
    @ViewInject(R.id.tv_goods_grade)
    private TextView mTvGrade;
    @ViewInject(R.id.rb_grade)
    private RatingBar mRbGrade;
    @ViewInject(R.id.ed_evaluate)
    private EditText mEdEvaluate;
    @ViewInject(R.id.chk)
    private CheckBox mChkAnonymous;
    @ViewInject(R.id.iv_yhs_icon)
    private ImageView ivYhsIcon;//易划算icon

    @ViewInject(R.id.tv_goods_beans)
    private TextView tvGoodsBeans; // 送益豆
    @ViewInject(R.id.tv_subsidy)
    private TextView tvSubsidy; // 平台加赠益豆

    private ImageDeleteAdapter adapter;
    private PopupWindow popupWindow;
    private String filiale_id = "0";
    private String order_type;
    private String filiale_id1;
    private String goods_id;
    private String orderId;
    private String orderGoodsIndex;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluate);
        ViewUtils.inject(this);
        netRequest = new MallNetRequest(mContext);
        bu = new BitmapUtils(mContext);
        imgList1.add("add");
        adapter = new ImageDeleteAdapter(mContext, imgList1, imgList, bu);
        rvImg.setAdapter(adapter);
        rvImg.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        getGoods();
        setListener();
    }

    private void getGoods() {
        startMyDialog();

        order_type = getIntent().getStringExtra("order_type");
        filiale_id = getIntent().getStringExtra("filiale_id");
        goods_id = getIntent().getStringExtra("goods_id");
        orderId = getIntent().getStringExtra("orderId");
        orderGoodsIndex = getIntent().getStringExtra("orderGoodsIndex");
        type = getIntent().getStringExtra("type");
        Logger.i("order_type  " + order_type + " type " + type);
        Logger.i("filiale_id  " + filiale_id);
        Logger.i("goods_id  " + goods_id);
        Logger.i("order_id  " + orderId);
        Logger.i("orderGoddsIndex  " + orderGoodsIndex);

        if (!TextUtils.isEmpty(filiale_id1)) {
            return;
        }
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        jpNetRequest.getV3MallGoodInfo(filiale_id, goods_id, type, new RequestCallBack<JPCommodityDetail>() {
            @Override
            public void onSuccess(ResponseInfo<JPCommodityDetail> responseInfo) {
                stopMyDialog();
                JPCommodityDetail result = responseInfo.result;
                JPCommodityDetail.DataBean data = result.data;
                JPCommodityDetail.DataBean.GoodInfoBean goodInfo = data.goodInfo;
                if (goodInfo == null) {
                    showToast("获取商品信息失败");
                    return;
                }
                switch (result.code) {
                    case 1:
                        initData(goodInfo);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    private void setListener() {

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add")) {
                    imgUpload(EvaluateActivity.this.getWindow().getDecorView());
                }
            }
        });
    }

    /**
     * 加载 view 数据
     *
     * @param mGoodInfo
     */
    private void initData(JPCommodityDetail.DataBean.GoodInfoBean mGoodInfo) {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EvaluateActivity.this.finish();
            }
        });
        tvTitle.setText("商品评价");

        GlideUtil.showImageWithSuffix(mContext, mGoodInfo.goodsAlbum.get(0), ivGoods);
        switch (mGoodInfo.type) {
            case "0":
                ivYhsIcon.setVisibility(View.GONE);
                tvShoppingCount.setText(getString(R.string.sales_volume2, mGoodInfo.goodsSale));
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(mGoodInfo.goodsCost)));
//                tvFen.setVisibility(View.VISIBLE);
//                tvFen.setText(getString(R.string.back_score) + MoneyUtil.getLeXiangBi(mGoodInfo.returnIntegral));
                break;
            case "1":
                ivYhsIcon.setVisibility(View.VISIBLE);
                ivYhsIcon.setImageResource(R.mipmap.icon_yhs);
                tvGoodsPrice.setVisibility(View.GONE);
//                tvGoodsIntegral.setVisibility(View.VISIBLE);
//                tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney(MoneyUtil.getLeXiangBi(mGoodInfo.goodsIntegral)));
                break;
            case "2":
                ivYhsIcon.setVisibility(View.VISIBLE);
                ivYhsIcon.setImageResource(R.mipmap.icon_jfg);
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getLeXiangBi(mGoodInfo.retailPrice)));
                double integral = Double.parseDouble(mGoodInfo.goodsPrice) - Double.parseDouble(mGoodInfo.retailPrice);
//                tvGoodsIntegral.setVisibility(View.VISIBLE);
//                tvGoodsIntegral.setText(MoneyUtil.setSmallIntegralMoney("+" + MoneyUtil.getLeXiangBi(integral)));
                break;
            case "3":
            case GoodsType.CAL_POWER: //区块益豆专区商品
                tvGoodsPrice.setVisibility(View.VISIBLE);
                tvGoodsPrice.setText(MoneyUtil.set¥Money(MoneyUtil.getXianJinQuan(mGoodInfo.goodsCost)));
                ivYhsIcon.setVisibility(View.GONE);
                tvGoodsBeans.setVisibility(View.VISIBLE);
                tvGoodsBeans.setText("送 " + MoneyUtil.getLeXiangBi(mGoodInfo.returnBean));
                if (mGoodInfo.subsidy != 0) {
                    tvSubsidy.setVisibility(View.VISIBLE);
                    tvSubsidy.setText("平台加赠" + Constants.APP_PLATFORM_DONATE_NAME + " " + MoneyUtil.getLeXiangBi(mGoodInfo.subsidy));
                } else {
                    tvSubsidy.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
        tvGoodsName.setText(mGoodInfo.goodsName);
        tvEvaluateCount.setText("" + mGoodInfo.goodsGradeCount);
        float grade = (float) (Math.round((mGoodInfo.goodsGrade / 10.0f) * 10)) / 10;
        mTvGrade.setText(grade == 0 ? "5.0分" : grade + "分");
        rbEvaluate.setRating(grade == 0 ? (float) 5.0 : grade);
        rbEvaluate.setVisibility(View.VISIBLE);

        tvEvaluateCount.setText(getString(R.string.evaluation_count, mGoodInfo.goodsGradeCount));


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
     * 发表评论
     *
     * @param view
     */
    public void evaluate(View view) {
        String evaluateStr = mEdEvaluate.getText().toString();
        if (TextUtils.isEmpty(evaluateStr)) {
            showToast("亲，说两句哦~~");
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
        netRequest.Evaluate(orderId, orderGoodsIndex, goods_id, mChkAnonymous.isChecked() ? "1" : "0", mRbGrade.getRating() * 10,
                evaluateStr, sb.toString(), new RequestCallBack<BaseEntity>() {
                    @Override
                    public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                        stopMyDialog();
                        switch (responseInfo.result.code) {
                            case 1:
                                showToast("评价成功");
                                //刷新个人页面标识
                                sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
                                finish();
                                break;

                            default:
                                showToast("评价失败");
                                break;
                        }

                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        stopMyDialog();
                        showToast("网络请求失败");
                    }
                });
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
                                                    if (imgList.size() < 3) {
                                                        imgList1.add("add");
                                                    }
                                                    rvImg.setVisibility(View.VISIBLE);
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

}
