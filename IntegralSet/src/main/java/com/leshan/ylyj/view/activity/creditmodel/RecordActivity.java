package com.leshan.ylyj.view.activity.creditmodel;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.presenter.CreditPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.DialogPopup6;
import com.leshan.ylyj.customview.DialogPopup7;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.ToastUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rxfamily.entity.BaseEntity;
import rxfamily.entity.SubmitRecordEntity;


/**
 * 学历
 */
public class RecordActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout returnImg;
    private RelativeLayout record_region_rl;
    private RelativeLayout record_academy_rl;
    private RelativeLayout record_status_rl;
    private RelativeLayout record_education_rl;
    private RelativeLayout record_graduation_time_rl;
    private TextView record_submit_tv;
    private TextView record_condition_name;

    private TextView record_province_name;
    private TextView record_school_name;
    private TextView record_education_name;
    private TextView record_graduation_time_name;

    private TextView the_highest_record_tv, graduate_time_tv;

    private DialogPopup6 dialogPopup6;
    private DialogPopup7 dialogPopup7;
    private String condition;
    private View horizontal;
    private View horizontal2;
    private int identification;
    private String[] strings1 = new String[]{"在读", "毕业"};
    private String[] strings2 = new String[]{"中专", "大专", "本科", "硕士", "博士"};
    private String[] strings3 = new String[]{"1990", "1991", "1992", "1993", "1994", "1995", "1996", "1997", "1998", "1999","2000", "2001", "2002", "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016", "2017","2018"};
    private String[] strings4 = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};


    Dialog dialog;
    private String provinceid = "";
    private String schoolid = "";
    private int currentStatus;
    private int education;
    private String time;
    CreditPresenter creditPresenter;

    private boolean click = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        initToolbar();
        setToolbarTitle("学历学籍");
        hasBack(true);
        StatusBarUtil.setColor(this, Color.WHITE);
        initView();
        initListener();
        initListener();
    }

    @Override
    protected void initView() {
        returnImg = findViewById(R.id.returnImg);
        record_region_rl = findViewById(R.id.record_region_rl);
        record_academy_rl = findViewById(R.id.record_academy_rl);
        record_status_rl = findViewById(R.id.record_status_rl);
        record_submit_tv = findViewById(R.id.record_submit_tv);
        record_education_rl = findViewById(R.id.record_education_rl);
        record_education_name = findViewById(R.id.record_education_name);
        record_graduation_time_name = findViewById(R.id.record_graduation_time_name);
        record_graduation_time_rl = findViewById(R.id.record_graduation_time_rl);
        the_highest_record_tv = findViewById(R.id.the_highest_record_tv);
        graduate_time_tv = findViewById(R.id.graduate_time_tv);
        returnImg = findViewById(R.id.returnImg);
        record_province_name = findViewById(R.id.record_province_name);
        record_school_name = findViewById(R.id.record_school_name);
        record_condition_name = findViewById(R.id.record_condition_name);
        horizontal = findViewById(R.id.horizontal);
        horizontal2 = findViewById(R.id.horizontal2);
    }

    @Override
    protected void initListener() {
        record_region_rl.setOnClickListener(this);
        record_academy_rl.setOnClickListener(this);
        record_status_rl.setOnClickListener(this);
        record_submit_tv.setOnClickListener(this);
        record_education_rl.setOnClickListener(this);
        record_graduation_time_rl.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    private void ClickBack() {
        if (!record_province_name.getText().equals("") && !record_school_name.getText().equals("") && !record_condition_name.getText().equals("") && !record_education_name.getText().equals("") && !record_graduation_time_name.getText().equals("")) {
            record_submit_tv.setBackgroundResource(R.drawable.red_button_back);
            record_submit_tv.setTextColor(getResources().getColor(R.color.white));
            click = true;
//            Toast.makeText(this,"aaa",Toast.LENGTH_SHORT).show();
        } else {
            click = false;
//            Toast.makeText(this,"bbb",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onClick(View v) {

        int i = v.getId();
        if (i == R.id.record_region_rl) {

            SkipUtils.toEducationRegion(this);
        } else if (i == R.id.record_academy_rl) {
            SkipUtils.toSchoolName(this, provinceid);//院校名称,传入地区id

        } else if (i == R.id.record_status_rl) {
            DialogPopupShow(0, strings1);

        } else if (i == R.id.record_education_rl) {
            DialogPopupShow(1, strings2);

        } else if (i == R.id.record_graduation_time_rl) {
            identification = 2;
            dialogPopup7 = new DialogPopup7(this, strings3, strings4);
            dialogPopup7.showPopupWindow();

        } else if (i == R.id.record_submit_tv) {//提交
            if (click) {
                getSubmitRecord();
            } else {
//                Toast.makeText(this,"请填写完信息在提交",Toast.LENGTH_SHORT).show();
            }
//            DialogWhether();

        } else if (i == R.id.record_sure_tv) {
            DialogPopupOnclikc();

        } else if (i == R.id.record_cancel_tv) {
            if (identification == 2) {
                dialogPopup7.dismiss();
            } else {
                dialogPopup6.dismiss();
            }

        } else if (i == R.id.submit_cancel) {
            dialog.dismiss();

        } else if (i == R.id.submit_continue) {
//            SkipUtils.toSubmitEducation(this);
//            dialog.dismiss();
//            finish();
            if (click) {
                getSubmitRecord();
            } else {
//                Toast.makeText(this,"请填写完信息在提交",Toast.LENGTH_SHORT).show();
            }

        }
        ClickBack();
    }

    /**
     * @net 执行网络请求
     */
    private void getSubmitRecord() {
        startMyDialog();
        //@net 初始化presenter，并绑定网络回调，
        if (creditPresenter == null) {
            creditPresenter = new CreditPresenter(mContext, this);
        }
        //@net  进行网络请求
        if (currentStatus == 0) {
            creditPresenter.getSubmitRecords(AddMap(record_province_name.getText().toString(), provinceid, record_school_name.getText().toString(), schoolid, currentStatus, education, time, ""));
//                creditPresenter.getSubmitRecord(record_province_name.getText().toString(), provinceid, record_school_name.getText().toString(), schoolid, currentStatus, education, time, null);
        } else {
            creditPresenter.getSubmitRecords(AddMap(record_province_name.getText().toString(), provinceid, record_school_name.getText().toString(), schoolid, currentStatus, education, "", time));
//                creditPresenter.getSubmitRecord(record_province_name.getText().toString(), provinceid, record_school_name.getText().toString(), schoolid, currentStatus, education, null, time);
        }
        Logger.i("aaa---" + record_province_name.getText().toString() + "---" + provinceid + "---" + record_school_name.getText().toString() + "---" + schoolid + "---" + currentStatus + "---" + education + "---" + time + "---");
    }

    public Map<String, String> AddMap(String areaName, String areaId, String collegeName, String collegeId, int currentStatus, int education, String enrolTime, String graduateTime) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("areaName", areaName);
        map.put("areaId", areaId);
        map.put("collegeName", collegeName);
        map.put("collegeId", collegeId);
        map.put("currentStatus", String.valueOf(currentStatus));
        map.put("education", String.valueOf(education));
        map.put("enrolTime", enrolTime);
        map.put("graduateTime", graduateTime);
        return map;
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
        if (baseEntity instanceof SubmitRecordEntity) {
//            Toast.makeText(this,((SubmitRecordEntity) baseEntity).desc+"----",Toast.LENGTH_SHORT).show();
            SkipUtils.toSubmitEducation(this);
//            dialog.dismiss();
            finish();
        }
    }

    /**
     * 询问是否提交学历信息
     */
    private void DialogWhether() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = LayoutInflater.from(this).inflate(R.layout.dialog_submit_education_whether, null);
        TextView submit_cancel = view1.findViewById(R.id.submit_cancel);
        TextView submit_continue = view1.findViewById(R.id.submit_continue);
        submit_cancel.setOnClickListener(this);
        submit_continue.setOnClickListener(this);
        builder.setView(view1);
//                builder.setCancelable(false);//点击周围不会消失
        dialog = builder.create();
        dialog.show();
    }

    /**
     * 弹出dialogPopup6选择框
     *
     * @param identification
     * @param strings1
     */
    private void DialogPopupShow(int identification, String[] strings1) {
        this.identification = identification;
        dialogPopup6 = new DialogPopup6(this, strings1);
        dialogPopup6.showPopupWindow();
    }

    /**
     * 获取DialogPopup6选择的数据
     */
    private void DialogPopupOnclikc() {
        switch (identification) {
            case 0:
                record_condition_name.setText(dialogPopup6.picker.getContentByCurrValue());
                currentStatus = dialogPopup6.picker.getValue() + 1;
                dialogPopup6.dismiss();
                if (dialogPopup6.picker.getValue() == 1) {
                    the_highest_record_tv.setText("最高学历");
                    graduate_time_tv.setText("毕业时间");
                } else {
                    the_highest_record_tv.setText("学历");
                    graduate_time_tv.setText("入学时间");
                }
                record_education_rl.setVisibility(View.VISIBLE);
                record_graduation_time_rl.setVisibility(View.VISIBLE);
                horizontal.setVisibility(View.VISIBLE);
                horizontal2.setVisibility(View.VISIBLE);
                break;
            case 1:
                record_education_name.setText(dialogPopup6.picker.getContentByCurrValue());
                education = dialogPopup6.picker.getValue() + 1;
                dialogPopup6.dismiss();
                break;
            case 2:
                record_graduation_time_name.setText(dialogPopup7.picker.getContentByCurrValue() + "年" + dialogPopup7.picker1.getContentByCurrValue() + "月");
                time = dialogPopup7.picker.getContentByCurrValue() + "-" + dialogPopup7.picker1.getContentByCurrValue();
                dialogPopup7.dismiss();
                break;
        }
    }

    //接收下级页面传过来的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            record_province_name.setText(bundle.getString("province"));
            provinceid = bundle.getString("provinceid");
//            Toast.makeText(this, bundle.getString("provinceid"), Toast.LENGTH_SHORT).show();
        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            record_school_name.setText(bundle.getString("schoolname"));
            schoolid = bundle.getString("schoolid");
//            Toast.makeText(this, bundle.getString("schoolid"), Toast.LENGTH_SHORT).show();
        }
        ClickBack();
    }
}
