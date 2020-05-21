package com.leshan.ylyj.view.activity.bankmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.MainFragmentAdapter;
import com.leshan.ylyj.adapter.TwoTypeCardAdpter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.eventmodel.FirstEvent;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.RxBus;
import com.leshan.ylyj.view.activity.BaseFragment;
import com.leshan.ylyj.view.activity.PersonalFragment;
import com.leshan.ylyj.view.activity.PublicFragment;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.MenuUtil;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ToastUtil;

import java.util.ArrayList;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.TwoTypeEntity;


/**
 * 对公，个人卡
 */
public class TwoTypeCardActivity extends BaseActivity {

    private ImageView right_iv;

    private ArrayList<BaseFragment> fragmentList;
    private MainFragmentAdapter mAdpter;

    private String[] title = {"个人卡", "对公卡"};
    private NavigationTabStrip mTopNavigationTabStrip;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_type_card);


        StatusBarUtil.setColor(this, Color.WHITE);
        initToolbar();
        setToolbarTitle("支持身份证认证的银行卡");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initListener();
        initData();

    }

    @Override
    protected void initView() {
        intiNavigationBar();
        right_iv = findViewById(R.id.right_iv);
    }

    @Override
    protected void initListener() {
        right_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuUtil.showMenu(TwoTypeCardActivity.this, R.id.right_iv);
            }
        });

    }

    @Override
    protected void initData() {

    }


    private void intiNavigationBar() {
        mTopNavigationTabStrip = (NavigationTabStrip) findViewById(R.id.mediator_tab);
        mViewPager = findViewById(R.id.viewpager);
        mViewPager.setOffscreenPageLimit(2);


        startMyDialog();

        PublicFragment publicFragment = new PublicFragment();

        PersonalFragment personalFragment = new PersonalFragment();


        RxBus.getDefault().toObservable(FirstEvent.class)
                //在io线程进行订阅，可以执行一些耗时操作
                .subscribeOn(Schedulers.io())
                //在主线程进行观察，可做UI更新操作
                .observeOn(AndroidSchedulers.mainThread())
                //观察的对象
                .subscribe(new Action1<FirstEvent>() {
                    @Override
                    public void call(FirstEvent event) {
                        if (event.getCode().equals("GETPERSONAL")) {
                            if (event.getTitle().equals("SUCCESS")) {
                                stopMyDialog();
                            }
                        }
                    }
                });

        fragmentList = new ArrayList<>();
        fragmentList.add(personalFragment);
        fragmentList.add(publicFragment);

        mAdpter = new MainFragmentAdapter(getSupportFragmentManager(), fragmentList);
        mViewPager.setAdapter(mAdpter);

        mTopNavigationTabStrip.setViewPager(mViewPager, 0);
        mTopNavigationTabStrip.setTitles(title);
        mTopNavigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);
        mTopNavigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        mTopNavigationTabStrip.setTabIndex(0, true);

    }
}
