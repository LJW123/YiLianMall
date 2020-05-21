package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
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
import com.yilian.mall.adapter.ViewPagerAdapter;
import com.yilian.mall.entity.SnatchPartEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mall.widgets.SnatchDialog;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/6.
 */
public class ProductDetails extends BaseActivity implements ViewPager.OnPageChangeListener {

    private final int AUTO = 0;
    private final long delay = 3 * 1000;
    @ViewInject(R.id.viewpager)
    private ViewPager viewPager;
    @ViewInject(R.id.tv_description)
    private TextView description;
    @ViewInject(R.id.tv_share)
    private TextView share;
    @ViewInject(R.id.item_pro)
    private ProgressBar progressBar;
    @ViewInject(R.id.activity_play_count)
    private TextView playCount;
    @ViewInject(R.id.activity_total_count)
    private TextView totalCount;
    @ViewInject(R.id.activity_other_count)
    private TextView otherCount;
    @ViewInject(R.id.ll_points)
    private LinearLayout layoutDots;
    @ViewInject(R.id.tv_goods_name)
    private TextView goodsName;
    @ViewInject(R.id.tv_snatch)
    private TextView tvStatus;
    @ViewInject(R.id.listView)
    private NoScrollListView listView;
    @ViewInject(R.id.scrollview)
    private PullToRefreshScrollView scrollview;
    private UmengDialog mShareDialog;
    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private ViewPagerAdapter mViewPagerAdp;
    private ArrayList<SnatchPartEntity.Record> list = new ArrayList<>();
    private ArrayList<String> viewPagerUrl = new ArrayList<>();
    private ImageView[] mDots;
    private int width;
    private int newWidth;
    private int padding = 8;
    private int page = 0;

    private SnatchNetRequest snatchNetRequest;

    //刷新头部轮播图
    private Handler mHandler = new Handler() {

        @Override
        public void dispatchMessage(Message msg) {
            switch (msg.what) {
                case AUTO:
                    int index = viewPager.getCurrentItem();
                    viewPager.setCurrentItem(index + 1);
                    mHandler.sendEmptyMessageDelayed(AUTO, delay);
                    break;
            }
        }
    };
    private String activityId;
    private String goodId;
    private String total;
    private String status;//进行状态  1进行中  2已揭晓
    private String goodname;
    private int count;
    //dialog消失时刷新UI数据
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj == Constants.ACTIVITY_STATE_END) {
                getViewPager();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        ViewUtils.inject(this);
        listView.setFocusable(false);
        Intent intent = getIntent();
        activityId = intent.getStringExtra("activity_id");
        initView();
        initScrollView();
    }

    private void initScrollView() {
        scrollview.setMode(PullToRefreshBase.Mode.BOTH);

        scrollview.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {

            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                // TODO Auto-generated method stub

            }
        });
        scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page = 0;
                getViewPager();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page++;
                getViewPager();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getViewPager();
    }

    private void initView() {
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        options = new DisplayImageOptions.Builder().showStubImage(R.mipmap.jiazaizhong)
                .showImageForEmptyUri(R.mipmap.jiazaizhong).showImageOnFail(R.mipmap.jiazaizhong)
                        // 这里的三张状态用一张替代
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
        //参与记录list
        list = new ArrayList<>();

        width = getResources().getDisplayMetrics().widthPixels;
        newWidth = (int) (divideWidth(width, 1080, 6) * 17);
//        getViewPager();
    }

    public void onBack(View view) {
        finish();
    }

    /**
     * 跳转到参与记录界面
     *
     * @param view
     */
    public void jumpToPartRecord(View view) {
        if (isLogin()) {
            Intent intent = new Intent(ProductDetails.this, SnatchMyRecording.class);
            intent.putExtra("activity_id", activityId);
            intent.putExtra("status", status);
            intent.putExtra("goods_name", goodname);
            startActivity(intent);
        } else {
            startActivity(new Intent(ProductDetails.this, LeFenPhoneLoginActivity.class));
        }

    }

    /**
     * 分享
     *
     * @param view
     */
    public void toShare(View view) {
        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    String content = getResources().getString(R.string.appshare);
                    new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, Ip.SHARE_URL).share();

                }
            });

        }
        mShareDialog.show();

    }

    /**
     * 跳转到图片详情界面
     *
     * @param view
     */
    public void jumpToImgDetail(View view) {
        Intent intent = new Intent(ProductDetails.this, WebViewActivity.class);
        intent.putExtra("title_name", "商品图文详情");
        intent.putExtra("url", Ip.getWechatURL(mContext) + "m/goods/snatchPic.php?goods_id=" + goodId);
        startActivity(intent);
    }


    /**
     * 弹出选择夺宝号对话框
     *
     * @param view
     */
    public void showSnatchDialog(View view) {
        //进行状态  1进行中  2已揭晓

        if (status == null){
            return;
        }

        switch (status) {
            case "1"://去参加夺宝
                if (isLogin()) {
                    SnatchDialog dialog = new SnatchDialog(mContext, activityId, goodname, count, handler);
                    dialog.show();
                } else {
                    startActivity(new Intent(ProductDetails.this, LeFenPhoneLoginActivity.class));
                }

                break;
            case "2"://查看夺宝详情
                if (isLogin()) {
                    Intent intent = new Intent(ProductDetails.this, SnatchSuccess.class);
                    intent.putExtra("totalCount", total);
                    intent.putExtra("activityId", activityId);
                    intent.putExtra("status", status);
                    intent.putExtra("goods_name", goodname);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(ProductDetails.this, LeFenPhoneLoginActivity.class));
                }

                break;

        }

    }

    /**
     * @author 初始化ViewPager的底部小点
     */
    private void initDots() {

        if (viewPagerUrl.size() <= 0)
            return;

        mDots = new ImageView[viewPagerUrl.size()];


        for (int i = 0; i < viewPagerUrl.size(); i++) {
            ImageView iv = new ImageView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(newWidth, newWidth);
            lp.leftMargin = padding;
            lp.rightMargin = padding;
            lp.topMargin = padding;
            lp.bottomMargin = padding;

            iv.setLayoutParams(lp);
            iv.setImageResource(R.mipmap.circle_off);
            layoutDots.addView(iv);
            mDots[i] = iv;
        }
        mDots[0].setImageResource(R.mipmap.circle_on);
    }

    public double divideWidth(int screenWidth, int picWidth, int retainValue) {
        BigDecimal screenBD = new BigDecimal(Double.toString(screenWidth));
        BigDecimal picBD = new BigDecimal(Double.toString(picWidth));
        return screenBD.divide(picBD, retainValue, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 获取viewpager网络url
     */
    public void getViewPager() {
        startMyDialog();
        if (snatchNetRequest == null) {
            snatchNetRequest = new SnatchNetRequest(mContext);
        }
        snatchNetRequest.snatchingNet(activityId, page, new RequestCallBack<SnatchPartEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchPartEntity> responseInfo) {
                Logger.json(responseInfo.result.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        scrollview.onRefreshComplete();
                        Logger.json(responseInfo.result.toString());
                        if (page == 0) {
                            list.clear();
                        }
                        list.addAll(responseInfo.result.log0);
                        PartRecordAdapter adapter = new PartRecordAdapter(mContext, list);
                        listView.setAdapter(adapter);
                        viewPagerUrl.clear();
                        viewPagerUrl.addAll(responseInfo.result.activity.goodsAlbum);
                        layoutDots.removeAllViews();
                        mHandler.removeMessages(AUTO);
                        initViewPager(viewPagerUrl);
                        initDots();
                        goodId = responseInfo.result.activity.goodsId.toString();
                        goodname = responseInfo.result.activity.goodsName;
                        status = responseInfo.result.status;
                        total = responseInfo.result.activity.totalCount + "";
                        count = responseInfo.result.activity.joinCount;

                        totalCount.setText(responseInfo.result.activity.totalCount + "");
                        playCount.setText(responseInfo.result.activity.joinCount + "");
                        otherCount.setText((responseInfo.result.activity.totalCount - responseInfo.result.activity.joinCount) + "");
                        progressBar.setMax(responseInfo.result.activity.totalCount);
                        progressBar.setProgress(count);
                        goodsName.setText(responseInfo.result.activity.goodsName);
                        description.setText(responseInfo.result.activity.activityName);

                        //进行状态  1进行中  2已揭晓
                        if (status.equals("1")) {
                            tvStatus.setText("去参与夺宝");
                        }
                        if (status.equals("2")) {
                            tvStatus.setText("查看夺宝详情");
                        }

                        break;
                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                scrollview.onRefreshComplete();
                showToast(R.string.net_work_not_available);
            }
        });
    }


    /**
     * @author 初始化ViewPager
     */
    private void initViewPager(ArrayList<String> viewPagerUrl) {
        int imgSize;
        if (viewPagerUrl.size() <= 0) {
            viewPager.setVisibility(View.GONE);
            return;
        }
        if (viewPagerUrl.size() <= 3) {
            imgSize = viewPagerUrl.size() * 4;
        } else {
            imgSize = viewPagerUrl.size();
        }
        ImageView[] imgs = new ImageView[imgSize];
        for (int i = 0; i < imgs.length; i++) {
            ImageView iv = new ImageView(mContext);

            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.FIT_CENTER);

            final String url = viewPagerUrl.get(i % viewPagerUrl.size());

            imageLoader.displayImage(Constants.ImageUrl + url, iv, options, new SimpleImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
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

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {

                    ((ImageView) view).setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            });
            // iv.setImageBitmap(bitmaps.get(i%urlString.size()));
            imgs[i] = iv;
        }

        mViewPagerAdp = new ViewPagerAdapter(imgs);
        viewPager.setAdapter(mViewPagerAdp);
        mHandler.sendEmptyMessageDelayed(AUTO, delay);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int count = viewPagerUrl.size();
        if (count > 0) {
            setCurrentDot(position % count);
        }
    }

    /**
     * 设置ViewPager当前的底部小点
     */
    private void setCurrentDot(int currentPosition) {

        for (int i = 0; i < mDots.length; i++) {
            if (i == currentPosition) {
                mDots[i].setImageResource(R.mipmap.circle_on);
            } else {
                mDots[i].setImageResource(R.mipmap.circle_off);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


    /**
     * listview适配器
     */

    public class PartRecordAdapter extends BaseAdapter {

        ViewHolder holder;
        private List<SnatchPartEntity.Record> list;
        private Context context;
        private BitmapUtils bitmapUtils;

        public PartRecordAdapter(Context context, List list) {
            this.context = context;
            this.list = list;
            bitmapUtils=new BitmapUtils(context);
        }

        @Override
        public int getCount() {
            if (list != null)
                return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list != null) {
                return list.get(position);
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_part_record, null);
                holder.textView = (TextView) convertView.findViewById(R.id.view);
                holder.imageView = (JHCircleView) convertView.findViewById(R.id.img_photo);
                holder.tvPhone = (TextView) convertView.findViewById(R.id.tv_phone);
                holder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (list != null) {
                if (!TextUtils.isEmpty(list.get(position).photo)) {
                    if (list.get(position).photo.contains("http://")||list.get(position).photo.contains("https://")) {
                        bitmapUtils.display(holder.imageView, list.get(position).photo);
                    } else {
                        bitmapUtils.display(holder.imageView, Constants.ImageUrl + list.get(position).photo + Constants.ImageSuffix);
                    }
                }
                String phone = list.get(position).phone;
                if (phone != null&& CommonUtils.isPhoneNumer(phone)) {
                    String newphone = phone.substring(0, 3) + "****" + phone.substring(7, 11);
                    holder.tvPhone.setText(newphone);
                }
                holder.tvTime.setText(StringFormat.formatDateS(list.get(position).joinTime));
//                if ((position + 1) % 3 != 0) {
//                    holder.textView.setBackgroundColor(0x562479);
//                }
            }
            return convertView;
        }
    }

    class ViewHolder {
        TextView textView;
        JHCircleView imageView;
        TextView tvPhone;
        TextView tvTime;
    }
}
