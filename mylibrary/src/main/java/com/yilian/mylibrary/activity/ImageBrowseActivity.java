package com.yilian.mylibrary.activity;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.yilian.mylibrary.R;
import com.yilian.mylibrary.WebImageUtil;
import com.yilian.mylibrary.entity.ImgAlterEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 图片预览
 * Created by Zg on 2018/6/21 0021.
 */

public class ImageBrowseActivity extends AppCompatActivity implements View.OnClickListener {

    private int currentPosition = 0;
    private List<String> imgList = new ArrayList<>();
    private ViewPager viewPager;
    private TextView imgNum;
    private ImageView ivBack, ivDelete;
    private List<View> listViews;
    private PhotoPagerAdapter photoPagerAdapter;
    /**
     * 是否可删除
     */
    private boolean canDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.library_module_activity_image_browse);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        viewPager = findViewById(R.id.view_pager);
        imgNum = findViewById(R.id.img_num);
        ivBack = findViewById(R.id.iv_back);
        ivDelete = findViewById(R.id.iv_delete);
    }

    private void initData() {
        canDelete = getIntent().getBooleanExtra("canDelete", false);
        imgList = getIntent().getStringArrayListExtra("imgList");
        currentPosition = getIntent().getIntExtra("position", 0);

        if (canDelete) {
            ivDelete.setVisibility(View.VISIBLE);
        } else {
            ivDelete.setVisibility(View.INVISIBLE);
        }

        listViews = new ArrayList<View>();
        for (int i = 0; i < imgList.size(); i++) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.library_module_fragment_picture_slide, null);
            final PhotoView photoview = (PhotoView) view.findViewById(R.id.photoview);
            photoview.setAdjustViewBounds(true);
            photoview.setScaleType(ImageView.ScaleType.FIT_CENTER);

            String url = WebImageUtil.getInstance().getWebImageUrlNOSuffix(imgList.get(i));
            Glide.with(this)
                    .load(url)
                    .skipMemoryCache(true)//不缓存到内存
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(photoview);
            listViews.add(view);
        }
        photoPagerAdapter = new PhotoPagerAdapter(listViews);
        viewPager.setAdapter(photoPagerAdapter);
        //通过viewPager.setCurrentItem来定义当前显示哪一个图片，position由上一个页面传过来
        imgNum.setText(String.valueOf(currentPosition + 1) + "/" + listViews.size());
        viewPager.setCurrentItem(currentPosition);
        photoPagerAdapter.notifyDataSetChanged();
    }

    private void initListener() {
        ivBack.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPosition = position;
                imgNum.setText(String.valueOf(currentPosition + 1) + "/" + listViews.size());//在当前页面滑动至其他页面后，获取position值
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            //返回
            finish();
        } else if (i == R.id.iv_delete) {
            //EventBus 推送
            ImgAlterEntity imgAlterEntity = new ImgAlterEntity();
            imgAlterEntity.imgUrl = imgList.get(currentPosition);
            EventBus.getDefault().post(imgAlterEntity);
            imgList.remove(currentPosition);
            //删除图片
            listViews.remove(currentPosition);//删除一项数据源中的数据
            photoPagerAdapter.notifyDataSetChanged();//通知UI更新
            if (listViews.size() <= 0) {
                finish();
            }
        }
    }

    public class PhotoPagerAdapter extends PagerAdapter {

        private List<View> viewList;

        public PhotoPagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }
}
