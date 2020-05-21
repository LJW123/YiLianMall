package com.yilian.mall.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.DetailsEntity;
import com.yilian.mall.http.MyIncomeNetRequest;
import com.yilian.mall.utils.StringFormat;
import com.yilian.mylibrary.Constants;


/**
 * 会员详情界面
 * Created by Administrator on 2016/4/18.
 */
public class DetailsActivity extends BaseActivity {

    @ViewInject(R.id.tv_level)
    private TextView tvLevel;

    @ViewInject(R.id.tv_time)
    private TextView tvTime;

    @ViewInject(R.id.tv_lefen)
    private TextView tvLefen;

    @ViewInject(R.id.tv_lexiang)
    private TextView tvLexiang;

    private MyIncomeNetRequest request;
    private String userId;

    @ViewInject(R.id.user_photo)
    private ImageView userPhoto;

    @ViewInject(R.id.user_name)
    private TextView userName;

    @ViewInject(R.id.tv_phone)
    private TextView tvPhone;


    @ViewInject(R.id.btn_send_message)
    private Button btnSendMessage;
    private BitmapUtils bitmapUtils;
    private String memberName;
    private String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ViewUtils.inject(this);
        bitmapUtils= new BitmapUtils(mContext);
        initView();

        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNum));
                startActivity(intent);
            }
        });

    }

    private void initView() {
        if (request == null) {
            request = new MyIncomeNetRequest(mContext);
        }
        startMyDialog();
        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id");
        request.DetailsNet(userId, new RequestCallBack<DetailsEntity>() {
            @Override
            public void onSuccess(ResponseInfo<DetailsEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        Logger.json(responseInfo.result.toString());
                        String imageUrl = responseInfo.result.head;
                        if (!TextUtils.isEmpty(imageUrl)){
                            Logger.i(" head  "+responseInfo.result.head+"   "+ Constants.ImageUrl+responseInfo.result.head);
                            if (imageUrl.contains("http://")||imageUrl.contains("https://")){
                                imageUrl=imageUrl+ Constants.ImageSuffix;
                            }else{
                                imageUrl=Constants.ImageUrl+imageUrl+Constants.ImageSuffix;
                            }
                            imageLoader.displayImage(imageUrl,userPhoto,options);
                        }else {
                            userPhoto.setImageResource(R.mipmap.bind_card_photo);
                        }

                        memberName = responseInfo.result.userName;
                        memberName = TextUtils.isEmpty(memberName)?"暂无昵称": memberName;
                        userName.setText(memberName);

                        tvLefen.setText(String.format("%.2f", Float.parseFloat(responseInfo.result.cash) / 100));
                        tvLexiang.setText(String.format("%.2f", Float.parseFloat(responseInfo.result.cash) / 100));
                        tvTime.setText(StringFormat.formatDate(responseInfo.result.regTime, "yyyy-MM-dd"));
                        switch (responseInfo.result.lev) {
                            case "0":
                                tvLevel.setText("普通会员");
                                break;
                            case "1":
                                tvLevel.setText("VIP会员");
                                break;
                            case "2":
                                tvLevel.setText("个体商家");
                                break;
                            case "3":
                                tvLevel.setText("实体商家");
                                break;
                            case "4":
                                tvLevel.setText("市级服务中心");
                                break;
                            case "5":
                                tvLevel.setText("省级服务中心");
                                break;
                            default:
                                break;
                        }

                        phoneNum = responseInfo.result.phone;
                        tvPhone.setText(phoneNum);

                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });
    }

    public void onBack(View view) {
        finish();
    }

}
