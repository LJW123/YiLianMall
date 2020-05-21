package com.yilian.mall.ui.mvp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.ProfessionMultiAdapter;
import com.yilian.mall.ui.mvp.presenter.IProfessionPresenter;
import com.yilian.mall.ui.mvp.presenter.ProfessionPresenterImpl;
import com.yilian.networkingmodule.entity.ProfessionEntity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

import static com.yilian.mall.ui.mvp.view.BasicInfomationViewImplActivity.PROFESSION_REQUEST_CODE;

/**
 * @author
 *         职业选择
 */
public class ProfessionViewImplActivity extends BaseAppCompatActivity implements View.OnClickListener, IProfessionView {

    private TextView tvLeft;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private RecyclerView recyclerView;
    private IProfessionPresenter professionPresenter;
    private ProfessionMultiAdapter professionMultiAdapter = new ProfessionMultiAdapter(new ArrayList());
    private String proName;
    private int proId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession_view_impl);
        professionPresenter = new ProfessionPresenterImpl(this);
        proName = getIntent().getStringExtra("pro_name");
        proId = getIntent().getIntExtra("pro_id", 0);
        initView();
        initData();
        initListener();
    }

    private void initListener() {
        professionMultiAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ProfessionEntity.DataBean item = (ProfessionEntity.DataBean) adapter.getItem(position);
                if (item.getItemType() != ProfessionEntity.EDIT_ID) {
                    proName = item.name;
                    proId = item.id;
                    Intent data = new Intent();
                    data.putExtra("pro_name", proName);
                    data.putExtra("pro_id", proId);
                    setResult(0, data);
                    finish();
                }


            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    private void initData() {
        Subscription subscription = professionPresenter.getProfessionData(mContext);
        addSubscription(subscription);
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("职业");
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(professionMultiAdapter);

        tvLeft.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
        v3Back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_left:

                break;
            case R.id.tv_right:

                break;
            case R.id.tv_right2:

                break;
            case R.id.v3Back:
                setSelfProfessionName();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        setSelfProfessionName();
        super.onBackPressed();
    }

    /**
     * 设置自定义行业
     */
    private void setSelfProfessionName() {
        EditText etProfession = professionMultiAdapter.etProfession;
        if (etProfession == null) {
            return;
        }
        String selfProfessionName = etProfession.getText().toString().trim();
        Logger.i("自定义行业1：selfProfessionName:" + selfProfessionName);
        if (!TextUtils.isEmpty(selfProfessionName)) {
            Logger.i("走了自定义行业1");
            Intent data = new Intent();
            data.putExtra("pro_name", selfProfessionName);
            data.putExtra("pro_id", ProfessionEntity.EDIT_ID);
            setResult(PROFESSION_REQUEST_CODE, data);
        }
    }

    @Override
    public void setData(ProfessionEntity professionEntity) {
        List<ProfessionEntity.DataBean> data = professionEntity.data;
        professionMultiAdapter.setNewData(data);
    }
}
