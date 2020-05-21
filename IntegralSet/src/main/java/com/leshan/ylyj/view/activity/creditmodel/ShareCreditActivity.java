package com.leshan.ylyj.view.activity.creditmodel;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.ShareCreditAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.bean.ShareCreditBean;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.yilian.mylibrary.BitmapUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.umeng.ShareUtile;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.ShareCreditEntity;
import rxfamily.entity.ShareSucceedEntity;

/**
 * 分享信用
 * @author zyh
 */
public class ShareCreditActivity extends BaseActivity implements View.OnClickListener {

    private List<ShareCreditBean> data;
    private RecyclerView rvShareCredit;
    private ShareCreditAdapter adapter;
    private ImageView ivShare;
    private TextView tvCircleFriends, tvShareWeixin, tvShareQQ;
    CreditPresenter creditPresenter;
    List<ShareCreditEntity.DataBean> beanList;
    private String credit ;//信用值
    /**
     * 分享有关
     */
    private String shareApp;//分享至1微信；2朋友圈；3qq
    private String shareImagUrl;
    private ShareUtile shareUtile;
    Bitmap bitmap = null;
    private TextView tvCore;
    //在沒有获取到数据的时候分享占位图
    private Bitmap mSeatBitmap;
    private TextView tvCreditScore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_credit);
        initToolbar();
        setToolbarTitle("信用分享");
        hasBack(true);

        initView();
        initListener();
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);

    }

    private void getHomePage() {
        startMyDialog(false);
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        creditPresenter.getShareCredit();
    }

    @Override
    protected void initView() {
        tvCore=(TextView)findViewById(R.id.tv_score);
        rvShareCredit = (RecyclerView) findViewById(R.id.share_credit_rv);
        ivShare = (ImageView) findViewById(R.id.iv_share);
        tvCircleFriends = (TextView) findViewById(R.id.tv_share_circle);
        tvShareWeixin = (TextView) findViewById(R.id.tv_share_weixin);
        tvShareQQ = (TextView) findViewById(R.id.tv_share_qq);
        tvCreditScore=(TextView)findViewById(R.id.tv_credit_score);

        shareUtile = new ShareUtile(mContext);

        beanList = new ArrayList<>();

        Drawable seatDrawable=getResources().getDrawable(R.mipmap.defaut_share_credit);
        mSeatBitmap= BitmapUtil.drawableToBitmap(seatDrawable);
    }

    @Override
    protected void initListener() {
        tvShareQQ.setOnClickListener(this);
        tvShareWeixin.setOnClickListener(this);
        tvCircleFriends.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        credit=getIntent().getStringExtra("creditTotal");
        tvCore.setText(credit);
        if (TextUtils.isEmpty(credit)){
            tvCreditScore.setVisibility(View.GONE);
        }else {
            tvCreditScore.setVisibility(View.VISIBLE);
            //假如有奖券就给占位图添加水印
            mSeatBitmap=createMarkBitmap(mSeatBitmap,credit);
        }
        getHomePage();
    }


    private void AddBackgroundPicture() {
        if (beanList!=null&&beanList.size()>0){
            adapter= new ShareCreditAdapter(this, beanList);
            GridLayoutManager manager = new GridLayoutManager(this, 3);
            rvShareCredit.setLayoutManager(manager);
            rvShareCredit.setAdapter(adapter);
            shareImagUrl=judgeShareImgUrl(beanList.get(0).getDeputyPicture());
            //添加水印
            if (!TextUtils.isEmpty(shareImagUrl)){
                getMyBitmap(shareImagUrl);
            }

        }

        //九宫格图
        adapter.setOnItemClickListener(new ShareCreditAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data, int position) {
                shareImagUrl=judgeShareImgUrl(beanList.get(position).getDeputyPicture());
                GlideUtil.showImage(mContext,shareImagUrl,ivShare);
                //添加水印
                getMyBitmap(shareImagUrl);

            }
        });
    }
    private String  judgeShareImgUrl(String shareImagUrl){
        if (!shareImagUrl.startsWith("http://")) {
            shareImagUrl = Constants.ImageUrl+shareImagUrl;
        }
        return shareImagUrl;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.returnImg) {
            finish();
        } else if (id == R.id.tv_share_qq) {
            //分享到QQ
            setShare(4);
            shareApp="3";
        } else if (id == R.id.tv_share_weixin) {
            //分享到微信
            setShare(0);
            shareApp="1";
        } else if (id == R.id.tv_share_circle) {
            //分享到朋友圈
            setShare(1);
            shareApp="2";
        }
    }

    /**
     * 分享成功后的回调
     * @param type
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(String type) {
        if (!TextUtils.isEmpty(shareApp)){
            creditPresenter.getShareSucceed(adapter.getSelectedData().getId(), credit, shareApp);//分享成功回调
        }

    }


    /**
     * 获取bitmap图
     *
     * @param url
     * @return
     */
    private void getMyBitmap(String url) {
        Glide.with(mContext).load(url).asBitmap().into(target);
    }
    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {
            //这里我们拿到回掉回来的bitmap，可以加载到我们想使用到的地方
            ShareCreditActivity.this.bitmap=bitmap;

        }
    };
    /**
     * 设置分享的图片
     *
     * @param type
     */

    private void setShare(int type) {
        if (beanList.size()==0||bitmap==null){
            shareUtile.shares(type, mSeatBitmap);
            return;
        }
        Bitmap  targetBitmap = createMarkBitmap(bitmap, credit);
        if (null != targetBitmap) {
            shareUtile.shares(type, targetBitmap);
        } else {
            showToast("请选择分享图片");
        }
    }

    /**
     * Bitmap上添加水印
     *
     * @param bitmap fraction--分数
     * @return
     */

    private Bitmap createMarkBitmap(Bitmap bitmap, String fraction) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        Paint p = new Paint();
        //从中间向两边画
        p.setTextAlign(Paint.Align.CENTER);
        //抗锯齿
        p.setAntiAlias(true);
        // 水印颜色
        p.setColor(Color.parseColor("#FFFEFE"));
        // 水印字体大小
        p.setTextSize(DPXUnitUtil.dp2px(mContext, 13));
        p.setShadowLayer(2f,0,2,getResources().getColor(R.color.color_666666));
        //获取文字信息
        Paint.FontMetrics fontMetrics = p.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        //绘制图像
        canvas.drawBitmap(bitmap, 0, 0, p);
        if (bitmap==mSeatBitmap){
            //绘制文字
            canvas.drawText("我的信用分", w / 2, textHeight / 2 + 105f, p);
        }else {
            //绘制文字
            canvas.drawText("我的信用分", w / 2,  DPXUnitUtil.dp2px(mContext,52.5f)  + textHeight / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2 , p);
        }
        if (TextUtils.isEmpty(fraction)){
            fraction="0";
        }
        //设置粗体文字
        Typeface fontBold = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD);
        p.setTypeface(fontBold);
        p.setTextSize(DPXUnitUtil.dp2px(mContext, 23));
        Paint.FontMetrics fontMetrics1 = p.getFontMetrics();
        float textHeight1 = fontMetrics1.descent - fontMetrics1.ascent;
        if (bitmap==mSeatBitmap){
            canvas.drawText(fraction, w / 2, textHeight + 105f + 14f + textHeight1 / 2, p);
        }else {
            canvas.drawText(fraction, w / 2, DPXUnitUtil.dp2px(mContext,72f)  + textHeight1 / 2 + (Math.abs(fontMetrics1.ascent) - fontMetrics1.descent) / 2 , p);

        }
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return bmp;
    }


    @Override
    public void onCompleted() {

        stopMyDialog();
    }

    @Override
    public void onError(Throwable e) {
        showToast(e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        if (baseEntity instanceof ShareCreditEntity) {
           ShareCreditEntity entity= (ShareCreditEntity) baseEntity;
            if (entity==null||entity.datas==null||entity.datas.size()==0){
                return;
            }
            beanList = ((ShareCreditEntity) baseEntity).datas;
            GlideUtil.showImageNoSuffixNoPlaceholder(mContext,beanList.get(0).getDeputyPicture(),ivShare);
            AddBackgroundPicture();
        }
        if (baseEntity instanceof ShareSucceedEntity) {
           showToast("分享成功");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
