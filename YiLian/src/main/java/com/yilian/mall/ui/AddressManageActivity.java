package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.ActivitySubmitGoods;
import com.yilian.mall.entity.UserAddressListEntity;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.MerchantAddressListEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 管理收货地址界面
 * Created by Administrator on 2016/1/19.
 */
public class AddressManageActivity extends BaseActivity {
    public static final String TAG_FROM_WEB_VIEW_ACTIVITY="tag_from_web_view_activity";
    public static final String TAG="FLAG";
    List<UserAddressLists> userAddressLists;
    ArrayList<MerchantAddressListEntity.ListBean> merchantList;
    private TextView tv_back;
    private TextView no_address;
    private TextView tvAddNew;
    private NoScrollListView address_listview;
    private MallNetRequest mallNetRequest;
    private String flagStr;//标记从我的收货地址进入

    private String selectedAddressId;

    private String activityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manage);

        tv_back = (TextView) findViewById(R.id.tv_back);
        no_address = (TextView) findViewById(R.id.no_address);
        tvAddNew = findViewById(R.id.tv_add_new);
        address_listview = (NoScrollListView) findViewById(R.id.address_listview);

        selectedAddressId = this.getIntent().getStringExtra(Constants.ADDRESS_ID_SELECTED);

        flagStr = this.getIntent().getStringExtra("FLAG");

        activityId = this.getIntent().getStringExtra("activityId");

        if (flagStr.equals("UserInfo")) {
            address_listview.setEnabled(false);//如果是从我的地址进入 则listview 条目不可点击
        } else {
            address_listview.setEnabled(true);
        }

        if ("merchantInfo".equals(flagStr)) {
            tv_back.setText("管理发货地址");
            tvAddNew.setText("使用新的发货地址");
        } else {
            tv_back.setText("管理收货地址");
            tvAddNew.setText("使用新的收货地址");
        }
        mallNetRequest = new MallNetRequest(mContext);

        address_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if("fromMT".equals(flagStr)){  //如果是从套餐订单界面跳到地址选择界面,如果 该地址没有经纬度，则不能使用，需要用户重新编辑该地址
                    UserAddressLists itemAtPosition = (UserAddressLists) parent.getItemAtPosition(position);
                    if("0".equals(itemAtPosition.isNew)){
                        showToast("该地址需要重新编辑");
                        return;
                    }
                }
                if ("ActivityDetail".equals(flagStr)) {
                    ActivitySubmitGoods goods = (ActivitySubmitGoods) PreferenceUtils.readObjectConfig(Constants.ACTIVITY_DETAIL_ENTITY, mContext);
                    UserAddressLists itemAtPosition = (UserAddressLists) parent.getItemAtPosition(position);
                    goods.userName = itemAtPosition.contacts;
                    goods.userPhone = itemAtPosition.phone;
                    goods.location = itemAtPosition.province_name + itemAtPosition.city_name + itemAtPosition.county_name + itemAtPosition.fullAddress + itemAtPosition.address;
                    goods.addressId = itemAtPosition.address_id;

                    Logger.i("2017年11月14日 09:14:31-" + goods.toString());
                    Intent intent = new Intent(AddressManageActivity.this, JPCommitOrderActivity.class);
                    intent.putExtra("OrderType", "ActivityDetail");
                    intent.putExtra("goods", goods);
                    startActivity(intent);
                } else if(TAG_FROM_WEB_VIEW_ACTIVITY.equals(flagStr)){
                    UserAddressLists itemAtPosition = (UserAddressLists) parent.getItemAtPosition(position);
//                    此处将地址ID存储下来，供webViewActivity调用
                    PreferenceUtils.writeStrConfig(Constants.APP_CALLBACK_PARAMETER, itemAtPosition.address_id, mContext);
                }else {
                    Intent intent = new Intent();
                    intent.putExtra("USE_RADDRESS_LIST", userAddressLists.get(position));
                    intent.putExtra("activityId", activityId);
                    Logger.e("USE_RADDRESS_LIST"+  userAddressLists.get(position).address_id+"\n"+userAddressLists.get(position).address);
                    setResult(RESULT_OK, intent);
                }
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressList();
    }

    private void getAddressList() {
        startMyDialog();
        mallNetRequest.userAddressList(UserAddressListEntity.class, new RequestCallBack<UserAddressListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<UserAddressListEntity> responseInfo) {
                stopMyDialog();
                if (CommonUtils.serivceReturnCode(mContext,responseInfo.result.code,responseInfo.result.msg)){
                    switch (responseInfo.result.code) {
                        case 1:

                            if (responseInfo.result.list.size() != 0) {
                                address_listview.setVisibility(View.VISIBLE);
                                no_address.setVisibility(View.VISIBLE);
                                if ("merchantInfo".equals(flagStr)) {
                                    no_address.setText("您使用过的发货地址！");
                                } else {
                                    no_address.setText("您使用过的收货地址！");
                                }
                                userAddressLists = responseInfo.result.list;
                                for (UserAddressLists address : responseInfo.result.list) {
                                    if (address.default_address == 1) {
                                        responseInfo.result.list.remove(address);
                                        responseInfo.result.list.add(0, address);
                                        break;
                                    }
                                }
                                address_listview.setAdapter(new AddressListAdapter(mContext, responseInfo.result.list));
                            } else {
                                address_listview.setVisibility(View.GONE);
                                no_address.setVisibility(View.VISIBLE);
                                if ("merchantInfo".equals(flagStr)) {
                                    no_address.setText("您还没有设置您的发货地址哦!");
                                } else {
                                    no_address.setText("您还没有设置您的收货地址哦!");
                                }
                            }
                            break;
                        default:
                            break;
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


    public void newAddress(View view) {
        Intent intent = new Intent(mContext, AddressEditActivity.class);
        intent.putExtra("flag", "NEW_ADDRESS");
        startActivity(intent);
    }

    class AddressListAdapter extends BaseAdapter {
        private Context context;
        private List<UserAddressLists> userAddressListses;

        public AddressListAdapter(Context context, List<UserAddressLists> userAddressListses) {
            this.context = context;
            this.userAddressListses = userAddressListses;
        }

        @Override
        public int getCount() {
            if (userAddressListses != null) {
                return userAddressListses.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return userAddressListses.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_address_manage, null);
                holder.item_address_list = (LinearLayout) convertView.findViewById(R.id.item_address_list);
                holder.address_duigou_icon_iv = (ImageView) convertView.findViewById(R.id.address_duigou_icon_iv);
                holder.name_texview = (TextView) convertView.findViewById(R.id.name_texview);
                holder.phone_texview = (TextView) convertView.findViewById(R.id.phone_texview);
                holder.addres_texview = (TextView) convertView.findViewById(R.id.addres_texview);
                holder.edit_address_icon_iv = (ImageView) convertView.findViewById(R.id.edit_address_icon_iv);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (userAddressListses != null) {
                final UserAddressLists userAddressLists = userAddressListses.get(position);

                if ("UserInfo".equals(flagStr) ||TAG_FROM_WEB_VIEW_ACTIVITY.equals(flagStr)) {  //如果是从我的地址进来
                    if (userAddressLists.default_address == 1) {//默认地址
                        holder.address_duigou_icon_iv.setVisibility(View.GONE);
                        holder.name_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                        holder.phone_texview.setTextColor(getResources().getColor(R.color.address_manage_item_phone_color));
                        holder.addres_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                        holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.white_color));
                        holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_blue_icon);
                        holder.addres_texview.setText("[默认] " + userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);
                    } else {
                        holder.address_duigou_icon_iv.setVisibility(View.GONE);
                        holder.name_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                        holder.phone_texview.setTextColor(getResources().getColor(R.color.address_manage_item_phone_color));
                        holder.addres_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                        holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.white_color));
                        holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_blue_icon);
                        holder.addres_texview.setText(userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);
                    }

                } else {   //从下订单选择地市进来

                    if (userAddressLists.default_address == 1) {  //默认地址
                        if (selectedAddressId.equals(userAddressLists.address_id)) {//选中
                            holder.address_duigou_icon_iv.setVisibility(View.VISIBLE);
                            holder.name_texview.setTextColor(getResources().getColor(R.color.white_color));
                            holder.phone_texview.setTextColor(getResources().getColor(R.color.white_color));
                            holder.addres_texview.setTextColor(getResources().getColor(R.color.white_color));
                            holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.color_red));
                            holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_white_icon);
                        } else {
                            holder.address_duigou_icon_iv.setVisibility(View.GONE);
                            holder.name_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                            holder.phone_texview.setTextColor(getResources().getColor(R.color.address_manage_item_phone_color));
                            holder.addres_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                            holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.white_color));
                            holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_blue_icon);
                        }
                        holder.addres_texview.setText("[默认] " + userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);
                    } else {
                        //幸运购过来的
                        if ("gotRecordId".equals(flagStr)) {
                            if (activityId.equals(userAddressLists.address_id)) {  //默认地址
                                holder.address_duigou_icon_iv.setVisibility(View.GONE);
                                holder.name_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                                holder.phone_texview.setTextColor(getResources().getColor(R.color.address_manage_item_phone_color));
                                holder.addres_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                                holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.white_color));
                                holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_blue_icon);
                                holder.addres_texview.setText("[默认] " + userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);
                            } else {
                                holder.address_duigou_icon_iv.setVisibility(View.GONE);
                                holder.name_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                                holder.phone_texview.setTextColor(getResources().getColor(R.color.address_manage_item_phone_color));
                                holder.addres_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                                holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.white_color));
                                holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_blue_icon);
                                holder.addres_texview.setText(userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);
                            }
                        } else {
                            if (selectedAddressId.equals(userAddressLists.address_id)) {
                                holder.address_duigou_icon_iv.setVisibility(View.VISIBLE);
                                holder.name_texview.setTextColor(getResources().getColor(R.color.white_color));
                                holder.phone_texview.setTextColor(getResources().getColor(R.color.white_color));
                                holder.addres_texview.setTextColor(getResources().getColor(R.color.white_color));
                                holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.color_red));
                                holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_white_icon);
                            } else {
                                holder.address_duigou_icon_iv.setVisibility(View.GONE);
                                holder.name_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                                holder.phone_texview.setTextColor(getResources().getColor(R.color.address_manage_item_phone_color));
                                holder.addres_texview.setTextColor(getResources().getColor(R.color.address_manage_item_name_color));
                                holder.item_address_list.setBackgroundColor(getResources().getColor(R.color.white_color));
                                holder.edit_address_icon_iv.setImageResource(R.mipmap.edit_address_blue_icon);
                            }
                            holder.addres_texview.setText(userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name + userAddressLists.fullAddress + userAddressLists.address);

                        }
                    }
                }
                holder.name_texview.setText(userAddressLists.contacts);
                holder.phone_texview.setText(userAddressLists.phone);
                holder.edit_address_icon_iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, AddressEditActivity.class);
                        intent.putExtra("flag", "EDIT_ADDRESS");
                        intent.putExtra("FLAG", flagStr);//标记从我的收获地址进入
                        intent.putExtra("name", userAddressLists.contacts);
                        intent.putExtra("phone", userAddressLists.phone);
                        String diqu = userAddressLists.province_name + userAddressLists.city_name + userAddressLists.county_name;
                        intent.putExtra("diqu", diqu);
                        intent.putExtra("address", userAddressLists.address);
                        intent.putExtra("address_id", userAddressLists.address_id);
                        intent.putExtra("default_address", userAddressLists.default_address);
                        intent.putExtra("province_id", userAddressLists.province_id);
                        intent.putExtra("province_name", userAddressLists.province_name);
                        intent.putExtra("city_id", userAddressLists.city_id);
                        intent.putExtra("city_name", userAddressLists.city_name);
                        intent.putExtra("county_id", userAddressLists.county_id);
                        intent.putExtra("county_name", userAddressLists.county_name);
                        intent.putExtra("fullAddress", userAddressLists.fullAddress);
                        intent.putExtra("latitude",userAddressLists.latitude);
                        intent.putExtra("longitude",userAddressLists.longitude);
                        startActivity(intent);
                    }
                });
            }

            return convertView;
        }

        class ViewHolder {
            private LinearLayout item_address_list;
            private ImageView address_duigou_icon_iv;
            private TextView name_texview;
            private TextView phone_texview;
            private TextView addres_texview;
            private ImageView edit_address_icon_iv;

        }
    }


}
