package com.yilian.mall.suning.activity.shippingaddress;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.suning.activity.SnCommitOrderActivity;
import com.yilian.mall.suning.address.SnAddressDataSourceImpl;
import com.yilian.mall.suning.fragment.goodsdetail.SnGoodsInfoLeftFragment;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.jdaddressseletor.AddressSelector;
import com.yilian.mylibrary.widget.jdaddressseletor.BottomDialog;
import com.yilian.mylibrary.widget.jdaddressseletor.OnAddressSelectedListener;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDStreet;
import com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SnEditShippingAddressActivity extends BaseAppCompatActivity implements View.OnClickListener, OnAddressSelectedListener, AddressSelector.OnDialogCloseListener, AddressSelector.onSelectorAreaPositionListener {
    public static final int REQUEST_CODE_EDIT = 11;
    public static final String TAG_EDIT_STATUS = "EditType";
    public static final String TAG_OLD_SHIPPING_ADDRESS_INFO = "old_shipping_address_info";
    /**
     * 请求获取联系人权限请求码
     */
    public static final int REQUEST_CODE = 99;
    public static final int CHOOSE_CONTACT = 1;
    public static final int RESULT_CODE = 333;
    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private View viewLine;
    private LinearLayout llTitle;
    private AppCompatEditText etName;
    private AppCompatEditText etPhone;
    private AppCompatTextView tvProvinceCityCounty;
    private AppCompatEditText etDetailShippingAddress;
    private Switch switchDefault;
    private LinearLayoutCompat llShippingAddressInfo;
    private Button btnCommit;
    private TextView chooseContact;
    private SnEditShippingAddressActivity.EditType mEditType;
    private SnShippingAddressInfoEntity.DataBean editShippingAddressInfo;
    private String mProvinceId;
    private String mProvinceName;
    private String mCityId;
    private String mCityName;
    private String mCountyId;
    private String mCountyName;
    private String mDetailAddress;
    private BottomDialog dialog;
    private String mAddressId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sn_edit_shipping_address);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        viewLine = (View) findViewById(R.id.view_line);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        etName = (AppCompatEditText) findViewById(R.id.et_name);
        etPhone = (AppCompatEditText) findViewById(R.id.et_phone);
        tvProvinceCityCounty = (AppCompatTextView) findViewById(R.id.tv_province_city_county);
        etDetailShippingAddress = (AppCompatEditText) findViewById(R.id.et_detail_shipping_address);
        switchDefault = (Switch) findViewById(R.id.switch_default);
        llShippingAddressInfo = (LinearLayoutCompat) findViewById(R.id.ll_shipping_address_info);
        btnCommit = (Button) findViewById(R.id.btn_commit);
        chooseContact = (TextView) findViewById(R.id.choose_contact);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnCommit.setOnClickListener(this);
    }

    private void initData() {
        mEditType = (EditType) getIntent().getSerializableExtra(TAG_EDIT_STATUS);
        switch (mEditType) {
            case NEW:
                v3Title.setText("新增地址");
                break;
            case EDIT:
                v3Title.setText("编辑地址");
                tvRight2.setVisibility(View.VISIBLE);
                tvRight2.setText("删除");
//                要编辑的地址信息
                editShippingAddressInfo
                        = (SnShippingAddressInfoEntity.DataBean) getIntent().getSerializableExtra(TAG_OLD_SHIPPING_ADDRESS_INFO);
                mAddressId = editShippingAddressInfo.id;
                etName.setText(TextUtils.isEmpty(editShippingAddressInfo.name) ? "" : editShippingAddressInfo.name);
//                mName = editShippingAddressInfo.name;
                etPhone.setText(TextUtils.isEmpty(editShippingAddressInfo.mobile) ? "" : editShippingAddressInfo.mobile);
                tvProvinceCityCounty.setText(editShippingAddressInfo.province + editShippingAddressInfo.city + editShippingAddressInfo.county);
                mProvinceName = editShippingAddressInfo.province;
                mProvinceId = editShippingAddressInfo.provinceId;
                mCityName = editShippingAddressInfo.city;
                mCityId = editShippingAddressInfo.cityId;
                mCountyName = editShippingAddressInfo.county;
                mCountyId = editShippingAddressInfo.countyId;
                etDetailShippingAddress.setText(TextUtils.isEmpty(editShippingAddressInfo.address) ? "" : editShippingAddressInfo.address);
                mDetailAddress = editShippingAddressInfo.address;
                if (editShippingAddressInfo.defaultAddress == SnShippingAddressInfoEntity.DataBean.DEFAULT_ADDRESS) {
                    switchDefault.setChecked(true);
                }

                break;
            default:
                break;
        }
    }

    private void initListener() {
        RxUtil.clicks(tvRight2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showSystemV7Dialog(null, "您确定要删除该地址？", "确定", "取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        deleteAddress();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        });
            }
        });
        RxUtil.clicks(tvProvinceCityCounty, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                dialog = new BottomDialog(mContext, new SnAddressDataSourceImpl());
                dialog.setSelectorTitle("选择地址");
                dialog.setOnAddressSelectedListener(SnEditShippingAddressActivity.this);
                dialog.setDialogDismisListener(SnEditShippingAddressActivity.this);
                dialog.setTextSize(14);//设置字体的大小
                dialog.setIndicatorBackgroundColor(R.color.color_FFAA00);//设置指示器的颜色
                dialog.setCheckedItemBackgroundResource(R.drawable.library_module_sn_selector_text_color_tab);
                dialog.setTextSelectedColor(R.color.color_FFAA00);//设置字体获得焦点的颜色
                dialog.setTextUnSelectedColor(R.color.color_333);//设置字体没有获得焦点的颜色
                dialog.setSelectorAreaPositionListener(SnEditShippingAddressActivity.this);
                dialog.show();
            }
        });
        RxUtil.clicks(chooseContact, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                selectContacts();
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void deleteAddress() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .deleteSnShippingAddress("suning_address/suning_delete_address", mAddressId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(HttpResultBean o) {
                        SnShippingAddressInfoEntity.DataBean snEditShippingAddressEvent = new SnShippingAddressInfoEntity.DataBean();
                        snEditShippingAddressEvent.id = editShippingAddressInfo.id;
                        /**
                         * {@link SnGoodsInfoLeftFragment#shippingAddressChanged(com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity.DataBean)}
                         * {@link SnCommitOrderActivity#shippingAddressChanged(com.yilian.networkingmodule.entity.suning.SnShippingAddressInfoEntity.DataBean)}
                         */
                        EventBus.getDefault().post(snEditShippingAddressEvent);
                        setResult(RESULT_CODE);
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 选择联系人
     */
    public void selectContacts() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            startContactActivity();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }

    }

    /**
     * 打开联系人界面
     */
    private void startContactActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, CHOOSE_CONTACT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHOOSE_CONTACT:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    namePhone(cursor);
                }
                break;
            default:
                break;
        }
    }

    public void namePhone(Cursor cursor) {
        cursor.moveToFirst();
        int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
        int phoneNum = 0;
        try {//部分手机在都区联系人权限为询问状态时，不会提示开启权限，参考魅族M3，会直接报异常，所以在此处添加tryCatch处理
            phoneNum = cursor.getInt(phoneColumn);
        } catch (Exception e) {
            showToast("请开启读取联系人权限!");
            e.printStackTrace();
            return;
        }
        if (phoneNum > 0) {
            // 获得联系人的ID号
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            String contactId = cursor.getString(idColumn);
            // 获得联系人电话的cursor
            Cursor phone = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
            if (phone.moveToFirst()) {
                for (; !phone.isAfterLast(); phone.moveToNext()) {
                    int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                    String phoneName = phone.getString(typeindex);
                    String string = phone.getString(index);
                    String phoneNumber;
                    if (string.contains(" ")) {
                        phoneNumber = string.replace(" ", "");//处理部分机型拿到的号码是由空格分隔的情况
                    } else {
                        phoneNumber = string;
                    }
//                    mName = phoneName;
//                    mPhone = phoneNumber;
                    etName.setText(phoneName);
                    etPhone.setText(phoneNumber);
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startContactActivity();
                    } else {
                        showToast("获取联系人被拒绝");
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onAddressSelected(JDProvince province, JDCity city, JDCounty county, JDStreet street) {
        mProvinceName = province.name;
        mProvinceId = province.id;
        mCityName = city.name;
        mCityId = city.id;
        mCountyName = county.name;
        mCountyId = county.id;
        tvProvinceCityCounty.setText(String.format("%s%s%s", province.name, city.name, county.name));
        dialogclose();
    }

    @Override
    public void dialogclose() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                finish();
                break;
            case R.id.btn_commit:
                submit();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchekced")
    private void submit() {
        // validate
        String name = etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            showToast("姓名不能为空");
            return;
        }

        String phone = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            showToast("电话不能为空");
            return;
        }


        String address = etDetailShippingAddress.getText().toString().trim();
        if (TextUtils.isEmpty(address)) {
            showToast("请填写详细地址");
            return;
        }

        // TODO validate success, do something
        HashMap<String, String> params = new HashMap<>();
        params.put("c", "suning_address/suning_update_address");
//        0 添加 1更新
        params.put("type", mEditType == EditType.EDIT ? "1" : "0");
//                添加地址是传0 否则传地址id
        params.put("address_id", mEditType == EditType.EDIT ? mAddressId : "0");
        params.put("provinceId", mProvinceId);
        params.put("province", mProvinceName);
        params.put("cityId", mCityId);
        params.put("city", mCityName);
        params.put("countyId", mCountyId);
        params.put("county", mCountyName);
        params.put("name", name);
        params.put("mobile", phone);
        params.put("address", getDetailAddress());
//                是否默认 1默认 0不默认
        params.put("default_address", switchDefault.isChecked() ? "1" : "0");
        addAddress(params);

    }

    private String getDetailAddress() {
        mDetailAddress = etDetailShippingAddress.getText().toString().trim();
        return mDetailAddress;
    }

    @SuppressWarnings("unchecked")
    private void addAddress(HashMap<String, String> params) {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .updateSnShippingAddress(params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
                    public void onNext(HttpResultBean o) {
                        setResult(RESULT_CODE);
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    public enum EditType {
        /**
         * 编辑地址
         */
        EDIT,
        /**
         * 新建地址
         */
        NEW
    }


}
