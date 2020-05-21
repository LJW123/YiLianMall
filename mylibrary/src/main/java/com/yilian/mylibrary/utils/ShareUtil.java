package com.yilian.mylibrary.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;

import com.yilian.mylibrary.R;
import com.yilian.mylibrary.umeng.IconModel;
import com.yilian.mylibrary.umeng.ShareUtile;
import com.yilian.mylibrary.umeng.UmengDialog;
import com.yilian.mylibrary.umeng.UmengGloble;

/**
 * @author xiaofo on 2018/8/1.
 */

public class ShareUtil {
    private UmengDialog mShareDialog = null;

    public ShareUtil(Context context) {
        mShareDialog = new UmengDialog(context,
                AnimationUtils.loadAnimation(context, R.anim.library_module_anim_wellcome_bottom), R.style.library_module_DialogControl,
                new UmengGloble().getAllIconModels());

    }

    public void share(final Context context, final String shareTitle, final String shareImgUrl, final String shareUrl) {
        mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                new ShareUtile(
                        context,
                        ((IconModel) arg4).getType(),
                        shareTitle,
                        shareImgUrl,
                        shareUrl).share();
            }
        });
    }

    public void share(final Context context, final String shareTitle, final String shareContent, final String shareImgUrl, final String shareUrl) {
        mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                new ShareUtile(
                        context,
                        ((IconModel) arg4).getType(),
                        shareContent,
                        shareTitle,
                        shareImgUrl,
                        shareUrl).share();
            }
        });
        mShareDialog.show();
    }
}
