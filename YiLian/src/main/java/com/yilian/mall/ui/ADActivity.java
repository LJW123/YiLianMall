package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;


/**
 * author XiuRenLi  PRAY NO BUG
 * Created by Administrator on 2016/8/4.
 */
public class ADActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_jump;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();
            int countDownTime = data.getInt("countDownTime");
            tv_jump.setVisibility(View.VISIBLE);
            tv_jump.setText(String.valueOf(countDownTime) + " 跳过");
        }
    };
    private ImageView iv_ad;
    private MallNetRequest request;
    private int showTime;
    private int tempTime;
    private Thread thread;
    private boolean jump = true;// 该标记防止点击广告或跳过按钮后，子线程继续执行导致的重复打开HomeActivity的问题

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
        initListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {

        iv_ad = (ImageView) findViewById(R.id.iv_ad);
        iv_ad.setOnClickListener(this);
        tv_jump = (TextView) findViewById(R.id.tv_jump);
        tv_jump.setOnClickListener(this);
    }

    private void initData() {
        String pathName = Environment.getExternalStorageDirectory().getPath() + Constants.BASE_PATH + "ad";
        Bitmap bitmap = BitmapFactory.decodeFile(pathName);
        String adImageUrl = PreferenceUtils.readStrConfig(Constants.SPKEY_AD_IMAGE_URL,mContext,"");
//        String adImageUrl =Constants.TestGifUrl;
        if (!TextUtils.isEmpty(adImageUrl)) {
            if (!adImageUrl.contains("http://") || !adImageUrl.contains("https://")) {
                adImageUrl = Constants.ImageUrl + adImageUrl;
            }
            Logger.i("AD_IMAGE    "+adImageUrl);
            GlideUtil.showImageNoSuffixNoPlaceholder(mContext,adImageUrl,iv_ad);
//            iv_ad.setScaleType(ImageView.ScaleType.FIT_XY);
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    showTime = sp.getInt(Constants.SPKEY_AD_SHOWTIME, 0);
                    for (int i = 1; i <= showTime; i++) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Message msg = new Message();
                        Bundle data = new Bundle();
                        data.putInt("countDownTime", showTime - i);
                        msg.setData(data);
                        handler.sendMessage(msg);
                    }
                    if (jump) {
                        jumpToHomeActivity();
                    }
                }
            });
            thread.start();
        } else {
            jumpToHomeActivity();
        }

    }

    /**
     * 跳转到首页
     */
    private void jumpToHomeActivity() {
        startActivity(new Intent(this, JPMainActivity.class));
    }

    private void initListener() {

    }

    @Override
    public void onClick(View v) {
        jump = false;
        switch (v.getId()) {
            case R.id.iv_ad:
                Intent intent = null;
                String type = sp.getString(Constants.SPKEY_AD_TYPE, "");
                String content = sp.getString(Constants.SPKEY_AD_CONTENT, "");
//                JumpToOtherPage.getInstance(mContext).jumpToOtherPage(Integer.valueOf(type),content);
                switch (type) {
                    case "1":// 1 URL
                        intent = new Intent(this, WebViewActivity.class);
                        intent.putExtra("url", content);
                        break;
                    case "2"://2. 商品详情页
                        intent = new Intent(mContext, JPNewCommDetailActivity.class);
                        intent.putExtra("goods_id", content);
                        intent.putExtra("filiale_id", "0");
                        break;
                    case "3"://3. 商家详情页面
                        intent = new Intent(mContext, MTMerchantDetailActivity.class);
                        intent.putExtra("merchant_id", content);
                        break;
                    case "4":// 4.线上店铺
                        intent = new Intent(mContext, JPFlagshipActivity.class);
                        intent.putExtra("index_id", content);
                        break;
                    default:
                        jump = true;//广告图片被点击后，若没有跳转类型，则在跳过倒计时跑秒结束后，能继续跳转到首页
                        break;
                }
                if (intent != null) {
                    jumpToHomeActivity();
                    startActivity(intent);
                } else {
                    jumpToHomeActivity();
                }
                finish();
                break;
            case R.id.tv_jump:
                jumpToHomeActivity();
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 避免Handler导致内存泄漏
        handler.removeCallbacksAndMessages(null);
//        handler=null;
    }
}
