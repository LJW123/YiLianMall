package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.SpellGoodsListRecyleAdapter;
import com.yilian.mall.adapter.SpellGroupGridViewAdapter;
import com.yilian.mall.adapter.SpellGroupHeadIconAdapter;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.JPSignGVEntity;
import com.yilian.mall.entity.SpellGroupListEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mall.widgets.CountdownView.CountdownView;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.networkingmodule.entity.SpellGroupShareListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yilian.mall.R.id.iv_nothing;


/**
 * 拼团结果成功/失败
 */
public class SpellGroupResultStatusActivity extends BaseActivity implements View.OnClickListener {

    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private TextView tvSpellGroupResult;
    private TextView tvRefundContent;
    private LinearLayout llSpellGroupInfo;
    private NoScrollGridView gridView;
    private TextView goodsName;
    private TextView openGroupTime;
    private Button btnOnceAgain;
    private RecyclerView recyclerListView;
    private PullToRefreshScrollView scrollView;
    private TextView tvRefund;
    private String groupId;
    private JPNetRequest jpNetRequest;
    private NoScrollGridView gridViewHead;
    private int page;
    private String count;
    private ArrayList<SpellGroupListEntity.DataBean> recycleListData = new ArrayList<>();
    private SpellGoodsListRecyleAdapter recycleViewAdapter;
    private LinearLayout llSpellGroupProgress;
    private TextView tvResidueNumber;
    private CountdownView countdownView;
    private ImageView ivShareWechar;
    private ImageView ivShareCircleOfFriends;
    private ImageView ivShareQQFriends;
    private ImageView ivShareQQSpace;
    private LinearLayout flShade;
    private SpellGroupGridViewAdapter gridViewAdapter;
    private LinearLayout llReundExplain;
    private String shareContent;
    private String shareImageUrl;
    private String shareUrl;
    private JPNetRequest netRequest;
    private String shareTitle;
    private LinearLayout llGroupName;
    private LinearLayout llErrorView;
    private TextView tvLeable1;
    private TextView tvLeable2;
    private TextView tvLeable3;
    private TextView tvLeable4;
    private TextView tvResidue;
    private UmengDialog umengDialog;
    private LinearLayout llSpellTital;
    private String activityId;
    private LinearLayout llHeadIcon;
    private LinearLayout llGroupInfo;
    private String orderId;
    private SpellGroupHeadIconAdapter headGridAdapter;
    private String refundContent;
    private SpellGroupShareListEntity.DataBean dataBean;
    private ImageView ivNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spell_group_result);
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");
        activityId = intent.getStringExtra("activityId");
        orderId = intent.getStringExtra("orderId");
        Logger.i("groupId  " + groupId);
        count = "30";
        page = 0;
        initView();
        initData();
        initListener();
        getShareUrl();
    }

    private void initListener() {
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                page = 0;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //上啦加载加载的是附近商家推荐的数据
                page++;
                initData();
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                JPGoodsEntity itemView = (JPGoodsEntity) gridViewAdapter.getItem(position);
                Intent intent = new Intent(mContext, JPNewCommDetailActivity.class);

                intent.putExtra("goods_id", itemView.JPGoodsId);
                intent.putExtra("filiale_id", itemView.JPFilialeId);
                intent.putExtra("tags_name", itemView.JPTagsName);
                startActivity(intent);
            }
        });

    }

    private void initData() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .getSpellGroupShareData(groupId, orderId, new Callback<SpellGroupShareListEntity>() {
                    @Override
                    public void onResponse(Call<SpellGroupShareListEntity> call, Response<SpellGroupShareListEntity> response) {
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        llErrorView.setVisibility(View.GONE);
                                        ivNothing.setVisibility(View.GONE);
                                        scrollView.setVisibility(View.VISIBLE);
                                        llHeadIcon.setVisibility(View.VISIBLE);
                                        llGroupInfo.setVisibility(View.VISIBLE);
                                        dataBean = response.body().data;
                                        initViewData(dataBean);
                                        stopMyDialog();
                                        scrollView.onRefreshComplete();
                                        break;
                                    default:
                                        llErrorView.setVisibility(View.VISIBLE);
                                        scrollView.setVisibility(View.GONE);
                                        llHeadIcon.setVisibility(View.GONE);
                                        llGroupInfo.setVisibility(View.GONE);
                                        Logger.i("ERROR  " + response.body().code);
                                        stopMyDialog();
                                        scrollView.onRefreshComplete();
                                        break;
                                }
                            } else {
                                stopMyDialog();
                                scrollView.onRefreshComplete();
                                ivNothing.setVisibility(View.VISIBLE);
                                scrollView.setVisibility(View.GONE);
                                llHeadIcon.setVisibility(View.GONE);
                                llGroupInfo.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<SpellGroupShareListEntity> call, Throwable t) {
                        stopMyDialog();
                        scrollView.onRefreshComplete();
                        llErrorView.setVisibility(View.VISIBLE);
                        scrollView.setVisibility(View.GONE);
                        llHeadIcon.setVisibility(View.GONE);
                        llGroupInfo.setVisibility(View.GONE);

                    }
                });

    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("有奖拼团"); //拼团的分享
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.GONE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.INVISIBLE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.VISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        tvSpellGroupResult = (TextView) findViewById(R.id.tv_spell_group_result);
        tvRefundContent = (TextView) findViewById(R.id.tv_refund_content);
        tvRefund = (TextView) findViewById(R.id.tv_refund);
        llSpellGroupInfo = (LinearLayout) findViewById(R.id.ll_spell_group_info);
        gridView = (NoScrollGridView) findViewById(R.id.grid_view);
        gridViewHead = (NoScrollGridView) findViewById(R.id.grid_view_head);
        goodsName = (TextView) findViewById(R.id.goods_name);
        openGroupTime = (TextView) findViewById(R.id.open_group_time);
        recyclerListView = (RecyclerView) findViewById(R.id.recycler_view);
        btnOnceAgain = (Button) findViewById(R.id.btn_once_again);
        scrollView = (PullToRefreshScrollView) findViewById(R.id.sv_pull_refresh);
        scrollView.setMode(PullToRefreshScrollView.Mode.BOTH);
        llReundExplain = (LinearLayout) findViewById(R.id.ll_refund_explain);
        llGroupName = (LinearLayout) findViewById(R.id.ll_group_name);
        llErrorView = (LinearLayout) findViewById(R.id.ll_error_view);
        llErrorView.setVisibility(View.GONE);
        TextView tvError = (TextView) findViewById(R.id.tv_update_error);
        llSpellTital = (LinearLayout) findViewById(R.id.ll_spell_tital);

        //拼团进行中的UI
        llSpellGroupProgress = (LinearLayout) findViewById(R.id.ll_spell_group_progress);
        tvResidueNumber = (TextView) findViewById(R.id.tv_residue_number);
        countdownView = (CountdownView) findViewById(R.id.countdownview);
        tvResidue = (TextView) findViewById(R.id.residue);

        ivShareWechar = (ImageView) findViewById(R.id.iv_share_wechar);
        ivShareCircleOfFriends = (ImageView) findViewById(R.id.iv_share_circle_of_friends);
        ivShareQQFriends = (ImageView) findViewById(R.id.iv_share_qq_firends);
        ivShareQQSpace = (ImageView) findViewById(R.id.iv_share_qq_space);
        flShade = (LinearLayout) findViewById(R.id.fl_shade);
        llHeadIcon = (LinearLayout) findViewById(R.id.ll_head_ioon);
        llGroupInfo = (LinearLayout) findViewById(R.id.ll_group_info);
        ivNothing = (ImageView) findViewById(iv_nothing);

        //标签的TextView
        tvLeable1 = (TextView) findViewById(R.id.tv_label1);
        tvLeable2 = (TextView) findViewById(R.id.tv_label2);
        tvLeable3 = (TextView) findViewById(R.id.tv_label3);
        tvLeable4 = (TextView) findViewById(R.id.tv_label4);


        llGroupName.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        btnOnceAgain.setOnClickListener(this);
        ivShareWechar.setOnClickListener(this);
        ivShareCircleOfFriends.setOnClickListener(this);
        ivShareQQFriends.setOnClickListener(this);
        ivShareQQSpace.setOnClickListener(this);
        llReundExplain.setOnClickListener(this);
        tvError.setOnClickListener(this);
        flShade.setOnClickListener(this);
        v3Share.setOnClickListener(this);
        tvRefund.setOnClickListener(this);

    }

    private int type;

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.iv_share_wechar:
                type = 0;
                share();
                break;
            case R.id.iv_share_circle_of_friends:
                type = 1;
                share();
                break;
            case R.id.iv_share_qq_firends:
                type = 4;
                share();
                break;
            case R.id.iv_share_qq_space:
                type = 3;
                share();
                break;
            case R.id.btn_once_again:
                switch (btnOnceAgain.getText().toString().trim()) {
                    case "看看其他拼团":
                    case "再开一团":
                        startActivity(new Intent(mContext, SpellGroupListActiviy.class));
                        break;
                    case "查看订单":
                        intent=new Intent(mContext,SpellGroupOrderActivity.class);
                        intent.putExtra("orderId",orderId);
                        startActivity(intent);
                        break;
                }
                break;
            case R.id.ll_refund_explain:
                //打开WebView
                intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", Constants.spellGroupRefundExplain);
                Logger.i("urldddddd  " + Constants.spellGroupRefundExplain);
                startActivity(intent);
                break;
            case R.id.ll_group_name:
                intent = new Intent(mContext, SpellGroupDetailsActivity.class);
                intent.putExtra("index", activityId);
                startActivity(intent);
                break;
            case R.id.tv_update_error:
                page = 0;
                initData();
                break;
            case R.id.fl_shade:
                flShade.setVisibility(View.GONE);
                break;
            case R.id.v3Share:
                UmShare();
                break;
            case R.id.tv_refund:
                refundContent = tvRefund.getText().toString().toString();
                switch (refundContent){
                    case "查看退款":
                        intent = new Intent(mContext, V3MoneyActivity.class);
                        intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                        startActivity(intent);
                        break;
                    case "查看物流":
                        String goodsName = dataBean.goodsName;
                        //查看物流
                        intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra("title_name", "包裹物流信息");
                        intent.putExtra("url", Ip.getWechatURL(mContext) + "m/expressInfo.php?type=" +
                                dataBean.expressCompany + "&postid=" + dataBean.expressNum +
                                "&token=" + RequestOftenKey.getToken(mContext) +
                                "&name=" + goodsName);
                        startActivity(intent);

                        break;
                        default:
                            break;
                }
                break;
            default:
                break;
        }
    }

    private void UmShare() {
        if (umengDialog == null) {

            umengDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(mContext, R.anim.anim_wellcome_bottom),
                    R.style.DialogControl, new UmengGloble().getAllIconModels());
            umengDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            shareContent,
                            shareTitle,
                            shareImageUrl,
                            shareUrl).share();
                }
            });
        }

        umengDialog.show();
    }

    private void share() {
        new ShareUtile(
                mContext,
                type,
                shareContent,
                shareTitle,
                WebImageUtil.getInstance().getWebImageUrlNOSuffix(shareImageUrl),
                shareUrl).share();
    }

    private void initViewData(SpellGroupShareListEntity.DataBean dataBean) {
        goodsName.setText(dataBean.goodsName);
        openGroupTime.setText(DateUtils.timeStampToStr(Long.parseLong(dataBean.openGroupTime)));
        if (null == dataBean.userIcon || dataBean.userIcon.size() <= 0) {
            if (page>0){
                showToast("暂无更多");
            }else{
                gridViewHead.setVisibility(View.GONE);
            }
        } else {
            if (headGridAdapter == null) {
                headGridAdapter = new SpellGroupHeadIconAdapter(dataBean.userIcon);
            }
            headGridAdapter.notifyDataSetChanged();
            gridViewHead.setAdapter(headGridAdapter);
        }
        switch (dataBean.groupStatus) {
            case "0"://停止
                llSpellGroupInfo.setVisibility(View.VISIBLE);
                tvSpellGroupResult.setText("拼团失败");
                tvRefundContent.setText(R.string.spell_group_refund);
                getRecommitData();
                break;
            case "1"://进行中
                tvSpellGroupResult.setText("进行中");
                llSpellGroupProgress.setVisibility(View.VISIBLE);
                tvResidueNumber.setText(Html.fromHtml("还差<font color=\"#F75A53\">" + (Integer.parseInt(dataBean.groupCondition)-Integer.parseInt(dataBean.num)) + "</font>人成团"));

                long differentTime = Long.parseLong(dataBean.timeFail) - Long.parseLong(dataBean.time);
                if (differentTime > 0) {
                    countdownView.setVisibility(View.VISIBLE);
                    countdownView.start(differentTime * 1000);
//                countdownView.dynamicShow(new DynamicConfig.Builder().setConvertDaysToHours(true).build());
                } else {
                    tvResidue.setVisibility(View.GONE);
                    countdownView.setVisibility(View.GONE);
                }
                countdownView.setOnCountdownEndListener(new CountdownView.OnCountdownEndListener() {
                    @Override
                    public void onEnd(boolean isEnd) {
                        initData();
                    }
                });

                flShade.setVisibility(View.VISIBLE);
                getSpellGroupListData();
                break;
            case "2"://未成团;
                llSpellGroupInfo.setVisibility(View.VISIBLE);
                tvSpellGroupResult.setText("拼团失败");
                tvRefundContent.setText(R.string.spell_group_refund);
                tvRefund.setVisibility(View.VISIBLE);
                tvRefund.setText("查看退款");
                btnOnceAgain.setVisibility(View.VISIBLE);
                btnOnceAgain.setText("看看其他拼团");
                getRecommitData();
                break;
            case "3"://已成团
                llSpellGroupInfo.setVisibility(View.VISIBLE);
                tvSpellGroupResult.setText("拼团成功");
                tvRefundContent.setText(DateUtils.timeStampToStr(Long.parseLong(dataBean.timeFail)));
                tvRefund.setText("拼够" + dataBean.groupCondition + "人");
                tvRefund.setTextColor(getResources().getColor(R.color.color_666));
                btnOnceAgain.setVisibility(View.VISIBLE);
                btnOnceAgain.setText("再开一团");
                getSpellGroupListData();
                break;
            case "4"://未中奖
                llSpellGroupInfo.setVisibility(View.VISIBLE);
                tvSpellGroupResult.setText("很遗憾，未中奖");
                tvSpellGroupResult.getPaint().setFakeBoldText(true);
                tvRefundContent.setText(R.string.spell_group_refund);
                tvRefund.setText("查看退款");
                tvRefund.setTextColor(getResources().getColor(R.color.color_red));
                btnOnceAgain.setVisibility(View.VISIBLE);
                btnOnceAgain.setText("看看其他拼团");
                getSpellGroupListData();
                break;
            case "5"://已中奖
                llSpellGroupInfo.setVisibility(View.VISIBLE);
                tvSpellGroupResult.setText("恭喜您，中奖啦!");
                tvSpellGroupResult.getPaint().setFakeBoldText(true);
                tvSpellGroupResult.setTextColor(getResources().getColor(R.color.color_red));
                tvRefundContent.setText("我们将在48小时内为您发货。");
                tvRefund.setTextColor(getResources().getColor(R.color.color_red));
                tvRefund.setText("查看物流");
                btnOnceAgain.setVisibility(View.VISIBLE);
                btnOnceAgain.setText("查看订单");
                getSpellGroupListData();
                break;
        }
    }


    public void getRecommitData() {
        llSpellTital.setVisibility(View.GONE);
        if (jpNetRequest == null) {
            jpNetRequest = new JPNetRequest(mContext);
        }
        //获取的每日精品推荐
        jpNetRequest.signGridView(new RequestCallBack<JPSignGVEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSignGVEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<JPGoodsEntity> goodsList = responseInfo.result.data.goodsList;
                        if (null != goodsList || goodsList.size() > 0) {
                            gridView.setVisibility(View.VISIBLE);
                            gridViewAdapter = new SpellGroupGridViewAdapter(goodsList);
                            gridView.setAdapter(gridViewAdapter);
                        } else {
                            gridView.setVisibility(View.GONE);
                        }
                        stopMyDialog();
                        break;
                    default:
                        Logger.i(responseInfo.result.code + responseInfo.result.msg);
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

    public void getSpellGroupListData() {
        startMyDialog();
        com.yilian.mall.retrofitutil.RetrofitUtils.getInstance(mContext).getSpellGroupListContent(String.valueOf(page), String.valueOf(count), new Callback<SpellGroupListEntity>() {
            @Override
            public void onResponse(Call<SpellGroupListEntity> call, Response<SpellGroupListEntity> response) {
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext,response.body())){
                    if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                        switch (response.body().code) {
                            case 1:
                                recyclerListView.setVisibility(View.VISIBLE);
                                llSpellTital.setVisibility(View.VISIBLE);
                                List<SpellGroupListEntity.DataBean> listData = response.body().data;
                                initListData((ArrayList<SpellGroupListEntity.DataBean>) listData);
                                scrollView.onRefreshComplete();
                                break;
                        }
                    }else{
                        recyclerListView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<SpellGroupListEntity> call, Throwable t) {
                stopMyDialog();
                scrollView.onRefreshComplete();
                recyclerListView.setVisibility(View.GONE);
            }
        });
    }

    private void initListData(ArrayList<SpellGroupListEntity.DataBean> listData) {
        if (null != listData && listData.size() > 0) {
            //分页
            if (page >= 1) {
                recycleListData.addAll(listData);
                recycleViewAdapter.notifyDataSetChanged();
            } else {
                if (recycleViewAdapter == null) {
                    recycleListData.clear();
                    recycleListData.addAll(listData);
                    recycleViewAdapter = new SpellGoodsListRecyleAdapter(recycleListData, mContext);

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);//recycle必须设置layoutManager不然不知道自己的孩子如何放置
                    linearLayoutManager.setSmoothScrollbarEnabled(true);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    linearLayoutManager.setAutoMeasureEnabled(true);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    recyclerListView.setLayoutManager(linearLayoutManager);
                    recyclerListView.setHasFixedSize(true);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    recyclerListView.setNestedScrollingEnabled(false);//这四句话是解决scrollView嵌套RecycleView滑动卡顿的情况
                    recyclerListView.setAdapter(recycleViewAdapter);
                }
                recycleViewAdapter.notifyDataSetChanged();
            }
        } else {
            if (page>0){
                showToast("暂无更多");
            }else{
                recyclerListView.setVisibility(View.GONE);
            }
        }

        Logger.i("listData.Size " + listData.size());
    }

    public void getShareUrl() {
        if (netRequest == null) {
            netRequest = new JPNetRequest(mContext);
        }
        String type = "15";
        Logger.i("groupId   " + groupId + " activityId  " + activityId);
        netRequest.getShareUrl(type, activityId + "," + groupId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code) {
                    case 1:
                        shareContent = responseInfo.result.content;
                        shareImageUrl = responseInfo.result.imgUrl;
                        String imageUrl = responseInfo.result.title;
                        shareUrl = responseInfo.result.url;
                        shareTitle = responseInfo.result.subTitle;
                        if (TextUtils.isEmpty(imageUrl)) {
                            shareImageUrl = "";
                        } else {
                            if (imageUrl.contains("http://") || imageUrl.contains("https://")) {
                                shareImageUrl = imageUrl;
                            } else {
                                shareImageUrl = Constants.ImageUrl + imageUrl;
                            }
                        }
                        Logger.i("shareContent  " + shareContent + "\ndimagUrl  " + shareImageUrl + "\n url " + shareUrl);
                        break;
                    default:
                        Logger.i("分享错误 " + responseInfo.result.code);
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }
}
