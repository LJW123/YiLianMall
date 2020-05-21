package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.yilian.mall.adapter.BaseAdapter;
import com.yilian.mall.adapter.MemberAdapter;
import com.yilian.mall.adapter.layoutManager.FullyGridLayoutManager;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.entity.MemberLevel1Entity;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.InvateDialog;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/25.
 * 二级会员界面
 */
public class MembersLevel2 extends BaseActivity{
    @ViewInject(R.id.fl_title)
    private RelativeLayout rlTitle;


    @ViewInject(R.id.tv0)
    private TextView tv0;

    @ViewInject(R.id.tv)
    private TextView mTv;
    @ViewInject(R.id.user_photo)
    private JHCircleView userPhoto;

    @ViewInject(R.id.user_level)
    private TextView userLevel;

    @ViewInject(R.id.user_name)
    private TextView userName;

    @ViewInject(R.id.referees)
    private TextView referees;

    @ViewInject(R.id.tv_level1_count)
    private TextView level1Count;

    @ViewInject(R.id.member_income)
    private TextView memberIncome;

    @ViewInject(R.id.rv)
    private RecyclerView mRecycleView;

    @ViewInject(R.id.scrollview)
    private PullToRefreshScrollView scrollView;

    @ViewInject(R.id.btn_level_up)
    private LinearLayout levelUp;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;

    private MyIncomeNetRequest request;
    private String level;
    private String charge;
    private double money;
    private String userId;
    private ArrayList<MemberLevel1Entity.Member> list;
    private InvateNetRequest request0;
    private String shareUrl;
    private Animation animBottom;
    private String income;
    private String url;
    private int page;
    private MemberAdapter adapter;
    private ArrayList<MemberLevel1Entity.Member> listsub=new ArrayList<>();

    /**
     * 矩形图片转换为园形
     *
     * @param bitmap
     * @return
     */
    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff000000;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_level2);

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
    }

    private void initScrollView() {


        mRecycleView.setFocusable(false);

        scrollView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);

        scrollView.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ScrollView>() {

            @Override
            public void onPullEvent(PullToRefreshBase<ScrollView> refreshView, PullToRefreshBase.State state, PullToRefreshBase.Mode direction) {
                // TODO Auto-generated method stub

            }
        });
        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page = 0;
                scrollView.onRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                // TODO Auto-generated method stub
                page++;
                if (list.size() >= 20) {
                    if (list.size() / 20 > page) {
                        Logger.i("list.size()" + list.size() / 20 + "page" + page);
                        listsub.addAll(list.subList(20 * page, 20 * (page + 1)));
                        adapter.notifyDataSetChanged();
                    } else if (list.size() / 20 == page) {
                        listsub.addAll(list.subList(20 * page, list.size()));
                        adapter.notifyDataSetChanged();
                    }
                }
                scrollView.onRefreshComplete();
            }
        });

    }

    public void onBack(View view) {
        finish();
    }

    public void search(View view) {
        startActivity(new Intent(MembersLevel2.this, SearchActivity.class));
    }

    public void levelUp(View view) {
        if (isLogin()){
//            Intent intent = new Intent(MembersLevel2.this,NMemberChargeActivity.class);
//            intent.putExtra("type","chooseCharge");
//            startActivity(intent);
            startActivity(new Intent(MembersLevel2.this, RechargeActivity.class));
        }else {
            startActivity(new Intent(MembersLevel2.this, LeFenPhoneLoginActivity.class));
        }
    }

    private void initData() {


        startMyDialog();
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }

        request.MemberLevel1Net("2", new RequestCallBack<MemberLevel1Entity>() {
            @Override
            public void onSuccess(ResponseInfo<MemberLevel1Entity> responseInfo) {
                    Logger.json(responseInfo.result.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        stopMyDialog();
                        Logger.json(responseInfo.result.toString());
                        if (!responseInfo.result.refereeName.equals("")) {
                            referees.setText("我的推荐人：" + responseInfo.result.refereeName);
                        }
                        if (responseInfo.result.memberCount == null) {
                            level1Count.setText("从友：0人");
                        } else {
                            level1Count.setText("从友：" + responseInfo.result.memberCount + "人");
                        }
                        if (responseInfo.result.memberIncome != null) {
                            income=String.format("%.2f", Float.parseFloat(responseInfo.result.memberIncome) / 100) + "";
                            memberIncome.setText(income);
                        }

                        switch (responseInfo.result.memberLev) {
                            case 1:
                                userLevel.setText("普通会员");
                                userLevel.setBackgroundResource(R.drawable.shape_level);
                                tv0.setText("升级VIP会员领奖励会更高");
                                charge = "(升级还需充值¥" + String.format("%.2f", Float.parseFloat(responseInfo.result.memberRecharge) / 100) + ")";
                                mTv.setText(charge);
                                break;
                            case 2:
                                userLevel.setText("VIP会员");
                                userLevel.setBackgroundResource(R.drawable.shape_level2);
                                tv0.setText("升级至尊会员领奖励会更高");
                                charge = "(升级还需充值¥" + String.format("%.2f", Float.parseFloat(responseInfo.result.memberRecharge) / 100) + ")";
                                mTv.setText(charge);
                                break;
                            case 3:
                                userLevel.setText("至尊会员");
                                userLevel.setBackgroundResource(R.drawable.shape_level3);
                                tv0.setText("您已是至尊会员领奖励,享受最高领奖励吧!");
                                mTv.setText("");
                                levelUp.setVisibility(View.GONE);
                                break;
                        }

                        list = responseInfo.result.membersList;
                        if (list.size() >= 20) {
                            listsub.addAll(list.subList(0, 20));
                        } else {
                            listsub.addAll(list);
                        }
                        adapter = new MemberAdapter(mContext, listsub);
                        mRecycleView.setLayoutManager(new FullyGridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));

                        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent1 = new Intent(MembersLevel2.this, DetailsActivity.class);
                                userId = list.get(position).memberId;
                                intent1.putExtra("user_id", userId);
                                startActivity(intent1);
                            }
                        });
                        mRecycleView.setAdapter(adapter);
                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void info(View view){

        String phone = sp.getString("phone", "");

        if (TextUtils.isEmpty(phone)) {
            showToast("数据异常，请重新登录后，再来分享！");
            return;
        }
        if (request0 == null) {
            request0= new InvateNetRequest(mContext);
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
                new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, shareUrl ).share();
            }
        });

        dialog1.show();
    }

    public void jumpToMember(View view){
        if (income.equals("0")){
            InvateDialog dialog = new InvateDialog(mContext, "你还没有领奖励哦，赶快邀请好友吧！");
            dialog.show();
        }else {
            Intent intent= new Intent(MembersLevel2.this,MembersAndGains.class);
            intent.putExtra("type","2");
            startActivity(intent);
        }
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
            if (sp.getString("photo", "").contains("http://")||sp.getString("photo", "").contains("https://")) {
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
        }else {
            userPhoto.setImageResource(R.mipmap.bind_card_photo);
        }
    }
}
