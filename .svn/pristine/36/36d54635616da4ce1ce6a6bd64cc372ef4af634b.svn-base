package com.pwdgame.view;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;



/**
 * 自定义实现的滚动布局
 * 
 * @author admin
 * 
 */
public class XYScrollLayout extends ViewGroup {

	private static final String TAG = "ScrollLayout";
	private VelocityTracker mVelocityTracker; // 用于判断甩动手势
	private static final int SNAP_VELOCITY = 600;
	private Scroller mScroller; // 滑动控制器
	private int mCurScreen;
	private int mDefaultScreen = 0;

	/*
	 * 记录滑动时上次手指所处的位置
	 */
	private float mLastMotionX;
	private float mLastMotionY;

	/*
	 * Touch状态值 0：静止 1：滑动
	 */
	private static final int TOUCH_STATE_REST = 0;
	private static final int TOUCH_STATE_SCROLLING = 1;

	/*
	 * 记录当前touch事件状态--滑动（TOUCH_STATE_SCROLLING）、静止（TOUCH_STATE_REST 默认）
	 */
	private int mTouchState = TOUCH_STATE_REST;
	/*
	 * 
	 * 记录touch事件中被认为是滑动事件前的最大可滑动距离
	 */

	private int mTouchSlop;

	// 滑动距离
	private int deltaX;

	/**
	 * 回调接口
	 */
	private OnViewChangeListener mOnViewChangeListener;
	private OnViewClickListener mOnViewClickListener;

	public XYScrollLayout(Context context) {
		super(context);
		init(context);
	}

	public XYScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public XYScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		mCurScreen = mDefaultScreen;
		mScroller = new Scroller(context);
		mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
	}

	/**
	 * 分配、调整布局位置 》》
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		if (changed) {
			int childLeft = 0;
			final int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final View childView = getChildAt(i);
				if (childView.getVisibility() != View.GONE) {
					final int childWidth = childView.getMeasuredWidth();
					childView.layout(childLeft, 0, childLeft + childWidth,
							childView.getMeasuredHeight());
					childLeft += childWidth;
				}
			}
		}
	}

	/**
	 * 测量并滚动布局
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		scrollTo(mCurScreen * width, 0);
	}

	/**
	 * 返回当前选中索引
	 * 
	 * @return
	 */
	public int getSelection() {
		return mCurScreen;
	}

	/**
	 * 根据当前的位置滑动到相应的页面
	 */
	public void snapToDestination() {
		final int screenWidth = getWidth();
		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	/**
	 * 滚动到视图到指定索引的子项位置
	 * 
	 * @param whichScreen
	 *            索引
	 */
	public void snapToScreen(int whichScreen) {
		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {
			final int delta = whichScreen * getWidth() - getScrollX();
			int duration=Math.max(Math.abs(delta)/getWidth()*1000,1000);
			mScroller.startScroll(getScrollX(), 0, delta, 0,duration);
			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout
			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.onViewChange(mCurScreen);
			}
		}
	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * 实现触摸滚动的逻辑 触摸事件
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {

		// TODO Auto-generated method stub
		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			Log.i("", "onTouchEvent  ACTION_DOWN");
			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				// 添加用户的运动跟踪器
				mVelocityTracker.addMovement(event);
			}
			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}
			mLastMotionX = x;

			break;
		case MotionEvent.ACTION_MOVE:
			deltaX = (int) (mLastMotionX - x);
			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}
				mLastMotionX = x;
				scrollBy(deltaX, 0);
			}

			break;
		case MotionEvent.ACTION_UP:
			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}
			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();

				// 滑动距离小，速度慢则视为点击
				if (Math.abs(deltaX) < 20
						&& Math.abs(velocityX) < SNAP_VELOCITY / 3
						&& mOnViewClickListener != null)
					mOnViewClickListener.onViewClick(mCurScreen);
			}
			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}
			deltaX = 0;
			mTouchState = TOUCH_STATE_REST;
			break;
		case MotionEvent.ACTION_CANCEL:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	/**
	 * 是否可以滚动
	 * 
	 * @param deltaX
	 * @return
	 */
	private boolean IsCanMove(int deltaX) {
		if (getScrollX() <= 0 && deltaX < 0) {
			return false;
		}
		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 实现这个方法来拦截所有的触摸屏运动事件。这可以让你看的事件， 他们派给你的孩子，并把当前的姿态的所有权的任何一点。
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		Log.e(TAG, "onInterceptTouchEvent-slop:" + mTouchSlop);

		final int action = event.getAction();

		if ((action == MotionEvent.ACTION_MOVE)
				&& (mTouchState != TOUCH_STATE_REST)) {
			return true;
		}

		final float x = event.getX();
		final float y = event.getY();

		switch (action) {

		case MotionEvent.ACTION_DOWN:
			mLastMotionX = x;
			mLastMotionY = y;
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_REST
					: TOUCH_STATE_SCROLLING;
			break;

		case MotionEvent.ACTION_MOVE:
			final int xDiff = (int) Math.abs(mLastMotionX - x);
			if (xDiff > mTouchSlop) {
				if (Math.abs(mLastMotionY - y) / Math.abs(mLastMotionX - x) < 1)
					mTouchState = TOUCH_STATE_SCROLLING;
			}

			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			mTouchState = TOUCH_STATE_REST;
			break;
		}

		return mTouchState != TOUCH_STATE_REST;
	}

	/**
	 * 设置回调函数（监听器）
	 * 
	 * @param listener
	 */
	public void setOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}

	/**
	 * 设置回调函数（监听器）
	 * 
	 * @param listener
	 */
	public void setOnViewClickListener(OnViewClickListener listener) {
		mOnViewClickListener = listener;
	}
	
	/**
	 * 视图更改回调（通知）接口
	 * 
	 * @author admin
	 * 
	 */
	public interface OnViewChangeListener {
		public void onViewChange(int index);
	}
	/**
	 * 视图更改回调（通知）接口
	 * 
	 * @author admin
	 * 
	 */
	public interface OnViewClickListener {
		public void onViewClick(int index);
	}
}