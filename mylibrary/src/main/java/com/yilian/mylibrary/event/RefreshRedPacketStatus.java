package com.yilian.mylibrary.event;

/**
 * @author Created by  on 2017/11/30.
 *         拆取奖励时，若奖励状态异常则刷新奖励数据 主要是解决网络状态不好时，奖励数据刷新不及时的问题
 */

public interface RefreshRedPacketStatus {
    void setOnRefreshRedPacketStatus();

}
