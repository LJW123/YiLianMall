package com.leshan.ylyj.view.activity.healthmodel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.HealthPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.StatusBarUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.HealthMyFamilyBean;


/**
 * 我的家庭健康
 *
 * @author Ray_L_Pain
 */
public class MyFamilyHealthActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rootLayout;
    private RelativeLayout titleLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView tvTitle;

    private ArrayList<Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>>> list = new ArrayList<>();
    Map<Integer, Integer> map = new HashMap();
    private ImageView ivBack, ivBackBlack;
    private TextView tvRight;
    private LinearLayout layoutNetError;
    private RelativeLayout layoutEmpty;
    private TextView tvRefresh;

    private HealthPresenter presenter;
    private boolean isRefresh = false;
    private int bannerHeight = 400;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_family_health);
        presenter = new HealthPresenter(mContext, this, 0);

        initView();
        initListener();
        initData();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
            params.height += StatusBarUtils.getStatusBarHeight(mContext);
            titleLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            StatusBarUtil.setTransparentForImageViewInFragment(MyFamilyHealthActivity.this, null);
        }
    }

    @Override
    protected void initView() {
        rootLayout = findViewById(R.id.root_layout);
        titleLayout = findViewById(R.id.layout_title);
        ivBack = findViewById(R.id.iv_back);
        ivBackBlack = findViewById(R.id.iv_back_black);
        tvTitle = findViewById(R.id.tv_title);
        tvRight = findViewById(R.id.tv_right);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        layoutNetError = findViewById(R.id.layout_net_error);
        tvRefresh = findViewById(R.id.tv_refresh);
        layoutEmpty = findViewById(R.id.layout_empty);

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext, R.color.library_module_color_red));
        swipeRefreshLayout.setProgressViewEndTarget(true, 300);
    }

    @Override
    protected void initListener() {
        ivBack.setOnClickListener(this);
        ivBackBlack.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRefresh.setOnClickListener(this);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int distance = 0;

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                distance += dy;
                if (distance <= 0) {
                    ivBack.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.GONE);

                    tvTitle.setTextColor(Color.argb((int) 255, 255, 255, 255));
                    tvRight.setTextColor(Color.argb((int) 255, 255, 255, 255));

                    titleLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                } else if (distance > 0 && distance <= bannerHeight) {
                    ivBack.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);

                    float scale = (float) distance / bannerHeight;
                    float alpha = (255 * scale);
                    ivBack.setImageAlpha((int) (255 * (1 - scale)));
                    ivBackBlack.setImageAlpha((int) alpha);

                    tvTitle.setTextColor(Color.argb((int) alpha, 0, 0, 0));
                    tvRight.setTextColor(Color.argb((int) alpha, 0, 0, 0));

                    titleLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    ivBack.setVisibility(View.GONE);
                    ivBackBlack.setVisibility(View.VISIBLE);

                    tvTitle.setTextColor(Color.argb((int) 255, 0, 0, 0));
                    tvRight.setTextColor(Color.argb((int) 255, 0, 0, 0));

                    titleLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                }

            }
        });
    }

    @Override
    protected void initData() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(true);
        }
        presenter.getMyFamilyList();
    }

    @Override
    protected void onResume() {
        super.onResume();

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_HEALTH_FAMILY_LIST, mContext, false);

        if (isRefresh) {
            Logger.i("走了onResume - family");
            initData();

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HEALTH_FAMILY_LIST, isRefresh, mContext);
        }
    }

    @Override
    public void onError(Throwable e) {
        layoutNetError.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        layoutEmpty.setVisibility(View.GONE);
        titleLayout.setBackgroundResource(R.drawable.myhealthback);
        netRequestEnd();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onNext(BaseEntity baseEntity) {
        layoutNetError.setVisibility(View.GONE);

        if (baseEntity instanceof HealthMyFamilyBean) {
            HealthMyFamilyBean bean = (HealthMyFamilyBean) baseEntity;

            list.clear();
            ArrayList<Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>>> netList = bean.data.homeplans;
            list.addAll(netList);
            Logger.i("走了onResume - list" + list.toString());

            ArrayList<Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>>> newList = new ArrayList<>();
            Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>> mMap = new HashMap<>();

            for (int i = 0; i < list.size(); i++) {
                mMap = list.get(i);

                for (Map.Entry<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>> entry : mMap.entrySet()) {
                    ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean> list = entry.getValue();
                    if (list == null || list.size() == 0) {
                        continue;
                    } else {
                        newList.add(mMap);
                    }
                }
            }

            if (newList.size() <= 0) {
                layoutEmpty.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                titleLayout.setBackgroundResource(R.drawable.myhealthback);
                rootLayout.setBackgroundColor(mContext.getResources().getColor(R.color.library_module_color_white));
            } else {
                rootLayout.setBackground(mContext.getResources().getDrawable(R.drawable.myhealthback));
                layoutEmpty.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);

                MyAdapter adapter = new MyAdapter(mContext, newList);
                LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }

        netRequestEnd();
    }

    private void netRequestEnd() {
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            finish();
        } else if (i == R.id.tv_right) {
            SkipUtils.toMyHealth(mContext);
        } else if (i == R.id.tv_refresh) {
            initData();
        } else if (i == R.id.iv_back_black) {
            finish();
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyVH> {
        private Context context;
        private ArrayList<Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>>> list;

        public MyAdapter(Context context, ArrayList<Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>>> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public MyVH onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.item_family, parent, false);
            return new MyVH(view);
        }

        @Override
        public void onBindViewHolder(final MyVH holder, int position) {
            Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>> mMap = new HashMap<>();
            ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean> valueList = new ArrayList<>();
            mMap.clear();
            valueList.clear();

            mMap = (Map<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>>) list.get(position);
            Logger.i("1-22:" + mMap.toString());

            for (Map.Entry<String, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean>> entry : mMap.entrySet()) {
                ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean> list = entry.getValue();
                valueList.addAll(list);
            }

            Logger.i("1-22:" + valueList.size() + "=======" + valueList.toString());


            MyPagerAdapter mPagerAdapter = new MyPagerAdapter(context, valueList);
            holder.viewpager.setAdapter(mPagerAdapter);
            holder.indicator.setViewPager(holder.viewpager);

            if (valueList.size() <= 1) {
                Logger.i("2018年1月26日 11:22:24-");
                holder.indicator.setVisibility(View.GONE);
            } else {
                holder.indicator.setVisibility(View.VISIBLE);
            }

            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                int key = (int) entry.getKey();
                int val = (int) entry.getValue();
                if (position == key) {
                    holder.viewpager.setCurrentItem(val);
                }
            }

            holder.viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    int recyclerPosition = holder.getLayoutPosition();
                    map.put(recyclerPosition, position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }

        @Override
        public int getItemCount() {
            if (null == list || list.size() == 0) {
                return 0;
            }
            return list.size();
        }

        class MyVH extends RecyclerView.ViewHolder {
            private ViewPager viewpager;
            private CircleIndicator indicator;
            View itemView;

            public MyVH(View itemView) {
                super(itemView);
                this.itemView = itemView;
                viewpager = (ViewPager) itemView.findViewById(R.id.viewpager);
                indicator = itemView.findViewById(R.id.indicator);
            }
        }
    }

    class MyPagerAdapter extends PagerAdapter {
        private Context context;
        private ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean> valueList;

        public MyPagerAdapter(Context context, ArrayList<HealthMyFamilyBean.DataBean.HomeBean.FamilyBean> valueList) {
            this.context = context;
            this.valueList = valueList;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = View.inflate(context, R.layout.item_family_item, null);

            @SuppressLint("WrongViewCast")
            CardView cardView = view.findViewById(R.id.root_layout);
            TextView tvTitle = view.findViewById(R.id.title_tv);
            TextView tvState = view.findViewById(R.id.state_tv);
            TextView tvPlan = view.findViewById(R.id.plan_tv);
            TextView tvPeople = view.findViewById(R.id.people_tv);
            TextView tvMoney = view.findViewById(R.id.money_tv);
            TextView tvTime = view.findViewById(R.id.time_tv);
            TextView tvUp = view.findViewById(R.id.tv_up);
            ImageView ivFailed = view.findViewById(R.id.iv_failed);

            final HealthMyFamilyBean.DataBean.HomeBean.FamilyBean bean = valueList.get(position);
            tvTitle.setText(bean.TAG);
            String statusStr = "";
            String bottomStr = "";
            switch (bean.STATUS) {
                case "1":
                    tvState.setBackgroundDrawable(null);
                    bottomStr = "追加保障";
                    ivFailed.setVisibility(View.GONE);
                    break;
                case "2":
                    tvState.setBackgroundDrawable(null);
                    bottomStr = "追加保障";
                    ivFailed.setVisibility(View.GONE);
                    break;
                case "3":
                    tvState.setBackgroundDrawable(null);
                    ivFailed.setVisibility(View.VISIBLE);
                    bottomStr = "重新提交";
                    break;
                case "4":
                    statusStr = "保障中";
                    bottomStr = "提升保障";
                    ivFailed.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            tvState.setText(statusStr);
            tvPlan.setText(bean.project_name);
            String nameStr = bean.user_name;
//            switch (nameStr.length()) {
//                case 1:
//                    nameStr = nameStr;
//                case 2:
//                    nameStr = nameStr.substring(0, 1) + "*";
//                    break;
//                case 3:
//                    nameStr = nameStr.substring(0, 1) + "*" + nameStr.substring(2, 3);
//                    break;
//                default:
//                    nameStr = nameStr.substring(0, 1) + "**" + nameStr.substring(nameStr.length() - 1, nameStr.length());
//                    break;
//            }
            tvPeople.setText("互助对象：" + nameStr);
            tvMoney.setText("互助额度：" + bean.JOIN_AMOUNT + "元");
            tvTime.setText(bean.msg);
            tvUp.setText(bottomStr);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SkipUtils.toHelpEach(mContext, bean.projectId, bean.STATUS, bean.TAG_ID, bean.id_num, bean.user_name, bean.birthday, bean.joinUserId, bean.project_name);
                }
            });

            container.addView(view);
            return view;

        }

        @Override
        public int getCount() {
            if (null == valueList || valueList.size() == 0) {
                return 0;
            }
            return valueList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}