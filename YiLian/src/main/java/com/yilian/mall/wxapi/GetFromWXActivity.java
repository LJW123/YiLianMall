package com.yilian.mall.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yilian.mylibrary.Constants;

public class GetFromWXActivity extends Activity {

	private static final int THUMB_SIZE = 150;

	private IWXAPI api;
	private Bundle bundle;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
		bundle = getIntent().getExtras();

//		setContentView(R.layout.get_from_wx);
	}

//	@Override
//	public void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		bundle = intent.getExtras();
//	}


}
