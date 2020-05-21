package com.yilian.mall.config;

import com.orhanobut.logger.Logger;

public class MyUncaughtExceptionHandler implements 
Thread.UncaughtExceptionHandler { 
	private Thread.UncaughtExceptionHandler a; 
	public MyUncaughtExceptionHandler(){ 
		this.a = Thread.getDefaultUncaughtExceptionHandler(); 
	} 
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		Logger.i("Exp", "ppppppppppppp=" + ex.getMessage());
		//是否抛出异常 
		//if(a!=null) 
		//a.uncaughtException(thread, ex); 
	} 
}
