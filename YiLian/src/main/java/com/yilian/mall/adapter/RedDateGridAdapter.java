package com.yilian.mall.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.RedPacketDialog2;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DPXUnitUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.event.RefreshRedPacketStatus;
import com.yilian.networkingmodule.entity.RedPacketDateEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/11/24 0024.
 */

public class RedDateGridAdapter extends android.widget.BaseAdapter {
    private final RefreshRedPacketStatus refreshRedPacketStatus;
    private Context mContext;
    private ArrayList<RedPacketDateEntity.ListArrBean> list;
    private int diffSize;
    private int screenWidth;
    private float userIntegral;

    public RedDateGridAdapter(Context mContext, ArrayList<RedPacketDateEntity.ListArrBean> list, int diffSize, float userIntegral, RefreshRedPacketStatus refreshRedPacketStatus) {
        this.refreshRedPacketStatus = refreshRedPacketStatus;
        this.mContext = mContext;
        this.list = list;
        this.diffSize = diffSize;
        this.userIntegral = userIntegral;
        screenWidth = ScreenUtils.getScreenWidth(mContext);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DateHolder holder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_red_date, null);
            holder = new DateHolder();
            holder.layout = (LinearLayout) convertView.findViewById(R.id.layout);
            holder.iv = (ImageView) convertView.findViewById(R.id.iv);
            holder.tv = (TextView) convertView.findViewById(R.id.tv);

            convertView.setTag(holder);
        } else {
            holder = (DateHolder) convertView.getTag();
        }

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.layout.getLayoutParams();
        params.width = (screenWidth - DPXUnitUtil.dp2px(mContext, 60) - DPXUnitUtil.dp2px(mContext, 30)) / 7;
        params.height = params.width;

        RedPacketDateEntity.ListArrBean bean = list.get(position);

        if (bean != null) {
            switch (bean.packetExist) {
                case "0":
                    holder.layout.setVisibility(View.VISIBLE);
                    holder.iv.setImageResource(R.mipmap.red_fragment_adapter_status1);
                    holder.tv.setVisibility(View.VISIBLE);
                    holder.tv.setText(String.valueOf(position - diffSize));
                    break;
                case "1":
                    holder.layout.setVisibility(View.VISIBLE);
                    holder.tv.setVisibility(View.VISIBLE);
                    holder.tv.setText(String.valueOf(position - diffSize));
                    switch (bean.packetInfo.status) {
                        case "0":
                            holder.iv.setImageResource(R.mipmap.red_fragment_adapter_status2);
                            holder.layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, RedPacketDialog2.class);
                                    intent.putExtra("red_id", bean.packetInfo.id);
                                    intent.putExtra("red_money", bean.packetInfo.amount);
                                    mContext.startActivity(intent);
                                }
                            });
                            break;
                        case "1":
                            holder.iv.setImageResource(R.mipmap.red_fragment_adapter_status3);
                            break;
                        case "2":
                            holder.iv.setImageResource(R.mipmap.red_fragment_adapter_status4);
                            holder.layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog builder = new AlertDialog.Builder(mContext).create();
                                    builder.setMessage("该奖励已失效，如需拆取需要消耗" + MoneyUtil.getLeXiangBiNoZero(bean.packetInfo.amount) + "奖券激活，是否立即激活该奖励？");
                                    builder.setButton(DialogInterface.BUTTON_NEGATIVE, "再想想", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setButton(DialogInterface.BUTTON_POSITIVE, "立即激活", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activateOneRed(bean);
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                    builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_red));
                                    builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.color_333));
                                }
                            });
                            break;
                        case "3":
                            holder.iv.setImageResource(R.mipmap.red_fragment_adapter_status2);
                            holder.layout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, RedPacketDialog2.class);
                                    intent.putExtra("red_id", bean.packetInfo.id);
                                    intent.putExtra("red_money", bean.packetInfo.amount);
                                    mContext.startActivity(intent);
                                }
                            });
                            break;
                        default:
                            break;
                    }
                    break;
                case "2":
                    holder.layout.setVisibility(View.INVISIBLE);
                    holder.tv.setVisibility(View.GONE);
                    break;
                default:
                    holder.layout.setVisibility(View.INVISIBLE);
                    holder.tv.setVisibility(View.GONE);
                    break;
            }
        } else {
            holder.layout.setVisibility(View.INVISIBLE);
            holder.tv.setVisibility(View.GONE);
        }

        return convertView;
    }


    public void activateOneRed(RedPacketDateEntity.ListArrBean bean) {
        /**
         * 拆取单个未激活奖励也需要比较用户奖券和当前拆取需要的奖券     201-11-27
         */
        float needIntegral = Float.parseFloat(bean.packetInfo.amount) / 100;
        Logger.i("2017年11月27日 11:11:02-" + needIntegral);
        Logger.i("2017年11月27日 11:11:02-" + userIntegral);

        if (needIntegral > userIntegral) {
            Toast.makeText(mContext, "当前奖券不足，无法激活！", 0).show();
        } else {
            RetrofitUtils2.getInstance(mContext).activityRedPacket(bean.packetInfo.id, new Callback<HttpResultBean>() {
                @Override
                public void onResponse(Call<HttpResultBean> call, Response<HttpResultBean> response) {
                    HttpResultBean body = response.body();
                    if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                        if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                            switch (body.code) {
                                case 1:
                                    PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);

                                    Intent intent = new Intent(mContext, RedPacketDialog2.class);
                                    intent.putExtra("red_id", bean.packetInfo.id);
                                    intent.putExtra("red_money", MoneyUtil.getLeXiangBiNoZero(bean.packetInfo.amount));
                                    mContext.startActivity(intent);
                                    break;
                                default:
                                    break;
                            }
                        } else {
//                             拆取奖励时，若奖励状态异常则刷新奖励数据
                            refreshRedPacketStatus.setOnRefreshRedPacketStatus();
                        }
                    }
                }

                @Override
                public void onFailure(Call<HttpResultBean> call, Throwable t) {
                    Toast.makeText(mContext, R.string.net_work_busy, 0).show();
                }
            });
        }
    }

    class DateHolder {
        LinearLayout layout;
        ImageView iv;
        TextView tv;
    }
}
