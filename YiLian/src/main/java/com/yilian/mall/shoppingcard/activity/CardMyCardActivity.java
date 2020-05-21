package com.yilian.mall.shoppingcard.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseAppCompatActivity;
import com.yilian.mall.R;
import com.yilian.mall.shoppingcard.CardAddressDataSourceImpl;
import com.yilian.mall.shoppingcard.utils.CheckApkExistUtil;
import com.yilian.mall.shoppingcard.utils.JumpCardActivityUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RxUtil;
import com.yilian.mylibrary.ToastUtil;
import com.yilian.mylibrary.utils.VaryViewUtils;
import com.yilian.mylibrary.widget.jdaddressseletor.AddressSelector;
import com.yilian.mylibrary.widget.jdaddressseletor.BottomDialog;
import com.yilian.mylibrary.widget.jdaddressseletor.OnAddressSelectedListener;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCity;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDCounty;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDProvince;
import com.yilian.mylibrary.widget.jdaddressseletor.areamodel.JDStreet;
import com.yilian.networkingmodule.entity.shoppingcard.MyCardInfoEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import io.reactivex.functions.Consumer;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rxfamily.utils.StringUtils;

/**
 * 作者：马铁超 on 2018/11/14 19:26
 * 我的购物卡 已开卡页和未开卡页面
 */

public class CardMyCardActivity extends BaseAppCompatActivity implements AddressSelector.onSelectorAreaPositionListener, OnAddressSelectedListener, AddressSelector.OnDialogCloseListener {
    public static final String TAG_CARD_NUMBER = "cardNumber";
    /**
     * IS_OPENED = 2购物卡已开
     */
    public static final int IS_OPENED = 2;
    /**
     * 请求成功
     */
    public static final int REQUEST_OK = 1;
    /**
     * 开卡
     */
    public static final int OPPEN_CARD_CODE = 1;
    /**
     * 充值
     */
    public static final int RECHARGE_CODE = 0;
    /**
     * 1第一次打开购物卡
     * 2不是第一次打开
     */
    public static final String ISFIRST_OPPEN = "1";
    /**
     * 乐淘天使 包名
     */
    public static final String LETAOANGLE_PACKAGENAME = "letaoangle.com.letaoangle";

    private TextView v3Title, tv_oppcard_address, tvCardCreateat, tvBalance, tvDialogAddress, tvRefresh;
    private ImageView v3Back, v3Shop, ivQuestion, ivOppencard;
    private RelativeLayout rl_content3, rlCardDetail, rlCardTopup;
    private BottomDialog selectorDialog;
    private String provinceName;
    private String provinceId, packgeName, className;
    private String cityName;
    private String cityId;
    private String countyName;
    private String countyId;
    private String townName = "";
    private String townId = "0", addressName = "", mCardNumber = "";
    private PopupWindow mChangeAddressPop;
    private PopupWindow mButtomPopu;
    private String cardId;
    private TextView tvCardUsername;
    private TextView tvCurrentMonthLimit;
    private TextView tvCardCurrentLimit;
    private LinearLayout llLimitContent;
    private TextView tvValidfrom;
    private TextView tvCardNumber;
    private RelativeLayout flCardBg, rlCardContentview;
    private CardView cardview;
    private View includeCardNotoppen, includeCardIsoppen, errorView;
    private TextView tvOppcardAddress;
    private RelativeLayout rlContent3, rl_card_contentview;
    private VaryViewUtils varyViewUtils;
    private boolean isRefresh = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mycard_contentview);
        initView();
        initData();
        initListener();
    }

    @Override
    public void onDestroy() {
        hidePop();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isRefresh) {
            checkShopCardRel();
            isRefresh = false;
        }
    }

    public boolean hidePop() {
        if (mChangeAddressPop != null && mChangeAddressPop.isShowing()) {
            mChangeAddressPop.dismiss();
            return true;
        }
        if (mButtomPopu != null && mButtomPopu.isShowing()) {
            mButtomPopu.dismiss();
            return true;
        }
        return false;
    }

    /**
     * 判断当前登陆用户是否关联购物卡
     */
    private void checkShopCardRel() {
        if (TextUtils.isEmpty(mCardNumber)) {
//            未开卡
            includeCardNotoppen.setVisibility(View.VISIBLE);
            includeCardIsoppen.setVisibility(View.GONE);
            ivQuestion.setVisibility(View.GONE);
            varyViewUtils = new VaryViewUtils(mContext);
            varyViewUtils.setVaryViewHelper(R.id.rl_card_contentview, getWindow().getDecorView(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            varyViewUtils.showDataView();
        } else {
//            已开卡
            includeCardIsoppen.setVisibility(View.VISIBLE);
            includeCardNotoppen.setVisibility(View.GONE);
            ivQuestion.setVisibility(View.VISIBLE);
            varyViewUtils = new VaryViewUtils(mContext);
            varyViewUtils.setVaryViewHelper(R.id.rl_card_contentview, getWindow().getDecorView(), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            requestCardInfo(mCardNumber);
        }
    }

    private void initData() {
        mCardNumber = getIntent().getStringExtra("cardNumber");
    }

    /**
     * 获取购物卡信息
     *
     * @param cardNumber
     */
    private void requestCardInfo(String cardNumber) {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .getCardInfo("card/getHplCardInfo", cardNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyCardInfoEntity>() {
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
                    public void onNext(MyCardInfoEntity cardInfoEntity) {
                        if (cardInfoEntity.code == 1) {
                            cardId = cardInfoEntity.data.card_id;
                            setCardInfoData(cardInfoEntity);
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 设置地址
     */
    @SuppressWarnings("unchecked")
    private void saveAddress() {
        startMyDialog();
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext)
                .saveAddress("card/set_shopcard_address", provinceId + "," + cityId + "," + countyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HttpResultBean>() {
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
                    public void onNext(HttpResultBean resultBean) {
                        if (resultBean.code == REQUEST_OK) {
                            hidePop();
                            ToastUtil.showMessage(mContext, "设置成功！");
                            tv_oppcard_address.setText(addressName);
                        }
                    }
                });
        addSubscription(subscription);
    }

    /**
     * 购物卡信息
     *
     * @param cardInfoEntity
     */
    private void setCardInfoData(MyCardInfoEntity cardInfoEntity) {
        // 左上角昵称
        if (!StringUtils.isEmpty(cardInfoEntity.data.name)) {
            tvCardUsername.setText(cardInfoEntity.data.name);
        }
        //开卡时间
        if (!StringUtils.isEmpty(cardInfoEntity.data.created_at)) {
            tvCardCreateat.setText(cardInfoEntity.data.created_at);
        }
        //总金额
        if (!StringUtils.isEmpty(cardInfoEntity.data.balance)) {
            tvBalance.setText(cardInfoEntity.data.balance);
        }
        // 可用金额
        if (!StringUtils.isEmpty(cardInfoEntity.data.limit)) {
            tvCardCurrentLimit.setText("¥  " + cardInfoEntity.data.limit);
        }

        // 卡号
        if (!StringUtils.isEmpty(cardInfoEntity.data.card_id)) {
            tvCardNumber.setText("NO." + cardInfoEntity.data.card_id);
        }

        // 设置的地址
        if (!StringUtils.isEmpty(cardInfoEntity.data.address)) {
            tv_oppcard_address.setText(cardInfoEntity.data.address);
        }

        if (!StringUtils.isEmpty(cardInfoEntity.data.isAddress)) {
            if (cardInfoEntity.data.isAddress.equals(ISFIRST_OPPEN)) {
                showChangeAddressPop();
            }
        }
        varyViewUtils.showDataView();
    }

    private void initView() {
        tvRefresh = findViewById(R.id.tv_refresh);
        errorView = findViewById(R.id.include_errorview);
        rl_card_contentview = findViewById(R.id.rl_card_contentview);
        includeCardIsoppen = findViewById(R.id.include_card_isoppen);
        includeCardNotoppen = findViewById(R.id.include_card_notoppen);
        ivQuestion = findViewById(R.id.iv_question);
        rlCardTopup = findViewById(R.id.rl_card_topup);
        rlCardDetail = findViewById(R.id.rl_card_detail);
        tv_oppcard_address = findViewById(R.id.tv_oppcard_address);
        v3Title = findViewById(R.id.v3Title);
        v3Back = findViewById(R.id.v3Back);
        rl_content3 = findViewById(R.id.rl_content3);
        v3Title.setText("我的购物卡");
        tvCardUsername = (TextView) findViewById(R.id.tv_card_username);
        tvCurrentMonthLimit = (TextView) findViewById(R.id.tv_current_month_limit);
        tvCardCurrentLimit = (TextView) findViewById(R.id.tv_card_current_limit);
        llLimitContent = (LinearLayout) findViewById(R.id.ll_limit_content);
        tvValidfrom = (TextView) findViewById(R.id.tv_validfrom);
        tvCardNumber = (TextView) findViewById(R.id.tv_card_number);
        flCardBg = (RelativeLayout) findViewById(R.id.fl_card_bg);
        cardview = (CardView) findViewById(R.id.cardview);
        tvOppcardAddress = (TextView) findViewById(R.id.tv_oppcard_address);
        rlContent3 = (RelativeLayout) findViewById(R.id.rl_content3);
        tvCardCreateat = findViewById(R.id.tv_card_createat);
        tvBalance = findViewById(R.id.tv_balance);
        ivOppencard = findViewById(R.id.iv_oppencard);
    }

    private void initListener() {
        if (rlCardDetail != null) {
            // 查看明细
            RxUtil.clicks(rlCardDetail, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    JumpCardActivityUtils.toCardDetailActivity(mContext);
                }
            });
        }

        if (v3Back != null) {
            // 返回
            RxUtil.clicks(v3Back, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    onBackPressed();
                }
            });
        }

        if (rlCardTopup != null) {
            //充值
            RxUtil.clicks(rlCardTopup, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    oppenButtomDialog(RECHARGE_CODE);
                    isRefresh = true;
                }
            });
        }
        if (ivQuestion != null) {
            //右上角问号点击
            RxUtil.clicks(ivQuestion, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    isRefresh = false;
                }
            });
        }

        if (ivOppencard != null) {
            //立即开卡
            RxUtil.clicks(ivOppencard, new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    oppenButtomDialog(OPPEN_CARD_CODE);
                }
            });
        }

    }

    /**
     * 选择省市县镇
     */
    public void chooseAddress() {
        selectorDialog = new BottomDialog(this, new CardAddressDataSourceImpl());
        selectorDialog.setCanceledOnTouchOutside(false);
//        selectorDialog.setCancelable(false);
        selectorDialog.setOnAddressSelectedListener(this);
        selectorDialog.setDialogDismisListener(this);
        //设置字体的大小
        selectorDialog.setTextSize(14);
        //设置指示器的颜色
        selectorDialog.setIndicatorBackgroundColor(R.color.color_red);
        //设置字体获得焦点的颜色
        selectorDialog.setTextSelectedColor(R.color.color_red);
        selectorDialog.setSelectorAreaPositionListener(this);
        //取消显示取消按钮
        selectorDialog.setCancelImgShow(false);
        selectorDialog.setSelectorTitle("选择地址");
        //设置返回按钮显示
        selectorDialog.setSelectorBackButttomShow(true);
        selectorDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (selectorDialog != null && selectorDialog.isShowing()) {
                        dialogclose();
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
        selectorDialog.show();
    }

    @Override
    public void onAddressSelected(JDProvince province, JDCity city, JDCounty county, JDStreet street) {
        addressName = "";
        StringBuilder addressBuild = new StringBuilder();
        if (province != null) {
            if (!TextUtils.isEmpty(province.name)) {
                addressBuild.append(province.name);
                provinceName = province.name;
                provinceId = province.id;
            }
        }
        if (city != null) {
            if (!TextUtils.isEmpty(city.name)) {
                addressBuild.append(city.name);
                cityName = city.name;
                cityId = city.id;
            }
        }
        if (county != null) {
            if (!TextUtils.isEmpty(county.name)) {
                addressBuild.append(county.name);
                countyName = county.name;
                countyId = county.id;
            }
        }
        if (street != null) {
            if (!TextUtils.isEmpty(street.name)) {
                addressBuild.append(street.name);
                townName = street.name;
                townId = street.id;
            }
        } else {
            //当选则的是 三级地址时 四级置空
            townName = "";
            townId = "0";
        }
        addressName = addressBuild.toString();
        dialogclose();
        showChangeAddressPop();
        tvDialogAddress.setText(addressName);
    }

    @Override
    public void dialogclose() {
        if (selectorDialog != null) {
            selectorDialog.dismiss();
        }
        if (StringUtils.isEmpty(addressName)) {
            showChangeAddressPop();
        }
        isRefresh = false;
    }

    @Override
    public void selectorAreaPosition(int provincePosition, int cityPosition, int countyPosition, int streetPosition) {
        Logger.i("选取的地址:province: " + provincePosition + "  city:" + cityPosition + "  county:" + countyPosition + "  street:" + streetPosition);
    }

    // 修改地址弹出框
    private void showChangeAddressPop() {
        if (mChangeAddressPop == null) {
            View contentView = View.inflate(mContext, R.layout.layout_pop_card_select_area, null);
            tvDialogAddress = contentView.findViewById(R.id.tv_address);
            mChangeAddressPop = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mChangeAddressPop.setContentView(contentView);
            mChangeAddressPop.setBackgroundDrawable(new ColorDrawable());
            mChangeAddressPop.setOutsideTouchable(true);
            // 点击弹窗外区域隐藏 popu
           /* RxUtil.clicks(contentView.findViewById(R.id.fl_root_view), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                }
            });*/
            //修改收货地址
            RxUtil.clicks(contentView.findViewById(R.id.ll_change_address), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                    chooseAddress();
                }
            });
            // 保存地址
            RxUtil.clicks(contentView.findViewById(R.id.tv_confirm), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    if (!StringUtils.isEmpty(provinceId) && !StringUtils.isEmpty(cityId) && !StringUtils.isEmpty(countyId)) {
                        saveAddress();
                    } else {
                        ToastUtil.showMessage(mContext, "请选择地址！");
                    }
                }
            });
        }
        mChangeAddressPop.showAtLocation(this.getWindow().getDecorView(), Gravity.CENTER, 0, 0);
    }

    /**
     * 充值/开卡
     */
    private void oppenButtomDialog(int dialogType) {

        if (mButtomPopu == null) {
            View contentView = View.inflate(mContext, R.layout.dailog_gootherapp, null);
            mChangeAddressPop = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mChangeAddressPop.setContentView(contentView);
            mChangeAddressPop.setBackgroundDrawable(new ColorDrawable());
            mChangeAddressPop.setOutsideTouchable(true);
            RxUtil.clicks(contentView.findViewById(R.id.tv_dialog_cancel), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                }
            });

            RxUtil.clicks(contentView.findViewById(R.id.tv_dialog_confirm), new Consumer() {
                @Override
                public void accept(Object o) throws Exception {
                    hidePop();
                    if (CheckApkExistUtil.isApplicationAvilible(mContext, LETAOANGLE_PACKAGENAME)) {
                        isRefresh = true;
                        if (dialogType == RECHARGE_CODE) {
                            packgeName = LETAOANGLE_PACKAGENAME;
                            className = "letaoangle.com.letaoangle.phaseone.activity.shoppingcard.RechargeShoppingCardActivity";
                            JumpCardActivityUtils.toOtherAppActivity(CardMyCardActivity.this, packgeName, className);
                        }
                        if (dialogType == OPPEN_CARD_CODE) {
                            packgeName = LETAOANGLE_PACKAGENAME;
                            className = "letaoangle.com.letaoangle.phaseone.activity.shoppingcard.OpenShoppingCardActivity";
                            JumpCardActivityUtils.toOtherAppActivity(CardMyCardActivity.this, packgeName, className);
                            //                           刷新个人中心数据
                            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
//                            跳转开卡关闭该页面，回到个人中心，保证数据的刷新
                            finish();
                        }
                    } else {
                        //跳转网页下载
                        Uri uri = Uri.parse("http://angelpool.jinleyuanmall.cn/share/index");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });
        }
        mChangeAddressPop.showAtLocation(this.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void onBackPressed() {
        if (selectorDialog != null && selectorDialog.isShowing()) {
            dialogclose();
        }
        super.onBackPressed();

    }
}
