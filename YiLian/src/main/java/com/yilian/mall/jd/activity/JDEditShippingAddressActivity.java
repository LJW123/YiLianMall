package com.yilian.mall.jd.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.AreaInfo;
import com.yilian.mall.jd.JdAddressDataSourceImpl;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.jdaddressseletor.AddressSelector;
import com.yilian.mylibrary.widget.jdaddressseletor.BottomDialog;
import com.yilian.mylibrary.widget.jdaddressseletor.OnAddressSelectedListener;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDStreet;
import com.yilian.networkingmodule.entity.jd.JDShippingAddressInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 改版编辑收货地址界面
 * <p>
 *
 * @author Administrator on 2016/1/19.
 *         province_id ，province_name ，city_id ， city_name， county_id ，county_name
 */
public class JDEditShippingAddressActivity extends BaseAppCompatActivity implements AddressSelector.onSelectorAreaPositionListener, OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {
    public static final String TAG = "JDEditAddressActivity";
    /**
     * 请求获取联系人权限请求码
     */
    public static final int REQUEST_CODE = 99;
    public static final int CHOOSE_CONTACT = 1;
    /**
     * 新增地址requestCode
     */
    public static final int ADD_ADDRESS_REQUEST_CODE = 666;
    /**
     * 新增地址完成后的resultCode
     */
    public static final int ADD_ADDRESS_RESULT_CODE = 777;
    /**
     * 编辑原有地址
     */
    public static final int CHANGE_ADDRESS_EDIT = 1;
    /**
     * 添加地址
     */
    public static final int CHANGE_ADDRESS_ADD = 0;
    /**
     * 地址编辑类型 0 添加 / 1更新 默认添加
     */
    public int mEditType = 0;
    private String provinceName;
    private String provinceId;
    private String cityName;
    private String cityId;
    private String countyName;
    private String countyId;
    /**
     * 没有该值则向服务器传空字符串,所以默认空字符串
     */
    private String townName = "";
    /**
     * 没有该值则向服务器传0,所以默认为0
     */
    private String townId = "0";
    @ViewInject(R.id.name_edit)
    private TextView tvName;
    @ViewInject(R.id.phone_edit)
    private EditText etPhone;
    @ViewInject(R.id.address_edit)
    private TextView etEditAddress;
    @ViewInject(R.id.address_default_checkbox)
    private CheckBox address_default_checkbox;
    @ViewInject(R.id.choose_address)
    private TextView chooseAddress;
    @ViewInject(R.id.v3Title)
    private TextView v3Title;
    @ViewInject(R.id.v3Back)
    private ImageView v3Back;
    @ViewInject(R.id.tv_right)
    private TextView right_textview;
    @ViewInject(R.id.iv_clear_detail_address)
    private ImageView clearDetailAddress;
    private int default_address;//0代表不是默认，1代表设置为默认地址
    private String address_id;
    private List<AreaInfo> mArrProvinces = new ArrayList<AreaInfo>();
    private Map<String, List<AreaInfo>> mCitisDatasMap = new HashMap<String, List<AreaInfo>>();
    private Map<String, List<AreaInfo>> mAreaDatasMap = new HashMap<String, List<AreaInfo>>();
    private AreaInfo[] areaInfos;
    private BottomDialog dialog;
    private JDShippingAddressInfoEntity.DataBean editAddress;
    /**
     * 编辑地址ID,若是新增地址 则该字段为"0"  默认为"0"
     */
    private String addressId = "0";
    /**
     * 是否是默认地址 编辑地址则按原地址默认类型赋值 新增地址则为非默认地址(0)
     */
    private int isDefaultAddress;
    /**
     * 完整电话号码  用于保存地址时传值 由于编辑旧地址时会将号码中间四位隐藏处理 所以保存地址信息传递手机号不能从editText直接获取
     */
    private String fullPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jd_edit_address);
        ViewUtils.inject(this);
        v3Title.setText("编辑收货地址");
        initData();
        initListener();
    }

    public void initData() {
        editAddress = (JDShippingAddressInfoEntity.DataBean) getIntent().getSerializableExtra(TAG);
        if (editAddress != null) {
            mEditType = CHANGE_ADDRESS_EDIT;
            addressId = editAddress.id;
            isDefaultAddress = editAddress.defaultAddress;
            tvName.setText(editAddress.name);
            etPhone.setText(PhoneUtil.formatPhoneMiddle4Asterisk(editAddress.mobile));
            fullPhone = editAddress.mobile;
            StringBuilder addressBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(editAddress.province)) {
                provinceName = editAddress.province;
                provinceId = editAddress.provinceId;
                addressBuilder.append(editAddress.province);
            }
            if (!TextUtils.isEmpty(editAddress.city)) {
                cityName = editAddress.city;
                cityId = editAddress.cityId;
                addressBuilder.append(editAddress.city);
            }
            if (!TextUtils.isEmpty(editAddress.county)) {
                countyName = editAddress.county;
                countyId = editAddress.countyId;
                addressBuilder.append(editAddress.county);
            }
            if (!TextUtils.isEmpty(editAddress.town)) {
                townName = editAddress.town;
                townId = editAddress.townId;
                addressBuilder.append(editAddress.town);
            }
            chooseAddress.setText(addressBuilder);
            etEditAddress.setText(editAddress.detailAddress);

        }
    }

    private void initListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (CommonUtils.isPhoneNumer(etPhone.getText().toString().trim())) {
                    fullPhone = etPhone.getText().toString().trim();
                }
            }
        });
        RxUtil.clicks(clearDetailAddress, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                etEditAddress.setText("");
            }
        });
        etEditAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 0) {
                    clearDetailAddress.setVisibility(View.INVISIBLE);
                } else {
                    clearDetailAddress.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        RxUtil.clicks(right_textview, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {

                try {
                    final Dialog dialog = new Dialog(mContext, R.style.Dialog);
                    View dialogView = View.inflate(mContext, R.layout.dialog_address_delete, null);
                    dialog.setContentView(dialogView);
                    Button delete_btn = (Button) dialogView.findViewById(R.id.delete_btn);
                    Button delete_cancle_btn = (Button) dialogView.findViewById(R.id.delete_cancle_btn);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.BOTTOM);
                    dialog.show();
                    delete_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });

                    delete_cancle_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });
                } catch (NullPointerException e) {

                }
            }
        });
        RxUtil.clicks(v3Back, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                finish();
            }
        });
    }

    /**
     * 选择联系人
     *
     * @param view
     */
    public void selectContacts(View view) {
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
        JDEditShippingAddressActivity.this.startActivityForResult(intent, CHOOSE_CONTACT);
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

                    tvName.setText(phoneName);
                    etPhone.setText(phoneNumber);
                    fullPhone = phoneNumber;
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
    }

    /**
     * 选择省市县镇
     *
     * @param view
     */
    public void chooseAddress(View view) {
        dialog = new BottomDialog(this, new JdAddressDataSourceImpl());
        dialog.setOnAddressSelectedListener(this);
        dialog.setDialogDismisListener(this);
        dialog.setTextSize(14);//设置字体的大小
        dialog.setIndicatorBackgroundColor(R.color.color_red);//设置指示器的颜色
        dialog.setTextSelectedColor(R.color.color_red);//设置字体获得焦点的颜色
//        dialog.setTextUnSelectedColor(android.R.color.holo_blue_light);//设置字体没有获得焦点的颜色
//            dialog.setDisplaySelectorArea("31",1,"2704",1,"2711",0,"15582",1);//设置已选中的地区
        dialog.setSelectorAreaPositionListener(this);
        dialog.show();
    }

    /**
     * 保存
     *
     * @param view
     */
    @SuppressWarnings("unchecked")
    public void saveAddress(View view) {
        if (TextUtils.isEmpty(getShippingName())) {
            showToast("请填写收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(getShippingPhone())) {
            showToast("请填写收货人手机号");
            return;
        }
        if (!CommonUtils.isPhoneNumer(getShippingPhone())) {
            showToast("您输入的手机号码不符合规则");
            return;
        }
        if (TextUtils.isEmpty(getShippingArea())) {
            showToast("请选择收货地区");
            return;
        }
        if (TextUtils.isEmpty(getShippingAddress())) {
            showToast("请填写详细地址");
            return;
        }
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .updateJDAddress("jd_address/jd_update_address",
                        mEditType, addressId, provinceId, provinceName, cityId, cityName, countyId, countyName, townId, townName,
                        getShippingName(), getShippingPhone(), getShippingAddress(), isDefaultAddress)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();
                    }

                    @Override
                    public void onNext(HttpResultBean httpResultBean) {
                        setResult(ADD_ADDRESS_RESULT_CODE);
                        postEvent();
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 获取收货人姓名
     *
     * @return
     */
    @NonNull
    private String getShippingName() {
        return tvName.getText().toString().trim();
    }

    /**
     * 获取收货人手机号
     *
     * @return
     */
    @NonNull
    private String getShippingPhone() {
        return fullPhone;
    }

    /**
     * 获取收货省市县
     *
     * @return
     */
    @NonNull
    private String getShippingArea() {
        return chooseAddress.getText().toString().trim();
    }

    /**
     * 获取收货详细地址
     *
     * @return
     */
    @NonNull
    private String getShippingAddress() {
        return etEditAddress.getText().toString().trim();
    }
    /**
     * 更新商品详情中的地址并且检查是否在销售范围
     * {@link com.yilian.mall.jd.fragment.goodsdetail.JDGoodsInfoLeftFragment#showShoppingAddress(JDShippingAddressInfoEntity entity)}
     */
    private void postEvent() {
        if (mEditType == CHANGE_ADDRESS_EDIT) {
            JDShippingAddressInfoEntity entity = new JDShippingAddressInfoEntity();
            JDShippingAddressInfoEntity.DataBean shippingAddressInfo = new JDShippingAddressInfoEntity.DataBean();
            shippingAddressInfo.cityId = cityId;
            shippingAddressInfo.countyId = countyId;
            shippingAddressInfo.provinceId = provinceId;
            shippingAddressInfo.townId = townId;
            shippingAddressInfo.province = provinceName;
            shippingAddressInfo.city = cityName;
            shippingAddressInfo.county = countyName;
            shippingAddressInfo.town = townName;
            shippingAddressInfo.detailAddress = getShippingAddress();
            entity.data = shippingAddressInfo;
            EventBus.getDefault().post(entity);
        }
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
        Logger.i("选取的地址:province: " + provincePosition + "  city:" + cityPosition + "  county:" + countyPosition + "  street:" + streetPosition);
    }

    @Override
    public void onAddressSelected(JDProvince province, JDCity city, JDCounty county, JDStreet street) {
        StringBuilder addressBuild = new StringBuilder();
        if (province != null) {
            if (!TextUtils.isEmpty(province.name)) {
                addressBuild.append(province.name);
                provinceName = province.name;
                provinceId = province.id;
            }
        }
        if (city != null) {
            if (!TextUtils.isEmpty(city.name)) {
                addressBuild.append(city.name);
                cityName = city.name;
                cityId = city.id;
            }
        }
        if (county != null) {
            if (!TextUtils.isEmpty(county.name)) {
                addressBuild.append(county.name);
                countyName = county.name;
                countyId = county.id;
            }
        }
        if (street != null) {
            if (!TextUtils.isEmpty(street.name)) {
                addressBuild.append(street.name);
                townName = street.name;
                townId = street.id;
            }
        } else {
            //当选则的是 三级地址时 四级置空
            townName = "";
            townId = "0";
        }
        chooseAddress.setText(addressBuild.toString());
        dialogclose();
    }

    @Override
    public void dialogclose() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }
}
