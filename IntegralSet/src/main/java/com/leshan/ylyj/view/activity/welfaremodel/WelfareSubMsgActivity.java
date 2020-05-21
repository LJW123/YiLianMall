package com.leshan.ylyj.view.activity.welfaremodel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.WelfarePresenter;
import com.leshan.ylyj.testfor.R;

import rxfamily.entity.BaseEntity;

/**
 * 提交留言界面
 *
 * @author zhaiyaohua on 2018/1/18 0018.
 */

public class WelfareSubMsgActivity extends BaseActivity implements View.OnClickListener {
    private ImageView mLeftBack;
    private EditText mEtMessage;
    private TextView mTvMessageNum, tvSubMsg;
    private WelfarePresenter mPrensent;
    private String proId;
    private String recordId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_msg);
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }
    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        mLeftBack = (ImageView) findViewById(R.id.left_back);
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mTvMessageNum = (TextView) findViewById(R.id.tv_message_num);
        tvSubMsg = (TextView) findViewById(R.id.tv_submit_msg);
        mPrensent = new WelfarePresenter(mContext, this);
    }
    /**
     * 提交公益爱心留言
     *
     * @param proId
     * @param content
     */
    private void submitWelfareLoveMsg(String proId, String recordId, String content) {
        startMyDialog(false);
        mPrensent.submitWefareLoveMsg(proId, recordId, content);
    }
    @Override
    protected void initListener() {
        tvSubMsg.setOnClickListener(this);
        mLeftBack.setOnClickListener(this);
        mEtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {



            }

            @Override
            public void afterTextChanged(Editable editable) {
                String content = editable.toString().trim();
                int lenght = content.length();
                mTvMessageNum.setText(lenght + "/60");
                checkSubmit();
            }
        });

    }

    /**
     * 检测是否有emoji表情
     *
     * @param source
     * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) { //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji
     *
     * @param codePoint 比较的单个字符
     * @return
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) ||
                (codePoint == 0xD) || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000)
                && (codePoint <= 0x10FFFF));
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        proId = intent.getStringExtra("proId");
        recordId = intent.getStringExtra("recordId");
    }

    /**
     * 检查是否能够提交
     *
     * @return
     */
    //默认不包含表情符
    private boolean isContainsEmoji=false;
    private boolean checkSubmit() {
        String msg = mEtMessage.getText().toString().trim();
        if (containsEmoji(msg)){
            //假如包含表情符
            isContainsEmoji=true;
        }else {
            isContainsEmoji=false;
        }
        if (TextUtils.isEmpty(msg)) {
            tvSubMsg.setBackgroundResource(R.drawable.bg_not_submit);
            tvSubMsg.setTextColor(getResources().getColor(R.color.color_cccccc));
            return false;
        } else {
            tvSubMsg.setBackgroundResource(R.drawable.shap_orange_gradient);
            tvSubMsg.setTextColor(getResources().getColor(R.color.white));
            return true;
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.tv_submit_msg) {
            //执行提交留言的接口
            if (isContainsEmoji){
                showToast("提交内容不支持表情");
                return;
            }
            if (checkSubmit()) {
                String msg = mEtMessage.getText().toString().trim();
                submitWelfareLoveMsg(proId, recordId, msg);
            }
        } else if (id == R.id.left_back) {
            finish();
        }
    }

    @Override
    public void onCompleted() {
        stopMyDialog();

    }

    @Override
    public void onError(Throwable e) {
        stopMyDialog();
        showToast(e.getMessage());
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        setResult(WelfareLoveMsgActivity.TO_SUB_MSG_RESESULT_CODE);
        finish();
    }

}
