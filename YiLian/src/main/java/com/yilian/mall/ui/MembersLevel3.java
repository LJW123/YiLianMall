package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.HeadImgAdapter;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.FullyGridLayoutManager;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.widget.NoticeView2;
import com.yilian.networkingmodule.entity.MemberGroupNotice;
import com.yilian.networkingmodule.entity.MembersEntity;
import com.yilian.networkingmodule.entity.MyGroupEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/4/1.
 * 三级会员界面
 */
public class MembersLevel3 extends BaseActivity {
    @ViewInject(R.id.fl_title)
    private RelativeLayout rlTitle;

    @ViewInject(R.id.tv0)
    private TextView tv0;

    @ViewInject(R.id.tv)
    private TextView mTv;
    @ViewInject(R.id.user_photo)
    private JHCircleView userPhoto;

    @ViewInject(R.id.user_level)
    private TextView uesrLevel;

    @ViewInject(R.id.user_name)
    private TextView userName;

    @ViewInject(R.id.referees)
    private TextView referees;

    @ViewInject(R.id.tv_level1_count)
    private TextView level1Count;

    @ViewInject(R.id.tv_my_cash)
    private TextView tvMyCash;

    @ViewInject(R.id.tv_my_integral)
    private TextView tvMyIntegral;

    @ViewInject(R.id.tv_my_perform)
    private TextView tvMyPerform;

    @ViewInject(R.id.rv)
    private RecyclerView mRecycleView;

    @ViewInject(R.id.scrollview)
    private PullToRefreshScrollView scrollView;

    @ViewInject(R.id.btn_level_up)
    private LinearLayout levelUp;

    @ViewInject(R.id.tv_member_num)
    private TextView tvMemberNum;
    @ViewInject(R.id.ll_noticeView)
    private LinearLayout llNoticeView;
    @ViewInject(R.id.notice_view)
    private com.yilian.mylibrary.widget.NoticeView2 noticeView;
    @ViewInject(R.id.tv_one_notice)
    private TextView tvOneNotice;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private String userId;
    private ArrayList<MembersEntity> list;
    private InvateNetRequest request0;
    private String shareUrl;
    private Animation animBottom;
    private String income;
    private String url;
    private int page = 0;
    private HeadImgAdapter adapter;
    private ArrayList<MembersEntity> listsub = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_level3);

        ViewUtils.inject(this);

        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        initScrollView();
        //设置头像昵称
        setData();
        //设置推荐人等信息
        initData();
        initListener();
    }

    private void initListener() {
        RxUtil.clicks(llNoticeView, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                Intent intent = new Intent(mContext, InformationActivity.class);
                intent.putExtra("currentItem", 2);
                startActivity(intent);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private void getGroupNotice() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getMemberGroupNotice("group_message")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MemberGroupNotice>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(MemberGroupNotice memberGroupNotice) {
                        ArrayList<MemberGroupNotice.ListBean> list = memberGroupNotice.list;
                        if (list.size() <= 0) {
                            llNoticeView.setVisibility(View.GONE);
                        } else {
                            llNoticeView.setVisibility(View.VISIBLE);
                            if (list.size() == 1) {
                                MemberGroupNotice.ListBean listBean = list.get(0);
                                tvOneNotice.setVisibility(View.VISIBLE);
                                noticeView.setVisibility(View.GONE);
                                tvOneNotice.setText(Html.fromHtml("恭喜您的" + listBean.relation + "<font color='#F72D42'>" + listBean.userName + "</font>获得" +
                                        listBean.prizeName));
                            } else {
                                tvOneNotice.setVisibility(View.GONE);
                                noticeView.setVisibility(View.VISIBLE);
                                noticeView.getMemberNotice(list, new NoticeView2.SetNew() {
                                    @Override
                                    public void setNews(View view, int position) {
                                        TextView tvNotice1 = (TextView) view.findViewById(R.id.tv_notice1);
                                        TextView tvNotice2 = (TextView) view.findViewById(R.id.tv_notice2);
                                        TextView tvMoreNotice1 = (TextView) view.findViewById(R.id.tv_more_notice1);
                                        tvMoreNotice1.setText("详情");
                                        TextView tvMoreNotice2 = (TextView) view.findViewById(R.id.tv_more_notice2);
                                        tvMoreNotice2.setText("详情");
                                        for (int i = 0; i < 2; i++) {
                                            int index = position * 2 + i;
                                            MemberGroupNotice.ListBean listBean = list.get(index);
                                            Spanned spanned;
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                spanned = Html.fromHtml("恭喜您的" + listBean.relation + "<font color='#F72D42'>" + listBean.userName + "</font>获得" +
                                                        listBean.prizeName, Html.FROM_HTML_MODE_LEGACY);
                                            } else {
                                                spanned = Html.fromHtml("恭喜您的" + listBean.relation + "<font color='#F72D42'>" + listBean.userName + "</font>获得" +
                                                        listBean.prizeName);
                                            }
                                            if (i == 0) {
                                                tvNotice1.setText(spanned);
                                            } else if (i == 1) {
                                                tvNotice2.setText(spanned);
                                            }
                                        }
                                    }
                                });
                            }
                        }

                    }
                });
        addSubscription(subscription);
    }

    private void initScrollView() {
        tvMyPerform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PerformanceActivity.class);
                intent.putExtra("title", "我的业绩");
                intent.putExtra("user_id", userId);
                startActivity(intent);
            }
        });

        mRecycleView.setFocusable(false);

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);

        scrollView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {

            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                // TODO Auto-generated method stub

            }
        });
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                initData();
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page++;
                if (null != list) {
                    if (list.size() >= 20) {
                        if (list.size() / 20 > page) {
                            Logger.i("list.size()" + list.size() / 20 + "page" + page);
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
        mRecycleView.addItemDecoration(decor);
        FullyGridLayoutManager manager = new FullyGridLayoutManager(mContext, 4);
        manager.setScrollEnabled(false);
        mRecycleView.setLayoutManager(manager);
        mRecycleView.setAdapter(adapter);
    }

    @Override
    public void onBack(View view) {
        finish();
    }

    public void search(View view) {
        Intent intent = new Intent(MembersLevel3.this, SearchActivity.class);
        intent.putExtra("user_id", userId);
        startActivity(intent);
    }

    public void levelUp(View view) {
        if (isLogin()) {
//            Intent intent = new Intent(MembersLevel3.this, NMemberChargeActivity.class);
//            intent.putExtra("type", "chooseCharge");
//            startActivity(intent);
            startActivity(new Intent(MembersLevel3.this, RechargeActivity.class));
        } else {
            startActivity(new Intent(MembersLevel3.this, LeFenPhoneLoginActivity.class));
        }
    }

    private void initData() {

        getMyGroupData();
        getGroupNotice();
    }

    private void getMyGroupData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getMyGroupData(new Callback<MyGroupEntity>() {
                    @Override
                    public void onResponse(Call<MyGroupEntity> call, Response<MyGroupEntity> response) {
                        stopMyDialog();
                        MyGroupEntity body = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                switch (body.code) {
                                    case 1:
                                        if (!TextUtils.isEmpty(body.refereeName)) {
                                            referees.setText("推荐人：" + body.refereeName);
                                        }
                                        /**
                                         * 这个是修改地方我的团队
                                         */
                                        if (TextUtils.isEmpty(body.memberCount)) {
                                            level1Count.setText("好友：0人");
                                        } else {
                                            level1Count.setText("好友：" + body.memberCount + "人");
                                        }
                                        if (!TextUtils.isEmpty(body.memberIncome)) {
                                            tvMyIntegral.setText(MoneyUtil.getLeXiangBi(body.memberIncome));
                                        } else {
                                            tvMyIntegral.setText("0.00");
                                        }

                                        if (!TextUtils.isEmpty(body.memberCash)) {
                                            tvMyCash.setText(MoneyUtil.getLeXiangBiNoZero(body.memberCash));
                                        } else {
                                            tvMyCash.setText("0.00");
                                        }

                                        switch (body.memberLev) {
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
                                            default:
                                                break;
                                        }

                                        tvMemberNum.setText("我的好友（" + body.memberCount + "人）");

                                        userId = body.parentUserId;

                                        if (page == 0) {
                                            listsub.clear();
                                        }
                                        list = (ArrayList<MembersEntity>) body.memberList;
                                        listsub.addAll(list);
                                        adapter.notifyDataSetChanged();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyGroupEntity> call, Throwable t) {
                        stopMyDialog();
                    }
                });
    }


    public void info(View view) {

        String phone = sp.getString("phone", "");

        if (TextUtils.isEmpty(phone)) {
            showToast("数据异常，请重新登录后，再来分享！");
            return;
        }
        if (request0 == null) {
            request0 = new InvateNetRequest(mContext);
        }
        request0.invateNet(new RequestCallBack<InvateEntity>() {
            @Override
            public void onSuccess(ResponseInfo<InvateEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        shareUrl = responseInfo.result.url;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
            }
        });

        animBottom = AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom);
        UmengDialog dialog1 = new UmengDialog(mContext, animBottom, R.style.DialogControl,
                new UmengGloble().getAllIconModels());
        dialog1.setItemLister(new UmengDialog.OnListItemClickListener() {

            @Override
            public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                String content = getResources().getString(R.string.appshare);
                new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, shareUrl).share();
            }
        });

        dialog1.show();
    }

    private void setData() {

        RequestOftenKey.getUserInfor(mContext, sp);
        String nickName = sp.getString("name", "暂无昵称");
        if (TextUtils.isEmpty(nickName)) {
            userName.setText("暂无昵称");
        } else {
            userName.setText(nickName);
        }
        if (!TextUtils.isEmpty(sp.getString("photo", ""))) {
            if (sp.getString("photo", "").contains("http://") || sp.getString("photo", "").contains("https://")) {
                url = sp.getString("photo", "");
            } else {
                url = Constants.ImageUrl + sp.getString("photo", "");
            }
            imageLoader.displayImage(url, userPhoto, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                            userPhoto.setImageBitmap(loadedImage);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                case DECODING_ERROR:
                                case NETWORK_DENIED:
                                case OUT_OF_MEMORY:
                                case UNKNOWN:
                                    message = "图片加载错误";
                                    break;
                            }
                        }
                    });
        } else {
            userPhoto.setImageResource(R.mipmap.bind_card_photo);
        }
    }
}
