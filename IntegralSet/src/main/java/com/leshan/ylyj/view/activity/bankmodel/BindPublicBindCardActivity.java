package com.leshan.ylyj.view.activity.bankmodel;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.BaseAdapter;
import com.leshan.ylyj.adapter.BranchBankListAdapter;
import com.leshan.ylyj.adapter.ImageDeletePublicAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.RetrofitUtils;
import com.leshan.ylyj.utils.SkipUtils;
import com.lidroid.xutils.BitmapUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Compressor;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.MenuUtil;
import com.yilian.mylibrary.RegExUtils;
import com.yilian.mylibrary.SmsCodeEntity;
import com.yilian.mylibrary.StopDialog;
import com.yilian.mylibrary.widget.ClearEditText;
import com.yilian.networkingmodule.entity.UploadImageEnity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.BranchBankEntity;
import rxfamily.entity.JBankModel;

import static com.yilian.mylibrary.SmsCodeType.BIND_BANK_CARD;

public class BindPublicBindCardActivity extends BaseActivity {

    public static final int PHOTO_ALBUM = 2;
    public static final int APPLY_CAMERA_PERMISSION_REQUEST_CODE = 999;//请求相机权限使用请求码
    public static final int ALBUM_CAMERA_REQUEST_CODE = 1;//打开相机请求码
    public static final int APPLY_WRITE_EXTERNAL_STORAGE_CODE = 888;
    public static final int APPLY_READ_SDCARD_PERMISSION_REQUEST_CODE = 100;//读取权限的判断
    private static String ImageName;
    public String bankId;
    public BranchBankListAdapter branchBankListAdapter;
    /**
     *
     */
    RecyclerView rvImg;
    ArrayList<String> imgList = new ArrayList<>();
    ArrayList<String> imgList1 = new ArrayList<>();
    BitmapUtils bu;
    /**
     *
     */
    ArrayList<BranchBankEntity.ListBean> branchList = new ArrayList();
    ArrayList<JBankModel.ListBean> bankList = new ArrayList<>();
    ArrayList<JRegionModel.ListBean> provinceList = new ArrayList<>();
    ArrayList<ArrayList<JRegionModel.ListBean.CitysBean>> cityList = new ArrayList<>();
    Bitmap photo;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivRight;
    private ClearEditText etCompanyName;
    private ClearEditText etTaxNum;
    private TextView tvBankName;
    private TextView tvProAndCity;
    private ClearEditText etSubBankName;
    private ImageView ivSub;
    private ListView listView;
    private ClearEditText etBankUser;
    private ClearEditText etPhone;
    private ClearEditText etAddress;
    private Button okBtn;
    private ImageDeletePublicAdapter adapter;
    private PopupWindow popupWindow;
    /**
     * 绑定时传0
     */
    private String cardIndex = "0";
    private String provinceId, cityId;
    private BankPresenter presenter;
    private boolean hasAddress, hasBankUser, hasCompanyName, hasPhone, hasSub, hasTax, hasBankName, hasPro, hasCity, hasImg;
    //公司名称，税号，开户支行，账户，电话，地址，短信验证码，银行名称
    private String companyNameStr, taxNumStr, branchStr, bankUserStr, phoneStr, addressStr, smsCodeStr, banNameStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_public_bind_card);

        presenter = new BankPresenter(this, this);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("添加银行卡");
        ivRight = findViewById(R.id.iv_right);
        ivRight.setImageDrawable(getResources().getDrawable(R.mipmap.icon_setting));
        ivRight.setVisibility(View.VISIBLE);

        etCompanyName = findViewById(R.id.et_company_name);
        etTaxNum = findViewById(R.id.et_tax_num);
        tvBankName = findViewById(R.id.tv_bank_name);
        tvProAndCity = findViewById(R.id.tv_pro_and_city);
        etSubBankName = findViewById(R.id.et_sub_bank_name);
        ivSub = findViewById(R.id.iv_sub_bank);
        listView = findViewById(R.id.listView);
        etBankUser = findViewById(R.id.et_bank_user);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_address);
        rvImg = findViewById(R.id.recycler_view);
        okBtn = findViewById(R.id.btn_next);

        imgList1.add("add");
        rvImg.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        bu = new BitmapUtils(mContext);
        adapter = new ImageDeletePublicAdapter(mContext, imgList1, imgList, bu);
        rvImg.setAdapter(adapter);
        listView.setVisibility(View.GONE);
    }

    @Override
    protected void initListener() {
        adapter.setDelImgListener(new ImageDeletePublicAdapter.DelImageListener() {
            @Override
            public void onDelImg() {
                setBtnNextStepStatus();
            }
        });


        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuUtil.showMenu(BindPublicBindCardActivity.this, R.id.iv_right);
            }
        });

        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (imgList1.get(position).equals("add")) {
                    imgUpload(BindPublicBindCardActivity.this.getWindow().getDecorView());
                }
            }
        });

        tvBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bankList.size() <= 0) {
                    getBankList(true);
                } else {
                    showBankList();
                }

            }
        });

        tvProAndCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provinceList.size() <= 0 || cityList.size() <= 0) {
                    getRegion(true);
                } else {
                    showArea();
                }
            }
        });

        ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                    showToast("请先选择开户银行和省市");
                    return;
                }
                startMyDialog(false);
                presenter.getBranchBankNameList(provinceId, cityId, bankId, getBranchBankName());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSubBankName.setText(branchList.get(position).branchName);
                listView.setVisibility(View.GONE);
                etSubBankName.setSelection(etSubBankName.getText().toString().trim().length());
            }
        });

        etSubBankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                branchStr = etSubBankName.getText().toString().trim();
                if (TextUtils.isEmpty(branchStr)) {
                    listView.setVisibility(View.GONE);
                } else {
                    if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                        Logger.i("走了这里-111-");
                        listView.setVisibility(View.GONE);
                    } else {
                        Logger.i("走了这里-333-");
                        startMyDialog(false);
                        presenter.getBranchBankNameList(provinceId, cityId, bankId, getBranchBankName());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listView.setVisibility(View.GONE);
            }
        });

        etSubBankName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etSubBankName.setSelection(etSubBankName.getText().toString().trim().length());
                } else {
                    listView.setVisibility(View.GONE);
                }
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bindCard();
            }
        });

        setTextChangedListener(etAddress);
        setTextChangedListener(etBankUser);
        setTextChangedListener(etCompanyName);
        setTextChangedListener(etPhone);
        setTextChangedListener(etSubBankName);
        setTextChangedListener(etTaxNum);
    }

    @Override
    protected void initData() {
        startMyDialog();
        getBankList(false);
        getRegion(false);
    }

    @Override
    public void onError(Throwable e) {
        stopMyDialog();
        EventBus.getDefault().post(new StopDialog());
        listView.setVisibility(View.GONE);
        showToast(e.getMessage());
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        if (baseEntity instanceof BranchBankEntity) {
            AppManager.getInstance().killActivity(AppManager.getInstance().getActivity("CertificationResultImplViewActivity"));
            BranchBankEntity bean = (BranchBankEntity) baseEntity;
            branchList.clear();
            ArrayList<BranchBankEntity.ListBean> newList = bean.list;
            branchList.addAll(newList);
            if (branchList.size() == 0) {
                branchList.add(0, new BranchBankEntity.ListBean("暂无数据"));
            }

            if (branchBankListAdapter == null) {
                branchBankListAdapter = new BranchBankListAdapter(mContext, branchList);
                listView.setAdapter(branchBankListAdapter);
            } else {
                branchBankListAdapter.notifyDataSetChanged();
            }

            Logger.i("走了这里-监听-" + branchStr);
            Logger.i("走了这里-监听-" + branchList.toString());
            if (useLoop(branchList, branchStr)) {
                Logger.i("走了这里-222-");
                listView.setVisibility(View.GONE);
            } else {
                Logger.i("走了这里-444-");
                listView.setVisibility(View.VISIBLE);
            }
        } else {
            showToast(baseEntity.msg);
            setResult(RESULT_OK);
            EventBus.getDefault().post(new StopDialog());
            AppManager.getInstance().killTopActivity();
            finish();
        }
        stopMyDialog();
    }

    private static boolean useLoop(ArrayList<BranchBankEntity.ListBean> list, String str) {
        for (BranchBankEntity.ListBean s : list) {
            if (s.branchName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void getBankList(final boolean show) {
        if (show) {
            startMyDialog(false);
        }
        RetrofitUtils.getInstance(mContext).getBankList(new Callback<JBankModel>() {
            @Override
            public void onResponse(Call<JBankModel> call, Response<JBankModel> response) {
                stopMyDialog();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                    switch (response.body().code) {
                        case 1:
                            bankList.clear();
                            bankList.addAll(response.body().list);
                            if (show) {
                                showBankList();
                            }
                            break;
                        default:
                            showToast(response.body().msg);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JBankModel> call, Throwable t) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    private void getRegion(final boolean show) {
        if (show) {
            startMyDialog();
        }
        RetrofitUtils.getInstance(mContext).getRegion(new Callback<JRegionModel>() {
            @Override
            public void onResponse(Call<JRegionModel> call, Response<JRegionModel> response) {
                stopMyDialog();
                switch (response.body().code) {
                    case 1:
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            ArrayList<JRegionModel.ListBean> list = response.body().list;
                            provinceList.clear();
                            cityList.clear();
                            for (int i = 0; i < list.size(); i++) {
                                provinceList.add(list.get(i));
                                cityList.add(list.get(i).citys);
                                if (show) {
                                    showArea();
                                }

//                                ArrayList<ArrayList<JRegionModel.ListBean.CitysBean.CountysBean>> countysBean = new ArrayList<ArrayList<JRegionModel.ListBean.CitysBean.CountysBean>>();
//                                for (int j = 0; j < list.get(i).citys.size(); j++) {
//                                    countysBean.add(list.get(i).citys.get(j).countys);
//                                }
//                                countyList.add(countysBean);
                            }
                            break;
                        }

                    default:
                        showToast(response.body().msg);
                        break;
                }
            }

            @Override
            public void onFailure(Call<JRegionModel> call, Throwable t) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    private void showBankList() {
        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                bankId = bankList.get(options1).bankId;
                tvBankName.setText(bankList.get(options1).bankName);
                etSubBankName.setText("");

            }
        }).setTitleText("选择银行")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        optionsPickerView.setPicker(bankList);
        optionsPickerView.show();
    }

    private void showArea() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                JRegionModel.ListBean provinceBean = provinceList.get(options1);
                provinceId = provinceBean.regionId;

                JRegionModel.ListBean.CitysBean citysBean = cityList.get(options1).get(options2);
                tvProAndCity.setText(provinceBean.getPickerViewText() + "  " + citysBean.getPickerViewText());
                cityId = citysBean.regionId;
                etSubBankName.setText("");
//                JRegionModel.ListBean.CitysBean.CountysBean countyBean = countyList.get(options1).get(options2).get(options3);
//                tv_bank_branch3.setText(countyBean.getPickerViewText());
//                countyId = countyBean.regionId;
            }
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(provinceList, cityList);
        pvOptions.show();
    }

    private void setBtnNextStepStatus() {
        if (!TextUtils.isEmpty(etAddress.getText().toString().trim())) {
            hasAddress = true;
        } else {
            hasAddress = false;
        }

        if (!TextUtils.isEmpty(etBankUser.getText().toString().trim())) {
            hasBankUser = true;
        } else {
            hasBankUser = false;
        }

        if (!TextUtils.isEmpty(etCompanyName.getText().toString().trim())) {
            hasCompanyName = true;
        } else {
            hasCompanyName = false;
        }

        if (RegExUtils.isPhone(getPhone())) {
            hasPhone = true;
        } else {
            hasPhone = false;
        }

        if (!TextUtils.isEmpty(etSubBankName.getText().toString().trim())) {
            hasSub = true;
        } else {
            hasSub = false;
        }

        if (!TextUtils.isEmpty(etTaxNum.getText().toString().trim())) {
            hasTax = true;
        } else {
            hasTax = false;
        }

        if (!TextUtils.isEmpty(tvBankName.getText().toString())) {
            hasBankName = true;
        } else {
            hasBankName = false;
        }

        if (!TextUtils.isEmpty(provinceId)) {
            hasPro = true;
        } else {
            hasPro = false;
        }

        if (!TextUtils.isEmpty(cityId)) {
            hasCity = true;
        } else {
            hasCity = false;
        }

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgList.size(); i++) {
            if (i == imgList.size() - 1) {
                sb.append(imgList.get(i));
            } else {
                sb.append(imgList.get(i) + ",");
            }
        }
        if (!TextUtils.isEmpty(sb.toString())) {
            hasImg = true;
        } else {
            hasImg = false;
        }

        if (hasTax && hasSub && hasPhone && hasCompanyName && hasBankUser && hasAddress && hasBankName && hasPro && hasCity && hasImg) {
            okBtn.setBackgroundResource(R.drawable.bg_new_solid_red);
            okBtn.setClickable(true);
        } else {
            okBtn.setBackgroundResource(R.drawable.bg_solid_ccc);
            okBtn.setClickable(false);
        }

    }

    public void imgUpload(View view) {
        getPhotoPopupWindow(R.layout.library_module_popupwindow_amenduserphoto, -1, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.merchant_AnimationFade);
        // 这里是位置显示方式,在屏幕的底部
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        backgroundAlpha(0.2f);
    }

    /**
     * 获取输入的支行名称
     *
     * @return
     */
    String getBranchBankName() {
        return etSubBankName.getText().toString().trim();
    }

    private void setTextChangedListener(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnNextStepStatus();
            }
        });
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
     * 获取短信验证码后的验证逻辑
     *
     * @param smsCodeEntity
     */
    @Subscribe
    public void checkInfoBySmsCode(SmsCodeEntity smsCodeEntity) {
        smsCodeStr = smsCodeEntity.smsCode;
        bindCard();
    }

    /**
     * 绑定银行卡
     */
    public void bindCard() {
        companyNameStr = etCompanyName.getText().toString().trim();
        taxNumStr = etTaxNum.getText().toString().trim();
        bankUserStr = etBankUser.getText().toString().trim();
        phoneStr = getPhone();
        addressStr = etAddress.getText().toString().trim();
        banNameStr = tvBankName.getText().toString().trim();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < imgList.size(); i++) {
            if (i == imgList.size() - 1) {
                sb.append(imgList.get(i));
            } else {
                sb.append(imgList.get(i)).append(",");
            }
        }

        if (!TextUtils.isEmpty(companyNameStr)
                && !TextUtils.isEmpty(taxNumStr)
                && !TextUtils.isEmpty(bankUserStr)
                && !TextUtils.isEmpty(phoneStr)
                && !TextUtils.isEmpty(addressStr)
                && !TextUtils.isEmpty(provinceId)
                && !TextUtils.isEmpty(cityId)
                && !TextUtils.isEmpty(sb.toString())) {

            if (TextUtils.isEmpty(smsCodeStr)) {
                startSmsCodeActivity();
            } else {
                presenter.bindPublicCards(bankUserStr, banNameStr, phoneStr, branchStr, companyNameStr, taxNumStr, provinceId, cityId, addressStr, sb.toString(), smsCodeStr, cardIndex);
            }
        } else {
            showToast("请先完善信息");
        }
    }

    private String getPhone() {
        String phone = etPhone.getText().toString().trim();
        if (RegExUtils.isPhone(phone)) {
            return phone;
        }
        return null;
    }

    private void startSmsCodeActivity() {
        SkipUtils.toSMSCheck(mContext, getPhone(), BIND_BANK_CARD);
        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
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
     * 申请存储卡写权限
     */
    private void requestWrite_External_Storage() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, APPLY_WRITE_EXTERNAL_STORAGE_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (requestCode == ALBUM_CAMERA_REQUEST_CODE) {//从拍照界面返回
            if (resultCode == RESULT_CANCELED) {
                return;
            }
            if (data == null) {
                Logger.i("2018年1月26日 16:18:17-666");

                String filePath = Environment.getExternalStorageDirectory() + File.separator + ImageName;
                Logger.i("filePath:" + filePath);
                int i = BitmapUtil.readPictureDegree(filePath);//获取照片被旋转的角度
                File fileName = new File(filePath);
                try {
                    File file = compressImage(fileName);
                    if (file != null) {
                        FileInputStream fileInputStream = new FileInputStream(file);
                        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                        Bitmap bitmap1 = BitmapUtil.rotaingImageView(i, bitmap);//将照片旋转的角度还原过来，解决三星手机拍照自动旋转90度的问题
                        Uri uri = saveBitmap(bitmap1);
                        File file1 = new File(new URI(uri.toString()));
                        sendImage(file1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
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
            Logger.i("filePath:" + imagePath);
            sendImage(new File(imagePath));
            c.close();
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
                    .uploadFilePublic(photo, new Callback<UploadImageEnity>() {
                        @Override
                        public void onResponse(Call<UploadImageEnity> call, Response<UploadImageEnity> response) {
                            stopMyDialog();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                UploadImageEnity body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        switch (body.code) {
                                            case 1:
                                                showToast("上传成功");
                                                imgList.add(body.filename);
                                                imgList1.clear();
                                                imgList1.addAll(imgList);
                                                if (imgList.size() < 1) {
                                                    imgList1.add("add");
                                                }
                                                rvImg.setVisibility(View.VISIBLE);
                                                adapter.notifyDataSetChanged();
                                                okBtn.setBackgroundResource(R.drawable.bg_new_solid_red);
                                                okBtn.setClickable(true);
                                                break;
                                            default:
                                                break;
                                        }
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UploadImageEnity> call, Throwable t) {
                            stopMyDialog();
                            showToast("上传失败,重新上传");
                            okBtn.setBackgroundResource(R.drawable.bg_solid_ccc);
                            okBtn.setClickable(false);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
