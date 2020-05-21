package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.adapter.BaseViewHolder;
import com.yilian.mall.adapter.SimpleAdapter;
import com.yilian.mall.adapter.layoutManager.FullyGridLayoutManager;
import com.yilian.mall.entity.SearchEntity;
import com.yilian.mall.entity.SearchMemEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.networkingmodule.entity.MembersEntity;
import com.yilianmall.merchant.utils.DateUtils;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/25.
 */
public class SearchActivity extends BaseActivity {

    @ViewInject(R.id.rv)
    private RecyclerView recyclerView;

    @ViewInject(R.id.ed_keyword)
    private EditText ed_keyword;

    @ViewInject(R.id.tv_seach)
    private TextView tv_seach;

    @ViewInject(R.id.iv_no_date)
    private ImageView imgView_no_date;

    private MyIncomeNetRequest request;
    private int isFind = 0;
    private ArrayList<MembersEntity> memberId = new ArrayList<>();
    private ArrayList<SearchMemEntity> list = new ArrayList<>();
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ViewUtils.inject(this);

        userId = getIntent().getStringExtra("user_id");
    }

    /**
     * 会员模糊查询
     */
    private void search() {
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }
        if (ed_keyword.getText().toString() == null) {
            imgView_no_date.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            return;
        } else {
            imgView_no_date.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        startMyDialog();
        if (CommonUtils.isPhoneNumer(ed_keyword.getText().toString().trim())) {
            isFind = 1;
        } else {
            isFind = 0;
        }
        Logger.i("手机号  " + ed_keyword.getText().toString().trim() + " is Find  " + isFind);
        startMyDialog();
        request.searchNet(ed_keyword.getText().toString(), isFind, userId, new RequestCallBack<SearchEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SearchEntity> responseInfo) {
                ArrayList<MembersEntity> memList = responseInfo.result.memList;
                Logger.i("responseInfosearchNet    " + memList.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        if (memberId.size() != 0) {
                            memberId.clear();
                        }
                        memberId.addAll(memList);
                        if (memberId.size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            MemberAdapter adapter = new MemberAdapter(mContext, memList);
                            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(mContext, TeamDetailActivity.class);
                                    intent.putExtra("user_id", memList.get(position).memberId);
                                    startActivity(intent);
                                }
                            });
                            recyclerView.setLayoutManager(new FullyGridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
                            recyclerView.setAdapter(adapter);
                        }
//                        searchById(memberId);
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    /**
     * 根据用户id查询单一会员信息
     *
     * @param memberId
     */
    public void searchById(final ArrayList<MembersEntity> memberId) {
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }
        if (list.size() != 0) {
            list.clear();
        }
        for (int i = 0; i < memberId.size(); i++) {
            final int finalI = i;
            request.searchByIdNet(memberId.get(i).memberId, new RequestCallBack<SearchMemEntity>() {
                @Override
                public void onSuccess(ResponseInfo<SearchMemEntity> responseInfo) {
                    switch (responseInfo.result.code) {
                        case 1:
                            list.add(responseInfo.result);
                            if (finalI == memberId.size() - 1) {
//                                MemberAdapter adapter = new MemberAdapter(mContext, list);
//                                recyclerView.setLayoutManager(new FullyGridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
//                                recyclerView.setAdapter(adapter);
                            }
                            stopMyDialog();
                            break;
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }

    }


    public void onBack(View view) {
        finish();
    }

    public void find(View view) {
        search();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
    }


    /**
     * 适配器
     */
    class MemberAdapter extends SimpleAdapter<MembersEntity> {
        private Context context;
        private ArrayList<MembersEntity> list;

        public MemberAdapter(Context context, ArrayList<MembersEntity> list) {
            super(context, R.layout.item_member, list);
            this.context = context;
            this.list = list;
            Logger.i("list size  " + list.size() + " list TOString  " + list.toString());
        }

        @Override
        protected void convert(BaseViewHolder viewHolder, MembersEntity item, int position) {

            viewHolder.getTextView(R.id.tv_name).setText(item.memberName);
//            String time0 = System.currentTimeMillis() + "";
//            String curTIme0 = time0.substring(0, 10);
//            long curTime = Long.parseLong(curTIme0);
////            long regTime = Long.parseLong(item.regTime);
//            long regTime = item.regTime;
//            long time = (curTime - regTime) * 1000;
//            SimpleDateFormat sdf = new SimpleDateFormat("dd");
//            String day = sdf.format(time);
//            if (day.startsWith("0")) {
//                if (day.length()>1) {
//                    day = day.split("0")[1];
//                }
//            }
//            viewHolder.getTextView(R.id.tv_time).setText(day + "天前");
            viewHolder.getTextView(R.id.tv_time).setText(DateUtils.convertTimeToFormat(item.regTime));

            String imageUrl = item.memberIcon;
            ImageView imageView = viewHolder.getImageView(R.id.img_photo);
            com.yilian.mylibrary.GlideUtil.showCirHeadNoSuffix(mContext, imageUrl, imageView);

            if (!TextUtils.isEmpty(list.get(position).rank)) {
                switch (list.get(position).rank) {
                    case "0":
                        viewHolder.getTextView(R.id.user_level).setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip0), null);
                        break;
                    case "1":
                        viewHolder.getTextView(R.id.user_level).setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip1), null);
                        break;
                    case "2":
                        viewHolder.getTextView(R.id.user_level).setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip2), null);
                        break;
                    case "3":
                        viewHolder.getTextView(R.id.user_level).setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip3), null);
                        break;
                    case "4":
                        viewHolder.getTextView(R.id.user_level).setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip4), null);
                        break;
                    case "5":
                        viewHolder.getTextView(R.id.user_level).setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip5), null);
                        break;
                    default:
                        viewHolder.getTextView(R.id.user_level).setVisibility(View.INVISIBLE);
                        break;
                }
            } else {
                viewHolder.getTextView(R.id.user_level).setVisibility(View.INVISIBLE);
            }

        }
    }
}
