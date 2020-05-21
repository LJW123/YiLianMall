package com.leshan.ylyj.view.activity.bankmodel;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.BranchBankListAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.RetrofitUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.MenuUtil;
import com.yilian.mylibrary.StopDialog;
import com.yilian.mylibrary.widget.ClearEditText;
import com.yilian.networkingmodule.entity.MerchantMyRevenueEntity;
import com.yilian.networkingmodule.entity.V3MoneyDetailWithDrawEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.BranchBankEntity;
import rxfamily.entity.JBankModel;
import rxfamily.entity.PublicCardDetailsEntityV2;

/**
 * @author zhaiyaohua
 *         重新提交银行卡信息页面
 */
public class ReCommitBankInfoActivity extends BaseActivity {

    public static final int PHOTO_ALBUM = 2;
    public String bankId;
    public BranchBankListAdapter branchBankListAdapter;

    ArrayList<BranchBankEntity.ListBean> branchList = new ArrayList();
    ArrayList<JBankModel.ListBean> bankList = new ArrayList<>();
    ArrayList<JRegionModel.ListBean> provinceList = new ArrayList<>();
    ArrayList<ArrayList<JRegionModel.ListBean.CitysBean>> cityList = new ArrayList<>();
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivRight;
    private ClearEditText etCompanyName;
    private TextView tvBankName;
    private TextView tvProAndCity;
    private ClearEditText etSubBankName;
    private ImageView ivSub;
    private ListView listView;
    private ClearEditText etBankUser;
    private Button okBtn;
    /**
     * 绑定时传0
     */
    private String provinceId, cityId;
    private BankPresenter presenter;
    private boolean hasBankUser, hasCompanyName, hasSub, hasBankName, hasPro, hasCity;
    //公司名称，税号，开户支行，账户，电话，地址，短信验证码，银行名称
    private String branchStr;
    private AppCompatCheckBox cbSynicBank;
    private int type;

    private V3MoneyDetailWithDrawEntity mEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommit_bank_info);
        StatusBarUtil.setColor(this, Color.WHITE);
        presenter = new BankPresenter(this, this);
        initView();
        initData();
        initListener();
        //设置默认选中
        cbSynicBank.setChecked(true);
        setBtnNextStepStatus();
    }


    @Override
    protected void initView() {
        Intent intent = getIntent();
        mEntity = intent.getParcelableExtra("bank_info");
        ivBack = findViewById(R.id.iv_back);
        tvTitle = findViewById(R.id.tv_title);
        tvTitle.setText("重新提交银行卡信息");
        ivRight = findViewById(R.id.iv_right);
        ivRight.setImageDrawable(getResources().getDrawable(R.mipmap.icon_setting));
        ivRight.setVisibility(View.VISIBLE);
        cbSynicBank = findViewById(R.id.cb_sync_bank);
        etCompanyName = findViewById(R.id.et_company_name);
        tvBankName = findViewById(R.id.tv_bank_name);
        tvProAndCity = findViewById(R.id.tv_pro_and_city);
        etSubBankName = findViewById(R.id.et_sub_bank_name);
        etBankUser = findViewById(R.id.et_bank_user);
        ivSub = findViewById(R.id.iv_sub_bank);
        listView = findViewById(R.id.listView);
        okBtn = findViewById(R.id.btn_next);
        okBtn.setText("确认保存");
        listView.setVisibility(View.GONE);
        etSubBankName.setText(mEntity.branchBank);
        tvBankName.setText(mEntity.cardBank);
        etBankUser.setText(mEntity.cardNum);
        etCompanyName.setText(mEntity.cardHolder);
        tvProAndCity.setText(mEntity.province_cn + " " + mEntity.cityCn);
        provinceId = mEntity.province;
        cityId = mEntity.city;
        bankId = mEntity.bankCode;
    }

    @Override
    protected void initListener() {
        cbSynicBank.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEntity.syncType = isChecked ? 1 : 0;

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ivRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MenuUtil.showMenu(ReCommitBankInfoActivity.this, R.id.iv_right);
            }
        });


        tvBankName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bankList.size() <= 0) {
                    getBankList(true);
                } else {
                    showBankList();
                }

            }
        });

        tvProAndCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (provinceList.size() <= 0 || cityList.size() <= 0) {
                    getRegion(true);
                } else {
                    showArea();
                }
            }
        });

        ivSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                    showToast("请先选择开户银行和省市");
                    return;
                }
                startMyDialog(false);
                presenter.getBranchBankNameList(provinceId, cityId, bankId, getBranchBankName());
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                etSubBankName.setText(branchList.get(position).branchName);
                listView.setVisibility(View.GONE);
                etSubBankName.setSelection(etSubBankName.getText().toString().trim().length());
            }
        });

        etSubBankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                branchStr = etSubBankName.getText().toString().trim();
                if (TextUtils.isEmpty(branchStr)) {
                    listView.setVisibility(View.GONE);
                } else {
                    if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                        Logger.i("走了这里-111-");
                        listView.setVisibility(View.GONE);
                    } else {
                        Logger.i("走了这里-333-");
                        startMyDialog(false);
                        presenter.getBranchBankNameList(provinceId, cityId, bankId, getBranchBankName());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                listView.setVisibility(View.GONE);
            }
        });

        etSubBankName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etSubBankName.setSelection(etSubBankName.getText().toString().trim().length());
                } else {
                    listView.setVisibility(View.GONE);
                }
            }
        });

        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCommitData();
                modifyExtractOrder();
            }
        });
        setTextChangedListener(etBankUser);
        setTextChangedListener(etCompanyName);
        setTextChangedListener(etSubBankName);

    }

    @Override
    protected void initData() {
        startMyDialog();
        getBankList(false);
        getRegion(false);
    }

    @Override
    public void onError(Throwable e) {
        stopMyDialog();
        EventBus.getDefault().post(new StopDialog());
        listView.setVisibility(View.GONE);
        showToast(e.getMessage());
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        if (baseEntity instanceof BranchBankEntity) {
            BranchBankEntity bean = (BranchBankEntity) baseEntity;
            branchList.clear();
            ArrayList<BranchBankEntity.ListBean> newList = bean.list;
            branchList.addAll(newList);
            if (branchList.size() == 0) {
                branchList.add(0, new BranchBankEntity.ListBean("暂无数据"));
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
        }
        stopMyDialog();
    }

    private static boolean useLoop(ArrayList<BranchBankEntity.ListBean> list, String str) {
        for (BranchBankEntity.ListBean s : list) {
            if (s.branchName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    private void getBankList(final boolean show) {
        if (show) {
            startMyDialog();
        }
        RetrofitUtils.getInstance(mContext).getBankList(new Callback<JBankModel>() {
            @Override
            public void onResponse(Call<JBankModel> call, Response<JBankModel> response) {
                stopMyDialog();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                    switch (response.body().code) {
                        case 1:
                            bankList.clear();
                            bankList.addAll(response.body().list);
                            if (show) {
                                showBankList();
                            }
                            break;
                        default:
                            showToast(response.body().msg);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(Call<JBankModel> call, Throwable t) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void getRegion(final boolean show) {
        if (show) {
            startMyDialog();
        }
        RetrofitUtils.getInstance(mContext).getRegion(new Callback<JRegionModel>() {
            @Override
            public void onResponse(Call<JRegionModel> call, Response<JRegionModel> response) {
                stopMyDialog();
                switch (response.body().code) {
                    case 1:
                        if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                            ArrayList<JRegionModel.ListBean> list = response.body().list;
                            provinceList.clear();
                            cityList.clear();
                            for (int i = 0; i < list.size(); i++) {
                                provinceList.add(list.get(i));
                                cityList.add(list.get(i).citys);
                            }
                            if (show) {
                                showArea();
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
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    private void setBtnNextStepStatus() {
        if (!TextUtils.isEmpty(etBankUser.getText().toString().trim())) {
            hasBankUser = true;
        } else {
            hasBankUser = false;
        }

        if (!TextUtils.isEmpty(etCompanyName.getText().toString().trim())) {
            hasCompanyName = true;
        } else {
            hasCompanyName = false;
        }


        if (!TextUtils.isEmpty(etSubBankName.getText().toString().trim())) {
            hasSub = true;
        } else {
            hasSub = false;
        }
        if (!TextUtils.isEmpty(tvBankName.getText().toString())) {
            hasBankName = true;
        } else {
            hasBankName = false;
        }

        if (!TextUtils.isEmpty(provinceId)) {
            hasPro = true;
        } else {
            hasPro = false;
        }

        if (!TextUtils.isEmpty(cityId)) {
            hasCity = true;
        } else {
            hasCity = false;
        }
        if (hasSub && hasCompanyName && hasBankUser && hasBankName && hasPro && hasCity) {
            okBtn.setBackgroundResource(R.drawable.bg_new_solid_red);
            okBtn.setClickable(true);
        } else {
            okBtn.setBackgroundResource(R.drawable.bg_solid_ccc);
            okBtn.setClickable(false);
        }
    }

    /**
     * 选择开户银行，开户支行置空
     */
    private void showBankList() {
        OptionsPickerView optionsPickerView = new OptionsPickerView.Builder(mContext, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                bankId = bankList.get(options1).bankId;
                mEntity.bankCode = bankId;
                tvBankName.setText(bankList.get(options1).bankName);
                //开户支行置空
                etSubBankName.setText("");
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
                provinceId = provinceBean.regionId;
                JRegionModel.ListBean.CitysBean citysBean = cityList.get(options1).get(options2);
                tvProAndCity.setText(provinceBean.getPickerViewText() + "  " + citysBean.getPickerViewText());
                cityId = citysBean.regionId;
                mEntity.province = provinceId;
                mEntity.city = cityId;
                //开户支行置空
                etSubBankName.setText("");
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

    /**
     * 获取输入的支行名称
     *
     * @return
     */
    String getBranchBankName() {
        return etSubBankName.getText().toString().trim();
    }

    /**
     * 设置提交数据
     */
    private void setCommitData() {
        if (mEntity != null) {
            mEntity.branchBank = getBranchBankName();
            mEntity.cardBank = tvBankName.getText().toString();
            mEntity.cardNum = etBankUser.getText().toString().trim();
            mEntity.cardHolder = etCompanyName.getText().toString().trim();
        }
    }

    /**
     * 修改提现失败订单
     */
    @SuppressWarnings("unchecked")
    private void modifyExtractOrder() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                modifyExtractOrder("modify_extract_order", mEntity.extractOrder,
                        mEntity.cardNum, mEntity.cardBank,
                        mEntity.branchBank, mEntity.cardHolder,
                        mEntity.bankCode, mEntity.province,
                        mEntity.city, mEntity.syncType)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
                    @Override
                    public void onCompleted() {
                        stopMyDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        stopMyDialog();

                    }

                    @Override
                    public void onNext(HttpResultBean bean) {
                        showToast(bean.msg);
                        /**
                         * 刷新我的营收页面
                         * {@link com.yilianmall.merchant.activity.MerchantMyRevenueActivity#updateRevenueInfo(MerchantMyRevenueEntity entity)}
                         */
                        EventBus.getDefault().post(new MerchantMyRevenueEntity());
                        /**
                         * 刷新提取营收页面银行卡信息
                         * {@link com.yilianmall.merchant.activity.MerchantExtractRevenueActivity#updateCarInfo(PublicCardDetailsEntityV2 entity)}
                         */
                        EventBus.getDefault().post(new PublicCardDetailsEntityV2());
                        setResult(RESULT_OK);
                        finish();
                    }
                });
        addSubscription(subscription);
    }

    private void setTextChangedListener(EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                setBtnNextStepStatus();
            }
        });
    }
}
