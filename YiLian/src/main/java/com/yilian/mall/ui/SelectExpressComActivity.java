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
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.MallExpressListEntity;
import com.yilian.mall.entity.MallExpressListEntity.ExpressLists;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mylibrary.Constants;

import java.util.List;

/**
 * 选择快递公司
 * Created by Administrator on 2016/2/18.
 */
public class SelectExpressComActivity extends BaseActivity {

    private TextView tvBack;
    private ListView express_com_lv;
    private MallNetRequest mallNetRequest;
    private List<ExpressLists> expressListses;
    private ExpressListAdapter expressListAdapter;
    private BitmapUtils bitmapUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_express_com_list);

        tvBack = (TextView) findViewById(R.id.tv_back);
        express_com_lv = (ListView) findViewById(R.id.express_com_lv);

        tvBack.setText("选择公司");
        mallNetRequest = new MallNetRequest(mContext);
        bitmapUtil = new BitmapUtils(mContext);

        getExpressList();

        express_com_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                expressListAdapter.changeSelected(position);

                Intent intent = new Intent();
                intent.putExtra("express_com_result", expressListses.get(position).expressName);
                intent.putExtra("express_id",expressListses.get(position).expressId);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        express_com_lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                expressListAdapter.changeSelected(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 获取快递公司列表
     */
    private void getExpressList() {
        startMyDialog();
        mallNetRequest.expressListV1(new RequestCallBack<MallExpressListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MallExpressListEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        expressListses = responseInfo.result.list;
                        expressListAdapter = new ExpressListAdapter(mContext, expressListses);
                        express_com_lv.setAdapter(expressListAdapter);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    class ExpressListAdapter extends BaseAdapter {
        private Context context;
        private List<ExpressLists> expressLists;


        int mSelector = 0;

        public void changeSelected(int positon) { //刷新方法
            if (positon != mSelector) {
                mSelector = positon;
                notifyDataSetChanged();
            }
        }

        public ExpressListAdapter(Context context, List<ExpressLists> expressLists) {
            this.context = context;
            this.expressLists = expressLists;
        }

        @Override
        public int getCount() {
            if (expressLists != null) {
                return expressLists.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return expressLists.get(position);
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_select_express_com, null);
                holder.express_com_linear = (LinearLayout) convertView.findViewById(R.id.express_com_linear);
                holder.express_icon_iv = (ImageView) convertView.findViewById(R.id.express_icon_iv);
                holder.express_name_tv = (TextView) convertView.findViewById(R.id.express_name_tv);
                holder.express_com_selected_icon_iv = (ImageView) convertView.findViewById(R.id.express_com_selected_icon_iv);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            bitmapUtil.display(holder.express_icon_iv, Constants.ImageUrl + expressLists.get(position).expressPic + Constants.ImageSuffix);
            holder.express_name_tv.setText(expressLists.get(position).expressName);

//            if (mSelector == position) {
//                convertView.setBackgroundResource(R.color.express_com_item_selected_color);  //选中项背景
//                holder.express_com_selected_icon_iv.setVisibility(View.VISIBLE);
//            } else {
//                convertView.setBackgroundResource(R.color.wheat);  //其他项背景
//                holder.express_com_selected_icon_iv.setVisibility(View.GONE);
//            }
            return convertView;
        }

        class ViewHolder {
            private LinearLayout express_com_linear;
            private ImageView express_icon_iv;
            private TextView express_name_tv;
            private ImageView express_com_selected_icon_iv;

        }
    }
}
