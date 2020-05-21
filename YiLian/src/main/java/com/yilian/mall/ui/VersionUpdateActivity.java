package com.yilian.mall.ui;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Constants;

/**
 * 版本更新
 */
public class VersionUpdateActivity extends BaseActivity {

	@ViewInject(R.id.tv_back)
	private TextView tv_back;

	@ViewInject(R.id.tv_version)
	private TextView tv_version;
	@ViewInject(R.id.tv_hint)
	private TextView tvHint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_version_update);
		ViewUtils.inject(this);
		tv_back.setText("关于我们");
		tvHint.setText("©2017 河南乐善网络科技有限公司. All rights reserved.");
		try {
			tv_version.setText("当前版本 :V"+this.getPackageManager().getPackageInfo("com.yilian.mall", 0).versionName);
		} catch (NameNotFoundException e) {

			e.printStackTrace();
		}
	}

	/**
	 * 跳到协议界面
	 * @param view
	 */
	public void jumpAgreement(View view){
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra(Constants.SPKEY_URL, Constants.REGISTER_HTTP);
		startActivity(intent);
	}

	/**
	 * 拨打 4000910365
	 * @param view
	 */
	public void callPhone(View view){
		Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:4001525159"));
		startActivity(intent);
	}

}
