package com.yilian.mall.ui.transfer;

import java.io.Serializable;

/**
 * @author xiaofo on 2018/10/10.
 * 受赠人
 */

public class Donee implements Serializable{
    public Donee(String userId, String nick, String phone, String photo) {
        this.userId = userId;
        this.nick = nick;
        this.phone = phone;
        this.photo = photo;
    }

    public String userId;
    /**
     * 昵称
     */
    public String nick;
    /**
     * 联系电话
     */
    public String phone;
    /**
     * 头像
     */
    public String photo;
}
