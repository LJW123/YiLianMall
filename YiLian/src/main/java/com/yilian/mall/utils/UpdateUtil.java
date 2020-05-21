package com.yilian.mall.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.Get_newest_version_info;
import com.yilian.mall.http.ServiceNetRequest;
import com.yilian.mall.service.UpdateService;
import com.yilian.mall.ui.VersionUpdateDialog;
import com.yilian.mylibrary.AppManager;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by  on 2016/11/12 0012.
 */

public class UpdateUtil {
    private static ServiceNetRequest mAppNetRequest;
    private static final String NEED_UPDATE = "0";
    private static final String MUST_UPDATE = "1";

    /**
     * 检查版本是否需要更新
     *
     * @param context
     * @param isShowInfo 检查后是否提示toast
     */
    public static void checkUpDate(final Context context, boolean isShowInfo) {
        if (mAppNetRequest == null) {
            mAppNetRequest = new ServiceNetRequest(context);
        }
        mAppNetRequest.getNewVersionInfo(new RequestCallBack<Get_newest_version_info>() {

            @Override
            public void onSuccess(ResponseInfo<Get_newest_version_info> arg0) {

                Get_newest_version_info info = arg0.result;
//                将请求到的最新版本和本地版本进行比较
                if (info.androidVersion.compareTo(CommonUtils.getAppVersion(context)) <= 0) {
                    if (isShowInfo) {
                        android.widget.Toast.makeText(context, "已经是最新版本", android.widget.Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                if (info.getCode() == 1) {
                    /**
                     * 先判断最后一次强制更新版本 如果最后一次强制更新版本大于当前app版本 则强制更新
                     * 再判断type 0：可选择性更新 1：强制更新
                     */
                    Logger.i("update-" + info.getForce_version());
                    Logger.i("update-" + CommonUtils.getAppVersion(context));

                    Intent intent = new Intent(context, VersionUpdateDialog.class);
                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("version", info.androidVersion);
                    intent.putExtra("message", info.getInfo());

                    if (info.getForce_version().compareTo(CommonUtils.getAppVersion(context)) > 0) {
                        intent.putExtra("type", MUST_UPDATE);
                    } else if (NEED_UPDATE.equals(info.getAndroid_type())) {
                        intent.putExtra("type", NEED_UPDATE);
                    } else if (MUST_UPDATE.equals(info.getAndroid_type())){
                        intent.putExtra("type", MUST_UPDATE);
                    }

                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "异常：" + info.getCode(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
            }
        });
    }

    /**
     * 可以选择更新或暂不更新的dialog
     */
    public static void showNormalDialog(Get_newest_version_info info, Context mContext) {
        AlertDialog builder = new AlertDialog.Builder(mContext).create();
        builder.setTitle("新版本更新内容");
        builder.setMessage(info.getInfo());
        builder.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, UpdateService.class);
                mContext.startService(intent);
                dialog.dismiss();
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_red));
        builder.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.color_333));
    }

    /**
     * 强制更新的dialog
     */
    public static void showMustUpdateDialog(Get_newest_version_info info, Context mContext) {
        AlertDialog builder = new AlertDialog.Builder(mContext).create();
        builder.setTitle("新版本更新内容");
        builder.setMessage(info.getInfo());
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(mContext, UpdateService.class);
                mContext.startService(intent);
            }
        });
        builder.setCanceledOnTouchOutside(false);
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    AppManager.getInstance().AppExit(mContext);
                }
                return false;
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_red));
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UpdateService.class);
                mContext.startService(intent);
            }
        });
    }

}
