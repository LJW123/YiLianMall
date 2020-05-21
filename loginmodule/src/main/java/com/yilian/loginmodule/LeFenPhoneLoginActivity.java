package com.yilian.loginmodule;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListPopupWindow;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.orhanobut.logger.Logger;
import com.yilian.mylibrary.CheckServiceReturnEntityUtil;
import com.yilian.mylibrary.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.JumpToOtherPageUtil;
import com.yilian.mylibrary.PreferenceUtils;
import com.yilian.mylibrary.RequestOftenKey;
import com.yilian.mylibrary.adapter.DefaultAdapter;
import com.yilian.mylibrary.adapter.ViewHolder;
import com.yilian.networkingmodule.entity.BaseEntity;
import com.yilian.networkingmodule.entity.CheckUserEntity;
import com.yilian.networkingmodule.entity.LoginEntity;
import com.yilian.networkingmodule.entity.StartAppEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 手机号码快捷登录
 * 需要salt和deviceIndex,同时登录的网络请求需要一个BaseURL来构建Retrofit
 * 手机号码登录成功后，发送登录成功广播，由主module里面的广播接收者接收到后，进行获取用户信息的逻辑处理
 */
public class LeFenPhoneLoginActivity extends BaseActivity implements View.OnClickListener {

    private String IpURL;
    private String deviceIndex;
    private String token;
    private TextView tvBack;
    private TextView rightTextview;
    private ImageView ivNearTitleSearch;
    private LinearLayout linearlayoutTitle;
    private ImageView back;
    private ImageView cancel;
    private TextView selectPhone;
    private EditText loginModuleEtphone;
    private TextView selectCode;
    private EditText etpassword;
    private Button btTestandlogin;
    private String salt;
    private Button btnLoginSmsCode;
    private TextView tvRegister;
    private ListPopupWindow listPopupWindow;
    private ArrayList<String> accountHistoryList;

    private HashMap<String, String> accountHistoryMap = new HashMap<>();//账号历史记录
    private DefaultAdapter<String> historyPhoneAdapter;
    private CheckBox checkboxRememberPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_module_activity_phone_login);
        initView();
        ViewUtils.inject(this);
        HashMap<String, String> accountHistory = (HashMap) PreferenceUtils.readObjectConfig(Constants.ACCOUNTHISTORY, mContext);

        if (accountHistory != null) {
            for (String key : accountHistory.keySet()) {
                Logger.i("读取的的账号信息：" + key + "  密码信息：" + accountHistory.get(key));
            }
            this.accountHistoryMap = accountHistory;
            Logger.i("获取的accountHistoryMapSize   " + accountHistoryMap.size());
        } else {
            Logger.i("读取的账号信息是null");
        }
        initListener();
        tvBack.setText("账号登录");
        salt = getIntent().getStringExtra("salt");
        deviceIndex = getIntent().getStringExtra("deviceIndex");
        Logger.i("获取的盐值：" + salt);
    }

    private void showListPopulWindow() {
        accountHistoryList = new ArrayList<>();
        if (accountHistoryMap != null && accountHistoryMap.size() > 0) {
            for (String key : accountHistoryMap.keySet()
                    ) {
                accountHistoryList.add(key);
            }
        } else {
            accountHistoryList.add("");
        }
        Logger.i("accountHistoryListSize   " + accountHistoryList.size());

        listPopupWindow = new ListPopupWindow(this);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                R.layout.login_module_account_history, R.id.tv_account, accountHistoryList);

        historyPhoneAdapter = new DefaultAdapter<String>(mContext, accountHistoryList, R.layout.login_module_account_history) {
            @Override
            public void convert(ViewHolder helper, String item, final int position) {
                final String phoneKey = accountHistoryList.get(position);
                helper.getView(R.id.iv_delete).setOnClickListener(new View.OnClickListener() {//点击删除此条账号记录
                    @Override
                    public void onClick(View v) {
                        Logger.i("popwindow的adapter,position:" + position);
                        accountHistoryList.remove(position);
                        if (accountHistoryList.size() <= 0) {
                            listPopupWindow.dismiss();
                        }
                        notifyDataSetChanged();
                        accountHistoryMap.remove(phoneKey);
                        updateAccountHistory(accountHistoryMap);
                        /**
                         * 如果删除的账号与目前文本上显示的账号一致，则删除文本账号的密码,并删除sp存储的账号对应密码信息
                         */
                        if (phoneKey.equals(loginModuleEtphone.getText().toString().trim())) {
                            etpassword.setText("");
                            checkboxRememberPassword.setChecked(false);
                            PreferenceUtils.writeStrConfig(Constants.SPKEY_PHONE_PASSWORD, "", mContext);
                        }
                        for (String key : accountHistoryMap.keySet()) {
                            Logger.i("删除后的的账号信息：" + key + "  密码信息：" + accountHistoryMap.get(key));
                        }
                    }
                });
                final TextView tvAccount = (TextView) helper.getView(R.id.tv_account);
                tvAccount.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(tvAccount.getText().toString().trim())) {
                            loginModuleEtphone.setText(phoneKey);
                            etpassword.setText(accountHistoryMap.get(phoneKey));
                            //如果上次选择了记住密码，则此次默认选中记住密码
                            checkboxRememberPassword.setChecked(true);
                        }
                        listPopupWindow.dismiss();
                    }
                });
                tvAccount.setText(item);
            }
        };
        listPopupWindow.setAdapter(historyPhoneAdapter);

        listPopupWindow.setAnchorView(loginModuleEtphone);
        listPopupWindow.setModal(true);
        listPopupWindow.show();

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showToast("选择了第 " + i + " 账号");
                loginModuleEtphone.setText(accountHistoryList.get(i));
            }
        });
        listPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Logger.i("pop消失了");
            }
        });
    }

    /**
     * 更新账号记录
     *
     * @param accountHistoryMap
     */
    private void updateAccountHistory(HashMap<String, String> accountHistoryMap) {
        Logger.i("更新账号记录信息   " + accountHistoryMap.size());
        PreferenceUtils.writeObjectConfig(Constants.ACCOUNTHISTORY, accountHistoryMap, mContext);
    }


    private void initListener() {

        loginModuleEtphone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableWidth = loginModuleEtphone
                            .getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width();
                    if (event.getX() >= (loginModuleEtphone.getWidth() - drawableWidth - 20)) {
                        // your action here
                        showListPopulWindow();
                        return true;
                    }
                }
                return false;
            }
        });
        btnLoginSmsCode.setOnClickListener(this);
        cancel.setOnClickListener(this);
        back.setOnClickListener(this);
        loginModuleEtphone.addTextChangedListener(new watcher(loginModuleEtphone, 11));

    }

    public void login(final View view) {
        login();
    }

    /**
     * 获取最新盐值，且只重新请求一次
     */
    boolean reGetSaltFailed = true;

    private void login() {
        Logger.i("lib走了这里1");
//        if (!isRegister) {//未注册
//            showToast("您当前还未注册,请先使用快捷登录");
//            return;
//        }
        final String phone = loginModuleEtphone.getText().toString();
        if (!CommonUtils.isPhoneNumer(phone)) {
            showToast(R.string.login_module_send_phone_number_not_legal);
            return;
        }

        // 登录
        final String inputPassword = etpassword.getText().toString();
        if (!CommonUtils.passwordVerify(inputPassword)) {
            Logger.i("lib走了这里3");
            Toast.makeText(mContext, "密码不符合规则", Toast.LENGTH_SHORT).show();
            return;
        }
        Logger.i("lib走了这里4");
        String password = CommonUtils.getMD5Str(inputPassword)
                + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext));
        Logger.i("登录的  密码的MD5值  " + CommonUtils.getMD5Str(inputPassword) + " 盐值MD5 " + CommonUtils.getMD5Str(RequestOftenKey.getServerSalt(mContext)) + " 盐值  " + RequestOftenKey.getServerSalt(mContext));
        try {
            Logger.i("lib走了这里5");
            startMyDialog();
            RetrofitUtils.getInstance(mContext).setDeviceIndex(RequestOftenKey.getDeviceIndex(mContext))
                    .loginByPassWord(phone.trim(), password.toLowerCase(), new Callback<LoginEntity>() {
                        @Override
                        public void onResponse(Call<LoginEntity> call, Response<LoginEntity> response) {
                            LoginEntity body = response.body();
                            stopMyDialog();
                            if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, response.body())) {
                                showToast(getString(R.string.login_module_service_exception));
                                return;
                            }
                            if (body.code == -24) {
//                                盐值验证失败，重新获取一次盐值，再次登录，若再次失败，则提示用户
                                if (reGetSaltFailed) {
                                    getStalt();
                                    return;
                                }
                            }
                            if (body.code == -16) {
                                showToast(R.string.login_module_phone_not_register);
                                return;
                            }
                            if (body.code == -28) {
                                showToast("未设置登录密码");
                                return;
                            }
                            if (CommonUtils.serivceReturnCode(mContext, body.code, body.msg, this.getClass())) {
                                switch (body.code) {
                                    case 1:
                                        Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                                        //刷新个人页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT, true, mContext);
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_USER_FRAGMENT_LOADING, true, mContext);
                                        //刷新购物车页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_SHOP_FRAGMENT, true, mContext);
                                        //刷新主页面标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_HOME_FRAGMENT, true, mContext);
                                        //刷新幸运购标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_LUCKY_DETAIL, true, mContext);
                                        //刷新奖励标识
                                        PreferenceUtils.writeBoolConfig(Constants.REFRESH_RED_FRAGMENT, true, mContext);

                                        //记住账号密码
                                        saveAccount(phone, inputPassword);

//                                        Intent intent = new Intent();
//                                        intent.setAction("com.yilian.phone.loginByPassWord");
//                                        intent.putExtra("RegisterAccount", body);
//                                        intent.putExtra("phone", phone);
//                                        sendBroadcast(intent);
/**
 * {@link com.yilian.mall.BaseActivity#onEventMainThread}
 */
                                        body.phone = phone;
                                        EventBus.getDefault().post(body);

                                        finish();
                                        break;
                                    case -5:
                                        Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        break;
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<LoginEntity> call, Throwable t) {
                            showToast(R.string.login_module_net_work_not_available);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取盐值
     */
    @SuppressWarnings("unchecked")
    private void getStalt() {
        startMyDialog();
        reGetSaltFailed = false;
        int versionCode = 0;
        try {
            versionCode = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
            Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).startApp("start_app", RequestOftenKey.getDeviceIndex(mContext), RequestOftenKey.gettoken(mContext),
                    "0", PreferenceUtils.readStrConfig(Constants.SPKEY_CLIENTID, mContext, "0"), "0", "0", String.valueOf(versionCode))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<StartAppEntity>() {
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
                        public void onNext(StartAppEntity startAppEntity) {
//                                                    存储最新盐值和服务器返回的最新版本
                            PreferenceUtils.writeStrConfig(Constants.SPKEY_SERVER_SALT, startAppEntity.serverSalt, mContext);
                            PreferenceUtils.writeStrConfig(Constants.SPKEY_LATEST_VERSION, startAppEntity.newestVersion, mContext);
                            login();
                        }
                    });
            addSubscription(subscription);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveAccount(String phone, String inputPassword) {
//       保存之前先清除，保证记住密码不重复，且没有选择记住密码时，把之前该账号记录清除
        if (accountHistoryMap.containsKey(phone)) {
            accountHistoryMap.remove(phone);
        }
        PreferenceUtils.writeStrConfig(Constants.SPKEY_PHONE_PASSWORD, "", mContext);

        if (checkboxRememberPassword.isChecked()) {
            for (String key :
                    accountHistoryMap.keySet()) {
                Logger.i("存入的账号信息：" + key + "  密码信息：" + accountHistoryMap.get(key));
            }
//      存入本次登录密码 下次打开时自动放置到密码框
            PreferenceUtils.writeStrConfig(Constants.SPKEY_PHONE_PASSWORD, phone + "," + inputPassword, mContext);
//       记录账号，显示在账号列表中
            accountHistoryMap.put(phone, inputPassword);
        }
        updateAccountHistory(accountHistoryMap);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cancel || i == R.id.back) {
            finish();
        } else if (i == R.id.btn_login_sms_code) {
            Intent intent = new Intent(this, V2_LoginActivity.class);
            intent.putExtra("phone", loginModuleEtphone.getText().toString().trim());
            startActivity(intent);
            finish();
        } else if (i == R.id.tv_register) {
            Intent intent = new Intent(this, V2_RegisterActivity.class);
            intent.putExtra("phone", loginModuleEtphone.getText().toString().trim());
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        checkboxRememberPassword = (CheckBox) findViewById(R.id.checkbox_remember_password);
        Spanned spanned = Html.fromHtml("<font color='#999999'>还没有账号?</color></font><font><color='#4a4a4a'>快速注册</font>");
        tvRegister = (TextView) findViewById(R.id.tv_register);
        tvRegister.setText(spanned);
        tvRegister.setOnClickListener(this);
        btnLoginSmsCode = (Button) findViewById(R.id.btn_login_sms_code);
        tvBack = (TextView) findViewById(R.id.tv_back);
        rightTextview = (TextView) findViewById(R.id.right_textview);
        ivNearTitleSearch = (ImageView) findViewById(R.id.iv_near_title_search);
        linearlayoutTitle = (LinearLayout) findViewById(R.id.linearlayout_title);
        back = (ImageView) findViewById(R.id.back);
        cancel = (ImageView) findViewById(R.id.cancel);
        selectPhone = (TextView) findViewById(R.id.select_phone);
        loginModuleEtphone = (EditText) findViewById(R.id.login_module_etphone);
        selectCode = (TextView) findViewById(R.id.select_code);
        etpassword = (EditText) findViewById(R.id.etpassword);
        btTestandlogin = (Button) findViewById(R.id.btn_login_by_password);

        tvBack.setOnClickListener(this);
        rightTextview.setOnClickListener(this);
        back.setOnClickListener(this);
        cancel.setOnClickListener(this);
        initPhoneAndPassword();
        if (!TextUtils.isEmpty(loginModuleEtphone.getText().toString().trim())) {
            loginModuleEtphone.setSelection(loginModuleEtphone.getText().toString().length());
        }
    }

    /**
     * 自动放置上次登录的账号和密码
     */
    private void initPhoneAndPassword() {
        String savePhone = sp.getString(Constants.SPKEY_PHONE, "");
        if (!TextUtils.isEmpty(savePhone)) {
            loginModuleEtphone.setText(savePhone);
        }
        String phone_password = PreferenceUtils.readStrConfig(Constants.SPKEY_PHONE_PASSWORD, mContext, "");
        String[] split = phone_password.split(",");
        if (split.length > 1) {
            if (split[0].equals(savePhone)) {
                etpassword.setText(split[1]);
                //如果上次选择了记住密码，则此次默认选中记住密码
                checkboxRememberPassword.setChecked(true);
            }
        }
    }


    class watcher implements TextWatcher {

        private EditText edit;
        private int maxlen;

        public watcher(EditText et_register_phone, int len) {
            this.edit = et_register_phone;
            this.maxlen = len;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String phone = edit.getText().toString().trim();
            int len = phone.length();
            if (len >= maxlen) {
                etpassword.requestFocus();//设置光标
                if (!CommonUtils.isPhoneNumer(phone)) {
                    showToast(R.string.login_module_send_phone_number_not_legal);
                    return;
                }
                RetrofitUtils.getInstance(mContext).checkUser(phone, new Callback<CheckUserEntity>() {
                    @Override
                    public void onResponse(Call<CheckUserEntity> call, Response<CheckUserEntity> response) {
                        BaseEntity body = response.body();
                        if (!CheckServiceReturnEntityUtil.checkServiceReturnEntity(mContext, body)) {
                            showToast(R.string.login_module_service_exception);
                            return;
                        }
                        if (body.code == 1) {
                            showToast(R.string.login_module_phone_not_register);
                            return;
                        }
                    }

                    @Override
                    public void onFailure(Call<CheckUserEntity> call, Throwable t) {
                        showToast(R.string.network_module_net_work_error);
                    }
                });
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkboxRememberPassword.setChecked(false);
            etpassword.setText("");
        }
    }

    // 找回密码
    public void backPassword(View view) {
        JumpToOtherPageUtil.getInstance().jumpToRetrievePasswordActivity(mContext);
        finish();
    }
}