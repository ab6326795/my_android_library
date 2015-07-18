package com.pwdgame.http;

import java.util.Map;

import org.apache.http.Header;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.pwdgame.app.AppConfig;
import com.pwdgame.cache.XYFileCache;
import com.pwdgame.library.BuildConfig;
import com.pwdgame.util.Logger;
/**
 * 异步Http请求封装类,封装了缓存的存取，简化了数据解析和回调
 * 备注：
 * 各个线程对静态方法的访问是交叉执行的，但是这并不影响各个线程静态方法print()中sum值的计算。
 * 也就是说，在此过程中没有使用全局变量的静态方法在多线程中是安全的，静态方法是否引起线程安全问题主要看该静态方法是否对全局变量
 * （静态变量static member）进行修改操作。、
 * @author xieyuan
 *
 */
public class XYAsyncHttpRequest {

	private static final Object obj=new Object();
	private static final String TAG=XYAsyncHttpRequest.class.getName();
	private static final int GET=0;
	private static final int POST=1;
	
    private AsyncHttpClient mClient;
    private IHttpResponse mIHttpResponse;

    private Context mContext;
    private Class beanClass;
   
    public XYAsyncHttpRequest(Context mContext){
    	this.mContext=mContext;
    	mClient = new AsyncHttpClient();
       
        PersistentCookieStore myCookieStore = new PersistentCookieStore(mContext);
        mClient.setCookieStore(myCookieStore);
        //如果有Cookie就会设置Cookie
        mClient.addHeader("Cookie", AppConfig.getHttpCookie());
      
    }
    

    /**
     * 发起GET请求
     * @param url 请求地址
     * @param netNotAvailableApplyCache 网络不可用是否使用缓存
     * @param params 请求参数
     * @param httpResponse 回调
     */
    public  void get(String url,Class beanClass,IHttpResponse httpResponse) {
    	this.beanClass = beanClass;
    	this.mIHttpResponse=httpResponse;    	
    	requestDate(url, false,false,null, GET);
    }
    
    /**
     * 发起GET请求
     * @param url 请求地址
     * @param netNotAvailableApplyCache 网络不可用是否使用缓存
     * @param params 请求参数
     * @param httpResponse 回调
     */
    public void get(String url,boolean netNotAvailableApplyCache,Class beanClass,IHttpResponse httpResponse) {
    	this.beanClass = beanClass;
    	this.mIHttpResponse=httpResponse;    	
    	requestDate(url, netNotAvailableApplyCache,false,null, GET);
    }

    /**
     * 发起GET请求
     * @param url  请求地址
     * @param netNotAvailableApplyCache 网络不可用是否使用缓存
     * @param ApplyTimeCache 是否直接读取定时缓存
     * @param params 请求参数
     * @param httpResponse 回调
     */
    public void get(String url,boolean netNotAvailableApplyCache,boolean ApplyTimeCache,  Map<String, String> hashMap,Class beanClass,
    		IHttpResponse httpResponse) {
    	this.beanClass = beanClass;
    	this.mIHttpResponse=httpResponse;   
    	requestDate(url, netNotAvailableApplyCache, ApplyTimeCache, hashMap, GET);
    }
    
    /**
     * 发起POST请求
     * @param url 请求地址
     * @param netNotAvailableApplyCache 网络不可用是否使用缓存
     * @param params 请求参数
     * @param httpResponse 回调
     */
    public void post(String url, Map<String, String> hashMap,Class beanClass, IHttpResponse httpResponse) {
    	this.beanClass = beanClass;
    	this.mIHttpResponse=httpResponse;   
    	requestDate(url,false, false, hashMap, POST);
    }
    
    /**
     * 发起POST请求
     * @param url 请求地址
     * @param netNotAvailableApplyCache 网络不可用是否使用缓存
     * @param params 请求参数
     * @param httpResponse 回调
     */
    public void post(String url,boolean netNotAvailableApplyCache, Map<String, String> hashMap,Class beanClass,
    		IHttpResponse httpResponse) {
    	this.beanClass = beanClass;
    	this.mIHttpResponse=httpResponse;   
    	requestDate(url,netNotAvailableApplyCache, false, hashMap, POST);
    }

    /**
     * 发起POST请求
     * @param url  请求地址
     * @param netNotAvailableApplyCache 网络不可用是否使用缓存
     * @param ApplyTimeCache 是否直接读取定时缓存
     * @param params 请求参数
     * @param httpResponse 回调
     */
    public void post(String url,boolean netNotAvailableApplyCache,boolean ApplyTimeCache, Map<String, String> hashMap, Class beanClass,
    		IHttpResponse httpResponse) {
    	this.beanClass = beanClass;
    	this.mIHttpResponse=httpResponse;   
    	requestDate(url, netNotAvailableApplyCache, ApplyTimeCache, hashMap, POST);       
    }
    /**
     * 设置请求超时
     * @param timeout
     */
    public void setTimeOut(int timeout){
    	mClient.setTimeout(timeout);
    }
    
    /**
     * 取消任务
     * @param mContext
     */
    public void cancel(Context mContext){
    	mClient.cancelRequests(mContext, true);    	
    }
    
    public void cancelAll(){
    	mClient.cancelAllRequests(true);
    }
    
    /**
     * 请求数据
     * @param url
     * @param netNotAvailableApplyCache
     * @param ApplyTimeCache
     * @param params
     * @param httpResponse
     * @param method
     */
    private void requestDate(String url,boolean netNotAvailableApplyCache,boolean ApplyTimeCache,Map<String, String> hashMap,int method){
    
    	RequestParams params = null;    	
    	String cacheKey = HttpUtils.getCacheKey(url,hashMap);;
    	if(hashMap != null){
    		params = new RequestParams(hashMap);    		
    	}
    	
    	String json=null;
    	
    	if(ApplyTimeCache){
    		 json=XYFileCache.getUrlCache(mContext, url, ApplyTimeCache);
    		if(json!=null){
    			Object object = JSONObject.parseObject(json,this.beanClass);
    			this.mIHttpResponse.onFinish(object);
    			if(BuildConfig.DEBUG){
    				Log.e(TAG, "使用定时缓存");
    			}
    		    return;    			 
    		}
    	}
		//不使用定期缓存，则从网络上拉取
		if(method==POST){
			 mClient.post(url, params,new XYAsyncHttpResponseHandler(url, netNotAvailableApplyCache,cacheKey));
		}else if(params!=null){
			mClient.get(url, params,new XYAsyncHttpResponseHandler(url,netNotAvailableApplyCache,cacheKey));
		}else{
			mClient.get(url,new XYAsyncHttpResponseHandler(url, netNotAvailableApplyCache,cacheKey));
		}
    	
    }
    
    private class XYAsyncHttpResponseHandler extends AsyncHttpResponseHandler{

    	private String url;
    	private boolean netNotAvailableApplyCache;
    	private String cacheKey;
    	
    	public XYAsyncHttpResponseHandler(String url,boolean netNotAvailableApplyCache,String cacheKey){    		
    		this.url=url;
    		this.netNotAvailableApplyCache=netNotAvailableApplyCache;
    		this.cacheKey=cacheKey;
    	}

		@Override
		public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
			if(mIHttpResponse!=null){
				String str=arg2==null? null:new String(arg2);
				
				new AsyncTask<String, Void, Object>() {
					@Override
					public Object doInBackground(String... params){
						Object object=null;
						String data = (params!=null && params[0] != null) ? params[0] : null;
						if(data != null){
							object=JSONObject.parseObject(data,beanClass);	
						
							if(object != null && netNotAvailableApplyCache){
								//保存缓存
								XYFileCache.setUrlCache(mContext, data, cacheKey);								
							}
						}
						
		    			return object;
					}
					@Override
					public void onPostExecute(Object baseModel){
						mIHttpResponse.onFinish(baseModel);
					}
				}.execute(str);

			}
		}

		@Override
		public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
			if(mIHttpResponse!=null){
				String str=arg2==null? null:new String(arg2);
				new AsyncTask<String, Void, Object>() {
					@Override
					public Object doInBackground(String... params){
						Object baseModel=null;
						String data = (params!=null && params[0] != null) ? params[0] : null;
						if(data == null && netNotAvailableApplyCache){
							//如果网络请求失败，需要使用缓存
							 data = XYFileCache.getUrlCache(mContext, cacheKey, false);
							
						}
						if(data != null){
							Logger.debug("2parse start "+data);									
							baseModel=JSONObject.parseObject(data,beanClass);	
							Logger.debug("parse end");
							
						}					
		    			return baseModel;
					}
					@Override
					public void onPostExecute(Object baseModel){
	
						mIHttpResponse.onFinish(baseModel);
					}
				}.execute(str);
			}
		}
    }
    

	/**
	 * 数据相应接口
	 * @author Administrator
	 *
	 */
	public interface IHttpResponse {
		public void onFinish(Object beanObject);
	}


}
