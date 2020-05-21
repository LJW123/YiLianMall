package com.yilian.mylibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.orhanobut.logger.Logger;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;

public class BitmapUtil {
    public static Bitmap toturn(Bitmap img) {
        Matrix matrix = new Matrix();
        matrix.postRotate(+90); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    /**
     * Change bitmap`s color.
     *
     * @param sourceBitmap The bitmap.
     * @param color        The color.
     * @return The new bitmap.
     */
    public static Bitmap changeColor(Bitmap sourceBitmap, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new LightingColorFilter(color, 1);
        p.setColorFilter(filter);

        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        return resultBitmap;
    }

    /**
     * Convert drawable to bitmap.
     *
     * @param drawable The drawable.
     * @return The bitmap.
     */
    public static Bitmap toBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * Convert bitmap to drawable.
     *
     * @param context The context.
     * @param bitmap  The bitmap to be converted.
     * @return The bitmap.
     */
    public static Drawable toDrawable(Context context, Bitmap bitmap) {
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 如果图片文件被旋转 则按照被旋转角度进行还原（处理三星手机拍照获得照片被旋转的问题）
     *
     * @param filePath 文件路径
     * @param file     压缩后的文件 传递进来的文件是被压缩后的文件 防止内存溢出
     * @return
     */
    public static File restoreFile(String filePath, File file,String FileName) {
        int i = BitmapUtil.readPictureDegree(filePath);//获取照片被旋转的角度
        Logger.i("照片旋转角度：" + i);
        if (i == 0) {
            return file;//没有旋转 原路返回
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
        Bitmap bitmap1 = BitmapUtil.rotaingImageView(i, bitmap);//将照片旋转的角度还原过来，解决三星手机拍照自动旋转90度的问题
        Uri uri = saveBitmap(bitmap1, "yilian",FileName);//将还原的图片保存到文件中 进行返回
        File file1 = null;
        try {
            file1 = new File(new URI(uri.toString()));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return file1;
    }

    /**
     * @param bm
     * @param innerPath 保存到的应用内文件夹（不可被其他应用查看看）
     * @return
     */
    public static Uri saveBitmap(Bitmap bm, String innerPath,String fileName) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/com.yilian/" + innerPath + "/");
        if (!tmpDir.exists()) {
            tmpDir.mkdirs();
        }
        File img = new File(tmpDir.getAbsolutePath() + fileName+".png");
        try {
            FileOutputStream fos = new FileOutputStream(img);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(img);
        } catch (java.io.FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 二进制转换为bitmap
     *
     * @param temp
     * @return
     */
    public static Bitmap Byte2Bitmap(byte[] temp) {
        if (temp != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);

            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap File2Bitmap(String uri) throws IOException {
        FileInputStream fs = new FileInputStream(uri);
        BufferedInputStream bs = new BufferedInputStream(fs);
        Bitmap btp = BitmapFactory.decodeStream(bs);
        return btp;
    }

    /**
     * @param urlpath
     * @return Bitmap
     * 根据图片url获取图片对象
     */
    public static Bitmap getBitMBitmap(String urlpath) {
        Bitmap map = null;
        try {
            URL url = new URL(urlpath);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream in;
            in = conn.getInputStream();
            map = BitmapFactory.decodeStream(in);
            in.close();
            // TODO Auto-generated catch block
        } catch (IOException e) {
            Logger.i("url解析错误");
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 二进制转换为图片
     *
     * @param temp
     * @return
     */
    public static Drawable Byte2Drawable(byte[] temp) {
        if (temp != null) {
            ByteArrayInputStream bais = new ByteArrayInputStream(temp);
            return Drawable.createFromStream(bais, "image");
        } else {
            return null;
        }

    }

    /**
     * 地址图片转换为二进制字符串
     *
     * @param picturePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String getStringByteFromPicPath(String picturePath) throws FileNotFoundException, IOException {
        FileInputStream fis;
        ByteArrayOutputStream baos = null;
        fis = new FileInputStream(picturePath);
        baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = fis.read(buffer)) >= 0) {
            baos.write(buffer, 0, count);
        }
        fis.close();
        return new String(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT)); // 进行Base64编码
    }

    /**
     * bitmap 转换为二进制
     *
     * @param bitmap
     * @return
     */
    public static byte[] Bitmap2Byte(Bitmap bitmap) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 将drawable转换为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 将Bitmap转换为drawable
     */
    public static Drawable bitmapToDrawble(Bitmap bm) {
        BitmapDrawable bd = new BitmapDrawable(bm);
        return bd;
    }

    /**
     * 将string转换为Bitmap
     */
    public static Bitmap stringtoBitmap(String string) {
        // 将字符串转换成Bitmap类型
        Bitmap bitmap = null;
        try {
            byte[] bitmapArray;
            bitmapArray = Base64.decode(string, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 从路径filePath 中 压缩图片 以480*800为基准
     *
     * @param filePath
     * @return
     */
    public static Bitmap getSmallBitmap(String filePath, int width, int height) {

        Logger.e("getSmallBitmap  filePath  " + filePath + " width " + width + "");
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Config.RGB_565;
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        // Calculate inSampleSize
        if (width == 0 || height == 0) {
            options.inSampleSize = calculateInSampleSize(options, 480, 800);// 480,
            // 800
        } else {
            options.inSampleSize = calculateInSampleSize(options, width, height);
        }

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 从data 中 压缩图片 以480*800为基准
     *
     * @param
     * @return
     */
    public static Bitmap getSmallBitmap(byte[] data) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return bitmap;
    }

    /**
     * 存储图片到filepath并且打上水印mark
     *
     * @param data
     * @param mark
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String storeImage(byte[] data, int textSize, String mark, String filePath) throws IOException {

        File file = new File(filePath);
        Bitmap bm = getSmallBitmap(data);
        bm = mark(bm, mark, textSize, false);
        // Bitmap bm = BitmapFactory.decodeByteArray(data, 0, data.length);

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);
        bos.flush();
        bos.close();
        return file.getPath();

    }

    /**
     * 计算图片的缩放值 调用以480*800为基准
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        // if (width>height) {
        // width=options.outHeight;
        // height=options.outWidth;
        // }
        // int inSampleSize = 1;
        // if (height > reqHeight || width > reqWidth) {
        // final int heightRatio = Math.round((float) height
        // / (float) reqHeight);
        // final int widthRatio = Math.round((float) width / (float) reqWidth);
        // inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        // }
        if (reqWidth == -1) {
            int flag = width > height ? width : height;
            int inSampleSize = Math.round((float) flag / (float) 600);
            return inSampleSize;
        } else {
            int inSampleSize = 1;
            if (height > reqHeight || width > reqWidth) {
                final int heightRatio = Math.round((float) height / (float) reqHeight);
                final int widthRatio = Math.round((float) width / (float) reqWidth);
                inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
            }
            return inSampleSize;
        }
    }

    /**
     * 给图片打水印
     *
     * @param src
     * @param watermark
     * @param size
     * @param underline
     * @return
     */
    public static Bitmap mark(Bitmap src, String watermark, int size, boolean underline) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);

        Paint paint = new Paint();

        // paint.setAlpha(alpha);
        paint.setDither(true); // 获取跟清晰的图像采样
        paint.setFilterBitmap(true);// 过滤一些

        Rect srcRect = new Rect(0, 0, src.getWidth(), src.getHeight());// 创建一个指定的新矩形

        Rect dst = new Rect(0, 0, w, h);// 创建一个指定的新矩形的坐标

        canvas.drawBitmap(src, srcRect, dst, paint);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DEV_KERN_TEXT_FLAG);

        textPaint.setTextSize(size);
        textPaint.setAntiAlias(true);
        textPaint.setUnderlineText(underline);
        textPaint.setColor(Color.WHITE);
        String familyName = "宋体";
        Typeface font = Typeface.create(familyName, Typeface.NORMAL);
        textPaint.setTypeface(font);

        Paint bg = new Paint();// 加fanb
        bg.setColor(Color.BLACK);
        canvas.drawRect((float) 0, (float) (h * 0.9), (float) w, (float) (h - 10), bg);
        canvas.drawText(watermark, 5, h - 30, textPaint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return result;
    }

    /**
     * Drawable缩放
     *
     * @param drawable
     * @param w
     * @param h
     * @return
     */
    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // drawable转换成bitmap
        Bitmap oldbmp = drawableToBitmap(drawable);
        // 创建操作图片用的Matrix对象
        Matrix matrix = new Matrix();
        // 计算缩放比例
        float sx = ((float) w / width);
        float sy = ((float) h / height);
        // 设置缩放比例
        matrix.postScale(sx, sy);
        // 建立新的bitmap，其内容是对原bitmap的缩放后的图
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(newbmp);
    }

    /**
     * 获得圆角图片
     *
     * @param bitmap
     * @param roundPx
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Config.RGB_565);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, w, h);
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 获得带倒影的图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
        final int reflectionGap = 4;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, h / 2, w, h / 2, matrix, false);

        Bitmap bitmapWithReflection = Bitmap.createBitmap(w, (h + h / 2), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0, 0, null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0, h, w, h + reflectionGap, deafalutPaint);

        canvas.drawBitmap(reflectionImage, 0, h + reflectionGap, null);

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
                bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw a rectangle using the paint with our linear gradient
        canvas.drawRect(0, h, w, bitmapWithReflection.getHeight() + reflectionGap, paint);

        return bitmapWithReflection;
    }

    /**
     * 图片转灰度
     *
     * @param
     * @return
     */
    public static Bitmap bitmap2Gray(Bitmap bmpOriginal) {
        try {
            int width, height;
            height = bmpOriginal.getHeight();
            width = bmpOriginal.getWidth();
            Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
                    Config.RGB_565);
            Canvas c = new Canvas(bmpGrayscale);

            // final Rect rect = new Rect(0, 0, bmpGrayscale.getWidth(),
            // bmpGrayscale.getHeight());
            // final RectF rectF = new RectF(new Rect(0, 0,
            // bmpGrayscale.getWidth(),
            // bmpGrayscale.getHeight()));
            // final float roundPx = 14;

            Paint paint = new Paint();
            // paint.setAntiAlias(true);
            // c.drawRoundRect(rectF, roundPx, roundPx, paint);

            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(bmpOriginal, 0, 0, paint);
            Log.e("Gay pic ", "is running...");
            return bmpGrayscale;
        } catch (Exception e) {
            System.gc();
            Log.e("Gay 图片 ", "有问题");
            return bmpOriginal;
        }


//		 int width, height;
//         height = bmpOriginal.getHeight();
//         width = bmpOriginal.getWidth();
//         Bitmap grey= Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
//         Canvas c = new Canvas(bmpOriginal);
//         Paint paint = new Paint();
//         ColorMatrix cm = new ColorMatrix();
//         cm.setSaturation(0);
//         ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//         paint.setColorFilter(f);
//         c.drawBitmap(grey, 0, 0, paint);
//         return grey;
    }

    /**
     * 给图片设置exif信息 make中传入 taskid和userid的T加密结果 model 中传入水印坐标 东经正数，西经为负数
     * 北纬为正数，南纬为负数
     */
    // public static void setExif(String file, String makeMsg, String modelMsg,
    // double gpslat, double gpslong) { // 纬度 经度
    // ExifInterface exif = null;
    // try {
    // exif = new ExifInterface(file);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // exif.setAttribute(ExifInterface.TAG_DATETIME,
    // DateUtils.getNowAllFormat());
    // exif.setAttribute(ExifInterface.TAG_MAKE, makeMsg);
    // exif.setAttribute(ExifInterface.TAG_MODEL, modelMsg);
    // exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE, gpslat + "");
    // exif.setAttribute(ExifInterface.TAG_GPS_LATITUDE_REF, gpslat > 0 ? "N"
    // : "S");//
    // exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE, gpslong + "");
    // exif.setAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF,
    // gpslong > 0 ? "E" : "W");
    // try {
    // exif.saveAttributes();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    //
    // }
    public static Bitmap convertBmp(Bitmap bmp, int angle) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        Bitmap convertBmp = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(convertBmp);
        Matrix matrix = new Matrix();
        switch (angle) {
            case 0:
                matrix.postScale(1, -1); //镜像垂直翻转
                break;
            case 1:
                matrix.postScale(-1, 1); //镜像水平翻转
                break;
            case 2:
                matrix.postRotate(-90); //旋转-90度
                break;
        }

        Bitmap newBmp = Bitmap.createBitmap(bmp, 0, 0, w, h, matrix, true);
        cv.drawBitmap(newBmp, new Rect(0, 0, newBmp.getWidth(), newBmp.getHeight()), new Rect(0, 0, w, h), null);
        return convertBmp;
    }


    /*
     * 质量压缩图片
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos

            try {
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
            } catch (Exception e) {
                Logger.i("options:" + options);
                e.printStackTrace();
            }
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 等比例缩小图片
     *
     * @param isTrans 是否按照480*800的比列压缩 还是原本图片的一半
     * @return
     */
    private Bitmap MaxBitMap(Bitmap bitmap, boolean isTrans) {
        // TODO Auto-generated method stub
        // 获取这个图片的宽和高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = 480;
        int newHeight = 800;
        if (!isTrans) {
            // 根据最大的一边按照800 的比列缩放
            if (width > height) {
                newWidth = 600;
                newHeight = (height * 600) / width;
            } else {
                newHeight = 600;
                newWidth = (width * 600) / height;
            }
        } else {
            // 如果按照480*800压缩 要先看看原本图片宽高那个长
            if (width > height) {
                newWidth = 800;
                newHeight = 480;
            }
        }

        if (width > newWidth) {// 比480大的缩放
            // 计算缩放率，新尺寸除原始尺寸
            float scaleWidth = ((float) newWidth) / width;
            float scaleHeight = ((float) newHeight) / height;

            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();

            // 缩放图片动作
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建新的图片
            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            bitmap.recycle();
            return resizedBitmap;
        } else {
            return bitmap;
        }
    }

    public String getThumbUploadPath(Context mContext, String oldPath) throws Exception {
        Bitmap newbitmap;
        if ((FileUtils.getFileSize(oldPath) / 1024) > 100) {//大于100kb
            int degree = BitmapUtil.readPictureDegree(oldPath);
            newbitmap = getSmallBitmap(oldPath, -1, -1);
            /**
             * 把图片旋转为正的方向
             */
            newbitmap = BitmapUtil.rotaingImageView(degree, newbitmap);
            // newbitmap = getSmallBitmap(oldPath, -1, -1);
            if (newbitmap.getWidth() > 480) {
                newbitmap = MaxBitMap(newbitmap, false);
            }
            String newPath = oldPath.substring(0, oldPath.lastIndexOf("/") + 1) + "user_photo1"
                    + oldPath.substring(oldPath.lastIndexOf("."), oldPath.length());
            return FileUtils.saveBtpToFile(mContext, newbitmap, newPath);
        } else {
            String newPath = oldPath.substring(0, oldPath.lastIndexOf("/") + 1) + "user_photo1"
                    + oldPath.substring(oldPath.lastIndexOf("."), oldPath.length());
            FileUtils.copyFile(oldPath, newPath);
            return newPath;
        }

    }

    /**
     * 从路径filePath 中 等比例缩放图片 以480*800为基准
     *
     * @param filePath
     * @return
     */
    public Bitmap getMaxBitmap(String filePath, int width, int height) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        if (bitmap.getWidth() > 480) {
            bitmap = MaxBitMap(bitmap, true);
        }
        return bitmap;
    }

    /**
     * Bitmap转String
     */
    public static String bitmaoToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] appicon = baos.toByteArray();
        return Base64.encodeToString(appicon, Base64.DEFAULT);
    }

    /**
     * Bitmap转String
     */
    public static String bitmaoToStringFromLength(Bitmap bitmap, int maxLength) {
        String string = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int option = 90;
        Logger.i("baos.toByteArray().length   " + baos.toByteArray().length);
        while (baos.toByteArray().length / 1024 > maxLength) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
            option -= 10;
        }
        byte[] bytes = baos.toByteArray();
        Base64Encoder base = new Base64Encoder();
        string = Base64Encoder.encode(bytes);

        return string;
    }


    /**
     * 旋转图片
     */
    public static Bitmap rotateBitmap(Bitmap bm) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(90);
        Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        if (bm.isRecycled()) {
            bm.recycle();
        }
        return newBm;
    }

    /*
    * 读取图片的旋转角度 并且旋转为正确的角度
    * @param  downloadPath 图片的绝对路径
    * @return  当前图片旋转的角度
    */
    public static Bitmap getRotateBitmap(String path, Bitmap bitmap) {
        int degree = 0;
        //从指定库路径下读取图片，平获取起EXIF信息
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            //获取图片的旋转信息
            int attributeInt = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (attributeInt) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postRotate(degree);
        Bitmap newBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        if (bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBm;

    }


    public static Drawable getDrawableFromImageUrl(final String imageUrl) {
        final Drawable[] drawable = {null};

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    URL url = new URL(imageUrl);
                    URLConnection conn = url.openConnection();
                    conn.connect();

                    final InputStream inputStream = conn.getInputStream();
                    new Runnable() {
                        @Override
                        public void run() {
                            drawable[0] = Drawable.createFromStream(inputStream, "background.jpg");
                        }
                    }.run();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

        return drawable[0];
    }


}
