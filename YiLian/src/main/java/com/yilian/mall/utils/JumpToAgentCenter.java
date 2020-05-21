package com.yilian.mall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.widgets.MerchantUpdateDialog;
import com.yilian.mylibrary.Constants;
import com.yilianmall.merchant.activity.MerchantApplyAgentCenterActivity;

/**
 * Created by  on 2017/6/21 0021.
 */

public class JumpToAgentCenter {
    /**
     * 服务中心
     */
    public static void jumpToAgencyCenter(Context mContext) {
        //会员等级   0普通会员,1VIP会员,2个体商家,3实体商家,4市服务中心,5省服务中心
        String lev = PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext);
        if (NumberFormat.convertToInt(lev, 0) < 3) {
            new MerchantUpdateDialog.Builder(mContext)
                    .setTitle("需要升级为实体商家")
                    .setMessage("您还不是实体商家会员，由于目前商家入驻功能正在调整中，若需申请成为实体商家，请联系当地运营中心，" +
                            "或者直接联系益联益家客服400-152-5159")
                    .setCancelable(false)
                    .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ((Activity) mContext).finish();
                        }
                    })
                    .create().show();
        } else if (NumberFormat.convertToInt(lev, 0) == 3) {
//                提示可申请区域服务中心
            if ("1".equals(PreferenceUtils.readStrConfig(Constants.AGENT_STATUS, mContext, "0"))) {
//                new MerchantUpdateDialog.Builder(mContext)
//                        .setMessage("您已申请成为" + PreferenceUtils.readStrConfig(Constants.AGENT_REGION, mContext) + "服务中心,请您耐心等待工作人员和您联系")
//                        .setCancelable(false)
//                        .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                ((Activity) mContext).finish();
//
//                            }
//                        }).create().show();
                new AlertDialog.Builder(mContext)
                        .setMessage("您已申请成为" + PreferenceUtils.readStrConfig(Constants.AGENT_REGION, mContext) + "服务中心,请您耐心等待工作人员和您联系")
                        .setCancelable(false)
                        .setNegativeButton("知道了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ((Activity) mContext).finish();

                            }
                        }).create().show();
            } else {
                new MerchantUpdateDialog.Builder(mContext)
                        .setTitle("申请成为区域服务中心")
                        .setMessage("申请成为区域服务中心后,平台商务会通过此账号绑定的手机号与您联系,您确定要申请成为区域服务中心吗?")
                        .setCancelable(false)
                        .setNegativeButton("点错了", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                ((Activity) mContext).finish();

                            }
                        })
                        .setPositiveButton("确定申请", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                            applyAgent(mContext);
                                mContext.startActivity(new Intent(mContext, MerchantApplyAgentCenterActivity.class));
                                dialog.dismiss();
                                ((Activity) mContext).finish();
                            }
                        }).create().show();
            }

        } else if (NumberFormat.convertToInt(lev, 0) > 3) {
//                跳转服务中心
            Intent intent = new Intent(mContext, WebViewActivity.class);
            intent.putExtra(Constants.SPKEY_URL, PreferenceUtils.readStrConfig(Constants.AGENT_URL, mContext));
            mContext.startActivity(intent);
            ((Activity) mContext).finish();
        }
    }
}
