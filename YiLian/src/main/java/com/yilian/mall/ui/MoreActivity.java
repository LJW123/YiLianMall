package com.yilian.mall.ui;

import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.DataCleanManager;
import com.yilian.mall.utils.FileUtils;
import com.yilian.mylibrary.Constants;

import java.io.File;

/**
 * 更多
 */
public class MoreActivity extends BaseActivity{
	
	@ViewInject(R.id.tv_back)
	private TextView tv_back;
	
	@ViewInject(R.id.tv_version)
	private TextView tv_version;
	
//	@ViewInject(R.id.toggle)
//	private MyToggle toggle;
	
	@ViewInject(R.id.clear_cache)
	private TextView clearCache;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		ViewUtils.inject(this);
		
		tv_back.setText("更多");
		
//		toggle.setToggleState(true);
//		 //设置开关显示所用的图片
//        toggle.setImageRes(R.drawable.btnred, R.drawable.btngray, R.drawable.btnwhite);
//        //设置开关的默认状态    true开启状态
		
		try {
			tv_version.setText(this.getPackageManager().getPackageInfo("com.yilian.mall", 0).versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clearCache.setText(FileUtils.getFileSizes("/com.yilian/")+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearCache(View view){
		try {
			File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separatorChar + "com.yilian");
			FileUtils.deleteDirectory(file);
			FileUtils.deleteDirectory(this.getCacheDir());
			DataCleanManager.cleanSharedPreference(this);
			clearCache.setText("暂无缓存数据");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void useHelp(View view){
		Intent intent = new Intent(mContext, WebViewActivity.class);
		intent.putExtra("title_name", getString(R.string.use_help));
		intent.putExtra("url", Constants.UseHelp);
		startActivity(intent);
	}
	
	public void feedback(View view){
		startActivity(new Intent(MoreActivity.this, FeedbackActivity.class));
	}
	
	public void versionUpdate(View view){
		startActivity(new Intent(MoreActivity.this, VersionUpdateActivity.class));
	}

}
