package com.pwdgame.cache;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.pwdgame.library.BuildConfig;
/**
 * 文件缓存
 * @author 谢源
 *
 */
public class XYFileCache {
	private static final String TAG="XYFileCache";
	// 手机流量时，两小时过期
	public static final int CONFIG_CACHE_MOBILE_TIMEOUT = 2 * 60 * 60 * 1000;
	// wifi网络时，30分钟过期
	public static final int CONFIG_CACHE_WIFI_TIMEOUT = 30 * 60 * 1000;

	/**
	 * 读取缓存
	 * @param context
	 * @param url
	 * @param checkOutTime  是否对文件进行过期检查
	 * @return
	 */
	public static String getUrlCache(Context context, String url,boolean checkOutTime) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}

		File file = new File(getCacheDir(context) + File.separator
				+ replaceUrlWithPlus(url));
		//存在缓存文件
		if (file.exists() && file.isFile()) {
			
			//是否检测文件是否过期，过期返回null,不过期则读取
			if(checkOutTime){
				//获取网络类型
				int netState = NetUtil.getNetworkState(context);
				
				long expiredTime = System.currentTimeMillis() - file.lastModified();
				Log.i("liweiping", url + ": expiredTime=" + expiredTime / 1000);
				// 1. in case the system time is incorrect (the time is turn back
				// long ago)
				// 2. when the network is invalid, you can only read the cache
				if (netState != NetUtil.NETWORN_NONE && expiredTime < 0) {
					return null;
				}
				// 如果是wifi网络，则30分钟过期
				if (netState == NetUtil.NETWORN_WIFI
						&& expiredTime > CONFIG_CACHE_WIFI_TIMEOUT) {
					return null;
					// 如果是手机网络，则2个小时过期
				} else if (netState == NetUtil.NETWORN_MOBILE
						&& expiredTime > CONFIG_CACHE_MOBILE_TIMEOUT) {
					return null;
				}
			}
			try {
				String result = FileUtils.readTextFile(file);
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 设置文件缓存
	 * @param context
	 * @param data
	 * @param url
	 */
	public static void setUrlCache(Context context, String data, String url) {
		if (getCacheDir(context) == null) {
			return;
		}
		try {
			File file = new File(getCacheDir(context) + File.separator
					+ replaceUrlWithPlus(url));
			// 创建缓存数据到磁盘，就是创建文件
			FileUtils.writeTextFile(file, data);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * delete cahce file recursively
	 * 
	 * @param cacheFile
	 *            if null means clear cache function, or clear cache file
	 */
	public static void clearCache(Context context, File cacheFile) {
		if (cacheFile == null) {
			try {
				File cacheDir = getCacheDir(context);
				if (cacheDir.exists()) {
					clearCache(context, cacheDir);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (cacheFile.isFile()) {
			cacheFile.delete();
		} else if (cacheFile.isDirectory()) {
			File[] childFiles = cacheFile.listFiles();
			for (int i = 0; i < childFiles.length; i++) {
				clearCache(context, childFiles[i]);
			}
		}
	}

	
	/**
	 * 清除指定URL的文件缓存
	 * @param context
	 * @param url
	 */
	public static void clearCache(Context context, String url) {
		try {
			File cacheFile = new File(getCacheDir(context) + File.separator+ replaceUrlWithPlus(url));
			if(cacheFile!=null&&cacheFile.exists()){
				cacheFile.delete();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	/**
	 * 将指定的文件缓存设置为过期。(如果缓存文件存在SD卡，有些手机设置修改时间可能会失败。这里默认使用手机内存来缓存文件)
	 * 我们在无网络的时可以不去检查缓存是否过期，在有网络的时候则检查过期，过期后则从网络上加载。
	 * 
	 * @param context
	 * @param url
	 */
	public static void setCacheTimeOut(Context context,String url){
		try {
			File cacheFile = new File(getCacheDir(context) + File.separator+ replaceUrlWithPlus(url));
			if(cacheFile!=null&&cacheFile.exists()&&cacheFile.isFile()){				
				boolean b=cacheFile.setLastModified(System.currentTimeMillis()-CONFIG_CACHE_MOBILE_TIMEOUT+1);
				if(BuildConfig.DEBUG)
				   Log.e(TAG, "设置文件缓存超时："+b);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String replaceUrlWithPlus(String url) {
		// 1. 处理特殊字符
		// 2. 去除后缀名带来的文件浏览器的视图凌乱(特别是图片更需要如此类似处理，否则有的手机打开图库，全是我们的缓存图片)
		if (url != null) {
			return url.replaceAll("http://(.)*?/", "")
					.replaceAll("[.:/,%?&=]", "+").replaceAll("[+]+", "+");
		}
		return null;
	}

	public static File getCacheDir(Context context) {
		File cacheDir = new File(context.getFilesDir(),"cache");
		if(!cacheDir.exists()){
			cacheDir.mkdirs();
		}
	/*	if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheDir = new File(Environment.getExternalStorageDirectory()
					.getAbsolutePath() + File.separator + "talker"+File.separator+"talker_cache");
			if (!cacheDir.exists())
				cacheDir.mkdirs();
		}*/
		return cacheDir;
	}
}
