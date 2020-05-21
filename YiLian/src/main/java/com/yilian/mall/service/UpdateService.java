package com.yilian.mall.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.widget.RemoteViews;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.Logger;
import com.yilian.mall.BuildConfig;
import com.yilian.mall.R;
import com.yilian.mall.utils.Toast;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.Ip;

import java.io.File;


public class UpdateService extends Service {

    private static final int NOTIFICATION_ID = 9999;

    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private HttpUtils mHttpUtils;
    private String mUrl;
    private boolean isDownloading = false;
    private String notificationChannelId = "1";
    private String notificationChnanelName = "yiliyijia_notification_channel";
    private NotificationChannel mNotificationChannel;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        mUrl = Ip.getURL(getApplicationContext()) + "downAndroid.php";
//		mUrl="http://lfyilian.oss-cn-beijing.aliyuncs.com/app/android/testApp/yilianmall-release_v1.2.1";
        initNotification();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        if (!isDownloading) {
            downLoad();
            isDownloading = true;
        }
        Toast.makeText(this, "正在下载更新", Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initNotification() {
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotification = new Notification.Builder(this, notificationChannelId).build();
        } else {
            mNotification = new Notification();
        }
        //设置通知声音和震动仅仅提醒一次
        mNotification.flags = Notification.FLAG_ONLY_ALERT_ONCE;
        mNotification.icon = R.mipmap.yilianlogo;
//		mNotification.largeIcon=BitmapFactory.decodeResource(getResources(), R.drawable.yilianlogo);
        mNotification.tickerText = "益联益家已经开始下载";

        RemoteViews rv = new RemoteViews(getPackageName(), R.layout.download_update_notification);
        rv.setImageViewResource(R.id.imgViewIcon, R.mipmap.yilianlogo);
        rv.setTextViewText(R.id.txt_notice, getString(R.string.download_update_notification_text, "0%"));
        rv.setProgressBar(R.id.progBar, 100, 10, false);
        mNotification.contentView = rv;

        Intent i = new Intent();
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, 0);
        mNotification.contentIntent = pendingIntent;
        createNotificationChannel();
        mNotificationManager.notify(NOTIFICATION_ID, mNotification);

    }

    @SuppressLint("WrongConstant")
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = new NotificationChannel(notificationChannelId, notificationChnanelName,
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mNotificationChannel);
        }
    }

    private void downLoad() {
        if (mHttpUtils == null) {
            mHttpUtils = new HttpUtils();
        }
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Toast.makeText(this, "SD卡不可用", Toast.LENGTH_LONG).show();
            return;
        }
        /**
         * 点击更新选择下载当前最新版本的apk写入到文件中,判断是否给了读写手机的操作
         */


        final String filePath = Environment.getExternalStorageDirectory() + Constants.BASE_PATH + "yilian.apk";
        mHttpUtils.download(mUrl, filePath, false, false, new RequestCallBack<File>() {
            RemoteViews rv = mNotification.contentView;

            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                // TODO Auto-generated method stub
                int progress = (int) (current * 100 / total);
                Logger.i("下载进度：" + progress);
                rv.setTextViewText(R.id.txt_notice, getString(R.string.download_update_notification_text, progress + "%"));
                rv.setProgressBar(R.id.progBar, 100, progress, false);
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
            }

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(ResponseInfo<File> arg0) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Intent.ACTION_VIEW);
                File file = new File(filePath);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri contentUri = FileProvider.getUriForFile(UpdateService.this, BuildConfig.APPLICATION_ID+".fileprovider", file);
                    Logger.i("BuildConfig.APPLICATION_ID:"+BuildConfig.APPLICATION_ID);
                    intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
                } else {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setDataAndType(Uri.fromFile(file),
                            "application/vnd.android.package-archive");
                }

                PendingIntent pendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0);
                mNotification.contentIntent = pendingIntent;
                mNotification.tickerText = "益联益家已经下载完成";
                mNotification.contentView.setTextViewText(R.id.txt_notice, "益联益家下载完成，点击安装");
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                isDownloading = false;
                startActivity(intent);
                UpdateService.this.stopSelf();
            }

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                // TODO Auto-generated method stub
                Toast.makeText(UpdateService.this, "下载失败" + arg1, Toast.LENGTH_LONG).show();
                mNotification.tickerText = "益联益家失败";
                mNotification.contentView.setTextViewText(R.id.txt_notice, "益联益家下载失败，请检查网络");
                mNotificationManager.notify(NOTIFICATION_ID, mNotification);
                isDownloading = false;
                UpdateService.this.stopSelf();
            }
        });
    }


}
