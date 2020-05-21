package com.yilian.mall.ctrip.widget.dropdownmenu;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.yilian.mall.R;
import com.yilian.mall.ctrip.activity.HotelQueryActivity;
import com.yilian.mall.ctrip.bean.MenuListBean;

public class DropDownMenu extends LinearLayout {
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //底部容器，包含popupMenuViews，maskView
    private FrameLayout containerView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position;
    //分割线颜色
    private int dividerColor;
    //tab选中颜色
    private int textSelectedColor;
    //tab未选中颜色
    private int textUnselectedColor;
    //遮罩颜色
    private int maskColor;
    //tab字体大小
    private int menuTextSize;
    //tab选中图标
    private int menuSelectedIcon;
    //tab未选中图标
    private int menuUnselectedIcon;
    List<MenuListBean> thisMenuList = new ArrayList<>();

    public DropDownMenu(Context context) {
        super(context, (AttributeSet) null);
        this.current_tab_position = -1;
        this.dividerColor = -3355444;
        this.textSelectedColor = -7795579;
        this.textUnselectedColor = -15658735;
        this.maskColor = -2004318072;
        this.menuTextSize = 14;
    }

    public DropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("WrongConstant")
    public DropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.current_tab_position = -1;
        this.dividerColor = -3355444;
        this.textSelectedColor = -7795579;
        this.textUnselectedColor = -15658735;
        this.maskColor = -2004318072;
        this.menuTextSize = 14;
        this.setOrientation(1);
        //为DropDownMenu添加自定义属性
        int menuBackgroundColor = -1;
        int underlineColor = -3355444;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenu);
        underlineColor = a.getColor(R.styleable.DropDownMenu_ddunderlineColor, underlineColor);
        this.dividerColor = a.getColor(R.styleable.DropDownMenu_dddividerColor, this.dividerColor);
        this.textSelectedColor = a.getColor(R.styleable.DropDownMenu_ddtextSelectedColor, this.textSelectedColor);
        this.textUnselectedColor = a.getColor(R.styleable.DropDownMenu_ddtextUnselectedColor, this.textUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.DropDownMenu_ddmenuBackgroundColor, menuBackgroundColor);
        this.maskColor = a.getColor(R.styleable.DropDownMenu_ddmaskColor, this.maskColor);
        this.menuTextSize = a.getDimensionPixelSize(R.styleable.DropDownMenu_ddmenuTextSize, this.menuTextSize);
        this.menuSelectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuSelectedIcon, this.menuSelectedIcon);
        this.menuUnselectedIcon = a.getResourceId(R.styleable.DropDownMenu_ddmenuUnselectedIcon, this.menuUnselectedIcon);
        a.recycle();
        //初始化tabMenuView并添加到tabMenuView
        this.tabMenuView = new LinearLayout(context);
        LayoutParams params = new LayoutParams(-1, -2);
        this.tabMenuView.setOrientation(0);
        this.tabMenuView.setBackgroundColor(menuBackgroundColor);
        this.tabMenuView.setLayoutParams(params);
        this.addView(this.tabMenuView, 0);

        //为tabMenuView添加下划线
        View underLine = new View(this.getContext());
        underLine.setLayoutParams(new LayoutParams(-1, this.dpTpPx(1.0F)));
        underLine.setBackgroundColor(underlineColor);
        this.addView(underLine, 1);

        //初始化containerView并将其添加到DropDownMenu
        this.containerView = new FrameLayout(context);
        this.containerView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
        this.addView(this.containerView, 2);
    }

    /**
     * 初始化DropDownMenu
     */
    @SuppressLint("WrongConstant")
    public void setDropDownMenu(@NonNull List<MenuListBean> menuList, @NonNull List<View> popupViews, @NonNull View contentView) {
        thisMenuList = menuList;
        if (menuList.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        } else {
            int i;
            for (i = 0; i < menuList.size(); ++i) {
                this.addTab(menuList, i);
            }
            this.containerView.addView(contentView, 0);
            this.maskView = new View(this.getContext());
            this.maskView.setLayoutParams(new android.widget.FrameLayout.LayoutParams(-1, -1));
            this.maskView.setBackgroundColor(this.maskColor);
            this.maskView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    DropDownMenu.this.closeMenu();
                }
            });
            this.containerView.addView(this.maskView, 1);
            this.maskView.setVisibility(8);
            this.popupMenuViews = new FrameLayout(this.getContext());
            this.popupMenuViews.setVisibility(8);
            this.containerView.addView(this.popupMenuViews, 2);

            for (i = 0; i < popupViews.size(); ++i) {
                ((View) popupViews.get(i)).setLayoutParams(new android.view.ViewGroup.LayoutParams(-1, -2));
                this.popupMenuViews.addView((View) popupViews.get(i), i);
            }
        }
    }

    private void addTab(@NonNull List<MenuListBean> menuList, int i) {
        final TextView tab = new TextView(this.getContext());
        tab.setSingleLine();
        tab.setEllipsize(TextUtils.TruncateAt.END);
        tab.setGravity(17);
        tab.setTextSize(0, (float) this.menuTextSize);
        tab.setLayoutParams(new LayoutParams(0, -2, 1.0F));
        tab.setTextColor(this.textUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.getResources().getDrawable(this.menuUnselectedIcon), (Drawable) null);
        tab.setText((CharSequence) menuList.get(i).getTitle());
        tab.setPadding(this.dpTpPx(5.0F), this.dpTpPx(12.0F), this.dpTpPx(5.0F), this.dpTpPx(12.0F));
        tab.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                DropDownMenu.this.switchMenu(tab);
            }
        });
        this.tabMenuView.addView(tab);
        if (i < menuList.size() - 1) {
            View view = new View(this.getContext());
            view.setLayoutParams(new LayoutParams(this.dpTpPx(0.5F), -1));
            view.setBackgroundColor(this.dividerColor);
            this.tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (this.current_tab_position != -1) {
            ((TextView) this.tabMenuView.getChildAt(this.current_tab_position)).setText(text);
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < this.tabMenuView.getChildCount(); i += 2) {
            this.tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    public void reSetTabCheck() {
        int index = 0;
        for (int i = index; i < tabMenuView.getChildCount(); i += 2) {
            for (int j = index; j < thisMenuList.size(); j++) {
                if (thisMenuList.get(j).isCheck() == true) {
                    ((TextView) this.tabMenuView.getChildAt(i)).setTextColor(this.textSelectedColor);
                    ((TextView) this.tabMenuView.getChildAt(i)).setText(thisMenuList.get(j).getTitle());
                } else {
                    ((TextView) this.tabMenuView.getChildAt(i)).setTextColor(this.textUnselectedColor);
                    ((TextView) this.tabMenuView.getChildAt(i)).setText(thisMenuList.get(j).getTitle());
                }
                index ++;
                break;
            }
        }
    }

    /**
     * 关闭菜单
     */
    @SuppressLint("WrongConstant")
    public void closeMenu() {
        if (this.current_tab_position != -1) {
            ((TextView) tabMenuView.getChildAt(current_tab_position)).setTextColor(textUnselectedColor);
            ((TextView) this.tabMenuView.getChildAt(this.current_tab_position)).setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.getResources().getDrawable(this.menuUnselectedIcon), (Drawable) null);
            this.popupMenuViews.setVisibility(8);
            this.popupMenuViews.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_menu_out));
            this.maskView.setVisibility(8);
            this.maskView.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_mask_out));
            this.current_tab_position = -1;
            reSetTabCheck();
//            HotelQueryActivity.keywordSearch_ResetMenuUnSelect();
        }
    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return this.current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    @SuppressLint("WrongConstant")
    private void switchMenu(View target) {
        System.out.println(this.current_tab_position);
        for (int i = 0; i < this.tabMenuView.getChildCount(); i += 2) {
            if (target == this.tabMenuView.getChildAt(i)) {
//              如果重复选中当前tab   关闭popuWindow
                if (this.current_tab_position == i) {
                    this.closeMenu();
                } else {
                    reSetTabCheck();
                    if (this.current_tab_position == -1) {
                        this.popupMenuViews.setVisibility(0);
                        this.popupMenuViews.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_menu_in));
                        this.maskView.setVisibility(0);
                        this.maskView.setAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.dd_mask_in));
                        this.popupMenuViews.getChildAt(i / 2).setVisibility(0);
                    } else {
                        this.popupMenuViews.getChildAt(i / 2).setVisibility(0);
                    }
                    this.current_tab_position = i;
                    ((TextView) this.tabMenuView.getChildAt(i)).setTextColor(this.textSelectedColor);
                    ((TextView) this.tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.getResources().getDrawable(this.menuSelectedIcon), (Drawable) null);
                }
            } else {
//                reSetTabCheck();
//                ((TextView) this.tabMenuView.getChildAt(i)).setTextColor(this.textUnselectedColor);
                ((TextView) this.tabMenuView.getChildAt(i)).setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.getResources().getDrawable(this.menuUnselectedIcon), (Drawable) null);
                this.popupMenuViews.getChildAt(i / 2).setVisibility(8);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        return (int) ((double) TypedValue.applyDimension(1, value, dm) + 0.5D);
    }
}
