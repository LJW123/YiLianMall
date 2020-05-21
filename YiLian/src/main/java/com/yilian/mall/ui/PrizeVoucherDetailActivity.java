package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mylibrary.Ip;
import com.yilian.mall.entity.PrizeVoucherDetailBean;
import com.yilian.mall.entity.UseVoucherEntity;
import com.yilian.mall.http.ServiceNetRequest;
import com.yilian.mall.image.ImageUtils;
import com.yilian.mall.widgets.DeliveryDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * 获奖凭证详情
 */
public class PrizeVoucherDetailActivity extends BaseActivity implements View.OnClickListener {

	@ViewInject(R.id.tv_back)
	private TextView tv_back;

	@ViewInject(R.id.prize_tiaoxinma)
	private ImageView tiaoxinma;// 条形码

	@ViewInject(R.id.vocher_secret)
	private TextView secret;// 奖品凭证编号

	@ViewInject(R.id.voucher_prize_name)
	private TextView prize_name;// 奖品名字

	@ViewInject(R.id.voucher_grant_time)
	private TextView grant_time;// 中奖时间

	@ViewInject(R.id.voucher_valid_time)
	private TextView valid_time;// 过期时间
	@ViewInject(R.id.activity_name)
	private TextView activity_name;// 活动名称

	@ViewInject(R.id.voucher_adderss)
	private TextView adderss;// 兑换地址

	@ViewInject(R.id.vocher_tel)
	private TextView tel;// 联系电话
	@ViewInject(R.id.voucher_filiale_name)
	private TextView filiale_name;// 商家名字

	@ViewInject(R.id.btn_used)
	private Button btnUserd;
	private String voucher_index;
@ViewInject(R.id.ll_voucher_type)
private LinearLayout llVoucherType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_huojiangpingzheng_detail);
		ViewUtils.inject(this);
		tv_back.setText("获奖凭证详情");
		getVoucherDetail();
		btnUserd.setOnClickListener(this);
	}

	private void getVoucherDetail() {


		startMyDialog();
		HttpUtils httpUtils = new HttpUtils();
		RequestParams params = new RequestParams();
		params.addBodyParameter("device_index", sp.getString("device_index", ""));// 设备编号
//		params.addBodyParameter("token", getToken());// 用户登录凭证(token值+盐做数学运算)
		voucher_index = getIntent().getStringExtra("voucher_index");
		params.addBodyParameter("prize_voucher", voucher_index);// 奖品凭证编号
		params.addBodyParameter("c", "prize_voucher_info");
		httpUtils.send(HttpMethod.POST, Ip.getURL(mContext) + "service.php", params, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				stopMyDialog();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				stopMyDialog();
				PrizeVoucherDetailBean prizeVoucherDetail = new Gson().fromJson(arg0.result,
						PrizeVoucherDetailBean.class);

				if (prizeVoucherDetail.getCode().equals("1")) {

					String voucher_prize_name = prizeVoucherDetail.getVoucher_prize_name();
					String voucher_grant_time = prizeVoucherDetail.getVoucher_grant_time();
					String voucher_valid_time = prizeVoucherDetail.getVoucher_valid_time();
					String prize_status = prizeVoucherDetail.getPrize_status();
					String vocher_secret = prizeVoucherDetail.getVoucher_vocher_secret();
					String voucher_filiale_name = prizeVoucherDetail.getVoucher_filiale_name();
					String voucher_address = prizeVoucherDetail.getVoucher_address();
					String voucher_tel = prizeVoucherDetail.getVoucher_tel();

					BitMatrix matrix;
					try {
						DisplayMetrics dm = new DisplayMetrics();
						getWindowManager().getDefaultDisplay().getMetrics(dm);
						int scrWidth = dm.widthPixels;
						int scrHeight = dm.heightPixels;
						matrix = new MultiFormatWriter().encode(vocher_secret, BarcodeFormat.CODE_128, scrWidth, 120);
						tiaoxinma.setImageBitmap(ImageUtils.toBitmap(matrix));

						// saveBitmap(ImageUtils.toBitmap(matrix));
					} catch (WriterException e) {
						showToast("条形码生成失败");
					}

					StringBuffer sb = null;
					if (vocher_secret.length() > 8) {
						int f = vocher_secret.length() / 4;
						int y = vocher_secret.length() % 4;
						sb = new StringBuffer();
						for (int j = 0; j < f - 1; j++) {
							sb.append(vocher_secret.substring(j * 4, (j + 1) * 4) + "   ");
						}
						sb.append(vocher_secret.substring(vocher_secret.length() - 4 - y, vocher_secret.length()));
						secret.setText(sb);
					} else {
						secret.setText(vocher_secret);
					}

					prize_name.setText(voucher_prize_name);

					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String timeGrant = sdf.format(new Date(Long.valueOf(voucher_grant_time + "000"))).substring(0, 10);
					String timeValid = sdf.format(new Date(Long.valueOf(voucher_valid_time + "000"))).substring(0, 10);
					grant_time.setText(timeGrant);
					valid_time.setText(timeValid);

					switch (prizeVoucherDetail.getVoucher_type()){
						case 1:
							activity_name.setText("大转盘");
							break;
						case 2:
							activity_name.setText("大家猜");
							break;
						case 3:
							activity_name.setText("刮刮乐");
							break;
						case 4:
							activity_name.setText("摇一摇");
							break;
						default:
							break;
					}
					adderss.setText(voucher_address);
					tel.setText(voucher_tel);
					filiale_name.setText(voucher_filiale_name);
				}
			}

		});
	}

	private Uri saveBitmap(Bitmap bm) {
		File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yilian/test/");
		if (!tmpDir.exists()) {
			tmpDir.mkdirs();
		}
		File img = new File(tmpDir.getAbsolutePath() + "1.png");
		try {
			FileOutputStream fos = new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (java.io.FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.btn_used:
				showDialog();

				break;
		}
	}

	/**
	 * 弹出确认使用弹窗
	 */
	private void showDialog() {
		new DeliveryDialog.Builder(mContext)
                .setMessage("在使用凭证之前，请确认您的奖品已经领取，该凭证使用后将不再具有兑换奖品的效用")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("取消使用");
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showToast("使用成功");
                        useVoucher();
                        dialog.dismiss();
                    }
                }).create().show();
	}

	private ServiceNetRequest request ;
	/**
	 * 使用中奖凭证
	 */
	private void useVoucher() {
		if(request==null){
			request = new ServiceNetRequest(mContext);
		}
		request.useVoucher(voucher_index, new RequestCallBack<UseVoucherEntity>() {
			@Override
			public void onSuccess(ResponseInfo<UseVoucherEntity> responseInfo) {
				UseVoucherEntity result = responseInfo.result;
				switch (result.code){
					case 1:
						showToast(R.string.use_succeed);
						finish();
						break;
					case -3:
						showToast(R.string.system_busy);
						break;
					default:
						showToast("返回码:"+result.code);
						break;

				}
			}

			@Override
			public void onFailure(HttpException e, String s) {
				showToast(R.string.net_work_not_available);
			}
		});
	}
}
