package com.yilian.mall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yilian.mall.MyApplication;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class PreferenceUtils {
	
	
	private final static String strPreferName = "UserInfor";

	public static void writeStrConfig(String key, String value, Context context) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String readStrConfig(String key, Context context) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getString(key, "");
	}

	public static String readStrConfig(String key, Context context, String defaultValue) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getString(key, defaultValue);
	}

	public static void writeLongConfig(String key, Long value, Context context) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putLong(key, value);
		editor.commit();
	}

	public static Long readLongConfig(String key, Context context) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getLong(key, 0);
	}

	public static Long readLongConfig(String key, Context context, Long defaultValue) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getLong(key, defaultValue);
	}

	public static int readIntConfig(String key, Context context, int defaultValue) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getInt(key, defaultValue);
	}

	public static void writeIntConfig(String key, int value, Context context) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putInt(key, value);
		editor.commit();
	}

	public static void writeBoolConfig(String key, Boolean value, Context context) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.putBoolean(key, value);
		editor.commit();
	}

	public static Boolean readBoolConfig(String key, Context context) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getBoolean(key, false);
	}

	public static Boolean readBoolConfig(String key, Context context, boolean defaultValue) {
		SharedPreferences settings = getSharedPreferences(context);
		return settings.getBoolean(key, defaultValue);
	}

	/**
	 * 读取对象
	 * @param key
	 * @param context
	 * @return
	 */
	public static Object readObjectConfig(String key, Context context){
		SharedPreferences settings = getSharedPreferences(context);
		String base64Str= settings.getString(key, null);
		if (base64Str==null||base64Str.length()==0){
			return null;
		}

		Object o=null;
		//读取字节
		byte[] objStr = Base64.decode(base64Str);
		//封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(objStr);
		try {
			//再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			//读取对象
			o = bis.readObject();
			bais.close();
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 保存序列化对象
	 * @param key
	 * @param object
	 * @param context
	 */
	public static void writeObjectConfig(String key, Serializable object, Context context){
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(object);
			// 将字节流编码成base64的字符窜
			String base64Str = Base64.encode(baos
					.toByteArray());
			SharedPreferences.Editor editor = getSharedPreferences(context).edit();
			editor.putString(key, base64Str);
			editor.commit();
			baos.close();
			oos.close();
		}catch (IOException e){
		}
	}

	private static SharedPreferences getSharedPreferences(Context context) {
		SharedPreferences settings = MyApplication.getInstance().getApplicationContext().getSharedPreferences(strPreferName, 0);
		return settings;
	}

	public static void RemoveStrConfig(String key, Context context) {
		SharedPreferences.Editor editor = getSharedPreferences(context).edit();
		editor.remove(key);
		editor.commit();
	}

	/**
	 * 把语言包存起来
	 * @param context
	 * @param info
	 * @return
	 */
	public static boolean saveLanguagePackae(Context context, String info) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.edit().putString("language_package", info).commit();
	}

	/**
	 * 取语言包
	 * @param context
	 * @return
	 */
	public static String getLanguaePakcage(Context context) {
		SharedPreferences preferences = getSharedPreferences(context);
		return preferences.getString("language_package", "");
	}


}
