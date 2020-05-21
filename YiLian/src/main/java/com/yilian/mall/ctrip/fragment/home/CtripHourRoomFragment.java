package com.yilian.mall.ctrip.fragment.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.JPBaseFragment;


/**
 * 携程 钟点房
 *
 * @author Created by Zg on 2018/8/15.
 */
public class CtripHourRoomFragment extends JPBaseFragment implements View.OnClickListener {


    @Override
    public View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.ctrip_fragment_hour, null);
        }
        initView();
        initData();
        initListener();
        return rootView;
    }

    private void initView() {


    }


    public void initData() {

    }


    private void initListener() {


    }



    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.v3Back:
//                finish();
//                break;
//            default:
//                break;
//        }
    }



}
