package com.yilian.mall.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.orhanobut.logger.Logger;
import com.yilian.loginmodule.LeFenPhoneLoginActivity;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mall.utils.RequestOftenKey;
import com.yilian.mall.utils.StatusBarUtils;
import com.yilian.mall.zxing.camera.CameraManager;
import com.yilian.mall.zxing.decoding.CaptureActivityHandler;
import com.yilian.mall.zxing.decoding.InactivityTimer;
import com.yilian.mall.zxing.view.ViewfinderView;
import com.yilian.mylibrary.Constants;
import com.yilian.mylibrary.MyQrCodeModel;
import com.yilian.networkingmodule.entity.BarCodeScanResultEntity;
import com.yilian.networkingmodule.retrofitutil.RetrofitUtils3;
import com.yilianmall.merchant.activity.BarCodeScanGoodResultActivity;
import com.yilianmall.merchant.activity.MerchantInputBarCodeSearchActivity;

import java.io.IOException;
import java.util.Vector;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Initial the merchant_camera
 *
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends BaseActivity implements Callback, OnClickListener {

    public static final int REQUEST_CODE = 1;
    private static final float BEEP_VOLUME = 0.10f;
    private static final long VIBRATE_DURATION = 200L;
    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private boolean vibrate;
    private ImageView v3Back;
    private ImageView v3Left;
    private TextView v3Title;
    private TextView tvRight;
    private ImageView v3Shop;
    private ImageView v3Share;
    private TextView tvRight2;
    private TextView tvGetQRCode;
    private FrameLayout v3Layout;
    private String merchantLev;
    private String jumpType;
    private boolean isFirst = true;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_camera);
        initView();
        CameraManager.init(getApplication());
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
        StatusBarUtils.setStatusBarColor(this, R.color.black_color);
        //区分是快递扫描还是商品条码
        jumpType = getIntent().getStringExtra(Constants.JUMP_MIPCA);
        if (null == jumpType) {
            jumpType = "barCode";//默认标记是商品条码扫描
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {

            openCamera(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private void openCamera(SurfaceHolder surfaceHolder) throws IOException {
        /**
         * 检查摄像头权限
         */

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (isFirst) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
                isFirst = !isFirst;
            } else {
                finish();
                showToast("请开启摄像头权限");
            }
        } else {
            CameraManager.get().openDriver(surfaceHolder);
        }
    }

    private void initView() {
        v3Back = (ImageView) findViewById(R.id.v3Back);
        v3Back.setImageResource(R.mipmap.baijiantou);
        v3Left = (ImageView) findViewById(R.id.v3Left);
        v3Title = (TextView) findViewById(R.id.v3Title);
        v3Title.setText("扫码");
        v3Title.setTextColor(Color.WHITE);
        tvRight = (TextView) findViewById(R.id.tv_right);
        v3Shop = (ImageView) findViewById(R.id.v3Shop);
        v3Share = (ImageView) findViewById(R.id.v3Share);
        tvRight2 = (TextView) findViewById(R.id.tv_right2);
        tvRight2.setText("手输条码");
        merchantLev = PreferenceUtils.readStrConfig(Constants.MERCHANT_LEV, mContext, "0");
        if (Integer.valueOf(merchantLev) > 1) {
            tvRight2.setVisibility(View.VISIBLE);
        }
        tvRight2.setTextColor(Color.WHITE);
        v3Layout = (FrameLayout) findViewById(R.id.v3Layout);
        v3Layout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        tvGetQRCode = (TextView) findViewById(R.id.tv_getQRCode);
        View vinewLine = findViewById(R.id.view_line);
        vinewLine.setBackgroundColor(ContextCompat.getColor(mContext, R.color.black));
        v3Back.setOnClickListener(this);
        tvRight.setOnClickListener(this);
        tvRight2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v3Back:
                finish();
                break;
            case R.id.tv_right:
                break;
            case R.id.tv_right2:
                startActivity(new Intent(mContext, MerchantInputBarCodeSearchActivity.class));
                break;
            default:
                break;
        }

    }

    /**
     * 处理扫描结果
     *
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();
        String jsonString = result.getText();
        Logger.i("扫描结果：" + jsonString);
        Intent intent = null;
        if (TextUtils.isEmpty(jsonString)) {
            showToast(R.string.scan_failure);
            restartScan();
        } else if (jsonString.contains("htt")) { //外部URL取消，全部走内部URL
            if (isLogin()) {
                //跳转至web收银台界面
                if (jsonString.contains("m/pay/merScanPay.php?")) {
                    if (PreferenceUtils.readBoolConfig(Constants.PAY_PASSWORD, mContext, false)) {
                        intent = new Intent(MipcaActivityCapture.this, WebViewActivity.class);
                        intent.putExtra("title_name", "益联益家");
                        intent.putExtra("url", jsonString);//此处使用的是不加盐的token
                        startActivity(intent);
                        Logger.i(RequestOftenKey.gettoken(mContext));
                    } else {
                        new AlertDialog.Builder(mContext)
                                .setMessage("您还未设置支付密码，请设置支付密码后在支付！")
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(mContext, InitialPayActivity.class));
                                        dialog.dismiss();
                                        finish();
                                    }
                                }).create().show();
                    }
                } else {
                    intent = new Intent(MipcaActivityCapture.this, WebViewActivity.class);
                    intent.putExtra("title_name", "益联益家");
                    intent.putExtra("url", jsonString);//此处使用的是不加盐的token
                    Logger.i(RequestOftenKey.gettoken(mContext));
                    startActivity(intent);
                    finish();
                }
            } else {
                intent = new Intent(mContext, LeFenPhoneLoginActivity.class);
                startActivity(intent);
                finish();
            }

        } else {
            MyQrCodeModel myQrCodeModel = null;
            try {
                myQrCodeModel = new Gson().fromJson(jsonString, MyQrCodeModel.class);
                switch (myQrCodeModel.type) {
                    case MyQrCodeModel.QrCodeType.MERCHANT_EXCHANGE_QR_CODE:
//                服务中心收券二维码
                        intent = new Intent(mContext, PayExchangeToMerchantActivity.class);
                        intent.putExtra(PayExchangeToMerchantActivity.TAG_MERCHANT_ID, myQrCodeModel.content);
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        isBarCodeOrExpressNumber(jsonString);
                        break;
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * 重置扫码，实现联系扫码功能
     */
    void restartScan() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1500);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (handler == null) {
                                handler = new CaptureActivityHandler(MipcaActivityCapture.this, decodeFormats,
                                        characterSet);
                            }
                            handler.restartPreviewAndDecode();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 商品条码
     *
     * @param jsonString
     */
    private void isBarCodeOrExpressNumber(String jsonString) {
        //扫描条码结果
        if (isLogin()) {
            //TODO 条形码区分是商品条形码扫描或者是快递单号扫码
            if (!TextUtils.isEmpty(jumpType)) {
                switch (jumpType) {
                    case Constants.EXPRESS_SELECT:
                        /**
                         * 快递单号扫描
                         */
                        Intent expressIntent = new Intent();
                        expressIntent.setAction("com.yilianmall.merchant.activity.MerchantExpressSelectActivity");
                        expressIntent.putExtra("result", jsonString);
                        sendBroadcast(expressIntent);
                        Logger.i("快递单号 result  " + jsonString);
                        finish();
                        break;
                    default:
                        /**
                         * 商品条形码扫描
                         */
                        if (Integer.valueOf(merchantLev) > 1) { //账号是商家时，处理扫描结果
//                获取条码信息时,不关闭扫码页面
                            getGoodsInfo(jsonString);
                        } else { //账号是会员时，直接提示扫描结果
                            new AlertDialog.Builder(mContext)
                                    .setTitle("扫描结果")
                                    .setMessage(jsonString)
                                    .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            restartScan();
                                        }
                                    })
                                    .setCancelable(false)
                                    .create().show();
                        }
                        break;
                }
            }
        } else {
            startActivity(new Intent(mContext, LeFenPhoneLoginActivity.class));
            finish();
        }
    }

    @SuppressWarnings("unchecked")
    void getGoodsInfo(String barCode) {
        Subscription subscription = RetrofitUtils3.getRetrofitService(mContext).
                getBarCodeScanResult("sc_scan_goods_code", com.yilian.mylibrary.RequestOftenKey.getToken(mContext), com.yilian.mylibrary.RequestOftenKey.getDeviceIndex(mContext), barCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BarCodeScanResultEntity>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showToast(e.getMessage());
                        restartScan();
                    }

                    @Override
                    public void onNext(BarCodeScanResultEntity barCodeScanResultEntity) {
                        Intent intent = new Intent(mContext, BarCodeScanGoodResultActivity.class);
                        BarCodeScanResultEntity.DataBean data = barCodeScanResultEntity.data;
                        intent.putExtra("barCodeScanResultEntity", data);
                        startActivity(intent);
                    }
                });
        addSubscription(subscription);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE:
                if (permissions.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
                        SurfaceHolder surfaceHolder = surfaceView.getHolder();
                        try {
                            CameraManager.get().openDriver(surfaceHolder);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                    } else {
                        finish();
                    }
                }
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;

    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }
}