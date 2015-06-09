package com.pwdgame.util;
import android.util.Log;

public class Logger {

	private static final String DEFAULT_TAG="con.pwdgame.library";
	public static final boolean DEBUG=true;
	
	public static void debug(String TAG,String info){
		if(DEBUG){
			Log.e(TAG, info);
		}
	}	
	public static void debug(String info){
		debug(DEFAULT_TAG, info);
	}
	
	public static void debug(Throwable e){
		debug(e.toString());
	}
	
	
}
