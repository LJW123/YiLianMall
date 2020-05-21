package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MallGoodsList;
import com.yilian.mall.entity.MallGoodsListEntity;
import com.yilian.mall.http.MallNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.pulltorefresh.library.PullToRefreshBase;
import com.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页传关键字显示搜索结果界面
 * Created by Administrator on 2016/8/3.
 */
public class MallListKyeWordActivity extends BaseActivity {

    @ViewInject(R.id.lv_find_data)
    private PullToRefreshListView lvFindData;//城市 商家的listview

    @ViewInject(R.id.iv_no_date)
    private ImageView imgView_no_date;

    @ViewInject(R.id.tv_back)
    private TextView tv_back;

    private int page=0;

    private String keyWord,type;
    private MallNetRequest mallNetRequest;

    private MallGoodsList goodsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mall_list_key_word);
        ViewUtils.inject(this);
        keyWord = getIntent().getStringExtra("keyword");
        type = getIntent().getStringExtra("type");

        tv_back.setText("乐分精品");

        switch (type) {
//            case "city":
//                edKeyWord.setHint("请输入城市名称");
//                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
//                break;
//
//            case "merchant":
//                merchantAdapter = new MerchantAdapter(mContext, merchantLists);
//                lvFindData.setAdapter(merchantAdapter);
//                edKeyWord.setHint("请输入商家名称");
//                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
//                break;

            case "goods":
                goodsListAdapter = new MallGoodsList(mContext, allGoodsLists);
                lvFindData.setAdapter(goodsListAdapter);
//                edKeyWord.setHint("请输入商品名称");
                lvFindData.setMode(PullToRefreshBase.Mode.BOTH);
                break;

//            case "oneBuy":
//                edKeyWord.setHint("请输入活动名称");
//                getHotKeyWord();
//                lvFindData.setMode(PullToRefreshBase.Mode.DISABLED);
//                lvFindData.setVisibility(View.GONE);
//                gridFindVData.setVisibility(View.VISIBLE);
//                break;
//
//            default:
//                edKeyWord.setText("请输入要搜索的关键字");
//                break;
        }
        initListView();
        switchKeyWord(keyWord);
    }

    private void initListView() {

        lvFindData.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 0;
                switchKeyWord(keyWord);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                switchKeyWord(keyWord);
            }

        });
    }

    private void switchKeyWord(String keyWord) {
        startMyDialog();
        switch (type) {
            case "city":
//                getCityList(keyWord);
                break;

            case "merchant":
//                getMerchant(keyWord);

                break;

            case "goods":
                getGoodsList(keyWord);
                break;

            case "afterSale":
//                getAfterSale(keyWord);
                break;

            case "oneBuy":
//                getOneBuy(keyWord);
                break;
            default:
                stopMyDialog();
                showToast("搜索类型错误");

                break;
        }
    }

    private List<MallGoodsListEntity.MallGoodsLists> allGoodsLists = new ArrayList<MallGoodsListEntity.MallGoodsLists>();

    private void getGoodsList(final String keyWord) {


        startMyDialog();
        if (mallNetRequest==null){
            mallNetRequest = new MallNetRequest(mContext);
        }

        mallNetRequest.mallSearchList(getIntent().getStringExtra("order_Type"), keyWord, page, MallGoodsListEntity.class, new RequestCallBack<MallGoodsListEntity>() {
            @Override
            public void onSuccess(ResponseInfo<MallGoodsListEntity> responseInfo) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
                switch (responseInfo.result.code) {
                    case 1:

                        Logger.json(responseInfo.result.toString());
                        if (page == 0) {
                            allGoodsLists.clear();
                        }

                        if (responseInfo.result.list != null && responseInfo.result.list.size() != 0) {
                            allGoodsLists.addAll(responseInfo.result.list);
                            goodsListAdapter.notifyDataSetChanged();
                        }

                        if (responseInfo.result.list.size() == 0) {
                            showToast("没有数据了");
                        }

                        if (allGoodsLists == null || allGoodsLists.size() == 0) {
                            imgView_no_date.setVisibility(View.VISIBLE);
                            imgView_no_date.setImageDrawable(getResources().getDrawable(R.mipmap.mall_list_nodata));
                            lvFindData.setVisibility(View.GONE);
                        } else {
                            imgView_no_date.setVisibility(View.GONE);
                            lvFindData.setVisibility(View.VISIBLE);
                        }
                        page++;

                        break;
                    default:

                        break;
                }

            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                lvFindData.onRefreshComplete();
            }
        });
    }
}
