package com.pwdgame.widget;
import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
/**
 * 自定义对话框有这么几种方法比较好
 * 1、使用Dialog构建
 * 2、自定义VIEW覆盖
 * 3、POPWINDOW
 * @author Administrator
 *
 */
public class XYSystemDialog {

	protected Context mContext;
	
	protected WindowManager windowManager;
	protected WindowManager.LayoutParams layoutParams;
	protected LayoutInflater inflater;
	protected View view;
	protected boolean isFinish,isAdd;

	public  XYSystemDialog(Context mContext,int layoutId,ViewGroup viewGroup){
		this.mContext=mContext;	
		
		isAdd=isFinish=false;
		inflater=(LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view=inflater.inflate(layoutId, viewGroup);
		
		windowManager=(WindowManager)this.mContext.getSystemService(Context.WINDOW_SERVICE);
		layoutParams=new WindowManager.LayoutParams();
	}
	
	public XYSystemDialog(Context mContext,View view){
		this.mContext=mContext;	
		
		this.view=view;
		isAdd=isFinish=false;
		inflater=(LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		windowManager=(WindowManager)this.mContext.getSystemService(Context.WINDOW_SERVICE);
		layoutParams=new WindowManager.LayoutParams();
	}
	public XYSystemDialog(Context mContext){
		this.mContext=mContext;	
		
		isAdd=isFinish=false;
		inflater=(LayoutInflater)this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		
		windowManager=(WindowManager)this.mContext.getSystemService(Context.WINDOW_SERVICE);
		layoutParams=new WindowManager.LayoutParams();		
	}
	
	public void setView(View view){
		this.view=view;
	}
	
	/**
	 * 使用默认的对对话框位置
	 */
	public void setLocation(){
		setLocation(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT,
					Gravity.CENTER);
	}
	/**
	 * 设置对话框位置
	 * @param w
	 * @param h
	 * @param gravity
	 */
	public void setLocation(int w,int h,int gravity){
		layoutParams.width=w;
		layoutParams.height=h;		
		layoutParams.type=LayoutParams.TYPE_SYSTEM_OVERLAY;//LayoutParams.TYPE_PHONE;
		layoutParams.flags=LayoutParams.FLAG_DIM_BEHIND;
		layoutParams.dimAmount=0.5f;
		layoutParams.format=PixelFormat.TRANSPARENT;	
		layoutParams.gravity = gravity;									
	}
	/**
	 * 显示视图
	 */
	public void showDialog(){		
		setLocation();
		view.setOnKeyListener(new View.OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				// TODO Auto-generated method stub
				return false;
			}
		});		
		windowManager.addView(view, layoutParams);					
	}	

	public void setFinish(boolean b){
		isFinish=b;
	}
	
	
	public boolean IsFinish(){
		return isFinish;
	}
	
	public boolean IsAdd(){
		return isAdd;
	}
	
	/**
	 * 第一次显示
	 */
	public void onFirstShow(){
		isFinish=true;
	}
	
	/**
	 * 更新时触发
	 */
	public void onUpdateShow(){
		
	}
	
	/**
	 * 移除视图
	 */
	public synchronized void removeDialog(){
		try {			
			windowManager.removeView(view);			
		} catch (Exception e) {
			//视图可能没有添加到窗口,因为重复删除
			e.printStackTrace();
		}
		view=null;		
		isFinish=false;
	}


}
