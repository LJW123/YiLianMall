package com.yilian.mall.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.HeadImgAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.entity.DetailsEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.FullyGridLayoutManager;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.MembersEntity;

import java.util.ArrayList;


/**
 * Created by Ray_L_Pain on 2017/8/17 0017.
 * 新版会员详情
 */

public class TeamDetailActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Left)
    ImageView ivLeft;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.v3Share)
    ImageView ivShare;
    @ViewInject(R.id.scrollview)
    PullToRefreshScrollView scrollView;
    @ViewInject(R.id.layout_net_error)
    LinearLayout layoutNetError;
    @ViewInject(R.id.tv_refresh)
    TextView tvRefresh;
    ////
    @ViewInject(R.id.tv_member)
    TextView tvMember;
    @ViewInject(R.id.tv_name)
    TextView tvName;
    @ViewInject(R.id.tv_phone)
    TextView tvPhone;
    @ViewInject(R.id.tv_time)
    TextView tvTime;
    @ViewInject(R.id.tv_money)
    TextView tvMoney;
    @ViewInject(R.id.layout_from_referrer)
    LinearLayout layoutFromReferrer;
    @ViewInject(R.id.tv_referrer_name)
    TextView tvReferrerName;
    @ViewInject(R.id.tv_my_perform)
    TextView tvMyPerform;
    @ViewInject(R.id.user_photo)
    JHCircleView userPhoto;
    @ViewInject(R.id.user_level)
    TextView uesrLevel;
    ////
    @ViewInject(R.id.btn_search)
    Button btnSearch;
    @ViewInject(R.id.tv_member_num)
    TextView tvMemberNum;
    @ViewInject(R.id.rv)
    RecyclerView recyclerView;


    private MyIncomeNetRequest request;
    private String userId, phoneNum, memberNum, fromRerrerName, fromRefrrerId, userLev;
    private ArrayList<MembersEntity> list;
    private ArrayList<MembersEntity> listsub = new ArrayList<>();
    private HeadImgAdapter adapter;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_detail);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    private void initView() {
        userId = getIntent().getStringExtra("user_id");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivLeft.setImageDrawable(getResources().getDrawable(R.mipmap.login_back));
        ivLeft.setVisibility(View.VISIBLE);
        ivLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, MembersLevel3.class));
            }
        });

        ivShare.setVisibility(View.INVISIBLE);

        tvTitle.setText("会员详情");

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        recyclerView.setFocusable(false);

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                startActivity(intent);
            }
        });

        tvMyPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PerformanceActivity.class);
                intent.putExtra("title", "Ta的业绩");
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        layoutFromReferrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(fromRefrrerId)) {
                    showToast("Ta没有推荐人哦");
                } else {
                    Intent intent;
                    if ("1".equals(userLev)) {
                        intent = new Intent(mContext, MembersLevel3.class);
                    } else {
                        intent = new Intent(mContext, TeamDetailActivity.class);
                        intent.putExtra("user_id", fromRefrrerId);
                    }
                    startActivity(intent);
                }
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeamDetailActivity.this, SearchActivity.class);
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        tvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                getData();
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                if (null != list){
                    if (list.size() >= 20) {
                        if (list.size() / 20 > page) {
                            listsub.addAll(list.subList(20 * page, 20 * (page + 1)));
                            adapter.notifyDataSetChanged();
                        } else if (list.size() / 20 == page) {
                            listsub.addAll(list.subList(20 * page, list.size()));
                            adapter.notifyDataSetChanged();
                        } else {
                            showToast(R.string.no_more_data);
                        }
                    }
                }
                scrollView.onRefreshComplete();
            }
        });


        adapter = new HeadImgAdapter(mContext, listsub);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4);
        manager.setScrollEnabled(false);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }
        request.DetailsNet(userId, new RequestCallBack<DetailsEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<DetailsEntity> responseInfo) {
                stopMyDialog();
                scrollView.setVisibility(View.VISIBLE);
                layoutNetError.setVisibility(View.GONE);
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, responseInfo.result)) {
                    if (CommonUtils.serivceReturnCode(mContext, responseInfo.result.code, responseInfo.result.msg)) {
                        String imageUrl = responseInfo.result.head;
                        if (!TextUtils.isEmpty(imageUrl)) {
                            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                                imageUrl = imageUrl + Constants.ImageSuffix;
                            } else {
                                imageUrl = Constants.ImageUrl + imageUrl + Constants.ImageSuffix;
                            }
                            imageLoader.displayImage(imageUrl, userPhoto, options);
                        } else {
                            userPhoto.setImageResource(R.mipmap.bind_card_photo);
                        }

                        if (!TextUtils.isEmpty(responseInfo.result.userLevName)) {
                            tvMember.setVisibility(View.VISIBLE);
                            tvMember.setText(responseInfo.result.userLevName);
                        }

                        tvName.setText(TextUtils.isEmpty(responseInfo.result.userName) ? "暂无昵称" : responseInfo.result.userName);

                        tvTime.setText(StringFormat.formatDate(responseInfo.result.regTime, "yyyy-MM-dd"));

                        tvMoney.setText(MoneyUtil.setNoSmallMoney(MoneyUtil.getLeXiangBiNoZero(responseInfo.result.cash)));

                        if (!TextUtils.isEmpty(responseInfo.result.fromRefrrer)) {
                            fromRerrerName = responseInfo.result.fromRefrrer;
                            tvReferrerName.setText(fromRerrerName);
                        }

                        fromRefrrerId = responseInfo.result.fromRefrrerId;

                        userLev = responseInfo.result.userLev;

                        switch (responseInfo.result.lev) {
                            case "0":
                                uesrLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.team_vip0), null);
                                break;
                            case "1":
                                uesrLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.team_vip1), null);
                                break;
                            case "2":
                                uesrLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.team_vip2), null);
                                break;
                            case "3":
                                uesrLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.team_vip3), null);
                                break;
                            case "4":
                                uesrLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.team_vip4), null);
                                break;
                            case "5":
                                uesrLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.team_vip5), null);
                                break;
                        }

                        phoneNum = responseInfo.result.phone;
                        tvPhone.setText(phoneNum);

                        memberNum = responseInfo.result.memberCount;
                        tvMemberNum.setText("Ta的好友（" + memberNum + "人）");

                        if (page == 0) {
                            listsub.clear();
                        }
                        list = responseInfo.result.memberList;
                        listsub.addAll(list);
                        adapter.notifyDataSetChanged();
//                        if (null != list && list.size() > 0) {
//                            if (list.size() >= 20) {
//                                listsub.addAll(list.subList(0, 20));
//                            } else {
//                                listsub.addAll(list);
//                            }
//                        }
                    }
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                scrollView.setVisibility(View.GONE);
                layoutNetError.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initData() {
        getData();
    }
}
