package com.yilian.mall.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.MyIncome;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.widgets.InvateDialog;
import com.yilian.mall.widgets.JHCircleView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

/**
 * Created by Administrator on 2016/3/24.
 * 会员及领奖励
 */
public class MembersDetails extends BaseActivity {

    @ViewInject(R.id.user_photo)
    private JHCircleView userPhoto;

    @ViewInject(R.id.user_name)
    private TextView userName;

    @ViewInject(R.id.member_income)
    private TextView menberIncome;

    @ViewInject(R.id.member_count)
    private TextView memberCount;

    @ViewInject(R.id.user_level)
    private TextView userLevel;

    @ViewInject(R.id.tv_member_level_1)
    private TextView memberLevel1Count;

    @ViewInject(R.id.tv_member_level_2)
    private TextView memberLevel2Count;

    @ViewInject(R.id.tv_member_level_3)
    private TextView memberLevel3Count;

    @ViewInject(R.id.tv_income_level1)
    private TextView incomeLevel1;

    @ViewInject(R.id.tv_income_level2)
    private TextView incomeLevel2;

    @ViewInject(R.id.tv_income_level3)
    private TextView incomeLevel3;

    @ViewInject(R.id.view_line_income_level3)
    private View viewLine;
    @ViewInject(R.id.ll)
    private LinearLayout ll;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options;
    private MyIncomeNetRequest request;
    private String level;
    private String lev1Count, lev2Count, lev3Count;
    private String income;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members_details);

        ViewUtils.inject(this);

        initDate();

        setData();

//        imageLoader.destroy();
//        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true).imageScaleType(ImageScaleType.EXACTLY).cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565).build();
    }

    private void initDate() {
        startMyDialog();
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }

        request.MyIncomeNet(new RequestCallBack<MyIncome>() {
            @Override
            public void onSuccess(ResponseInfo<MyIncome> responseInfo) {

                switch (responseInfo.result.code) {
                    case 1:
                        stopMyDialog();
                        Logger.json(responseInfo.result.toString());
                        income = responseInfo.result.memberIncome;
                        if (!responseInfo.result.memberIncome.equals("0")) {
                            menberIncome.setText(String.format("%.2f", Float.parseFloat(income.toString()) / 100));
                        } else {
                            menberIncome.setText("0.00");
                        }
                        memberCount.setText(responseInfo.result.memberCount);
                        level = responseInfo.result.memberLev;
                        switch (Integer.parseInt(level)) {
                            case 1:
                                userLevel.setText("普通会员");
                                userLevel.setBackgroundResource(R.drawable.shape_level);
                                break;
                            case 2:
                                userLevel.setText("VIP会员");
                                userLevel.setBackgroundResource(R.drawable.shape_level2);
                                break;
                            case 3:
                                userLevel.setText("至尊会员");
                                userLevel.setBackgroundResource(R.drawable.shape_level3);
                                break;
                        }
                        lev1Count = responseInfo.result.memberLev1.memberCount;
                        memberLevel1Count.setText("共计(" + lev1Count + ")人");
                        incomeLevel1.setText(String.format("%.2f", Float.parseFloat(responseInfo.result.memberLev1.memberIncome.toString()) / 100) + "元");

                        lev2Count = responseInfo.result.memberLev2.memberCount;
                        memberLevel2Count.setText("共计(" + lev2Count + ")人");
                        incomeLevel2.setText(String.format("%.2f", Float.parseFloat(responseInfo.result.memberLev2.memberIncome.toString()) / 100) + "元");

                        lev3Count = responseInfo.result.memberLev3.memberCount;
//                        if (responseInfo.result.memberLev.equals("3") || responseInfo.result.memberLev.equals("2")) {
//                            ll.setVisibility(View.VISIBLE);
//                            viewLine.setVisibility(View.VISIBLE);
                            memberLevel3Count.setText("共计(" + lev3Count + ")人");
                            incomeLevel3.setText(String.format("%.2f", Float.parseFloat(responseInfo.result.memberLev3.memberIncome.toString()) / 100) + "元");
//                        } else {
//                            ll.setVisibility(View.GONE);
//                            viewLine.setVisibility(View.GONE);
//                        }
                        break;

                }
                stopMyDialog();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
            }
        });
    }

    private void setData() {

        RequestOftenKey.getUserInfor(MembersDetails.this, sp);
        String nickName = sp.getString("name", "暂无昵称");
        if (TextUtils.isEmpty(nickName)) {
            userName.setText("暂无昵称");
        } else {
            userName.setText(nickName);
        }
        if (!TextUtils.isEmpty(sp.getString("photo", ""))) {
            if (sp.getString("photo", "").contains("http://")||sp.getString("photo", "").contains("https://")) {
                url = sp.getString("photo", "");
            } else {
                url = Constants.ImageUrl + sp.getString("photo", "");
            }
            imageLoader.displayImage(url, userPhoto, options,
                    new SimpleImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String imageUri, View view) {
                        }

                        @Override
                        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                            userPhoto.setImageBitmap(loadedImage);
                        }

                        @Override
                        public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                            String message = null;
                            switch (failReason.getType()) {
                                case IO_ERROR:
                                case DECODING_ERROR:
                                case NETWORK_DENIED:
                                case OUT_OF_MEMORY:
                                case UNKNOWN:
                                    message = "图片加载错误";
                                    break;
                            }
                        }
                    });
        } else {
            userPhoto.setImageResource(R.mipmap.bind_card_photo);
        }
    }

    public void onBack(View view) {
        finish();
    }

    public void jumpToShowDialog(View view) {

        if (income.equals("0")) {
            InvateDialog dialog = new InvateDialog(mContext, "你还没有领奖励哦，赶快邀请好友吧！");
            dialog.show();
        } else {
            Intent intent = new Intent(MembersDetails.this, MembersAndGains.class);
            intent.putExtra("type", "0");
            startActivity(intent);
        }
    }

    //领奖励模式说明
    public void jumpToShouYi(View view) {
        Intent intent = new Intent(MembersDetails.this, WebViewActivity.class);
        intent.putExtra("title_name", "领奖励模式");
        intent.putExtra("url", Ip.getHelpURL() + "agreementforios/distribution.html");
        startActivity(intent);
    }

    public void jumpToLevel1(View view) {
        if (!lev1Count.equals("0")) {
            Intent intent = new Intent(MembersDetails.this, MembersLevel1.class);
            intent.putExtra("level", level);
            startActivity(intent);
        } else {
            InvateDialog dialog = new InvateDialog(mContext, "你还没有乐友哦，赶快邀请好友吧！");
            dialog.show();
        }
    }

    public void jumpToLevel2(View view) {
        if (!lev2Count.equals("0")) {
            Intent intent = new Intent(MembersDetails.this, MembersLevel2.class);
            intent.putExtra("level", level);
            startActivity(intent);
        } else {
            InvateDialog dialog = new InvateDialog(mContext, "你还没有从友哦，赶快邀请好友吧！");
            dialog.show();
        }
    }

    public void jumpToLevel3(View view) {
        if (!lev3Count.equals("0")) {
            Intent intent = new Intent(MembersDetails.this, MembersLevel3.class);
            intent.putExtra("level", level);
            startActivity(intent);
        } else {
            InvateDialog dialog = new InvateDialog(mContext, "你还没有众友哦，赶快邀请好友吧！");
            dialog.show();
        }
    }

    public void incomeGetOut(View view){
        startActivity(new Intent(this, MembersCash.class));
    }


//    /**
//     * 矩形图片转换为园形
//     *
//     * @param bitmap
//     * @return
//     */
//    public static Bitmap makeRoundCorner(Bitmap bitmap) {
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int left = 0, top = 0, right = width, bottom = height;
//        float roundPx = height / 2;
//        if (width > height) {
//            left = (width - height) / 2;
//            top = 0;
//            right = left + height;
//            bottom = height;
//        } else if (height > width) {
//            left = 0;
//            top = (height - width) / 2;
//            right = width;
//            bottom = top + width;
//            roundPx = width / 2;
//        }
//        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(output);
//        int color = 0xff000000;
//        Paint paint = new Paint();
//        Rect rect = new Rect(left, top, right, bottom);
//        RectF rectF = new RectF(rect);
//        paint.setAntiAlias(true);
//        canvas.drawARGB(0, 0, 0, 0);
//        paint.setColor(color);
//        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//        canvas.drawBitmap(bitmap, rect, rect, paint);
//        return output;
//    }

}
