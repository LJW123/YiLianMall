package com.yilianmall.merchant.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.yilianmall.merchant.R;
import com.yilianmall.merchant.activity.MerchantPayActivity;

/**
 * Created by  on 2017/6/13 0013.
 */

public class OverDueDialog extends Dialog {
    public OverDueDialog(@NonNull Context context) {
        super(context);
    }

    public OverDueDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected OverDueDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private final Context context;
        private String time;

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * 格式 ****年*月*日
         *
         * @param time
         */
        public Builder setOverDueTime(String time) {
            this.time = time;
            return this;
        }

        public OverDueDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View inflate = inflater.inflate(R.layout.merchant_overdue, null);
            final OverDueDialog overDueDialog = new OverDueDialog(context, R.style.merchant_Dialog);
            overDueDialog.addContentView(inflate, new ViewGroup.LayoutParams(
                    900, 1080));
            if (!TextUtils.isEmpty(time)) {
                ((TextView) inflate.findViewById(R.id.tv_overdue)).setText("您好，此商户的平台运营费\n已在" + time + "到期");
            }
            Button btnRenew = (Button) inflate.findViewById(R.id.btn_renew);
            btnRenew.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overDueDialog.dismiss();
                    context.startActivity(new Intent(context, MerchantPayActivity.class));
                }
            });
            Button btnJumpToMemberCenter = (Button) inflate.findViewById(R.id.btn_jump_to_member_center);
            btnJumpToMemberCenter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    overDueDialog.dismiss();
                    ((Activity) context).finish();
                }
            });
            overDueDialog.setCanceledOnTouchOutside(false);
            return overDueDialog;
        }

    }
}
