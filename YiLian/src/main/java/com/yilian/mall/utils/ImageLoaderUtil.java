package com.yilian.mall.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.yilian.mylibrary.Constants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageLoaderUtil {

	private MemoryCache memoryCache = new MemoryCache();
    private Map<ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap<ImageView, String>());

    private ExecutorService executorService;
    private File cacheDir;

    private int type = 0;// 0 小图正常 、1小图灰度图、2 大图
    
    
    public ImageLoaderUtil(Context cont) {
        initCacheDir(cont);
        executorService = Executors.newFixedThreadPool(5);
    }

    private void initCacheDir(Context context) {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            cacheDir = new File(Environment.getExternalStorageDirectory().toString() + Constants.BASE_IMAGE_CACHE);
        } else {
            cacheDir = context.getCacheDir();
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }

    // 加载小图正常
    public void displayImage1(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
        type = 0;
        getBitmapFromMemory(url, imageView, isLoadOnlyFromCache);
    }

    // 加载小图灰度
    public void displayImage2(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
        type = 1;
        getBitmapFromMemory(url, imageView, isLoadOnlyFromCache);
    }

    // 加载大图
    public void displayImage3(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
        type = 2;
        getBitmapFromMemory(url, imageView, isLoadOnlyFromCache);
    }

    // 内存中得到数据
    private void getBitmapFromMemory(String url, ImageView imageView, boolean isLoadOnlyFromCache) {
        imageViews.put(imageView, url);
        Bitmap bitmap = memoryCache.get(url);
        if (bitmap != null) {
            if (type == 0) {
                imageView.setImageBitmap(bitmap);
            } else if (type == 1) {
                imageView.setImageBitmap(getGreyBitmapFromMemoryOrNew(url, bitmap));
            } else {
                imageView.setImageBitmap(rotateImage(bitmap));
            }
        } else if (!isLoadOnlyFromCache) {
            queuePhoto(url, imageView);
        }
    }

    private void queuePhoto(String url, ImageView imageView) {
        PhotoToLoad p = new PhotoToLoad(url, imageView);
        executorService.submit(new PhotosLoader(p));
    }

    // 从sd卡或者从网络下载
    private Bitmap getBitmapFromFileOrNet(String url,boolean saveToSdCard) {

        String fileName = String.valueOf(url.hashCode());
        File f = new File(cacheDir, fileName);
        Bitmap b = null;
        if (f != null && f.exists()) {
            b = decodeFile(f);
        }
        if (b != null) {
            return b;
        }

        // 网络下载
        try {
            Bitmap bitmap = null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            if (saveToSdCard) {
            	OutputStream os = new FileOutputStream(f);
            	CopyStream(is, os);
            	os.close();
            	is.close();
			}
//            bitmap = decodeFile(f);
            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 从文件读取bitmap
    private Bitmap decodeFile(File f) {
        try {
            // decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);

            // Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE = 500;
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {
        }
        return null;
    }
    
    private Bitmap getGreyBitmapFromMemoryOrNew(String url,Bitmap bmpOriginal) {
    	String greyMapKey=url+"grey";
		Bitmap greyBitmap=memoryCache.get(greyMapKey);
		if (greyBitmap==null) {
			greyBitmap=bitmap2Gray(bmpOriginal);
			memoryCache.put(greyMapKey, greyBitmap);
		}
		return greyBitmap;
	}

    // Task for the queue
    private class PhotoToLoad {
        public String url;
        public ImageView imageView;

        public PhotoToLoad(String u, ImageView i) {
            url = u;
            imageView = i;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad) {
            this.photoToLoad = photoToLoad;
        }

        @Override
        public void run() {
            if (imageViewReused(photoToLoad)) {
                return;
            }
            Bitmap bmp = getBitmapFromFileOrNet(photoToLoad.url,false);
            memoryCache.put(photoToLoad.url, bmp);
            if (imageViewReused(photoToLoad)) {
                return;
            }

            if (type == 0) {
                photoToLoad.imageView.setImageBitmap(bmp);
            } else if (type == 1) {
                BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
                Activity a = (Activity) photoToLoad.imageView.getContext();
                a.runOnUiThread(bd);
            } else {
                photoToLoad.imageView.setImageBitmap(rotateImage(bmp));
            }

        }
    }

    /**
     * 防止图片错位
     * 
     * @param photoToLoad
     * @return
     */
    boolean imageViewReused(PhotoToLoad photoToLoad) {
        String tag = imageViews.get(photoToLoad.imageView);
        if (tag == null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    class BitmapDisplayer implements Runnable {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;

        public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
            bitmap = b;
            photoToLoad = p;
        }

        public void run() {
            if (imageViewReused(photoToLoad)) {
                return;
            }
            if (bitmap != null) {
                photoToLoad.imageView.setImageBitmap(getGreyBitmapFromMemoryOrNew(photoToLoad.url, bitmap));
            }
        }
    }

    public void clearCache() {
        memoryCache.clear();
    }

    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
            // Log.e("", "CopyStream catch Exception...");
        }
    }

    public static Bitmap rotateImage(Bitmap bmp) {

        if (bmp == null)
            return bmp;

        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float aspectRatio = ((float) height) / width;

        if (aspectRatio > 1) {
            // no need to rotate the image
            return bmp;
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = null;
        try {
            rotatedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height, matrix, false);

        } catch (OutOfMemoryError e) {
        }
        return rotatedBitmap;
    }
    
    public static Bitmap bitmap2Gray(Bitmap bmpOriginal) {
    	try {
    		int width, height;
    		height = bmpOriginal.getHeight();
    		width = bmpOriginal.getWidth();
    		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
    				Bitmap.Config.RGB_565);
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
			// TODO: handle exception
			return bmpOriginal;
		}catch (OutOfMemoryError e) {
			// TODO: handle exception
			return bmpOriginal;
		}
    }
}
