/*
 * @description
 * BaseResult.java
 * classes:com.vcyber.drivingservice.eneity.BaseResult
 * @author yym create at 2014-10-30下午3:49:03
*/
package com.yilian.mall.entity;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.orhanobut.logger.Logger;
import com.yilian.networkingmodule.httpresult.HttpResultBean;

/**
 * @description:实体基类
 */

public class BaseEntity extends HttpResultBean {


	/**
	 * 旧版接口使用的message字段
	 */
	@SerializedName("message")
	public String message;

	public void parser(String src) {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public <T> T parserT(String src) {
		// TODO Auto-generated method stub
		if (src == null || src.length() == 0) {
			return null;
		}
		Logger.i(src);
		Logger.json(src);

		Gson gson = new Gson();
		return (T) gson.fromJson(src, this.getClass());
	}

}
