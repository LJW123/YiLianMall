package com.yilian.mylibrary.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import com.yilian.mylibrary.R;



/**
 * Created by Ray_L_Pain on 2016/10/27 0027.
 */

public class PopupMenu extends PopupWindow implements View.OnClickListener {

    private Activity activity;
    private View popView;

    private View message;
    private View home;
    private View share;
    private View line;
    private View user;

    private OnItemClickListener onItemClickListener;

    public enum MENUITEM {
        ITEM1, ITEM2, ITEM3, ITEM4
    }

    public PopupMenu(Activity activity) {
        super(activity);
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.library_module_popup_menu, null);// 加载菜单布局文件
        this.setContentView(popView);// 把布局文件添加到popupwindow中
        this.setWidth(dip2px(activity, 140));// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);// 获取焦点
        this.setTouchable(true); // 设置PopupWindow可触摸
        this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        ColorDrawable dw = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(dw);

        // 获取选项卡
        message = popView.findViewById(R.id.layout_message);
        home = popView.findViewById(R.id.layout_home);
        share = popView.findViewById(R.id.layout_share);
        line = popView.findViewById(R.id.view_line);
        user = popView.findViewById(R.id.layout_user);
        // 添加监听
        message.setOnClickListener(this);
        home.setOnClickListener(this);
        share.setOnClickListener(this);
        user.setOnClickListener(this);
    }

    /**
     * @param activity
     * @param showShare 是否显示分享
     */
    public PopupMenu(Activity activity, boolean showShare) {
        super(activity);
        this.activity = activity;
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        popView = inflater.inflate(R.layout.library_module_popup_menu, null);// 加载菜单布局文件
        this.setContentView(popView);// 把布局文件添加到popupwindow中
        this.setWidth(dip2px(activity, 140));// 设置菜单的宽度（需要和菜单于右边距的距离搭配，可以自己调到合适的位置）
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);// 获取焦点
        this.setTouchable(true); // 设置PopupWindow可触摸
        this.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
        ColorDrawable dw = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(dw);

        // 获取选项卡
        message = popView.findViewById(R.id.layout_message);
        home = popView.findViewById(R.id.layout_home);
        share = popView.findViewById(R.id.layout_share);
        line = popView.findViewById(R.id.view_line);
        user = popView.findViewById(R.id.layout_user);
        if (showShare) {
            share.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        } else {
            share.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        // 添加监听
        message.setOnClickListener(this);
        home.setOnClickListener(this);
        share.setOnClickListener(this);
        user.setOnClickListener(this);
    }

    /**
     * 设置显示的位置
     * 这里的x,y值自己调整可以
     */
    public void showLocation(int resourId) {
        showAsDropDown(activity.findViewById(resourId), dip2px(activity, -110),
                dip2px(activity, 3));
    }

    @Override
    public void onClick(View v) {
        MENUITEM menuitem = null;
        String str = "";
        if (v == message) {
            menuitem = MENUITEM.ITEM1;
            str = "消息";
        } else if (v == home) {
            menuitem = MENUITEM.ITEM2;
            str = "首页";
        } else if (v == share) {
            menuitem = MENUITEM.ITEM3;
            str = "分享";
        } else if (v == user) {
            menuitem = MENUITEM.ITEM4;
            str = "我的";
        }
        if (onItemClickListener != null) {
            onItemClickListener.onClick(menuitem, str);
        }
        dismiss();
    }

    // dip转换为px
    public int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 点击监听接口
    public interface OnItemClickListener {
        public void onClick(MENUITEM item, String str);
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
