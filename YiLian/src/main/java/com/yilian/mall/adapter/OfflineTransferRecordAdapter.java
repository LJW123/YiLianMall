package com.yilian.mall.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.LayoutRes;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yilian.mall.R;
import com.yilian.mall.utils.DateUtils;
import com.yilian.networkingmodule.entity.OfflineTransferCardInfoEntity;
import com.yilianmall.merchant.utils.MoneyUtil;

/**
 * Created by Ray_L_Pain on 2017/9/4 0004.
 */

public class OfflineTransferRecordAdapter extends BaseQuickAdapter<OfflineTransferCardInfoEntity.DataBean, BaseViewHolder> {
    String type;

    public OfflineTransferRecordAdapter(@LayoutRes int layoutResId, String type) {
        super(layoutResId);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder helper, OfflineTransferCardInfoEntity.DataBean item) {
        OfflineTransferCardInfoEntity.DataBean data = item;
        switch (type) {
            case "OfflineTransferActivity":
                helper.setText(R.id.tv_time, DateUtils.timeStampToStr(Long.parseLong(data.transTime)));
                helper.setText(R.id.tv_money, MoneyUtil.set¥Money(MoneyUtil.getLeXiangBiNoZero(data.amount)));
                helper.setText(R.id.tv_phone, "充值账户：" + data.phone);
                TextView tvStatus = helper.getView(R.id.tv_status);
                if (!TextUtils.isEmpty(data.transStatus)) {
                    switch (data.transStatus) {
                        case "0":
                            tvStatus.setText("待处理");
                            tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_orange));
                            break;
                        case "1":
                            tvStatus.setText("已充值");
                            tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_green));
                            break;
                        case "2":
                            tvStatus.setText("已驳回");
                            tvStatus.setTextColor(mContext.getResources().getColor(R.color.color_red));
                            break;
                    }
                }
                break;
            case "OfflineTransferVoucherActivity":
                helper.setText(R.id.tv_bank_name, data.transBank);
                helper.setText(R.id.tv_user_name, data.transName);
                TextView tv = helper.getView(R.id.tv_bank_num);
//                bankCardNumAddSpace(et);
                String transCard = data.transCard;
                tv.setText(transCard.substring(transCard.length()-4,transCard.length()));
                break;
        }
    }

    public void showDialog(String bankName, String bankNum, String userName) {
        AlertDialog builder = new AlertDialog.Builder(mContext).create();
        builder.setMessage(bankName + "\n" + bankNum + "\n" + userName);
        builder.setButton(DialogInterface.BUTTON_POSITIVE, "知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
        builder.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(mContext.getResources().getColor(R.color.color_red));
    }

    private void bankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0; //记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }
}
