package com.yilian.mall.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.Ali;
import com.yilian.mall.entity.AliMessageEntity;
import com.yilian.mall.entity.AliSideEntity;
import com.yilian.mall.entity.UpLoadIdCardEntity;
import com.yilian.mall.http.MTNetRequest;
import com.yilian.mall.utils.Base64Encoder;
import com.yilian.mall.utils.BitmapUtil;
import com.yilian.mall.utils.HttpUtil;
import com.yilian.mall.utils.URIUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.RegExUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * 收货人身份证信息界面
 */
public class ConsigneeIdCardInformationActivity extends BaseActivity implements View.OnClickListener {

    private static final int UPLOADSUCCESS = 5;
    private final int GO_CAMERA = 1;
    private final int GO_ALBUM = 2;
    private final int GO_ZOMM = 3;
    private static final int UPDATAFROMALIYUN = 4;

    private String ImageName;
    private ImageView v3Back;
    private TextView v3Title;
    private TextView tvHint;
    private ImageView ivFrontIdcard;
    private ImageView ivBackIdcard;
    private TextView edUserName;
    private TextView edUserIdcard;
    private Button btnSave;
    private Context mContext;
    private PopupWindow popupWindow;
    private String name;
    private String IDtype;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATAFROMALIYUN:
                    stopMyDialog();
                    showToast("请重新上传或手动输入信息");
                    edUserName.setEnabled(true);
                    edUserIdcard.setEnabled(true);
                    break;
                case UPLOADSUCCESS:
                    stopMyDialog();

                    break;
            }
        }
    };

    private String faceURL = "";
    private String backURL = "";
    private MTNetRequest mtNetRequest;
    private String addressId;
    private ImageView ivFront;
    private ImageView ivBack;
    private String has_card;
    private String identity_front;
    private String identity_back;
    private TextView tvDelFront;
    private TextView tvDelBack;
    private String identity_id;
    private String identity_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consignee_id_card_information);
        mContext = this;
        initView();
        getCachePhote();
    }

    private void initView() {
        //上个界面传递过来的用户名字
        name = getIntent().getStringExtra("name");
        addressId = getIntent().getStringExtra("addressId");
        has_card = getIntent().getStringExtra("has_card");
        identity_front = getIntent().getStringExtra("identity_front");
        identity_back = getIntent().getStringExtra("identity_back");
        identity_id = getIntent().getStringExtra("identity_id");
        identity_name = getIntent().getStringExtra("identity_name");

        Logger.i("上个界面传递的数据  name  " + name);
        Logger.i("上个界面传递的数据  addressId  " + addressId);
        Logger.i("上个界面传递的数据  has_card  " + has_card);
        Logger.i("上个界面传递的数据  identity_front  " + identity_front);
        Logger.i("上个界面传递的数据  identity_back  " + identity_back);
        Logger.i("上个界面传递的数据  identity_id  " + identity_id);
        Logger.i("上个界面传递的数据  identity_name  " + identity_name);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("收货人身份证信息");
        tvHint = (TextView) findViewById(R.id.tv_hint);
        tvHint.setText(Html.fromHtml("<font color=\"#666666\">身份证照片</font> <font color=\"#999999\"> (上传后系统会自动加上清关专用水印)<small> <small> </font>"));
        ivFrontIdcard = (ImageView) findViewById(R.id.iv_front_idcard);
        ivBackIdcard = (ImageView) findViewById(R.id.iv_back_idcard);
        edUserName = (TextView) findViewById(R.id.tv_user_name);
        edUserIdcard = (TextView) findViewById(R.id.tv_user_idcard);
        tvDelFront = (TextView) findViewById(R.id.tv_front_ivdel);
        tvDelBack = (TextView) findViewById(R.id.tv_back_ivdel);

        ivFront = (ImageView) findViewById(R.id.iv_front);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        //默认进来的时候是可以编辑输入框的
        edUserIdcard.setEnabled(false);
        edUserName.setEnabled(false);
        btnSave = (Button) findViewById(R.id.btn_save);

        tvDelBack.setOnClickListener(this);
        tvDelFront.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivFront.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        ivBackIdcard.setOnClickListener(this);
        ivFrontIdcard.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        if (mtNetRequest == null)
            mtNetRequest = new MTNetRequest(mContext);
    }


    String[] faceUrls = new String[1];
    String[] backUrls = new String[1];

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back_idcard:
                ImageName = name + "1.png";
                IDtype = "back";
                showPopupWindow(view);
                break;
            case R.id.iv_front_idcard:
                ImageName = name + "0.png";
                edUserName.setText("");
                edUserIdcard.setText("");
                IDtype = "face";
                edUserIdcard.setEnabled(false);
                edUserName.setEnabled(false);
                showPopupWindow(view);
                break;
            case R.id.tv_back_ivdel:
                backURL = "";
                ivBack.setVisibility(View.GONE);
                tvDelBack.setVisibility(View.GONE);
                ivBackIdcard.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_front_ivdel:
                edUserIdcard.setText("");
                edUserName.setText("");
                has_card = "0";
                faceURL = "";
                ivFront.setVisibility(View.GONE);
                tvDelFront.setVisibility(View.GONE);
                ivFrontIdcard.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_back:
                backUrls[0] = backURL;
                imageBrower(0, backUrls);
                break;
            case R.id.iv_front:
                faceUrls[0] = faceURL;
                imageBrower(0, faceUrls);
                break;
            case R.id.btn_save:
                saveInformationData();
                break;
            case R.id.v3Back:
                finish();
                break;
        }
    }

    //保存
    private void saveInformationData() {
        String nameStr = edUserName.getText().toString().trim();
        String idcardStr = edUserIdcard.getText().toString().trim();
        boolean isIdcard = RegExUtils.isIdCard(idcardStr);
        if (TextUtils.isEmpty(faceURL)) {
            showToast("请上传身份证反面");
            return;
        }
        if (TextUtils.isEmpty(backURL)) {
            showToast("请上传身份证正面");
            return;
        }
        if (!name.equals(nameStr)) {
            showToast("身份证信息与收货人信息不符，请检查核对");
            return;
        }
        if (!isIdcard) {
            showToast("身份证号输入不正确");
            return;
        }
        Logger.i("name  " + name + "nameStr  " + nameStr + "idCard  " + idcardStr + "boolean  " + isIdcard);
        if (!name.equals(nameStr) || TextUtils.isEmpty(nameStr) || !isIdcard) {
            showToast("您输入的信息有误，请重新上传或更改信息");
            return;
        }
        if (TextUtils.isEmpty(addressId)) {
            showToast("收货地址信息有误，请检查核对");
            return;
        }

        startMyDialog();
        mtNetRequest.saveGolbalContactInfo(faceURL, backURL, idcardStr, nameStr, addressId, new RequestCallBack<UpLoadIdCardEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UpLoadIdCardEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        //缓存当前加上清关水印的图片
                        has_card = "1";
                        Intent intent = new Intent();
                        intent.putExtra("name", nameStr);
                        intent.putExtra("idCard", idcardStr);
                        intent.putExtra("faceURL", faceURL);
                        intent.putExtra("backURL", backURL);
                        intent.putExtra("hasCard", has_card);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case -3:
                        showToast(responseInfo.result.message);
                        break;
                    default:
                        Logger.i("保存身份证异常码 " + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    private void showPopupWindow(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1,
                LinearLayout.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        //这里是位置显示方式，在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    private void getPhotoPopupWindow(int resoure, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPopupWindow(resoure, width, height, animationStyle);
        }
    }

    private void initPopupWindow(int resoure, int width, int height, int animationStyle) {
        View popupWindowView = getLayoutInflater().inflate(resoure, null, false);
        popupWindow = new PopupWindow(popupWindowView, width, height, true);
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        //点击其他地方消失
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

    }

    private void backgroundAlpha(float alpha) {
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = alpha;
        getWindow().setAttributes(attributes);
        ColorDrawable colorDrawable = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(colorDrawable);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //不透明
                backgroundAlpha(1f);
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

    //跳转至相册选择
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
        startActivityForResult(intent, GO_ALBUM);
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
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GO_CAMERA:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }

                File picture = null;
                Bitmap bitmap = null;
                Uri uri = null;
                if (data == null) {
                    picture = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);
                    uri = Uri.fromFile(picture);
                } else {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        Bitmap imagBit = bundle.getParcelable("data");
                        //先判断该当前的片保存的时候是否旋转的角度
//                        Bitmap rotateBitmap = BitmapUtil.rotateBitmap(imagBit);
                        //获得保存图片的路径
                        String imagPath = getImagPath(mContext, ImageName);
                        bitmap = BitmapUtil.getRotateBitmap(imagPath, imagBit);
                    }
                }

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    //上传阿里云验证身份证信息
                    Uri finalUri = uri;
                    //拍照直接上传服务器加水印
                    String imageAbsolutePath = URIUtils.getImageAbsolutePath(ConsigneeIdCardInformationActivity.this, finalUri);
                    Bitmap smallBitmap = BitmapUtil.getSmallBitmap(imageAbsolutePath, 350, 350);
                    Bitmap rotateBitmap = BitmapUtil.getRotateBitmap(imageAbsolutePath, smallBitmap);
                    Logger.i("相机的拍照的图片  ", BitmapUtil.bitmaoToString(rotateBitmap));

                    if ("face".equals(IDtype)) {
                        verify(rotateBitmap);
                    }

                    upLoadIdCard(ImageName, rotateBitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                //startImageZoom(uri);
                break;
            case GO_ALBUM:
                String fileSrc;
                String columnName = null;
                if (null == data) {
                    return;
                }
                if ("file".equals(data.getData().getScheme())) {
                    // 有些低版本机型返回的Uri模式为file
                    fileSrc = data.getData().getPath();
                } else {
                    // Uri模型为content
                    String[] proj = {MediaStore.Images.Media.DATA};
                    Cursor cursor = this.getContentResolver().query(data.getData(), proj,
                            null, null, null);
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    fileSrc = cursor.getString(idx);
                    cursor.close();
                }

                Bitmap smallBitmap = BitmapUtil.getSmallBitmap(fileSrc, 350, 350);
                Logger.i("imagePath  " + fileSrc);
                Logger.e("smallBitmap " + smallBitmap);
                Bitmap rotateBitmap = BitmapUtil.getRotateBitmap(fileSrc, smallBitmap);
                //展示图片之前去获取加水印的图片
                Logger.i("选择相册的IDTYPE  " + IDtype + "图片名字 " + columnName);

                if ("face".equals(IDtype)) {
                    verify(rotateBitmap);
                }
                upLoadIdCard(ImageName, rotateBitmap);
                switchBitmap(rotateBitmap);

                break;
            case GO_ZOMM:
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
                //bm = BitmapUtil.rotateBitmap(bm);

                //upLoadIdCard(ImageName, bm);
                //switchBitmap(bm);
                break;
        }
    }


    private void switchBitmap(Bitmap Bitmap) {
        if ("back".equals(IDtype)) {
            ivBack.setImageBitmap(Bitmap);
        } else {
            ivFront.setImageBitmap(Bitmap);
        }
    }

    /**
     * 阿里云服务器验证身份证信息
     */
    private void verify(Bitmap bitmap) {
        startMyDialog();
        String imgBase64 = bitmaptoString(bitmap);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // 拼装请求body的json字符串
                JSONObject requestObj = new JSONObject();
                try {
                    JSONObject configObj = new JSONObject();
                    JSONObject obj = new JSONObject();
                    JSONArray inputArray = new JSONArray();
                    configObj.put("side", "face");
                    obj.put("image", getParam(50, imgBase64));
                    obj.put("configure", getParam(50, configObj.toString()));
                    inputArray.put(obj);
                    requestObj.put("inputs", inputArray);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                String content = requestObj.toString();
                String getPath = "/rest/160601/ocr/ocr_idcard.json";

                HttpUtil.getInstance().httpPostBytes(getPath, null, null, content.getBytes(Constants.CLOUDAPI_ENCODING), null, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Logger.i("识别错误！ 错误信息：" + e.getMessage());
                        handler.sendEmptyMessage(UPDATAFROMALIYUN);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response.code() != 200) {
                            handler.sendEmptyMessage(UPDATAFROMALIYUN);
                        } else {
                            Gson gson = new Gson();
                            Ali ali = gson.fromJson(response.body().string(), Ali.class);
                            showMessage(ali);
                        }
                    }
                });
            }
        }).start();

    }

    private JSONObject getParam(int type, String detaValue) {
        JSONObject object = new JSONObject();
        try {
            object.put("dataType", type);
            object.put("dataValue", detaValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    private void showMessage(Ali ali) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();

                Ali.OutputsBean.OutputValueBean jsonData = ali.outputs.get(0).outputValue;
                AliMessageEntity entity = gson.fromJson(jsonData.dataValue, AliMessageEntity.class);
                AliSideEntity side = gson.fromJson(entity.configStr, AliSideEntity.class);
                String str = "姓名：" + entity.name + Constants.CLOUDAPI_LF + "身份证号：" + entity.num;
                Logger.i("2017年2月23日 10:44:50--" + str);
                if (entity.success) {
                    Logger.i("IdType  " + IDtype + " SIDE.SIDE  " + side.side);
                    if ("face".equals(IDtype) && "face".equals(side.side) && name.equals(entity.name)) {
                        edUserName.setText(entity.name);
                        edUserName.setTextColor(getResources().getColor(R.color.color_h1));
                        edUserIdcard.setText(entity.num);
                    } else {
//                                showToast("身份证信息和收货人信息不一致,请重新上传");
                        //警告性信息不要使用吐司的方式与用户交互
                        showDialog(null, "当前身份证信息与收货信息不一致，请重新上传", null, 0, Gravity.CENTER, "是", null, true, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        dialog.dismiss();
                                        break;
                                }

                            }
                        }, mContext);
                    }
                    edUserIdcard.setEnabled(false);
                    edUserName.setEnabled(false);
                } else {
                    showToast("请重新上传或手动输入信息");
                    edUserIdcard.setEnabled(true);
                    edUserName.setEnabled(true);
                }

                stopMyDialog();
            }
        });

    }


    //保存信息至本地服务器
    private void upLoadIdCard(String imageName, Bitmap Bitmap) {
        ivBackIdcard.setEnabled(false);
        ivFrontIdcard.setEnabled(false);
        //上传之前判断是否大于服务器接受的最大长度
//        String bitmapStr = BitmapUtil.bitmaoToStringFromLength(Bitmap, 500000);
        String bitmapStr = BitmapUtil.bitmaoToString(Bitmap);
        Logger.i("上传加水印  图片不为null 图片名字  " + imageName);
        // Logger.i("上传加水印  图片的data bitStr  "+bitStr);
        startMyDialog();
        mtNetRequest.upLoadIDCardImagData(imageName, bitmapStr.length(), bitmapStr, new RequestCallBack<UpLoadIdCardEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UpLoadIdCardEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        String imagUrl = responseInfo.result.url;
                        if ("face".equals(IDtype)) {
                            ivFront.setVisibility(View.VISIBLE);
                            tvDelFront.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(Constants.ImageUrl + imagUrl + Constants.ImageSuffix, ivFront, options);
                            faceURL = imagUrl;
                        } else if ("back".equals(IDtype)) {
                            ivBack.setVisibility(View.VISIBLE);
                            tvDelBack.setVisibility(View.VISIBLE);
                            imageLoader.displayImage(Constants.ImageUrl + imagUrl, ivBack, options);
                            backURL = imagUrl;
                        }
                        ivBackIdcard.setEnabled(true);
                        ivFrontIdcard.setEnabled(true);
                        stopMyDialog();
                        break;
                    case -4:
                        ivBackIdcard.setEnabled(true);
                        ivFrontIdcard.setEnabled(true);
                        showToast("请重新登录");
                        break;
                    case -3:
                        ivBackIdcard.setEnabled(true);
                        ivFrontIdcard.setEnabled(true);
                        showToast("系统繁忙，请稍后重试");
                        break;
                    case -7:
                        ivBackIdcard.setEnabled(true);
                        ivFrontIdcard.setEnabled(true);
                        showToast("上传系统文件失败，请稍后重试");
                        break;
                    case -8:
                        ivBackIdcard.setEnabled(true);
                        ivFrontIdcard.setEnabled(true);
                        showToast("上传失败，请重新上传");
                        break;
                    default:
                        Logger.i("上传加水印错误返回码 " + responseInfo.result.code);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                ivBackIdcard.setEnabled(true);
                ivFrontIdcard.setEnabled(true);
                Logger.e("加水印错误内容 " + s);
                showToast("上传失败，请重新上传");
            }
        });
    }


    /**
     * 不压缩只是把Bitmap转为base64的字符串
     *
     * @param bitmap
     * @return
     */

    public String bitmaptoString(Bitmap bitmap) {
        // 将Bitmap转换成字符串
        String string = null;
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, bStream);
        byte[] bytes = bStream.toByteArray();
        Base64Encoder base = new Base64Encoder();
        string = Base64Encoder.encode(bytes); //

        return string;

    }


    //裁剪身份证图片
    private void startImageZoom(Uri uri) {
        Intent intent = new Intent("com.android.merchant_camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 350);
        intent.putExtra("outputY", 350);
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, GO_ZOMM);
    }

    public String getImagPath(Context context, String fileName) {
        String path;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            path = context.getFilesDir().getAbsolutePath();
            Logger.i("path = mContext.getFilesDir().getAbsolutePath() " + path);
        } else {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/com.yilian/userphoto/";
        }
        if (!path.contains("/")) {
            path += "/";
        }
        File file = new File(path);
        if (file != null && file.exists()) {
            file.mkdirs();
        }
        if (TextUtils.isEmpty(fileName)) {
            if ("face".equals(IDtype)) {
                path += name + "0.png";
            } else if ("back".equals(IDtype)) {
                path += name + "1.png";
            }

        } else {
            path += (fileName + ".png");
        }
        return path;
    }


    //    进来的时候获取上个界面传递的是否有缓存数据悠久显示没有就制成原始状态
    public void getCachePhote() {
        if ("1".equals(has_card) && name.equals(identity_name)) {
            faceURL = identity_front;
            backURL = identity_back;
            edUserIdcard.setText(identity_id);
            ivFront.setVisibility(View.VISIBLE);
            tvDelFront.setVisibility(View.VISIBLE);
            ivBack.setVisibility(View.VISIBLE);
            tvDelBack.setVisibility(View.VISIBLE);
            Logger.i("identity_front " + Constants.ImageUrl + identity_front + " identity_back " + Constants.ImageUrl + identity_back);
            imgVisOrInvis();
        }
    }

    private void imgVisOrInvis() {
        if (!TextUtils.isEmpty(identity_name) && identity_name != null) {
            edUserName.setText(name);
        }
        if (TextUtils.isEmpty(faceURL)) {
            ivFront.setVisibility(View.GONE);
            tvDelFront.setVisibility(View.GONE);
            ivFrontIdcard.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(backURL)) {
            ivBack.setVisibility(View.GONE);
            tvDelBack.setVisibility(View.GONE);
            ivBackIdcard.setVisibility(View.VISIBLE);
        }
        if (faceURL.contains("http://") || faceURL.contains("https://")) {
            imageLoader.displayImage(faceURL, ivFront);
        } else {
            imageLoader.displayImage(Constants.ImageUrl + faceURL, ivFront);
        }
        if (backURL.contains("http://") || backURL.contains("https://")) {
            imageLoader.displayImage(backURL, ivBack);
        } else {
            imageLoader.displayImage(Constants.ImageUrl + backURL, ivBack);
        }
    }

    private void imageBrower(int position, String[] urls) {
        Intent intent = new Intent(this.mContext, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        this.mContext.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
