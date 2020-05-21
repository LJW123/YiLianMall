package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshGridView;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.JPGoodsAdapter;
import com.yilian.mall.entity.InvateEntity;
import com.yilian.mall.entity.JPGoodsEntity;
import com.yilian.mall.entity.JPSubClassfiyGoodsEntity;
import com.yilian.mall.http.InvateNetRequest;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liuyuqi on 2017/3/22 0022.
 * 身边好货界面
 */
public class NearbyGoodsActivity extends BaseActivity implements View.OnClickListener {
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private LinearLayout v3Layout;
    private RadioButton rbSortNewest;
    private RadioButton rbSortPrice;
    private RadioButton rbSortCount;
    private RadioGroup rgSort;
    private CheckBox cbSortHasData;
    private LinearLayout llSort;
    private PullToRefreshGridView gridView;
    private ImageView ivNothing;

    private JPNetRequest request;
    private String filialeId;
    private int page;
    private String sort;
    private boolean isHasData;
    private List<JPGoodsEntity> goodsList = new ArrayList<>();
    private JPGoodsAdapter adapter;
    private UmengDialog mShareDialog;
    private String shareUrl;
    private Object shrareUrl;
    private String city_id;
    private String county_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jp_activity_subclassfiygoods);
        initView();
        initListener();
        initDate();
    }

    private void initView() {
        city_id = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_CITY_ID, mContext, "0");
        county_id = PreferenceUtils.readStrConfig(Constants.SPKEY_SELECT_COUNTY_ID, mContext, "0");

        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setVisibility(View.INVISIBLE);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("身边好货");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.INVISIBLE);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.VISIBLE);
        v3Shop.setImageResource(R.mipmap.iv_shopingcar);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.VISIBLE);
        v3Layout = (LinearLayout) findViewById(R.id.v3Layout);
        rbSortNewest = (RadioButton) findViewById(R.id.rb_sort_newest);
        rbSortNewest.setChecked(true);
        rbSortPrice = (RadioButton) findViewById(R.id.rb_sort_price);
        rbSortCount = (RadioButton) findViewById(R.id.rb_sort_count);
        rgSort = (RadioGroup) findViewById(R.id.rg_sort);
        cbSortHasData = (CheckBox) findViewById(R.id.cb_sort_has_data);
        llSort = (LinearLayout) findViewById(R.id.ll_sort);
        gridView = (PullToRefreshGridView) findViewById(R.id.class_gv);
        ivNothing = (ImageView) findViewById(R.id.iv_nothing);

        v3Back.setOnClickListener(this);
        v3Shop.setOnClickListener(this);
        v3Share.setOnClickListener(this);
        page = 0;
        sort = "1,0,0,0";
    }

    private void initListener() {
        rgSort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (isHasData) {
                    sort = "1";
                } else {
                    sort = "0";
                }
                switch (checkedId) {
                    case R.id.rb_sort_newest:
                        sort = "1,0,0," + sort;
                        //initData不能放在外边因为价格的点击也会走这个方法
                        rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mContext,R.mipmap.price_default),null);
                        initDate();
                        break;
                    case R.id.rb_sort_count:
                        sort = "0,0,1," + sort;
                        rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mContext,R.mipmap.price_default),null);
                        initDate();
                        break;
                    default:
                        break;
                }

            }
        });

        cbSortHasData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isHasData = isChecked;
                if (isHasData) {
                    sort = sort.substring(0, sort.length() - 1) + "1";
                    cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.hasgoods), null);
                } else {
                    sort = sort.substring(0, sort.length() - 1) + "0";
                    cbSortHasData.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.no_hasgoods), null);
                }
                initDate();
            }
        });

        rbSortPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPriceSort();
            }
        });

        gridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<GridView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
                page = 0;
                initDate();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                page++;
                initDate();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JPGoodsEntity item= (JPGoodsEntity) adapter.getItem(position);
                Intent intent=new Intent(NearbyGoodsActivity.this,JPNewCommDetailActivity.class);
                intent.putExtra("goods_id",item.JPGoodsId);
                intent.putExtra("filiale_id",item.JPFilialeId);
                intent.putExtra("tags_name",item.JPTagsName);
                startActivity(intent);
            }
        });
    }

    private void setPriceSort() {
        //截取现在的排序状态
        String replace = sort.replace(",", "");
        String substring = replace.substring(0, replace.length() - 1);
        String endSort = replace.substring(replace.length() - 1);
        if ("010".equals(substring)) {
            sort = "0,0,0," + endSort;
            rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_top), null);
        } else {
            sort = "0,1,0," + endSort;
            rbSortPrice.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.price_buttom), null);
        }

        Logger.i("价格的只看有货 " + sort);
        initDate();
    }

    private void initDate() {
        if (request == null) {
            request = new JPNetRequest(mContext);
        }
        startMyDialog();
        request.getNearbyGoodsList(city_id,county_id, page, sort, new RequestCallBack<JPSubClassfiyGoodsEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPSubClassfiyGoodsEntity> responseInfo) {
                gridView.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:
                        gridView.setVisibility(View.VISIBLE);
                        ivNothing.setVisibility(View.GONE);
                        ArrayList<JPGoodsEntity> nearbyGoodsList = responseInfo.result.data.goods;
                        Logger.i("身边好货的集合size  "+nearbyGoodsList.size()+"goodsList size  "+goodsList.size());

                        if (null != nearbyGoodsList && nearbyGoodsList.size() >= 1) {
                            if (page >= 1) {
                                goodsList.addAll(nearbyGoodsList);
                                adapter.notifyDataSetChanged();
                            } else {
                                goodsList.clear();
                                goodsList.addAll(nearbyGoodsList);
                                if (null == adapter) {
                                    adapter = new JPGoodsAdapter(mContext, goodsList);
                                }
                                gridView.setAdapter(adapter);
                            }
                        } else {
                            if (page ==0) {
                                ivNothing.setVisibility(View.VISIBLE);
                                gridView.setVisibility(View.GONE);
                                Logger.i("ivNothing Visible 111111");
                            } else {
                                showToast(R.string.no_more_data);
                            }
                        }

                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                gridView.onRefreshComplete();
                showToast(R.string.net_work_not_available);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.v3Shop:
                Intent intent = new Intent(this, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_SHOPPING_CART_FRAGMENT);
                startActivity(intent);
                break;
            case R.id.v3Share:
                shareGoods();
                break;
        }
    }

    private void shareGoods() {
        getShrareUrl();
        if (null == mShareDialog){
            mShareDialog=new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());

            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    String content = getResources().getString(R.string.appshare);
                    new ShareUtile(mContext, ((IconModel) arg4).getType(), content, null, shareUrl).share();

                }
            });
        }
        mShareDialog.show();
        }

    public void getShrareUrl() {
        InvateNetRequest invateNetRequest=new InvateNetRequest(mContext);
        startMyDialog();
        invateNetRequest.invateNet(new RequestCallBack<InvateEntity>() {
            @Override
            public void onSuccess(ResponseInfo<InvateEntity> responseInfo) {
                stopMyDialog();
                switch (responseInfo.result.code){
                    case 1:
                        shrareUrl=responseInfo.result.url;
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast("请求分享失败");
            }
        });
    }
}

