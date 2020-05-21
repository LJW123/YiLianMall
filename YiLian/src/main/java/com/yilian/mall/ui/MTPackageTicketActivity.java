package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.adapter.MTPackageTicketsAdapter;
import com.yilian.mall.entity.MTCodesEntity;
import com.yilian.mall.entity.MTOrderListEntity;
import com.yilian.mall.entity.MTPaySuccessEntity;
import com.yilian.mall.entity.ShowMTPackageTicketEntity;
import com.yilian.mall.utils.DateUtils;
import com.yilian.mall.utils.NumberFormat;
import com.yilian.mylibrary.QRCodeUtils;
import com.yilian.mall.widgets.NoScrollListView;

import java.util.ArrayList;

/**
 * 套餐票据  套餐券
 */
public class MTPackageTicketActivity extends BaseActivity implements View.OnClickListener {


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
    private MTOrderListEntity.DataBean mtOrderEntity;
    LinearLayout ticketDialog;
    ImageView iv_QRcode, iv_close;
    private String subQrCode = "";
    private Button btnOrderDetail;
    private MTPaySuccessEntity mtPaySuccessEntity;
    private ShowMTPackageTicketEntity showMTPackageTicketEntity;
    private ArrayList<String> successCodeList = new ArrayList<>();//存储当前是可用的券码的集合，用来生成总码


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtpackage_ticket);

        initView();
        initData();
        initListener();
    }

    private void initView() {
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
        ticketDialog = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.dialog_ticket, null);
        iv_QRcode = (ImageView) ticketDialog.findViewById(R.id.iv_qrCode);
        iv_close = (ImageView) ticketDialog.findViewById(R.id.iv_close);

        //        对title重新赋值
        ivLeft1.setImageResource(R.mipmap.v3back);
        ivTitle.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.package_mt_qr_code));
        tvTitle.setVisibility(View.VISIBLE);
        ivRight2.setVisibility(View.GONE);

        ivLeft1.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        btnOrderDetail.setOnClickListener(this);
    }

    String packageName;
    String usableTime;
    ArrayList<MTCodesEntity> codesArray;
    String packageOrderId;

    private void initData() {

        showMTPackageTicketEntity = (ShowMTPackageTicketEntity) getIntent().getSerializableExtra("ShowMTPackageTicketEntity");
        Logger.i("接收到的showMTPackageTicketEntity：" + showMTPackageTicketEntity.toString());
        packageName = showMTPackageTicketEntity.packageName;
        usableTime = showMTPackageTicketEntity.usableTime;
        codesArray = showMTPackageTicketEntity.codes;
        packageOrderId = showMTPackageTicketEntity.packageOrderId;
        tvMtPackageName.setText(packageName);//套餐名称
        tvEndingTime.setText(DateUtils.formatDate(NumberFormat.convertToLong(usableTime, 0L)));//有效期
        ArrayList<MTCodesEntity> codes = codesArray;
        if (codes != null) {
            nslvPackageSubQrCode.setAdapter(new MTPackageTicketsAdapter(mContext, codes, showMTPackageTicketEntity.isDelivery));
            successCodeList.clear();
            for (int i = 0, count = codes.size(); i < count; i++) {
                MTCodesEntity mtCodesEntity = codes.get(i);
                if ("1".equals(mtCodesEntity.status)) { //只有未使用的才显示到二维码里面
//                    if (i < count - 1) {
//                        Logger.i("mtCodesEntity.code:" + mtCodesEntity.code);
//                        subQrCode += mtCodesEntity.code + ",";
//                    } else {
//                        Logger.i("mtCodesEntity.code:" + mtCodesEntity.code);
//                        subQrCode += mtCodesEntity.code;
//                    }
                    //取出来当前可用的券码
                    successCodeList.add(mtCodesEntity.code);
                }
            }

            for (int i = 0; i < successCodeList.size(); i++) {
                if (i < successCodeList.size() - 1) {
                    subQrCode += successCodeList.get(i) + ",";
                } else {
                    subQrCode += successCodeList.get(i);
                }
            }

        }
        /**
         * APP套餐二维码 （0,0,10,0,0,0）
         *
         0:lefen
         1:固定值10，代表APP套餐二维码类型
         2:不带颜值的token
         3:套餐订单id
         4:券码，多个券码以0X间隔
         */
        Logger.i("subQrCode:" + subQrCode);
        subQrCode = QRCodeUtils.getInstance(mContext).getMTPackageTicketQRCodePrefix(packageOrderId) + subQrCode;
        QRCodeUtils.createImage(subQrCode, ivTotalQrCode);
    }

    PopupWindow popupWindow;

    private void initListener() {
        iv_close.setOnClickListener(this);
        nslvPackageSubQrCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MTCodesEntity mtCodesEntity = (MTCodesEntity) parent.getItemAtPosition(position);//生成二维码的字符串 即：券码
                if (null != popupWindow && popupWindow.isShowing()) {
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
                String qrString = QRCodeUtils.getInstance(mContext).getMTPackageTicketQRCodePrefix(packageOrderId) + mtCodesEntity.code;
                QRCodeUtils.createImage(qrString, iv_QRcode);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_left1:
                finish();
                break;
            case R.id.tv_right:

                break;
            case R.id.btn_order_detail:
//                跳转到订单详情
                Intent intent = new Intent(this, MTOrderDetailActivity.class);
                intent.putExtra("index_id", packageOrderId);
                startActivity(intent);
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

}
