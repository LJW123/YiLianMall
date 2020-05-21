package com.yilian.luckypurchase.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.yilian.luckypurchase.R;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.widget.MyLoading;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by  on 2017/8/16 0016.
 */

public abstract class BaseFragment extends Fragment {
    protected Context mContext;
    public View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mContext == null) {
            mContext = getActivity();
        }
        if (rootView == null) {
            rootView = createView(inflater, container, savedInstanceState);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        GlideUtil.resumeRequests(getActivity().getApplicationContext());

    }

    protected CompositeSubscription compositeSubscription;

    public void addSubscription(Subscription subscription) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(subscription);

    }


    /**
     * 弹出V7版本系统弹窗
     *
     * @param title
     * @param message
     * @param positiveText
     * @param negativeText
     * @param onClickListener
     */
    public void showSystemV7Dialog(@Nullable String title, @Nullable String message, @Nullable String positiveText, @Nullable String negativeText,
                                   @Nullable DialogInterface.OnClickListener onClickListener) {
        AlertDialog alertDialog = new AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, onClickListener)
                .setNegativeButton(negativeText, onClickListener)
                .create();
        alertDialog.show();
        alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_red));
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(mContext.getResources().getColor(R.color.library_module_color_333));

    }

    /**
     * 加载化布局
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    protected abstract View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);


    /**
     * dialog 启动
     */
    public void startMyDialog() {

        if (isAdded() && getActivity() != null && !getActivity().isFinishing()) {
            if (myloading == null) {
                myloading = MyLoading.createLoadingDialog(mContext);
                Logger.i(this.getClass().getName() + "  创建了了dialog  " + this.toString());
            }
            try {//捕获异常，处理 is your activity running的异常
                myloading.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(20000);
                            if (myloading != null && myloading.isShowing()) {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stopMyDialog();
                                    }
                                });
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Logger.i(this.getClass().getName() + "  弹出了dialog  " + this.toString());
            } catch (Exception e) {
                Logger.i("异常信息：" + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    protected MyLoading myloading;

    /**
     * dialog 销毁
     */
    public void stopMyDialog() {
        if (myloading != null && getActivity() != null && !getActivity().isFinishing() && isAdded()) {
            try {
                myloading.dismiss();
                Logger.i(this.getClass().getName() + "  取消了dialog  " + this.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i(this.getClass().getName() + "  取消dialog时异常了  " + this.toString());
            }
        }
    }

    public void showToast(String msg) {
        if (isAdded()) {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(int strId) {
        if (isAdded()) {
            Toast.makeText(getActivity(), getResources().getString(strId), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
        GlideUtil.pauseRequests(getActivity().getApplicationContext());
        super.onDestroy();

    }
}
