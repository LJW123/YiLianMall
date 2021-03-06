/*
 * @description
 * BaseResult.java
 * classes:com.vcyber.drivingservice.eneity.BaseResult
 * @author yym create at 2014-10-30下午3:49:03
*/
package com.yilian.mylibrary;

import com.google.gson.Gson;

/**
 * @description:实体基类
 */
public class BaseEntity {

	public int code;

	/**
	 * 新版接口使用的msg字段
	 */
	public String msg;


	public void parser(String src) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public <T> T parserT(String src) {
		// TODO Auto-generated method stub
		if (src == null || src.length() == 0) {
			return null;
		}

		Gson gson = new Gson();
		return (T) gson.fromJson(src, this.getClass());
	}

}
