package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;

/**
 * 引导界面
 */
public class GuideActivity extends BaseActivity {

    @ViewInject(R.id.viewPager_guide_img)
    ViewPager mViewPager;
    @ViewInject(R.id.iv_experience)
    Button ivExperience;

    private Context mContext = GuideActivity.this;
    private int[] imgs = {R.mipmap.guide_01, R.mipmap.guide_02, R.mipmap.guide_03};
    private boolean isTranslation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ViewUtils.inject(this);
        initListener();
        GuideViewPagerAdapter adapter = new GuideViewPagerAdapter();
        mViewPager.setAdapter(adapter);

    }

    private void initListener() {
        ivExperience.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean(Constants.SPKEY_IS_FIRST, false);
                editor.commit();
                startActivity(new Intent(mContext, JPMainActivity.class));
                finish();
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == imgs.length - 1) {
                    ivExperience.setVisibility(View.VISIBLE);
                }else{
                    ivExperience.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class GuideViewPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return imgs.length;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            // TODO Auto-generated method stub
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // TODO Auto-generated method stub
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            // TODO Auto-generated method stub
            ViewGroup.LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            ImageView mView = new ImageView(mContext);
            switch (position) {
                case 0:
                    mViewPager.setBackgroundColor(Color.parseColor("#fde30d"));
                    break;

                case 1:
                    mViewPager.setBackgroundColor(Color.parseColor("#ffc30d"));
                    break;

                case 2:
                    mViewPager.setBackgroundColor(Color.parseColor("#ff790d"));
                    break;
            }


            mView.setScaleType(ScaleType.CENTER_CROP);
            mView.setBackgroundResource(imgs[position]);
            container.addView(mView, lp);


            return mView;
        }
    }
}
