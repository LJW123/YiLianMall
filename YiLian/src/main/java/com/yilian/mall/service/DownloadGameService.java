package com.yilian.mall.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.R;
import com.yilian.mall.ui.GameHomePageActivity;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;
import com.yilian.mylibrary.event.EventDownLoadProgress;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

/**
 * Created by  on 2017/9/15 0015.
 */

public class DownloadGameService extends Service {

    public static final int NOTIFICATION_ID = 888;
    private NotificationManager notificationManager;
    private Notification notification;
    private boolean isDownloading;
    private HttpUtils mHttpUtils;
    private String downloadUrl;
    private String redirectDownloadUrl;
    private String gameName;
    private EventDownLoadProgress event;
    private int position;
    private String filePath;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        downloadUrl = intent.getStringExtra("game_download_url");
        redirectDownloadUrl = Ip.getURL(getApplicationContext()) + "downGame.php?game_url=" + downloadUrl;
        Logger.i("downloadUrl;" + redirectDownloadUrl);
        gameName = intent.getStringExtra("game_name");
        position = intent.getIntExtra("position", -1);
        initNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotification() {
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notification = new Notification();
        notification.icon = android.R.drawable.stat_sys_download_done;
        notification.tickerText = "游戏已经开始下载";
        RemoteViews remoteView = new RemoteViews(getPackageName(), R.layout.download_game_notification);
        remoteView.setImageViewResource(R.id.imgViewIcon, R.mipmap.game);
        remoteView.setTextViewText(R.id.txt_notice, "正在下载" + gameName + getString(R.string.download_game_notification_text, "0%"));
        remoteView.setProgressBar(R.id.progBar, 100, 10, false);
        notification.contentView = remoteView;

        if (!isDownloading) {
            download();
            isDownloading = true;
        }
    }

    void updateNotification() {

    }


    private void download() {
        if (mHttpUtils == null) {
            mHttpUtils = new HttpUtils();
        }
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "SD卡不可用", Toast.LENGTH_LONG).show();
            return;
        }
        filePath = Environment.getExternalStorageDirectory() + Constants.GAME_PATH + gameName + ".unapk";//下载中的文件名，不要以apk结尾，防止未下载完成时进行打开操作会报错
        Logger.i("游戏下载路径：" + filePath);
        event = new EventDownLoadProgress(position, filePath);
        Logger.i("源文件路径：" + redirectDownloadUrl);
        mHttpUtils.download(redirectDownloadUrl, filePath, false, false, new RequestCallBack<File>() {
            RemoteViews rv = notification.contentView;

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                int progress = (int) (current * 100 / total);
                rv.setTextViewText(R.id.txt_notice, "正在下载" + gameName + getString(R.string.download_game_notification_text, progress + "%"));
                rv.setProgressBar(R.id.progBar, 100, progress, false);
                notificationManager.notify(NOTIFICATION_ID, notification);
                event.progress = progress;
                postEvent();
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                isDownloading = false;
                DownloadGameService.this.stopSelf();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String downloadFinishFile = Environment.getExternalStorageDirectory() + Constants.GAME_PATH + gameName + ".apk";//下载完成后才已apk作为文件后缀
                File downloadFile = new File(filePath);
                Logger.i("oldFile:" + downloadFile.getAbsolutePath());
                File newFile = new File(downloadFinishFile);
                boolean renameSuccess = downloadFile.renameTo(newFile);
                File successFile;
                if (renameSuccess) {
                    successFile = newFile;
                } else {
                    successFile = downloadFile;
                }
                Logger.i("newFile::" + downloadFile.getAbsolutePath());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri uriForFile = FileProvider.getUriForFile(DownloadGameService.this, com.yilian.mall.BuildConfig.APPLICATION_ID+".fileprovider", successFile);
                    intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(successFile), "application/vnd.android.package-archive");
                }
                PendingIntent pendingIntent = PendingIntent.getActivity(DownloadGameService.this, 0, intent, 0);
                notification.contentIntent = pendingIntent;
                notification.icon = android.R.drawable.stat_sys_download_done;
                notification.tickerText = "游戏下载完毕";
                notification.contentView.setTextViewText(R.id.txt_notice, "游戏下载完毕,点击安装");
                notification.flags = Notification.FLAG_AUTO_CANCEL;
                notificationManager.notify(NOTIFICATION_ID, notification);
                startActivity(intent);
            }

            @Override
            public void onFailure(HttpException e, String s) {
                isDownloading = false;
                Toast.makeText(DownloadGameService.this, "下载失败" + s, Toast.LENGTH_LONG).show();
                notification.tickerText = "游戏下载失败";
                notification.contentView.setTextViewText(R.id.txt_notice, "游戏下载失败,请检查网络");
                notificationManager.notify(NOTIFICATION_ID, notification);
                DownloadGameService.this.stopSelf();
            }
        });

    }

    /**
     * 发送下载进度时间
     * {@link  GameHomePageActivity}
     */
    private void postEvent() {
        EventBus.getDefault().post(event);
    }
}
