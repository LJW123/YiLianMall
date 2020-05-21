package com.yilianmall.merchant.event;

/**
 * Created by  on 2017/8/19 0019.
 */

public class UpdateComboOrderItemStatusEvent {
    public final int newStatus;
    public final int position;

    public UpdateComboOrderItemStatusEvent(int position, int newStatus) {
        this.position = position;
        this.newStatus = newStatus;
    }
}
