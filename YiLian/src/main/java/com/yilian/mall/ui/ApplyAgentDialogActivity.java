package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yilian.mall.utils.JumpToAgentCenter;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;

public class ApplyAgentDialogActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.apply_agent_activity_dialog);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        JumpToAgentCenter.jumpToAgencyCenter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, true, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        PreferenceUtils.writeBoolConfig(Constants.APP_IS_FOREGROUND, false, this);
    }

}
