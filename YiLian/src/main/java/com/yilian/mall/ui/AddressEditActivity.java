package com.yilian.mall.ui;

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
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.AreaInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.MyPoiItem;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.yilian.mall.widgets.ChangeAddressDialog;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.entity.UserAddressLists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 改版编辑收货地址界面
 * <p>
 * Created by Administrator on 2016/1/19.
 * province_id ，province_name ，city_id ， city_name， county_id ，county_name
 */
public class AddressEditActivity extends BaseActivity {

    public static final int REQUEST_CODE = 99;//请求获取联系人权限请求码
    public static final int LOCATION_PERMISSION = 999;//请求定位权限请求码
    public static final int CHOOSE_ADDRESS = 100;//跳转地址选择界面请求码
    public static final int CHOOSE_CONTACT = 1;
    ChangeAddressDialog mChangeAddressDialog;
    @ViewInject(R.id.tv_back)
    private TextView tv_back;
    @ViewInject(R.id.right_textview)
    private TextView right_textview;
    @ViewInject(R.id.name_edit)
    private TextView name_edit;
    @ViewInject(R.id.phone_edit)
    private TextView phone_edit;
    @ViewInject(R.id.diqu_edit)
    private TextView diqu_edit;
    @ViewInject(R.id.address_edit)
    private TextView address_edit;
    @ViewInject(R.id.address_default_checkbox)
    private CheckBox address_default_checkbox;
    @ViewInject(R.id.choose_address)
    private TextView chooseAddress;
    @ViewInject(R.id.ll_area)
    private LinearLayout llArea;

    private int default_address;//0代表不是默认，1代表设置为默认地址
    private String address_id;
    //        private String province_id;
//    private String province_name;
//    private String city_id;
//    private String city_name;
//    private String county_id;
//    private String county_name;
    private MallNetRequest mallNetRequest;
    private List<AreaInfo> mArrProvinces = new ArrayList<AreaInfo>();
    private Map<String, List<AreaInfo>> mCitisDatasMap = new HashMap<String, List<AreaInfo>>();
    private Map<String, List<AreaInfo>> mAreaDatasMap = new HashMap<String, List<AreaInfo>>();
    private AreaInfo[] areaInfos;
    private double longitude;
    private double latitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_edit);
        ViewUtils.inject(this);
        tv_back.setText("编辑收货地址");
        mallNetRequest = new MallNetRequest(mContext);

        initData();
    }

    public void initData() {
        if ("EDIT_ADDRESS".equals(this.getIntent().getStringExtra("flag"))) {   //如果是编辑地址
            longitude = NumberFormat.convertToDouble(getIntent().getStringExtra("longitude"), 0d);
            latitude = NumberFormat.convertToDouble(getIntent().getStringExtra("latitude"), 0d);
            llArea.setVisibility(View.VISIBLE);
            name_edit.setText(this.getIntent().getStringExtra("name"));
            phone_edit.setText(this.getIntent().getStringExtra("phone"));
            InputFilter[] phoneFilter = {new InputFilter.LengthFilter(11)};
            phone_edit.setFilters(phoneFilter);
            diqu_edit.setText(this.getIntent().getStringExtra("diqu"));
            address_edit.setText(this.getIntent().getStringExtra("address"));
            chooseAddress.setText(this.getIntent().getStringExtra("fullAddress"));
            default_address = this.getIntent().getIntExtra("default_address", 0);
            if (default_address == 1) {
                address_default_checkbox.setVisibility(View.GONE);
            } else {
                address_default_checkbox.setVisibility(View.VISIBLE);
            }
            address_id = this.getIntent().getStringExtra("address_id");
//            province_id = this.getIntent().getStringExtra("province_id");
//            province_name = this.getIntent().getStringExtra("province_name");
//            city_id = this.getIntent().getStringExtra("city_id");
//            city_name = this.getIntent().getStringExtra("city_name");
//            county_id = this.getIntent().getStringExtra("county_id");
//            county_name = this.getIntent().getStringExtra("county_name");

            if ("UserInfo".equals(this.getIntent().getStringExtra("FLAG"))) { //标记从我的收获地址进入
                right_textview.setText("删除");
            } else {
                right_textview.setVisibility(View.GONE);
            }

        } else {//新建地址

            address_id = "0";//新建地址的address_id
        }

        address_default_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    default_address = 1;

//                    mallNetRequest.DefaultUserAddress(address_id, BaseEntity.class, new RequestCallBack<BaseEntity>() {
//                        @Override
//                        public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
//                            switch (responseInfo.result.code) {
//                                case 1:
//
//                                    break;
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(HttpException e, String s) {
//                            showToast(R.string.net_work_not_available);
//                        }
//                    });
                } else {
                    default_address = 0;
                }
            }
        });
    }

    /**
     * 选择联系人
     *
     * @param view
     */
    public void selectContacts(View view) {
        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            startContactActivity();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, REQUEST_CODE);
        }

    }

    /**
     * 打开联系人界面
     */
    private void startContactActivity() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        AddressEditActivity.this.startActivityForResult(intent, CHOOSE_CONTACT);
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
            case LOCATION_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        startActivityForResult(new Intent(this, MTChooseAddressActivity.class), CHOOSE_ADDRESS);
                    } else {
                        showToast("定位被拒绝");
                    }
                }
                break;
        }
    }

    /**
     * 选择地区
     *
     * @param view
     */
    public void selectAddress(View view) {
//地区由用户选择地址后自动返显，不再让用户能够手动选择
//        Intent intent = new Intent(AddressEditActivity.this, CitySelectionActivity.class);
//        startActivityForResult(intent, 0);

    }

    /**
     * 选择小区/大厦/学校 等标志性建筑物
     *
     * @param view
     */
    public void chooseAddress(View view) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            startActivityForResult(new Intent(this, MTChooseAddressActivity.class), CHOOSE_ADDRESS);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION);
        }

    }
void test(int temp){

    if (temp>3) {

    }
}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
//            case 0:
//                if (resultCode == RESULT_OK) {
//                    String[] cityInfo = data.getExtras().getString("cityInfo").split(",");
//                    Logger.i(data.getExtras().getString("cityInfo"));
//                    province_name = cityInfo[0];
//                    city_name = cityInfo[2];
//                    county_name = cityInfo[4];
//
//                    province_id = cityInfo[1];
//                    city_id = cityInfo[3];
//                    county_id = cityInfo[5];
//
//                    String diquStr = province_name + city_name + county_name;
//                    diqu_edit.setText(diquStr);
//                }
//                break;

            case CHOOSE_CONTACT:
                if (resultCode == RESULT_OK) {
                    Uri contactData = data.getData();
                    Cursor cursor = managedQuery(contactData, null, null, null, null);
                    namePhone(cursor);

                }
                break;
            case CHOOSE_ADDRESS:
                if (resultCode == RESULT_OK) {//选择地址界面回来后的处理
                    llArea.setVisibility(View.VISIBLE);
                    PoiItem poiItem = data.getParcelableExtra("PoiItem");
                    chooseAddress.setText(poiItem.getTitle());
                    diqu_edit.setText(poiItem.getProvinceName() + poiItem.getCityName() + poiItem.getAdName());
                    LatLonPoint latLonPoint = poiItem.getLatLonPoint();
                    longitude = latLonPoint.getLongitude();
                    latitude = latLonPoint.getLatitude();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //显示手动查询出的地址信息
        SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(mContext, "SearchAddress");
        MyPoiItem searchAddress = sharedPreferencesUtils.getObject("SearchAddress", MyPoiItem.class);
        if (searchAddress != null) {
            llArea.setVisibility(View.VISIBLE);
            longitude = searchAddress.Longitude;
            latitude = searchAddress.Latitude;
            chooseAddress.setText(searchAddress.title);
            diqu_edit.setText(searchAddress.provinceName + searchAddress.cityName + searchAddress.adName);
            sharedPreferencesUtils.setObject("SearchAddress", null);
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
        String result = "";
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

                    name_edit.setText(phoneName);
                    phone_edit.setText(phoneNumber);
                }
                if (!phone.isClosed()) {
                    phone.close();
                }
            }
        }
    }


    private void initAddrLists() {
        AreaInfo[] areas = areaInfos;
        for (AreaInfo area : areas) {
            if ("1".equals(area.getRegion_type())) {
                mArrProvinces.add(area);
                List<AreaInfo> cities = new ArrayList<>();
                for (int i = 0; i < areas.length; ++i) {
                    if (area.getRegion_id().equals(areas[i].getParent_id())) {
                        cities.add(areas[i]);
                        List<AreaInfo> districts = new ArrayList<>();
                        for (int j = 0; j < areas.length; ++j) {
                            if (areas[i].getRegion_id().equals(areas[j].getParent_id())) {
                                districts.add(areas[j]);
                            }
                        }
                        if (districts.size() > 0) {
                            districts.remove(0);
                        }
                        mAreaDatasMap.put(areas[i].getRegion_id(), districts);
                    }
                }
                mCitisDatasMap.put(area.getRegion_id(), cities);
            }

        }
    }

    /**
     * 保存
     *
     * @param view
     */
    public void saveAddress(View view) {
        if (TextUtils.isEmpty(name_edit.getText())) {
            showToast("请填写收货人姓名");
            return;
        }
        if (TextUtils.isEmpty(phone_edit.getText())) {
            showToast("请填写收货人手机号");
            return;
        }
        if (!CommonUtils.isPhoneNumer(phone_edit.getText().toString())) {
            showToast("您输入的手机号码不符合规则");
            return;
        }
        if (TextUtils.isEmpty(diqu_edit.getText())) {
            showToast("请选择收货地区");
            return;
        }
        if (TextUtils.isEmpty(address_edit.getText())) {
            showToast("请填写收货地址");
            return;
        }

        UserAddressLists userAddressLists = new UserAddressLists();
        userAddressLists.setAddress_id(address_id);
        userAddressLists.setAddress(address_edit.getText().toString());
        userAddressLists.setDefault_address(default_address);
        userAddressLists.setContacts(name_edit.getText().toString());
        userAddressLists.setPhone(phone_edit.getText().toString());

//        userAddressLists.setProvince_id(province_id);
//        userAddressLists.setProvince_name(province_name);
//        userAddressLists.setCity_id(city_id);
//        userAddressLists.setCity_name(city_name);
//        userAddressLists.setCounty_id(county_id);
//        userAddressLists.setCounty_name(county_name);
        userAddressLists.fullAddress = chooseAddress.getText().toString().trim();
        userAddressLists.latitude = String.valueOf(latitude);
        userAddressLists.longitude = String.valueOf(longitude);
        mallNetRequest.editeMTUserAddress(userAddressLists, BaseEntity.class, new RequestCallBack<BaseEntity>() {

            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code + "")) {
                    showToast("保存成功");
                    if ("ActivityDetail".equals(getIntent().getStringExtra("flag"))) {
                        Intent intent = new Intent(AddressEditActivity.this, AddressManageActivity.class);
                        intent.putExtra("FLAG", "ActivityDetail");
                        intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 删除按钮
     *
     * @param view
     */
    public void rightTextview(View view) {
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

                    mallNetRequest.deleteUserAddress(address_id, BaseEntity.class, new RequestCallBack<BaseEntity>() {
                        @Override
                        public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                            switch (responseInfo.result.code) {
                                case 1:
                                    showToast("删除成功");
                                    dialog.cancel();
                                    finish();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            showToast(R.string.net_work_not_available);
                        }
                    });
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
}
