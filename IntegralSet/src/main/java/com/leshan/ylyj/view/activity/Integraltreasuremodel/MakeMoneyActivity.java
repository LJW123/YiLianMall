package com.leshan.ylyj.view.activity.Integraltreasuremodel;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.adapter.MakeMoneyAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * 赚取佣金
 */
public class MakeMoneyActivity extends BaseActivity {
    private Context mContext;

    private RecyclerView recyclerview;
    private MakeMoneyAdapter makeMoneyAdapter;

    private Dialog dialog;
    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_money);
        mContext = this;

        initToolbar();
        setToolbarTitle("赚取佣金");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);

        initView();

        initData();
        initListener();
    }

    @Override
    protected void initView() {
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initListener() {
        makeMoneyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                share();
            }
        });
    }

    @Override
    protected void initData() {
        List<String> list = new ArrayList<>();
        list.add("澳洲a2 Platinum 白金婴幼儿奶粉3段900g （-13） 新西兰原装进口");
        list.add("澳洲a2 Platinum 白金婴幼儿奶粉3段900g （-13） 新西兰原装进口");
        list.add("澳洲a2 Platinum 白金婴幼儿奶粉3段900g （-13） 新西兰原装进口");
        list.add("澳洲a2 Platinum 白金婴幼儿奶粉3段900g （-13） 新西兰原装进口");
        makeMoneyAdapter = new MakeMoneyAdapter(list);
        recyclerview.setAdapter(makeMoneyAdapter);

    }

    private void share() {
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(mContext).inflate(R.layout.make_money_pop_window_layout, null);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
