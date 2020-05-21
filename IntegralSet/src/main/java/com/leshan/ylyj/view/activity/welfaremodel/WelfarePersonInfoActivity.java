package com.leshan.ylyj.view.activity.welfaremodel;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.chad.library.adapter.base.BaseQuickAdapter;

import rxfamily.entity.WelfareExpericeEntity;
import rxfamily.entity.WelfareGrowthSystem;
import rxfamily.entity.WelfareLoveEntity;
import rxfamily.entity.WelfarePersonInfoEntity;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.customview.LoveLevelView;
import com.leshan.ylyj.adapter.DonationAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.WelfarePresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.FontUtils;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.MenuUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rxfamily.entity.BaseEntity;


/**
 * 公益个人信息界面
 *
 * @author ZYH
 */
public class WelfarePersonInfoActivity extends BaseActivity implements View.OnClickListener {
    private RecyclerView recycleView;
    private DonationAdapter adapter;
    private Button toDonationBtn;
    private TextView tvLoveNum;
    private LoveLevelView levelView;
    //成长体系弹窗
    private Dialog mLoveDialog;
    //爱心成就弹框
    private Dialog mAchievementDialog;
    private ImageView rightImage, ivHeaderPhoto;
    private WindowManager windowManager;
    private Display display;
    private ProgressBar mProgressBar;
    private WelfarePresenter mPresent;
    private WelfarePersonInfoEntity personInfo;
    private List<WelfarePersonInfoEntity.Data.Condition> acheivementList;
    private TextView tvLoveWeight;
    private TextView tvCurLevel;
    private TextView tvLevelName;
    private TextView tvName;
    private TextView tvLoveExpericeNum;
    private TextView tvIntegral;
    private TextView tvDonationNum;
    private ImageView ivBigStar;
    private ImageView ivSmallStar0;
    private ImageView ivSmallStar1;
    private ImageView ivSmallStar2;
    private TextView tvDonationNum0;
    private TextView tvDonationNum1;
    private TextView tvDonationNum2;
    private TextView tvTatalTimes;
    private ImageView ivLeft;
    private ImageView iconSetting;


    //底部底
    private String[] levelBottomList = new String[]{};
    //顶级名称列表
    private String[] levelTopList;
    //级别集合
    private List<Integer> levelIntegral = new ArrayList<>();

    //成长体系当前级别
    private int currentLevel=0;

    //成长体系相关控件
    private ProgressBar mPbLoveProgress;
    private TextView mTvExpWeight, tvLevleName, tvLevel, tvNextLevel, tvCurLevelName, tvNextLevelName;
    private WelfareExpericeEntity expericeBase;
    private ImageView ivFirstDonation;
    private ImageView ivSecondDonation;
    private ImageView ivThreeDonation;
//    private WelfareLoveEntity loveBean;
//    private String loveNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_care_donation);

        //执行网络请求
        initView();
        initData();
        initListener();
        StatusBarUtil.setColor(this, Color.WHITE);
    }
    @Override
    protected void initView() {
        StatusBarUtil.setColor(this, getResources().getColor(R.color.white));
        windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        tvCurLevel = (TextView) findViewById(R.id.tv_level);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvLoveExpericeNum = (TextView) findViewById(R.id.tv_experience_num);
        rightImage = (ImageView) findViewById(R.id.right_image);
        ivHeaderPhoto = (ImageView) findViewById(R.id.iv_header_photo);
        recycleView = (RecyclerView) findViewById(R.id.donation_rv);
        tvLoveWeight = (TextView) findViewById(R.id.tv_love_weight);
        toDonationBtn = (Button) findViewById(R.id.to_donation_btn);
        tvLoveNum = (TextView) findViewById(R.id.tv_love_num);
        tvLevelName = (TextView) findViewById(R.id.level_titile);
        tvIntegral = (TextView) findViewById(R.id.tv_donation_integral);
        tvDonationNum = (TextView) findViewById(R.id.tv_donation_num);
        ivLeft=(ImageView) findViewById(R.id.left_back);
        iconSetting=(ImageView) findViewById(R.id.iv_iocn_share);
        iconSetting.setImageResource(R.mipmap.icon_setting);
        iconSetting.setVisibility(View.VISIBLE);


        acheivementList = new ArrayList<>();
        adapter = new DonationAdapter(acheivementList);

        recycleView.setLayoutManager(new GridLayoutManager(this, 3));
        recycleView.setNestedScrollingEnabled(false);
        recycleView.setAdapter(adapter);
        initListData();
        adapter.setNewData(acheivementList);
        if (mPresent == null) {
            mPresent = new WelfarePresenter(mContext, this);
        }
    }
    @Override
    protected void initData() {
        getWelfarePersonInfo();
        getWelfareExperice();
        getWelfareGrowthSys();
//        loveNum = getIntent().getStringExtra("love_num");
//        if (null==loveNum){
//            getWelfareLoveNum();
//        }else {
//            setLoveNum(loveNum);
//        }

    }
    @Override
    protected void initListener() {
        toDonationBtn.setOnClickListener(this);
        tvLevelName.setOnClickListener(this);
        rightImage.setOnClickListener(this);
        ivHeaderPhoto.setOnClickListener(this);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WelfarePersonInfoEntity.Data.Condition condition = (WelfarePersonInfoEntity.Data.Condition) adapter.getItem(position);
                showVirtueDialog(condition);
            }
        });
        ivLeft.setOnClickListener(this);
        iconSetting.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.left_back||id==R.id.to_donation_btn) {
            finish();
        } else if (id == R.id.iv_iocn_share) {
            showMenu();
        }else if (id == R.id.iv_dissmss) {
            dismissAllDialog();
        } else if (id == R.id.level_titile) {
            showLoveDiolog();
        } else if (id == R.id.iv_cancel) {
            dismissAllDialog();
        }else if (id==R.id.tv_get_experice){
            SkipUtils.toWelfareGetOperation(mContext);
        }
    }
    private void showMenu() {
        MenuUtil.showMenu(WelfarePersonInfoActivity.this, R.id.iv_iocn_share);
    }

    /**
     * 设置占位列表
     */
    private void initListData(){
        for (int i=0;i<6;i++){
            WelfarePersonInfoEntity.Data.Condition condition=new WelfarePersonInfoEntity.Data.Condition();
            acheivementList.add(condition);
        }
    }

    /**
     * 设置当前的进度
     *
     * @param progress
     */
    private void setLevelProgress(int progress, int maxProgress) {
        if (mProgressBar == null) {
            mProgressBar = findViewById(R.id.pb_love_progress);
        }
        mProgressBar.setMax(maxProgress);
        mProgressBar.setProgress(progress);
    }





    /**
     * 获取成长体系
     */
    private void getWelfareGrowthSys() {
        mPresent.getWelfareGrowthSystem();
    }

    /**
     * 获取公益个人详情数据
     */

    private void getWelfarePersonInfo() {
        mPresent.getWelfarePersonalInfo();

    }

    /**
     * 获取公益个人信息经验值
     */

    private void getWelfareExperice() {
        startMyDialog(false);
        mPresent.getWelfareExperice();
    }

    /**
     * 获取爱心数量
     */
    private void getWelfareLoveNum(){
        mPresent.getWelfareLoveNum();
    }


    /**
     * 设置爱心数量
     */
    private void setLoveNum(String loveNum) {
        if (null == loveNum) {
            return;
        }
        loveNum = "您凝聚" + formatLoveNum(loveNum) + "爱心";
        SpannableString spannableString = new SpannableString(loveNum);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FA5C16"));
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(27, true);
        spannableString.setSpan(colorSpan, 3, loveNum.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, 3, loveNum.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        Typeface typeface = FontUtils.getFontTypeface(mContext, "fonts/DINMittelschrift.otf");
        tvLoveNum.setTypeface(typeface);
        tvLoveNum.setText(spannableString);
    }

    /**
     * 格式化爱心数量
     *
     * @param loveNum
     * @return
     */
    private String formatLoveNum(String loveNum) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" ");
        for (int i = 0; i < loveNum.length(); i++) {
            buffer.append(loveNum.charAt(i));
            if ((i + 1) % 3 == 0 && i != loveNum.length() - 1) {
                buffer.append(",");
            }
        }
        return buffer.toString() + " ";
    }



    /**
     * 展示爱心体系dialog
     */
    private void showLoveDiolog() {
        if (mLoveDialog == null) {
            mLoveDialog = new Dialog(mContext, R.style.Custom_dialog);
            View layout = View.inflate(mContext, R.layout.dialog_love_layout, null);
            mPbLoveProgress = (ProgressBar) layout.findViewById(R.id.pb_love_progress);
            mLoveDialog.setContentView(layout);
            levelView = (LoveLevelView) layout.findViewById(R.id.love_level_view);


            tvNextLevelName = (TextView) layout.findViewById(R.id.tv_next_level_name);
            tvCurLevelName = (TextView) layout.findViewById(R.id.tv_curlevel_name);
            tvNextLevel = (TextView) layout.findViewById(R.id.tv_next_level);
            tvLevel = (TextView) layout.findViewById(R.id.tv_level);
            tvLevleName = (TextView) layout.findViewById(R.id.tv_leve_name);
            mTvExpWeight = (TextView) layout.findViewById(R.id.tv_expnum_weight);
            mPbLoveProgress = (ProgressBar) layout.findViewById(R.id.pb_love_progress);
            layout.findViewById(R.id.iv_cancel).setOnClickListener(this);
            layout.findViewById(R.id.tv_get_experice).setOnClickListener(this);
            Window window = mLoveDialog.getWindow();// 获取Window对象
            window.setLayout(display.getWidth(), display.getHeight());
            window.setGravity(Gravity.CENTER);// 居中显示
        }
        if (null==growthSystem){
            getWelfareGrowthSys();
        }else {
            setGrowthSystemInfo();
        }
        if (null != expericeBase) {
            setLevelInfo();
        }else {
           getWelfareExperice();
        }
        mLoveDialog.show();

    }

    private void setLevelInfo() {
        tvNextLevel.setText("Lv." + expericeBase.data.supLevel);
        tvNextLevelName.setText(expericeBase.data.supName);
        tvLevel.setText("Lv." + expericeBase.data.level);

       if (expericeBase.data.levelName!=null){
           String levelString="Lv." + expericeBase.data.level + expericeBase.data.levelName;
           int index=expericeBase.data.levelName.length();
           SpannableString spannableString=new SpannableString(levelString);
           spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),levelString.length()-index,levelString.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
           tvLevleName.setText(spannableString);
       }
        mPbLoveProgress.setMax(expericeBase.data.end);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mPbLoveProgress.setProgress(expericeBase.data.totalCredit, true);
        } else {
            mPbLoveProgress.setProgress(expericeBase.data.totalCredit);
        }
        mTvExpWeight.setText(expericeBase.data.totalCredit + "/" + expericeBase.data.end);
        tvCurLevelName.setText(expericeBase.data.levelName);
    }

    /**
     * 爱心成就dialog
     */
    private void showVirtueDialog(WelfarePersonInfoEntity.Data.Condition condition) {
        if (condition==null||condition.sanImgUrlList==null||condition.rule==null){
            return;
        }
        if (mAchievementDialog == null) {
            mAchievementDialog = new Dialog(mContext, R.style.Custom_dialog);
            View view = View.inflate(mContext, R.layout.dialog_chievement_layout, null);
            view.findViewById(R.id.iv_dissmss).setOnClickListener(this);
            ivBigStar = (ImageView) view.findViewById(R.id.iv_big_star);
            ivSmallStar0 = (ImageView) view.findViewById(R.id.iv_small_star0);
            ivSmallStar1 = (ImageView) view.findViewById(R.id.iv_small_star1);
            ivSmallStar2 = (ImageView) view.findViewById(R.id.iv_small_star2);
            tvDonationNum0 = (TextView) view.findViewById(R.id.tv_donation_num0);
            tvDonationNum1 = (TextView) view.findViewById(R.id.tv_donation_num1);
            tvDonationNum2 = (TextView) view.findViewById(R.id.tv_donation_num2);
            tvTatalTimes = (TextView) view.findViewById(R.id.tv_total_time);

            ivFirstDonation=(ImageView)view.findViewById(R.id.iv_first_donation);
            ivSecondDonation=(ImageView)view.findViewById(R.id.iv_second_donation);
            ivThreeDonation=(ImageView)view.findViewById(R.id.iv_three_donation);


            mAchievementDialog.setContentView(view);
            Window window = mAchievementDialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setLayout(display.getWidth(), display.getHeight());
        }
        GlideUtil.showImage(mContext, condition.secondaryImgUrl, ivBigStar);
        for (int i = 0; i < condition.sanImgUrlList.size(); i++) {
            ImageView currentImage = null;
            if (i == 0) {
                currentImage = ivSmallStar0;
            }
            if (i == 1) {
                currentImage = ivSmallStar1;
            }
            if (i == 2) {
                currentImage = ivSmallStar2;
            }
            if (currentImage != null) {
                GlideUtil.showImage(mContext, condition.sanImgUrlList.get(i), currentImage);
            }
        }
        int index=0;
        Double donationNums=Double.parseDouble(personInfo.data.donationNumber);
        for (int i = 0; i < condition.rule.size(); i++) {
            if (i == 0) {
                formatTimes(condition.rule.get(i), tvDonationNum0);
                if (donationNums>=Double.parseDouble(condition.rule.get(i))){
                    ivFirstDonation.setImageResource(R.mipmap.light_heart);
                }else {
                    ivFirstDonation.setImageResource(R.mipmap.gray_heart);
                }
            }
            if (i == 1) {
                if (donationNums>=Double.parseDouble(condition.rule.get(i))){
                    ivSecondDonation.setImageResource(R.mipmap.light_heart);
                }else {
                    ivSecondDonation.setImageResource(R.mipmap.gray_heart);
                }
                formatTimes(condition.rule.get(i), tvDonationNum1);
            }
            if (i == 2) {
                if (donationNums>=Double.parseDouble(condition.rule.get(i))){
                    ivThreeDonation.setImageResource(R.mipmap.light_heart);
                }else {
                    ivThreeDonation.setImageResource(R.mipmap.gray_heart);
                }
                formatTimes(condition.rule.get(i), tvDonationNum2);
                
            }
        }
        formatTotalContributionTime(personInfo.data.donationNumber);
        mAchievementDialog.show();
    }

    /**
     * 格式化
     *
     * @param times
     * @param view
     */
    private void formatTimes(String times, TextView view) {
        SpannableString spannableString = new SpannableString("已完成" + times + "次\n捐款");
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#CCCCCC"));
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(13, true);
        StyleSpan styleSpan = new StyleSpan(Typeface.BOLD);
        spannableString.setSpan(colorSpan, 3, times.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(sizeSpan, 3, times.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(styleSpan, 3, times.length() + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        view.setText(spannableString);
    }

    /**
     * 设置捐款次数
     */
    private void formatTotalContributionTime(String totalTimes) {
        totalTimes = "已完成捐款 " + totalTimes + " 次";
        SpannableString spannableString = new SpannableString(totalTimes);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(18, true);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#FE6D37"));
        StyleSpan styleSpan=new StyleSpan(android.graphics.Typeface.BOLD);
        spannableString.setSpan(sizeSpan, 6, totalTimes.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(colorSpan, 6, totalTimes.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       spannableString.setSpan(styleSpan,6, totalTimes.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTatalTimes.setText(spannableString);
    }
    /**
     * 设置比重的不同颜色
     * @param weight
     */
    private void setCreditWeight(String weight){
        int index=weight.indexOf("/");
        SpannableString spannableString=new SpannableString(weight);
        ForegroundColorSpan colorSpan=new ForegroundColorSpan(Color.parseColor("#007ED9"));
        spannableString.setSpan(colorSpan,0,index,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLoveWeight.setText(spannableString);
    }


    @Override
    public void onCompleted() {
        if (personInfo!=null&&expericeBase!=null&&growthSystem!=null){
//            if (null==loveNum&&loveBean!=null){
//                stopMyDialog();
//                return;
//            }
            stopMyDialog();
        }
    }

    @Override
    public void onError(Throwable e) {
        stopMyDialog();
        showToast(e.getMessage());
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        setWelfareAchievement(baseEntity);
        setWelfareExprice(baseEntity);
        setGrowthSysData(baseEntity);
//        setWelfareLoveNum(baseEntity);
    }

//    private void setWelfareLoveNum(BaseEntity baseEntity) {
//        if (baseEntity instanceof WelfareLoveEntity){
//            loveBean= (WelfareLoveEntity) baseEntity;
//            if (null==loveBean.data){
//                return;
//            }
//            String loveNum=loveBean.data.total;
//            setLoveNum(loveNum);
//        }
//    }

    private WelfareGrowthSystem growthSystem;

    private void setGrowthSysData(BaseEntity baseEntity) {
        if (baseEntity instanceof WelfareGrowthSystem) {
            growthSystem = (WelfareGrowthSystem) baseEntity;
            if (mLoveDialog!=null){
                setGrowthSystemInfo();
            }
        }
    }
    private void setGrowthSystemInfo(){
        if (levelIntegral.size()==0){
            List<WelfareGrowthSystem.Data.Details> lisGrowthSys = growthSystem.data.list;
            levelTopList = new String[lisGrowthSys.size()];
            levelBottomList = new String[lisGrowthSys.size()];
            for (int i = 0; i < lisGrowthSys.size(); i++) {
                levelIntegral.add(lisGrowthSys.get(i).level);
                levelTopList[i] = lisGrowthSys.get(i).levelName;
                levelBottomList[i] = "Lv." + lisGrowthSys.get(i).level;
            }
            levelView.setBottomTextArray(levelBottomList);
            levelView.setTopTextArray(levelTopList);


            if (currentLev>levelIntegral.get(levelIntegral.size()-1)){
                levelView.setCurLeve(levelIntegral.size() + 1);
            }else {
                for (int i=0;i<levelIntegral.size();i++){
                    if (i==0){
                        currentLevel=0;
                    }else {
                        if (levelIntegral.get(i)<=currentLev){
                            currentLevel++;
                        }
                    }
                }
                levelView.setCurLeve(currentLevel + 1);
            }
            levelView.resetDrawView();
        }
    }

    /**
     * 设置累计经验
     */
    private int currentLev = 0;
    private String totalIntegral;

    private void setWelfareExprice(BaseEntity baseEntity) {
        if (baseEntity instanceof WelfareExpericeEntity) {
            expericeBase = (WelfareExpericeEntity) baseEntity;
            //头像
            GlideUtil.showCirHeadNoSuffix(mContext, expericeBase.data.photo, ivHeaderPhoto);
            //当前等级
            tvCurLevel.setText("Lv." + expericeBase.data.level);
            //获取到当前级别
            currentLev = expericeBase.data.level;
            //等级名称
            tvLevelName.setText(expericeBase.data.levelName);
            //用户名字
            if (TextUtils.isEmpty(expericeBase.data.name)){
                tvName.setText("暂无昵称");
            }else {
                tvName.setText(expericeBase.data.name);
            }
            //累计爱心
            tvLoveExpericeNum.setText("累计爱心经验" + expericeBase.data.totalCredit);
            //爱心进度设置
            setLevelProgress(expericeBase.data.totalCredit, expericeBase.data.end);
            //格式化累积奖券数量
            formatIntegral(expericeBase.data.totalIntegral);
            //累计次数
            tvDonationNum.setText(expericeBase.data.count);
            if (mLoveDialog!=null){
                setLevelInfo();
            }
            if (totalIntegral==null){
                totalIntegral=expericeBase.data.totalIntegral;
                Integer itegral=Integer.parseInt(totalIntegral)/100;
                setLoveNum(itegral+"");
            }
        }
    }

    /**
     * 格式化累积奖券数量
     *
     * @param intergral
     */
    private void formatIntegral(String intergral) {
        if (TextUtils.isEmpty(intergral)) {
            intergral = "0.00";
        }
        DecimalFormat df = new DecimalFormat("######0.00");
        Double integrals = Double.parseDouble(intergral)/100;
        String interalTotal = df.format(integrals);
        SpannableString spand = new SpannableString(interalTotal);
        AbsoluteSizeSpan sizeSpan = new AbsoluteSizeSpan(30, true);
        int index = interalTotal.indexOf(".");
        spand.setSpan(sizeSpan, 0, index, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvIntegral.setText(spand);
    }

    /**
     * 设置公益成就数据
     *
     * @param baseEntity
     */
    private void setWelfareAchievement(BaseEntity baseEntity) {
        if (baseEntity instanceof WelfarePersonInfoEntity) {
            adapter.setFlag(false);
            acheivementList.clear();
            personInfo = (WelfarePersonInfoEntity) baseEntity;
            acheivementList.addAll(personInfo.data.condition);
            //设置数据
            adapter.setNewData(acheivementList);
            //设置百分比
            String weight = personInfo.data.completed + "/" + acheivementList.size();
             setCreditWeight(weight);
            tvDonationNum.setText(personInfo.data.donationNumber + "");
        }
    }

    @Override
    public void finish() {
        dismissAllDialog();
        super.finish();
    }

    /**
     * 弹框消失
     */

    private void dismissAllDialog() {
        if (mAchievementDialog != null && mAchievementDialog.isShowing()) {
            mAchievementDialog.dismiss();
        }
        if (mLoveDialog != null && mLoveDialog.isShowing()) {
            mLoveDialog.dismiss();
        }
    }

}
