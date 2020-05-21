package com.yilian.mall.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.RedPacketFragment;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;

public class OpenRedPackageActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_red_package);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container,new RedPacketFragment());
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        //刷新奖励标识
        PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);
        super.onBackPressed();

    }
}
