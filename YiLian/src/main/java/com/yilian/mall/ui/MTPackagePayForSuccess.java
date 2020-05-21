package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTPackageTicketsAdapter;
import com.yilian.mall.entity.JPShareEntity;
import com.yilian.mall.entity.MTCodesEntity;
import com.yilian.mall.entity.ShowMTPackageTicketEntity;
import com.yilian.mall.http.JPNetRequest;
import com.yilian.mall.umeng.IconModel;
import com.yilian.mall.umeng.ShareUtile;
import com.yilian.mall.umeng.UmengDialog;
import com.yilian.mall.umeng.UmengGloble;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilian.mall.widgets.NoScrollListView;
import com.yilian.mylibrary.Constants;
import com.yilianmall.merchant.utils.MoneyUtil;

import java.util.ArrayList;

/**
 * 套餐支付成功
 */
public class MTPackagePayForSuccess extends BaseActivity implements View.OnClickListener {

    private TextView tvLeft1;
    private RelativeLayout rlTvLeft1;
    private ImageView ivLeft1;
    private RelativeLayout rlIvLeft1;
    private ImageView ivLeft2;
    private RelativeLayout rlIvLeft2;
    private ImageView ivTitle;
    private TextView tvTitle;
    private ImageView ivRight1;
    private ImageView ivRight2;
    private RelativeLayout rlIvRight2;
    private TextView tvRight;
    private RelativeLayout rlTvRight;
    private View viewLine;
    private LinearLayout llJpTitle;
    private TextView tvMtPackageName;
    private TextView tvEndingTime;
    private NoScrollListView nslvPackageSubQrCode;
    private ImageView ivTotalQrCode;
    private Button btnOrderDetail;
    private Button shareGetPrize;
    private LinearLayout activityMtpackagePayForSuccess;

    private UmengDialog mShareDialog;
    JPNetRequest jpRequest;
    String share_type = "10"; //1.旗舰店分享:#
    String getedId = "0";
    String share_title,share_content,share_img,share_url,shareImg;
    private TextView tvIntegral;
    private RelativeLayout rlQrCode;
    private ArrayList<String>  successCodeList=new ArrayList<>();
    private String isDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtpackage_pay_for_success);
        initView();
        initData();
        initListener();
    }

    ImageView iv_QRcode, iv_close;
    LinearLayout ticketDialog;

    private void initView() {
        rlQrCode = (RelativeLayout) findViewById(R.id.rl_qr_code);
        tvIntegral = (TextView) findViewById(R.id.tv_integral);
        tvLeft1 = (TextView) findViewById(R.id.tv_left1);
        rlTvLeft1 = (RelativeLayout) findViewById(R.id.rl_tv_left1);
        ivLeft1 = (ImageView) findViewById(R.id.iv_left1);
        rlIvLeft1 = (RelativeLayout) findViewById(R.id.rl_iv_left1);
        ivLeft2 = (ImageView) findViewById(R.id.iv_left2);
        rlIvLeft2 = (RelativeLayout) findViewById(R.id.rl_iv_left2);
        ivTitle = (ImageView) findViewById(R.id.iv_title);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        ivRight1 = (ImageView) findViewById(R.id.iv_right1);
        ivRight2 = (ImageView) findViewById(R.id.iv_right2);
        rlIvRight2 = (RelativeLayout) findViewById(R.id.rl_iv_right2);
        tvRight = (TextView) findViewById(R.id.tv_right);
        rlTvRight = (RelativeLayout) findViewById(R.id.rl_tv_right);
        viewLine = (View) findViewById(R.id.view_line);
        llJpTitle = (LinearLayout) findViewById(R.id.ll_jp_title);
        tvMtPackageName = (TextView) findViewById(R.id.tv_mt_package_price);
        tvEndingTime = (TextView) findViewById(R.id.tv_ending_time);
        nslvPackageSubQrCode = (NoScrollListView) findViewById(R.id.nslv_package_sub_qr_code);
        nslvPackageSubQrCode.setFocusable(false);
        nslvPackageSubQrCode.setFocusableInTouchMode(false);
        ivTotalQrCode = (ImageView) findViewById(R.id.iv_total_qr_code);
        btnOrderDetail = (Button) findViewById(R.id.btn_order_detail);
        shareGetPrize = (Button) findViewById(R.id.share_get_prize);
        activityMtpackagePayForSuccess = (LinearLayout) findViewById(R.id.activity_mtpackage_pay_for_success);

        ticketDialog = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_ticket, null);
        iv_QRcode = (ImageView) ticketDialog.findViewById(R.id.iv_qrCode);
        iv_close = (ImageView) ticketDialog.findViewById(R.id.iv_close);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.pay_success));
        tvTitle.setVisibility(View.VISIBLE);
        ivRight2.setVisibility(View.GONE);

        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnOrderDetail.setOnClickListener(this);
        shareGetPrize.setOnClickListener(this);
    }

    private ShowMTPackageTicketEntity showMTPackageTicketEntity;

    String packageName;
    String usableTime;
    ArrayList<MTCodesEntity> codesArray;
    String packageOrderId;
    private String subQrCode = "";

    private void initData() {


        showMTPackageTicketEntity = (ShowMTPackageTicketEntity) getIntent().getSerializableExtra("ShowMTPackageTicketEntity");

        Logger.i("接收到的showMTPackageTicketEntity：" + showMTPackageTicketEntity.toString());
        packageName = showMTPackageTicketEntity.packageName;
        usableTime = showMTPackageTicketEntity.usableTime;
        codesArray = showMTPackageTicketEntity.codes;
        packageOrderId = showMTPackageTicketEntity.packageOrderId;
        tvMtPackageName.setText(packageName);//套餐名称
        tvIntegral.setText(MoneyUtil.getLeXiangBi(showMTPackageTicketEntity.integral) + getResources().getString(R.string.integral));//赠送零购券金额
        tvEndingTime.setText(DateUtils.formatDate(NumberFormat.convertToLong(usableTime, 0L)));//有效期
        isDelivery = showMTPackageTicketEntity.isDelivery;
        ArrayList<MTCodesEntity> codes = codesArray;
        if (codes != null) {
            nslvPackageSubQrCode.setAdapter(new MTPackageTicketsAdapter(mContext,codes, showMTPackageTicketEntity.isDelivery));
            successCodeList.clear();
            for (int i = 0, count = codes.size(); i < count; i++) {
                MTCodesEntity mtCodesEntity = codes.get(i);
                if("1".equals(mtCodesEntity.status)){
                    successCodeList.add(mtCodesEntity.code);
                }
            }

            for (int i = 0; i < successCodeList.size(); i++) {
                if (i<successCodeList.size()-1){
                    subQrCode+=successCodeList.get(i)+",";
                }else {
                    subQrCode+=successCodeList.get(i);
                }
            }

            Logger.i("subQrCode     " + subQrCode);
        }
        /**
         * APP套餐二维码 （10,0,0,0）
         0:固定值10，代表APP套餐二维码类型
         1:不带颜值的token
         2:套餐订单id
         3:券码，多个券码以，间隔
         */
        Logger.i("subQrCode:" + subQrCode);
        subQrCode =  QRCodeUtils.getInstance(mContext).getMTPackageTicketQRCodePrefix(packageOrderId)+ subQrCode;
        QRCodeUtils.createImage(subQrCode, ivTotalQrCode);
        if("0".equals(showMTPackageTicketEntity.isDelivery)){
            rlQrCode.setVisibility(View.VISIBLE);
            nslvPackageSubQrCode.setVisibility(View.VISIBLE);
        } else {
            rlQrCode.setVisibility(View.GONE);
            nslvPackageSubQrCode.setVisibility(View.VISIBLE);
        }
//        getShareUrl();
    }

    PopupWindow popupWindow;

    private void initListener() {
        iv_close.setOnClickListener(this);
        if ("1".equals(isDelivery)) {
            nslvPackageSubQrCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } else {
            nslvPackageSubQrCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    MTCodesEntity mtCodesEntity = (MTCodesEntity) parent.getItemAtPosition(position);//生成二维码的字符串 即：券码
                    if (null!=popupWindow&& popupWindow.isShowing()){
                        popupWindow.dismiss();
                        return;//有二维码显示时，将老二维码dismiss掉
                    }
                    popupWindow = new PopupWindow();
                    popupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(false);
                    popupWindow.setFocusable(false);
                    popupWindow.setContentView(ticketDialog);
                    popupWindow.setAnimationStyle(R.style.merchant_AnimationFade);
//                目前还没有显示位置
//                popupWindow.showAsDropDown(llJpTitle,0,0, Gravity.CENTER);
                    popupWindow.showAtLocation(llJpTitle, Gravity.CENTER, 0, 0);  //屏幕居中显示
                    //点击背景变暗
                    ColorDrawable cd = new ColorDrawable(0x000000);
                    popupWindow.setBackgroundDrawable(cd);
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 0.4f;
                    getWindow().setAttributes(lp);

                    // TODO 显示二维码
                    QRCodeUtils.createImage(QRCodeUtils.getInstance(mContext).getMTPackageTicketQRCodePrefix(packageOrderId) +mtCodesEntity.code, iv_QRcode);

                    popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            WindowManager.LayoutParams lp = getWindow().getAttributes();
                            lp.alpha = 1f;
                            getWindow().setAttributes(lp);
                        }
                    });
                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, JPMainActivity.class);
        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.iv_left1:
                intent = new Intent(mContext, JPMainActivity.class);
                intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                startActivity(intent);
                break;
            case R.id.tv_right:

                break;
            case R.id.btn_order_detail:
                if (null!=popupWindow&&popupWindow.isShowing()){
                    popupWindow.dismiss();
                }
                intent = new Intent(this, MTOrderDetailActivity.class);
                intent.putExtra("index_id",packageOrderId);
                startActivity(intent);
//                finish();
                break;
            case R.id.share_get_prize:
                share();
                break;
            case R.id.iv_close:
                popupWindow.dismiss();
                break;
        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    /**
     * 分享
     */
    private void share(){
        if(mShareDialog == null){
            mShareDialog = new UmengDialog(mContext, AnimationUtils.loadAnimation(this, R.anim.anim_wellcome_bottom), R.style.DialogControl,
                    new UmengGloble().getAllIconModels());
            mShareDialog.setItemLister(new UmengDialog.OnListItemClickListener(){

                @Override
                public void OnItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3, Object arg4) {
                    new ShareUtile(
                            mContext,
                            ((IconModel) arg4).getType(),
                            share_title,
                            share_content,
                            shareImg,
                            share_url)
                            .share();
                }
            });
        }
        mShareDialog.show();
    }

    private void getShareUrl() {
        if (jpRequest == null) {
            jpRequest = new JPNetRequest(mContext);
        }
        Logger.i("2017-1-13:" + getedId);
        jpRequest.getShareUrl(share_type, getedId, new RequestCallBack<JPShareEntity>() {
            @Override
            public void onSuccess(ResponseInfo<JPShareEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        share_title = responseInfo.result.title;
                        String sub_title = responseInfo.result.subTitle;
                        share_content = responseInfo.result.content;
                        share_img = responseInfo.result.imgUrl;
                        share_url = responseInfo.result.url;
                        if (TextUtils.isEmpty(share_img)) {
                            shareImg = "";
                        } else {
                            if (share_img.contains("http://") || share_img.contains("https://")) {
                                shareImg = share_img;
                            } else {
                                shareImg =  Constants.ImageUrl + share_img;
                            }
                        }
                        Logger.i("share_title"+share_title);
                        Logger.i("share_content"+share_content);
                        Logger.i("share_img"+share_img);
                        Logger.i("share_url"+share_url);
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
