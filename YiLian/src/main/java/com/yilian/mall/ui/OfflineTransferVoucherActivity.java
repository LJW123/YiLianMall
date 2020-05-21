package com.yilian.mall.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Compressor;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.networkingmodule.entity.MyBalanceEntity2;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/9/4 0004.
 * 提交转账凭证
 */

public class OfflineTransferVoucherActivity extends BaseActivity {
    @ViewInject(R.id.iv_back)
    ImageView ivBack;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.tv_right)
    TextView tvRight;

    @ViewInject(R.id.tv_user_phone)
    TextView tvUserPhone;
    @ViewInject(R.id.tv_user_balance)
    TextView tvUserBalance;
    @ViewInject(R.id.et_money)
    EditText etMoney;
    @ViewInject(R.id.et_remark)
    EditText etRemark;
    @ViewInject(R.id.et_name)
    EditText etName;
    @ViewInject(R.id.iv_history)
    ImageView ivHistory;
    @ViewInject(R.id.et_card)
    EditText etCard;
    @ViewInject(R.id.et_bank)
    EditText etBank;
    @ViewInject(R.id.iv)
    ImageView iv;
    @ViewInject(R.id.btn_commit)
    Button btnCommit;

    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int PHOTO_ALBUM = 2;//打开相册
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//请求读取SD卡的权限使用请求码
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;

    private static String ImageName;
    private PopupWindow popupWindow;

    private String amountStr, remarkStr, cardNameStr, cardNumStr, cardBankStr, cardVoucherStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_transfer_voucher);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText("线下转账凭证");
        tvRight.setVisibility(View.GONE);

        etMoney.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().contains(".")) {
                    Logger.i("走了这里含点");
                    etMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        etMoney.setText(s);
                        etMoney.setSelection(s.length());
                        Logger.i("走了这里0");
                    }
                } else {
                    Logger.i("走了这里不含点");
                    etMoney.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                }

                if (s.toString().length() == 9 && !s.toString().endsWith(".") && !s.toString().contains(".")) {
                    etMoney.setText(s.subSequence(0, s.toString().length() - 1));
                    etMoney.setSelection(8);
                    Logger.i("走了这里1");
                }

                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    etMoney.setText(s);
                    etMoney.setSelection(2);
                    Logger.i("走了这里2");
                }

                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        etMoney.setText(s.subSequence(0, 1));
                        etMoney.setSelection(1);
                        Logger.i("走了这里3");
                        return;
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void initData() {
        if (isLogin()) {
            getUserBalance();
            String phone = sp.getString("phone", "");
            tvUserPhone.setText("需充值账户：" + phone);
//            BigDecimal value1 = new BigDecimal(Double.parseDouble(sp.getString("lebi", "0.00")));
//            BigDecimal value2 = new BigDecimal(100);
//            tvUserBalance.setText("奖励：¥" + value1.divide(value2));
        }

        ivHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OfflineTransferRecordActivity.class);
                intent.putExtra("from_type", "OfflineTransferVoucherActivity");
                startActivityForResult(intent, Constants.OFFLINE_TRANSFER_RECORD);
            }
        });

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upLoadVoucher(iv);
            }
        });

        btnCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });

        bankCardNumAddSpace(etCard);
    }

    /**
     * 获取用户奖励
     */
    private void getUserBalance() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getMyBalance(new Callback<MyBalanceEntity2>() {
                    @Override
                    public void onResponse(Call<MyBalanceEntity2> call, Response<MyBalanceEntity2> response) {
                        MyBalanceEntity2 body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        BigDecimal value1 = new BigDecimal(body.lebi);
                                        BigDecimal value2 = new BigDecimal("100");
                                        tvUserBalance.setText("奖励：¥" + value1.divide(value2));

                                        sp.edit().putString("lebi", String.valueOf(body.lebi)).commit();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<MyBalanceEntity2> call, Throwable t) {
                        BigDecimal value1 = new BigDecimal(sp.getString("lebi", "0.00"));
                        BigDecimal value2 = new BigDecimal("100");
                        tvUserBalance.setText("奖励：¥" + value1.divide(value2));
                        stopMyDialog();
                    }
                });
    }


    /**
     * 提交
     */
    private void submit() {
        amountStr = etMoney.getText().toString().trim();
        remarkStr = etRemark.getText().toString().trim();
        cardNameStr = etName.getText().toString().trim();
        cardNumStr = etCard.getText().toString().trim();
        cardNumStr = cardNumStr.replaceAll("\\s", "");
        cardBankStr = etBank.getText().toString().trim();

        if (TextUtils.isEmpty(amountStr) || "0.00".equals(amountStr) || "0".equals(amountStr) || "0.0".equals(amountStr)) {
            showToast(R.string.please_complete_money_first);
            return;
        }
        if (TextUtils.isEmpty(cardNameStr)) {
            showToast(R.string.please_complete_name_first);
            return;
        }
        if (TextUtils.isEmpty(cardNumStr)) {
            showToast(R.string.please_complete_card_first);
            return;
        }
        if (cardNumStr.length() < 15) {
            showToast(R.string.please_write_right_num);
            return;
        }
        if (TextUtils.isEmpty(cardBankStr)) {
            showToast(R.string.please_complete_bank_first);
            return;
        }
        if (TextUtils.isEmpty(cardVoucherStr)) {
            showToast(R.string.please_complete_voucher_first);
            return;
        }

        startMyDialog();
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext)).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext))
                .userTransfer(amountStr, remarkStr, cardNameStr, cardNumStr, cardBankStr, cardVoucherStr, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        stopMyDialog();
                        HttpResultBean body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        showToast(body.msg);
                                        Intent intent = new Intent(mContext, OfflineTransferRecordActivity.class);
                                        intent.putExtra("from_type", "OfflineTransferActivity");
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.net_work_not_available);
                    }
                });
    }

    private void upLoadImage(File file) {
        startMyDialog(false);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part imgPhoto = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", requestBody);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .AddressuploadFile(imgPhoto, new Callback<UploadImageEnity>() {
                    @Override
                    public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        cardVoucherStr = response.body().filename;
                                        if (!TextUtils.isEmpty(cardVoucherStr)) {
                                            GlideUtil.showImageNoSuffix(mContext, cardVoucherStr, iv);
                                        } else {
                                            iv.setImageResource(R.mipmap.library_module_ic_after_sale_img);
                                        }
                                        showToast("上传成功");
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                        showToast(R.string.net_work_not_available);
                        stopMyDialog();
                    }
                });
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
     * 开启照相机
     */
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ImageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), ImageName);

        FileUtils.startActionCapture(OfflineTransferVoucherActivity.this, file, ALBUM_CAMERA_REQUEST_CODE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//        startActivityForResult(intent, ALBUM_CAMERA_REQUEST_CODE);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    /**
     * 跳转至相册选择
     */
    public void photoalbum(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
            requestWrite_External_Storage();
            return;
        }

        albumSelect();
    }

    /**
     * 选择相册
     */
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//跳转至相册相册存在的图片url
        startActivityForResult(intent, PHOTO_ALBUM);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String fileSrc;
        switch (requestCode) {
            case Constants.OFFLINE_TRANSFER_RECORD:
                if (resultCode == RESULT_OK) {
                    etName.setText(data.getStringExtra("card_name"));
                    etCard.setText(data.getStringExtra("card_num"));
                    etBank.setText(data.getStringExtra("card_bank"));
                }
                break;
            case ALBUM_CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (data == null) {
                    Logger.i("resultCode4  " + resultCode + "  requestCode " + requestCode + " intent:" + data);
                    String pathname = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                    int i = BitmapUtil.readPictureDegree(pathname);//获取照片被旋转的角度
                    Logger.i("照片旋转角度：" + i);
                    File picFile = new File(pathname);
                    try {
                        File file = compressImage(picFile);
                        if (file != null) {
                            FileInputStream fileInputStream = new FileInputStream(file);
                            Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                            Bitmap bitmap1 = BitmapUtil.rotaingImageView(i, bitmap);//将照片旋转的角度还原过来，解决三星手机拍照自动旋转90度的问题
                            Uri uri = saveBitmap(bitmap1);
                            File file1 = new File(new URI(uri.toString()));
                            upLoadImage(file1);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
            case PHOTO_ALBUM:
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                if (null == data) {
                    return;
                }
                Logger.i("data.getData().getScheme  " + data.getData().getScheme());
                if ("file".equals(data.getData().getScheme())) {
                    //有些低版本返回Uri模式为file
                    fileSrc = data.getData().getPath();
                } else {
                    Uri selectImage = data.getData();
                    Logger.i("data.getData().getScheme33333  " + selectImage);
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(selectImage, filePathColumns, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    fileSrc = cursor.getString(columnIndex);
                    cursor.close();
                }
                Bitmap smallBitmap = BitmapUtil.getSmallBitmap(fileSrc, 350, 350);
                Logger.i("imagePath  " + fileSrc);
                Logger.e("smallBitmap " + smallBitmap);
                File file = new File(fileSrc);
                try {
                    File compressFile = compressImage(file);
                    if (compressFile != null) {
                        upLoadImage(compressFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yilian/userphoto/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + "user.png");
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

    private File compressImage(File picFile) throws IOException {
        if (picFile == null) {
            return null;
        }
        File file = new Compressor(this)
                .setMaxHeight(150)
                .setMaxWidth(150)
                .setQuality(75)
                .compressToFile(picFile);
        try {
            long fileSize = FileUtils.getFileSize(file);
            if (fileSize > 256000) {
                file = compressImage(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    private void upLoadVoucher(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    private void getPhotoPopupWindow(int resource, int width, int height, int animationStyle) {
        if (null != popupWindow) {
            popupWindow.dismiss();
            return;
        } else {
            initPhotoPopuptWindow(resource, width, height, animationStyle);
        }
    }

    protected void initPhotoPopuptWindow(int resource, int width, int height, int animationStyle) {
        // 获取自定义布局文件activity_popupwindow_left.xml的视图
        View popupWindow_view = getLayoutInflater().inflate(resource, null, false);
        popupWindow = new PopupWindow(popupWindow_view, width, height, true);
        // 设置动画效果
        popupWindow.setAnimationStyle(animationStyle);
        backgroundAlpha(0.2f);
        // 点击其他地方消失
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ColorDrawable cd = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(cd);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
    }

    private void bankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0; //记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }
}
