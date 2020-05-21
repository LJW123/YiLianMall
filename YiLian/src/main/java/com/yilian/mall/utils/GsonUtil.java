package com.yilian.mall.utils;

import android.content.Context;
import android.content.res.AssetManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Describe 
 * @Author leeandy007
 * @Date 2016-9-5 下午10:09:10
 */
@SuppressWarnings("unchecked")
public class GsonUtil {

	private static Gson GSON = new Gson();
	
	/**
	 * JSON转Bean
	 * */
	public static <T> T getBeanFromJson(String json, Class<?> clazz){
		return (T) GSON.fromJson(json, clazz);
	}
	
	/**
	 * JSON转List<T>
	 */
	public static <T> List<T> getListFromJson(String json, TypeToken<List<T>> tt) {
		return GSON.fromJson(json, tt.getType());
	}

	/**
	 * 根据key从json中取出value
	 * */
	public static String getJsonFromKey(String json, String key) {
		try {
			return getValue(new JSONObject(json), key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getValue(JSONObject json, String key) {
		if (json == null) {
			return "";
		}
		String str = null;
		try {
			str = json.getString(key);
		} catch (JSONException e) {
			str = "";
		}
		return str;
	}

	/**
	 * Bean转JSON
	 * */
	public static String setBeanToJson(Object object){
		return GSON.toJson(object);
	}

	/*从本地获取json数据*/
	public String getJson(Context context, String fileName) {

		StringBuilder stringBuilder = new StringBuilder();
		try {
			AssetManager assetManager = context.getAssets();
			BufferedReader bf = new BufferedReader(new InputStreamReader(
					assetManager.open(fileName)));
			String line;
			while ((line = bf.readLine()) != null) {
				stringBuilder.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

}
