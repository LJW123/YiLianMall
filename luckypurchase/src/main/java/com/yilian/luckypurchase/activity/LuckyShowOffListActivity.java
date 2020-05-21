package com.yilian.luckypurchase.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.ShowOffListAddPhotoAdapter;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.ImageCompressUtil;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.UploadImageEnity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author LYQ 晒单界面
 */
public class LuckyShowOffListActivity extends BaseAppCompatActivity implements View.OnClickListener {


    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//请求读取SD卡的权限使用请求码
    public static final int APPLY_WRITE_SDCARD_PERMISSION_REQUEST_CODE = 888;//请求读取SD卡的权限使用请求码
    private static final int GO_CAMERA = 99;//打开相机请求码
    private static final int GO_ALBUM = 97;//打开相册请求码

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView ivMore;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private EditText etRemark;
    private RecyclerView recycleView;
    private ArrayList<String> imageList = new ArrayList<>();
    private ShowOffListAddPhotoAdapter adapter;
    private PopupWindow popupWindow;
    private String imageName;
    private ArrayList<String> imageContentList = new ArrayList<>();
    private int totalCount;
    private TextView tvCommit;
    private String activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_show_off_list);
        activityId = getIntent().getStringExtra("activityId");
        imageList.add("add");//添加一个标记（区别是添加的按钮还是上传过的图片内容）
        initView();
        initListener();
    }

    private void initListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int i = view.getId();
                if (i == R.id.iv_add) {
                    //添加照片
                    showPopupWindow(view);
                } else if (i == R.id.iv_del) {
                    //移除当前条目的内容,但是要判断是否是第一张
                    deleteItem(adapter, position);

                }
            }
        });
    }

    private void deleteItem(BaseQuickAdapter adapter, int position) {
        adapter.remove(position);
        imageContentList.remove(position);
        //判断最后一个
        if (imageList.size() < 6 && !imageList.get(imageList.size() - 1).equals("add")) {
            imageList.add("add");
        }
    }

    /**
     * 选择相册或者相机的弹窗
     *
     * @param view
     */
    private void showPopupWindow(View view) {
        getPopupWindow(view);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    private void getPopupWindow(View view) {
        if (null != popupWindow) {
            popupWindow.dismiss();
        } else {
            initPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1,
                    LinearLayout.LayoutParams.WRAP_CONTENT, R.style.library_module_AnimationFade);
        }
    }

    public void initPopupWindow(int layout, int width, int height, int anim) {
        View view = View.inflate(mContext, layout, null);
        popupWindow = new PopupWindow(view, width, height, true);
        popupWindow.setAnimationStyle(anim);
        backgroundAlpha(0.2f);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
    }

    private void backgroundAlpha(float alpha) {
        //获取窗体的属性集重新设置
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = alpha;
        getWindow().setAttributes(attributes);

        ColorDrawable drawable = new ColorDrawable(0x000000);
        popupWindow.setBackgroundDrawable(drawable);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);//popupWindow消失时不透明
            }
        });
    }

    private void initView() {
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("晒单");
        tvRight = (TextView) findViewById(R.id.tv_right);
        ivMore = (ImageView) findViewById(R.id.v3Shop);
        ivMore.setImageResource(R.mipmap.library_module_v3_more_bottom);
        ivMore.setVisibility(View.VISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        etRemark = (EditText) findViewById(R.id.et_remark);
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        recycleView.setLayoutManager(new GridLayoutManager(mContext, 3));
        adapter = new ShowOffListAddPhotoAdapter(R.layout.lucky_item_add_photo, imageList);
        tvCommit = (TextView) findViewById(R.id.tv_commit);
        recycleView.setAdapter(adapter);
        totalCount = 6;

        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        tvCommit.setOnClickListener(this);
        ivMore.setOnClickListener(this);
    }

    /**
     * 相册选择
     *
     * @param view
     */
    public void photoalbum(View view) {
        //相册的读取权限
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE);
            showToast("请开启读写SD卡的权限");
            return;
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_SDCARD_PERMISSION_REQUEST_CODE);
        }
        albumSelect();
    }

    /**
     * 相册选择
     */
    private void albumSelect() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GO_ALBUM);
        if (null != popupWindow && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_SDCARD_PERMISSION_REQUEST_CODE);
            return;
        }
        //            有权限了
        openCamera();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case APPLY_CAMERA_PERMISSION_REQUEST_CODE:
                //相机权限
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        showToast("拍照权限被拒绝，请前往设置开启");
                    }
                }
                break;
            case APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE:
                //读SD卡的权限
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        albumSelect();
                    } else {
                        showToast("读取SD卡的权限暂未开启，请前往设置开启");
                    }
                }
                break;
            case APPLY_WRITE_SDCARD_PERMISSION_REQUEST_CODE:
                //写SD卡的权限
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        openCamera();
                    } else {
                        showToast("拍照权限被拒绝，请前往设置开启");
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
        imageName = System.currentTimeMillis() + ".png";
        File file = new File(Environment.getExternalStorageDirectory(), imageName);
        FileUtils.startActionCapture(this, file, GO_CAMERA);
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            backgroundAlpha(1f);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_commit) {
            commit();
        } else if (i == R.id.v3Shop) {
            showMenu();
        }
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (isLogin()) {
                            intent.setComponent(new ComponentName(LuckyShowOffListActivity.this, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(LuckyShowOffListActivity.this, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(LuckyShowOffListActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(LuckyShowOffListActivity.this, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }

    private void commit() {
        String content = etRemark.getText().toString().trim();
        String imagUrls = "";
        for (int i = 0; i < imageContentList.size(); i++) {
            if (i == imageContentList.size() - 1) {
                imagUrls += imageContentList.get(i);
            } else {
                imagUrls += imageContentList.get(i) + ",";
            }
        }

        if (TextUtils.isEmpty(content)) {
            showToast(R.string.lucky_show_no_tv);
            return;
        }

        if (content.length() > 200) {
            showToast(R.string.lucky_show_much_tv);
            return;
        }

        if (TextUtils.isEmpty(imagUrls)) {
            showToast(R.string.lucky_show_no_img);
            return;
        }

        Logger.i("图片集合toString  " + imageContentList.toString());
        startMyDialog(false);
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .snatchShowCommit(activityId, content, imagUrls, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast(response.body().msg);
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
                        stopMyDialog();
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case GO_CAMERA:
                //相机
                Logger.i("相机回调   " + resultCode);
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                Logger.i("data::  " + data);
                String pathname = Environment.getExternalStorageDirectory() + File.separator + imageName;
                File file2 = new File(pathname);
                file2 = ImageCompressUtil.compressImage(file2, ScreenUtils.getScreenWidth(mContext)/2, ScreenUtils.getScreenHeight(mContext)/2, 50, mContext);
                Logger.i("pathname:"+pathname);
                file2= BitmapUtil.restoreFile(pathname, file2, "luckyComment");//处理图片旋转问题
                sendImage(file2);
                break;
            case GO_ALBUM:
                //相册
                Logger.i("resultCode   " + resultCode);
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                Logger.i("data   " + data);
                if (null == data) {
                    return;
                }
                Logger.i("data.getData().getScheme()   " + data.getData().getScheme() + "  URI  " + data.getData());
                //解决低版本手机返回的URI模式为file
                String fileStr;
                if ("file".equals(data.getData().getScheme())) {
                    fileStr = data.getData().getPath();
                } else {
                    Uri uri = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, filePathColumns, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumns[0]);
                    fileStr = cursor.getString(columnIndex);
                    cursor.close();

                }
                if (null != fileStr || !fileStr.equals("")) {
                    Logger.i("fileStr: "+ fileStr);
                    File file = ImageCompressUtil.compressImage(new File(fileStr), ScreenUtils.getScreenWidth(mContext)/2,
                            ScreenUtils.getScreenHeight(mContext)/2, 50, mContext);
                    file= BitmapUtil.restoreFile(fileStr, file, "luckyComment");//处理图片旋转问题
                    sendImage(file);
                }
                break;
            default:
                Logger.i("请求码 ：： " + requestCode);
                break;
        }

    }

    /**
     * 上传图片
     *
     * @param file
     */
    private void sendImage(File file) {

        startMyDialog(false);

        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("file", System.currentTimeMillis() + "environment", requestBody);
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .uploadFile(part, new Callback<UploadImageEnity>() {
                    @Override
                    public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        initImageUrls(response);
                                        break;
                                    default:
                                        showToast(response.body().msg);
                                        break;
                                }
                            }
                        }
                        stopMyDialog();
                    }

                    @Override
                    public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                        showToast("上传图片失败");
                        stopMyDialog();
                    }
                });
    }

    private void initImageUrls(Response<UploadImageEnity> response) {
        String filename = response.body().filename;
        imageContentList.add(filename);
        imageList.clear();
        imageList.addAll(imageContentList);
        if (imageList.size() < totalCount) {
            imageList.add("add");
        }
        adapter.setNewData(imageList);
    }

}
