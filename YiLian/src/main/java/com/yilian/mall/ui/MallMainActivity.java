package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseFragmentActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.fragment.MallFragment;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.SharedPreferencesUtils;
import com.yilian.mall.widgets.SlidingTabLayout;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.networkingmodule.entity.ClassifyListV2Entity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ray_L_Pain on 2017/7/19 0019.
 */

public class MallMainActivity extends BaseFragmentActivity {
    @ViewInject(R.id.v3Back)
    ImageView ivBack;
    @ViewInject(R.id.v3Title)
    TextView tvTitle;
    @ViewInject(R.id.v3Shop)
    ImageView ivSearch;
    @ViewInject(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @ViewInject(R.id.vp)
    ViewPager vp;

    private YhsViewpagerAdapter adapter;
    //商品分类数组
    private ClassifyListV2Entity.ListBean[] newCateorysList = new ClassifyListV2Entity.ListBean[]{};
    //易划算或是奖券购
    String titleStr;
    String type;
    SharedPreferencesUtils spUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            savedInstanceState.remove("android:support:fragments");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_yhs);
        ViewUtils.inject(this);

        initView();
        initData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private void initData() {
        getClassifyListV2();
    }

    /**
     * 获取易划算/奖券购分类列表
     */
    private void getClassifyListV2() {
        if ("1".equals(type)) {
            spUtils = new SharedPreferencesUtils(context, Constants.YHS_CATEGORY_LIST);
            newCateorysList = (ClassifyListV2Entity.ListBean[]) spUtils.getObject(Constants.YHS_CATEGORY_LIST, Object.class);
        } else if ("2".equals(type)) {
            spUtils = new SharedPreferencesUtils(context, Constants.JFG_CATEGORY_LIST);
            newCateorysList = (ClassifyListV2Entity.ListBean[]) spUtils.getObject(Constants.JFG_CATEGORY_LIST, Object.class);
        }

        startMyDialog();
        RetrofitUtils2.getInstance(context).getClassifyListV2(type, new Callback<ClassifyListV2Entity>() {
            @Override
            public void onResponse(Call<ClassifyListV2Entity> call, Response<ClassifyListV2Entity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(context, body)) {
                    if (CommonUtils.serivceReturnCode(context, body.code, body.msg)) {
                        switch (body.code) {
                            case 1:
                                Logger.i("2017年8月31日 14:32:28-" + "进行网络请求");
                                ArrayList<ClassifyListV2Entity.ListBean> fromNetList = response.body().list;
                                ClassifyListV2Entity.ListBean firstCategory = new ClassifyListV2Entity.ListBean();
                                firstCategory.name = "上新";
                                fromNetList.add(0, firstCategory);
                                newCateorysList = new ClassifyListV2Entity.ListBean[fromNetList.size()];
                                for (int i = 0; i < fromNetList.size(); i++) {
                                    newCateorysList[i] = fromNetList.get(i);
                                }

                                initViewPager();

                                //存下来
                                if ("1".equals(type)) {
                                    spUtils.setObject(Constants.YHS_CATEGORY_LIST, newCateorysList);
                                } else if ("2".equals(type)) {
                                    spUtils.setObject(Constants.JFG_CATEGORY_LIST, newCateorysList);
                                }
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ClassifyListV2Entity> call, Throwable t) {
                Logger.i("2017年8月31日 14:32:28-" + "sp去数据");
                //网络请求失败从之前存的取
                if ("1".equals(type)) {
                    newCateorysList = (ClassifyListV2Entity.ListBean[]) spUtils.getObject(Constants.YHS_CATEGORY_LIST, Object.class);
                } else if ("2".equals(type)) {
                    newCateorysList = (ClassifyListV2Entity.ListBean[]) spUtils.getObject(Constants.JFG_CATEGORY_LIST, Object.class);
                }

                initViewPager();

                if (newCateorysList == null || newCateorysList.length <= 0) {
                    showToast("分类获取异常,请检查您的网络状况后,刷新页面重试");//获取失败，且从本地没有取到，才进行该提示
                }
            }
        });
    }

    private void initViewPager() {
        vp.setOffscreenPageLimit(1);
        if (adapter == null) {
            adapter = new YhsViewpagerAdapter(getSupportFragmentManager());
        }
        vp.setAdapter(adapter);
        tabLayout.setViewPager(vp);
        tabLayout.setVisibility(View.VISIBLE);
        stopMyDialog();
    }

    private void initView() {
        titleStr = getIntent().getStringExtra("title");
        if ("易划算".equals(titleStr)) {
            type = "1";
        } else if ("奖券购".equals(titleStr)) {
            type = "2";
        } else {
            type = "0";
        }

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvTitle.setText(titleStr);
        ivSearch.setVisibility(View.VISIBLE);
        ivSearch.setImageDrawable(getResources().getDrawable(R.mipmap.icon_search60));
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JPFindActivity.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
    }

    class YhsViewpagerAdapter extends FragmentPagerAdapter {

        public YhsViewpagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            MallFragment mallFragment = new MallFragment();
            Bundle bundle = new Bundle();
            bundle.putString("category", type);
            bundle.putString("categoryId", newCateorysList[position].id);
            mallFragment.setArguments(bundle);
            return mallFragment;
        }

        @Override
        public int getCount() {
            if (newCateorysList != null) {
                return newCateorysList.length;
            } else {
                return 0;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return newCateorysList[position].name;
        }
    }
}
