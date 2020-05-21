package com.yilian.mall.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.Feedback;
import com.yilian.mall.http.ServiceNetRequest;
import com.yilian.mall.utils.CommonUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 意见反馈
 */
public class FeedbackActivity extends BaseActivity {

    @ViewInject(R.id.tv_back)
    private TextView tv_back;

    @ViewInject(R.id.spinner1)
    private Spinner spinner1;

    @ViewInject(R.id.contact)
    private EditText contact;

    @ViewInject(R.id.content)
    private EditText content;

    private ServiceNetRequest serviceNetRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ViewUtils.inject(this);
        tv_back.setText("意见反馈");
        serviceNetRequest = new ServiceNetRequest(mContext);
        setData();
    }

    /**
     * 获取反馈类型列表
     */
    private void setData() {
        contact.setHint(sp.getString("phone", ""));
        startMyDialog();
        serviceNetRequest.getFeedBackTypeList(Feedback.class, new RequestCallBack<Feedback>() {
            @Override
            public void onSuccess(ResponseInfo<Feedback> responseInfo) {
                stopMyDialog();

                switch (responseInfo.result.code) {
                    case 1:
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(FeedbackActivity.this, R.layout.feedback_spinner);

                        for (int i = 0; i < responseInfo.result.getList().size(); i++) {
                            adapter.add(responseInfo.result.getList().get(i));
                        }
                        adapter.setDropDownViewResource(R.layout.drop_down_checked_text);
                        ;
                        spinner1.setAdapter(adapter);
                        break;
                    default:

                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {
                stopMyDialog();
                showToast(R.string.net_work_not_available);
            }
        });
    }

    /**
     * 提交意见反馈
     *
     * @param view
     */
    public void pushOpinions(View view) {
        String tel;
        if (content.getText().toString().length() < 5) {
            showToast("反馈内容不应小于5个字");
            return;
        }

        if (!CommonUtils.isPhoneNumer(contact.getHint().toString())) {
            tel = contact.getText().toString();
        } else {
            tel = contact.getHint().toString();
        }

        if (!CommonUtils.isPhoneNumer(tel)) {
            showToast("联系方式有误");
            return;
        }

        startMyDialog();

        try {
            String type = URLEncoder.encode(spinner1.getSelectedItem().toString(), "UTF-8");
            String conten = URLEncoder.encode(content.getText().toString(), "UTF-8");

            serviceNetRequest.submitFeedback_v2(type, conten, tel, BaseEntity.class, new RequestCallBack<BaseEntity>() {
                @Override
                public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                    stopMyDialog();

                    switch (responseInfo.result.code) {
                        case 1:
                            showToast("谢谢您的反馈~");
                            finish();
                            break;
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    stopMyDialog();
                    showToast(R.string.net_work_not_available);
                }
            });

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
