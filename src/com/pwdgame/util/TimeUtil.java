package com.pwdgame.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;

/**
 * 时间工具类
 * 
 * @author way
 * 
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	/**
	 * 返回 2014-12-12 12:40
	 * @param time
	 * @return
	 */
	public static String getTime(long time) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.getDefault());

		return format.format(new Date(time));
	}

	/**
	 * 返回 12:50如此
	 * @param time
	 * @return
	 */
	public static String getHourAndMin(long time) {
		SimpleDateFormat format = new SimpleDateFormat("HH:mm", Locale.getDefault());

		return format.format(new Date(time));
	}

	/**
	 * 返回诸如：今天 12：50，昨天 17.50
	 * @param timesamp
	 * @return
	 */
	public static String getDayAndHourTime(long timesamp) {
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			result = "今天 " + getHourAndMin(timesamp);
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;
		default:
			// result = temp + "天前 ";
			result = getTime(timesamp);
			break;
		}

		return result;
	}
	
	/**
	 * 24小时日内返回 5分钟前、3小时前、昨天12:40 等类型
	 * @return
	 */
	public static String getBeforeHourTime(long timesamp){
		String result = "";
		SimpleDateFormat sdf = new SimpleDateFormat("dd", Locale.getDefault());
		Date today = new Date(System.currentTimeMillis());
		Date otherDay = new Date(timesamp);
		int temp = Integer.parseInt(sdf.format(today))
				- Integer.parseInt(sdf.format(otherDay));

		switch (temp) {
		case 0:
			//今天的
			if(today.getHours()==otherDay.getHours()){
				int minute=today.getMinutes()-otherDay.getMinutes();
				result=minute+"分钟前";
			}else{
				int hour=today.getHours()-otherDay.getHours();
				result=hour+"小时前";
			}
			break;
		case 1:
			result = "昨天 " + getHourAndMin(timesamp);
			break;
		case 2:
			result = "前天 " + getHourAndMin(timesamp);
			break;
		default:
			if(today.getYear()==otherDay.getYear()){
				//今年的
				SimpleDateFormat format = new SimpleDateFormat("M月d日 HH:mm",
						Locale.getDefault());

				result=format.format(new Date(timesamp));
			}else{
				SimpleDateFormat format = new SimpleDateFormat("y年M月d日 HH:mm",
						Locale.getDefault());

				result=format.format(new Date(timesamp));
			}

			break;
		}

		return result;
	}
}