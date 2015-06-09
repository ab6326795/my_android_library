package com.pwdgame.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.xmlpull.v1.XmlPullParserException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pwdgame.constant.PWDKeys;

public final class Utility {
	
	/**
	 * 检查桌面上是否有XX快捷方式，经测试小米无效
	 * @param mContext
	 * @return
	 */
	 public static boolean isAddShortCut(Context mContext,String appName) {

        boolean isInstallShortcut = false;
        ContentResolver cr = mContext.getContentResolver();

        int versionLevel = android.os.Build.VERSION.SDK_INT;
        String AUTHORITY = "com.android.launcher2.settings";
       
        //2.2以上的系统的文件文件名字是不一样的
        if (versionLevel >= 8) {
            AUTHORITY = "com.android.launcher2.settings";
        } else {
            AUTHORITY = "com.android.launcher.settings";
        }

        final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
                + "/favorites?notify=true");
        Cursor c = cr.query(CONTENT_URI,
                new String[] { "title", "iconResource" }, "title=?",
                new String[] { appName }, null);

        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        return isInstallShortcut;
    }



/*	*//**
	 * 创建桌面快捷方式，如果原来已经存在返回true，不存在返回false（返回false说明系统是第一次创建）
	 * 
	 * @param context
	 * @return
	 *//*
	public static boolean addShortCut(Activity context,String appName,int resIcon) {
		// 判断是否要添加快捷方式
		boolean IsCreateShort = SharedPreferenceUtil.getRecordBoolean( "is_create_shortcut");

		if (!IsCreateShort) {
			// 构建快捷方式的Intent
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.setClassName(context, context.getClass().getName());

			Intent addShortcut = new Intent(
					"com.android.launcher.action.INSTALL_SHORTCUT");
			// 快捷方式的名称
			addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,appName);
			// 构建快捷方式中的专门图标
			Parcelable icon = Intent.ShortcutIconResource.fromContext(context,resIcon);
			// 添加快捷方式图标
			addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
			// 添加快捷方式的Intent
			addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
			// duplicate created
			addShortcut.putExtra("duplicate", false);
			// 发送广播给launcher
			context.sendBroadcast(addShortcut);

			// 写入记录
			SharedPreferenceUtil.setRecordBoolean("is_create_shortcut", true);
			
		    //Toast.makeText(context, "创建桌面快捷方式成功", Toast.LENGTH_SHORT).show();		
		}
		return IsCreateShort;
	}*/

	/**
	 * 返回指定包的CODE（版本号）
	 * 
	 * @param context
	 * @param packagename
	 *            包名
	 * @return
	 */
	public static int getPackageVersionCode(Context context, String packagename) {
		int i1 = 0;
		PackageManager packagemanager = context.getPackageManager();
		try {
			i1 = packagemanager.getPackageInfo(packagename, 0).versionCode;
		} catch (NameNotFoundException namenotfoundexception) {
			namenotfoundexception.printStackTrace();
		}
		return i1;
	}
	/**
	 * 返回指定包的CODE（版本号）
	 * 
	 * @param context
	 * @param packagename
	 *            包名
	 * @return
	 */
	public static String getPackageVersionName(Context context, String packagename) {
		String i1 = null;
		PackageManager packagemanager = context.getPackageManager();
		try {
			i1 = packagemanager.getPackageInfo(packagename, 0).versionName;
		} catch (NameNotFoundException namenotfoundexception) {
			namenotfoundexception.printStackTrace();
		}
		return i1;
	}
	/**
	 * 返回某个程序包的安装位置
	 * 
	 * @param packageName
	 * @param context
	 * @return auto" | "internalOnly" | "preferExternal" 0 1 2
	 */
	public static int GetInstallLocation(String packageName, Context context) {
		XmlResourceParser xmlresourceparser;
		int i1;
		int j1;
		int k1;
		int l1;
		byte byte0 = 1;

		try {
			xmlresourceparser = context.createPackageContext(packageName, 0)
					.getAssets().openXmlResourceParser("AndroidManifest.xml");
			i1 = xmlresourceparser.getEventType();

			for (j1 = i1; j1 != XmlResourceParser.END_DOCUMENT; j1 = xmlresourceparser
					.nextToken()) {
				if (j1 == XmlResourceParser.START_TAG) {
					if (!xmlresourceparser.getName().matches("manifest"))
						break;

					for (k1 = 0; k1 < xmlresourceparser.getAttributeCount(); k1++) {
						if (xmlresourceparser.getAttributeName(k1).matches(
								"installLocation")) {
							l1 = Integer.parseInt(xmlresourceparser
									.getAttributeValue(k1));
							if (l1 == 0) {
								byte0 = 0;
							} else if (l1 == 1) {
								byte0 = 1;
							} else if (l1 == 2) {
								byte0 = 2;
							}
						}
					}
				}
			}
		} catch (XmlPullParserException xmlpullparserexception) {
			byte0 = 1;
			xmlpullparserexception.printStackTrace();
		} catch (IOException ioexception) {
			byte0 = 1;
			ioexception.printStackTrace();
		} catch (NameNotFoundException namenotfoundexception) {
			byte0 = 1;
			namenotfoundexception.printStackTrace();
		} catch (Exception exception) {
			byte0 = 1;
			exception.printStackTrace();
		}

		return byte0;
	}

	/**
	 * 将文件大小long（B，字节）转成相适宜的单位描述
	 * 
	 * @param codeSize
	 *            字节大小
	 * @return
	 */
	public static String transCodesize2String(long codeSize) {
		String s = null;
		if (codeSize < 0x40000000L) {
			if (codeSize >= 0x100000L) {
				int j1 = String.valueOf((float) codeSize / 1048576F).indexOf(
						".");
				s = (new StringBuilder())
						.append((new StringBuilder())
								.append((float) codeSize / 1048576F)
								.append("000").toString().substring(0, j1 + 3))
						.append("MB").toString();
			} else if (codeSize >= 1024L) {
				int i1 = String.valueOf((float) codeSize / 1024F).indexOf(".");
				s = (new StringBuilder())
						.append((new StringBuilder())
								.append((float) codeSize / 1024F).append("000")
								.toString().substring(0, i1 + 3)).append("KB")
						.toString();
			} else if (codeSize < 1024L)
				s = (new StringBuilder()).append(Long.toString(codeSize))
						.append("B").toString();
		} else {
			int k1 = String.valueOf((float) codeSize / 1.073742E+009F).indexOf(
					".");
			s = (new StringBuilder())
					.append((new StringBuilder())
							.append((float) codeSize / 1.073742E+009F)
							.append("000").toString().substring(0, k1 + 3))
					.append("GB").toString();
		}
		return s;
	}

	/**
	 * 返回设备上已经安装的所有APP包名版本号拼接的字符串
	 * 
	 * @param context
	 * @return com.app.app|12,com.app2.app2|3232 ...
	 */
	public static String PrepareAppUpdateListParam(Context context) {
		// 返回已安装在设备上的所有包的列表。
		List<PackageInfo> list = context.getPackageManager()
				.getInstalledPackages(PackageManager.PERMISSION_GRANTED);
		StringBuilder stringbuilder = new StringBuilder();
		Iterator<PackageInfo> iterator = list.iterator();
		for (boolean flag = true; iterator.hasNext(); flag = false) {
			PackageInfo packageinfo = (PackageInfo) iterator.next();
			if (!flag)
				stringbuilder.append(",");
			stringbuilder.append(packageinfo.packageName).append("|")
					.append(packageinfo.versionCode);
		}

		return stringbuilder.toString();
	}

	// a
	public static String toString(Object obj) {
		String s;
		if (obj == null || " ".equals(obj) || "".equals(obj))
			s = null;
		else
			s = obj.toString();
		return s;
	}

	// a
	public static String replaceX(String s) {
		String s1;
		if (s == null || TextUtils.isEmpty(s.trim()))
			s1 = (new StringBuilder()).append(System.currentTimeMillis())
					.append("").toString();
		else
			s1 = s.replaceAll("/", "").replace(":", "").replace("?", "")
					.replace(".", "");
		return s1;
	}

	// a
	public static void addd(Context context, Class class1, String resourceId,
			String fromModule) {
		Intent intent = new Intent(context, class1);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("resource_id_intent", resourceId);
		intent.putExtra("from_module_intent", fromModule);
		context.startActivity(intent);
	}

	/**
	 * 返回指定路径的APK文件是否可用 通过能否获取包名就能检查出来
	 * 
	 * @param context
	 * @param file
	 *            文件对象
	 * @return true可用，false不可用
	 */
	public static boolean isApkFileValid(Context context, File file) {
		boolean flag = false;
		if (file.exists()) {
			String s1 = getPackageName(context, file.getAbsolutePath());
			if (!Utility.isNullOrEmpty(s1))
				flag = true;
		}
		return flag;
	}

	// a
	public static String[] aggg(Uri uri) {
		String as[] = new String[2];
		if (uri == null) {
			as[0] = "2";
			as[1] = "";
		} else if ("details".equals(uri.getHost())) {
			as[0] = "1";
			as[1] = uri.getQueryParameter("id");
		} else {
			as[0] = "2";
			String s = uri.getQueryParameter("q");
			if (s == null)
				as[1] = "";
			else if (s.startsWith("pname:"))
				as[1] = s.substring("pname:".length());
			else if (s.startsWith("pub:"))
				as[1] = s.substring("pub:".length());
			else
				as[1] = s;
		}
		return as;
	}

	// b
	/**
	 * 检查设备是否安装了包名为pagename的APP，包括已经删除的！！！
	 * 
	 * @param paramContext
	 * @param packageName
	 * @return 安装了true,否则false
	 */
	public static boolean isPkgInstalled(Context paramContext,
			String packageName) {
		boolean flag = false;
		try {
			PackageInfo localPackageInfo = paramContext.getPackageManager()
					.getPackageInfo(packageName.toLowerCase(), 0);
			if (localPackageInfo != null)
				flag = true;
		} catch (NameNotFoundException localNameNotFoundException) {
			// break label20;
			//localNameNotFoundException.printStackTrace();
		}
		return flag;
	}
	

	/**
	 * 弹出安装界面
	 * 
	 * @param context
	 * @param filePath
	 *            文件路径
	 */
	public static void popUpInstall(Context context, String filePath) {
		if (filePath != null) {
			Intent intent = new Intent("android.intent.action.VIEW");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(Uri.fromFile(new File(filePath)),
					"application/vnd.android.package-archive");
			context.startActivity(intent);
		}
	}
	// d
	/**
	 * 搜索包管理器，查找包名为s的APPlication，然后设置组件名为该android:name属性名称 跳转到该包的主Activity
	 * 
	 * @param context
	 * @param s
	 */
	public static void openApplication(Context context, String s) {
		/*
		 * ResolveInfo这个类是通过解析一个与IntentFilter相对应的intent得到的信息。
		 * 它部分地对应于从AndroidManifest.xml的< intent>标签收集到的信息。
		 */
		Iterator<ResolveInfo> iterator;
		String s1;
		PackageManager packagemanager = context.getPackageManager();
		Intent intent = new Intent("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");
		// 检索可以为给定的意图进行的所有活动。intent为意图所需的原意，
		iterator = packagemanager.queryIntentActivities(intent, 0).iterator();

		do {
			if (!iterator.hasNext()) {
				s1 = "";
				break;
			} else {
				ActivityInfo activityinfo = ((ResolveInfo) iterator.next()).activityInfo;
				if (!activityinfo.packageName.equals(s)) {
					continue;
				} else {
					// "android:name"
					s1 = activityinfo.name;
					break;
				}
			}
		} while (true);

		if (!"".equals(s1)) {
			Intent intent1 = new Intent();
			intent1.setComponent(new ComponentName(s, s1));
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent1);
		}
	}

	/**
	 * 卸载
	 */
	public static void unInstall(Context context, String s) {
		Intent intent = new Intent("android.intent.action.DELETE",
				Uri.fromParts("package", s, null));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 返回网络状况
	 * 
	 * @param context
	 * @return 有网络true,没有false
	 */
	public static boolean checkNetWork(Context context) {
		NetworkInfo networkinfo = ((ConnectivityManager) context
				.getSystemService("connectivity")).getActiveNetworkInfo();
		boolean flag;
		if (networkinfo != null)
			flag = networkinfo.isConnectedOrConnecting();
		else
			flag = false;
		return flag;
	}

	/**
	 * 跳转到无线网（Wifi）设置页面
	 * 
	 * @param context
	 */
	public static void wirelessSetting(Context context) {
		Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}
	
	public static void startWireless(Context context){
		if(android.os.Build.VERSION.SDK_INT > 10) {
			// 3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面 
			context.startActivity(new Intent( android.provider.Settings.ACTION_SETTINGS)); 
			}else { 
			context.startActivity(new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS)); 
		}

	}

	/**
	 * 获取AndroidManifest.xml定义的某个key的属性值
	 * 
	 * @param paramContext
	 *            上下文对象
	 * @param key
	 *            要检索的KEY名
	 * @return Object
	 */
	public static Object getManiMetaData(Context paramContext, String key) {
		Object localObject = "";
		PackageManager localPackageManager = paramContext.getPackageManager();
		try {
			Bundle localBundle = localPackageManager
					.getApplicationInfo(paramContext.getPackageName(),
							PackageManager.GET_META_DATA).metaData;
			if (localBundle != null) {
				localObject = localBundle.get(key);
			}
		} catch (NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
			Log.e("Not Found",
					"An application with the given package name can not be found on the system.");
		}
		return localObject;
	}

	// j
	public static void jhhhh(Context context, String s) {
		Intent intent = new Intent();
		int i1 = VERSION.SDK_INT;
		if (i1 >= 9) {
			intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
			intent.setData(Uri.fromParts("package", s, null));
		} else {
			String s1;
			if (i1 == 8)
				s1 = "pkg";
			else
				s1 = "com.android.settings.ApplicationPkgName";
			intent.setAction("android.intent.action.VIEW");
			intent.setClassName("com.android.settings",
					"com.android.settings.InstalledAppDetails");
			intent.putExtra(s1, s);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 返回当前系统是否正在运行pagename为s的APP
	 * 
	 * @param context
	 * @param s
	 *            包名
	 * @return
	 */
	public static boolean lgggg(Context context, String s) {
		boolean flag;
		if (context
				.checkCallingOrSelfPermission("android.permission.GET_TASKS") != PackageManager.PERMISSION_GRANTED) {
			flag = false;
		} else {
			List<RunningTaskInfo> list = ((ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE))
					.getRunningTasks(1);
			if (list.size() > 0
					&& s.equals(((RunningTaskInfo) list.get(0)).topActivity
							.getPackageName()))
				flag = true;
			else
				flag = false;
		}
		return flag;
	}

	// m
	/**
	 * 检索有关一个包的归档文件中定义的应用程序包的全部信息
	 * 
	 * @param context
	 * @param archiveFilePath
	 *            存档文件
	 * @return
	 */
	private static String getPackageName(Context context, String archiveFilePath) {
		PackageInfo packageinfo = context.getPackageManager()
				.getPackageArchiveInfo(archiveFilePath, 1);
		String s1 = "";
		if (packageinfo != null)
			s1 = packageinfo.packageName;
		return s1;
	}

	/**
	 * 检查s是否为null或者“”
	 * 
	 * @param s
	 *            需要检查的字符串
	 * @return true为空，false不为空
	 */
	public static boolean isNullOrEmpty(String s) {
		boolean flag;
		flag = true;
		if (s != null && !"".equals(s.trim()))
			flag = false;
		return flag;
	}

	/**
	 * 返回SD卡目录下filename文件的File对象
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static File newFile(String fileName) {
		return new File(Environment.getExternalStorageDirectory(), fileName);
	}

	// a
	public static String dealException(Exception paramException) {
		ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		PrintWriter localPrintWriter = new PrintWriter(
				localByteArrayOutputStream);
		paramException.printStackTrace(localPrintWriter);
		localPrintWriter.close();
		try {
			localByteArrayOutputStream.close();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return localByteArrayOutputStream.toString();
	}

	// a
	/**
	 * Closeable 是可以关闭的数据源或目标。调用 close 方法可释放对象保存的资源（如打开文件）。
	 * 此方法用于简化IO关闭操作，实现了关闭的封装
	 */
	public static void closeSth(Closeable paramCloseable) {
		if (paramCloseable != null)
			try {
				paramCloseable.close();
			} catch (IOException localIOException) {
				Log.e("IOUtilities", "Could not close stream", localIOException);
			}
	}

	/**
	 * 此方法用于检查设备是否挂载有SD卡
	 * 
	 * @return true有，false没有
	 */
	public static boolean hasSdcard() {
		boolean flag = true;
		if (Environment.getExternalStorageState().equals("mounted"))
			flag = true;
		else
			flag = false;

		return flag;
	}

	/**
	 * 此方法返回当前实现-paramLong的毫秒数
	 * 
	 * @param paramLong
	 *            开始时间
	 * @return
	 */
	public static float getSeconds(long paramLong) {
		return (float) (System.currentTimeMillis() - paramLong) / 1000.0F;
	}

	/**
	 * 打开任意文件
	 * 
	 * @param mContext
	 * @param f
	 */
	public static void openFile(Context mContext, File f) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);

		/* 调用getMIMEType()来取得MimeType */
		String type = getMIMEType(f);
		/* 设置intent的file与MimeType */
		intent.setDataAndType(Uri.fromFile(f), type);
		mContext.startActivity(intent);
	}

	/* 判断文件MimeType的method */
	private static String getMIMEType(File f) {
		String type = "";
		String fName = f.getName();
		/* 取得扩展名 */
		String end = fName
				.substring(fName.lastIndexOf(".") + 1, fName.length())
				.toLowerCase();

		/* 依扩展名的类型决定MimeType */
		if (end.equals("m4a") || end.equals("mp3") || end.equals("mid")
				|| end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
			type = "audio";
		} else if (end.equals("3gp") || end.equals("mp4")) {
			type = "video";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg") || end.equals("bmp")) {
			type = "image";
		} else if (end.equals("apk")) {
			/* android.permission.INSTALL_PACKAGES */
			type = "application/vnd.android.package-archive";
		} else if (end.equals("zip")) {
			type = "application/zip";
		} else if (end.equals("rar")) {
			type = "application/x-rar-compressed";
		} else if (end.equals("txt")) {
			type = "text/plain";
		} else {
			type = "*";
		}
		/* 如果无法直接打开，就跳出软件列表给用户选择 */
		if (end.equals("apk")) {
		} else {
			type += "/*";
		}
		return type;
	}
	/**
	 *文件选择器 
	 */
	public static void showFileChooser(Activity activity,String directory){
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);  
	    //intent.setType("*/*");  
	    intent.setDataAndType(Uri.fromFile(new File(directory)),"*/*");
	    intent.addCategory(Intent.CATEGORY_OPENABLE);  
	    try {  
	    	activity.startActivity(Intent.createChooser(intent, "请选择文件管理器"));  
	    } catch (android.content.ActivityNotFoundException ex) {  
	        // Potentially direct the user to the Market with a Dialog  
	        Toast.makeText(activity, "请安装文件管理器", Toast.LENGTH_SHORT)  
	                .show();  
	    }  
	}
	
	/**
	 * 数字转积分，如1000转为1千，10000转为1W，15000转为1万5
	 * @param number
	 * @return
	 */
	public static String NumberToIntegral(int number){

		String temp= String.valueOf(number);
		//如果数值都没上百，那么直接返回
		if(temp.length()<3)
		   return temp;
		
		String[] unit={"","","百","千","万"};

		int end=(temp.length()>4? temp.length()-4:1);
		int uintIndex=(temp.length()>4? 4:temp.length()-1);
		//4万3
		String result=temp.substring(0,end)
				 + unit[uintIndex]+temp.substring(end,end+1).replace("0", "");
		//如果取所有详细的，就需要截取剩余的，移除0即可。这里取大概的
		//result=result.replaceAll("0", "");
		
		return result;
	}

	/**
	 * 根据包名启动APP
	 * @param mContext
	 * @param pack
	 */
	public static void startApp(Context mContext,String pack){
		Intent newTask=mContext.getPackageManager().getLaunchIntentForPackage(pack);
		newTask.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(newTask);
	}
	
	/**
	 * 显示通知
	 * @param mContext
	 * @param tickerText
	 * @param contentTitle
	 * @param contentText
	 * @param intent
	 */
	public static void showNotification(Context mContext,int id,int resIcon,String tickerText,String contentTitle,String contentText,Intent intent){
         
         NotificationManager manager=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
 		
 		Notification notification=new Notification(resIcon,tickerText,System.currentTimeMillis());	    
		notification.setLatestEventInfo(mContext, contentTitle, contentText,
				                     PendingIntent.getActivity(mContext, 0, intent, 0));
		notification.flags = notification.FLAG_AUTO_CANCEL|notification.DEFAULT_VIBRATE|notification.DEFAULT_LIGHTS;
		manager.notify(id, notification);
		
	}

	/**
	 * 取消指定ID的通知
	 * @param mContext
	 * @param id 当id==-1则取消本程序所有通知，否则为取消指定ID的通知
	 */
	public static void cancelNotification(Context mContext,int id){
		NotificationManager manager=(NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		if(id==-1)
		    manager.cancelAll();
		else
			manager.cancel(id);
	}
	
	/**
	 * 将PX转成dp
	 * @param context
	 * @param px
	 * @return
	 */
	public static int pxToDip(Context context,float px){
		return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics()));
/*		float desity=context.getResources().getDisplayMetrics().density;
		return (int)(px/desity+0.5f);*/
	}
	public static int dipToPx(Context context,float dip){
		 return Math.round(TypedValue.applyDimension(
	                TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics()));
	/*	float desity=context.getResources().getDisplayMetrics().density;
		return (int)(dip*desity+0.5f);*/
	}
	
	/*
	 * 执行LINUX命令
	 * 参数：是否以ROOT身份运行，命令...
	 * 成功true,失败false
	 */
	public static boolean executeCmd(boolean isRootRun,String... Command)
	{
		return executeCmdEx(isRootRun, "", Command);
	}
	/*
	 * 执行LINUX命令
	 * 参数：是否以ROOT身份运行，命令...
	 * 成功true,失败false
	 */
	public static boolean executeCmdEx(boolean isRootRun,String result,String... Command)
	{
		Process process = null;
		DataOutputStream os = null;
		DataInputStream is=null;
		boolean bResult=false;
		
		try {
			process = Runtime.getRuntime().exec(isRootRun? "su":"sh");
			os = new DataOutputStream(process.getOutputStream());
			
			for(int i=0;i<Command.length;i++)
			   os.writeBytes(Command[i] + "\n");
			os.writeBytes("exit\n");
			os.flush();
			
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			is = new DataInputStream(process.getErrorStream());
			int read=-1;
			while((read=is.read())!=-1){
				bos.write(read);
			}			
			//检查是否有错误信息，没有则则执行成功
			String temp = new String(bos.toByteArray()).toLowerCase();
			if (temp.trim().equals(result)){
				bResult = true; 
			}
			process.waitFor();
		} catch (Exception e) {
			Log.e("***DEBUG***", "ROOT FAL "+e.getMessage());
			bResult = false;
		}
		finally{
			try {
				if (os != null){
					os.close();
				}
				if (process != null){
					process.destroy();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return bResult;
	}
	
	/**
	 * 分享文件
	 * @param mContext
	 * @param file
	 */
	public static void ShareFile(Context mContext,File file){

		Intent intent=new Intent(Intent.ACTION_SEND);					
		 //png
		 String getSuffix=getMIMEType(file);
		//拷贝文件到SD卡才能分享? 
		 intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		 intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     mContext.startActivity(Intent.createChooser(intent, "选择分享方式"));
	}
	
	/**
	 * 分享
	 * @param mContext
	 * @param title
	 * @param text
	 */
	public static void ShareText(Context mContext,String chooseTitle,String title,String text){
		 Intent intent=new Intent(Intent.ACTION_SEND);   
         intent.setType("text/plain");   
         intent.putExtra(Intent.EXTRA_SUBJECT, title);   
         intent.putExtra(Intent.EXTRA_TEXT, text);   
         
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
         mContext.startActivity(Intent.createChooser(intent,chooseTitle));   
	}
	
	/**
	 * 打开URL
	 * @param mContext
	 * @param url
	 */
	public static void openUrl(Context mContext,String url){
		Uri uri=Uri.parse(url);
		Intent intent=new Intent(Intent.ACTION_VIEW, uri);  
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	
	}
	

	/**
	 * 启动Intent，并设置传递参数
	 * @param context1
	 * @param activity
	 * @param object Serializable对象，没有为null
	 */
	public static void startIntent(Context context1,Class<?> activity,Serializable... object){
		startIntentTarget(context1, activity, null, object);
	}
	/**
	 * 启动Intent，并设置初始目标，并传递参数
	 * @param context1
	 * @param direction 初始目标，一般是activity
	 * @param activity
	 * @param object Serializable对象，没有为null
	 */
	public static void startIntentTarget(Context context1,Class<?> activity,Class<?> target,Serializable... object){
		Intent intent=new Intent(context1,activity);

		if(object!=null&&object.length>0){
			for(int i=0;i<object.length;i++)
			  intent.putExtra(PWDKeys.INTENT_DATA+i, object[i]);
		}
		if(target!=null){
			intent.putExtra(PWDKeys.TARGET, target);
		}
		
		context1.startActivity(intent);
	}
	
	/**
	 * 启动Intent，并设置初始目标，并传递参数
	 * @param context1
	 * @param direction 初始目标，一般是activity
	 * @param activity
	 * @param object Serializable对象，没有为null
	 */
	public static Intent getIntentHasTarget(Context context1,Class<?> activity,Class<?> target,Serializable... object){
		Intent intent=new Intent(context1,activity);

		if(object!=null&&object.length>0){
			for(int i=0;i<object.length;i++)
			  intent.putExtra(PWDKeys.INTENT_DATA+i, object[i]);
		}
		if(target!=null){
			intent.putExtra(PWDKeys.TARGET, target);
		}
		
		return intent;
	}
	/**
	 * 启动Intent，并设置传递参数
	 * @param context1
	 * @param activity
	 * @param object Serializable对象，没有为null
	 */
	public static void startIntentForResult(Activity mActivity,Class<?> activity,int requestCode,Serializable... object){
		Intent intent=new Intent(mActivity,activity);

		if(object!=null&&object.length>0){
			for(int i=0;i<object.length;i++)
			  intent.putExtra(PWDKeys.INTENT_DATA+i, object[i]);
		}
		mActivity.startActivityForResult(intent, requestCode);;
	}
	/**
	 * 启动Intent，并设置传递参数
	 * @param context1
	 * @param activity
	 * @param object 基本数据类型(可以为String,integer,boolean)，没有为null
	 */
	public static void startIntentA(Context context1,Class<?> activity,Object... object){
		Intent intent=new Intent(context1,activity);

		if(object!=null&&object.length>0){
			for(int i=0;i<object.length;i++){
			   if(object[i] instanceof String)
			      intent.putExtra(PWDKeys.INTENT_DATA+i, String.valueOf(object[i]));
			   else if(object[i] instanceof Integer)
				  intent.putExtra(PWDKeys.INTENT_DATA+i, Integer.valueOf(object[i].toString()));
			   else if(object[i] instanceof Boolean)
				   intent.putExtra(PWDKeys.INTENT_DATA+i, Boolean.valueOf(object[i].toString()));
			   else if(object[i] instanceof Double)
				   intent.putExtra(PWDKeys.INTENT_DATA+i, Double.valueOf(object[i].toString()));
			   else if(object[i] instanceof Parcelable)
				   intent.putExtra(PWDKeys.INTENT_DATA+i, (Parcelable)object[i]);
			}
		}
		context1.startActivity(intent);
	}
	
	
	 /**
	  * 失败返回NULL,成功非NULL
	  * @return
	  */
	public static String getTotalMemory(){
		String file="/proc/meminfo";
		String str2;
		String[] arrayOfString;
		FileReader localFileReader=null;
		BufferedReader localBufferedReader=null;
		
		
		  try
		  {
		   localFileReader = new FileReader(file);
		   localBufferedReader = new BufferedReader(localFileReader, 8192);
		   str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

		   arrayOfString = str2.split("\\s+");
		   /*for (String num : arrayOfString)
		   {
		    Log.i(str2, num + "\t");
		   }*/
		   String memory=Utility.transCodesize2String(Long.parseLong(arrayOfString[1])*1024);
		   return memory;
		   //return arrayOfString[1]+arrayOfString[2];
		  }catch(Exception e){
			  e.printStackTrace();
		  }finally{
			  try {
				  if(localBufferedReader!=null)
					  localBufferedReader.close();
				  if(localFileReader!=null)
					  localFileReader.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}

		  }
		  return null;
	}
	
	public static String getCPUName(){
		   FileReader fr = null;
	        BufferedReader br = null;
	        try
	        {
	            fr = new FileReader("/proc/cpuinfo");
	            br = new BufferedReader(fr);
	            String text = br.readLine();
	            String[] array = text.split(":\\s+", 2);
	            for (int i = 0; i < array.length; i++)
	            {
	            }
	            return array[1];
	        } catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	        } catch (IOException e)
	        {
	            e.printStackTrace();
	        } finally
	        {
	            if (fr != null)
	                try
	                {
	                    fr.close();
	                } catch (IOException e)
	                {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	            if (br != null)
	                try
	                {
	                    br.close();
	                } catch (IOException e)
	                {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
	        }
	        return null;
	}
	

	/**
	 * 显示Animation
	 * @param context
	 * @param view
	 * @param anim
	 * @param endRunnable
	 */
	public static void startAnimation(Context context,View view,int anim,final Runnable endRunnable){
		Animation animation=AnimationUtils.loadAnimation(context, anim);
		animation.setAnimationListener(new AnimationListener(){

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if(endRunnable!=null)
				    endRunnable.run();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
		});
		view.startAnimation(animation);
	}
	
	/**
	 * 获取APK签名的HashCode
	 * @param context
	 * @return 失败-1 成功非-1
	 */
	public static int getSignature(Context context,String packName){
		PackageManager pm=context.getPackageManager();
		try {
			PackageInfo info=pm.getPackageInfo(packName, PackageManager.GET_SIGNATURES);
			
			Signature[] signature= info.signatures;
			if(signature!=null){
				return signature[0].hashCode();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		return -1;
	}
	
	/**
	 * 获取classes.dex的CRC 
	 * @param context
	 * @return 失败-1 成功非-1
	 */
	public static long getDexFileCrc(Context context){
		String apkPath=context.getPackageCodePath();
		try {
	
			ZipFile zipFile=new ZipFile(apkPath);
			ZipEntry zipEntry=zipFile.getEntry("classes.dex");
			if(zipEntry!=null){
				return zipEntry.getCrc();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return -1;
	}
	/**
	 * 将long转成时间，如：昨天 12:24 ，2014-12-10 12:20:20
	 * @param date
	 * @return
	 */
	public static String transMillisToString(long millis){
		 

		StringBuilder builder=new StringBuilder();

		Date millisDate=new Date(millis);
		
		Date currentDate=new Date();
		Calendar mCalendar=Calendar.getInstance();
		mCalendar.setTime(currentDate);		
		mCalendar.add(Calendar.DAY_OF_MONTH, 1);
		mCalendar.set(Calendar.HOUR_OF_DAY, 0);
		mCalendar.set(Calendar.MINUTE, 0);
				
		//小时
		int millisHour=millisDate.getHours();		
		long cha=mCalendar.getTimeInMillis()-millis;
		if(cha<86400000){
			//builder.append("今天");
			builder.append(transMillisToGreet(millisHour));
			builder.append(new SimpleDateFormat("h:mm").format(millis));
		}else if(cha<172800000){
			builder.append("昨天");
			//builder.append(transMillisToGreet(millisHour));
			builder.append(new SimpleDateFormat("H:mm").format(millis));
		}else if(cha<259200000){
			builder.append("前天");
			//builder.append(transMillisToGreet(millisHour));
			builder.append(new SimpleDateFormat("H:mm").format(millis));
		}else if(currentDate.getYear()==millisDate.getYear()){
			builder.append(new SimpleDateFormat("MM月dd日 HH:mm").format(millis));
		}else{
			builder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(millis));
		}
		

		 return builder.toString();
	}
	/**
	 * 将long转成时间，如：昨天12:24 ，2014-12-10
	 * @param date
	 * @return
	 */
	public static String transMillisToStringEx(long millis){
		 

		StringBuilder builder=new StringBuilder();

		Date millisDate=new Date(millis);
		
		Date currentDate=new Date();
		Calendar mCalendar=Calendar.getInstance();
		mCalendar.setTime(currentDate);		
		mCalendar.add(Calendar.DAY_OF_MONTH, 1);
		mCalendar.set(Calendar.HOUR_OF_DAY, 0);
		mCalendar.set(Calendar.MINUTE, 0);
				
		//小时
		int millisHour=millisDate.getHours();		
		long cha=mCalendar.getTimeInMillis()-millis;
		if(cha<86400000){
			//builder.append("今天");
			builder.append(transMillisToGreet(millisHour));
			builder.append(new SimpleDateFormat("h:mm").format(millis));
		}else if(cha<172800000){
			builder.append("昨天");
			//builder.append(transMillisToGreet(millisHour));
			builder.append(new SimpleDateFormat("H:mm").format(millis));
		}else if(cha<259200000){
			builder.append("前天");
			//builder.append(transMillisToGreet(millisHour));
			builder.append(new SimpleDateFormat("H:mm").format(millis));
		}else if(currentDate.getYear()==millisDate.getYear()){
			builder.append(new SimpleDateFormat("MM月dd日").format(millis));
		}else{
			builder.append(new SimpleDateFormat("yyyy-MM-dd").format(millis));
		}
		

		 return builder.toString();
	}
	private static String transMillisToGreet(int hour){
		
		if(hour>=0&&(hour<6||hour==24)){
			return "凌晨";
		}else if(hour>=6&&hour<12){
			return "上午";
		}else if(hour>=12&&hour<18){
			return "下午";
		}else if(hour>=18&&hour<24){
			return "晚上";
		}
		
		return "";
	}
	
	/**
	 * 将long转成时间，如：昨天 12:24 ，2014-12-10 12:20:20
	 * @param date
	 * @return
	 *//*
	public static String transMillisToString(long millis){
		 
		 Date currentDate=new Date();
		 Date millisDate=new Date(millis);
		 String result="";
		 //5分钟以内的电话
		 if(currentDate.getTime()-millisDate.getTime()<300000){
			 result="刚刚";
		 }
		 else if(currentDate.getYear()==millisDate.getYear()
				 &&currentDate.getMonth()==millisDate.getMonth()&&
				 currentDate.getDay()-millisDate.getDay()<=1){
			 if(currentDate.getDay()-millisDate.getDay()==0){
				 result="今天 ";
			 }else{
				 result="昨天 ";
			 }
			 result+=new String().format("%02d:%02d", millisDate.getHours(),millisDate.getMinutes());
		 }else if(currentDate.getYear()==millisDate.getYear()){
			 //今年的电话
			 result=new SimpleDateFormat("M月d号").format(millisDate);
		 }else {
			 //不是今年的就显示全部
			 SimpleDateFormat sfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
			 result=sfd.format(millis);
		 }
		 return result;
	}
	*/
	/**
	 * 将时长转成时分秒，如2分20秒
	 * @param millis
	 * @return
	 */
	public static String transDurationToString(long millis,boolean showHours){
		StringBuilder builder=new StringBuilder();
				
		long minutes=0;
		long hours=0;
		
		if(millis/60>0){
			minutes=millis/60;
			millis=millis%60;			
		}
		if(minutes/60>0){
			hours=minutes/60;
			minutes=minutes%60;
		}
		
		if(showHours||hours>0){
			builder.append(hours);
			builder.append("小时");
		}
		if(minutes>0){
			builder.append(minutes);
			builder.append("分");
		}
		if(millis!=0||!showHours){
			builder.append(millis);
			builder.append("秒");
		}
		
		return builder.toString();
	}
	/**
	 * 将时长转成时分秒，如2分20秒
	 * @param millis
	 * @return
	 */
	public static String transDurationToStringA(long millis,boolean showHours){
		StringBuilder builder=new StringBuilder();
				
		long minutes=0;
		long hours=0;
		
		if(millis/60>0){
			minutes=millis/60;
			millis=millis%60;			
		}
		if(minutes/60>0){
			hours=minutes/60;
			minutes=minutes%60;
		}
		
		if(showHours||hours>0){
			builder.append(hours);
			builder.append("小时");
		}
		
		builder.append(minutes);
		builder.append("分钟");
	
		if(millis!=0||!showHours){
			builder.append(millis);
			builder.append("秒");
		}
		
		return builder.toString();
	}
	/**
	 * 关闭虚拟键盘
	 * @param mcontext
	 * @param v
	 */
	 public static void closeBoard(Context mcontext,View v) {
		  InputMethodManager imm = (InputMethodManager) mcontext
		    .getSystemService(Context.INPUT_METHOD_SERVICE);
		  // imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
		  if (imm.isActive())  //一直是true
			  imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		      //imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,InputMethodManager.HIDE_NOT_ALWAYS);
	}

	 /**
	  * 打开键盘
	  * @param mContext
	  * @param v
	  */
	 public static void openBoard(Context mContext,View v){
		 InputMethodManager inputManager =(InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
         inputManager.showSoftInput(v, 0);         
	 }
		
		/**
		 * 搜索包管理器，查找包名为s的APPlication，然后设置组件名为该android:name属性名称 跳转到该包的主Activity
		 * 
		 * @param context
		 * @param s
		 */
		public static String getSystemActionComponent(Context context, String action,android.net.Uri uri) {
			String component=null;
			/*
			 * ResolveInfo这个类是通过解析一个与IntentFilter相对应的intent得到的信息。
			 * 它部分地对应于从AndroidManifest.xml的< intent>标签收集到的信息。
			 */
			Iterator<ResolveInfo> iterator;
			PackageManager packagemanager = context.getPackageManager();
			Intent intent = new Intent(action);
			intent.addCategory("android.intent.category.DEFAULT");
			intent.setData(uri);
			// 检索可以为给定的意图进行的所有活动。intent为意图所需的原意，
			iterator = packagemanager.queryIntentActivities(intent, 0).iterator();
			String packName=null,activityName=null;

			while(iterator.hasNext()){
				ActivityInfo activityinfo = ((ResolveInfo) iterator.next()).activityInfo;
				if((ApplicationInfo.FLAG_SYSTEM&activityinfo.applicationInfo.flags)>0){
					
					packName=activityinfo.packageName;
					activityName=activityinfo.name;
					component=packName+"/"+activityName;
					break;
				}	
			}

			return component;
		}

	/**
	 * 搜索包管理器，查找包名为s的APPlication，然后设置组件名为该android:name属性名称 跳转到该包的主Activity
	 * 
	 * @param context
	 * @param s
	 */
	public static String openSystemActionPackage(Context context, String action,android.net.Uri uri) {
		/*
		 * ResolveInfo这个类是通过解析一个与IntentFilter相对应的intent得到的信息。
		 * 它部分地对应于从AndroidManifest.xml的< intent>标签收集到的信息。
		 */
		Iterator<ResolveInfo> iterator;
		PackageManager packagemanager = context.getPackageManager();
		Intent intent = new Intent(action);
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(uri);
		// 检索可以为给定的意图进行的所有活动。intent为意图所需的原意，
		iterator = packagemanager.queryIntentActivities(intent, 0).iterator();
		String packName=null,activityName=null;

		while(iterator.hasNext()){
			ActivityInfo activityinfo = ((ResolveInfo) iterator.next()).activityInfo;
			if((ApplicationInfo.FLAG_SYSTEM&activityinfo.applicationInfo.flags)>0){
				
				packName=activityinfo.packageName;
				activityName=activityinfo.name;
				break;
			}	
		}
		
		if(activityName!=null){
			Intent intent1 = new Intent();
			intent1.setComponent(new ComponentName(packName, activityName));
			intent1.setAction(action);
			intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent1.setData(uri);
			context.startActivity(intent1);
		}
		return activityName;
	}

	/**
	 * 震动
	 * @param content
	 */
	public static void vibrator(Context content){
		 Vibrator vibrator = ( Vibrator)content.getSystemService(Context.VIBRATOR_SERVICE);  
		 vibrator.vibrate( new long[]{0,100},-1);  
	}
	

	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
	   for(int i=str.length();--i>=0;){
	      int chr=str.charAt(i);
	      if(chr<48 || chr>57)
	         return false;
	   }
	   return true;
	}
	
	
	
	/**

	@param content要分享的字符串
	**/
	@SuppressLint("NewApi")
	public static void setClipBoard(Context mContext,String content) {
	  int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	  if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) 
		    		 mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		   ClipData clip = ClipData.newPlainText("label", content);
		   clipboard.setPrimaryClip(clip);
	  } else {
	    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) 
			   mContext.getSystemService(Context.CLIPBOARD_SERVICE);
	    clipboard.setText(content);
	  }
	 } 
	/**

	@param content要分享的字符串
	**/
	@SuppressLint("NewApi")
	public static String getClipBoardText(Context mContext) {
	  int currentapiVersion = android.os.Build.VERSION.SDK_INT;
	  String result=null;
	  if (currentapiVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
		    android.content.ClipboardManager clipboard = (android.content.ClipboardManager) 
		    		 mContext.getSystemService(Context.CLIPBOARD_SERVICE);
		    if(clipboard.getPrimaryClip().getItemCount()>0)
		        result=clipboard.getPrimaryClip().getItemAt(0).getText().toString();
		   
	  } else {
	    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) 
			   mContext.getSystemService(Context.CLIPBOARD_SERVICE);
	    if(clipboard!=null&&clipboard.getText()!=null)
	        result=clipboard.getText().toString();
	  }
	   
	  return result;
	 } 
	
	/**
	 * 弹出APK安装
	 * @param mContext
	 * @param file
	 */
	public static void popInstall(Context mContext,File file){
		//弹出安装
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");			
		mContext.startActivity(intent);
	}

   /**
    * 显示带图片的TOAST
    * @param context
    * @param ImageResourceId
    * @param text
    * @param duration
    */
   public static void showImageToast(Context context,int ImageResourceId,CharSequence text,int duration){  
        //创建一个Toast提示消息   
        Toast toast = Toast.makeText(context, text,duration );  
        //设置Toast提示消息在屏幕上的位置   
        toast.setGravity(Gravity.CENTER, 0, 0);  
        //获取Toast提示消息里原有的View   
        View toastView = toast.getView();  
        //创建一个ImageView   
        ImageView img = new ImageView(context);  
        img.setImageResource(ImageResourceId);  
        //创建一个LineLayout容器   
        LinearLayout ll = new LinearLayout(context);  
        //向LinearLayout中添加ImageView和Toast原有的View   
        ll.addView(img);  
        ll.addView(toastView);  
        //将LineLayout容器设置为toast的View   
        toast.setView(ll);  
        //显示消息   
        toast.show();
   }
   

   /**
    * 返回当前进程名，如果是remote进程会在包名后叠加:xxx,如com.sirendaou和comsirendaou:remote
    * @param context
    * @return
    */
   public static String getCurProcessName(Context context) {
       int pid = android.os.Process.myPid();
       ActivityManager mActivityManager = (ActivityManager) context
               .getSystemService(Context.ACTIVITY_SERVICE);
       for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
               .getRunningAppProcesses()) {
           if (appProcess.pid == pid) {

               return appProcess.processName;
           }
       }
       return null;
   }
   
   /**
    * 得到状态栏高度
    *
    * @param context
    * @return
    */
   public  static  int getStatusBarHeight(Activity context){
       int statusHeight = 0;
       Rect frame = new Rect();
       context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
       statusHeight = frame.top;
       if (0 == statusHeight){
           Class<?> localClass;
           try {
               localClass = Class.forName("com.android.internal.R$dimen");
               Object localObject = localClass.newInstance();
               int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
               statusHeight = context.getResources().getDimensionPixelSize(i5);
           } catch (Exception e) {
               e.printStackTrace();
           }
       }
       return  statusHeight;
   }
}
	
