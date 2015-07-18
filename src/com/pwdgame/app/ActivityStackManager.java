package com.pwdgame.app;

import java.util.Iterator;
import java.util.Stack;

import android.app.Activity;

import com.pwdgame.base.BaseActivity;

public class ActivityStackManager  {

	// Activity集合
	private static Stack<Activity> activityStack;
	private static ActivityStackManager mInstance;

	private ActivityStackManager(){}
	
	public static ActivityStackManager getInstance(){
		if(mInstance==null){
			mInstance=new ActivityStackManager();
		}
		return mInstance;
	}
	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 */
	public Activity currentActivity(){
		Activity activity=activityStack.lastElement();
		return activity;
	}
	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity(){
		Activity activity=activityStack.lastElement();
		finishActivity(activity);
	}
	
	/**
	 * 结束指定的Activity
	 */
	public void finishActivity(Activity activity){
		if(activity!=null){
			activityStack.remove(activity);
			activity.finish();
			activity=null;
		}
	}
	
	public void addActivity(Activity activity){
		if(activityStack==null){
			activityStack=new Stack<Activity>();
		}
		activityStack.add(activity);
	}
	
	public void removeActivity(Class mClass){
		Iterator<Activity> iterator=activityStack.iterator();
		while(iterator.hasNext()){
			Activity temp=iterator.next();			
			if(temp.getClass()==mClass){				
				if(temp instanceof BaseActivity){
				  ((BaseActivity)temp).bAnimation=false;	
				}				
				temp.finish();
				
				iterator.remove();
				
			}
		}
	}
	
	public boolean containsActivity(Class cls){
		Iterator<Activity> iterator=activityStack.iterator();
		while(iterator.hasNext()){
			Activity temp=iterator.next();			
			if(temp.getClass()==cls){				
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 移除所有activity，并可以设置忽略，返回是否被忽略成功，失败说明栈区没有该acitivity
	 * @param ignore
	 * @return
	 */
	public boolean removeAllActivity(Class<?>...ignore){
		Iterator<Activity> iterator=activityStack.iterator();
		boolean bIgnoreSuccess=false;
		while(iterator.hasNext()){
			Activity temp=iterator.next();			
			boolean ignoreActivity=false;
			if(ignore!=null&&temp!=null)
			  for(int i=0;i<ignore.length;i++){
				  Class<? extends Activity> class1=(Class<? extends Activity>) ignore[i];
				  if(temp.getClass()==class1){
					  ignoreActivity=true;
					  bIgnoreSuccess=true;
					  break;
				  }
			  }
			if(ignoreActivity)
				continue;
			if(temp!=null&&!temp.isFinishing()){
				iterator.remove();
				if(temp instanceof BaseActivity){
				  ((BaseActivity)temp).bAnimation=false;	
				}
			   temp.finish();			   
			}
		}
		return bIgnoreSuccess;
	}

}
