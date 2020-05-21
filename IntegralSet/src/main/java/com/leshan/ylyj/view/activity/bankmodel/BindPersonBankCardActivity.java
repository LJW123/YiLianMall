package com.leshan.ylyj.view.activity.bankmodel;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaeger.library.StatusBarUtil;
import com.leshan.ylyj.adapter.BranchBankListAdapter;
import com.leshan.ylyj.basemodel.BaseActivity;
import com.leshan.ylyj.customview.ScrollListView;
import com.leshan.ylyj.presenter.BankPresenter;
import com.leshan.ylyj.testfor.R;
import com.leshan.ylyj.utils.SkipUtils;
import com.orhanobut.logger.Logger;
import com.vondear.rxtools.RxRegTool;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.BankCardEditTextWatcher;
import com.yilian.mylibrary.CodeException;
import com.yilian.mylibrary.IShowAreaUtil;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ShowAreaUtil;
import com.yilian.mylibrary.SmsCodeEntity;
import com.yilian.mylibrary.StopDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rxfamily.entity.AuthInfoEntity;
import rxfamily.entity.BankInfoEntity;
import rxfamily.entity.BaseEntity;
import rxfamily.entity.BindPersonBankCardSuccessEntity;
import rxfamily.entity.BranchBankEntity;
import rxfamily.entity.CheckBankCard4ElementEntity;
import rxfamily.entity.IntegralRegionEntity;

import static com.yilian.mylibrary.SmsCodeType.BIND_BANK_CARD;

/**
 * @author  绑定私人卡
 */
public class BindPersonBankCardActivity extends BaseActivity implements View.OnClickListener, IShowAreaUtil {

    public static final int BIND_BANK_CARD_FLAG = 0;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private ImageView v3Back;
    private FrameLayout v3Layout;
    private LinearLayout llTitle;
    private TextView tvTitle;
    private EditText etContent;
    private ImageView ivRightIcon;
    private LinearLayout llBankCardInfo;
    private Button btnBind;
    private ImageView ivRightIconBankPhone;
    private EditText etContentBankPhone;
    private ImageView ivRightIconBankCardNumber;
    private EditText etContentBankName;
    private EditText etContentBankCardNumber;
    private BankPresenter bankPresenter;
    private EditText etContentBankCity;
    private ArrayList<IntegralRegionEntity.ProvincesBean> provinceList;
    private ArrayList<ArrayList<IntegralRegionEntity.ProvincesBean.CitysBean>> cityList;
    private ArrayList<ArrayList<ArrayList<IntegralRegionEntity.ProvincesBean.CitysBean.CountysBean>>> countyList;
    private ImageView ivRightIconBankCity;
    private String provinceId;
    private String cityId;
    private ImageView ivRightIconBranchBankName;
    private ScrollListView scrollListView;
    private BranchBankListAdapter branchBankListAdapter;
    private EditText etContentBranchBankName;
    /**
     * 银行卡所属银行ID
     */
    private String bankId;
    /**
     * 银行卡对应银行客服电话
     */
    private String servicePhone;
    private BankPresenter bindBankCardPresenter;
    /**
     * 实名认证姓名
     */
    private String authName;
    /**
     * 老卡跳转过来时传递的银行卡号 用于重新绑定
     */
    private String bankCardNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_person_bank_card);
        bankPresenter = new BankPresenter(this, this);
        bindBankCardPresenter = new BankPresenter(mContext, this, BIND_BANK_CARD_FLAG);
        bankCardNumber = getIntent().getStringExtra("bank_card_number");
        initView();
        initListener();
//        必须放在initListener之后，否则无法监听银行卡号输入，不能触发查询银行卡号信息
        if (!TextUtils.isEmpty(bankCardNumber)) {
            etContentBankCardNumber.setText(bankCardNumber);
        }
        initData();
        StatusBarUtil.setColor(this, Color.WHITE);
    }

    @Override
    protected void initView() {
        scrollListView = (ScrollListView) findViewById(R.id.mistView);

        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Left.setOnClickListener(this);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setOnClickListener(this);
        v3Title.setText("添加银行卡");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setOnClickListener(this);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setOnClickListener(this);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setOnClickListener(this);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setOnClickListener(this);
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setOnClickListener(this);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setOnClickListener(this);
        llTitle = (LinearLayout) findViewById(R.id.ll_title);
        llTitle.setOnClickListener(this);
//银行卡号
        View includeBankCardNumber = findViewById(R.id.include_bank_card_number);
        TextView tvTitleBankCardNumber = (TextView) includeBankCardNumber.findViewById(R.id.tv_title);
        tvTitleBankCardNumber.setText("银行卡号");
        etContentBankCardNumber = (EditText) includeBankCardNumber.findViewById(R.id.et_content);
        etContentBankCardNumber.setInputType(InputType.TYPE_CLASS_NUMBER);
        etContentBankCardNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(25)});
        ivRightIconBankCardNumber = (ImageView) includeBankCardNumber.findViewById(R.id.iv_right_icon);
        ivRightIconBankCardNumber.setImageResource(R.mipmap.integral_icon_ka);
//        发卡行名称
        View includeBankName = findViewById(R.id.include_bank_name);
        TextView tvTitleBankName = (TextView) includeBankName.findViewById(R.id.tv_title);
        tvTitleBankName.setText("发卡型/发卡行");
        etContentBankName = (EditText) includeBankName.findViewById(R.id.et_content);
        etContentBankName.setFocusable(false);
        ImageView ivRightIconBankName = (ImageView) includeBankName.findViewById(R.id.iv_right_icon);
        ivRightIconBankName.setVisibility(View.INVISIBLE);
//       开户省市
        View includeBankCity = findViewById(R.id.include_bank_city);
        TextView tvTitleBankCity = (TextView) includeBankCity.findViewById(R.id.tv_title);
        tvTitleBankCity.setText("开户省市");
        etContentBankCity = (EditText) includeBankCity.findViewById(R.id.et_content);
        etContentBankCity.setEnabled(false);
        ivRightIconBankCity = (ImageView) includeBankCity.findViewById(R.id.iv_right_icon);
        ivRightIconBankCity.setImageResource(R.mipmap.arrows_down_gray);
//    支行名称
        View includeBranchBankName = findViewById(R.id.include_branch_bank_name);
        TextView tvTitleBranchBankName = (TextView) includeBranchBankName.findViewById(R.id.tv_title);
        Spanned spanned = Html.fromHtml("开户支行<font color='#F22424'>（*下拉列表未含有时可手动输入）</font>");
        tvTitleBranchBankName.setText(spanned);
        etContentBranchBankName = (EditText) includeBranchBankName.findViewById(R.id.et_content);
        ivRightIconBranchBankName = (ImageView) includeBranchBankName.findViewById(R.id.iv_right_icon);
        ivRightIconBranchBankName.setImageResource(R.mipmap.arrows_down_gray);
//        银行卡预留手机号
        View includeBankPhone = findViewById(R.id.include_bank_phone);
        TextView tvTitleBankPhone = (TextView) includeBankPhone.findViewById(R.id.tv_title);
        tvTitleBankPhone.setText("银行卡预留手机号");
        etContentBankPhone = (EditText) includeBankPhone.findViewById(R.id.et_content);
        etContentBankPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        etContentBankPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        ivRightIconBankPhone = (ImageView) includeBankPhone.findViewById(R.id.iv_right_icon);
        ivRightIconBankPhone.setImageResource(R.mipmap.icon_chahao);


        llBankCardInfo = (LinearLayout) findViewById(R.id.ll_bank_card_info);
        llBankCardInfo.setOnClickListener(this);
        btnBind = (Button) findViewById(R.id.btn_bind);
    }

    private Handler handler = new Handler();

    /**
     * 检测银行卡信息
     */
    private Runnable delayRun = new Runnable() {

        @Override
        public void run() {
            //在这里调用服务器的接口，获取数据
            Logger.i("获取银行卡信息");
            bankPresenter.checkCard(getBankCardNumber());
        }
    };
    private Runnable branchBankRun = new Runnable() {

        @Override
        public void run() {
            bankPresenter.getBranchBankNameList(provinceId, cityId, bankId, getBranchBankName());
        }
    };

    /**
     * 获取短信验证码后的验证逻辑
     *
     * @param smsCodeEntity
     */
    @Subscribe
    public void checkInfoBySmsCode(SmsCodeEntity smsCodeEntity) {
        String smsCode = smsCodeEntity.smsCode;
        bindBankCardPresenter.bindPrivaceCard(getBankCardNumber(), getPhone(), provinceId, cityId, servicePhone, getBranchBankName(), smsCode);
    }

    private void setBtnStatus() {

    }

    @Override
    protected void initListener() {
//        每隔1秒检查信息是否完整，判断按钮能否点击
        Subscription subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (messageComplete()) {
                            Logger.i("按钮可点");
                            btnBind.setClickable(true);
                            btnBind.setBackgroundResource(R.drawable.integral_jp_bg_btn_red_all_radius1);
                        } else {
                            Logger.i("按钮不可点");
                            btnBind.setClickable(false);
                            btnBind.setBackgroundResource(R.drawable.integral_jp_bg_btn_gray_all_radius1);
                        }
                    }
                });
        addSubscription(subscription);
//        点击同意绑定按钮
        RxUtil.clicks(btnBind, 2, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                if (!messageComplete()) {
                    showToast("请填写完整信息");
                    return;
                }
                bindBankCardPresenter.checkFourState(getBankCardNumber(), getPhone());

            }
        });
//        支行名称输入框获取焦点监听
        etContentBranchBankName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    etContentBranchBankName.setSelection(getBranchBankName().length());

                } else {
                    scrollListView.setVisibility(View.GONE);
                }
            }
        });
//        支行名称列表条目点击
        scrollListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dismissInputMethod();
                etContentBranchBankName.setText(branchList.get(position).branchName);
                scrollListView.setVisibility(View.GONE);
                etContentBranchBankName.setSelection(getBranchBankName().length());
            }
        });
//        输入支行名称监听
        etContentBranchBankName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                scrollListView.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(getBranchBankName())) {
                    scrollListView.setVisibility(View.GONE);
                } else {
                    if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                        Logger.i("走了这里-111-");
                        scrollListView.setVisibility(View.GONE);
                    } else {
                        Logger.i("走了这里-333-");
//                        此处需要优化 每隔1S 请求一次
                        if (branchBankRun != null) {
                            //每次editText有变化的时候，则移除上次发出的延迟线程
                            handler.removeCallbacks(branchBankRun);
                        }
                        handler.postDelayed(branchBankRun, 1000);

                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
//        选择支行信息
        RxUtil.clicks(ivRightIconBranchBankName, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                dismissInputMethod();
                if (TextUtils.isEmpty(provinceId) || TextUtils.isEmpty(cityId) || TextUtils.isEmpty(bankId)) {
                    showToast("请选择开户省市");
                    return;
                }
                startMyDialog();
                bankPresenter.getBranchBankNameList(provinceId, cityId, bankId, getBranchBankName());
            }
        });
//        选择城市
        RxUtil.clicks(ivRightIconBankCity, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                dismissInputMethod();
                new ShowAreaUtil().showArea(provinceList, cityList, null, mContext, BindPersonBankCardActivity.this);
            }
        });
//        银行卡号
        etContentBankCardNumber.addTextChangedListener(new BankCardEditTextWatcher(etContentBankCardNumber) {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                super.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (delayRun != null) {
                    //每次editText有变化的时候，则移除上次发出的延迟线程
                    handler.removeCallbacks(delayRun);
                }
                if (RxRegTool.isBankCard(getBankCardNumber())) {
                    handler.postDelayed(delayRun, 1000);
                } else {
                    etContentBankName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
            }
        });
//        支持银行列表
        RxUtil.clicks(ivRightIconBankCardNumber, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                SkipUtils.toSupportTwoBankCardType(mContext);
            }
        });
//        清除手机号
        RxUtil.clicks(ivRightIconBankPhone, new Consumer() {
            @Override
            public void accept(Object o) throws Exception {
                etContentBankPhone.setText("");
            }
        });
    }

    private boolean messageComplete() {
        return !(TextUtils.isEmpty(getBankCardNumber()) || TextUtils.isEmpty(getBankName()) || TextUtils.isEmpty(getCityName()) || TextUtils.isEmpty(getBranchBankName()) || TextUtils.isEmpty(getPhone()));
    }

    /**
     * 获取银行卡号
     *
     * @return
     */
    private String getBankCardNumber() {
        String trim = etContentBankCardNumber.getText().toString().trim();
        if (trim.contains(" ")) {
            return trim.replace(" ", "");
        } else {
            return trim;
        }
    }

    @Override
    protected void initData() {
        startMyDialog(false);
        bankPresenter.getRegion();
        bankPresenter.getAuthInfo();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.tv_right) {
        } else if (i == R.id.tv_right2) {
        } else if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.btn_bind) {
        }
    }

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        showToast(e.getMessage());
        stopMyDialog();
    }

    @Override
    public void onNext(BaseEntity baseEntity) {
        super.onNext(baseEntity);
        if (baseEntity instanceof BankInfoEntity) {
//            获取银行卡信息成功
            setBankCardInfo((BankInfoEntity) baseEntity);
        } else if (baseEntity instanceof IntegralRegionEntity) {
//            获取省市县
            setAreaInfo((IntegralRegionEntity) baseEntity);
        } else if (baseEntity instanceof BranchBankEntity) {
//            支行信息获取成功
            setBranchBankInfo((BranchBankEntity) baseEntity);

        } else if (baseEntity instanceof CheckBankCard4ElementEntity) {
//            银行卡四元素校验成功
            startSmsCodeActivity();
        } else if (baseEntity instanceof BindPersonBankCardSuccessEntity) {
//            绑定私有卡成功
            setResult(RESULT_OK);
            EventBus.getDefault().post(new StopDialog());
            AppManager.getInstance().killTopActivity();
            finish();
        } else if (baseEntity instanceof AuthInfoEntity) {
            authName = ((AuthInfoEntity) baseEntity).info.cardName;
//            ((AuthInfoEntity) baseEntity).info.cardId
        }
    }

    private void startSmsCodeActivity() {
        SkipUtils.toSMSCheck(mContext, getPhone(), BIND_BANK_CARD);
        overridePendingTransition(R.anim.library_module_dialog_enter, android.R.anim.fade_out);
    }

    /**
     * 设置银行卡信息
     *
     * @param baseEntity
     */
    private void setBankCardInfo(BankInfoEntity baseEntity) {
        llBankCardInfo.setVisibility(View.VISIBLE);
        BankInfoEntity.InfoBean info = baseEntity.info;
        etContentBankName.setText(info.type + "/" + info.bank);
        bankId = info.bankId;
        servicePhone = info.servicePhone;
    }

    /**
     * 设置支行信息
     *
     * @param baseEntity
     */
    private void setBranchBankInfo(BranchBankEntity baseEntity) {
        List<BranchBankEntity.ListBean> branchBank = baseEntity.list;
        if (branchBank == null || branchBank.size() <= 0) {
            return;
        }
        branchList.clear();
        branchList.addAll(branchBank);
        if (branchList.size() <= 0) {
            branchList.add(0, new BranchBankEntity.ListBean("暂无数据"));
        }
        if (branchBankListAdapter == null) {
            branchBankListAdapter = new BranchBankListAdapter(mContext, branchList);
            scrollListView.setAdapter(branchBankListAdapter);
        } else {
            branchBankListAdapter.notifyDataSetChanged();
        }
        if (useLoop(branchList, getBranchBankName())) {
//                支行查询结果中包含输入支行名称，则支行列表隐藏
            scrollListView.setVisibility(View.GONE);
        } else {
//                支行查询结果中不包含输入支行名称，则支行列表显示
            scrollListView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置省市县信息
     *
     * @param baseEntity
     */
    private void setAreaInfo(IntegralRegionEntity baseEntity) {
        IntegralRegionEntity baseEntity1 = baseEntity;

        provinceList = new ArrayList<>();
        cityList = new ArrayList<>();
        countyList = new ArrayList<>();
        ArrayList<IntegralRegionEntity.ProvincesBean> list = baseEntity1.list;
        for (int i = 0, count = list.size(); i < count; i++) {
            IntegralRegionEntity.ProvincesBean provinceBean = list.get(i);
            //省份列表
            provinceList.add(provinceBean);
            //市列表
            cityList.add(provinceBean.citys);
            ArrayList<ArrayList<IntegralRegionEntity.ProvincesBean.CitysBean.CountysBean>> countysBeen = new ArrayList<>();
            for (int j = 0; j < provinceBean.citys.size(); j++) {
                countysBeen.add(provinceBean.citys.get(j).countys);
            }
//                    县级列表
            countyList.add(countysBeen);
        }
    }

    /**
     * 获取开户省市
     *
     * @return
     */
    String getCityName() {
        return etContentBankCity.getText().toString().trim();
    }

    /**
     * 获取输入的支行名称
     *
     * @return
     */
    String getBranchBankName() {
        return etContentBranchBankName.getText().toString().trim();
    }

    String getBankName() {
        return etContentBankName.getText().toString().trim();
    }

    /**
     * 获取手机号
     *
     * @return
     */
    String getPhone() {
        return etContentBankPhone.getText().toString().trim();
    }

    /**
     * 检测支行名称输入内容和网络获取的支行名称是否相同
     *
     * @param list
     * @param str
     * @return
     */
    private static boolean useLoop(ArrayList<BranchBankEntity.ListBean> list, String str) {
        for (BranchBankEntity.ListBean listBean : list) {

        }
        for (BranchBankEntity.ListBean s : list) {
            if (s.branchName.equals(str)) {
                return true;
            }
        }
        return false;
    }

    ArrayList<BranchBankEntity.ListBean> branchList = new ArrayList();

    @Override
    public void onErrors(int flag, Throwable e) {
        super.onErrors(flag, e);
        stopMyDialog();
        if (BIND_BANK_CARD_FLAG == flag) {
//            绑定失败
            if (e instanceof CodeException) {
                EventBus.getDefault().post(new StopDialog());
                if (-1 == ((CodeException) e).code) {
//                    绑定银行卡时验证码错误
                }
            }

        }
    }

    @Override
    public void onCompleted() {
        super.onCompleted();
        stopMyDialog();
    }

    @Override
    public void selectArea(Object o, Object t1, Object t2) {
        IntegralRegionEntity.ProvincesBean provincesBean = (IntegralRegionEntity.ProvincesBean) o;
        IntegralRegionEntity.ProvincesBean.CitysBean citysBean = (IntegralRegionEntity.ProvincesBean.CitysBean) t1;
        IntegralRegionEntity.ProvincesBean.CitysBean.CountysBean countysBean = (IntegralRegionEntity.ProvincesBean.CitysBean.CountysBean) t2;
        etContentBankCity.setText(provincesBean.regionName + "省" + citysBean.regionName + "市");
        provinceId = provincesBean.regionId;
        cityId = citysBean.regionId;
    }
}
