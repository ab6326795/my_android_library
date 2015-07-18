package com.pwdgame.app;

import android.content.Context;

import com.pwdgame.util.XYProperties;


public class AppConfig {

	 public final static String CONF_COOKIE = "cookie";
	 public static Context mContext;
	 
	 private static XYProperties xyProperties;
	 
	 /**
	  * 初始化，由application调用
	  * @param context
	  */
	 public static void init(Context context){
		 mContext = context;
		 xyProperties = XYProperties.getInstance(mContext);
	 }
	 
	 public static String getHttpCookie(){
		 return xyProperties.get(CONF_COOKIE);
	 }
	 
	 public static void setHttpCookie(String cookie){
		 xyProperties.set(CONF_COOKIE, cookie);
	 }
}
