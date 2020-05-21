package com.leshan.ylyj.view.activity.creditmodel;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.CreditScoreView;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ToastUtil;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.CreditRatingEntity;
import rxfamily.entity.DimensionalityEntity;


/**
 * 了解信用
 */

public class KnowMoreCreditActivity extends BaseActivity implements View.OnClickListener {

    private CreditScoreView know_more_credit_radar_view;
    //各维度分值
    private float[] data = {170, 180, 160, 170, 180};
    //数据最大值
    private float maxValue = 290;
    //各维度标题
    private String[] titles = {"财产", "信用", "人脉", "行为", "身份特质"};
    //各维度图标
    private int[] icons = {R.mipmap.icon_qian, R.mipmap.icon_ka, R.mipmap.icon_ren,
            R.mipmap.icon_xing, R.mipmap.icon_huang};


    //ViewPager
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private PagerAdapter pagerAdapter;
    private List<View> viewList;
//    private KnowMoreCreditBean knowMoreCreditBean;
//    private ThreePageAdapter twoPageAdapter;
//    private List<KnowMoreCreditBean> threePageBeans;

    private TextView title_tv;

    private TextView range_tv, medium_tv, well_tv, excellent_tv, brilliant_tv;

    private TextView grade_one_tv, grade_two_tv, grade_three_tv, grade_four_tv, grade_five_tv, grade_six_tv;
    private ImageView know_more_credit_share_img, know_more_credit_information_img;

    private TextView temporary_credit_tv;//临时信用
    CreditPresenter creditPresenter;
    List<DimensionalityEntity.CreditBean> mCredit;//正序
    List<DimensionalityEntity.CreditBean> mCredit1;//逆序
    private int imgposition = 0;
    private List<Integer> listpostion;
    int score = 0;
    int totalScore = 0;
    private RelativeLayout know_more_back_rl;
    private int myNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_know_more_credit);
        CreditScoreView.iconsbool = false;
        initView();
        initListener();
        initData();
        getHomePage();
        StatusBarUtils.transparencyBar(this);

    }

    @Override
    protected void initView() {
        viewPager = findViewById(R.id.credit_viewpager);
        circleIndicator = findViewById(R.id.credit_indicator);
//        circleIndicator =  findViewById(R.id.indicator);
        know_more_credit_radar_view = findViewById(R.id.know_more_credit_radar_view);
        know_more_credit_radar_view.setContext(this);
        range_tv = findViewById(R.id.range_tv);
        medium_tv = findViewById(R.id.medium_tv);
        well_tv = findViewById(R.id.well_tv);
        excellent_tv = findViewById(R.id.excellent_tv);
        brilliant_tv = findViewById(R.id.brilliant_tv);

        grade_one_tv = findViewById(R.id.grade_one_tv);
        grade_two_tv = findViewById(R.id.grade_two_tv);
        grade_three_tv = findViewById(R.id.grade_three_tv);
        grade_four_tv = findViewById(R.id.grade_four_tv);
        grade_five_tv = findViewById(R.id.grade_five_tv);
        grade_six_tv = findViewById(R.id.grade_six_tv);
        temporary_credit_tv = findViewById(R.id.temporary_credit_tv);
        know_more_credit_share_img = findViewById(R.id.know_more_credit_share_img);
        know_more_credit_information_img = findViewById(R.id.know_more_credit_information_img);
        know_more_back_rl = findViewById(R.id.know_more_back_rl);
    }

    @Override
    protected void initListener() {
        know_more_credit_share_img.setOnClickListener(this);
        know_more_credit_information_img.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                Toast.makeText(KnowMoreCreditActivity.this,"aa"+position,Toast.LENGTH_SHORT).show();
                imgposition = listpostion.get(position);
                new Thread(new MyThread()).start();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    protected void initData() {
//        CreditScoreView.data = data;
//        CreditScoreView.maxValue = maxValue;
//        CreditScoreView.titles = titles;
//        CreditScoreView.icons = icons;
        myNum = getIntent().getIntExtra("myNum", 0);
    }

    public void setSite(int clickimg) {
        if (clickimg == 0) {
            imgposition = 0;
            new Thread(new MyThread()).start();
            viewPager.setCurrentItem(4);
        } else if (clickimg == 1) {
            imgposition = 1;
            new Thread(new MyThread()).start();
            viewPager.setCurrentItem(3);
        } else if (clickimg == 2) {
            imgposition = 2;
            new Thread(new MyThread()).start();
            viewPager.setCurrentItem(2);
        } else if (clickimg == 3) {
            imgposition = 3;
            new Thread(new MyThread()).start();
            viewPager.setCurrentItem(1);
        } else if (clickimg == 4) {
            imgposition = 4;
            new Thread(new MyThread()).start();
            viewPager.setCurrentItem(0);
        }
//        Toast.makeText(this,"4444444444",Toast.LENGTH_SHORT).show();
    }

    /**
     * @net 执行网络请求
     */
    private void getHomePage() {
        startMyDialog(false);
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        //@net  进行网络请求
        creditPresenter.getCreditRating();
        creditPresenter.getDimensionality();
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("获取数据结果");
        //@net 数据类型判断执行对应请求的逻辑
        if (baseEntity instanceof CreditRatingEntity) {
            List<CreditRatingEntity.DataBean> mDatas = ((CreditRatingEntity) baseEntity).datas;
            SetCreditRating(mDatas);
//            Toast.makeText(this,"bb",Toast.LENGTH_SHORT).show();
        } else if (baseEntity instanceof DimensionalityEntity) {
            List<DimensionalityEntity.CreditBean> mCredit = ((DimensionalityEntity) baseEntity).data.creditDimensionalList;
            DimensionalityEntity.DataBean mData = ((DimensionalityEntity) baseEntity).data;
            SetDimensionality(mCredit, mData);
//            Toast.makeText(this,"aa",Toast.LENGTH_SHORT).show();
        }
    }

    private void SetDimensionality(List<DimensionalityEntity.CreditBean> mCredit, DimensionalityEntity.DataBean mData) {
        CreditScoreView.titles = new String[mCredit.size()];
        CreditScoreView.data = new float[mCredit.size()];
        CreditScoreView.iconss = new String[mCredit.size()];
        Collections.reverse(mCredit);
        mCredit1 = mCredit;
        listpostion = new ArrayList<>();
        for (int i = 0; i < mCredit.size(); i++) {
            CreditScoreView.titles[i] = mCredit.get(i).getName();
            int latitude = mCredit.get(i).getCredit() + (mData.creditInit / 5);
            if (latitude > 200) {
                CreditScoreView.data[i] = 200;
            } else {
                if (mCredit.get(i).getCredit() < 0) {
                    CreditScoreView.data[i] = 0 + (mData.creditInit / 5);
                } else {
                    CreditScoreView.data[i] = latitude;
                }
            }

            listpostion.add(i);
//            CreditScoreView.iconsb[i]=returnBitMap(mCredit.get(i).getIconBright());
        }
        CreditScoreView.score = mData.creditTotal;//了解信用信用值
        totalScore = mData.creditTotal;//了解信用信用值
        Collections.reverse(listpostion);
        imgposition = mCredit.size() - 1;
        new Thread(new MyThread()).start();
        CreditScoreView.dataCount = mCredit.size();
        this.mCredit = mCredit;
        CreditScoreView.maxValue = mData.creditCeiling;
        int temporaryScore = 0;
        if((mData.tempCredit+mData.creditTotal)>1000){
            temporaryScore = 1000 - totalScore;
        }else{
            temporaryScore=mData.tempCredit;
        }

        if (temporaryScore <= 0) {
            temporaryScore = 0;
        }
        temporary_credit_tv.setText("临时信用" + temporaryScore);
//        CreditScoreView.score=mData.creditTotal;//总分
        know_more_credit_radar_view.invalidate();

        InitViewPager(mCredit);

//        计算总分
        for (int i = 0; i < mCredit.size(); i++) {
            score += mCredit.get(i).getCredit();
        }
        if (myNum < 450) {
            range_tv.setBackgroundResource(R.mipmap.icon_jiaocha_liaojie);
            range_tv.setTextColor(getResources().getColor(R.color.white));
        } else if (myNum >= 450 && myNum < 600) {
            medium_tv.setBackgroundResource(R.mipmap.icon_zhongdeng_liaojie);
            medium_tv.setTextColor(getResources().getColor(R.color.white));
        } else if (myNum >= 600 && myNum < 750) {
            well_tv.setBackgroundResource(R.mipmap.icon_lianghao_liaojie);
            well_tv.setTextColor(getResources().getColor(R.color.white));
        } else if (myNum >= 750 && myNum < 900) {
            excellent_tv.setBackgroundResource(R.mipmap.icon_youxiu_liaojie);
            excellent_tv.setTextColor(getResources().getColor(R.color.white));
        } else if (myNum >= 900) {
            brilliant_tv.setBackgroundResource(R.mipmap.icon_zhuoyue_liaojie);
            brilliant_tv.setTextColor(getResources().getColor(R.color.white));
        }
        if (myNum < 450) {
            know_more_back_rl.setBackgroundResource(R.drawable.credit_poor_gradation_back);
        } else if (myNum >= 450 && myNum < 600) {
            know_more_back_rl.setBackgroundResource(R.drawable.credit_medium_gradation_back);
        } else if (myNum >= 600 && myNum < 750) {
            know_more_back_rl.setBackgroundResource(R.drawable.credit_well_gradation_back);
        } else if (myNum >= 750 && myNum < 900) {
            know_more_back_rl.setBackgroundResource(R.drawable.credit_good_gradation_back);
        } else if (myNum >= 900) {
            know_more_back_rl.setBackgroundResource(R.drawable.credit_excellence_gradation_back);
        }
    }

    public class MyThread extends Thread {
        boolean empty = true;
        //继承Thread类，并改写其run方法
        private final static String TAG = "My Thread ===> ";

        public void run() {
            Log.d(TAG, "run");
            CreditScoreView.iconsb = new Bitmap[mCredit.size()];

            for (int i = 0; i < mCredit.size(); i++) {
//                if(returnBitMap(mCredit.get(i).getIconBright())==null||returnBitMap(mCredit.get(i).getIconGray())==null){
//                    empty=false;
//                }else {
                if (i == imgposition) {//五个图标   后台让加前缀
                    CreditScoreView.titlecolor[i] = 1;
                    CreditScoreView.iconsb[i] = returnBitMap(Constants.ImageUrl + mCredit.get(i).getIconBright());
                } else {
                    CreditScoreView.titlecolor[i] = 0;
                    CreditScoreView.iconsb[i] = returnBitMap(Constants.ImageUrl + mCredit.get(i).getIconGray());
                }
//                }
            }
//            if(empty){
            CreditScoreView.iconsbool = true;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    know_more_credit_radar_view.invalidate();
                }
            });
//            }
        }
    }

    public Bitmap returnBitMap(String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    private void SetCreditRating(List<CreditRatingEntity.DataBean> listdata) {
        range_tv.setText(listdata.get(0).getName());
        medium_tv.setText(listdata.get(1).getName());
        well_tv.setText(listdata.get(2).getName());
        excellent_tv.setText(listdata.get(3).getName());
        brilliant_tv.setText(listdata.get(4).getName());
        grade_one_tv.setText(String.valueOf(listdata.get(0).getStart()));
        grade_two_tv.setText(String.valueOf(listdata.get(1).getStart()));
        grade_three_tv.setText(String.valueOf(listdata.get(2).getStart()));
        grade_four_tv.setText(String.valueOf(listdata.get(3).getStart()));
        grade_five_tv.setText(String.valueOf(listdata.get(4).getStart()));
        grade_six_tv.setText(String.valueOf(listdata.get(4).getEnd()));


    }

    /**
     * 添加ViewPager
     */
    private void InitViewPager(List<DimensionalityEntity.CreditBean> mCredit) {
        pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView(viewList.get(position));
            }

            @Override
            public int getItemPosition(Object object) {
                return super.getItemPosition(object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "title";
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        };
        viewList = new ArrayList<View>();
        int j = mCredit.size() - 1;
        for (int i = 0; i < mCredit.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.item_for_three_page_layout, null);
            title_tv = view.findViewById(R.id.title_tv);
            title_tv.setText(mCredit.get(j).getDesc());
            viewList.add(view);
            j--;
        }
        viewPager.setAdapter(pagerAdapter);

        circleIndicator.setViewPager(viewPager);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        } else if (i == R.id.know_more_credit_share_img) {
            SkipUtils.toShareCredit(this, String.valueOf(myNum));
        } else if (i == R.id.know_more_credit_information_img) {//右上角跳转信用介绍界面
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.MESSAGE_INTRODUCE);
            startActivity(intent);
        }
    }
}
