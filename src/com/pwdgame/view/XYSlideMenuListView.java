package com.pwdgame.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import com.pwdgame.library.R;
import com.pwdgame.util.Logger;
/**
 * 这是一个华丽的滑动删除ListView，可以自定义开启左滑、右滑
 * @author xieyuan
 *
 */
public class XYSlideMenuListView extends ListView{

	//达到这个速率也可以说是滑动
	//private static final int SNAP_VELOCITY=600;
	//
/*	//滑动效果持续时间
	protected static final long mAnimationTime = 200;  
	
	//滑动回滚动画时间
	protected static final long mAnimationResetTime=100;
	*/
/*	private static final int MODE_LEFT=1;
	private static final int MODE_RIGHT=2;*/

	
	/**
	 * 最后滚动的项
	 */
	private static int lastPosition=-1;
	
	/**当前滑动的posotion*/
	private int slidePosition;
	/**手指按下的X坐标，Y坐标*/
	private int downX,downY;
	/**屏幕宽度*/
	private int mListWidth;
	/**listview滑动的VIEW*/
	private ViewGroup itemView;

	//动画是否已经结束
	private boolean isAnimationFinish=true;
	
	/**是否响应滑动，默认不响应*/
	private boolean isSlide=false;
	/**速率对象*/
	private VelocityTracker mVelocityTracker;
	/**
	 * 认为是用户滑动的最小距离
	 */
	private int mTouchSlop;	

	/**
	 * 滑动回调
	 */
	private SlideMenuListener onSlideMenuListener;
	
	/**
	 * 菜单总长度
	 */
	private int menuWidth;
	/**
	 * 菜单总数
	 */
	private int menuCount;
	/**
	 * 滑动多少算菜单滚动
	 */
	private int menuScrollSlop;
	
	public XYSlideMenuListView(Context context) {
		this(context, null);
	}
	public XYSlideMenuListView(Context context,AttributeSet attributeSet){
		this(context, attributeSet, 0);
	}
	public XYSlideMenuListView(Context context,AttributeSet attributeSet,int defStyle){
		super(context, attributeSet,defStyle);

		mTouchSlop=ViewConfiguration.get(context).getScaledTouchSlop();
        parseAttributes(context.obtainStyledAttributes(attributeSet,R.styleable.XYSlideCutListView));
       
    }
	
	private void parseAttributes(TypedArray a){			
		//mode=a.getInt(R.styleable.XYSlideCutListView_slideMode, MODE_ALL);
		a.recycle();
	}  
	/**
	 * 分发事件，主要做的是判断点击的是那个item, 以及通过postDelayed来设置响应左右滑动事件
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// 假如滚动还没有结束，我们直接返回
		if (!isAnimationFinish) {
			return super.dispatchTouchEvent(event);
		}
		addVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			
			downX = (int) event.getX();
			downY = (int) event.getY();

			slidePosition = pointToPosition(downX, downY);

			// 无效的position, 不做任何处理
			if (slidePosition == AdapterView.INVALID_POSITION) {
				return super.dispatchTouchEvent(event);
			}

			// 获取我们点击的item view
			itemView = (ViewGroup) getChildAt(slidePosition - getFirstVisiblePosition());
			if(itemView!=null){
				try{
					
					mListWidth=itemView.getWidth();		
					View contentView= itemView.getChildAt(0);
					View menuView=itemView.getChildAt(1);
					
					if(menuView!=null&&onSlideMenuListener!=null){
						//获取菜单总数和总宽度
						menuCount=onSlideMenuListener.onMenuPreShow(slidePosition);		
						menuView.measure(0, 0);
						menuWidth=menuView.getMeasuredWidth();	
						//滑动单个菜单的1/2为滚动临界值
						menuScrollSlop=menuWidth/menuCount/2;
						Log.i("XYSlideMenu", "width:"+menuWidth+",slop:"+menuScrollSlop);
					}
				}catch(NullPointerException e){
					e.printStackTrace();
				}catch (ClassCastException e) {
					e.printStackTrace();
				}

			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			//速率满足、滑动距离满足  update 2：当用户手指上滑动，速度很快，Xvelocity也会变大，导致上滑也执行了删除，所以去掉
			//int velocityX = getScrollVelocity();
			//if((mode&MODE_LEFT)>0){
			if(event.getX()-downX<-mTouchSlop&& Math.abs(event.getY() - downY) < mTouchSlop){
				setSlideFalg();
			}else if((event.getX()-downX>mTouchSlop&& Math.abs(event.getY() - downY) < mTouchSlop)){
				setSlideFalg();
			}
		
			break;
		}
		case MotionEvent.ACTION_UP:
			addVelocityTracker(event);
			break;
		}

		return super.dispatchTouchEvent(event);
	}
	
	private void setSlideFalg(){
		isSlide=true;
		if(lastPosition!=-1&&lastPosition!=slidePosition){
			resetSlideAll(true);
		}
		lastPosition=slidePosition;
	}
	
	/**
	 * 处理我们拖动ListView item的逻辑
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isSlide && slidePosition != AdapterView.INVALID_POSITION) {
			requestDisallowInterceptTouchEvent(true);
			addVelocityTracker(ev);

			final int action = ev.getAction();
			int x = (int) ev.getX();
			switch (action) {
			case MotionEvent.ACTION_DOWN:
				//downX = x;
				
				break;
			case MotionEvent.ACTION_MOVE:
				//当手指滑动item,取消item的点击事件，不然我们滑动Item也伴随着item点击事件的发生
				MotionEvent cancelEvent = MotionEvent.obtain(ev);
	            cancelEvent.setAction(MotionEvent.ACTION_CANCEL |
	                       (ev.getActionIndex()<< MotionEvent.ACTION_POINTER_INDEX_SHIFT));
	            onTouchEvent(cancelEvent);
	            
	            
				int deltaX = downX-x;
				downX=x;
				// 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
				//itemView.scrollBy(deltaX, 0);
				if(itemView.getScrollX()<menuWidth&&deltaX>0){
					//那么可以向右滚动
					itemView.scrollBy(Math.min(deltaX,menuWidth-itemView.getScrollX()), 0);
				}else if(itemView.getScrollX()>0&&deltaX<0){
					//那么可以向左滚动 deltaX=负数
					itemView.scrollBy(Math.min(deltaX,itemView.getScrollX()), 0);
				}
				return true;  //拖动的时候ListView不滚动
			case MotionEvent.ACTION_UP:
				scrollByDistanceX();
				recycleVelocityTracker();
				// 手指离开的时候就不响应左右滚动
				isSlide = false;
				break;
			}
		}

		//否则直接交给ListView来处理onTouchEvent事件
		return super.onTouchEvent(ev);
	}

	/**
	 * 左右滚动itemview
	 * @param direction 滚动的方向
	 */
	public void slideLeft(){
		if(itemView==null)
			return;
		isAnimationFinish=false;
		itemView.scrollTo(menuWidth,  0);
		isAnimationFinish=true;
	}
	
	/**
	 * 恢复初始位置
	 */
	public void resetSlide(){
		if(itemView==null)
			return;
		
		isAnimationFinish=false;
		itemView.scrollTo(0, 0);
		smoothScroll(itemView, itemView.getScrollX(), 0, new com.nineoldandroids.animation.AnimatorListenerAdapter(){
        	@Override
			public void onAnimationEnd(Animator animation) { 
				isAnimationFinish=true;
			}
		},true);
	}

	public void resetSlideAll(){
		resetSlideAll(false);
	}
	/**
	 * 将所有菜单恢复初始位置
	 */
	public void resetSlideAll(boolean ignoreCurrent){
		int size=getChildCount();
		if(size==0)
			return;
		isAnimationFinish=false;
		
		for(int i=0;i<size;i++){
			if(ignoreCurrent&&i==slidePosition){
				//ignore
			}else{
				smoothScroll(getChildAt(i), getChildAt(i).getScrollX(), 0,null,true);	
			}			   			
		}
		isAnimationFinish=true;		
	}
	
	/**
	 * 滚动到指定位置
	 * @param view
	 * @param x 起始位置
	 * @param toX 结束为止
	 * @param animation 是否显示补间动画
	 */
	private void smoothScroll(final View view,int x,int toX,com.nineoldandroids.animation.Animator.AnimatorListener
			animationListener,boolean animation){
		if(animation){
			ValueAnimator mValueAnimator=ValueAnimator.ofInt(x,toX).setDuration(200*x/menuWidth);
			if(animationListener!=null){
				mValueAnimator.addListener(animationListener);
			}
			mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				
				@Override
				public void onAnimationUpdate(ValueAnimator animation) {			
					int value = (Integer) animation.getAnimatedValue(); 
					view.scrollTo(value, 0);
				}
			});
			mValueAnimator.start();
		}else{
			view.scrollTo(toX, 0);
		}
	}
	
	/**
	 * 根据手指滚动itemView的距离来判断是滚动到开始位置还是向左或者向右滚动
	 */
	private void scrollByDistanceX() {
		// 如果向左滚动的距离大于mTouchSlop，就让其删除  正数向右移动
		int scrollX=itemView.getScrollX();
		if (scrollX >= menuScrollSlop) {
			slideLeft();
			//负数向左边移动
		} else if (Math.abs(scrollX)>= menuScrollSlop) {
			resetSlide();
		} else {
			// 滚回到原始位置,为了偷下懒这里是直接调用scrollTo滚动
			resetSlide();
		}

	}
	/**
	 * 添加用户的速度跟踪器
	 * @param event
	 */
	public void addVelocityTracker(MotionEvent event){
		if(mVelocityTracker==null){
			mVelocityTracker=VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	/**
	 * 移除用户速度跟踪器
	 */
	private void recycleVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
/*	
	*//**
	 * 获取X方向的滑动速度,大于0向右滑动，反之向左
	 * 
	 * @return
	 *//*
	private int getScrollVelocity() {
		mVelocityTracker.computeCurrentVelocity(1000);
		int velocity = (int) mVelocityTracker.getXVelocity();
		return velocity;
		return 0;
	}*/
    
/*	@Override
	*//**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 *//*
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
	    MeasureSpec.AT_MOST);
	    super.onMeasure(widthMeasureSpec, expandSpec);
	}*/
	
	/**
	 * 设置滑动完成的监听器
	 * @param listener
	 */
	public void setOnSlideMenuListener(SlideMenuListener listener){
		this.onSlideMenuListener=listener;
	}
	

	public interface SlideMenuListener{
		/**
		 * 菜单将要显示，这里可以根据position处理需要显示或隐藏的菜单
		 * @param position 
		 * @return 返回菜单总数
		 */
		public int onMenuPreShow(int position);
	}	
	
}
