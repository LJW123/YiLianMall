package com.yilianmall.merchant.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.dialog.WarnCommonPopwindow;
import com.yilianmall.merchant.R;


/**
 * Created by liuyuqi on 2017/6/10 0010.
 * 商家入驻
 */
public class MerchantSettledActivity extends BaseActivity implements View.OnClickListener {
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvPersonage;
    private TextView tvPersonageContent;
    private TextView tvEntity;
    private TextView tvEntityContent;
    private LinearLayout llStorePersonage;
    private LinearLayout llStoreBrand;
    //提醒弹出
    private WarnCommonPopwindow warnCommonPopwindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.merchant_activity_merchant_settled);
        initView();
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("商家入驻");
        tvRight = (TextView) findViewById(R.id.tv_right);
        tvRight.setVisibility(View.VISIBLE);
        tvRight.setText("商家入驻协议");
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Shop.setVisibility(View.GONE);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        v3Share.setVisibility(View.GONE);
        tvPersonage = (TextView) findViewById(R.id.tv_personage);
        tvPersonageContent = (TextView) findViewById(R.id.tv_personage_content);
        tvEntity = (TextView) findViewById(R.id.tv_entity);
        tvEntityContent = (TextView) findViewById(R.id.tv_entity_content);
        llStorePersonage = (LinearLayout) findViewById(R.id.ll_store_personage);
        llStorePersonage.setOnClickListener(this);
        llStoreBrand = (LinearLayout) findViewById(R.id.ll_store_brand);
        llStoreBrand.setOnClickListener(this);
        v3Back.setOnClickListener(this);

        tvPersonage.setText(Html.fromHtml("<font color=\"#333333\">由于平台运营战略调整，个体商家申请通道关闭，已申请的个体商家继续保留现有功能</font>"));
        tvPersonageContent.setText(Html.fromHtml("入住需提供:个人身份证   " + "</font color=\"#333333\">360元/年</font>"));
        tvEntity.setText(Html.fromHtml("<font color=\"#333333\">实体店 - </font>有营业执照的实体店铺或拥有 自身品牌的商家"));
        tvEntityContent.setText(Html.fromHtml("入住需提供:营业执照   </font color=\"#333333\">999元/年</font>"));
        tvRight.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();

        if (i == R.id.v3Back) {
            finish();
        } else if (i == R.id.ll_store_brand) {
            warnCommonPopwindow();
            //实体 保存当前选择的支付类实体
            Intent intent = new Intent(mContext, MerchantPayActivity.class);
            intent.putExtra(Constants.MERCHANT_STATUS_FLAG, "3");
            intent.putExtra("merchantPayType", "0");//判断当前是缴费
            startActivity(intent);
//            finish();
        } else if (i == R.id.ll_store_personage) {
            //个体
//            Intent intent=new Intent(mContext,MerchantPayActivity.class);
//            intent.putExtra(Constants.MERCHANT_STATUS_FLAG,"2");
//            intent.putExtra("merchantPayType","0");//判断当前是缴费
//            startActivity(intent);
//            finish();
        } else if (i == R.id.tv_right) {
            startActivity(new Intent(mContext, MerchantAgreementActivity.class));
        }
    }


    private void warnCommonPopwindow() {
        if (warnCommonPopwindow == null) {
            warnCommonPopwindow = new WarnCommonPopwindow(mContext);
            String content = "商家入驻流程调整中,暂停使用;如需提交入驻申请,请联系当地益联益家运营中心,或直接拨打益联益家客服电话:";
            final String phone = "400-152-5159";

            SpannableString spannableString = new SpannableString(content + phone);
            spannableString.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    Uri data = Uri.parse("tel:" + phone);
                    intent.setData(data);
                    startActivity(intent);
                }

                @Override
                public void updateDrawState(TextPaint ds) {
                    super.updateDrawState(ds);
                    ds.setUnderlineText(false);
                    ds.setColor(Color.parseColor("#17A5F0")); //设置颜色
                }
            }, spannableString.length() - phone.length(), spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            warnCommonPopwindow.setContent(spannableString);
            warnCommonPopwindow.confirm(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    warnCommonPopwindow.dismiss();
                }
            });
        }
        warnCommonPopwindow.showPop(llStoreBrand);
    }
}
