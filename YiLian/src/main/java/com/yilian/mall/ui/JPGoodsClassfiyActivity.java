package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsClassfiyEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.GlideUtil;

import java.util.List;

/**
 * Created by liuyuqi on 2016/10/18 0018.
 * 商品分类界面
 */

public class JPGoodsClassfiyActivity extends BaseActivity implements View.OnClickListener {


    private JPNetRequest jpNetRequest;
    private Context mContext;
    private LinearLayout ll_container;
    private ImageView iv_back;
    private TextView tv_title;
    private ImageView iv_shop;
    private ImageView iv_share;
    private ImageView ivNothing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.jp_activity_goodsclassfiy);
        mContext = this;
        initView();
        initData();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.v3Back);
        iv_back.setOnClickListener(this);
        tv_title = (TextView) findViewById(R.id.v3Title);
        tv_title.setText("商品分类");
        iv_shop = (ImageView) findViewById(R.id.v3Shop);
        iv_shop.setVisibility(View.GONE);
        iv_share = (ImageView) findViewById(R.id.v3Share);
        iv_share.setVisibility(View.INVISIBLE);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);

        imageLoader = ImageLoader.getInstance();

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.login_module_default_jp) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.login_module_default_jp)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.login_module_default_jp) //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(false) //设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) //设置下载的图片是否缓存在SD卡中
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片的解码类型
//              .displayer(new SimpleBitmapDisplayer()) // default 可以设置动画，比如圆角或者渐变
                .build();
    }

    private void initData() {
        if (null == jpNetRequest) {
            jpNetRequest = new JPNetRequest(this);
        }
        ll_container.removeAllViews();
        startMyDialog();
        jpNetRequest.getGoodsClassList(new RequestCallBack<JPGoodsClassfiyEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPGoodsClassfiyEntity> responseInfo) {
                stopMyDialog();
                Logger.i("responseInfo  " + responseInfo.result.code + "  body  " + responseInfo.result.list.toString());
                switch (responseInfo.result.code) {
                    case 1:
                        List<JPGoodsClassfiyEntity.ListBean> goodsClassList = responseInfo.result.list;
                        TextView textview;
                        //动态添加布局
                        Logger.i("goodsClassList " + goodsClassList.toString());
                        if (goodsClassList != null) {
                            ivNothing.setVisibility(View.GONE);
                            ll_container.setVisibility(View.VISIBLE);
                            for (int i = 0; i < goodsClassList.size(); i++) {
                                View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_jp_goodsclass_listview, null);
                                final ImageView goodsClassIcon = (ImageView) itemView.findViewById(R.id.iv_class_icon_title);
                                TextView goodsTitle = (TextView) itemView.findViewById(R.id.tv_class_title);
                                NoScrollGridView mGridView = (NoScrollGridView) itemView.findViewById(R.id.gridView);
                                JPGoodsClassfiyEntity.ListBean listBean = goodsClassList.get(i);
                                String url = listBean.img;
                                if (!url.contains("http")) {
                                    url = Constants.ImageUrl + url + Constants.ImageSuffix;
                                }
                                imageLoader.displayImage(url, goodsClassIcon, options);
                                goodsTitle.setText(listBean.name);
                                List<JPGoodsClassfiyEntity.ListBean.InfoBean> infoBeanList = listBean.info;
                                if (null != infoBeanList && infoBeanList.size() > 0) {
                                    mGridView.setAdapter(new GridAdapter(infoBeanList));
                                    ll_container.addView(itemView);
                                    textview = new TextView(mContext);
                                    textview.setHeight(DPXUnitUtil.dp2px(mContext, 10));
                                    textview.setBackgroundColor(getResources().getColor(R.color.color_bg));
                                    ll_container.addView(textview);
                                    ll_container.setBackgroundResource(R.color.white);
                                }
                            }
                        }

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                ll_container.setVisibility(View.GONE);
                ivNothing.setVisibility(View.VISIBLE);
                Logger.e("e " + e + "  s " + s);
            }
        });
    }

    //跳转搜索界面
    public void find(View view) {
        Intent intent = new Intent(this, SearchCommodityActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
        }
    }


    //设置GridView的适配器
    class GridAdapter extends BaseAdapter {
        private final List<JPGoodsClassfiyEntity.ListBean.InfoBean> infoBeanlist;

        public GridAdapter(List<JPGoodsClassfiyEntity.ListBean.InfoBean> infoBeanList) {
            this.infoBeanlist = infoBeanList;
        }

        @Override
        public int getCount() {
            return infoBeanlist.size();
        }

        @Override
        public Object getItem(int position) {
            return infoBeanlist.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final GoodsClassGridHolder holder;
            if (view == null) {
                view = View.inflate(mContext, R.layout.item_jp_goodsclass_gridview, null);
                holder = new GoodsClassGridHolder();
                holder.goodItem = (LinearLayout) view.findViewById(R.id.goods_class_item);
                holder.goodsimageView = (ImageView) view.findViewById(R.id.iv_goods_icon);
                holder.goodsName = (TextView) view.findViewById(R.id.tv_goods_name);
                view.setTag(holder);
            } else {
                holder = (GoodsClassGridHolder) view.getTag();
            }

            final JPGoodsClassfiyEntity.ListBean.InfoBean infoBean = infoBeanlist.get(position);
            for (int i = 0; i < infoBeanlist.size(); i++) {
                GlideUtil.showImageWithSuffix(mContext, infoBean.img, holder.goodsimageView);
                holder.goodItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //条转小分类
                        Intent intent = new Intent(mContext, OnLineMallGoodsListActivity.class);
                        intent.putExtra("class_id", infoBeanlist.get(position).id);
                        intent.putExtra("goods_classfiy", infoBeanlist.get(position).name);
                        Logger.i("class_id class_id" + infoBeanlist.get(position).id);
                        startActivity(intent);

                    }
                });

                holder.goodsName.setText(infoBean.name);
            }
            return view;
        }
    }


    class GoodsClassGridHolder {
        LinearLayout goodItem;
        ImageView goodsimageView;
        TextView goodsName;
    }

}
