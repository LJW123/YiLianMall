package com.yilian.mall.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;

import com.google.zxing.common.BitMatrix;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orhanobut.logger.Logger;

import org.apache.http.Header;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {

	public static final String TAG = "ImageUtils=======";
	

	/**
	 * 根据宽度等比例缩放图片
	 * 
	 * @param defaultBitmap
	 * @return
	 */
	public static Bitmap resizeImageByWidth(Bitmap defaultBitmap,
			int targetWidth) {
		int rawWidth = defaultBitmap.getWidth();
		int rawHeight = defaultBitmap.getHeight();
		float targetHeight = targetWidth * (float) rawHeight / (float) rawWidth;
		float scaleWidth = targetWidth / (float) rawWidth;
		float scaleHeight = targetHeight / (float) rawHeight;
		Matrix localMatrix = new Matrix();
		localMatrix.postScale(scaleHeight, scaleWidth);
		return Bitmap.createBitmap(defaultBitmap, 0, 0, rawWidth, rawHeight,
				localMatrix, true);
	}
	/**
	 * 将给定图片维持宽高比缩放后，截取正中间的正方形部分。
	 * @param bitmap      原图
	 * @param edgeLength  希望得到的正方形部分的边长
	 * @return  缩放截取正中部分后的位图。
	 */
	public static Bitmap centerSquareScaleBitmap(Bitmap bitmap, int edgeLength)
	{
		if(null == bitmap || edgeLength <= 0)
		{
			return  null;
		}

		Bitmap result = bitmap;
		int widthOrg = bitmap.getWidth();
		int heightOrg = bitmap.getHeight();

		Logger.i(widthOrg+""+heightOrg+"heightOrg------");
		if(widthOrg >=edgeLength && heightOrg >= edgeLength)
		{
			//压缩到一个最小长度是edgeLength的bitmap
			int longerEdge = (int)(edgeLength * Math.max(widthOrg, heightOrg) / Math.min(widthOrg, heightOrg));
			int scaledWidth = widthOrg > heightOrg ? longerEdge : edgeLength;
			int scaledHeight = widthOrg > heightOrg ? edgeLength : longerEdge;
			Bitmap scaledBitmap;

			try{
				scaledBitmap = Bitmap.createScaledBitmap(bitmap, scaledWidth, scaledHeight, true);
			}
			catch(Exception e){
				return null;
			}

			//从图中截取正中间的正方形部分。
			int xTopLeft = (scaledWidth - edgeLength) / 2;
			int yTopLeft = (scaledHeight - edgeLength) / 2;

			try{
				result = Bitmap.createBitmap(scaledBitmap, xTopLeft, yTopLeft, edgeLength, edgeLength);
				scaledBitmap.recycle();
			}
			catch(Exception e){
				return null;
			}
		}

		return result;
	}

	/**
	 *  获取本地图片
	 * @param name 文件名称
	 * @return bitmap
	 */
	public static Bitmap getLoacalBitmap(String path,String name) {
		try {
			File f = new File(path);
			FileInputStream fis = new FileInputStream(path + name);
			return BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 *  获取网络图片，并保存
	 */
	public static void getNetworkBitmap(String url,final String path,final String bitmapName){
		AsyncHttpClient client= new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				if (statusCode == 200) {
					//创建工厂对象
					BitmapFactory bitmapFactory = new BitmapFactory();
					//工厂对象的decodeByteArray把字节转换成Bitmap对象
					Bitmap bitmap = bitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);
					File f = new File(Environment.getExternalStorageDirectory() + path);
					if (!f.exists()) {
						f.mkdirs();
					}
					File img = new File(f.getAbsolutePath() + "/"+bitmapName);
					try {
						FileOutputStream out = new FileOutputStream(img);
						bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
						out.flush();
						out.close();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				error.printStackTrace();
			}
		});

	}

	/**
	 * 生成条形码（一维码）
	 * @param byteMatrix
	 * @return
	 */
	public static Bitmap toBitmap(BitMatrix byteMatrix) { 
		// 定义位图的宽和高 
		int width = byteMatrix.getWidth(); 
		int height = byteMatrix.getHeight(); 
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (byteMatrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// 通过像素数组生成bitmap,具体参考api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;  
	}

	public static Bitmap markPhoto(BitmapDrawable mark,Bitmap photo){
		if (mark ==null)
			return photo;
		else {
			Bitmap photoMark = Bitmap.createBitmap(photo.getWidth(), photo.getHeight(), Bitmap.Config.ARGB_8888);
			Canvas canvas = new Canvas(photoMark);
			canvas.drawBitmap(photo, 0, 0, null);
			canvas.drawBitmap(mark.getBitmap(), photo.getWidth()/10, 20, null);

			canvas.save();
			canvas.restore();
			return photoMark;
		}

	}


}
