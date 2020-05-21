package com.yilian.mall.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.loginmodule.V2_RegisterActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MineFunctionAdapter;
import com.yilian.mall.adapter.MineMallAdapter;
import com.yilian.mall.ctrip.activity.CtripHomePageActivity;
import com.yilian.mall.ctrip.util.JumpCtripActivityUtils;
import com.yilian.mall.ui.InformationActivity;
import com.yilian.mall.ui.LoadingActivity;
import com.yilian.mall.ui.LoadingShopCarActivity;
import com.yilian.mall.ui.MyMallOrderListActivity2;
import com.yilian.mall.ui.SettingActivity;
import com.yilian.mall.ui.UserInfoActivity;
import com.yilian.mall.ui.V3MoneyActivity;
import com.yilian.mall.ui.WebViewActivity;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mall.utils.JumpYlActivityUtils;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.widgets.NoScrollGridView;
import com.yilian.mall.widgets.pulllayout.DropZoomScrollView;
import com.yilian.mall.widgets.pulllayout.RayScrollViewListener;
import com.yilian.mylibrary.AppManager;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.GlideUtil;
import com.yilian.mylibrary.JumpToOtherPage;
import com.yilian.mylibrary.MoneyUtil;
import com.yilian.mylibrary.ScreenUtils;
import com.yilian.mylibrary.StatusBarUtils;
import com.yilian.mylibrary.ThreadUtil;
import com.yilian.networkingmodule.entity.MallBeanEntity;
import com.yilian.networkingmodule.entity.MineIconsEntity;
import com.yilian.networkingmodule.entity.MineMallOrderEntity;
import com.yilian.networkingmodule.httpresult.HttpResultBean;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils2;
import com.yilianmall.merchant.activity.MerchantAuditActivity;
import com.yilianmall.merchant.activity.MerchantCenterActivity;
import com.yilianmall.merchant.activity.MerchantCertificateActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 新版个人中心
 * 啦啦啦
 *
 * @author Ray_L_Pain
 * @date 2017/12/3 0003
 */

public class MineFragment extends JPBaseFragment implements View.OnClickListener {
    public View rootView;

    @ViewInject(R.id.layout_title)
    RelativeLayout titleLayout;
    @ViewInject(R.id.iv_setting)
    ImageView ivSetting;
    @ViewInject(R.id.tv_title)
    TextView tvTitle;
    @ViewInject(R.id.iv_message)
    ImageView ivMessage;
    @ViewInject(R.id.iv_dot)
    ImageView ivDot;

    @ViewInject(R.id.scroll_view)
    DropZoomScrollView scrollView;
    @ViewInject(R.id.ll_whole)
    LinearLayout llWhole;
    @ViewInject(R.id.ll_top)
    LinearLayout topLayout;
    @ViewInject(R.id.iv_zoom)
    ImageView ivTopBg;
    @ViewInject(R.id.view_holder)
    View holderView;

    @ViewInject(R.id.layout_register)
    LinearLayout registerLayout;
    @ViewInject(R.id.btn_login_by_sms_code)
    Button btnLoginByCode;
    @ViewInject(R.id.btn_login_by_pwd)
    Button btnLoginByPwd;

    @ViewInject(R.id.ll_red)
    LinearLayout redLayout;
    @ViewInject(R.id.tv_red)
    TextView tvRed;

    @ViewInject(R.id.ll_ledou)
    LinearLayout ledouLayout;
    @ViewInject(R.id.tv_ledou)
    TextView tvLedou;

    @ViewInject(R.id.ll_integral)
    LinearLayout integralLayout;
    @ViewInject(R.id.tv_integral)
    TextView tvIntegral;

    @ViewInject(R.id.ll_dai_gou)
    LinearLayout llDaiGou;
    @ViewInject(R.id.tv_dai_gou)
    TextView tvDaiGou;


    @ViewInject(R.id.layout_login)
    LinearLayout loginLayout;
    @ViewInject(R.id.iv_login_photo)
    ImageView ivLoginPhoto;
    @ViewInject(R.id.tv_level)
    TextView tvLevel;
    @ViewInject(R.id.tv_login_name)
    TextView tvLoginName;

    @ViewInject(R.id.layout_play_center)
    LinearLayout gameLayout;
    @ViewInject(R.id.iv_game_icon)
    ImageView ivGameIcon;
    @ViewInject(R.id.tv_game_name)
    TextView tvGameName;
    @ViewInject(R.id.tv_game_desc)
    TextView tvGameDesc;

    @ViewInject(R.id.layout_merchant)
    LinearLayout merchantLayout;
    @ViewInject(R.id.iv_ltb_icon)
    ImageView ivMerchantIcon;
    @ViewInject(R.id.tv_letao)
    TextView tvMerchantName;
    @ViewInject(R.id.tv_ltb_desc)
    TextView tvMerchantDesc;

    @ViewInject(R.id.ll_all_order)
    LinearLayout allOrderLayout;
    @ViewInject(R.id.grid_view_mall)
    NoScrollGridView mallGridView;
    @ViewInject(R.id.ll_all_function)
    LinearLayout functionLayout;
    @ViewInject(R.id.tv_function_look)
    TextView tvFunctionLook;
    @ViewInject(R.id.grid_view_function)
    NoScrollGridView functionGridView;
    int distance = 0;
    private MineMallAdapter mallAdapter;
    private MineFunctionAdapter functionAdapter;
    private int screenWidth;
    private String lookedAll;
    private boolean isFirst = true;
    private boolean isRefresh = false;
    private boolean isShowLoading = false;
    private long startTime;
    private ArrayList<MineIconsEntity.IconsBean> manifestList = new ArrayList<>();

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            Logger.i("2017年12月5日 15:19:47-走了setUserVisibleHint");
            initData();
            isFirst = false;
        }
    }

    private void initData() {
        initIcons();
        initCorner();
    }

    /**
     * 个人中心icons的显示
     */
    private void initIcons() {
        if (isFirst) {
            Logger.i("2017年12月5日 15:19:47-走了setUserVisibleHint---return");
            return;
        }
        startTime = System.currentTimeMillis();
        RetrofitUtils2.getInstance(mContext).mineIconsMessage(new Callback<MineIconsEntity>() {
            @Override
            public void onResponse(Call<MineIconsEntity> call, Response<MineIconsEntity> response) {
                HttpResultBean body = response.body();
                if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                    if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                        MineIconsEntity entity = response.body();
                        switch (entity.code) {
                            case 1:
                                timeOut(System.currentTimeMillis());

                                if ("1".equals(entity.loginStatus)) {
                                    com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, true, mContext);
                                } else {
                                    com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.SPKEY_STATE, false, mContext);
                                }

                                /**
                                 * 2018-1-11 增加用户是否认证字段 用于提现
                                 */
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.IS_CERT, entity.userInfo.isCert, mContext);

                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.MERCHANT_ID, entity.merchantInfo.merchantId, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.MERCHANT_LEV, entity.userInfo.lev, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.ON_OFF_VOICE, "1".equals(entity.userInfo.voiceButtonStatus), mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.MER_STATUS, entity.merchantInfo.mer_status, mContext);

                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.AGENT_STATUS, entity.agentInfo.server_status, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.AGENT_REGION, entity.agentInfo.agentRegion, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.AGENT_ID, entity.agentInfo.agent_id, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.AGENT_URL, entity.agentInfo.agent_url, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.USER_LEVEL, entity.userInfo.lev, mContext);
                                com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.MER_REFUSE_REASON, entity.merchantInfo.refuse, mContext);

//                                setGameCenter(entity.gameCenter);
//                                setMerchantCenter(entity.merchantCenter);
                                setFunction(entity.beLooked);

                                lookedAll = entity.lookedAll;
                                if ("0".equals(lookedAll)) {
                                    tvFunctionLook.setVisibility(View.GONE);
                                } else {
                                    tvFunctionLook.setVisibility(View.VISIBLE);
                                }

                                if (isLogin()) {
                                    login(entity);
                                } else {
                                    notLogin();
                                }
                                break;
                            default:
                                timeOut(System.currentTimeMillis());

                                Logger.i("new mine-走了获取icons成功default1111");
                                if (isLogin()) {
                                    login(entity);
                                } else {
                                    notLogin();
                                }
                                break;
                        }
                    } else {
                        timeOut(System.currentTimeMillis());

                        Logger.i("new mine-走了获取icons成功default222");
                        MineIconsEntity entity = response.body();
                        if (isLogin()) {
                            login(entity);
                        } else {
                            notLogin();
                        }
                    }
                }
                stopDialog();
            }

            @Override
            public void onFailure(Call<MineIconsEntity> call, Throwable t) {
                Logger.i("new mine-走了获取icons失败");
                showToast(getResources().getString(R.string.net_work_not_available));

                if (isLogin()) {
                    login(null);
                } else {
                    notLogin();
                }
                stopDialog();
            }
        });
    }

    /**
     * 个人中心线上商城角标数量
     */
    private void initCorner() {
        if (isFirst) {
            return;
        }
        ThreadUtil.getThreadPollProxy().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 这里睡眠1s是为了保证登录后存储USER_ID字段在先，这里获取再后。否则可能出现获取的USER_ID为0的情况
                     */
                    Thread.sleep(1000);
                    String userId;
                    if (isLogin()) {
                        userId = PreferenceUtils.readStrConfig(Constants.USER_ID, mContext, "0");
                    } else {
                        userId = "";
                    }
                    Logger.i("userId 我的---" + userId);
                    RetrofitUtils2.getInstance(mContext).mineCornerMessage(userId, new Callback<MineMallOrderEntity>() {
                        @Override
                        public void onResponse(Call<MineMallOrderEntity> call, Response<MineMallOrderEntity> response) {
                            HttpResultBean body = response.body();
                            if (CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                                if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg)) {
                                    MineMallOrderEntity entity = response.body();
                                    setMallOrder(entity.mallOrder);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MineMallOrderEntity> call, Throwable t) {
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void timeOut(long endTime) {
        if ((endTime - startTime) > 20000) {
            stopDialog();
        }
    }

    /**
     * 必看清单功能的设置
     */
    private void setFunction(ArrayList<MineIconsEntity.IconsBean> list) {
        manifestList.clear();
        manifestList.addAll(list);
        if (functionAdapter == null) {
            functionAdapter = new MineFunctionAdapter(mContext, manifestList);
            functionGridView.setAdapter(functionAdapter);
        } else {
            functionAdapter.notifyDataSetChanged();
        }
        functionGridView.setFocusable(false);

        functionGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLogin()) {
                    if (list.get(position).type == 9 && "29".equals(list.get(position).content)) {
                        jumpToMerchantSettled();
                    } else {
                        JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(position).type, list.get(position).content);
                    }
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
            }
        });
    }

    /**
     * 已登陆状态下---数据的获取
     */
    private void login(MineIconsEntity entity) {
        Logger.i("new mine-已登录");
        registerLayout.setVisibility(View.INVISIBLE);
        loginLayout.setVisibility(View.VISIBLE);
        if (null == entity) {
            tvLoginName.setText("暂无昵称");
            tvTitle.setText("暂无昵称");
            ivLoginPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.userinfor_photo));
            tvLevel.setVisibility(View.GONE);
            tvRed.setText("0.00");
            tvLedou.setText("0.00");
            tvIntegral.setText("0.00");
            tvDaiGou.setText("0.00");
            if (myloading != null) {
                stopMyDialog();
            }
            return;
        }

        Logger.i("new mine-已登录" + entity.userInfo.memberName);
        Logger.i("new mine-已登录" + entity.userInfo.member_photo);
        if (sp.getBoolean(com.yilian.mylibrary.Constants.HAS_MESSAGE, false)) {
            ivDot.setVisibility(View.VISIBLE);
        } else {
            ivDot.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(entity.userInfo.memberName)) {
            tvLoginName.setText("暂无昵称");
            tvTitle.setText("暂无昵称");
        } else {
            tvLoginName.setText(entity.userInfo.memberName);
            tvTitle.setText(entity.userInfo.memberName);
        }

        setPhoto(entity);
        setLevel(entity);

        if (entity.userInfo.userLebi == null) {
            entity.userInfo.userLebi = "0.00";
        }
        tvRed.setText(MoneyUtil.getLeXiangBiNoZero(entity.userInfo.lebi));
        if (entity.userInfo.integral == null) {
            entity.userInfo.integral = "0.00";
        }
        tvLedou.setText(MoneyUtil.getLeXiangBiNoZero(entity.userInfo.bean));

        if (entity.userInfo.userIntegral == null) {
            entity.userInfo.userIntegral = "0.00";
        }
        tvIntegral.setText(MoneyUtil.getLeXiangBiNoZero(entity.userInfo.integral));

        if (entity.userInfo.daiGouQuan == null) {
            entity.userInfo.daiGouQuan = "0.00";
        }
        tvDaiGou.setText(MoneyUtil.getLeXiangBiNoZero(entity.userInfo.daiGouQuan));
    }

    /**
     * 未登陆状态下或者token失效
     */
    private void notLogin() {
        Logger.i("new mine-未登录");
        registerLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);
        tvTitle.setText("未登录");

        tvRed.setText("0.00");
        tvLedou.setText("0.00");
        tvIntegral.setText("0.00");
        tvDaiGou.setText("0.00");
    }

    private void stopDialog() {
        AppManager.getInstance().killActivity(LoadingActivity.class);
        AppManager.getInstance().killActivity(LoadingShopCarActivity.class);
        scrollView.smoothScrollTo(0, 20);
    }

    /**
     * 线上商城的设置
     */
    private void setMallOrder(ArrayList<MallBeanEntity> list) {
        Logger.i("new mine-setMallOrder" + list.toString());
        mallAdapter = new MineMallAdapter(mContext, list);
        mallGridView.setAdapter(mallAdapter);
        mallGridView.setFocusable(false);

        mallGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isLogin()) {
                    Logger.i("new mine-setMallOrder" + list.get(position).type + "---" + list.get(position).content);
                    JumpToOtherPage.getInstance(mContext).jumpToOtherPage(list.get(position).type, list.get(position).content);
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
            }
        });
    }

    /**
     * 商家入驻
     */
    public void jumpToMerchantSettled() {
        if (isLogin()) {
            String merStatus = PreferenceUtils.readStrConfig(Constants.MER_STATUS, mContext);
            Logger.i("2017年11月18日 10:01:08-" + merStatus);
//            Intent intent = new Intent(mContext, MerchantSettledActivity.class);//初始化赋值，防止个人会员没有merStatus字段，从而报空指针
            Intent intent = new Intent(mContext, WebViewActivity.class);//初始化赋值，防止个人会员没有merStatus字段，从而报空指针
            intent.putExtra(Constants.SPKEY_URL, Constants.MERCHANT_ENTER);
            switch (merStatus) {// 0 未成为商家（未缴纳年费） 1 已缴年费成为商家，但未提交资料  2已提交资料待审核 3提交资料审核通过 4审核拒绝
                case "0":
//                    intent = new Intent(mContext, MerchantSettledActivity.class);
                    intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(Constants.SPKEY_URL, Constants.MERCHANT_ENTER);
                    break;
                case "1":
                    intent = new Intent(mContext, MerchantCertificateActivity.class);
                    Logger.i("merStatus  " + com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MER_STATUS, mContext, "") + " levname  " + com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext, ""));
                    intent.putExtra("merchantId", com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext, ""));
                    //  MER_STATUS  0 未成为商家（未缴纳年费） 1 已缴年费成为商家，但未提交资料  2已提交资料待审核 3提交资料审核通过 4审核拒绝
                    switch (com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MER_STATUS, mContext, "")) {
                        case "1"://成为商家未提交资料
                            switch (com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext, "")) {
                                case "2"://个体商家
                                    intent.putExtra("merchantType", "0");
                                    break;
                                case "3"://实体商家
                                case "4":
                                    intent.putExtra("merchantType", "1");
                                    break;
                                default:
                                    intent.putExtra("merchantType", "1");
                                    break;
                            }
                            break;
                        default:
                            break;
                    }
                    break;
                case "2":
                    intent = new Intent(mContext, MerchantAuditActivity.class);
                    intent.putExtra("refuse", com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MER_REFUSE_REASON, mContext, ""));
                    break;
                case "3":
                    intent = new Intent(mContext, MerchantCenterActivity.class);
                    intent.putExtra(Constants.MERCHANT_ID, PreferenceUtils.readStrConfig(Constants.MERCHANT_ID, mContext));
                    break;
                case "4":
                    intent = new Intent(mContext, MerchantAuditActivity.class);
                    intent.putExtra("refuse", com.yilian.mylibrary.PreferenceUtils.readStrConfig(Constants.MER_REFUSE_REASON, mContext, ""));
                    break;
                default:
                    break;
            }
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
        }
    }

    /**
     * 已登陆状态下---头像的显示
     */
    private void setPhoto(MineIconsEntity entity) {
        GlideUtil.showCirHeadNoSuffix(mAppCompatActivity, entity.userInfo.member_photo, ivLoginPhoto);
    }

    /**
     * 已登陆状态下---商家等级的显示
     */
    private void setLevel(MineIconsEntity entity) {
        if (entity.userInfo.lev == null) {
            tvLevel.setVisibility(View.GONE);
        } else {
            tvLevel.setVisibility(View.VISIBLE);
        }
        Logger.i("new mine-已登录" + entity.userInfo.lev);
        switch (entity.userInfo.lev) {
            case "0":
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip0), null);
                break;
            case "1":
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip1), null);
                break;
            case "2":
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip2), null);
                break;
            case "3":
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip3), null);
                break;
            case "4":
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip4), null);
                break;
            case "5":
                tvLevel.setCompoundDrawablesWithIntrinsicBounds(null, null, ContextCompat.getDrawable(mContext, R.mipmap.vip5), null);
                break;
            default:
                break;
        }
    }

    /**
     * 游戏中心的设置
     */
    private void setGameCenter(MineIconsEntity.Bean bean) {
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, bean.url, ivGameIcon);
        tvGameName.setText(bean.title);
        tvGameDesc.setText(bean.Subtitle);

        com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.GAME_CENTER_TYPE, bean.type, mContext);
        com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.GAME_CENTER_CONTENT, bean.content, mContext);
    }

    /**
     * 商家中心的设置
     */
    private void setMerchantCenter(MineIconsEntity.Bean bean) {
        GlideUtil.showImageNoSuffixNoPlaceholder(mContext, bean.url, ivMerchantIcon);
        tvMerchantName.setText(bean.title);
        tvMerchantDesc.setText(bean.Subtitle);

        com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.LE_TAO_BI_TYPE, bean.type, mContext);
        com.yilian.mylibrary.PreferenceUtils.writeStrConfig(Constants.LE_TAO_BI_CONTENT, bean.content, mContext);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_setting:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.iv_message:
                if (isLogin()) {
                    startActivity(new Intent(mContext, InformationActivity.class));
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.btn_login_by_sms_code:
                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                sp.edit().putString(Constants.SPKEY_WX_LOGIN, "1").commit();//此处标记微信登录是用于账号登录
                sp.edit().putString(Constants.SPKEY_ALIPAY_LOGIN, "1").commit();//此处标记支付宝登录是用于账号登录
                startActivity(intent);
                break;
            case R.id.btn_login_by_pwd:
                startActivity(new Intent(mContext, V2_RegisterActivity.class));
                break;
            case R.id.ll_red:
                if (isLogin()) {
                    intent = new Intent(mContext, V3MoneyActivity.class);
                    intent.putExtra("type", V3MoneyActivity.TYPE_MONEY);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.ll_ledou:
                if (isLogin()) {
                    intent = new Intent(mContext, V3MoneyActivity.class);
                    intent.putExtra("type", V3MoneyActivity.TYPE_LE_DOU);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.ll_integral:
                if (isLogin()) {
                    intent = new Intent(mContext, V3MoneyActivity.class);
                    intent.putExtra("type", V3MoneyActivity.TYPE_INTEGRAL);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
                }
                break;
            case R.id.ll_dai_gou:
                JumpYlActivityUtils.toExchangeTicket(mContext);
                break;
//            case R.id.layout_play_center:
//                jumpToPlayCenter();
//                break;
//            case R.id.layout_merchant:
//                jumpToMerchant();
//                break;
            case R.id.ll_all_order:
                jumpToOrder();
                break;
            case R.id.layout_login:
                intent = new Intent(mContext, UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_all_function:
//                intent = new Intent(mContext, AllFunctionActivity.class);
//                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 全部订单
     */
    public void jumpToOrder() {
        if (isLogin()) {
            Intent intent = new Intent(mContext, MyMallOrderListActivity2.class);
            intent.putExtra("order_Type", 0);
            startActivity(intent);
        } else {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        isRefresh = PreferenceUtils.readBoolConfig(Constants.REFRESH_USER_FRAGMENT, mContext, false);
        isShowLoading = PreferenceUtils.readBoolConfig(Constants.REFRESH_USER_FRAGMENT_LOADING, mContext, false);
        Logger.i("JPUserinfo onResume:" + isRefresh);
        Logger.i("JPUserinfo onResume loading:" + isShowLoading);

        if (isRefresh == true) {
            Logger.i("2017年12月5日 15:19:47-走了onResume");
            if (isShowLoading) {
                showDialog();
            }
            registerLayout.setVisibility(View.INVISIBLE);
//            loginLayout.setVisibility(View.INVISIBLE);

            isRefresh = false;
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, isRefresh, mContext);
            PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT_LOADING, false, mContext);
        }
        initData();

        isFirst = false;
    }

    @Override
    protected View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_mine, container, false);
        }
        ViewUtils.inject(this, rootView);

        initView();
        com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
        com.yilian.mylibrary.PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT_LOADING, true, mContext);
        initListener();
        return rootView;
    }

    @Override
    protected void loadData() {

    }

    private void initView() {
        titleLayout.post(new Runnable() {
            @Override
            public void run() {
                int oldHeight = titleLayout.getHeight();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
                int statusBarHeight = StatusBarUtils.getStatusBarHeight(mContext);
                layoutParams.height = oldHeight + statusBarHeight;
                titleLayout.setPadding(0, statusBarHeight, 0, 0);
            }
        });
        screenWidth = ScreenUtils.getScreenWidth(mContext);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) ivTopBg.getLayoutParams();
        params.width = screenWidth;
        params.height = (int) ((433 * 0.1) * ScreenUtils.getScreenWidth(mContext) / (1080 * 0.1));

        ivSetting.setOnClickListener(this);
        ivMessage.setOnClickListener(this);
        btnLoginByCode.setOnClickListener(this);
        btnLoginByPwd.setOnClickListener(this);
        redLayout.setOnClickListener(this);
        ledouLayout.setOnClickListener(this);
        integralLayout.setOnClickListener(this);
        llDaiGou.setOnClickListener(this);
        gameLayout.setOnClickListener(this);
        merchantLayout.setOnClickListener(this);
        allOrderLayout.setOnClickListener(this);
        loginLayout.setOnClickListener(this);
        functionLayout.setOnClickListener(this);
    }

    private void initListener() {
        scrollView.setScrollViewListener(new RayScrollViewListener() {
            @Override
            public void onScrollChanged(DropZoomScrollView scrollView, int x, int y, int oldx, int oldy) {
                distance = y;
                Logger.i("2017年12月6日 11:04:17-" + distance);

                if (distance <= 0) {
                    titleLayout.setBackgroundColor(Color.argb((int) 0, 247, 45, 66));
                    tvTitle.setVisibility(View.GONE);
                } else if (distance > 0 && distance <= 450) {
                    float scale = (float) distance / 450;
                    float alpha = (255 * scale);
                    titleLayout.setBackgroundColor(Color.argb((int) alpha, 247, 45, 66));
                    tvTitle.setVisibility(View.GONE);
                } else {
                    titleLayout.setBackgroundColor(Color.argb((int) 255, 247, 45, 66));
                    tvTitle.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showDialog() {
        startActivity(new Intent(mContext, LoadingActivity.class));
        tvRed.setText("加载中...");
        tvLedou.setText("加载中...");
        tvIntegral.setText("加载中...");
        tvDaiGou.setText("加载中...");
    }


}
