package com.yilian.mall.ui.mvp.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Objects;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.ui.mvp.presenter.EditUserNamePresenterImpl;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.EmojiUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RxUtil;

import io.reactivex.functions.Consumer;
import rx.Subscription;

/**
 * @author
 *         编辑昵称、个性签名
 */
public class EditUserNameViewImplActivity extends BaseAppCompatActivity implements IEditUserNameView, View.OnClickListener {

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
    private EditText etUserName;
    private EditUserNamePresenterImpl mPresenter;
    /**
     * 编辑类型 0 昵称  1 个性签名
     */
    private int editType;
    private EditText etUserStateOfMind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_name);
        if (mPresenter == null) {
            mPresenter = new EditUserNamePresenterImpl(this);
        }
        editType = getIntent().getIntExtra("editType", 0);
        initView();
        initListener();
    }

    private void initListener() {
        RxUtil.clicks(tvRight, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (checkEmoji()) {
                    showToast("不能含有特殊字符");
                    return;
                }
                Subscription subscription = mPresenter.saveData(mContext, editType);
                addSubscription(subscription);
            }
        });
    }

    private boolean checkEmoji() {
        String inputString;
        if (editType == 0) {
            inputString = getUserName();
        } else {
            inputString = getStateOfMind();
        }
        if (EmojiUtil.containsEmoji(inputString)) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        showFinishDialog();
    }

    /**
     * 未保存数据时，关闭页面的提示
     */
    private void showFinishDialog() {
        String message;
        String positiveText;
        String negativeText;
        if (editType == 0) {
            if (Objects.equal(etUserName.getText().toString().trim(), PreferenceUtils.readStrConfig(Constants.USER_NAME, mContext))) {
                finish();
                return;
            }
            message = "您修改的昵称还没有保存，是否保存？";
            positiveText = "保存";
            negativeText = "放弃";
        } else {
            if (Objects.equal(etUserStateOfMind.getText().toString().trim(), PreferenceUtils.readStrConfig(Constants.STATE_OF_MIND, mContext))) {
                finish();
                return;
            }
            message = "您修改的个性签名还没有发布，是否离开？";
            negativeText = "退出";
            positiveText = "继续编辑";
        }

        showSystemV7Dialog("温馨提示", message, positiveText, negativeText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                dialogInterface.dismiss();
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        if (editType == 0) {
                            if (checkEmoji()) {
                                showToast("不能含有特殊字符");
                                return;
                            }
                            Subscription subscription = mPresenter.saveData(mContext, editType);
                            addSubscription(subscription);
                        } else {
//                            用户继续编辑此处不做处理
                        }
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        finish();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestory();
        mPresenter = null;
    }

    private void initView() {
        tvLeft = (TextView) findViewById(R.id.tv_left);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("保存");
        tvRight.setTextColor(Color.BLACK);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        etUserName = (EditText) findViewById(R.id.et_user_name);
        etUserStateOfMind = (EditText) findViewById(R.id.et_user_state_of_mind);
        if (editType == 0) {
            v3Title.setText("修改昵称");
            etUserName.setVisibility(View.VISIBLE);
            String userName = PreferenceUtils.readStrConfig(Constants.USER_NAME, mContext);
            if (!TextUtils.isEmpty(userName)) {
                etUserName.setText(userName);
                etUserName.setSelection(etUserName.getText().toString().trim().length());
            }
            etUserStateOfMind.setVisibility(View.GONE);
        } else {
            v3Title.setText("编辑个性签名");
            etUserName.setVisibility(View.GONE);
            etUserStateOfMind.setVisibility(View.VISIBLE);
            String stateOfMind = PreferenceUtils.readStrConfig(Constants.STATE_OF_MIND, mContext);
            if (!TextUtils.isEmpty(stateOfMind)) {
                etUserStateOfMind.setText(stateOfMind);
                etUserStateOfMind.setSelection(etUserStateOfMind.getText().toString().trim().length());
            }
        }
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
                showFinishDialog();
                break;
            default:
                break;
        }
    }

    @Override
    public String getUserName() {
        return etUserName.getText().toString().trim();
    }

    @Override
    public String getStateOfMind() {
        return etUserStateOfMind.getText().toString().trim();
    }
}
