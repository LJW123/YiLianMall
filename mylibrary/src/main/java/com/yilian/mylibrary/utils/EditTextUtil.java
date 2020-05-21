package com.yilian.mylibrary.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.yilian.mylibrary.DecimalUtil;

/**
 * @author xiaofo on 2018/11/17.
 */

public class EditTextUtil {
    public static EditTextUtil getInstance(){
        return Holder.INSTANCE;
    }
    private static class Holder {
        private static final EditTextUtil INSTANCE = new EditTextUtil();
    }
    public  void keepDecimal(final EditText editText, final int length, final AbstractTextWatcher textWatcher){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                textWatcher.beforeTextChanged(s,start,count,after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                textWatcher.onTextChanged(s,start,before,count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                DecimalUtil.keepDecimal(s.toString(),editText,length);
                textWatcher.afterTextChanged(s);
            }
        });
    }


}
