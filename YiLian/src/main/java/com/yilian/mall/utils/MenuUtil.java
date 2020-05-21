package com.yilian.mall.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import com.yilian.luckypurchase.widget.PopupMenu;
import com.yilian.mylibrary.Constants;


/**
 * @author Created by  on 2017/11/21 0021.
 */

public class MenuUtil {
    /**
     * 右上角更多
     */
    public static void showMenu(Activity context, int showLocation) {
        final com.yilian.luckypurchase.widget.PopupMenu popupMenu = new PopupMenu(context, false);
        popupMenu.showLocation(showLocation);
        popupMenu.setOnItemClickListener(new PopupMenu.OnItemClickListener() {
            @Override
            public void onClick(PopupMenu.MENUITEM item, String str) {
                Intent intent = new Intent();
                switch (item) {
                    case ITEM1:
                        if (PreferenceUtils.readBoolConfig(Constants.SPKEY_STATE, context)) {
                            intent.setComponent(new ComponentName(context, "com.yilian.mall.ui.InformationActivity"));
                        } else {
                            intent.setComponent(new ComponentName(context, "com.yilian.loginmodule.LeFenPhoneLoginActivity"));
                        }
                        break;
                    case ITEM2:
                        intent.setComponent(new ComponentName(context, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_MT__HOME_FRAGMENT);
                        break;
                    case ITEM3:
                        break;
                    case ITEM4:
                        intent.setComponent(new ComponentName(context, "com.yilian.mall.ui.JPMainActivity"));
                        intent.putExtra(Constants.INTENT_KEY_JUMP_TO_JP_MAIN_ACTIVITY, Constants.INTENT_VALUE_JUMP_TO_PERSON_CENTER_FRAGMENT);
                        break;
                    default:
                        break;
                }
                context.startActivity(intent);
            }
        });
    }
}
