package com.yilian.mall.jd.dialog;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vondear.rxtools.RxDeviceTool;
import com.yilian.mall.R;


/**
 * Created by Zg on 2017/8/30.
 */
public class SpecPopwindow extends PopupWindow {

    private Context mContext;
    private View view;

    private ImageView ll_close;

    private ImageView sku_img;
    private TextView sku_price, sku_num;
    private TextView seckill_price;


    //修改数量
    public TextView num_current;//当前数量
    private TextView num_sub, num_add;
    public LinearLayout add_cart;
    public TextView add_cart_tv;


    public SpecPopwindow(Context mContext) {

        this.view = LayoutInflater.from(mContext).inflate(R.layout.jd_popupwindow_spec, null);
        this.mContext = mContext;

        num_current = view.findViewById(R.id.num_current);
        num_sub = view.findViewById(R.id.num_sub);
        num_add = view.findViewById(R.id.num_add);
        add_cart = view.findViewById(R.id.add_cart);




        ll_close = view.findViewById(R.id.ll_close);
        // 取消按钮
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 销毁弹出框
                dismiss();
            }
        });


        // 设置外部可点击
        this.setOutsideTouchable(true);
        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        this.view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.pop_layout).getTop();

                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });


    /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        DisplayMetrics dm = RxDeviceTool.getDisplayMetrics(mContext);
        this.setHeight((int) (dm.heightPixels * 0.7));
//        this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);

        // 设置弹出窗体可点击
        this.setFocusable(true);

        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // 设置弹出窗体的背景
        this.setBackgroundDrawable(new BitmapDrawable());

        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.popup_window_anim);

        setSelectNum();
    }


    public void setSelectNum() {
//        num_sub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int num = Integer.parseInt(num_current.getText().toString());
//                num -= 1;
//                if (num < 1) {
//                    Toast.makeText(mContext, "物品数量最小为1", Toast.LENGTH_SHORT).show();
//                    num = 1;
//                }
//                num_current.setText(String.valueOf(num));
//                activity.imputedPrice(num);
//            }
//        });
//        num_add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int num = Integer.parseInt(num_current.getText().toString());
//                num += 1;
//                num_current.setText(String.valueOf(num));
//                activity.imputedPrice(num);
//            }
//        });
    }


//    private void imputedPrice(int num) {//计算价格
//        String str = "";
//        for (SpecSelectBean selected : activity.specSelectList) {
//            if (str.equals("")) {
//                str += "\"" + selected.getChildID() + "\"";
//            } else {
//                str += ",\"" + selected.getChildID() + "\"";
//            }
//        }
//        String selectTypeStr = "[" + str + "]";
////        Toast.makeText(this, selectTypeStr, Toast.LENGTH_SHORT).show();
//        for (SkuBean sku : activity.skuList) {
//            if (sku.getAttributeList().equals(selectTypeStr)) {
////                Toast.makeText(this, String.valueOf(map.get("SellPrice")), Toast.LENGTH_SHORT).show();
//                activity.skuID = sku.getID();
//
//            }
//        }
//    }
}