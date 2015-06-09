package com.pwdgame.animation;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.pwdgame.util.Utility;


public class AnimationUtils {	
	
	/**
	 * 启动动画 {@link ObjectAnimator}
	 * @param target 
	 * @param propertyName
	 * @param duration 动画时长
	 * @param endHide 动画完成后是否target隐藏(如果是view)
	 * @param values
	 */
	@SuppressLint("NewApi")
	public static void startAnimationEx(final Object target, final String propertyName,final long duration,final boolean endHide, final float... values){
		if(target==null)
			return;
		if(target instanceof View){
			final View view=(View)target;
			if(view.getHeight()==0){
				view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					
					@Override
					public void onGlobalLayout() {								
						startAnimation(target,propertyName,duration,endHide,values);
						if(Build.VERSION.SDK_INT>=16){
							view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						}else{
							view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
						}		
					}
				});
			}else{
				startAnimation(target,propertyName,duration,endHide,values);	
			}
			
		}else{
			startAnimation(target,propertyName,duration,endHide,values);	
		}
	}
	/**
	 * 启动动画
	 * @param target
	 * @param propertyName
	 * @param duration
	 * @param endHide
	 * @param values
	 */
	private static void startAnimation(final Object target, final String propertyName,final long duration,final boolean endHide, final float... values){
		startAnimation(target, propertyName, duration, new Runnable() {
			
			@Override
			public void run() {
				if(endHide&&(target instanceof View))
					((View)target).setVisibility(View.GONE);
			}
		}, values);
	}
	/**
	 * 启动动画
	 * @param target
	 * @param propertyName
	 * @param duration
	 * @param endRunnable 动画结束回调
	 * @param values
	 */
	private static void startAnimation(final Object target, final String propertyName,final long duration,final Runnable endRunnable, final float... values){
		//将 value 的1f转换成VIEW的宽度或高度
		if(target instanceof View && (propertyName.startsWith("translation")||propertyName.equals("width")||propertyName.equals("height"))){
			View view=(View)target;
			boolean isX=propertyName.equals("translationX")||propertyName.equals("width")? true:false;
			for(int i=0,size=values.length;i<size;i++){
				if(values[i]==1f){
					if(isX){
						values[i]=view.getWidth();
					}else{
						values[i]=view.getHeight();
					}
				}
			}
		}

		
		com.nineoldandroids.animation.Animator animator ;
				
		if("height".equals(propertyName)||"width".equals(propertyName)){
			animator =ValueAnimator.ofFloat(values);
			((com.nineoldandroids.animation.ValueAnimator)animator).addUpdateListener(new com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {
					View view=(View)target;
					ViewGroup.LayoutParams mLayoutParams=view.getLayoutParams();
					if("height".equals(propertyName)){
					    mLayoutParams.height = (int)Float.parseFloat(animation.getAnimatedValue().toString());
					}else if("width".equals(propertyName)){						
						mLayoutParams.width = (int)Float.parseFloat(animation.getAnimatedValue().toString());						
					}
				
					view.requestLayout();
				}
			});
		}else{
			animator=com.nineoldandroids.animation.ObjectAnimator.ofFloat(target, propertyName, values).setDuration(duration);

		}
		if(endRunnable!=null){
			animator.addListener(new com.nineoldandroids.animation.AnimatorListenerAdapter(){
				@Override
				public void onAnimationEnd(Animator animation) {
					endRunnable.run();
					
				}
			});		
		}
		animator.start();
	}
	
	/**
	 * 3d翻转动画，oldView将被隐藏，newView将显示
	 * @param oldView
	 * @param newView
	 * @param duration
	 * @param switchRunnable 在第一个动画切换到第二哥动画的回调
	 * @param endRunnable 动画完成回调
	 */
	public static void startFlipYAnimation(final View oldView,final View newView,final int duration,final Runnable switchRunnable,final Runnable endRunnable){
		int width=oldView.getWidth();
		int height=oldView.getHeight();
		
		Rotate3dAnimation animation=new Rotate3dAnimation(0, 90, width/2, height/2, 0, false);
		
		animation.setFillAfter(false);
		animation.setDuration(duration/2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override	
			public void onAnimationEnd(Animation animation) {

				oldView.setVisibility(View.GONE);
				newView.setVisibility(View.VISIBLE);
				
				int width=newView.getWidth();
				int height=newView.getHeight();
				if(width==0&&height==0){
					newView.measure(0, 0);
					width=newView.getMeasuredWidth();
					height=newView.getMeasuredHeight();
				}
				if(switchRunnable!=null){
					switchRunnable.run();
				}
				Rotate3dAnimation animationSearch=new Rotate3dAnimation(90, 0, width/2, height/2, 0, false);				
				animationSearch.setFillAfter(false);
				animationSearch.setDuration(duration/2);
				animationSearch.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if(endRunnable!=null){
							endRunnable.run();
						}
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
				});
				newView.setAnimation(animationSearch);
				
			}
		});
		oldView.startAnimation(animation);
	}
	/**
	 * 3d翻转动画，oldView将被隐藏，newView将显示
	 * @param oldView
	 * @param newView
	 * @param duration
	 * @param switchRunnable 在第一个动画切换到第二哥动画的回调
	 * @param endRunnable 动画完成回调
	 */
	public static void startFlipYAnimationEx(final View oldView,final View newView,final int duration,final Runnable switchRunnable,final Runnable endRunnable){
		int width=oldView.getWidth();
		int height=oldView.getHeight();
		
		Rotate3dAnimation animation=new Rotate3dAnimation(0, 30, 0, width/2, 30, false);
		
		animation.setFillAfter(false);
		animation.setDuration(duration/2);
		animation.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override	
			public void onAnimationEnd(Animation animation) {

				oldView.setVisibility(View.GONE);
				newView.setVisibility(View.VISIBLE);
				
				int width=newView.getWidth();
				int height=newView.getHeight();
				if(width==0&&height==0){
					newView.measure(0, 0);
					width=newView.getMeasuredWidth();
					height=newView.getMeasuredHeight();
				}
				if(switchRunnable!=null){
					switchRunnable.run();
				}
				Rotate3dAnimation animationSearch=new Rotate3dAnimation(30, 0, 0, width/2, 0, false);				
				animationSearch.setFillAfter(false);
				animationSearch.setDuration(duration/2);
				animationSearch.setAnimationListener(new AnimationListener(){

					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						if(endRunnable!=null){
							endRunnable.run();
						}
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
				});
				newView.setAnimation(animationSearch);
				
			}
		});
		oldView.startAnimation(animation);
	}

}
