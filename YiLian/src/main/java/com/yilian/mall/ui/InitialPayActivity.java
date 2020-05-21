package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 设置支付密码
 *
 */
public class InitialPayActivity extends BaseActivity {
	@ViewInject(R.id.tv_back)
	private TextView tv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial_pay);
		ViewUtils.inject(this);

		tv_back.setText("设置支付密码");
		dismissInputMethod();
	}

	public void onBack(View view){
		onBackPressed();
	}

	public void OK(View view){
		startActivity(new Intent(InitialPayActivity.this, AuthenticationActivity.class));
		finish();
	}
}
