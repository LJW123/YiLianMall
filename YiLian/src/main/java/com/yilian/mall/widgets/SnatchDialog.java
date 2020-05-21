package com.yilian.mall.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.entity.BaseEntity;
import com.yilian.mall.entity.SnatchPartEntity;
import com.yilian.mall.http.SnatchNetRequest;
import com.yilian.mall.ui.RechargeActivity;
import com.yilian.mall.utils.MoneyUtil;
import com.yilian.mall.utils.Toast;
import com.yilian.mall.widgets.wheel.adapters.AbstractWheelTextAdapter;
import com.yilian.mall.widgets.wheel.views.OnWheelChangedListener;
import com.yilian.mall.widgets.wheel.views.OnWheelScrollListener;
import com.yilian.mall.widgets.wheel.views.WheelView;
import com.yilian.mylibrary.Constants;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2016/4/7.
 */


public class SnatchDialog extends Dialog implements View.OnClickListener, OnWheelChangedListener {

    private String activity_expend;
    private int count;
    private String goodsName;
    private Context context;
    private String activityId;

    private TextView mTv;

    private TextView tvNumberL, tvNumberC, tvNumberR;

    //数字控件
    private WheelView wvLeft;
    private WheelView wvCenter;
    private WheelView wvRight;

    //数字集合
    private List<String> list = new ArrayList<String>();

    //选中的数字信息
    private String strLeft;
    private String strCenter;
    private String strRight = "1";

    private TextView btnSure;//确定按钮
    private ImageView btnCancle;//取消按钮
    private TextView btnRandom;//随机

    //回调函数
    private OnSnatchCListener onSnatchCListener;

    private NumberAdapter adapter;

    //显示文字的字体大小
    private int maxsize = 26;
    private int minsize = 18;
    private LinearLayout ll;

    private SnatchNetRequest request;
    private Handler handler;


    public SnatchDialog(Context context, String activityId, String goodsName, int count, Handler handler) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.activityId = activityId;
        this.goodsName = goodsName;
        this.count = count;
        this.handler = handler;
    }

    public SnatchDialog(Context context, String activityId, String goodsName, int count) {
        super(context, R.style.ShareDialog);
        this.context = context;
        this.activityId = activityId;
        this.goodsName = goodsName;
        this.count = count;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_snatch);

        initView();

    }

    private void initView() {
        mTv = (TextView) findViewById(R.id.tv);
        ll = (LinearLayout) findViewById(R.id.ll);

        if (request == null) {
            request = new SnatchNetRequest(context);
        }
        request.snatchingNet(activityId, 0, new RequestCallBack<SnatchPartEntity>() {
            @Override
            public void onSuccess(ResponseInfo<SnatchPartEntity> responseInfo) {
                switch (responseInfo.result.code) {
                    case 1:
                        activity_expend = responseInfo.result.activity.activity_expend;
                        mTv.setText("参与需要消耗" + MoneyUtil.set¥Money(String.format("%.2f", Float.parseFloat(activity_expend) / 100)));
                        break;
                }
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });


        tvNumberL = (TextView) findViewById(R.id.tv_number1);
        tvNumberC = (TextView) findViewById(R.id.tv_number2);
        tvNumberR = (TextView) findViewById(R.id.tv_number3);
        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                context.startActivity(new Intent(context, RechargeActivity.class));


//                Intent intent = new Intent(context, NMemberChargeActivity.class);
//                intent.putExtra("type", "chooseCharge");
//                context.startActivity(intent);
            }
        });

        for (int i = 0; i < 10; i++) {
            list.add(i + "");
        }

        wvLeft = (WheelView) findViewById(R.id.wv_snatch_left);
        wvCenter = (WheelView) findViewById(R.id.wv_snatch_center);
        wvRight = (WheelView) findViewById(R.id.wv_snatch_right);
        btnSure = (TextView) findViewById(R.id.tv_sure);
        btnCancle = (ImageView) findViewById(R.id.img_cancel);
        btnRandom = (TextView) findViewById(R.id.tv_random);

        btnSure.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
        btnRandom.setOnClickListener(this);

        wvLeft.addChangingListener(this);
        wvCenter.addChangingListener(this);
        wvRight.addChangingListener(this);

        wvLeft.setCyclic(true);
        wvRight.setCyclic(true);
        wvCenter.setCyclic(true);

        wvLeft.setVisibleItems(3);

        wvCenter.setVisibleItems(3);

        wvRight.setVisibleItems(3);

        wvLeft.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) adapter.getItemText(wheel.getCurrentItem());
                strLeft = (String) adapter.getItemObject(wheel.getCurrentItem());
                setTextViewSize(strLeft, adapter);
                tvNumberL.setText(strLeft);
            }
        });
        wvCenter.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) adapter.getItemText(wheel.getCurrentItem());
                strCenter = (String) adapter.getItemObject(wheel.getCurrentItem());
                setTextViewSize(strCenter, adapter);
                tvNumberC.setText(strCenter);
            }
        });
        wvRight.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) adapter.getItemText(wheel.getCurrentItem());
                strRight = (String) adapter.getItemObject(wheel.getCurrentItem());
                setTextViewSize(strRight, adapter);
                tvNumberR.setText(strRight);
            }
        });
        /**
         * 设置适配器
         */
        adapter = new NumberAdapter(context, list, 0, maxsize, minsize);
        wvLeft.setViewAdapter(adapter);
        wvLeft.setCurrentItem(0);

        wvCenter.setViewAdapter(adapter);
        wvCenter.setCurrentItem(0);
        wvRight.setViewAdapter(adapter);
        wvRight.setCurrentItem(1);

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {

    }

    @Override
    public void onClick(View v) {

        if (v == btnSure) {
            if (onSnatchCListener != null) {
                onSnatchCListener.onClick(strLeft, strCenter, strRight);
            }
            if (strLeft == null) {
                strLeft = "0";
            }
            if (strCenter == null) {
                strCenter = "0";
            }
            if (strRight == null) {
                strRight = "0";
            }
            if ((strLeft + strCenter + strRight).equals("000")) {
                Toast.makeText(context, "不能为0", Toast.LENGTH_SHORT).show();
            } else {
//                dismiss();
                Logger.i(strLeft + strCenter + strRight);
                if (Integer.parseInt(strLeft + strCenter + strRight) > 0 && Integer.parseInt(strLeft + strCenter + strRight) <= 999) {

                    request.SnatchPayNet(activityId, strLeft + strCenter + strRight, "123456", new RequestCallBack<BaseEntity>() {
                        @Override
                        public void onSuccess(ResponseInfo<BaseEntity> responseInfo) {
                            Logger.json(responseInfo.result.toString());
                            switch (responseInfo.result.code) {
                                case 1:
                                    Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();

                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Message message = new Message();
                                            message.obj = Constants.ACTIVITY_STATE_END;
                                            message.arg1 = Constants.EXECUTE_SUCCESS;
                                            handler.sendMessage(message);
                                        }
                                    }).start();

                                    dismiss();
                                    break;
                                case -5:
//                                pwdView.clearPassword();
//                                Toast.makeText(mContext, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                                    break;
                                case -13:
                                    Toast.makeText(context, "奖励不足，请充值", Toast.LENGTH_SHORT).show();
                                    dismiss();
//                                    Intent intent = new Intent(context, NMemberChargeActivity.class);
//                                    intent.putExtra("type", "chooseCharge");
//                                    context.startActivity(intent);

                                    context.startActivity(new Intent(context, RechargeActivity.class));

                                    break;
                                case -3:
                                    Toast.makeText(context, "操作频繁，歇一歇吧", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                    break;
                            }
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Logger.i(e.getExceptionCode() + "|" + s);
                            Toast.makeText(context, "请检查网络", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

//                SnatchPayDialog dialog=new SnatchPayDialog(mContext,goodsName,strLeft,strCenter,strRight,activityId,count,activity_expend,handler);
//                dialog.show();

                //软键盘弹出
//                InputMethodManager imm = (InputMethodManager)mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(dialog.get, InputMethodManager.RESULT_SHOWN);

            }
        }
        if (v == btnCancle) {
            dismiss();
        }
        if (v == btnRandom) {
            int a = (int) (Math.random() * 5000 + 1);
            int b = (int) (Math.random() * 5000 + 1);
            int c = (int) (Math.random() * 5000 + 1);
            wvLeft.scroll(a, 500);
            wvRight.scroll(b, 500);
            wvCenter.scroll(c, 500);

        }
    }

    public void setTextViewSize(String curriteItemText, NumberAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textview = (TextView) arrayList.get(i);
            currentText = textview.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textview.setTextSize(maxsize);
            } else {
                textview.setTextSize(minsize);
            }
        }
    }

    public void setOnSnatchCListener(OnSnatchCListener onSnatchCListener) {
        this.onSnatchCListener = onSnatchCListener;
    }

    public interface OnSnatchCListener {
        void onClick(String strLeft, String strCenter, String strRight);
    }

    //适配器
    public class NumberAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected NumberAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_birth_year, NO_RESOURCE, currentItem, maxsize, minsize);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        protected CharSequence getItemText(int index) {
            if (list != null && list.size() > 0) {
                return list.get(index);
            }
            return "";
        }

        @Override
        protected Object getItemObject(int index) {
            if (list != null && list.size() > 0) {
                return list.get(index);
            }
            return null;
        }

        @Override
        public int getItemsCount() {
            if (list != null) {
                return list.size();
            }
            return 0;
        }
    }

}
