package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.http.AfterSaleNetRequest;
import com.yilian.mall.http.MallNetRequest;
import com.yilian.mylibrary.RegExUtils;

/**
 * 提交物流信息
 * Created by Administrator on 2016/2/17.
 */
public class CommitExpressNumActivity extends BaseActivity {

    String express_num;
    private TextView tvBack;
    private EditText expressNum;
    private TextView expressCom;
    private MallNetRequest mallNetRequest;
    private int orderType;
    private String orderId;
    private String express_com;
    private AfterSaleNetRequest netRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commit_express_num);
        tvBack = (TextView) findViewById(R.id.tv_back);
        expressNum = (EditText) findViewById(R.id.ed_express_num);
        expressCom = (TextView) findViewById(R.id.tv_express_com);

        tvBack.setText("提交快递单号");

        mallNetRequest = new MallNetRequest(mContext);

        orderType = getIntent().getIntExtra("order_type", 0);
        orderId = getIntent().getStringExtra("order_id");

    }

    public void selcetExpressCom(View view) {

        Intent intent = new Intent(this, SelectExpressComActivity.class);
        startActivityForResult(intent, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case RESULT_OK:
                String ExpressComResult = data.getExtras().getString("express_com_result");
                expressCom.setText(ExpressComResult);
                express_com = data.getExtras().getString("express_id");
                break;
        }

    }

    public void commit(View view) {
        if (!RegExUtils.isExpressNum(expressNum.getText().toString().trim())) {
            showToast("快递单号为8-18位字符，支持字母、数字");
            return;
        }
        if (TextUtils.isEmpty(expressCom.getText())) {
            showToast("请选择快递公司");
            return;
        }


        express_num = expressNum.getText().toString();

        showDialog(null, "是否确认提交快递信息？", null, 0, Gravity.CENTER, "确定",
                "取消", false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                dialog.dismiss();
                                setExpress();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                }, mContext);
    }

    public void setExpress() {
        startMyDialog();
        mallNetRequest.setExpress(orderType == 1 ? 0 : 1, orderId, express_com, express_num, new RequestCallBack<BaseEntity>() {
            @Override
            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        showToast("提交成功");
                        setResult(RESULT_OK);
                        finish();
                        break;
                    default:
                        stopMyDialog();
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });


//        if (netRequest == null) {
//            netRequest = new AfterSaleNetRequest(mContext);
//        }
//        netRequest.setExpress(order_Type == 1 ? 0 : 1, orderId, express_com, express_num, new RequestCallBack<BaseEntity>() {
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                startMyDialog();
//            }
//
//            @Override
//            public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
//                stopMyDialog();
//                switch (responseInfo.result.code) {
//                    case 1:
//                        showToast("提交成功");
//                        finish();
//                        break;
//                    case 0:
//                    case -3:
//                        showToast("繁忙");
//                        break;
//
//                    case -4:
//                        showToast("登录状态失效，请重新登录");
//                        break;
//
//                }
//            }
//
//            @Override
//            public void onFailure(HttpException e, String s) {
//                stopMyDialog();
//                showToast("请检查网络连接");
//            }
//        });
    }
}
