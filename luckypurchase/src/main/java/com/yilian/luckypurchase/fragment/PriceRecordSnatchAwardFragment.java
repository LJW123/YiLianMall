package com.yilian.luckypurchase.fragment;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyPrizeDetailsActivity;
import com.yilian.luckypurchase.activity.LuckyShowOffListActivity;
import com.yilian.luckypurchase.adapter.SnatchAwardAdapter;
import com.yilian.luckypurchase.umeng.IconModel;
import com.yilian.luckypurchase.umeng.ShareUtile;
import com.yilian.luckypurchase.umeng.UmengDialog;
import com.yilian.luckypurchase.umeng.UmengGloble;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.entity.UserAddressLists;
import com.yilian.networkingmodule.entity.SnatchAwardListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * @authorCreated by LYQ on 2017/10/30.
 * 中奖记录
 */

public class PriceRecordSnatchAwardFragment extends BaseMyRecordFragment {

    private static final int REQUEST_CODE = 7;
    private SnatchAwardAdapter adapter;
    private String activityId;
    private String activityIdValue;

    private boolean isRefresh = false;
    private UmengDialog mShareDialog;
    private String share_title, share_content, shareImg, share_url, goodsName, goodsImg;

    @Override
    public void onResume() {
        super.onResume();

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_PRZIE_DETAIL, mContext, false);

        if (isRefresh) {
            getData();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_PRZIE_DETAIL, isRefresh, mContext);
        }
    }

    /**
     * 分享
     */
    private void share() {
        share_title = "幸运GO,GO精彩! 揣上点儿益豆进来转转吧,说不定就有惊喜等着你呢!";
        share_content = "我在幸运GO成功夺了 —" + goodsName;
        share_url = Constants.LUCKY_SHARE_URL;
        shareImg = goodsImg;
        if (shareImg.startsWith("http://") || shareImg.startsWith("https://")) {

        } else {
            shareImg = Constants.ImageUrl + shareImg;
        }

        Logger.i("share-" + share_title);
        Logger.i("share-" + share_content);
        Logger.i("share-" + shareImg);
        Logger.i("share-" + share_url);

        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(mContext, R.anim.lucky_anim_wellcome_bottom), R.style.lucky_DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url)
                            .share();
                }
            });
        }
        mShareDialog.show();
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }

    @Override
    public void getData() {
        if (null == adapter) {
            adapter = new SnatchAwardAdapter(R.layout.lucky_item_record_prize_record);
            recyclerView.setAdapter(adapter);
            adapter.setOnLoadMoreListener(this, recyclerView);
            initAdapterListener();
        }
        RetrofitUtils2.getInstance(mContext).setToken(RequestOftenKey.getToken(mContext)).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                .getSnatchAwardList(page, userId, new Callback<SnatchAwardListEntity>() {
                    @Override
                    public void onResponse(Call<SnatchAwardListEntity> call, Response<SnatchAwardListEntity> response) {
                        HttpResultBean bean = response.body();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, bean)) {
                            if (CommonUtils.serivceReturnCode(mContext, bean.code, bean.msg)) {
                                switch (bean.code) {
                                    case 1:
                                        List<SnatchAwardListEntity.SnatchInfoBean> snatchInfo = response.body().snatchInfo;
                                        if (page > 0) {
                                            if (null == snatchInfo || snatchInfo.size() <= 0) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                            adapter.addData(snatchInfo);
                                        } else {
                                            if (null == snatchInfo || snatchInfo.size() <= 0) {
                                                adapter.setEmptyView(getEmptyView());
                                            } else {
                                                adapter.setNewData(snatchInfo);
                                            }
                                            if (snatchInfo.size() < Constants.PAGE_COUNT) {
                                                adapter.loadMoreEnd();
                                            } else {
                                                adapter.loadMoreComplete();
                                            }
                                        }
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<SnatchAwardListEntity> call, Throwable t) {
                        refreshLayout.setRefreshing(false);
                        if (page > 0) {
                            showToast(R.string.aliwx_net_null_setting);
                        } else {
                            adapter.setEmptyView(getErrorView());
                        }
                    }
                });
    }

    private void initAdapterListener() {
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                SnatchAwardListEntity.SnatchInfoBean item = (SnatchAwardListEntity.SnatchInfoBean) adapter.getItem(position);
                activityIdValue = item.activityId;
                PriceRecordSnatchAwardFragment.this.activityId = item.activityId;
                int i = view.getId();
                if (i == R.id.tv_share) {
                    //分享
                    goodsName = item.snatchName;
                    goodsImg = item.goodsUrl;
                    share();
                } else if (i == R.id.tv_show_line) {
                    //区别是晒单还是设置收货地址
                    TextView tvShowLine = (TextView) view.findViewById(R.id.tv_show_line);
                    String textStr = tvShowLine.getText().toString().trim();
                    if (textStr.equals("晒单")) {
                        Intent intent = new Intent(mContext, LuckyShowOffListActivity.class);
                        intent.putExtra("activityId", item.activityId);
                        startActivity(intent);
                    } else if (textStr.equals("收货地址")) {
                        //发送广播
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.AddressManageActivity"));
                        intent.putExtra("FLAG", "orderIn");
                        intent.putExtra(Constants.ADDRESS_ID_SELECTED, "");
                        startActivityForResult(intent, REQUEST_CODE);
                    } else if (textStr.equals("确认收货")) {
                        //确认收货
                        confirmGoods();
                    } else if (textStr.equals("待收货")) {

                    }
                } else if (i == R.id.ll_content) {
                    //中奖详情
                    Intent intent = new Intent(mContext, LuckyPrizeDetailsActivity.class);
                    intent.putExtra("activityId", item.activityId);
                    startActivity(intent);
                }
            }
        });
    }

    private void confirmGoods() {
        showSystemV7Dialog("提醒", "请确定您已经收到货物，是否确认收货？", "确认", "否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_POSITIVE:
                        refreshLayout.setRefreshing(true);
                        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                                .snatchConfirmGoods(activityIdValue, new Callback<HttpResultBean>() {
                                    @Override
                                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                                switch (response.body().code) {
                                                    case 1:
                                                        showToast(response.body().msg);
                                                        refreshAdapterItem();
                                                        break;
                                                    default:
                                                        break;
                                                }
                                            }
                                        }
                                        refreshLayout.setRefreshing(false);
                                    }

                                    @Override
                                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                                        refreshLayout.setRefreshing(false);
                                        showToast(R.string.aliwx_net_null_setting);
                                    }
                                });
                        dialog.dismiss();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (null != data) {
                        UserAddressLists addressLists = (UserAddressLists) data.getSerializableExtra("USE_RADDRESS_LIST");

                        final String address_id = addressLists.address_id;
                        if (!TextUtils.isEmpty(address_id) && !TextUtils.isEmpty(activityId)) {
                            showSystemV7Dialog(null, "我们会将您的夺宝商品发送到该地址，请耐心等待~", "确定", "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case DialogInterface.BUTTON_NEGATIVE:
                                            dialog.dismiss();
                                            break;
                                        case DialogInterface.BUTTON_POSITIVE:
                                            setAddress(address_id, activityId);
                                            dialog.dismiss();
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            });
                        }
                        Logger.i("接受的address： " + addressLists.address);
                    }
                }
                break;
            default:
                Logger.i(" REQUEST_CODE   " + requestCode);
                break;
        }
    }

    private void setAddress(String address_id, String activityId) {
        RetrofitUtils2.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext)).setToken(RequestOftenKey.getToken(mContext))
                .snatchSetAddress(activityId, address_id, new Callback<HttpResultBean>() {
                    @Override
                    public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                        stopMyDialog();
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            if (CommonUtils.serivceReturnCode(mContext, response.body().code, response.body().msg)) {
                                switch (response.body().code) {
                                    case 1:
                                        showToast(response.body().msg);
                                        refreshAdapterItem();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HttpResultBean> call, Throwable t) {
                        stopMyDialog();
                        showToast(R.string.aliwx_net_null_setting);
                    }
                });
    }

    private void refreshAdapterItem() {
        refreshLayout.setRefreshing(true);
        getData();
    }
}
