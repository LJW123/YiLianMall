package com.yilian.mall.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.GetUserInfoEntity;
import com.yilian.mall.http.AccountNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.RegExUtils;

/**
 * 修改昵称界面
 * @author Administrator
 */
public class AmendUserinfoActivity extends BaseActivity{

	@ViewInject(R.id.tv_back)
	private TextView tv_back;

	@ViewInject(R.id.edit_user_info)
	private EditText edit_user_info;

	private AccountNetRequest accountNetRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_amend_userinfor);

		ViewUtils.inject(this);

		tv_back.setText(this.getIntent().getStringExtra("title"));

		accountNetRequest = new AccountNetRequest(mContext);
		
	}

	public void save(View view){
		if(TextUtils.isEmpty(edit_user_info.getText()))
		{
			showToast("请输入昵称");
			return;
		}
		if(edit_user_info.getText().toString().trim().length()>10){
			showToast("请输入长度小于10的昵称");
			return;
		}
        if (!RegExUtils.isNumberEnglishChinexe(edit_user_info.getText().toString().trim())) {
            showToast("可能包含特殊字符,请修改重试");
            return;
        }

		GetUserInfoEntity info = new GetUserInfoEntity();
		GetUserInfoEntity.UserInfo userInfo = info.new UserInfo();
        userInfo.name = edit_user_info.getText().toString().trim();
        accountNetRequest.setUserInfo(userInfo, new RequestCallBack<BaseEntity>() {
            @Override
            public void onStart() {
                super.onStart();
                startMyDialog();
            }

            @Override
			public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
				stopMyDialog();
				if (CommonUtils.NetworkRequestReturnCode(mContext, responseInfo.result.code+"")) {
					showToast("修改成功！");
					//刷新个人页面标识
					sp.edit().putBoolean(Constants.REFRESH_USER_FRAGMENT, true).commit();
					sp.edit().putString(AmendUserinfoActivity.this.getIntent().getStringExtra("key").toString(), edit_user_info.getText().toString()).commit();
					finish();
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				stopMyDialog();
				showToast("网络连接失败，请检查网络~");
			}
		});
	}

}
