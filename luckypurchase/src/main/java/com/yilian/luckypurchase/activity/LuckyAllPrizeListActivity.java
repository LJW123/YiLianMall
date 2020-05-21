package com.yilian.luckypurchase.activity;


import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.adapter.LuckyGoodsListAdapter;
import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.networkingmodule.entity.LuckyGoodsListEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author  幸运购全部进行中活动列表
 */
public class LuckyAllPrizeListActivity extends BaseAppCompatActivity implements View.OnClickListener {

    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View includeTitle;
    private View includeSearchView;
    private boolean isSearch;
    private TextView tvFinish;
    private EditText etKeyword;
    private TextView tvSeach;
    private String keyWord = "";
    private LuckyGoodsListAdapter luckyAdapter;
    private ImageView ivReturnTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lucky_activity_all_prize_list);
        initView();
        initData();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSearch) {
//            如果是搜索界面，第一次进来时不查询数据
            return;
        }
        getFirstPageData();
    }

    private void initView() {
        isSearch = getIntent().getBooleanExtra("isSearch", false);

        ivReturnTop = (ImageView) findViewById(R.id.iv_return_top);
        includeTitle = findViewById(R.id.include_title);
        includeSearchView = findViewById(R.id.include_search_view);
        if (isSearch) {
            includeSearchView.setVisibility(View.VISIBLE);
            includeTitle.setVisibility(View.GONE);
        } else {
            includeSearchView.setVisibility(View.GONE);
            includeTitle.setVisibility(View.VISIBLE);
        }
        tvFinish = (TextView) findViewById(R.id.tv_finish);
        tvFinish.setOnClickListener(this);
        etKeyword = (EditText) findViewById(R.id.ed_keyword);
        etKeyword.setOnClickListener(this);
        tvSeach = (TextView) findViewById(R.id.tv_seach);
        tvSeach.setOnClickListener(this);

        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("全部");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setImageResource(R.mipmap.library_module_v3_more_bottom);
        if (isSearch) {
            v3Shop.setVisibility(View.GONE);
        } else {
            v3Shop.setVisibility(View.VISIBLE);
        }
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
        luckyAdapter = new LuckyGoodsListAdapter(R.layout.lucky_item_prize_progress, false);
        recyclerView.setAdapter(luckyAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        if (isSearch) {
            swipeRefreshLayout.setEnabled(false);//初始化禁用下拉刷新
        }
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
        ivReturnTop.setOnClickListener(this);
    }

    private void initData() {
        if (isSearch) {
//            如果是搜索界面，第一次进来时不查询数据
            return;
        }
        swipeRefreshLayout.setRefreshing(true);
        getFirstPageData();
    }

    /**
     * recyclerView滑动高度
     */
    int scrollDy = 0;

    private void initListener() {
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    getFirstPageData();
                }
                return false;
            }
        });
        luckyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LuckyGoodsListEntity.ListBean adapterItem = (LuckyGoodsListEntity.ListBean) adapter.getItem(position);
                Intent intent = new Intent(mContext, LuckyActivityDetailActivity.class);
                intent.putExtra("activity_id", adapterItem.activityId);
                intent.putExtra("type", "0");
                startActivity(intent);
            }
        });
        if (isSearch) {
            etKeyword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().length() <= 0) {
                        swipeRefreshLayout.setEnabled(false);
                    } else {
                        swipeRefreshLayout.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        } else {
            swipeRefreshLayout.setEnabled(true);
        }
        luckyAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getNextPageData();
            }
        }, recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                scrollDy += dy;
                int screenHeight3 = ScreenUtils.getScreenHeight(mContext) * 3;
                if (scrollDy > screenHeight3) {
                    ivReturnTop.setVisibility(View.VISIBLE);
                } else {
                    ivReturnTop.setVisibility(View.GONE);
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
    }

    int page = 0;

    private void getFirstPageData() {
        page = 0;
        getData();
    }

    private void getNextPageData() {
        page++;
        getData();
    }

    /**
     * 获取幸运购活动列表数据
     */
    void getData() {
        if (isSearch) {
            searchLuckyData();
        } else {
            getLuckyData();
        }
    }

    @SuppressWarnings("unchecked")
    private void getLuckyData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getAllLuckyGoodsListData("snatch/snatch_list_all", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext),
                        page, Constants.PAGE_COUNT)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        swipeRefreshLayout.setRefreshing(false);
                        Logger.i("swipe走了这里1");
                        showToast(e.getMessage());
                        luckyAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(LuckyGoodsListEntity luckyGoodsListEntity) {
                        List<LuckyGoodsListEntity.ListBean> list = luckyGoodsListEntity.list;
                        setData(list);
                    }
                });
        addSubscription(subscription);
    }

    private void setData(List<LuckyGoodsListEntity.ListBean> list) {
        if (page <= 0) {
            if (null == list || list.size() <= 0) {
                luckyAdapter.setEmptyView(R.layout.library_module_no_data);
//                                处理Invalid view holder adapter positionViewHolder
                luckyAdapter.notifyDataSetChanged();
            } else {
                luckyAdapter.setNewData(list);
                if (list.size() < Constants.PAGE_COUNT) {
                    luckyAdapter.loadMoreEnd();
                } else {
                    luckyAdapter.loadMoreComplete();
                }
            }
        } else {
            if (list.size() >= Constants.PAGE_COUNT) {
                luckyAdapter.loadMoreComplete();
            } else {
                luckyAdapter.loadMoreEnd();
            }
            luckyAdapter.addData(list);
        }
    }

    @SuppressWarnings("unchecked")
    private void searchLuckyData() {
        keyWord = etKeyword.getText().toString();
        if (TextUtils.isEmpty(keyWord)) {
            showToast("请输入搜索关键字");
            return;
        }
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getPrizeList("snatch/snatch_search", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.getToken(mContext), keyWord, String.valueOf(page), String.valueOf(Constants.PAGE_COUNT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LuckyGoodsListEntity>() {
                    @Override
                    public void onCompleted() {
                        swipeRefreshLayout.setRefreshing(false);
                        Logger.i("搜索幸运购走了这里： onCompleted：");
                        dismissInputMethod();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.i("搜索幸运购走了这里： throwable：" + e.getMessage());
                        swipeRefreshLayout.setRefreshing(false);
                        Logger.i("swipe走了这里1");
                        showToast(e.getMessage());
                        luckyAdapter.loadMoreFail();
                    }

                    @Override
                    public void onNext(LuckyGoodsListEntity luckyGoodsListEntity) {
                        Logger.i("搜索幸运购走了这里： luckyGoodsListEntity：" + luckyGoodsListEntity);
                        List<LuckyGoodsListEntity.ListBean> list = luckyGoodsListEntity.list;
                        if (page <= 0) {
                            if (null == list || list.size() <= 0) {
                                Logger.i("搜索幸运购走了这里： luckyGoodsListEntity1：" + list.size());
                                luckyAdapter.setNewData(list);
                                luckyAdapter.setEmptyView(R.layout.library_module_no_data);
                                luckyAdapter.notifyDataSetChanged();
                            } else {
                                Logger.i("搜索幸运购走了这里： luckyGoodsListEntity2：" + list.size());
                                luckyAdapter.setNewData(list);
                            }
                        } else {
                            if (list.size() >= Constants.PAGE_COUNT) {
                                luckyAdapter.loadMoreComplete();
                            } else {
                                luckyAdapter.loadMoreEnd();
                            }
                            luckyAdapter.addData(list);
                        }
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
        } else if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.tv_finish) {
            finish();
        } else if (i == R.id.tv_seach) {
            getFirstPageData();
        } else if (i == R.id.v3Shop) {
            showMenu();
        } else if (i == R.id.iv_return_top) {
            recyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 右上角更多
     */
    private void showMenu() {
        final PopupMenu popupMenu = new PopupMenu(this, false);
        popupMenu.showLocation(R.id.v3Shop);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (sp.getBoolean(Constants.SPKEY_STATE, false)) {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                startActivity(intent);
            }
        });
    }
}
