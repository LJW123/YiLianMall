package com.leshan.ylyj.view.activity.creditmodel;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.leshan.ylyj.adapter.DonationIntegralAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.CreditProtocolsPopwindow;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.RoundProgressBar;
import com.leshan.ylyj.utils.SkipUtils;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareDonationListActivity;
import com.leshan.ylyj.view.activity.welfaremodel.WelfareLoveMsgActivity;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.RefreshPersonCenter;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.CreditProtocolsEntity;
import rxfamily.entity.DonateEntity;
import rxfamily.entity.MyCreditEntity;
import rxfamily.entity.PublicBenefitHomeBageEntity;
import rxfamily.entity.ShortcutDonateEntity;


/**
 * 我的信用值
 */
public class MyCreditActivity extends BaseActivity implements View.OnClickListener {

    private RoundProgressBar credit_strip_rpb;
    private TextView my_credit_donation_integral;
    private RecyclerView donation_integral_rv;
    private TextView donation_integral_confirm_tv;
    private TextView my_credit_credit_administration_tv;
    private TextView my_credit_credit_improve_tv;
    private TextView my_credit_credit_share_tv;
    private TextView my_credit_understand_tv;
    private TextView my_credit_credit_up_tv;
    private TextView my_credit_time_tv;
    private TextView creditpercentage_tv;
    private TextView my_credit_grade_tv;

    private TextView love_quantity_tv;
    private LinearLayout ll_to_caredonation;
    private TextView integral_donate_tv;
    private ImageView credit_bill_img;

    Dialog dialog;
    private int positions;

    Timer updateTimer;
    TimerTask updateTimerTask;
    private int currentValue = 0;//当前值
    private int myNum = 0;//我的信用
    public static int max = 1000;

    CreditPresenter creditPresenter;
    List<ShortcutDonateEntity.LintegralListBean> lintegralListBeans;
    private String project_id;
    MyCreditEntity.DataBean myCreditEntity;
    MyCreditEntity myCreditEntity1;

    private RelativeLayout my_credit_back_rl;
    private LinearLayout public_benefit_ll;

    //用户协议 弹出
    private CreditProtocolsPopwindow creditProtocols;
    private Boolean isAgree = false;//是否 同意协议
    private ImageView iv_share_pro;
    private TextView tv_welfare_theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_credit);
        initView();
        initListener();
        initData();
        StatusBarUtils.transparencyBar(this);
    }

    @Override
    protected void initView() {
        credit_strip_rpb = findViewById(R.id.credit_strip_rpb);
        tv_welfare_theme=findViewById(R.id.tv_welfare_theme);
        my_credit_donation_integral = findViewById(R.id.my_credit_donation_integral);
        my_credit_credit_administration_tv = findViewById(R.id.my_credit_credit_administration_tv);
        my_credit_credit_improve_tv = findViewById(R.id.my_credit_credit_improve_tv);
        my_credit_credit_share_tv = findViewById(R.id.my_credit_credit_share_tv);
        my_credit_understand_tv = findViewById(R.id.my_credit_understand_tv);
        my_credit_credit_up_tv = findViewById(R.id.my_credit_credit_up_tv);
        my_credit_time_tv=findViewById(R.id.my_credit_time_tv);
        creditpercentage_tv=findViewById(R.id.creditpercentage_tv);
        my_credit_grade_tv=findViewById(R.id.my_credit_grade_tv);
        iv_share_pro=findViewById(R.id.iv_share_pro);
        //爱心数量
        love_quantity_tv=findViewById(R.id.love_quantity_tv);
        ll_to_caredonation=findViewById(R.id.ll_to_caredonation);
        integral_donate_tv=findViewById(R.id.integral_donate_tv);
        credit_bill_img=findViewById(R.id.credit_bill_img);
        my_credit_back_rl=findViewById(R.id.my_credit_back_rl);
        public_benefit_ll=findViewById(R.id.public_benefit_ll);

    }

    /**
     * 设置爱心数量
     */
    private void setLoveNum(String loveNum) {
        if (null == loveNum) {
            return;
        }
        loveNum = "凝聚" + formatLoveNum(loveNum) + "爱心";
        SpannableString spannableString = new SpannableString(loveNum);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FF5704"));
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(17, true);
        spannableString.setSpan(colorSpan, 2, loveNum.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, 2, loveNum.length() -2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Typeface typeface = FontUtils.getFontTypeface(mContext, "fonts/DINMittelschrift.otf");
        love_quantity_tv.setTypeface(typeface);
        love_quantity_tv.setText(spannableString);
    }

    /**
     * 格式化爱心数量
     *
     * @param loveNum
     * @return
     */
    private String formatLoveNum(String loveNum) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < loveNum.length(); i++) {
            buffer.append(loveNum.charAt(i));
            if ((i + 1) % 3 == 0 && i != loveNum.length() - 1) {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }


    @Override
    protected void initListener() {
        my_credit_understand_tv.setOnClickListener(this);
        my_credit_credit_share_tv.setOnClickListener(this);
        my_credit_credit_administration_tv.setOnClickListener(this);
        my_credit_credit_improve_tv.setOnClickListener(this);
        my_credit_donation_integral.setOnClickListener(this);
        my_credit_credit_up_tv.setOnClickListener(this);
        ll_to_caredonation.setOnClickListener(this);
        integral_donate_tv.setOnClickListener(this);
        credit_bill_img.setOnClickListener(this);
        iv_share_pro.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        my_credit_time_tv.setText("评估于" + date);
        credit_strip_rpb.setMax(max);
        getHomePage();//获取首页数据
    }

    /**
     * @net 执行网络请求
     */
    private void getHomePage() {
        //@net 初始化presenter，并绑定网络回调，
        startMyDialog(false);
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        creditPresenter.getHomePage();
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
        if (baseEntity instanceof MyCreditEntity) {
            myCreditEntity = ((MyCreditEntity) baseEntity).data;
            myCreditEntity1=(MyCreditEntity) baseEntity;
            if (myCreditEntity.dataCredit.getIsAgree() == 0) {// 同意协议状态 1同意0不同意
                Logger.i("获取的html:" + myCreditEntity.dataCredit.creditAgreement);
                showCreditProtocols(myCreditEntity.dataCredit.creditAgreement);
            } else {
                setHomePage((MyCreditEntity) baseEntity);
                getPublicBenefitHomeBage();
            }
        } else if (baseEntity instanceof PublicBenefitHomeBageEntity) {
            PublicBenefitHomeBageEntity.DataBean dataBean = ((PublicBenefitHomeBageEntity) baseEntity).data;
            List<PublicBenefitHomeBageEntity.ListBean> listBean = ((PublicBenefitHomeBageEntity) baseEntity).data.list;
            if (listBean!=null&&listBean.size()>0) {
                project_id = listBean.get(0).getId();
                GlideUtil.showImageRadius(mContext, listBean.get(0).getImg1(), iv_share_pro,5);
                tv_welfare_theme.setText(listBean.get(0).getProjectName());
                if (TextUtils.isEmpty(dataBean.totalIntegral)){
                    setLoveNum("0");
                }else {
                    Integer integral=Integer.parseInt(dataBean.totalIntegral)/100;
                    setLoveNum(integral+"");
                }
                public_benefit_ll.setVisibility(View.VISIBLE);
            }else{
                public_benefit_ll.setVisibility(View.INVISIBLE);
            }
        } else if (baseEntity instanceof ShortcutDonateEntity) {
            lintegralListBeans = new ArrayList<>();
            lintegralListBeans = ((ShortcutDonateEntity) baseEntity).data.integralList;
            DialogWhether();
        } else if (baseEntity instanceof CreditProtocolsEntity) {
            isAgree = true;
            setHomePage(myCreditEntity1);
            if (creditProtocols != null){
                creditProtocols.dismiss();
            }
            getPublicBenefitHomeBage();
        } else if (baseEntity instanceof DonateEntity) {
            RefreshPersonCenter.refresh(this);
            Toast toast = Toast.makeText(MyCreditActivity.this, "成功捐赠" + lintegralListBeans.get(positions).getIntegral() + "奖券,感谢您的爱心！", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            dialog.dismiss();
            stopMyDialog();
            getHomePage();
            getPublicBenefitHomeBage();
        }
    }

    private void getPublicBenefitHomeBage() {
        creditPresenter.getPublicBenefitHomeBage("0", "1");
    }

    /**
     * 更新数据
     *
     * @param myCreditEntity
     */
    private void setHomePage(MyCreditEntity myCreditEntity) {
        credit_strip_rpb.setMax(myCreditEntity.data.dataCredit.getCreditCeiling());
        credit_strip_rpb.invalidate();
        my_credit_grade_tv.setText(myCreditEntity.data.dataCredit.getCreditLevelName());
        my_credit_time_tv.setText("评估于" + myCreditEntity.data.dataCredit.getUpdateTime());
        myNum = myCreditEntity.data.dataCredit.getCreditTotal();

        TotalPoints();
        creditpercentage_tv.setText("高于" + myCreditEntity.data.dataCreditPercentage.getCreditPercentage() + "的用户");
        if (myNum < 450) {
            my_credit_back_rl.setBackgroundResource(R.drawable.credit_poor_gradation_back);
        } else if (myNum >= 450 && myNum < 600) {
            my_credit_back_rl.setBackgroundResource(R.drawable.credit_medium_gradation_back);
        } else if (myNum >= 600 && myNum < 750) {
            my_credit_back_rl.setBackgroundResource(R.drawable.credit_well_gradation_back);
        } else if (myNum >= 750 && myNum < 900) {
            my_credit_back_rl.setBackgroundResource(R.drawable.credit_good_gradation_back);
        } else if (myNum >= 900) {
            my_credit_back_rl.setBackgroundResource(R.drawable.credit_excellence_gradation_back);
        }
    }

    /**
     * 信用总分
     */
    private void TotalPoints() {

        updateTimer = new Timer();

        updateTimerTask = new TimerTask() {
            @Override
            public void run() {
                currentValue += 5;
                if (currentValue >= myNum)
                    currentValue = myNum;
                credit_strip_rpb.setValue(currentValue);
            }
        };
        updateTimer.schedule(updateTimerTask, 0, 10);
    }

    /**
     * 爱心捐赠dialog
     */
    private void DialogWhether() {
        positions = 0;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_donation_integral, null);
        donation_integral_rv = view1.findViewById(R.id.donation_integral_rv);
        donation_integral_confirm_tv = view1.findViewById(R.id.donation_integral_confirm_tv);
        donation_integral_confirm_tv.setOnClickListener(this);
//        identification_ok.setOnClickListener(this);
        builder.setView(view1);
//                builder.setCancelable(false);//点击周围不会消失
        dialog = builder.create();

//        donation_integral_rv.setLayoutManager(new GridLayoutManager(this, 3));
//        adress_edit_rv.addItemDecoration(new DividerGridItemDecoration(this));

        //添加标签数据
//        addInformation();
        final DonationIntegralAdapter adapter = new DonationIntegralAdapter(this, lintegralListBeans);
        GridLayoutManager manager = new GridLayoutManager(this, 3);
        donation_integral_rv.setLayoutManager(manager);
        donation_integral_rv.setAdapter(adapter);
        adapter.setOnItemClickListener(new DonationIntegralAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String data, int position) {
                positions = position;
            }
        });


        dialog.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.returnImg) {
            finish();
        } else if (i == R.id.my_credit_donation_integral) {//点击捐赠大捐赠按钮
            startMyDialog(false);
            creditPresenter.getShortcutDonate("1");

        } else if (i == R.id.my_credit_credit_administration_tv) {
            SkipUtils.toCreditManagement(this);

        } else if (i == R.id.my_credit_credit_improve_tv) {//提升信用
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.PROMOTE_CREDIT);
            startActivity(intent);
        } else if (i == R.id.donation_integral_confirm_tv) {//点击捐赠奖券
            startMyDialog(false);
            creditPresenter.getDonate(project_id, String.valueOf(lintegralListBeans.get(positions).getIntegral() * 100), "2");
            Logger.i("---奖券捐赠---" + project_id + "---" + String.valueOf(lintegralListBeans.get(positions).getIntegral() * 100));
        } else if (i == R.id.my_credit_credit_share_tv) {//分享信用
            SkipUtils.toShareCredit(this, String.valueOf(myNum));

        } else if (i == R.id.my_credit_understand_tv) {//了解信用
            SkipUtils.toKnowMoreCredit(this,myNum);
        } else if (i == R.id.my_credit_credit_up_tv) {//UP指数

        } else if (i == R.id.ll_to_caredonation) {//查看全部
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.leshan.ylyj.view.activity.welfaremodel.WelfareDonationListActivity"));
            startActivityForResult(intent,0);

        } else if (i == R.id.iv_share_pro) {//公益主图片点击事件
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.WELFARE_LOVE_DONATION + project_id);
            startActivityForResult(intent,TO_WELFARE_DETAILS_CODE);
        } else if (i == R.id.integral_donate_tv) {//奖券捐赠协议
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.YLYJ_USER_AGREEMENT);
            startActivity(intent);
        } else if (i == R.id.credit_bill_img) {//信用账单
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(mContext, "com.yilian.mall.ui.WebViewActivity"));
            intent.putExtra(Constants.SPKEY_URL, Constants.CREDIT_BILL);
            startActivity(intent);
        }
    }
    private static final int TO_WELFARE_DETAILS_CODE=0;


    //显示协议窗口
    public void showCreditProtocols(String str) {
        creditProtocols = new CreditProtocolsPopwindow(this, str);
        creditProtocols.setOnNegativeListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        creditProtocols.setOnPositiveListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                creditPresenter.setAgreement("1");
            }
        });
        //设置Popupwindow显示位置（从底部弹出）
        creditProtocols.showAtLocation(my_credit_credit_administration_tv, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        backgroundAlpha(0.3f);
        //设置Popupwindow关闭监听，当Popupwindow关闭，背景恢复1f
        creditProtocols.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });
        creditProtocols.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (isAgree) {
                    backgroundAlpha(1f);
                    creditProtocols.dismiss();
                } else {
                    finish();
                }
            }
        });
    }

    /**
     * 设置添加屏幕的背景透明度
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode== WelfareLoveMsgActivity.TO_PERSON_DETAIL){
          Intent intent=new Intent(mContext, WelfareDonationListActivity.class);
          intent.putExtra("from",TO_WELFARE_DETAILS_CODE+"");
          startActivityForResult(intent,0);
        }
        getPublicBenefitHomeBage();
    }
}
