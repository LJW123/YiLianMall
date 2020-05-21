package com.yilianmall.merchant.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.adapter.DefaultAdapter;
import com.yilian.mylibrary.adapter.ViewHolder;
import com.yilian.networkingmodule.entity.ScoreExponent;
import com.yilianmall.merchant.R;
import com.yilianmall.merchant.utils.DateUtils;
import com.yilianmall.merchant.utils.NumberFormat;

import java.util.ArrayList;

/**
 * 最近七天发消费指数列表
 */
public class MerchantScoreExponentActivity extends Activity implements View.OnClickListener {
    private TextView tvTitle;
    private ImageButton ibClose;
    private ListView lsScoreExponent;
    private ArrayList<ScoreExponent> scoreExponentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.merchant_activity_merchant_score_exponent);
        initView();
        initData();
        setData(scoreExponentList);
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
    private void setData(final ArrayList<ScoreExponent> scoreExponentList) {
        if (scoreExponentList != null) {
            lsScoreExponent.setAdapter(new DefaultAdapter<ScoreExponent>(MerchantScoreExponentActivity.this, scoreExponentList, R.layout.merchant_layout_text_value) {
                @Override
                public void convert(ViewHolder helper, ScoreExponent item, int position) {
                    ((TextView) helper.getView(R.id.tv_key)).setText(DateUtils.timeStampToStr3_1(NumberFormat.convertToLong(item.createdAt, 0L)));
                    TextView tvScoreExponent = (TextView) helper.getView(R.id.tv_value);
                    tvScoreExponent.setVisibility(View.VISIBLE);
                    tvScoreExponent.setTextColor(ContextCompat.getColor(mContext, R.color.library_module_color_red));
                    tvScoreExponent.setText(scoreExponentList.get(position).integralNumbers);
                    tvScoreExponent.setCompoundDrawablesWithIntrinsicBounds(null,null,ContextCompat.getDrawable(mContext, R.mipmap.merchant_icon_ppm),null);
                    Logger.i("tvScoreExponentGetText    "+tvScoreExponent.getText());
                }
            });
        }
    }

    private void initData() {
        scoreExponentList = (ArrayList<ScoreExponent>) getIntent().getSerializableExtra("score_exponent_list");
    }

    private void initView() {
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ibClose = (ImageButton) findViewById(R.id.ib_close);
        lsScoreExponent = (ListView) findViewById(R.id.ls_score_exponent);
        ibClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.ib_close) {
            finish();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,R.anim.merchant_score_exponent_out);

    }
}
