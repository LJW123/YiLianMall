package com.yilian.mylibrary.event;

/**
 * Created by  on 2017/9/23 0023.
 */

public class EventGameDownloadStatusChanged {
    public final boolean isChanged;

    public EventGameDownloadStatusChanged(boolean isChanged) {
        this.isChanged = isChanged;
    }
}
