package com.pwdgame.http;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.pwdgame.cache.XYFileCache;
import com.pwdgame.secure.MD5;
import com.pwdgame.util.DeviceInfo;
import com.pwdgame.util.Logger;

/**
 * HTTP 请求工具类
 * @author xieyuan
 *
 */
public class HttpUtils {	
	
	private static final String CHECK_KEY="dodonew_wx#Pay";
	

	/**
	 * 返回拼装后的基本参数 LinkedHashMap<String, String>
	 * @param hashMap
	 * @return
	 */
	public static LinkedHashMap<String, String> appendBaseData(LinkedHashMap<String, String> map){

		map.put("app_sign",""+DeviceInfo.getSign());		//+DeviceInfo.getSign()
		map.put("version",""+DeviceInfo.getAppVersionCode());
		map.put("platfrom","android");
		map.put("phone_model",""+DeviceInfo.getPhoneModel());
		map.put("imei",""+DeviceInfo.getIMEI());
		map.put("channel",""+DeviceInfo.getChannel());
		
		return map;
	}
	/**
	 * 返回拼装后的基本参数 String
	 * @return
	 */
	public static String getBaseDataString(){
		StringBuilder builder=new StringBuilder();
		builder.append("app_sign=");
		builder.append(""+DeviceInfo.getSign());
		builder.append("&version=");
		builder.append(DeviceInfo.getAppVersionCode());
		builder.append("&clientflag=android");
		builder.append("&phone_model=");
		builder.append(DeviceInfo.getPhoneModel());
		builder.append("&imei=");
		builder.append(""+DeviceInfo.getIMEI());		
		builder.append("&channel=");
		builder.append(DeviceInfo.getChannel());
		
		return builder.toString();
	}

	/**
	 * 返回拼装基本参数和加密后的LinkedHashMap
	 * @param hashMap
	 * @return
	 */
	public static LinkedHashMap<String, String> appendEncryptBaseData(LinkedHashMap<String, String> hashMap){
	    String key=null;
	    
		try {
			key=encryptHashMap(hashMap);
		} catch (Exception e1) {
			Logger.debug("sendRequest", "加密发生异常");
			e1.printStackTrace();
		}
		//拼接
		LinkedHashMap<String, String> requestContent=appendBaseData(hashMap);		
		//添加密钥
		requestContent.put("sign", key);
		
		return requestContent;
	}
	
	/**
	 * 将参数Ascii排序  后加密，返回加密字符串
	 * @return
	 * @throws Exception 
	 */
	public static String encryptHashMap(Map<String, String> hashMap) throws Exception{
		String result="";
		
		if(hashMap!=null&&hashMap.size()>0){
			StringBuilder builder=new StringBuilder();
			
			//对hashMap Ascii排序'
			List<Map.Entry<String, String>> infoIds =new ArrayList<Map.Entry<String, String>>(hashMap.entrySet());
			java.util.Collections.sort(infoIds,new Comparator<Map.Entry<String, String>>() {   
			    public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {      
			        //return (o2.getValue() - o1.getValue()); 
			        return o1.getKey().compareTo(o2.getKey());
			    }
			});

			for(Map.Entry<String, String> entry:infoIds){
				builder.append(entry.getKey());	
				builder.append("=");
				builder.append(entry.getValue());					
				builder.append("&");		
			}
			builder.delete(builder.length()-1, builder.length());
			builder.append("||");
			result=MD5.encryptToHex(builder.toString(),CHECK_KEY);
			
		}
		
		return result;
	}
	
	
	/**
	 * 返回Map的缓存key，此方法移除了影响hashCode的变动因素
	 * @param hashMap
	 * @return
	 */
	public static String getCacheKey(String requestURL,Map<String, String> map) {
		Map<String, String> paramsHashMap = null;
		
		if(map != null){
			paramsHashMap = new LinkedHashMap<String, String>(map);//getBaseData(); 
		}else{
			paramsHashMap = new LinkedHashMap<String, String>();
		}
		
		if(paramsHashMap!=null){
			//如果有time之类的需要剔除
			//paramsHashMap.remove("time");			
		}
		paramsHashMap.put("requestURL", requestURL);
		return String.valueOf(paramsHashMap.hashCode());			
	}
	
	
	
	/**
	 * 读取缓存
	 * @param url 请求URL
	 * @param hashMap 请求参数
	 * @param parseClass 请求结果后需要转化的BEAN CLASS
	 * @return 
	 */
	public static <T> T readCache(String url,Map<String, String> hashMap,Class<T> parseClass){
	    return readCache(getCacheKey(url, hashMap), hashMap, parseClass);
	}
	
	/**
	 * 读取缓存
	 * @param url 请求URL
	 * @param hashMap 请求参数
	 * @param parseClass 请求结果后需要转化的BEAN CLASS
	 * @return 
	 */
	public static <T> T readCache(String key,Class<T> parseClass){
		Context context=AppConfig.mContext;
		String json=XYFileCache.getUrlCache(context, key, false);
		if(json!=null){		
			return JSONObject.parseObject(json, parseClass);					    			    			 
		}
	    return null;
	}

	/**
	 * 保存缓存
	 * @param url
	 * @param hashMap
	 * @param data
	 */
	public static void saveCache(String url,Map<String, String> hashMap,String data){
		saveCache(getCacheKey(url, hashMap),data);		
	}
	
	/**
	 * 保存缓存
	 * @param url
	 * @param hashMap
	 * @param data
	 */
	public static void saveCache(String key,String data){
		Context context=AppConfig.mContext;		
		XYFileCache.setUrlCache(context, data, key);
	}
}
