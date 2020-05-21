package com.leshan.ylyj.view.activity.creditmodel;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.adapter.EducationRegionAdapter;
import com.leshan.ylyj.bean.RegionAcademyBean;
import com.leshan.ylyj.utils.ClearEditText;
import com.leshan.ylyj.utils.LetterIndexView;
import com.leshan.ylyj.utils.PinnedSectionListView;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.EducationRegionEntity;
import rxfamily.entity.SchoolNameEntity;

/**
 * 学历地区
 */
public class EducationRegionActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 搜索栏
     */
    ClearEditText edit_search;
    /**
     * 列表
     */
    PinnedSectionListView listView;
    /**
     * 右边字母列表
     */
    LetterIndexView letterIndexView;
    /**
     * 中间显示右边按的字母
     */
    TextView txt_center;

    /**
     * 所有名字集合
     */
    private ArrayList<RegionAcademyBean> list_all = new ArrayList<RegionAcademyBean>();
    /**
     * 显示名字集合
     */
    private ArrayList<RegionAcademyBean> list_show;
    /**
     * 列表适配器
     */
    private EducationRegionAdapter adapter;
    /**
     * 保存名字首字母
     */
    public HashMap<String, Integer> map_IsHead = new HashMap<String, Integer>();
    ;
    /**
     * item标识为0
     */
    public static final int ITEM = 0;
    /**
     * item标题标识为1
     */
    public static final int TITLE = 1;

    private LinearLayout returnImg;
    CreditPresenter creditPresenter;

    //    String[] str = getResources().getStringArray(R.array.region_academy_all);
    String[] str=null;
    String[] str2 = null;
    private TextView title_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_education_region);
        InitializationView();
        initView();
//        initData();
        getHomePage();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    /**
     * @net 执行网络请求
     */
    private void getHomePage() {
        startMyDialog(false);
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        //@net  进行网络请求
        creditPresenter.getEducationRegion();
    }

    //@net  网络完成的回调
    @Override
    public void onCompleted() {
        Logger.i("获取数据完成");
        stopMyDialog();
    }

    //@net  网络请求发生异常的回调（只要发生异常都会回调到这）
    @Override
    public void onError(Throwable e) {
        Logger.i("获取数据异常：" + e.getMessage());
        ToastUtil.showMessage(mContext, e.getMessage());
        stopMyDialog();
    }

    //@net  网络完成的回调，执行
    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        Logger.i("获取数据结果");
        //@net 数据类型判断执行对应请求的逻辑
        if (baseEntity instanceof EducationRegionEntity) {
            List<EducationRegionEntity.DataBean> mDatas = ((EducationRegionEntity) baseEntity).datas;
//            initData();
            AddData(mDatas);
        }
    }

    private void AddData(List<EducationRegionEntity.DataBean> mDatas) {

        List<EducationRegionEntity.AreasBean> mAdrees = new ArrayList<>();
        for (EducationRegionEntity.DataBean item : mDatas) {
            for (EducationRegionEntity.AreasBean bean : item.areas) {
                mAdrees.add(bean);
            }
        }
         str=new String[mAdrees.size()];
        str2=new String[mAdrees.size()];
        for (int i=0;i<mAdrees.size();i++){
            str[i]=mAdrees.get(i).getName();
            str2[i] = mAdrees.get(i).getCode();
        }
        initData();
//        Toast.makeText(this, mAdrees.size() + "---", Toast.LENGTH_SHORT).show();
    }

    private void InitializationView() {
        returnImg = (LinearLayout) findViewById(R.id.returnImg);
        edit_search = (ClearEditText) findViewById(R.id.edit_search);
        listView = (PinnedSectionListView) findViewById(R.id.phone_listview);
        letterIndexView = (LetterIndexView) findViewById(R.id.phone_LetterIndexView);
        txt_center = (TextView) findViewById(R.id.phone_txt_center);
        title_tv=findViewById(R.id.title_tv);
//        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void initView() {

        // 输入监听
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i,
                                          int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {

            }


            @Override
            public void afterTextChanged(Editable editable) {
                list_show.clear();
                map_IsHead.clear();
                //把输入的字符改成大写
                String search = editable.toString().trim().toUpperCase();

                if (TextUtils.isEmpty(search)) {
                    for (int i = 0; i < list_all.size(); i++) {
                        RegionAcademyBean bean = list_all.get(i);
                        //中文字符匹配首字母和英文字符匹配首字母
                        if (!map_IsHead.containsKey(bean.getHeadChar())) {// 如果不包含就添加一个标题
                            RegionAcademyBean bean1 = new RegionAcademyBean();
                            // 设置名字
                            bean1.setName(bean.getName());
                            // 设置标题type
                            bean1.setType(EducationRegionActivity.TITLE);
                            list_show.add(bean1);
                            // map的值为标题的下标
                            map_IsHead.put(bean1.getHeadChar(),
                                    list_show.size() - 1);
                        }
                        // 设置Item type
                        bean.setType(EducationRegionActivity.ITEM);
                        list_show.add(bean);
                    }
                } else {
                    for (int i = 0; i < list_all.size(); i++) {
                        RegionAcademyBean bean = list_all.get(i);
                        //中文字符匹配首字母和英文字符匹配首字母
                        if (bean.getName().indexOf(search) != -1 || bean.getName_en().indexOf(search) != -1) {
                            if (!map_IsHead.containsKey(bean.getHeadChar())) {// 如果不包含就添加一个标题
                                RegionAcademyBean bean1 = new RegionAcademyBean();
                                // 设置名字
                                bean1.setName(bean.getName());
                                // 设置标题type
                                bean1.setType(EducationRegionActivity.TITLE);
                                list_show.add(bean1);
                                // map的值为标题的下标
                                map_IsHead.put(bean1.getHeadChar(),
                                        list_show.size() - 1);
                            }
                            // 设置Item type
                            bean.setType(EducationRegionActivity.ITEM);
                            list_show.add(bean);
                        }
                    }
                }

                adapter.notifyDataSetChanged();

            }
        });

        // 右边字母竖排的初始化以及监听
        letterIndexView.init(new LetterIndexView.OnTouchLetterIndex() {
            //实现移动接口
            @Override
            public void touchLetterWitch(String letter) {
                // 中间显示的首字母
                txt_center.setVisibility(View.VISIBLE);
                txt_center.setText(letter);
                // 首字母是否被包含
                if (adapter.map_IsHead.containsKey(letter)) {
                    // 设置首字母的位置
                    listView.setSelection(adapter.map_IsHead.get(letter));
                }
            }

            //实现抬起接口
            @Override
            public void touchFinish() {
                txt_center.setVisibility(View.GONE);
            }
        });

        /** listview点击事件 */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (list_show.get(i).getType() == EducationRegionActivity.ITEM) {// 标题点击不给操作
//                    Toast.makeText(EducationRegionActivity.this,list_show.get(i).getName(), Toast.LENGTH_LONG).show();
                    Intent intent = getIntent();
                    Bundle bundle = intent.getExtras();
                    bundle.putString("province", list_show.get(i).getName());//添加要返回给页面1的数据
                    bundle.putString("provinceid", list_show.get(i).getCity_id());//添加要返回给页面1的数据
                    intent.putExtras(bundle);
                    setResult(Activity.RESULT_OK, intent);//返回页面1
                    finish();
                }
            }
        });

        // 设置标题部分有阴影
        // listView.setShadowVisible(true);
    }

    @Override
    protected void initListener() {

    }

    protected void initData() {
//        list_all = new ArrayList<RegionAcademyBean>();
        list_show = new ArrayList<RegionAcademyBean>();
//        map_IsHead = new HashMap<String, Integer>();
        adapter = new EducationRegionAdapter(EducationRegionActivity.this, list_show, map_IsHead);
        listView.setAdapter(adapter);
        // 开启异步加载数据
        new Thread(runnable).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            adapter.notifyDataSetChanged();
        }
    };

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
//            String[] str = getResources().getStringArray(R.array.region_academy_all);

            for (int i = 0; i < str.length; i++) {
                RegionAcademyBean cityBean = new RegionAcademyBean();
                cityBean.setName(str[i]);
                cityBean.setCity_id(str2[i]);
                list_all.add(cityBean);
            }
            //按拼音排序
            MemberSortUtil sortUtil = new MemberSortUtil();
            Collections.sort(list_all, sortUtil);

            // 初始化数据，顺便放入把标题放入map集合
            for (int i = 0; i < list_all.size(); i++) {
                RegionAcademyBean cityBean = list_all.get(i);
                if (!map_IsHead.containsKey(cityBean.getHeadChar())) {// 如果不包含就添加一个标题
                    RegionAcademyBean cityBean1 = new RegionAcademyBean();
                    // 设置名字
                    cityBean1.setName(cityBean.getName());
                    // 设置标题type
                    cityBean1.setType(EducationRegionActivity.TITLE);
                    //设置id
                    cityBean1.setCity_id(cityBean.getCity_id());
                    list_show.add(cityBean1);

                    // map的值为标题的下标
                    map_IsHead.put(cityBean1.getHeadChar(), list_show.size() - 1);
                }
                list_show.add(cityBean);
            }

            handler.sendMessage(handler.obtainMessage());
        }
    };

    public class MemberSortUtil implements Comparator<RegionAcademyBean> {
        /**
         * 按拼音排序
         */
        @Override
        public int compare(RegionAcademyBean lhs, RegionAcademyBean rhs) {
            Comparator<Object> cmp = Collator
                    .getInstance(java.util.Locale.CHINA);
            return cmp.compare(lhs.getName_en(), rhs.getName_en());
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();

        }
    }
}
