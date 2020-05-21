package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.BranchBankListAdapter;
import com.yilian.mall.entity.JBankModel;
import com.yilian.mall.entity.JRegionModel;
import com.yilian.mall.retrofitutil.RetrofitUtils;
import com.yilian.mylibrary.widget.ClearEditText;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.networkingmodule.entity.BranchListEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ray_L_Pain on 2017/7/12 0012.
 */

public class JBindCardActivity extends BaseActivity {
    @ViewInject(R.id.v3Back)
    ImageView v3Back;
    @ViewInject(R.id.v3Title)
    TextView v3Title;
    @ViewInject(R.id.sv)
    ScrollView sv;
    @ViewInject(R.id.ll_choice_bank)
    LinearLayout ll_choice_bank;
    @ViewInject(R.id.ll_choice_address)
    LinearLayout ll_choice_address;
    @ViewInject(R.id.textView4)
    TextView tv4;
    @ViewInject(R.id.listView)
    ListView listView;
    @ViewInject(R.id.btn_next)
    Button btn_next;
    @ViewInject(R.id.tv_bank_name)
    TextView tv_bank_name;
    @ViewInject(R.id.tv_bank_branch1)
    TextView tv_bank_branch1;
    @ViewInject(R.id.tv_bank_branch2)
    TextView tv_bank_branch2;
    //    @ViewInject(R.id.tv_bank_branch3)
//    TextView tv_bank_branch3;
    @ViewInject(R.id.ll_choice_branch)
    LinearLayout ll_choice_branch;
    @ViewInject(R.id.et_address)
    ClearEditText et_address;
    @ViewInject(R.id.et_user_name)
    EditText et_user_name;
    @ViewInject(R.id.et_bank_num)
    EditText et_bank_num;
    @ViewInject(R.id.et_bank_num2)
    EditText et_bank_num2;

    ArrayList<BranchListEntity.ListBean> branchList = new ArrayList();

    ArrayList<JBankModel.ListBean> bankList = new ArrayList<>();
    ArrayList<JRegionModel.ListBean> provinceList = new ArrayList<>();
    ArrayList<ArrayList<JRegionModel.ListBean.CitysBean>> cityList = new ArrayList<>();
    //    ArrayList<ArrayList<ArrayList<JRegionModel.ListBean.CitysBean.CountysBean>>> countyList = new ArrayList<>();
//    ArrayList<ArrayList<RegionModel.ListBean.CityBean>> cityList = new ArrayList<>();
//    ArrayList<ArrayList<ArrayList<RegionModel.ListBean.CityBean.CountysBean>>>  countyList = new ArrayList<>();
    private String provinceId, cityId, countyId, card_holder, card_bank, card_number, branch_bank, pro_name, city_name, county_name;

    /**
     * 开户支行名称str
     */
    public String branchStr;

    /**
     * 银行卡id
     */
    public String bankId;
    public BranchBankListAdapter branchBankListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        ViewUtils.inject(this);

        initView();
        initData();
        initListener();
    }

    private void initView() {
        bankId = getIntent().getStringExtra("bank_id");
        card_holder = getIntent().getStringExtra("card_holder");
        card_bank = getIntent().getStringExtra("card_bank");
        card_number = getIntent().getStringExtra("card_number");
        branch_bank = getIntent().getStringExtra("branch_bank");
        provinceId = getIntent().getStringExtra("pro_id");
        pro_name = getIntent().getStringExtra("pro_name");
        cityId = getIntent().getStringExtra("city_id");
        city_name = getIntent().getStringExtra("city_name");
        countyId = getIntent().getStringExtra("county_id");
        county_name = getIntent().getStringExtra("county_name");

        String name = null;
        if (card_bank != "") {
            name = "修改银行卡";
        } else {
            name = "绑定银行卡";
        }
        v3Title.setText(name);

        bankCardNumAddSpace(et_bank_num);
        bankCardNumAddSpace(et_bank_num2);

        tv4.setText(Html.fromHtml("<font color=\"#999999\">开户支行 </font><font color=\"#fe5062\">（*下拉列表未含有时可手动输入） </font>"));
        et_address.setSelection(et_address.getText().toString().trim().length());
        et_address.clearFocus();
        listView.setVisibility(View.GONE);
    }

    private void initData() {
        tv_bank_name.setText(card_bank);
        et_address.setText(branch_bank);
        et_user_name.setText(card_holder);
        if (!TextUtils.isEmpty(card_holder)) {
            et_user_name.setSelection(card_holder.length());
        }
        et_bank_num.setText(card_number);
        et_bank_num2.setText(card_number);
        tv_bank_branch1.setText(pro_name);
        tv_bank_branch2.setText(city_name);
//        tv_bank_branch3.setText(county_name);

        getBankList();
        getRegion();
    }

    private void initListener() {
        v3Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindCard();
            }
        });

        ll_choice_bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bankList == null || bankList.size() <= 0) {
                    showToast(R.string.try_again);
                    return;
                }
                showBankList();
            }
        });

        ll_choice_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (provinceList.size() <= 0 || cityList.size() <= 0) {
                    showToast(R.string.try_again);
                    return;
                }
                showArea();
            }
        });

        ll_choice_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                    showToast("请先选择开户银行和省市");
                    return;
                }
                showBranch();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_address.setText(branchList.get(position).branchName);
                listView.setVisibility(View.GONE);
                et_address.setSelection(et_address.getText().toString().trim().length());
            }
        });

        et_address.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                branchStr = et_address.getText().toString().trim();
                if (TextUtils.isEmpty(branchStr)) {
                    listView.setVisibility(View.GONE);
                } else {
                    if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                        Logger.i("走了这里-111-");
                        listView.setVisibility(View.GONE);
                    } else {
                        Logger.i("走了这里-333-");
                        showBranch();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                listView.setVisibility(View.GONE);
            }
        });

        et_address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    et_address.setSelection(et_address.getText().toString().trim().length());
                } else {
                    listView.setVisibility(View.GONE);
                }
            }
        });
    }

    private void getBankList() {
        startMyDialog(false);
        RetrofitUtils.getInstance(mContext).getBankList(new Callback<JBankModel>() {
            @Override
            public void onResponse(Call<JBankModel> call, Response<JBankModel> response) {
                stopMyDialog();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                    switch (response.body().code) {
                        case 1:
                            bankList.addAll(response.body().list);
                            break;
                        default:
                            showToast(response.body().msg);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JBankModel> call, Throwable t) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    private void getRegion() {
        startMyDialog();
        RetrofitUtils.getInstance(mContext).getRegion(new Callback<JRegionModel>() {
            @Override
            public void onResponse(Call<JRegionModel> call, Response<JRegionModel> response) {
                stopMyDialog();
                switch (response.body().code) {
                    case 1:
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            ArrayList<JRegionModel.ListBean> list = response.body().list;
                            for (int i = 0; i < list.size(); i++) {
                                provinceList.add(list.get(i));
                                cityList.add(list.get(i).citys);
//                                ArrayList<ArrayList<JRegionModel.ListBean.CitysBean.CountysBean>> countysBean = new ArrayList<ArrayList<JRegionModel.ListBean.CitysBean.CountysBean>>();
//                                for (int j = 0; j < list.get(i).citys.size(); j++) {
//                                    countysBean.add(list.get(i).citys.get(j).countys);
//                                }
//                                countyList.add(countysBean);
                            }
                            break;
                        }

                    default:
                        showToast(response.body().msg);
                        break;
                }
            }

            @Override
            public void onFailure(Call<JRegionModel> call, Throwable t) {
                showToast(R.string.net_work_not_available);
                stopMyDialog();
            }
        });
    }

    private void showBankList() {
        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                bankId = bankList.get(options1).bankId;
                tv_bank_name.setText(bankList.get(options1).bankName);
            }
        }).setTitleText("选择银行")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        optionsPickerView.setPicker(bankList);
        optionsPickerView.show();
    }

    private void showArea() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                JRegionModel.ListBean provinceBean = provinceList.get(options1);
                tv_bank_branch1.setText(provinceBean.getPickerViewText());
                provinceId = provinceBean.regionId;

                JRegionModel.ListBean.CitysBean citysBean = cityList.get(options1).get(options2);
                tv_bank_branch2.setText(citysBean.getPickerViewText());
                cityId = citysBean.regionId;

//                JRegionModel.ListBean.CitysBean.CountysBean countyBean = countyList.get(options1).get(options2).get(options3);
//                tv_bank_branch3.setText(countyBean.getPickerViewText());
//                countyId = countyBean.regionId;
            }
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK)
                .setContentTextSize(20)
                .setOutSideCancelable(false)
                .build();
        pvOptions.setPicker(provinceList, cityList);
        pvOptions.show();
    }

    private void showBranch() {
        Logger.i("ray-" + provinceId);
        Logger.i("ray-" + cityId);
        Logger.i("ray-" + bankId);
        branchStr = et_address.getText().toString().trim();
        if (TextUtils.isEmpty(branchStr)) {
            branchStr = "";
        }
        startMyDialog(false);
        RetrofitUtils2.getInstance(mContext).getBranchBankList(provinceId, cityId, bankId, branchStr, new Callback<BranchListEntity>() {
            @Override
            public void onResponse(Call<BranchListEntity> call, Response<BranchListEntity> response) {
                stopMyDialog();
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        switch (response.body().code) {
                            case 1:
                                branchList.clear();
                                ArrayList<BranchListEntity.ListBean> newList = response.body().list;
                                branchList.addAll(newList);
                                if (branchList.size() == 0) {
                                    branchList.add(0, new BranchListEntity.ListBean("暂无数据"));
                                }

                                if (branchBankListAdapter == null) {
                                    branchBankListAdapter = new BranchBankListAdapter(mContext, branchList);
                                    listView.setAdapter(branchBankListAdapter);
                                } else {
                                    branchBankListAdapter.notifyDataSetChanged();
                                }

                                Logger.i("走了这里-监听-" + branchStr);
                                Logger.i("走了这里-监听-" + branchList.toString());
                                if (useLoop(branchList, branchStr)) {
                                    Logger.i("走了这里-222-");
                                    listView.setVisibility(View.GONE);
                                } else {
                                    Logger.i("走了这里-444-");
                                    listView.setVisibility(View.VISIBLE);
                                }

                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<BranchListEntity> call, Throwable t) {
                stopMyDialog();
                listView.setVisibility(View.GONE);
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private static boolean useLoop(ArrayList<BranchListEntity.ListBean> list, String str) {
        for (BranchListEntity.ListBean s : list) {
            if (s.branchName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void bindCard() {
        String bankName = tv_bank_name.getText().toString().trim();
        String userName = et_user_name.getText().toString().trim();
        String bankNum = et_bank_num.getText().toString().trim();
        String bankNum2 = et_bank_num2.getText().toString().trim();
        bankNum = bankNum.replaceAll("\\s", "");
        bankNum2 = bankNum2.replaceAll("\\s", "");
        branchStr = et_address.getText().toString().trim();

        if (TextUtils.isEmpty(bankName) || TextUtils.isEmpty(userName) || TextUtils.isEmpty(bankNum)|| TextUtils.isEmpty(branchStr)) {
            showToast(R.string.please_complete_info_first);
            return;
        }

//        if (bankNum.length() < 15) {
//            showToast(R.string.please_write_right_num);
//            return;
//        }

        if (!bankNum.equals(bankNum2)) {
            showToast(R.string.twice_write_num_no_same);
            return;
        }

        Intent intent = new Intent(mContext, JBindCardCodeActivity.class);
        intent.putExtra("card_bank", bankName);
        intent.putExtra("card_holder", userName);
        intent.putExtra("card_number", bankNum);
        intent.putExtra("card_branch", branchStr);
        intent.putExtra("province_id", provinceId);
        intent.putExtra("city_id", cityId);
        intent.putExtra("county_id", countyId);
        startActivity(intent);
    }

    private void bankCardNumAddSpace(final EditText mEditText) {
        mEditText.addTextChangedListener(new TextWatcher() {
            int beforeTextLength = 0;
            int onTextLength = 0;
            boolean isChanged = false;

            int location = 0; //记录光标的位置
            private char[] tempChar;
            private StringBuffer buffer = new StringBuffer();
            int konggeNumberB = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextLength = s.length();
                if (buffer.length() > 0) {
                    buffer.delete(0, buffer.length());
                }
                konggeNumberB = 0;
                for (int i = 0; i < s.length(); i++) {
                    if (s.charAt(i) == ' ') {
                        konggeNumberB++;
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                onTextLength = s.length();
                buffer.append(s.toString());
                if (onTextLength == beforeTextLength || onTextLength <= 3
                        || isChanged) {
                    isChanged = false;
                    return;
                }
                isChanged = true;
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isChanged) {
                    location = mEditText.getSelectionEnd();
                    int index = 0;
                    while (index < buffer.length()) {
                        if (buffer.charAt(index) == ' ') {
                            buffer.deleteCharAt(index);
                        } else {
                            index++;
                        }
                    }

                    index = 0;
                    int konggeNumberC = 0;
                    while (index < buffer.length()) {
                        if ((index == 4 || index == 9 || index == 14 || index == 19)) {
                            buffer.insert(index, ' ');
                            konggeNumberC++;
                        }
                        index++;
                    }

                    if (konggeNumberC > konggeNumberB) {
                        location += (konggeNumberC - konggeNumberB);
                    }

                    tempChar = new char[buffer.length()];
                    buffer.getChars(0, buffer.length(), tempChar, 0);
                    String str = buffer.toString();
                    if (location > str.length()) {
                        location = str.length();
                    } else if (location < 0) {
                        location = 0;
                    }

                    mEditText.setText(str);
                    Editable etable = mEditText.getText();
                    Selection.setSelection(etable, location);
                    isChanged = false;
                }
            }
        });
    }
}
