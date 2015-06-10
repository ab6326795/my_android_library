package com.pwdgame.util;

import java.util.List;

import com.pwdgame.util.Utility;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class TaskUtils {
	
    /**
     * 返回APP进程是否在运行，这里不管它在前台还是后台
     * @param context
     * @return
     */
    public static boolean isAppProcessRunning(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo appProcess : appProcesses) {
             if (appProcess.processName.equals(context.getPackageName())) {
                    /*if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                       return true;
                    }else{
                       return false;
                    }*/
            	 
            	 return true;
               }
        }
        return false;
    }

     /**
      * 判断某个服务是否在运行 ，ServiceInterfaces.class.getCanonicalName()
      * @param serviceClassName
      * @return
      */
	 public static boolean isServiceRunning(Context context,String serviceClassName){ 
	   final ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
	   final List<RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE); 
	
	   for (RunningServiceInfo runningServiceInfo : services) { 
		   //Logger.debug(runningServiceInfo.service.getClassName()+","+serviceClassName);
	       if (runningServiceInfo.service.getClassName().equals(serviceClassName)){ 
	           return true; 
	       } 
	   } 
	   return false; 
	}
	 
	 /**
	  * 判断当前是否在主进程运行
	  * @param context
	  * @return
	  */
	 public static boolean isMainProcess(Context context){
		 return context.getPackageName().equals(Utility.getCurProcessName(context));
	 }
}
