package com.yilian.mall.listener;

import android.view.View;
import java.util.ArrayList;

public interface MyItemClickListener {

    //    价格筛选回调
    void onOneClick(View view, int position, String title, String sorts, int page,String keyWord);

    //  距离筛选回调
    void onTwoClick(String title, String distance, String zoneid, String locationid, String key);

    //   星级价格筛选结果回调
    void onThereClick(boolean priceIsCheck,String minprice, String maxprice, ArrayList<String> startlevelList, String showStr);

    //   综合筛选回调
    void onFourClick(String themeTitle, String themeId,String brandid,String facilityid,String bedid,String grade,String keyWordId);
}
