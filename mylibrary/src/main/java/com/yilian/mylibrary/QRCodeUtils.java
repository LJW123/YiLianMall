package com.yilian.mylibrary;/**
 * Created by  on 2016/12/7 0007.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.orhanobut.logger.Logger;

import java.util.Hashtable;

/**
 * Created by  on 2016/12/7 0007.
 */
public class QRCodeUtils {
    private final Context context;

    public QRCodeUtils(Context context) {
        this.context = context;
    }

    public static QRCodeUtils getInstance(Context context) {
        return new QRCodeUtils(context);
    }

    //套餐二维码前缀
    public String getMTPackageTicketQRCodePrefix(String packageOrderId) {
        return Ip.getWechatURL(context)+"merchant/verifyCode/verifyConfirm.php?code=";
//        return "lefen,10," + CommonUtils.getMD5(RequestOftenKey.gettoken(context)) + "," + packageOrderId + ",";
    }

    // 根据字符串，生成QR图并显示在ImageView上
    public static void createImage(String str, final ImageView ivQrCode) {
        ViewGroup.LayoutParams ivQrCodeParam = ivQrCode.getLayoutParams();
        Bitmap bitmapSrc = null;
        int times = 0;
        try {
            if (str == null || "".equals(str) || str.length() < 1) {
//                生成失败
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
            ivQrCode.setImageBitmap(bitmapSrc);//显示二维码
        } catch (Exception e) {
            Logger.i("生成错误二维码：" + e);
//            if (times < 5) {
//                times++;
//                createImage(str, ivQrCode);
//            }
        }
    }

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

    /**
     * 生成带logo的二维码
     *
     * @param content
     * @param widthPix
     * @param heightPix
     * @param logo
     */
    public static void createQRLogoImage(String content, int widthPix, int heightPix, Bitmap logo,ImageView ivQrCode) {

        ViewGroup.LayoutParams ivQrCodeParam = ivQrCode.getLayoutParams();
        Bitmap bitmapSrc = null;
        int times = 0;
        try {
            if (content == null || "".equals(content) || content.length() < 1) {
//                生成失败
                return;
            }
            // 用于设置QR二维码参数
            Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            // 设置编码方式
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //容错率：容错率越高,二维码的有效像素点就越多 L水平7%的字码可被修正 M水平15%的字码可被修正 Q水平25%的字码可被修正 H水平30%的字码可被修正
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            BitMatrix bitMatrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, ivQrCodeParam.width, ivQrCodeParam.height, hints);

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
            if (null!=logo){
                bitmapSrc=addLogo(bitmapSrc,logo);
            }
            ivQrCode.setImageBitmap(bitmapSrc);
            ivQrCode.setImageBitmap(bitmapSrc);//显示二维码
        } catch (Exception e) {
            Logger.i("生成错误二维码：" + e);
//            if (times < 5) {
//                times++;
//                createImage(str, ivQrCode);
//            }
        }

    }
}
