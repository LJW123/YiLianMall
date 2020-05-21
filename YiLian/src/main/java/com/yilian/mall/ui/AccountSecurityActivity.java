package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.utils.PreferenceUtils;

/**
 * 账号与安全
 */
public class AccountSecurityActivity extends BaseActivity {


	@ViewInject(R.id.tv_back)
	private TextView tv_back;

	@ViewInject(R.id.tv_pay_password)
	private TextView tv_pay_password;

	@ViewInject(R.id.tv_login_password)
	private TextView tv_login_password;

	@ViewInject(R.id.phone)
	private TextView phone;

	@ViewInject(R.id.nickname)
	private TextView nickname;

	@ViewInject(R.id.card_name)
	private TextView card_name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_security);
		ViewUtils.inject(this);

		tv_back.setText("账号与安全");

	}

	@Override
	protected void onResume() {
		super.onResume();
		setData();
	}

	private void setData() {

		nickname.setText(sp.getString("name", "未设置"));
		if (sp.getBoolean("login_password", false)) {
			tv_login_password.setText("修改");
		} else {
			tv_login_password.setText("未设置");
		}	

		if (PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD,mContext,false)) {
			tv_pay_password.setText("修改");
		} else {
			tv_pay_password.setText("未设置");
		}
		
		if (!sp.getString("card_index", "0").equals("0")) {
			if (!sp.getString("card_name", "").equals("")) {
				card_name.setText(sp.getString("card_name", ""));
				card_name.setClickable(false);
			}else{
				card_name.setText("审核中");
			}
		} else {
			card_name.setText("未设置");
		}
		phone.setText(sp.getString("phone", "").length()>10?sp.getString("phone", "").substring(0,3)+"****"+sp.getString("phone", "").substring(7):"");
	}
	// 修改昵称
	public void changeNickname(View view){
		Intent intent = new Intent(AccountSecurityActivity.this, AmendUserinfoActivity.class);
		intent.putExtra("key", "name");
		intent.putExtra("title", "修改昵称");
		startActivity(intent);
	}
	// 修改绑定手机
//	public void changePhone(View view){
//		Toast.makeText(AccountSecurityActivity.this, "目前暂不支持修改绑定手机~", Toast.LENGTH_SHORT).show();
//	}
	// 修改密码
	public void changePassword(View view){
		if (sp.getBoolean("login_password", false)) {
			startActivity(new Intent(AccountSecurityActivity.this, ChangePasswordActivity.class));
		} else {
			Intent intent = new Intent(AccountSecurityActivity.this, RetrievePasswordActivity.class);
			intent.putExtra("title", "设置密码");
			startActivity(intent);
		}

	}
	
	// 修改支付密码
	public void changePayPassword(View view){
		if (PreferenceUtils.readBoolConfig(com.yilian.mylibrary.Constants.PAY_PASSWORD,mContext,false)) {
			Intent intent = new Intent(AccountSecurityActivity.this, PhonePayPasswordActivity.class);
			intent.putExtra("titleName", "修改支付密码");
			startActivity(intent);
		} else {
			startActivity(new Intent(AccountSecurityActivity.this, InitialPayActivity.class));
		}
	}

	// 重置支付密码
	public void resetPayPassword(View view){
		startActivity(new Intent(AccountSecurityActivity.this, InitialPayActivity.class));
	}

	// 实名认证
	public void Authentication(View view){
	}

	// 修改绑定手机
	public void editPhone(View view){
		startActivity(new Intent(AccountSecurityActivity.this, EditPhoneActivity.class));
	}




}
