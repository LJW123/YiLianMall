package com.yilian.mall.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.RecommendAppEntity;
import com.yilian.mall.http.RecommendNetRequest;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

public class RecommendAppActivity extends BaseActivity implements View.OnClickListener {

    private TextView tvBack;
    private TextView rightTextview;
    private ImageView ivNearTitleSearch;
    private LinearLayout linearlayoutTitle;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_app);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        tvBack = (TextView) findViewById(R.id.tv_back);
        tvBack.setText("应用推荐");
        rightTextview = (TextView) findViewById(R.id.right_textview);
        ivNearTitleSearch = (ImageView) findViewById(R.id.iv_near_title_search);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.linearlayout_title);
        lv = (ListView) findViewById(R.id.lv);
        tvBack.setOnClickListener(this);
        rightTextview.setOnClickListener(this);
    }

    RecommendNetRequest recommendNetRequest;

    private void initData() {

        if (recommendNetRequest == null) {
            recommendNetRequest = new RecommendNetRequest(mContext);
        }
        recommendNetRequest.getRecommendApp(new RequestCallBack<RecommendAppEntity>() {
            @Override
            public void onSuccess(ResponseInfo<RecommendAppEntity> responseInfo) {
                RecommendAppEntity result = responseInfo.result;
                switch (result.code) {
                    case 1:
                        final ArrayList<RecommendAppEntity.ListBean> list = result.list;
                        lv.setAdapter(new BaseAdapter() {
                           class ViewHolder {
                                public View rootView;
                                public ImageView ivAppIcon;
                                public TextView tvAppName;
                                public TextView tvAppNum;
                                public TextView tvAppDetail;
                                public Button btnAppDownload;

                                public ViewHolder(View rootView) {
                                    this.rootView = rootView;
                                    this.ivAppIcon = (ImageView) rootView.findViewById(R.id.iv_app_icon);
                                    this.tvAppName = (TextView) rootView.findViewById(R.id.tv_app_name);
                                    this.tvAppNum = (TextView) rootView.findViewById(R.id.tv_app_num);
                                    this.tvAppDetail = (TextView) rootView.findViewById(R.id.tv_app_detail);
                                    this.btnAppDownload = (Button) rootView.findViewById(R.id.btn_app_download);
                                }

                            }

                            @Override
                            public int getCount() {
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
                                ViewHolder holder ;
                                if (convertView == null) {
                                    convertView = View.inflate(mContext, R.layout.item_recommend_app, null);
                                    holder = new ViewHolder(convertView);
                                    convertView.setTag(holder);
                                }else{
                                    holder = (ViewHolder) convertView.getTag();
                                }
                                final RecommendAppEntity.ListBean listBean = list.get(position);
                                String uri = Constants.ImageUrl + listBean.icon;
                                holder.ivAppIcon.setTag(uri);
                                if(holder.ivAppIcon.getTag().equals(uri)){
                                    imageLoader.displayImage(uri,holder.ivAppIcon,options);
                                }

                                holder.tvAppName.setText(listBean.title);
                                holder.tvAppDetail.setText(listBean.msg);
                                holder.btnAppDownload.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Uri uri = Uri.parse(listBean.url);

                                        Intent intent = new Intent(Intent.ACTION_VIEW,uri);

                                        startActivity(intent);
//                                        Intent intent = new Intent(RecommendAppActivity.this, WebViewActivity.class);
//                                        intent.putExtra("url",listBean.url);
//                                        startActivity(intent);
                                    }
                                });
                                //考虑到实际情况 应用大小普遍大于1M 所以这里使用M做单位，人数不超过一万的使用个做单位 超过的使用万做单位
                                holder.tvAppNum.setText((float)(Math.round(listBean.size/1024*100))/100+"M  用户量:"+( listBean.count > 10000 ? listBean.count / 10000 + "万人+" : listBean.count + "人"));
                                return convertView;
                            }
                        });
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    private void initListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.right_textview:

                break;
        }
    }
}
