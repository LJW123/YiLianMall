package com.yilian.luckypurchase.adapter;

import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jakewharton.rxbinding2.view.RxView;
import com.yilian.luckypurchase.R;
import com.yilian.luckypurchase.activity.LuckyRecordActivity;
import com.yilian.luckypurchase.utils.DateUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.networkingmodule.entity.LuckyInfoEntity;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * @author Ray_L_Pain
 * @date 2017/10/24 0024
 */

public class LuckyItemJoinRecordAdapter extends BaseQuickAdapter<LuckyInfoEntity.SnatchLog, BaseViewHolder> {

    public LuckyItemJoinRecordAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, final LuckyInfoEntity.SnatchLog item) {

        ImageView iv = helper.getView(R.id.iv);
        GlideUtil.showCirImage(mContext, item.photo, iv);
        RxView.clicks(iv)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        if (PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, mContext, false)) {
                            Intent intent = new Intent(mContext, LuckyRecordActivity.class);
                            intent.putExtra("userId", item.userId);
                            mContext.startActivity(intent);
                        } else {
                            Intent intent = new Intent();
                            intent.setComponent(new ComponentName(mContext, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                            mContext.startActivity(intent);
                        }
                    }
                });

        TextView tvName = helper.getView(R.id.tv_name);
        if (TextUtils.isEmpty(item.user_name)) {
            tvName.setText(R.string.lucky_user_no_name);
        } else {
            tvName.setText(item.user_name);
        }

        TextView tvJoinNum = helper.getView(R.id.tv_join_num);
        tvJoinNum.setText(Html.fromHtml("<font color=\"#666666\">参与人次 </font><font color=\"#fe5062\">" + item.count + "</font>"));

        TextView tvIp = helper.getView(R.id.tv_ip);
        tvIp.setText(item.ip_address + "( IP " + item.ip + " )");

        TextView tvTime = helper.getView(R.id.tv_time);
        tvTime.setText(DateUtils.luckyTime(item.time));

    }
}
