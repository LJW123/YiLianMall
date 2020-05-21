package com.yilianmall.merchant.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/9/30 0030.
 */

public class MerchantChooseDialogActivity extends BaseActivity {
    private LinearLayout lineLayout, yhsLayout, jfgLayout;
    private CheckBox lineCb, yhsCb, jfgCb;
    private TextView tv_btn_cancel;
    private TextView tv_btn_ok;

    private String goodsId, isYhs, isJfg, isNormal, authority;//1 奖券购 益划算都显示 2奖券购显示  3益划算显示

    /**
     * 选择区域的str集合
     */
    private ArrayList<String> chooseList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_dialog_choose_way);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        lineLayout = (LinearLayout) findViewById(R.id.layout_line);
        yhsLayout = (LinearLayout) findViewById(R.id.layout_yhs);
        jfgLayout = (LinearLayout) findViewById(R.id.layout_jfg);
        lineCb = (CheckBox) findViewById(R.id.cb_line);
        yhsCb = (CheckBox) findViewById(R.id.cb_yhs);
        jfgCb = (CheckBox) findViewById(R.id.cb_jfg);
        tv_btn_cancel = (TextView) findViewById(R.id.tv_btn_cancel);
        tv_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_btn_ok = (TextView) findViewById(R.id.tv_btn_ok);
        tv_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer chooseSb = new StringBuffer();
                for (int i = 0; i < chooseList.size(); i++) {
                    if (i < chooseList.size() - 1) {
                        chooseSb.append(chooseList.get(i) + ",");
                    } else {
                        chooseSb.append(chooseList.get(i));
                    }
                }
                Logger.i("ray-" + chooseList.toString());
                Logger.i("ray-" + String.valueOf(chooseSb));
                startMyDialog();
                RetrofitUtils2.getInstance(mContext).setToken(com.yilian.mylibrary.RequestOftenKey.getToken(mContext)).setDeviceIndex(com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext))
                        .getGoodsDownUp(goodsId, String.valueOf(chooseSb), new Callback<HttpResultBean>() {
                            @Override
                            public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                                stopMyDialog();
                                HttpResultBean body = response.body();
                                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                        switch (body.code) {
                                            case 1:
                                                PreferenceUtils.writeBoolConfig(Constants.REFRESH_MERCHANT_MANAGER_LIST, true, mContext);
                                                showToast(body.msg);
                                                finish();
                                                break;
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<HttpResultBean> call, Throwable t) {
                                stopMyDialog();
                                showToast(R.string.merchant_system_busy);
                            }
                        });
            }
        });
    }

    private void initData() {
        goodsId = getIntent().getStringExtra("goods_id");
        isNormal = getIntent().getStringExtra("is_normal");
        isYhs = getIntent().getStringExtra("is_yhs");
        isJfg = getIntent().getStringExtra("is_jfg");
        chooseList.add(0, isNormal);
        chooseList.add(1, isYhs);
        chooseList.add(2, isJfg);
        authority = getIntent().getStringExtra("authority");

        switch (authority) {
            case "1":
                yhsLayout.setVisibility(View.VISIBLE);
                jfgLayout.setVisibility(View.VISIBLE);
                break;
            case "2":
                yhsLayout.setVisibility(View.GONE);
                jfgLayout.setVisibility(View.VISIBLE);
                break;
            case "3":
                yhsLayout.setVisibility(View.VISIBLE);
                jfgLayout.setVisibility(View.GONE);
                break;
        }

        if ("1".equals(isNormal)) {
            lineCb.setChecked(true);
        }
        if ("1".equals(isYhs)) {
            yhsCb.setChecked(true);
        }
        if ("!".equals(isJfg)) {
            jfgCb.setChecked(true);
        }
    }

    private void initListener() {
        lineCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logger.i("ray-" + "走了lineCb");
                if (isChecked) {
                    chooseList.set(0, "1");
                } else {
                    chooseList.set(0, "0");
                }
                Logger.i("ray-" + chooseList.toString());
            }
        });
        yhsCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logger.i("ray-" + "走了yhsCb");
                if (isChecked) {
                    chooseList.set(1, "1");
                } else {
                    chooseList.set(1, "0");
                }
                Logger.i("ray-" + chooseList.toString());
            }
        });
        jfgCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Logger.i("ray-" + "走了jfgCb");
                if (isChecked) {
                    chooseList.set(2, "1");
                } else {
                    chooseList.set(2, "0");
                }
                Logger.i("ray-" + chooseList.toString());
            }
        });
    }

    public class ChooseWayAdapter extends BaseAdapter {
        private Context context;
        private List<String> list;

        public ChooseWayAdapter(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            if (list == null || list.size() == 0) {
                return 0;
            }
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.merchant_item_choose_way, null);
                holder = new MyViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.tv = (TextView) convertView.findViewById(R.id.tv);
                holder.cb = (CheckBox) convertView.findViewById(R.id.cb);
                convertView.setTag(holder);
            } else {
                holder = (MyViewHolder) convertView.getTag();
            }

            GlideUtil.showImageWithSuffix(context, "", holder.iv);

            holder.tv.setText("易划算专区");

            holder.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        //                        chooseStr.add();
                    } else {
                        //                        chooseStr.remove();
                    }
                }
            });
            return convertView;

        }

        class MyViewHolder {
            ImageView iv;
            TextView tv;
            CheckBox cb;
        }
    }
}
