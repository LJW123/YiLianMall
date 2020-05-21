package com.yilian.mall.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.PrizeVoucherListAdapter;
import com.yilian.mall.entity.GetPrizeVoucherListResult;
import com.yilian.mall.entity.PrizeVoucher;
import com.yilian.mall.http.ServiceNetRequest;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Collections;
import java.util.Comparator;

/**
 * 中奖凭证
 * @author Administrator
 *
 */
public class PrizeVoucherListActivity extends BaseActivity implements OnClickListener {


	@ViewInject(R.id.tv_back)
	TextView mTxtTitle;

	@ViewInject(R.id.list_vouchers)
	ListView mListVouchers;

	@ViewInject(R.id.layout_no_data)
	View mViewNoData;

	/**
	 * 去看看
	 */
	@ViewInject(R.id.imgView_see)
	ImageView mImgViewSee;

	private ServiceNetRequest serviceNetRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prize_voucher);
		ViewUtils.inject(this);
		serviceNetRequest = new ServiceNetRequest(mContext);
		init();
		registerEvents();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPrizeVouchers();
	}

	public void init() {
		mTxtTitle.setText("兑换凭证");
	}

	public void registerEvents() {
		mImgViewSee.setOnClickListener(this);
		mListVouchers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				PrizeVoucher voucher=(PrizeVoucher) parent.getItemAtPosition(position);
				if (voucher.status == 0) {
					Intent intent=new Intent(mContext, PrizeVoucherDetailActivity.class);
					intent.putExtra("voucher_index", voucher.id);
					startActivity(intent);
				}else if(voucher.status==2){
					showToast("已过期");
				}else if(voucher.status==1){
					showToast("已使用");
				}
			}
		});
	}

	public void getPrizeVouchers() {

		serviceNetRequest.getPrizeVoucherList(GetPrizeVoucherListResult.class, new RequestCallBack<GetPrizeVoucherListResult>() {

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				startMyDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<GetPrizeVoucherListResult> responseInfo) {
				stopMyDialog();
                GetPrizeVoucherListResult result = responseInfo.result;
                if (result ==null) {
					return;
				}
				if (result.code==1) {
					if (result.vouchers==null|| result.vouchers.size()==0) {
						mViewNoData.setVisibility(View.VISIBLE);
						mListVouchers.setVisibility(View.GONE);
					} else {
						mViewNoData.setVisibility(View.GONE);
						mListVouchers.setVisibility(View.VISIBLE);
						Collections.sort(result.vouchers, new Comparator<PrizeVoucher>() {

							@Override
							public int compare(PrizeVoucher lhs, PrizeVoucher rhs) {
								// TODO Auto-generated method stub
								return (int) (rhs.validTime-lhs.validTime);
							}
						});
						PrizeVoucherListAdapter adapter = new PrizeVoucherListAdapter(PrizeVoucherListActivity.this, result.vouchers);
						mListVouchers.setAdapter(adapter);
					}
				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				stopMyDialog();
				showToast(R.string.net_work_not_available);
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgView_see:
			Intent intent = new Intent(this, ListActivity.class);
			intent.putExtra("activityName", "活动中心");
			intent.putExtra("activityType", "0");
			startActivity(intent);
			break;

		default:
			break;
		}
	}


}
