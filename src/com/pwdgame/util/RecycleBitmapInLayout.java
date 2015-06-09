package com.pwdgame.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class RecycleBitmapInLayout {

	public static final String TAG="RecycleBitmapInLayout";

	private RecycleBitmapInLayout(){
		
	}
	
	public static void recycle(Activity activity){
		try {
			View view=((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
			if(view!=null){
				if(view instanceof ViewGroup){
					recycle((ViewGroup)view);
					return;
				}else if(view instanceof ImageView){
					recycleImageView((ImageView)view);
				}
				recycleBackground(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static void recycle(ViewGroup view){
		for(int i=0,size=view.getChildCount();i<size;i++){
			View subView=view.getChildAt(i);
			if(subView instanceof ViewGroup){
				//递归释放
				recycle((ViewGroup)subView);
			}else{
				if(subView instanceof ImageView){
					recycleImageView((ImageView)subView);
					recycleBackground(subView);
				}				
			}
			
		}
		//释放VIEWGROUP的背景
		recycleBackground(view);
	}
	
	/**
	 * 释放ROOT VIEW 背景图片
	 * @param activity
	 */
	public static void recycleRootBackground(Activity activity){
		try {
			View view=((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
			if(view!=null){				
				recycleBackground(view);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	public static void recycleBackground(View view){
		if(view!=null){			
			Drawable draw=view.getBackground();
			if(draw!=null&&draw instanceof BitmapDrawable){
				BitmapDrawable drawable=(BitmapDrawable)draw;
				recycleBitmapDrawable(drawable);
			}
		}
	}
	
	public static void recycleImageView(ImageView view){
		if(view!=null){
			Drawable draw=view.getDrawable();
			if(draw!=null&&draw instanceof BitmapDrawable){
				BitmapDrawable drawable=(BitmapDrawable)draw;
				recycleBitmapDrawable(drawable);
			}

		}
	}
	
	public static void recycleBitmapDrawable(BitmapDrawable drawable){
		if(drawable!=null){
			Bitmap bitmap=drawable.getBitmap();			
			recycleBitmap(bitmap);
			drawable=null;
		}
	}
	
	public static void recycleBitmap(Bitmap bitmap){		
		if(bitmap!=null&&!bitmap.isRecycled()){
			try {
				Log.e(TAG, "释放图片");
				bitmap.recycle();
				bitmap=null;	
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}
