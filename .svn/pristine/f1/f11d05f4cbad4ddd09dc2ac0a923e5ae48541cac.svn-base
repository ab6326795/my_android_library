package com.pwdgame.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

public class NativeFunction {
	
	private static final String TAG="NativeFunction";

	static{
		System.loadLibrary("talker_native");
	}
	
	/**
	 * 添加卸载监听，卸载后打开网页
	 * @param mContext
	 * @param url
	 * @return 返回监听进程PID，失败-1
	 */
	public static int addUninstallListener(Context mContext,String url){
	    // 监听进程pid
	    int mObserverProcessPid = -1;
	    
		String packName=mContext.getPackageName();
		String component=Utility.getSystemActionComponent(mContext, Intent.ACTION_VIEW, Uri.parse(url));
		
	     // API level小于17，不需要获取userSerialNumber
        if (Build.VERSION.SDK_INT < 17){
            mObserverProcessPid = startObserver(packName,null,component,url);
        }
        // 否则，需要获取userSerialNumber
        else{
            mObserverProcessPid = startObserver(packName,getUserSerial(mContext),component,url);
        }
        return mObserverProcessPid;

	}

	// 由于targetSdkVersion低于17，只能通过反射获取
    private static String getUserSerial(Context mContext){
        Object userManager = mContext.getSystemService("user");
        if (userManager == null)
        {
            Log.e(TAG, "userManager not exsit !!!");
            return null;
        }
        
        try
        {
            Method myUserHandleMethod = android.os.Process.class.getMethod("myUserHandle", (Class<?>[]) null);
            Object myUserHandle = myUserHandleMethod.invoke(android.os.Process.class, (Object[]) null);
            
            Method getSerialNumberForUser = userManager.getClass().getMethod("getSerialNumberForUser", myUserHandle.getClass());
            long userSerial = (Long) getSerialNumberForUser.invoke(userManager, myUserHandle);
            return String.valueOf(userSerial);
        }
        catch (NoSuchMethodException e)
        {
            Log.e(TAG, "", e);
        }
        catch (IllegalArgumentException e)
        {
            Log.e(TAG, "", e);
        }
        catch (IllegalAccessException e)
        {
            Log.e(TAG, "", e);
        }
        catch (InvocationTargetException e)
        {
            Log.e(TAG, "", e);
        }
        
        return null;
    }
    
	/**
	 * 初始化
	 */
	public static native void init(String observerName,int interval);

	/**
	 * 启动卸载回调观察者、服务监听
	 * @param packName 包名
	 * @param userSerial 
	 * @param url 卸载打开的URL
	 * @return pid
	 */
	private static native int startObserver(String packName,String userSerial,String component,String url);
	
	/**
	 * 停止服务监听
	 */
	public static native void stopServiceObserver();
	
	/**
	 * 销毁服务
	 * @param packName
	 * @return
	 */
	public static native boolean destroyObserver(String packName); 
}
