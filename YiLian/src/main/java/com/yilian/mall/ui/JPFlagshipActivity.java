package com.yilian.mall.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshScrollView;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.FlagshipAdapter2;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.entity.AliCustomerServiceInfo;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.JPFlagshipEntity;
import com.yilian.mall.entity.JPFragmentGoodEntity;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.http.LeFenHomeRequest;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PhoneUtil;
import com.yilian.mylibrary.RxUtil;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2016/10/21 0021.
 * 旗舰店详情
 */

public class JPFlagshipActivity extends BaseActivity implements View.OnClickListener, PullToRefreshScrollView.ScrollListener {
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
    /**
     * 7代表旗舰店
     */
    private static final int COLLECTTYPE = 7;
    public String title, id, goodId, getedId;
    @ViewInject(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    /**
     * 标题
     */
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    /**
     * 返回
     */
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Left)
    ImageView ivLeft;
    /**
     * 购物车
     */
    @ViewInject(R.id.v3Shop)
    ImageView ivShop;
    /**
     * 粘性头部
     */
    @ViewInject(R.id.sticky_view)
    View stickyView;
    @ViewInject(R.id.rg_sort)
    RadioGroup rgSticky;
    @ViewInject(R.id.rb_sort_price)
    RadioButton rbStickyPrice;
    @ViewInject(R.id.cb_sort_has_data)
    CheckBox cbStickySortHasData;
    /**
     * 分享
     */
//    icon_customer_service_offline
    @ViewInject(R.id.v3Share)
    ImageView ivShare;
    /**
     * 标题下面的图
     */
    ImageView ivTop;
    /**
     * 收藏按钮
     */
    ImageView collectBtn;
    /**
     * layout
     */
    LinearLayout topLayout;
    /**
     * inner_layout
     */
    LinearLayout innerLayout;
    /**
     * 返回顶部
     */
    @ViewInject(R.id.iv_return_top)
    ImageView ivReturnTop;
    /**
     * 满减tag
     */
    TextView tvTag;
    /**
     * 满减desc
     */
    TextView tvDesc;
    /**
     * 新排序控件
     */
    RadioGroup rgSort;
    /**
     * 默认排序
     */
    RadioButton rbSortNewest;
    /**
     * 价格排序
     */
    RadioButton rbSortPrice;
    /**
     * 数量排序
     */
    RadioButton rbSortCount;
    /**
     * 只看有货
     */
    CheckBox cbSortHasData;
    /**
     * GridView
     */
    JPFlagshipEntity.DataBean bean;
    LeFenHomeRequest request;
    JPNetRequest jpRequest;
    /**
     * 旗舰店分享
     */
    String share_type = "1";
    String share_title, share_content, share_img, share_url, shareImg;
    /**
     * 1 默认 2 价格 3 数量 4 只看有货
     */
    int flag = 1;
    /**
     * 上一次选中的按钮
     */
    int lastCheckedRadioButton;
    GridLayoutManager manager;
    @ViewInject(R.id.ll_v3title)
    private View titleView;
    private int titleHeight;
    @ViewInject(R.id.recyclerview)
    private RecyclerView recyclerView;
    private FlagshipAdapter2 mAdapter;
    private UmengDialog mShareDialog;
    private int isCollect;
    private int pageIndex = 0;
    private String sort = "1000";
    private String customerServiceGroupId;
    private String customerServicePersonId;
    private String tagStr, contentStr;
    private View headerView;
    private boolean isFirst = true;
    /**
     * 记录头部筛选在屏幕上的位置信息
     */
    private int[] location;
    /**
     * 记录title在屏幕中的位置信息
     */
    private int[] loc;
    /**
     * 店铺简介
     */
    @ViewInject(R.id.tv_store_introduction)
    private TextView tvStoreIntroduction;
    /**
     * 联系客服
     */
    @ViewInject(R.id.fl_contact_service)
    private FrameLayout flContactService;
    private PopupWindow mPupwindow;
    /**
     * 弹窗客服电话
     */
    private TextView tvServicePhone;
    /**
     * 客服电话
     */
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flagship2);
        ViewUtils.inject(this);

        initView();
        swipeRefreshLayout.setRefreshing(true);
        initData();
        getCustomerServiceInfo();
        initListener();
        getGoods();
        initScrollView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissPup();
    }

    /**
     * 关闭客服电话弹窗
     *
     * @return
     */
    private boolean dismissPup() {
        if (mPupwindow != null && mPupwindow.isShowing()) {
            mPupwindow.dismiss();
            return true;
        }
        return false;
    }

    private void initHeaderView() {
        headerView = View.inflate(mContext, R.layout.header_flagship, null);
        ivTop = headerView.findViewById(R.id.img_top);
        collectBtn = headerView.findViewById(R.id.iv_collect);
        topLayout = headerView.findViewById(R.id.top_layout);
        innerLayout = headerView.findViewById(R.id.inner_layout);
        tvTag = headerView.findViewById(R.id.tv_tag);
        tvDesc = headerView.findViewById(R.id.tv_desc);

        rgSort = (RadioGroup) headerView.findViewById(R.id.rg_sort);
        rbSortNewest = (RadioButton) headerView.findViewById(R.id.rb_sort_newest);
        rbSortPrice = (RadioButton) headerView.findViewById(R.id.rb_sort_price);
        rbSortCount = (RadioButton) headerView.findViewById(R.id.rb_sort_count);
        cbSortHasData = (CheckBox) headerView.findViewById(R.id.cb_sort_has_data);
    }

    /**
     * 获取旗舰店客服信息
     */
    private void getCustomerServiceInfo() {
        try {
            RetrofitUtils.getInstance(mContext).setContext(mContext).getAliCustomerServiceInfo("5", id).enqueue(new Callback<AliCustomerServiceInfo>() {
                @Override
                public void onResponse(Call<AliCustomerServiceInfo> call, Response<AliCustomerServiceInfo> response) {
                    AliCustomerServiceInfo body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response)) {
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                            AliCustomerServiceInfo.DataBean data = body.data;
                            customerServicePersonId = data.personId;
                            customerServiceGroupId = data.groupId;

                        }
                    }
                }

                @Override
                public void onFailure(Call<AliCustomerServiceInfo> call, Throwable t) {

                }
            });
        } catch (Exception e) {
            Logger.i(e.getMessage());
            e.printStackTrace();
        }
    }

    private void initView() {
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, R.color.white), Constants.STATUS_BAR_ALPHA_60);
        id = getIntent().getStringExtra("index_id");
        recyclerView = findViewById(R.id.recyclerview);
        mAdapter = new FlagshipAdapter2(mContext, new ArrayList<JPGoodsEntity>());
        manager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(manager);
        initHeaderView();
        mAdapter.addHeaderView(headerView);
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 8, R.color.color_line, true);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addItemDecoration(decor);


        rgSort.check(R.id.rb_sort_newest);
        rgSticky.check(R.id.rb_sort_newest);
        //初始化时上次选中按钮设置为第一次默认选中的按钮
        lastCheckedRadioButton = R.id.rb_sort_newest;
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        ivBack.setOnClickListener(this);
        ivLeft.setVisibility(View.GONE);
        ivShop.setOnClickListener(this);
        ivShop.setVisibility(View.VISIBLE);
//        ivShare.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.icon_customer_service_offline));
        ivShare.setOnClickListener(this);
        ivShare.setVisibility(View.VISIBLE);
        collectBtn.setOnClickListener(this);
        request = new LeFenHomeRequest(mContext);

    }

    /**
     * 获取第一页数据
     */
    private void getFirstData() {
        swipeRefreshLayout.setRefreshing(true);
        pageIndex = 0;
        initData();
        getGoods();
    }

    /**
     * 获取第下一页数据
     */
    private void getNextData() {
        pageIndex++;
        getGoods();
    }

    private void getGoods() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        //只有在进行网络请求时才为排序条件添加上逗号，其余地方获取的排序值都要去除逗号再使用
        StringBuffer sb = new StringBuffer();
        for (int i = 0, count = sort.length(); i < count; i++) {
            if (i < count - 1) {
                sb.append(sort.charAt(i) + ",");
            } else {
                sb.append(sort.charAt(i));
            }
        }
        jpRequest.getJPGoodsDatas(id, sb.toString(), pageIndex, Constants.PAGE_COUNT, new RequestCallBack<JPFragmentGoodEntity>() {

            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<JPFragmentGoodEntity> responseInfo) {
                swipeRefreshLayout.setRefreshing(false);
                switch (responseInfo.result.code) {
                    case 1:
                        ArrayList<JPGoodsEntity> moreList = responseInfo.result.JPShopData.JPShopGoods;
                        if (pageIndex == 0) {
                            if (moreList == null || moreList.size() == 0) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.setNewData(moreList);
                                if (moreList.size() < Constants.PAGE_COUNT) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.loadMoreComplete();
                                }
                            }
                        } else {
                            if (moreList.size() < Constants.PAGE_COUNT) {
                                mAdapter.loadMoreEnd();
                            } else {
                                mAdapter.addData(moreList);
                                mAdapter.loadMoreComplete();
                            }
                        }
                        break;
                    default:
                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                swipeRefreshLayout.setRefreshing(false);
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void initData() {
        request.getFlagship(id, new RequestCallBack<JPFlagshipEntity>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<JPFlagshipEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        bean = responseInfo.result.data;
                        getedId = bean.supplierId;
                        Logger.i("getedId:" + getedId);
                        title = bean.supplierName;
                        tvTitle.setText(title);
                        GlideUtil.showImageNoSuffix(mContext, bean.imageUrl, ivTop);
                        tagStr = bean.noticeMark;
                        contentStr = bean.noticeContent;
                        mobile = bean.frequentPhone;
                        if (TextUtils.isEmpty(contentStr)) {
                            topLayout.setVisibility(View.GONE);
                        } else {
                            topLayout.setVisibility(View.VISIBLE);
                            if (!TextUtils.isEmpty(tagStr)) {
                                tvTag.setVisibility(View.VISIBLE);
                                tvTag.setText(tagStr);
                            } else {
                                tvTag.setVisibility(View.GONE);
                            }
                            tvDesc.setText(contentStr);
                        }


                        isCollect = bean.isCollect;
                        switch (isCollect) {
                            //0 表示未收藏
                            case 0:
                                collectBtn.setBackgroundResource(R.mipmap.flagship_to_collect);
                                break;
                            //1 表示已收藏
                            case 1:
                                collectBtn.setBackgroundResource(R.mipmap.flagship_not_collect);
                                break;
                            default:
                                collectBtn.setVisibility(View.GONE);
                        }

                        //载入时，同时获取分享有关数据
                        getShareUrl();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(e.getMessage());
            }
        });
    }

    private void initListener() {
        RxUtil.clicks(flContactService, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!TextUtils.isEmpty(mobile)) {
                    showServicePhoneWindow();
                }

            }
        });
        RxUtil.clicks(tvStoreIntroduction, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (null != bean) {
                    Intent intent = new Intent(mContext, StoreIntroductionActivity.class);
                    intent.putExtra("store_data", bean);
                    startActivity(intent);
                }
            }
        });
        /**
         * 返回头部监听
         */
        RxUtil.clicks(ivReturnTop, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                recyclerView.smoothScrollToPosition(0);
            }
        });
        /**
         * 列表滚动监听
         */
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (location == null) {
                    location = new int[2];
                }
                innerLayout.getLocationOnScreen(location);
                if (dy > 0 && location[1] - loc[1] <= titleHeight) {
                    stickyView.setVisibility(View.VISIBLE);
                    ivReturnTop.setVisibility(View.VISIBLE);
                }
                if (dy < 0 && location[1] - loc[1] > titleHeight) {
                    stickyView.setVisibility(View.GONE);
                    ivReturnTop.setVisibility(View.GONE);
                }
            }
        });
        /**
         * item点击监听
         */
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                JPGoodsEntity entity = (JPGoodsEntity) adapter.getItem(position);
                if (entity != null) {
                    goodId = entity.JPGoodsId;
                    Intent intent = new Intent(JPFlagshipActivity.this, JPNewCommDetailActivity.class);
                    intent.putExtra("filiale_id", entity.JPFilialeId);
                    intent.putExtra("goods_id", entity.JPGoodsId);
                    intent.putExtra("tags_name", entity.JPTagsName);
                    startActivity(intent);
                }
            }
        });
        RxUtil.clicks(topLayout, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                showNoticeDialog();
            }
        });

        /**
         * 价格排序监听
         */
        RxUtil.clicks(rbSortPrice, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                setPriceSort();
            }
        });
        RxUtil.clicks(rbStickyPrice, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                setPriceSort();
            }
        });


        /**
         *   是否有货 作为排序条件监听
         */
        cbSortHasData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbStickySortHasData.setChecked(isChecked);
                getHasGoods(isChecked);
            }
        });
        cbStickySortHasData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                getHasGoods(isChecked);
                cbSortHasData.setChecked(isChecked);
            }
        });


        /**
         * 默认筛选监听
         */
        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isChecked = cbSortHasData.isChecked();
                if (stickyView.getVisibility() == View.GONE) {
                    getSort(checkedId, isChecked);
                    rgSticky.check(checkedId);
                }
            }
        });

        rgSticky.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                boolean isChecked = cbStickySortHasData.isChecked();
                if (stickyView.getVisibility() == View.VISIBLE) {
                    getSort(checkedId, isChecked);
                    rgSort.check(checkedId);
                }
            }
        });

    }

    /**
     * 获取有货商品
     *
     * @param isChecked
     */
    private void getHasGoods(boolean isChecked) {
        if (isChecked) {
            cbStickySortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
            cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
            sort = sort.substring(0, sort.length() - 1) + "1";
        } else {
            cbStickySortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
            cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
            sort = sort.substring(0, sort.length() - 1) + "0";
        }
        refreshGetData();
    }

    private void showNoticeDialog() {
        AlertDialog builder = new AlertDialog.Builder(mContext).create();
        builder.setTitle(tagStr);
        builder.setMessage(contentStr);
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_red));
    }

    /**
     * 获取排序方式
     */
    private void getSort(int checkedId, boolean isChecked) {
        if (isChecked) {
            sort = "1";
        } else {
            sort = "0";
        }
        switch (checkedId) {
            case R.id.rb_sort_newest:
                lastCheckedRadioButton = R.id.rb_sort_newest;
                sort = "100" + sort;
                rbStickyPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                refreshGetData();
                break;
            //价格排序的监听不在此处进行，而是在价格按钮的单独监听里进行，防止重复请求数据
            case R.id.rb_sort_count:
                lastCheckedRadioButton = R.id.rb_sort_count;
                sort = "001" + sort;
                rbStickyPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                refreshGetData();
                break;
            default:
                break;
        }
    }

    /**
     * 价格作为排序条件时的sort处理
     */
    private void setPriceSort() {
        //处理sort之前先把逗号去掉，在进行网络请求时再加上
        sort = sort.replace(",", "");
        String substring = sort.substring(0, sort.length() - 1);
        String subEnd = sort.substring(sort.length() - 1);
        //点击价格时，之前字符串若是010+是否有货的0或1 则字符串改为000+是否有货的0或1，其他情况字符串都会改为010+是否有货的0或1
        if (substring.equals("010")) {
            sort = "000" + subEnd;
            rbStickyPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
            rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);

        } else {
            sort = "010" + subEnd;
            rbStickyPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
            rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
        }
        Logger.i("价格排序改变了 sort:" + sort);
        refreshGetData();
    }

    /**
     * 页数归零后重新查询数据
     */
    private void refreshGetData() {
        pageIndex = 0;
        getGoods();
    }

    private void initScrollView() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstData();
            }
        });
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextData();
            }
        }, recyclerView);
    }

    private void getShareUrl() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        jpRequest.getShareUrl("9", "", new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        String sub_title = responseInfo.result.subTitle;
                        share_content = responseInfo.result.content;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg = Constants.ImageUrl + share_img;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                showToast(R.string.net_work_not_available);
            }
        });
    }

    @Override
    public void onScroll(ScrollView scrollView, int x, int y, int oldx, int oldy) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                JPFlagshipActivity.this.finish();
                break;
            case R.id.v3Shop:
                Intent intent = new Intent(this, JPMainActivity.class);
                intent.putExtra("jpMainActivity", "jpShoppingCartFragment");
                startActivity(intent);
                break;
            case R.id.v3Share:
//                本处原本是分享，之后改为了联系客服
//                AliCustomerServiceChatUtils.openServiceChatByActivity(mContext,customerServicePersonId,customerServiceGroupId,id,"");
                share();
                break;
            case R.id.iv_collect:
                if (!isLogin()) {
                    startActivity(new Intent(JPFlagshipActivity.this, LeFenPhoneLoginActivity.class));
                    return;
                }
                if (isCollect == 0) {
                    collect();
                }
                if (isCollect == 1) {
                    collectCancle();
                }
            default:
                break;
        }
    }

    /**
     * 分享
     */
    private void share() {
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url)
                            .share();
                }
            });
        }
        mShareDialog.show();
    }

    /**
     * 收藏
     */
    private void collect() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        jpRequest.collectV3(id, COLLECTTYPE + "", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                collectBtn.setClickable(false);
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                collectBtn.setClickable(true);
                collectBtn.setBackgroundResource(R.mipmap.flagship_not_collect);
                isCollect = 1;
                showToast("收藏成功");
                //刷新个人页面标识
                sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                collectBtn.setClickable(true);
                stopMyDialog();
                showToast("收藏失败");
            }
        });
    }

    /**
     * 取消收藏
     */
    private void collectCancle() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        jpRequest.cancelCollectV3(id, COLLECTTYPE + "", "0", Constants.COLLECT_TYPE, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                collectBtn.setClickable(false);
                startMyDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                stopMyDialog();
                collectBtn.setClickable(true);
                collectBtn.setBackgroundResource(R.mipmap.flagship_to_collect);
                isCollect = 0;
                showToast("取消收藏成功");
                //刷新个人页面标识
                sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                collectBtn.setClickable(true);
                stopMyDialog();
                showToast("取消收藏失败");
            }
        });
    }

    /**
     * 展示客服电话弹窗
     */
    private void showServicePhoneWindow() {
        if (mPupwindow == null) {
            View contentView = View.inflate(mContext, R.layout.pup_custom_phone, null);
            mPupwindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mPupwindow.setContentView(contentView);
            tvServicePhone = contentView.findViewById(R.id.tv_service_phone);
            RxUtil.clicks(tvServicePhone, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    PhoneUtil.call(mobile,mContext);
                    dismissPup();
                }
            });
            RxUtil.clicks(contentView, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    dismissPup();
                }
            });
            mPupwindow.setOutsideTouchable(true);
            String formatPhone = PhoneUtil.formatPhone(mobile);
            String note = "客服电话" + formatPhone;
            SpannableString spannableString = new SpannableString(note);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#1AA2E9"));
            spannableString.setSpan(colorSpan, note.length() - formatPhone.length(), note.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvServicePhone.setText(spannableString);
        }
        mPupwindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }


    @Override
    public void onBackPressed() {
        if (!dismissPup()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (isFirst) {
            loc = new int[2];
            titleView.getLocationOnScreen(loc);
            titleHeight = titleView.getMeasuredHeight();
            isFirst = false;
        }

    }

}
