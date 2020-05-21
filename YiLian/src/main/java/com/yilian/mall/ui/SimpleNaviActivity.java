package com.yilian.mall.ui;

import com.yilian.mall.BaseActivity;
/**
 * 
 *导航界面
 * 
 * */
public class SimpleNaviActivity extends BaseActivity {
//	//导航View
//	private AMapNaviView mAmapAMapNaviView;
//	//是否为模拟导航
//	private boolean mIsEmulatorNavi = true;
//	//记录有哪个页面跳转而来，处理返回键
//	private int mCode=-1;
//
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_simplenavi);
//		Bundle bundle = getIntent().getExtras();
//		processBundle(bundle);
//		init(savedInstanceState);
//
//	}
//
//	private void processBundle(Bundle bundle) {
//		if (bundle != null) {
//			mIsEmulatorNavi = bundle.getBoolean("isemulator", true);
//			mCode=bundle.getInt("activityindex");
//		}
//	}
//
//	/**
//	 * 初始化
//	 *
//	 * @param savedInstanceState
//	 */
//	private void init(Bundle savedInstanceState) {
//		mAmapAMapNaviView = (AMapNaviView) findViewById(R.id.simplenavimap);
//		mAmapAMapNaviView.onCreate(savedInstanceState);
//		mAmapAMapNaviView.setAMapNaviViewListener(this);
//		TTSController.getInstance(this).startSpeaking();
//		if (mIsEmulatorNavi) {
//			// 设置模拟速度
//			AMapNavi.getInstance(this).setEmulatorNaviSpeed(100);
//			// 开启模拟导航
//			AMapNavi.getInstance(this).startNavi(AMapNavi.EmulatorNaviMode);
//
//		} else {
//			// 开启实时导航
//			AMapNavi.getInstance(this).startNavi(AMapNavi.GPSNaviMode);
//		}
//	}
//
//	//-----------------------------导航界面回调事件------------------------
//	/**
//	 * 导航界面返回按钮监听
//	 * */
//	@Override
//	public void onNaviCancel() {
//		finish();
//	}
//
//	@Override
//	public void onNaviSetting() {
//
//	}
//
//	@Override
//	public void onNaviMapMode(int arg0) {
//		// TODO Auto-generated method stub
//
//	}
//	@Override
//	public void onNaviTurnClick() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onNextRoadClick() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onScanViewButtonClick() {
//		// TODO Auto-generated method stub
//
//	}
//	/**
//	 *
//	 * 返回键监听事件
//	 * */
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			showDialog("提示", "确定退出导航?", null, R.mipmap.icon_warning,
//					Gravity.CENTER,
//					"确定", "取消", true, new OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					switch (which) {
//					case DialogInterface.BUTTON_POSITIVE:
//						dialog.dismiss();
//						finish();
//						break;
//
//					case DialogInterface.BUTTON_NEGATIVE:
//						dialog.dismiss();
//						break;
//					}
//
//				}
//			}, mContext);
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}
//
//	// ------------------------------生命周期方法---------------------------
//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		mAmapAMapNaviView.onSaveInstanceState(outState);
//	}
//
//	@Override
//	public void onResume() {
//		super.onResume();
//		mAmapAMapNaviView.onResume();
//
//	}
//
//	@Override
//	public void onPause() {
//		super.onPause();
//		mAmapAMapNaviView.onPause();
//		AMapNavi.getInstance(this).stopNavi();
//	}
//
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		mAmapAMapNaviView.onDestroy();
//
//		TTSController.getInstance(this).stopSpeaking();
//
//	}
//
//	@Override
//	public void onLockMap(boolean arg0) {
//
//		// TODO Auto-generated method stub
//
//	}
//
//	/**
//	 * 导航页面返回监听
//	 */
//	@Override
//	public boolean onNaviBackClick() {
//		showDialog("提示", "确定退出导航?", null, R.mipmap.icon_warning,
//				Gravity.CENTER,
//				"确定", "取消", true, new OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				switch (which) {
//				case DialogInterface.BUTTON_POSITIVE:
//					finish();
//					break;
//
//				case DialogInterface.BUTTON_NEGATIVE:
//					dialog.dismiss();
//					break;
//				}
//
//			}
//		}, mContext);
//		return true;
//
//	}

}
