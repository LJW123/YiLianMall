package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yilian.mall.R;
import com.yilian.mall.ui.WheelOfFortuneActivity;
import com.yilian.mall.utils.WebImageUtil;
import com.yilian.mylibrary.GlideUtil;

/**
 * Created by  on 2017/4/27 0027.
 */

public class WheelOfFortunePrizeDialog extends Dialog implements View.OnClickListener {
    private  String imageUrl;
    private  WheelOfFortuneActivity activity;
    private Button btnClose;
    private ImageView ivWheelPrize;
    private ImageButton ibTryAgain;
    private RelativeLayout rlPrizeContent;
    private WheelOfFortunePrizeDialogListener listener;

    public WheelOfFortunePrizeDialog(@NonNull Context context,String imageUrl) {
        super(context);
        this.imageUrl = imageUrl;
    }

    public WheelOfFortunePrizeDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected WheelOfFortunePrizeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_wheel_of_frotune_prize);
        btnClose = (Button) findViewById(R.id.btn_close);
        ivWheelPrize = (ImageView) findViewById(R.id.iv_wheel_prize);
        GlideUtil.showImage(getContext(), WebImageUtil.getInstance().getWebImageUrlWithSuffix(imageUrl),ivWheelPrize);
        ibTryAgain = (ImageButton) findViewById(R.id.ib_try_again);
        btnClose.setOnClickListener(this);
        ivWheelPrize.setOnClickListener(this);
        ibTryAgain.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v);

    }

    public void setListener(WheelOfFortunePrizeDialogListener listener){
        this.listener = listener;
    }
   public  interface WheelOfFortunePrizeDialogListener{
        public void onClick(View view);
    }
}
