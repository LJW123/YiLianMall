package com.yilian.mall.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.yilian.mall.BaseActivity;
import com.yilian.mall.R;
import com.yilian.mall.utils.PreferenceUtils;
import com.yilian.mylibrary.Constants;

import java.util.Hashtable;

/**
 * 我的二维码界面
 */
public class MyQRCodeActivity extends BaseActivity {

    @ViewInject(R.id.iv_qrCode)
    private ImageView ivQrCode;

    @ViewInject(R.id.tv_back)
    private TextView tvBack;

    private ViewGroup.LayoutParams ivQrCodeParam;
    private Bitmap bitmapSrc;

    /**
     * 在二维码中间添加Logo图案
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_qr_code_activity);
        ViewUtils.inject(this);
        tvBack.setText("我的二维码");

        ivQrCodeParam = ivQrCode.getLayoutParams();

        getQrCode();
    }

    private void getQrCode() {
        //// 二维码里面的内容  0:固定值9，代表APP用户身份二维码类型 1:用户手机号 2:用户昵称（Url编码）3:积发奖励包 4:奖励 5:乐分币奖励
        String qrCode = 9 + ","
                + PreferenceUtils.readStrConfig("phone", mContext, "") + ","
                + sp.getString("name", "暂无昵称") + ","
                + sp.getString("integral", "0") + ","
                + Float.parseFloat(sp.getString("lebi", "0.00")) / 100 + ","
                + Float.parseFloat(sp.getString("coupon", "0.00")) / 100;
        createImage(qrCode, ivQrCode);
        return;


    }

    // 生成QR图
    private void createImage(String str, final ImageView ivQrCode) {

        try {
            if (str == null || "".equals(str) || str.length() < 1) {
                showToast("生成失败");
                return;
            }
            // 用于设置QR二维码参数
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            // 设置编码方式
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错率：容错率越高,二维码的有效像素点就越多 L水平7%的字码可被修正 M水平15%的字码可被修正 Q水平25%的字码可被修正 H水平30%的字码可被修正
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bitMatrix = new QRCodeWriter().encode(str, BarcodeFormat.QR_CODE, ivQrCodeParam.width, ivQrCodeParam.height, hints);

            // 开始利用二维码数据创建Bitmap图片，分别设为黑白两色
            int[] pixels = new int[ivQrCodeParam.width * ivQrCodeParam.height];
            for (int y = 0; y < ivQrCodeParam.height; y++) {
                for (int x = 0; x < ivQrCodeParam.width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * ivQrCodeParam.width + x] = 0xff000000;// 黑色
                    } else {
                        pixels[y * ivQrCodeParam.width + x] = -1;// -1 相当于0xffffffff 白色
                    }
                }
            }

            // 创建一张bitmap图片，采用最高的图片效果ARGB_8888
            bitmapSrc = Bitmap.createBitmap(ivQrCodeParam.width, ivQrCodeParam.height, Bitmap.Config.ARGB_8888);
            // 将上面的二维码颜色数组传入，生成图片颜色
            bitmapSrc.setPixels(pixels, 0, ivQrCodeParam.width, 0, 0, ivQrCodeParam.width, ivQrCodeParam.height);

            //获取用户头像bitmap
            ImageLoader.getInstance().loadImage(Constants.ImageUrl + sp.getString("photo", ""), new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {

                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                    ivQrCode.setImageBitmap(bitmapSrc);

                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                    Bitmap bitmapCompleted = addLogo(bitmapSrc, bitmap);
                    ivQrCode.setImageBitmap(bitmapCompleted);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {

                    showDialog("温馨提示", "很抱歉，二维码生成失败", null, R.mipmap.ic_warning, Gravity.CENTER, null, "再试一次", false,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getQrCode();
                                    dialog.dismiss();

                                }
                            }, mContext);
                }
            });

        } catch (Exception e) {
            showDialog("温馨提示", "很抱歉，二维码生成失败", null, R.mipmap.ic_warning, Gravity.CENTER, null, "再试一次", false,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getQrCode();
                            dialog.dismiss();

                        }
                    }, mContext);
        }
    }
}
