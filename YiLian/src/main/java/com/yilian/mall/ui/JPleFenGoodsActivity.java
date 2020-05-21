package com.yilian.mall.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshGridView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSubClassfiyGoodsEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.GlideUtil;

import java.util.ArrayList;


/**
 * Created by liuyuqi on 2016/10/20 0020.
 * 乐分币商品界面
 */

public class JPleFenGoodsActivity extends BaseActivity implements View.OnClickListener {
    private RadioButton rbDefault;
    private RadioButton rbPrice;
    private RadioButton rbCount;
    private CheckBox cbHasGoods;
    private PullToRefreshGridView mGridView;
    private JPNetRequest requestData;
    private ArrayList<JPGoodsEntity> jpGoodsEntityList = new ArrayList<>();
    private String sort;
    private int page;
    private int count;
    private MyGridViewAdapter adapter;
    private TextView tv_title;
    private ImageView back;
    private ImageView shopCar;
    private ImageView share;
    private ImageView ivNothing;
    private int isFirst = 0;
    private int lastCheckedRadioButton;
    private Context mContext;
    private android.widget.RadioGroup rgSort;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = this;
        setContentView(R.layout.jp_activity_lefengoods);
        initView();
        Listener();

    }


    private void initView() {
        tv_title = (TextView) findViewById(R.id.v3Title);
        back = (ImageView) findViewById(R.id.v3Back);
        shopCar = (ImageView) findViewById(R.id.v3Shop);
        shopCar.setVisibility(View.GONE);
        share = (ImageView) findViewById(R.id.v3Share);
        share.setVisibility(View.VISIBLE);
        share.setImageDrawable(getResources().getDrawable(R.mipmap.bg_search));
        share.setOnClickListener(this);
        rgSort = (android.widget.RadioGroup) findViewById(R.id.rg_sort);
        rbDefault = (RadioButton) findViewById(R.id.rb_sort_newest);
        rbDefault.setChecked(true);
        rbPrice = (RadioButton) findViewById(R.id.rb_sort_price);
        rbCount = (RadioButton) findViewById(R.id.rb_sort_count);
        cbHasGoods = (CheckBox) findViewById(R.id.cb_sort_has_data);
        mGridView = (PullToRefreshGridView) findViewById(R.id.class_gv);
        TextView tv_defult = (TextView) findViewById(R.id.tv_default);
        tv_title.setText("乐分币商品");
        mGridView.setMode(PullToRefreshBase.Mode.BOTH);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);
        if (adapter == null) {
            adapter = new MyGridViewAdapter(jpGoodsEntityList);
            mGridView.setAdapter(adapter);
        }
        back.setOnClickListener(this);
        sort = "0000";
        page = 0;
        count = 30;
        initGridData();
    }

    private void Listener() {
        rgSort.setOnCheckedChangeListener(new android.widget.RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(android.widget.RadioGroup radioGroup, int checkedId) {
                boolean isChecked = cbHasGoods.isChecked();
                getSort(checkedId, isChecked);

            }
        });

        //是否有货 作为排序条件监听
        cbHasGoods.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    cbHasGoods.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
                    sort = sort.substring(0, sort.length() - 1) + "1";
                } else {
                    cbHasGoods.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
                    sort = sort.substring(0, sort.length() - 1) + "0";
                    Logger.i("只看有货" + sort);
                }
                page = 0;//排序都是从第0页开始的
                initGridData();
            }
        });
        rbPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPriceSrot();
            }
        });
        mGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 0;
                initGridData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                initGridData();
            }
        });

    }

    private void setPriceSrot() {
        sort = sort.replace(",", "");//处理sort之前先把逗号去掉，在进行网络请求时再加上
        String substring = sort.substring(0, sort.length() - 1);
        String subEnd = sort.substring(sort.length() - 1);
        if ("010".equals(substring)) {
            sort = "020" + subEnd;
            rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
        } else {
            sort = "010" + subEnd;
            rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
        }
        Logger.i("价格只看有货排序" + sort);
        page = 0;
        initGridData();
    }

    private void getSort(int checkedId, boolean isChecked) {
        if (isChecked) {
            sort = "1";
        } else {
            sort = "0";
        }
        switch (checkedId) {
            case R.id.rb_sort_newest:
                lastCheckedRadioButton = R.id.rb_sort_newest;
                sort = "100" + sort;
                rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                page = 0;
                initGridData();
                break;
            case R.id.rb_sort_count:
                lastCheckedRadioButton = R.id.rb_sort_count;
                sort = "001" + sort;
                rbPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_default), null);
                page = 0;
                initGridData();
                break;
            default:
                break;
        }
    }


    private void initGridData() {

        isFirst++;
        //请求GridView数据
        if (requestData == null) {
            requestData = new JPNetRequest(this);
        }
        startMyDialog();
        Logger.i("请求参数的sort" + sort);
        requestData.getLeFenGoodsList(sort, page, count, new RequestCallBack<JPSubClassfiyGoodsEntity>() {

            @Override
            public void onSuccess(ResponseInfo<JPSubClassfiyGoodsEntity> responseInfo) {
                stopMyDialog();
                mGridView.onRefreshComplete();//设置刷新完成
                JPSubClassfiyGoodsEntity result = responseInfo.result;
                JPSubClassfiyGoodsEntity.DataBean data = result.data;
                switch (result.code) {
                    case 1:
                        if (result == null || data.goods.size() == 0 && isFirst == 1) {
                            ivNothing.setVisibility(View.VISIBLE);
                            mGridView.setVisibility(View.GONE);
                            return;
                        } else if (isFirst < 1) {
                            showToast("没有更多数据");
                        }

                        if (page == 0 && jpGoodsEntityList != null) {
                            jpGoodsEntityList.clear();
                        }
                        if (data.goods != null && data.goods.size() != 0) {
                            jpGoodsEntityList.addAll(data.goods);
                            adapter.notifyDataSetChanged();
                        }
                        break;
                }


            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                Toast.makeText(JPleFenGoodsActivity.this.mContext, "请求数据失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Share:
                //搜索界面
                Intent intent = new Intent(this, SearchCommodityActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestData != null) {
            requestData = null;
        }
    }

    class MyGridViewAdapter extends BaseAdapter {

        private final ArrayList<JPGoodsEntity> datas;

        public MyGridViewAdapter(ArrayList<JPGoodsEntity> jpGoodsEntityList) {
            this.datas = jpGoodsEntityList;
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

        @Override
        public int getCount() {
            if (datas.size() <= 0) {
                return 0;
            }
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            final MyLenFenHolder holder;
            if (view == null) {
                view = View.inflate(JPleFenGoodsActivity.this.mContext, R.layout.item_lefengoods_list, null);
                holder = new MyLenFenHolder();
                holder.goodsItem = (FrameLayout) view.findViewById(R.id.frame_layout_jp_goods_item);
                holder.goodsIcon = (ImageView) view.findViewById(R.id.iv_goods1);
                holder.tv_goodsName = (TextView) view.findViewById(R.id.tv_goods_name);
                holder.tv_goodsPrice = (TextView) view.findViewById(R.id.tv_price);
                holder.tag = (TextView) view.findViewById(R.id.tv_market_price);
                holder.iv_null_goods = (ImageView) view.findViewById(R.id.iv_sold_out);
                view.setTag(holder);
            } else {
                holder = (MyLenFenHolder) view.getTag();
            }

            final JPGoodsEntity goodsEntity = datas.get(position);
            final int dataPosition = position;

            GlideUtil.showImageWithSuffix(mContext,goodsEntity.JPImageUrl,holder.goodsIcon);
            holder.tv_goodsName.setText(goodsEntity.JPGoodsName);
            holder.tv_goodsPrice.setText(MoneyUtil.getLeFenBi(goodsEntity.JPGoodsPrice));
            holder.tag.setText(goodsEntity.JPTagsName);
            if ("0".equals(goodsEntity.hasGoods)) {
                holder.iv_null_goods.setVisibility(View.VISIBLE);
            } else {
                holder.iv_null_goods.setVisibility(View.GONE);
            }

            holder.goodsItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(JPleFenGoodsActivity.this.mContext, JPNewCommDetailActivity.class);
                    intent.putExtra("goods_id", datas.get(dataPosition).JPGoodsId);
                    intent.putExtra("tags_name", datas.get(dataPosition).JPTagsName);
                    startActivity(intent);
                }
            });
            return view;
        }
    }

    class MyLenFenHolder {
        FrameLayout goodsItem;
        ImageView iv_null_goods;
        ImageView goodsIcon;
        TextView tv_goodsName;
        TextView tv_goodsPrice;
        TextView tag;
    }
}
