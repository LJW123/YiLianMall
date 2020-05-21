package com.yilian.mall.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.adapter.decoration.DividerGridItemDecoration;
import com.yilian.mall.widgets.TextViewExpandableAnimation;
import com.yilian.mylibrary.BannerViewGlideUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.FileUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.event.EventDownLoadProgress;
import com.yilian.mylibrary.event.EventGameDownloadStatusChanged;
import com.yilian.mylibrary.widget.OutLineTextView;
import com.yilian.networkingmodule.entity.GameEntity_Android;
import com.yilian.networkingmodule.entity.GameListEntity;
import com.yilian.networkingmodule.entity.GameListHeaderEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.activity.BaseActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class GameHomePageActivity extends BaseActivity implements BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    private RecyclerView recyclerView;
    private int page = 0;
    private GameListAdapter gameListAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View errorView;
    private List<GameListHeaderEntity.BannerBean> bannerDatas;
    private Banner headerView;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private View emptyView;
    public ProgressBar progressBar;
    public Button btnEnterGame;
    private ImageView v3Back;
    private int gameDownloadPosition = -1;
    private OutLineTextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_home_page);
        initView();
        initData();
        initListener();
    }

    private void initData() {
        getFirstPageData();
    }

    private void initListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        gameListAdapter.setOnLoadMoreListener(this, recyclerView);
    }

    private void initView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        if (gameListAdapter == null) {
            gameListAdapter = new GameListAdapter(R.layout.item_game_list);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        DividerGridItemDecoration decor = new DividerGridItemDecoration(mContext, 2, R.color.color_bg);
        recyclerView.addItemDecoration(decor);
        gameListAdapter.bindToRecyclerView(recyclerView);
//        recyclerView.setAdapter(gameListAdapter);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.color_red));
        v3Left = (ImageView) findViewById(R.id.v3Left);
//        v3Left.setOnClickListener(this);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("游戏");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setOnClickListener(this);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setOnClickListener(this);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setOnClickListener(this);
        tvRight2.setText("游戏记录");
        tvRight2.setVisibility(View.VISIBLE);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setOnClickListener(this);
    }


    void getFirstPageData() {
        swipeRefreshLayout.setRefreshing(true);
        headerDataCancel = false;
        gameListDataCancel = false;
        page = 0;
        gameDownloadPosition = -1;//刷新时，将下载信息重置。
        adapterGameDownloadStatusChangeNotifyed = false;
        getGameList();
        getBannerData();
    }

    private void getNextPageData() {
        page++;
        gameListDataCancel = false;
        headerDataCancel = true;
//        getGameList();
    }

    boolean headerDataCancel = false;
    boolean gameListDataCancel = false;

    void swipeRefreshLayoutCancelRefreshing() {
        if (headerDataCancel && gameListDataCancel) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @SuppressWarnings("unchecked")
    void getBannerData() {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).getGameListHeaderData("getBannerList")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GameListHeaderEntity>() {

                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                        headerDataCancel = true;
                        swipeRefreshLayoutCancelRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        headerDataCancel = true;
//                        swipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayoutCancelRefreshing();
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(GameListHeaderEntity gameListHeaderEntity) {
                        bannerDatas = gameListHeaderEntity.banner;
                        if (headerView == null) {
                            headerView = (Banner) View.inflate(mContext, R.layout.library_module_banner, null);
//                          headerView = new Banner(mContext);
                            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) headerView.getLayoutParams();
                            if (layoutParams == null) {
                                layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (ScreenUtils.getScreenWidth(mContext) / 2.6));
                            }
                            layoutParams.bottomMargin = DPXUnitUtil.dp2px(mContext, 8);
                            headerView.setLayoutParams(layoutParams);
                        }
                        ArrayList<String> images = new ArrayList<String>();
                        for (int i = 0; i < bannerDatas.size(); i++) {
                            String bannerPhoto = bannerDatas.get(i).bannerPhoto;
                            Logger.i("bannerPhoto:" + bannerPhoto);
                            images.add(WebImageUtil.getInstance().getWebImageUrlNOSuffix(bannerPhoto));
//                            images.add(bannerPhoto);
                        }
                        headerView.setImages(images)
                                .setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                                .setImageLoader(new BannerViewGlideUtil())
                                .setIndicatorGravity(BannerConfig.CENTER)
                                .setOnBannerListener(new OnBannerListener() {
                                    @Override
                                    public void OnBannerClick(int position) {
//                                        showToast("点击了：" + position);
                                    }
                                })
                                .start();
                        if (gameListAdapter.getHeaderLayoutCount() <= 0) {
                            gameListAdapter.addHeaderView(headerView);
                            gameListAdapter.notifyDataSetChanged();
                        }
                    }
                });
        addSubscription(subscription);
    }

    @SuppressWarnings("unchecked")
    void getGameList() {
        Subscription subscribe = RetrofitUtils3.getRetrofitService(mContext).getGameList("gameList", String.valueOf(page), String.valueOf(Constants.PAGE_COUNT), "1")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<GameListEntity>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                        gameListDataCancel = true;
                        swipeRefreshLayoutCancelRefreshing();
                    }

                    @Override
                    public void onError(Throwable e) {
                        gameListDataCancel = true;
                        gameListAdapter.loadMoreFail();
                        swipeRefreshLayoutCancelRefreshing();
//                        swipeRefreshLayout.setRefreshing(false);
                        if (page > 0) {
                            page--;
                        } else {
                            if (errorView == null) {
                                errorView = View.inflate(mContext, R.layout.library_module_load_error, null);
                            }
                            errorView.findViewById(R.id.tv_refresh).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    getFirstPageData();
                                }
                            });
                            gameListAdapter.setEmptyView(errorView);
                        }
                        showToast(e.getMessage());
                    }

                    @Override
                    public void onNext(GameListEntity gameListEntity) {
                        List<GameListEntity.DataBean> data = gameListEntity.data;
                        if (page == 0) {
                            if (data.size() <= 0) {
                                if (emptyView == null) {
                                    emptyView = View.inflate(mContext, R.layout.library_module_no_data, null);
                                }
                                gameListAdapter.setEmptyView(emptyView);
                            } else {
                                if (data.size() < Constants.PAGE_COUNT) {
                                    gameListAdapter.loadMoreEnd();
                                }
                                gameListAdapter.setNewData(data);
                            }
                        } else {
                            gameListAdapter.addData(data);
                        }
                        if (data.size() < Constants.PAGE_COUNT) {
                            gameListAdapter.loadMoreEnd();
                        } else {
                            gameListAdapter.loadMoreComplete();
                        }
                    }
                });
        addSubscription(subscribe);
    }

    @Override
    public void onLoadMoreRequested() {
        getNextPageData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:
                startActivity(new Intent(mContext, GameHistoryListActivity.class));
                break;
        }
    }

    /**
     * 游戏下载状态改变后，adapter是否刷新过
     */
    boolean adapterGameDownloadStatusChangeNotifyed;
    boolean gameDownloadFinish;
    int gameDownloadProgress;//游戏下载进度

    @Subscribe
    public void onMessageEvent(EventDownLoadProgress eventDownLoadProgress) {
        Logger.i("下载游戏收到了消息");
        gameDownloadPosition = eventDownLoadProgress.position;
        Logger.i("gameDownloadPosition  0 :" + gameDownloadPosition);
        if (!adapterGameDownloadStatusChangeNotifyed) {
            adapterGameDownloadStatusChangeNotifyed = !adapterGameDownloadStatusChangeNotifyed;
            gameListAdapter.notifyItemChanged(gameDownloadPosition);
        }
        if (progressBar != null) {
            progressBar.setProgress(eventDownLoadProgress.progress);
        }
        if (tvProgress != null) {
            tvProgress.setText(eventDownLoadProgress.progress + "%");
            tvProgress.getOutLineTextView().setText(eventDownLoadProgress.progress + "%");
        }
        String downloadPath = eventDownLoadProgress.downloadPath;
        gameDownloadProgress = eventDownLoadProgress.progress;
        if (eventDownLoadProgress.progress == 100) {
            gameDownloadFinish = true;
            gameListAdapter.notifyItemChanged(gameDownloadPosition);//下载完毕后再次刷新，保证按钮状态正常
//            gameDownloadPosition = -1;//下载完毕后，取消条目的下载状态

            btnEnterGame.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            tvProgress.setVisibility(View.GONE);
            btnEnterGame.setBackgroundResource(R.drawable.library_module_bg_style_red_3_0);
            btnEnterGame.setTextColor(Color.WHITE);
        }
//        downloadProgress = eventDownLoadProgress.progress;
//        gameListAdapter.notifyItemChanged(gameDownloadPosition);
    }

    @Subscribe
    @Override
    public void onMessageGameDownloadStatusChanged(EventGameDownloadStatusChanged eventGameDownloadStatusChanged) {
        adapterGameDownloadStatusChangeNotifyed = eventGameDownloadStatusChanged.isChanged;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //处理安装完毕、卸载游戏后，回到该界面，状态按钮不改变的问题
//        if (gameListAdapter != null && gameDownloadPosition >= 0) {
        if (gameListAdapter != null ) {
            gameListAdapter.notifyDataSetChanged();
        }
    }

    class GameListAdapter extends BaseQuickAdapter<GameListEntity.DataBean, BaseViewHolder> {

        public GameListAdapter(@LayoutRes int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, GameListEntity.DataBean item) {
            GlideUtil.showImageWithSuffix(mContext, item.icon, helper.getView(R.id.iv_game_icon));
            helper.setText(R.id.tv_game_name, item.name);
            long playCount = item.playCount;
            if (playCount / 10000 <= 0) {
                helper.setText(R.id.tv_player_counts, "最近有" + playCount + "人在玩");
            } else {
                helper.setText(R.id.tv_player_counts, "最近有" + playCount / 10000 + "万+人在玩");
            }
            long downloadCount = item.downloadCount;
            if (downloadCount / 10000 <= 0) {
                helper.setText(R.id.tv_download_counts, downloadCount + "次下载");
            } else {
                helper.setText(R.id.tv_download_counts, downloadCount / 10000 + "万+人次下载");
            }
            TextViewExpandableAnimation textViewExpandableAnimation = helper.getView(R.id.tv_game_recommend);
            textViewExpandableAnimation.setText(item.desc);
            textViewExpandableAnimation.resetState(true);
            String androidPackage = item.androidPackage;

            GameEntity_Android gameEntity_android = new Gson().fromJson(androidPackage, GameEntity_Android.class);
            boolean h5Exist = false;//是否存在h5页面
            boolean androidExist = false;//是否已经安装了游戏
            boolean apkDownloaded = false;//安装包是否已经下载
            boolean androidDownloadExist = false;//Android下载地址是否存在
//            checkH5Exist(item);
            if (!TextUtils.isEmpty(item.html5Url)) {
//                        存在h5游戏页面
                h5Exist = true;
            } else {
//                        不存在h5游戏页面
                h5Exist = false;
            }
//            checkGameApkExist(gameEntity_android);
            try {
                if (TextUtils.isEmpty(gameEntity_android.packageName)) {
                    androidExist = false;
                } else {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(gameEntity_android.packageName, 0);
                    if (packageInfo == null) {
//                            应用未安装
                        androidExist = false;
                    } else {
//                            应用已安装
                        androidExist = true;
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                androidExist = false;
                e.printStackTrace();
            }

            String downloadPath = Environment.getExternalStorageDirectory() + Constants.GAME_PATH + item.name + ".apk";//游戏下载路径
            File file = new File(downloadPath);
            if (file.exists()) {
                apkDownloaded = true;
            } else {
                apkDownloaded = false;
            }
            String downloadUrl = gameEntity_android.url;
            if (TextUtils.isEmpty(downloadUrl)) {
//                        没有游戏下载地址
                androidDownloadExist = false;
            } else {
//                        有游戏下载地址
                androidDownloadExist = true;
            }
            Button btnEnterGame = helper.getView(R.id.btn_enter_game);
            helper.setText(R.id.btn_enter_game, "进入");
            if (androidExist) {//如果游戏已经安装
                btnEnterGame.setBackgroundResource(R.drawable.library_module_bg_style_red_3_0);
                btnEnterGame.setTextColor(Color.WHITE);
            } else {
                btnEnterGame.setBackgroundResource(R.drawable.library_module_bg_red_stroke_gray_solider);
                btnEnterGame.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
            }
            ProgressBar progressBar = helper.getView(R.id.progressBar);
            OutLineTextView tvProgress = helper.getView(R.id.tv_progress);
            Logger.i("gameDownloadPosition:" + gameDownloadPosition + "  helper.getAdapterPosition():" + helper.getAdapterPosition());
            if (gameDownloadPosition == helper.getAdapterPosition()) {//如果是正在下载的条目
                GameHomePageActivity.this.btnEnterGame = btnEnterGame;
                GameHomePageActivity.this.progressBar = progressBar;
                GameHomePageActivity.this.tvProgress = tvProgress;

//                progressBar.setProgress(downloadProgress);
                if (gameDownloadProgress == 100) {
                    showEnterBtn(btnEnterGame, progressBar, tvProgress);
                } else {

                    showProgressBar(btnEnterGame, progressBar, tvProgress, View.VISIBLE, View.GONE);
                }
            } else {
                showEnterBtn(btnEnterGame, progressBar, tvProgress);
            }
            if (androidExist) {//如果已经下载并安装了游戏,那么直接打开游戏客户端
                btnEnterGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGameApp(gameEntity_android, item);

                    }
                });
            } else {//没有安装游戏
                if (apkDownloaded) {//如果下载了没有安装
                    btnEnterGame.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //使用apk包 打开安装界面
                            FileUtils.openApk(mContext, downloadPath);
                        }
                    });
                } else {//如果没有下载
                    if (androidDownloadExist) {//如果有android下载地址
                        btnEnterGame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //                            弹出选择界面，选择界面处理进入h5或下载页面的逻辑
                                //TODO
                                Intent intent = new Intent(mContext, GameEnterWayChooseActivity.class);
                                intent.putExtra("gameEntity_android", gameEntity_android);
                                intent.putExtra("game_name", item.name);
                                intent.putExtra("h5Url", item.html5Url);
                                intent.putExtra("position", helper.getAdapterPosition());
                                startActivity(intent);
                            }
                        });
                    } else if (!androidDownloadExist && h5Exist) {//只有h5游戏页面
                        btnEnterGame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //                            直接打开h5游戏界面
                                Intent intent = new Intent(mContext, WebViewActivity.class);
                                intent.putExtra(Constants.SPKEY_URL, item.html5Url);
                                startActivity(intent);
                            }
                        });
                    } else if (!androidDownloadExist && !h5Exist) {//什么都没有
                        btnEnterGame.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //                            提示用户没有游戏信息
                                showToast("暂无游戏信息");
                            }
                        });
                    }
                }

            }
//            (btnEnterGame).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (androidExist) {//如果已经下载了游戏,那么直接打开游戏客户端
//                        openGameApp(gameEntity_android, item);
//                    } else {//没有下载游戏
//                        if (androidDownloadExist) {//如果有android下载地址
////                            弹出选择界面，选择界面处理进入h5或下载页面的逻辑
//                            //TODO
//                            Intent intent = new Intent(mContext, GameEnterWayChooseActivity.class);
//                            intent.putExtra("gameEntity_android", gameEntity_android);
//                            intent.putExtra("game_name", item.name);
//                            intent.putExtra("h5Url", item.html5Url);
//                            startActivity(intent);
//                        } else if (!androidDownloadExist && h5Exist) {//只有h5游戏页面
////                            直接打开h5游戏界面
//                            Intent intent = new Intent(mContext, WebViewActivity.class);
//                            intent.putExtra(Constants.SPKEY_URL, item.html5Url);
//                            startActivity(intent);
//                        } else if (!androidDownloadExist && !h5Exist) {//什么都没有
////                            提示用户没有游戏信息
//                            showToast("暂无游戏信息");
//                        }
//
//                    }
//                }
//            });
        }

        /**
         * 显示进度条
         *
         * @param btnEnterGame
         * @param progressBar
         * @param tvProgress
         * @param visible
         * @param gone
         */
        private void showProgressBar(Button btnEnterGame, ProgressBar progressBar, OutLineTextView tvProgress, int visible, int gone) {
            progressBar.setVisibility(visible);
            btnEnterGame.setVisibility(gone);
            tvProgress.setVisibility(visible);
        }

        /**
         * 显示进入游戏按钮
         *
         * @param btnEnterGame
         * @param progressBar
         * @param tvProgress
         */
        private void showEnterBtn(Button btnEnterGame, ProgressBar progressBar, OutLineTextView tvProgress) {
            showProgressBar(btnEnterGame, progressBar, tvProgress, View.GONE, View.VISIBLE);
        }

        /**
         * 打开游戏客户端
         *
         * @param gameEntity_android
         * @param item
         */
        private void openGameApp(GameEntity_Android gameEntity_android, GameListEntity.DataBean item) {
            if (!TextUtils.isEmpty(gameEntity_android.startActivityName)) {
                Intent intent = new Intent();
                ComponentName cn = new ComponentName(gameEntity_android.packageName, gameEntity_android.startActivityName);
                intent.setComponent(cn);
                intent.putExtra("jwt", PreferenceUtils.readStrConfig(Constants.JWT, mContext, ""));
                intent.putExtra("jwt_expires_in", PreferenceUtils.readStrConfig(Constants.JWT_EXPIRES_IN, mContext, ""));
                intent.putExtra("game_id", item.gameId);
                intent.putExtra("user_id", PreferenceUtils.readStrConfig(Constants.USER_ID, mContext));
                Logger.i("jwt:" + PreferenceUtils.readStrConfig(Constants.JWT, mContext, "") + " jwt_expires_in:" + PreferenceUtils.readStrConfig(Constants.JWT_EXPIRES_IN, mContext, "")
                        + "game_id:" + item.gameId);
                startActivity(intent);
            } else if (!TextUtils.isEmpty(gameEntity_android.scheme)) {
                Uri uri = Uri.parse(gameEntity_android.scheme);
                Intent intent = new Intent(Intent.ACTION_MAIN, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                showToast("游戏打开失败,请回到桌面打开游戏");
            }
        }
    }

//    /**
//     * 检查是否存在apk下载地址
//     *
//     * @param gameEntity_android
//     */
//    private void checkApkDownloadUrl(GameEntity_Android gameEntity_android) {
//        String downloadUrl = gameEntity_android.url;
//        if (TextUtils.isEmpty(downloadUrl)) {
////                        没有游戏下载地址
//            androidDownloadExist = false;
//        } else {
////                        有游戏下载地址
//            androidDownloadExist = true;
//        }
//    }
//
//    /**
//     * 检查是否安装了游戏apk
//     *
//     * @param gameEntity_android
//     */
//    private void checkGameApkExist(GameEntity_Android gameEntity_android) {
//        try {
//            if (TextUtils.isEmpty(gameEntity_android.packageName)) {
//                androidExist = false;
//            } else {
//                PackageInfo packageInfo = getPackageManager().getPackageInfo(gameEntity_android.packageName, 0);
//                if (packageInfo == null) {
////                            应用未安装
//                    androidExist = false;
//                } else {
////                            应用已安装
//                    androidExist = true;
//                }
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            androidExist = false;
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 检查是否存在h5游戏
//     *
//     * @param item
//     */
//    private void checkH5Exist(GameListEntity.ListBean item) {
//        if (!TextUtils.isEmpty(item.html5Url)) {
////                        存在h5游戏页面
//            h5Exist = true;
//        } else {
////                        不存在h5游戏页面
//            h5Exist = false;
//        }
//    }
}
