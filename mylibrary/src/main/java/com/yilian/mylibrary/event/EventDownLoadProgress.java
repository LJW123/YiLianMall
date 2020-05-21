package com.yilian.mylibrary.event;

/**
 * Created by  on 2017/9/22 0022.
 */

public class EventDownLoadProgress {
    public final int position;//游戏下载条目在list中的position
    public final String downloadPath;//游戏下载路径
    public int progress;//游戏加载进度


    public EventDownLoadProgress(int position, String path) {
        this.position=position;
        this.downloadPath = path;
    }
}
