package com.yilian.mylibrary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.bigkoo.pickerview.OptionsPickerView;

import java.util.ArrayList;

/**
 * @author Created by  on 2018/1/23.
 */

public class ShowAreaUtil {

    public <T, T1, T2> void showArea(final ArrayList<T> provinceList, @Nullable final ArrayList<ArrayList<T1>> cityList
            , @Nullable final ArrayList<ArrayList<ArrayList<T2>>> countyList, Context context, final IShowAreaUtil iShowAreaUtil) {
        if (provinceList == null || provinceList.size() <= 0) {
            Toast.makeText(context, "网络繁忙，请重试", Toast.LENGTH_SHORT).show();
            return;
        }
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {

            private Object t;
            private Object t2;
            private Object t1;

            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                t = provinceList.get(options1);
                if (cityList != null) {
                    t1 = cityList.get(options1).get(options2);
                }
                if (countyList != null) {
                    t2 = countyList.get(options1).get(options2).get(options3);
                }

                iShowAreaUtil.selectArea(t, t1, t2);
            }
        })

                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();
        if (cityList == null) {
            pvOptions.setPicker(provinceList);
        } else if (countyList == null) {
            pvOptions.setPicker(provinceList, cityList);
        } else {
            pvOptions.setPicker(provinceList, cityList, countyList);
        }
        pvOptions.show();
    }
}
