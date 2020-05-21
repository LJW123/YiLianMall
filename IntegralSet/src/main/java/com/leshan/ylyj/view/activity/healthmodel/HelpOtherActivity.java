package com.leshan.ylyj.view.activity.healthmodel;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.jakewharton.rxbinding.view.RxView;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.CustWebView;
import com.leshan.ylyj.presenter.HealthPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxRegTool;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.DateUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.umeng.IconModel;
import com.yilian.mylibrary.umeng.ShareUtile;
import com.yilian.mylibrary.umeng.UmengDialog;
import com.yilian.mylibrary.umeng.UmengGloble;
import com.yilian.mylibrary.widget.ClearEditText;
import com.yilian.mylibrary.widget.MyRadioGroup;
import com.zcw.togglebutton.ToggleButton;

import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import rx.functions.Action1;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.HelpOtherDetailBean;
import rxfamily.entity.HelpOtherFindBAIBean;


/**
 * 互助计划
 *
 * @author Ray_L_Pain
 */
public class HelpOtherActivity extends BaseActivity {

    private LinearLayout not_yourself_ll;
    private ToggleButton toggleButton;
    private ImageView back_iv;
    private ImageView share_img;

    private ScrollView scrollView;
    private RelativeLayout titleLayout;
    private RadioGroup radioGroup;
    private RadioButton kidRadioBtn, loverRadioBtn, parentsRadioBtn;
    private TextView tvCenter;
    private CustWebView webViewCenter, webViewThree;
    private ImageView ivBackBlack, ivShareBlack, ivTop;
    private TextView tvJoinCount, tvName, tvDesc, tvBirthday, tvYear, tvDate;
    private ClearEditText etFromName, etToName, etIdCard;
    private LinearLayout moreLayout;
    private LinearLayout bottomLayout;
    private Button joinBtn;
    private LinearLayout layoutNetError;
    private TextView tvRefresh;

    private LinearLayout oneTitleLayout, oneContentLayout, twoTitleLayout, twoContentLayout, threeTitleLayout, threeContentLayout, fourTitleLayout, fourContentLayout;
    private TextView tvTitleOne, tvContentOne, tvTitleTwo, tvContentTwo, tvTitleThree, tvContentThree, tvTitleFour, tvContentFour;
    private ImageView ivRightOne, ivRightTwo, ivRightThree, ivRightFour;
    private ImageView ivReplyOne, ivReplyTwo, ivReplyThree, ivReplyFour;
    private View viewOneLine, viewTwoLine, viewThreeLine, viewFourLine;

    int distance = 0;
    int bannerHeight;

    private HealthPresenter presenter, tPresenter, nPresenter;
    private String fruitCount, tagId, toNameStr, idCardStr, birthdayStr;

    /**
     * intent传递的数据
     * intentType主要区别是否是被驳回的计划
     * intentTag：1：本人；2：配偶；3：父母；4：子女
     */
    private String projectId, intentType, intentName, intentIdNum, intentBirthday, intentTag, intentId, intentTitle;
    private boolean isShowDialog = true;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_other);
        presenter = new HealthPresenter(mContext, this, 0);
        tPresenter = new HealthPresenter(mContext, this, 1);
        nPresenter = new HealthPresenter(mContext, this, 2);

        initView();
        initListener();
        initData();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            RelativeLayout.LayoutParams titleParams = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
            titleParams.height += StatusBarUtils.getStatusBarHeight(mContext);
            titleLayout.setPadding(0, StatusBarUtils.getStatusBarHeight(mContext), 0, 0);

            StatusBarUtil.setTranslucentForImageViewInFragment(HelpOtherActivity.this, 60, null);
        }
    }

    @Override
    protected void initView() {
        projectId = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_ID);
        Logger.i("2018年1月25日 16:20:46-" + projectId);
        intentType = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_TYPE);
        intentTag = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_TAG);
        intentIdNum = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_ID_NUM);
        intentName = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_NAME);
        intentBirthday = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_BIRTHDAY);
        intentId = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_JOIN_ID);
        intentTitle = getIntent().getStringExtra(Constants.HEALTH_HELP_OTHER_TITLE);

        webViewCenter = findViewById(R.id.web_view_center);
        webViewThree = findViewById(R.id.web_view_three);
        scrollView = findViewById(R.id.scroll_view);
        titleLayout = findViewById(R.id.layout_title);
        ivBackBlack = findViewById(R.id.iv_back_black);
        ivShareBlack = findViewById(R.id.iv_share_black);
        ivTop = findViewById(R.id.iv_top);
        tvJoinCount = findViewById(R.id.tv_join_count);
        tvName = findViewById(R.id.tv_name);
        tvDesc = findViewById(R.id.tv_desc);
        moreLayout = findViewById(R.id.layout_more);
        etFromName = findViewById(R.id.et_from_help);
        etToName = findViewById(R.id.et_to_help);
        etIdCard = findViewById(R.id.et_id_card);
        tvCenter = findViewById(R.id.tv_center);
        tvYear = findViewById(R.id.tv_year);
        tvDate = findViewById(R.id.tv_date);

        bottomLayout = findViewById(R.id.layout_bottom);
        joinBtn = findViewById(R.id.btn_join);
        layoutNetError = findViewById(R.id.layout_net_error);
        tvRefresh = findViewById(R.id.tv_refresh);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) ivTop.getLayoutParams();
        params.height = (int) ((200 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));
        bannerHeight = params.height;

        FrameLayout.LayoutParams paramsTv = (FrameLayout.LayoutParams) tvJoinCount.getLayoutParams();
        paramsTv.height = (int) ((200 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (375 * 0.1));


        not_yourself_ll = findViewById(R.id.not_yourself_ll);
        toggleButton = findViewById(R.id.toggle_btn);
        radioGroup = findViewById(R.id.radio_group);
        kidRadioBtn = findViewById(R.id.rb_children);
        loverRadioBtn = findViewById(R.id.rb_lover);
        parentsRadioBtn = findViewById(R.id.rb_parent);
        back_iv = findViewById(R.id.back_iv);
        tvBirthday = findViewById(R.id.birthday_tv);
        share_img = findViewById(R.id.share_img);

        oneTitleLayout = findViewById(R.id.layout_one_title);
        tvTitleOne = findViewById(R.id.tv_one_title);
        ivRightOne = findViewById(R.id.iv_right_one);
        viewOneLine = findViewById(R.id.view_one);
        ivReplyOne = findViewById(R.id.iv_reply_one);
        oneContentLayout = findViewById(R.id.layout_one_content);
        tvContentOne = findViewById(R.id.tv_one_content);

        twoTitleLayout = findViewById(R.id.layout_two_title);
        tvTitleTwo = findViewById(R.id.tv_two_title);
        ivRightTwo = findViewById(R.id.iv_right_two);
        viewTwoLine = findViewById(R.id.view_two);
        ivReplyTwo = findViewById(R.id.iv_reply_two);
        twoContentLayout = findViewById(R.id.layout_two_content);
        tvContentTwo = findViewById(R.id.tv_two_content);

        threeTitleLayout = findViewById(R.id.layout_three_title);
        tvTitleThree = findViewById(R.id.tv_three_title);
        ivRightThree = findViewById(R.id.iv_right_three);
        viewThreeLine = findViewById(R.id.view_three);
        ivReplyThree = findViewById(R.id.iv_reply_three);
        threeContentLayout = findViewById(R.id.layout_three_content);
        tvContentThree = findViewById(R.id.tv_three_content);

        fourTitleLayout = findViewById(R.id.layout_four_title);
        tvTitleFour = findViewById(R.id.tv_four_title);
        ivRightFour = findViewById(R.id.iv_right_four);
        viewFourLine = findViewById(R.id.view_four);
        ivReplyFour = findViewById(R.id.iv_reply_four);
        fourContentLayout = findViewById(R.id.layout_four_content);
        tvContentFour = findViewById(R.id.tv_four_content);

        oneTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomViewClick(ivRightOne, viewOneLine, ivReplyOne, oneContentLayout);
            }
        });

        twoTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomViewClick(ivRightTwo, viewTwoLine, ivReplyTwo, twoContentLayout);
            }
        });

        threeTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomViewClick(ivRightThree, viewThreeLine, ivReplyThree, threeContentLayout);
            }
        });

        fourTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomViewClick(ivRightFour, viewFourLine, ivReplyFour, fourContentLayout);
            }
        });

        //finish
        RxView.clicks(back_iv)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(ivBackBlack)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        finish();
                    }
                });

        RxView.clicks(share_img)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        share();
                    }
                });

        RxView.clicks(ivShareBlack)
                .throttleFirst(2, TimeUnit.SECONDS)   //两秒钟之内只取一个点击事件，防抖操作
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        share();
                    }
                });

        RxView.clicks(joinBtn)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (isShowDialog) {
                            showJoinDialog();
                        } else {
                            /**
                             * 走了这里说明是被驳回的
                             * 健康果不能更改 故传 0
                             */
                            if (TextUtils.isEmpty(tagId) || !tagId.equals(intentTag)) {
                                showToast("被保人不能更改");
                                return;
                            }

                            if ("1".equals(tagId)) {
                                toNameStr = "";
                                idCardStr = "";
                                birthdayStr = "";
                            } else {
                                toNameStr = etToName.getText().toString();
                                idCardStr = etIdCard.getText().toString();
                                birthdayStr = tvBirthday.getText().toString();
                                if (TextUtils.isEmpty(toNameStr)) {
                                    showToast("请填写互助人姓名");
                                    return;
                                }
                                if (TextUtils.isEmpty(idCardStr)) {
                                    showToast("请填写互助人身份证号");
                                    return;
                                }
                                if (TextUtils.isEmpty(birthdayStr)) {
                                    showToast("请填写正确的互助人身份证号");
                                    return;
                                }
                                if (toNameStr.equals(intentName) && idCardStr.equals(intentIdNum) && birthdayStr.equals(intentBirthday)) {
                                    showToast("请修改填写的互助人信息");
                                    return;
                                }
                            }

                            Logger.i("2018年1月21日 16:04:37-fruitCount-" + fruitCount);
                            Logger.i("2018年1月21日 16:04:37-tagId-" + tagId);
                            Logger.i("2018年1月21日 16:04:37-toNameStr-" + toNameStr);
                            Logger.i("2018年1月21日 16:04:37-idCardStr-" + idCardStr);
                            Logger.i("2018年1月21日 16:04:37-birthdayStr-" + birthdayStr);

                            startMyDialog();
                            Logger.i("二〇一八年一月二十七日 23:07:28-走了直接");
                            tPresenter.getHelpOtherJoin(projectId, null, tagId, toNameStr, idCardStr, birthdayStr, intentId);
                        }
                    }
                });

        RxView.clicks(tvRefresh)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        initData();
                    }
                });

        initIntentView();
    }

    /**
     * 这里处理反显的view
     */
    public void initIntentView() {
        Logger.i("2018年1月21日 16:04:37-intentTag-" + intentTag);
        Logger.i("2018年1月21日 16:04:37-intentName-" + intentName);
        Logger.i("2018年1月21日 16:04:37-intentIdNum-" + intentIdNum);
        Logger.i("2018年1月21日 16:04:37-intentBirthday-" + intentBirthday);
        Logger.i("2018年1月21日 16:04:37-intentType-" + intentType);

        if (!TextUtils.isEmpty(intentTag)) {
            switch (intentTag) {
                case "1":
                    toggleButton.setToggleOn();
                    tagId = "1";
                    not_yourself_ll.setVisibility(View.GONE);
                    break;
                case "2":
                    toggleButton.setToggleOff();
                    tagId = "2";
                    loverRadioBtn.setChecked(true);
                    not_yourself_ll.setVisibility(View.VISIBLE);
                    break;
                case "3":
                    toggleButton.setToggleOff();
                    tagId = "3";
                    parentsRadioBtn.setChecked(true);
                    not_yourself_ll.setVisibility(View.VISIBLE);
                    break;
                case "4":
                    toggleButton.setToggleOff();
                    tagId = "4";
                    kidRadioBtn.setChecked(true);
                    not_yourself_ll.setVisibility(View.VISIBLE);
                    break;
                default:
                    break;
            }
            toggleButton.setClickable(false);
            loverRadioBtn.setEnabled(false);
            parentsRadioBtn.setEnabled(false);
            kidRadioBtn.setEnabled(false);
        }

        if (!TextUtils.isEmpty(intentName)) {
            etToName.setText(intentName);
            if (!TextUtils.isEmpty(intentName)) {
                etToName.setSelection(intentName.length());
            }
        }

        if (!TextUtils.isEmpty(intentIdNum)) {
            etIdCard.setText(intentIdNum);
        }

        if (!TextUtils.isEmpty(intentBirthday)) {
            tvBirthday.setText(intentBirthday);
        }

        if (!TextUtils.isEmpty(intentType)) {
            if ("3".equals(intentType)) {
                isShowDialog = false;
            } else if ("4".equals(intentType)) {
                joinBtn.setText("提升保障");
            } else {
                joinBtn.setText("追加保障");
            }
        } else {
            joinBtn.setText("立即参与");
        }
    }

    private void bottomViewClick(ImageView iv, View line, ImageView ivReply, LinearLayout layout) {
        if (View.VISIBLE == layout.getVisibility()) {
            //隐藏
            rotationExpandIcon(iv, 180, 0);
            line.setVisibility(View.VISIBLE);
            layout.setVisibility(View.GONE);
            ivReply.setVisibility(View.GONE);
        } else {
            //显示
            rotationExpandIcon(iv, 0, 180);
            line.setVisibility(View.GONE);
            layout.setVisibility(View.VISIBLE);
            ivReply.setVisibility(View.VISIBLE);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void rotationExpandIcon(final ImageView iv, float from, float to) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);//属性动画
            valueAnimator.setDuration(200);
            valueAnimator.setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    iv.setRotation((Float) valueAnimator.getAnimatedValue());
                }
            });
            valueAnimator.start();
        }
    }

    private Dialog dialog;
    private View view;
    private MyRadioGroup dialogRadioGroup;
    private RadioButton radioButton1;
    private RadioButton radioButton2;
    private RadioButton radioButton5;
    private RadioButton radioButton10;
    private RadioButton radioButton20;
    private RadioButton radioButton50;
    private RadioButton radioButton100;
    private RadioButton radioButton200;
    private RadioButton radioButton500;
    private TextView tvWebHead, tvWebKids, tvWebLover, tvWebParents, tvWebSelfOne, tvWebSelfTwo;

    private void showJoinDialog() {
        fruitCount = "1";
        dialog = new Dialog(this, R.style.ActionSheetDialogStyle);
        view = LayoutInflater.from(this).inflate(R.layout.dialog_join_help_other, null);

        dialogRadioGroup = view.findViewById(R.id.radio_group);
        radioButton1 = view.findViewById(R.id.radioBtn1);
        radioButton2 = view.findViewById(R.id.radioBtn2);
        radioButton5 = view.findViewById(R.id.radioBtn5);
        radioButton10 = view.findViewById(R.id.radioBtn10);
        radioButton20 = view.findViewById(R.id.radioBtn20);
        radioButton50 = view.findViewById(R.id.radioBtn50);
        radioButton100 = view.findViewById(R.id.radioBtn100);
        radioButton200 = view.findViewById(R.id.radioBtn200);
        radioButton500 = view.findViewById(R.id.radioBtn500);
        radioButton1.setChecked(true);

        tvWebHead = view.findViewById(R.id.tv_web_head);
        tvWebKids = view.findViewById(R.id.tv_web_kids);
        tvWebLover = view.findViewById(R.id.tv_web_lover);
        tvWebParents = view.findViewById(R.id.tv_web_parents);
        tvWebSelfOne = view.findViewById(R.id.tv_web_self_one);
        tvWebSelfTwo = view.findViewById(R.id.tv_web_self_two);

        switch (intentTitle) {
            case "少儿健康互助计划":
                tvWebHead.setTextSize(13);
                tvWebKids.setVisibility(View.VISIBLE);
                tvWebLover.setVisibility(View.GONE);
                tvWebParents.setVisibility(View.GONE);
                tvWebSelfOne.setVisibility(View.GONE);
                tvWebSelfTwo.setVisibility(View.GONE);
                break;
            case "中青年抗癌互助计划":
                tvWebHead.setTextSize(13);
                tvWebKids.setVisibility(View.GONE);
                tvWebLover.setVisibility(View.VISIBLE);
                tvWebParents.setVisibility(View.GONE);
                tvWebSelfOne.setVisibility(View.GONE);
                tvWebSelfTwo.setVisibility(View.GONE);
                break;
            case "中老年抗癌互助计划":
                tvWebHead.setTextSize(13);
                tvWebKids.setVisibility(View.GONE);
                tvWebLover.setVisibility(View.GONE);
                tvWebParents.setVisibility(View.VISIBLE);
                tvWebSelfOne.setVisibility(View.GONE);
                tvWebSelfTwo.setVisibility(View.GONE);
                break;
            case "综合意外互助计划":
                tvWebHead.setTextSize(11);
                tvWebKids.setVisibility(View.GONE);
                tvWebLover.setVisibility(View.GONE);
                tvWebParents.setVisibility(View.GONE);
                tvWebSelfOne.setVisibility(View.VISIBLE);
                tvWebSelfTwo.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }


        ImageView ivClose = view.findViewById(R.id.ic_close);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialogRadioGroup.setOnCheckedChangeListener(new MyRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(MyRadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.radioBtn1) {
                    fruitCount = "1";
                } else if (i == R.id.radioBtn2) {
                    fruitCount = "2";
                } else if (i == R.id.radioBtn5) {
                    fruitCount = "5";
                } else if (i == R.id.radioBtn10) {
                    fruitCount = "10";
                } else if (i == R.id.radioBtn20) {
                    fruitCount = "20";
                } else if (i == R.id.radioBtn50) {
                    fruitCount = "50";
                } else if (i == R.id.radioBtn100) {
                    fruitCount = "100";
                } else if (i == R.id.radioBtn200) {
                    fruitCount = "200";
                } else if (i == R.id.radioBtn500) {
                    fruitCount = "500";
                } else {
                    fruitCount = "0";
                }
            }
        });

        RxView.clicks(tvWebKids)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(HelpOtherActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "haKidPlan");
                        startActivity(intent);
                    }
                });

        RxView.clicks(tvWebLover)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(HelpOtherActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "haYoungPlan");
                        startActivity(intent);
                    }
                });

        RxView.clicks(tvWebParents)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(HelpOtherActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "haOldPlan");
                        startActivity(intent);
                    }
                });

        RxView.clicks(tvWebSelfOne)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(HelpOtherActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "haAccidentPlan");
                        startActivity(intent);
                    }
                });

        RxView.clicks(tvWebSelfTwo)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(HelpOtherActivity.this, "com.yilian.mall.ui.WebViewActivity"));
                        intent.putExtra("url", Constants.INTEGRAL_SET_PROTOCOL + "haWorkPlan");
                        startActivity(intent);
                    }
                });

        LinearLayout joinLayout = view.findViewById(R.id.layout_join);
        RxView.clicks(joinLayout)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {

                        Logger.i("2018年1月21日 16:04:37-tagId-" + tagId);

                        if (TextUtils.isEmpty(tagId)) {
                            showToast("请选择被保人");
                            return;
                        }

                        if (!TextUtils.isEmpty(intentTag)) {
                            if (!tagId.equals(intentTag)) {
                                showToast("被保人不可更改");
                                return;
                            }
                        }

                        if ("1".equals(tagId)) {
                            if (!TextUtils.isEmpty(intentName)) {
                                toNameStr = intentName;
                            } else {
                                toNameStr = "";
                            }
                            if (!TextUtils.isEmpty(intentIdNum)) {
                                idCardStr = intentIdNum;
                            } else {
                                idCardStr = "";
                            }
                            if (!TextUtils.isEmpty(intentBirthday)) {
                                birthdayStr = intentBirthday;
                            } else {
                                birthdayStr = "";
                            }
                        } else {
                            toNameStr = etToName.getText().toString();
                            idCardStr = etIdCard.getText().toString();
                            birthdayStr = tvBirthday.getText().toString();

                            if (TextUtils.isEmpty(toNameStr)) {
                                showToast("请填写互助人姓名");
                                return;
                            }
                            if (TextUtils.isEmpty(idCardStr)) {
                                showToast("请填写互助人身份证号");
                                return;
                            }
                            if (TextUtils.isEmpty(birthdayStr)) {
                                showToast("请填写正确的互助人身份证号");
                                return;
                            }
                        }

                        if (!TextUtils.isEmpty(intentName)) {
                            if (!toNameStr.equals(intentName)) {
                                showToast("互助人姓名不可更改");
                                return;
                            }
                        }

                        if (!TextUtils.isEmpty(intentIdNum)) {
                            if (!idCardStr.equals(intentIdNum)) {
                                showToast("互助人身份证号码不可更改");
                                return;
                            }
                        }

                        if (!TextUtils.isEmpty(intentBirthday)) {
                            if (!birthdayStr.equals(intentBirthday)) {
                                showToast("互助人生日不可更改");
                                return;
                            }
                        }

                        if (TextUtils.isEmpty(fruitCount)) {
                            showToast("请选择参与的健康果数量");
                            return;
                        }

                        Logger.i("2018年1月21日 16:04:37-fruitCount-" + fruitCount);
                        Logger.i("2018年1月21日 16:04:37-tagId-" + tagId);
                        Logger.i("2018年1月21日 16:04:37-toNameStr-" + toNameStr);
                        Logger.i("2018年1月21日 16:04:37-idCardStr-" + idCardStr);
                        Logger.i("2018年1月21日 16:04:37-birthdayStr-" + birthdayStr);

                        Logger.i("二〇一八年一月二十七日 23:07:28-走了间接");
                        startMyDialog();
                        tPresenter.getHelpOtherJoin(projectId, fruitCount, tagId, toNameStr, idCardStr, birthdayStr, intentId);
                    }
                });

        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.FILL_PARENT;
        dialogWindow.setAttributes(lp);

        dialog.show();
    }


    private UmengDialog mShareDialog;
    private String shareTitle, shareContent, shareImg, shareUrl;

    private void share() {

        if (shareImg.startsWith("http://") || shareImg.startsWith("https://")) {

        } else {
            shareImg = Constants.ImageUrl + shareImg;
        }

        shareUrl = Ip.getWechatURL(mContext) + "m/activity/health/helpPlan.php?project_id=" + projectId;

        Logger.i("2018年1月26日 09:00:19-" + shareTitle);
        Logger.i("2018年1月26日 09:00:19-" + shareContent);
        Logger.i("2018年1月26日 09:00:19-" + shareImg);
        Logger.i("2018年1月26日 09:00:19-" + shareUrl);

        if (TextUtils.isEmpty(shareTitle) || TextUtils.isEmpty(shareContent) || TextUtils.isEmpty(shareImg) || TextUtils.isEmpty(shareUrl)) {
            showToast("获取分享信息失败");
            return;
        }


        if (mShareDialog == null) {
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.library_module_anim_wellcome_bottom), R.style.library_module_DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            shareTitle,
                            shareContent,
                            shareImg,
                            shareUrl).share();
                }
            });
        }
        mShareDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initListener() {
        toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                if (on) {
                    not_yourself_ll.setVisibility(View.GONE);
                    tagId = "1";
                } else {
                    not_yourself_ll.setVisibility(View.VISIBLE);
                    if (kidRadioBtn.isChecked()) {
                        tagId = "4";
                    } else if (parentsRadioBtn.isChecked()) {
                        tagId = "3";
                    } else if (loverRadioBtn.isChecked()) {
                        tagId = "2";
                    } else {
                        tagId = null;
                    }
                }
            }
        });

        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                distance = i1;

                if (distance <= 0) {
                    back_iv.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.GONE);
                    share_img.setVisibility(View.VISIBLE);
                    ivShareBlack.setVisibility(View.GONE);
                    titleLayout.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));
                } else if (distance > 0 && distance <= bannerHeight) {
                    back_iv.setVisibility(View.VISIBLE);
                    ivBackBlack.setVisibility(View.VISIBLE);
                    share_img.setVisibility(View.VISIBLE);
                    ivShareBlack.setVisibility(View.VISIBLE);

                    float scale = (float) distance / bannerHeight;
                    float alpha = (255 * scale);
                    back_iv.setImageAlpha((int) (255 * (1 - scale)));
                    ivBackBlack.setImageAlpha((int) alpha);
                    share_img.setImageAlpha((int) (255 * (1 - scale)));
                    ivShareBlack.setImageAlpha((int) alpha);
                    titleLayout.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                } else {
                    back_iv.setVisibility(View.GONE);
                    ivBackBlack.setVisibility(View.VISIBLE);
                    share_img.setVisibility(View.GONE);
                    ivShareBlack.setVisibility(View.VISIBLE);
                    titleLayout.setBackgroundColor(Color.argb((int) 255, 255, 255, 255));
                }
            }
        });

        etToName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Logger.i("2018年1月19日 17:41:22-" + "beforeTextChanged");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Logger.i("2018年1月19日 17:41:22-" + "onTextChanged");
                String toNameStr = etToName.getText().toString().trim();
                if (TextUtils.isEmpty(toNameStr)) {
                    etIdCard.setText("");
                    tvBirthday.setText("");
                } else {
                    nPresenter.getHelpOtherFind(toNameStr);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Logger.i("2018年1月19日 17:41:22-" + "afterTextChanged");
            }
        });

        etIdCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /**
                 * 这里本来还要判断15位，即一代身份证，后来产品定一代不可用，故不再判断15位
                 */
                if (etIdCard.getText().length() == 18) {
                    Logger.i("2018年1月27日 19:02:57-" + "这里1 ");
                    if (RxRegTool.isIDCard18(etIdCard.getText().toString().trim())) {
                        Logger.i("2018年1月27日 19:02:57-" + "这里2 ");
                        int length = etIdCard.getText().length();
                        String str = etIdCard.getText().toString();
                        str = str.substring(length - 12, length - 8) + "-" + str.substring(length - 8, length - 6) + "-" + str.substring(length - 6, length - 4);
                        tvBirthday.setText(str);
                    } else {
                        try {
                            Logger.i("2018年1月27日 19:02:57-" + "这里3 ");
                            showToast(RxRegTool.IDCardValidate(etIdCard.getText().toString().trim()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                            Logger.i("2018年1月27日 19:02:57-" + "这里4 ");
                        }
                    }
                } else {
                    tvBirthday.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (i == R.id.rb_children) {
                    tagId = "4";
                } else if (i == R.id.rb_lover) {
                    tagId = "2";
                } else if (i == R.id.rb_parent) {
                    tagId = "3";
                }
            }
        });
    }

    @Override
    protected void initData() {
        startMyDialog();
        presenter.getHelpOtherDetail(projectId);
    }

    @Override
    public void onErrors(int flags, Throwable e) {
        if (1 == flags) {
            Logger.i("2018年1月20日 10:32:48-5555");
            showToast(e.getMessage());
        } else if (0 == flags) {
            Logger.i("2018年1月20日 10:32:48-6666");
            layoutNetError.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
            bottomLayout.setVisibility(View.GONE);
        }

        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        Logger.i("2018年1月20日 10:32:48-1111");
        if (baseEntity instanceof HelpOtherDetailBean) {
            Logger.i("2018年1月20日 10:32:48-2222");
            layoutNetError.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            bottomLayout.setVisibility(View.VISIBLE);

            HelpOtherDetailBean detailBean = (HelpOtherDetailBean) baseEntity;

            GlideUtil.showImage(mContext, detailBean.datas.previewImgUrl, ivTop);
            tvJoinCount.setText(detailBean.datas.total + "人参与互助");

            shareTitle = detailBean.datas.title;
            shareContent = detailBean.datas.desc;
            shareImg = detailBean.datas.imgUrl;

            tvName.setText(detailBean.datas.title);
            tvDesc.setText(detailBean.datas.desc);
            tvYear.setText(detailBean.datas.PLAN_PERIOD + "年");
            tvDate.setText(DateUtils.formatDate(Long.parseLong(detailBean.datas.effectDate)));

            String linkCss = "<style type=\"text/css\"> " +
                    "img {" +
                    "width:100%;" +
                    "height:auto;" +
                    "}" +
                    "body {" +
                    "margin-right:15px;" +
                    "margin-left:15px;" +
                    "margin-top:15px;" +
                    "}" +
                    "</style>";
            webViewCenter.getSettings().setJavaScriptEnabled(true);
            webViewCenter.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
            String htmlCenter = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no\">" + linkCss + "</head>" + detailBean.datas.absTract + "</body></html>";
            Logger.i("2018年1月27日 20:56:45-" + htmlCenter);
            webViewCenter.loadData(htmlCenter, "text/html; charset=UTF-8", null);

            webViewThree.getSettings().setJavaScriptEnabled(true);
            webViewThree.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
            String htmlThree = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no\">" + linkCss + "</head>" + detailBean.datas.amountContent + "</body></html>";
            webViewThree.loadData(htmlThree, "text/html; charset=UTF-8", null);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                tvCenter.setText(Html.fromHtml(detailBean.datas.absTract, Html.FROM_HTML_MODE_LEGACY));

                tvTitleOne.setText(Html.fromHtml(detailBean.datas.ruleTitle, Html.FROM_HTML_MODE_LEGACY));
                tvContentOne.setText(Html.fromHtml(detailBean.datas.ruleContent, Html.FROM_HTML_MODE_LEGACY));

                tvTitleTwo.setText(Html.fromHtml(detailBean.datas.rangeTitle, Html.FROM_HTML_MODE_LEGACY));
                tvContentTwo.setText(Html.fromHtml(detailBean.datas.rangeContent, Html.FROM_HTML_MODE_LEGACY));

                tvTitleThree.setText(Html.fromHtml(detailBean.datas.amountTitle, Html.FROM_HTML_MODE_LEGACY));
                tvContentThree.setText(Html.fromHtml(detailBean.datas.amountContent, Html.FROM_HTML_MODE_LEGACY));

                tvTitleFour.setText(Html.fromHtml(detailBean.datas.waitTitle, Html.FROM_HTML_MODE_LEGACY));
                tvContentFour.setText(Html.fromHtml(detailBean.datas.waitContent, Html.FROM_HTML_MODE_LEGACY));
            } else {
//                tvCenter.setText(Html.fromHtml(detailBean.datas.absTract));

                tvTitleOne.setText(Html.fromHtml(detailBean.datas.ruleTitle));
                tvContentOne.setText(Html.fromHtml(detailBean.datas.ruleContent));

                tvTitleTwo.setText(Html.fromHtml(detailBean.datas.rangeTitle));
                tvContentTwo.setText(Html.fromHtml(detailBean.datas.rangeContent));

                tvTitleThree.setText(Html.fromHtml(detailBean.datas.amountTitle));
                tvContentThree.setText(Html.fromHtml(detailBean.datas.amountContent));

                tvTitleFour.setText(Html.fromHtml(detailBean.datas.waitTitle));
                tvContentFour.setText(Html.fromHtml(detailBean.datas.waitContent));
            }

        } else if (baseEntity instanceof HelpOtherFindBAIBean) {
            Logger.i("2018年1月20日 10:32:48-3333");
            HelpOtherFindBAIBean findBean = (HelpOtherFindBAIBean) baseEntity;

            if (findBean.datas != null) {
                etIdCard.setText(findBean.datas.ID_NUM);
                tvBirthday.setText(findBean.datas.BIRTHDAY);
            } else {
                etIdCard.setText("");
                tvBirthday.setText("");
            }
        } else {
            Logger.i("2018年1月20日 10:32:48-4444");
            if (intentTag != null) {
                PreferenceUtils.writeBoolConfig(Constants.REFRESH_HEALTH_FAMILY_LIST, true, mContext);
            }
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_HEALTH_HOME, true, mContext);
            //刷新个人页面标识
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
            if (dialog != null) {
                dialog.dismiss();
            }
            finish();
            SkipUtils.toMyFamily(mContext);
        }
        stopMyDialog();
    }
}
