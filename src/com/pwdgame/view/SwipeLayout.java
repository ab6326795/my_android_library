package com.pwdgame.view;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
/**
 * 滑动边缘可以关闭Activity的GroupView
 * lastupdate 2015.7.11
 * @author xieyuan 
 * @version 1.1
 *
 */
public class SwipeLayout  extends FrameLayout {

	/**边缘多少DP以内开始监听滑动边缘事件 ，这个单位会是DP，不是PX**/
	private static final int SWIPE_EDGE_WIDTH = 40;
	
	protected Context mContext;
	
	protected boolean swipeAnyWhere = false;//是否可以在页面任意位置右滑关闭页面，如果是false则从左边滑才可以关闭
    private int layerColor = Color.parseColor("#88000000");
    private View backgroundLayer;
	private boolean swipeFinished = false;

	private VelocityTracker mVelocityTracker; 
	private int mSwipeEdgeWidth;
	private int mScreenWidth;
    private int mMaxVelocity;
    
    private float lastX,lastY;

	private boolean canSwipe = false;
	private View contentView;
	private Activity mActivity;

    private int duration = 200;

    private AnimatorSet animator;
    
    public SwipeLayout(Context context) {
        this(context,null);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    /**
     * 初始化一些数值
     */
    private void init(){
    	mSwipeEdgeWidth =  Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
    			          SWIPE_EDGE_WIDTH, mContext.getResources().getDisplayMetrics()));
    	mScreenWidth = getScreenWidth(mContext);
    	mMaxVelocity = ViewConfiguration.get(mContext).getScaledMaximumFlingVelocity();
    }
    
	/**
	 * 添加用户的速度跟踪器
	 * @param event
	 */
    private void addVelocityTracker(MotionEvent event){
		if(mVelocityTracker==null){
			mVelocityTracker=VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}
	/**
	 * 移除用户速度跟踪器
	 */
	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}
	
    public boolean isSwipeAnyWhere() {
		return swipeAnyWhere;
	}

	public void setSwipeAnyWhere(boolean swipeAnyWhere) {
		this.swipeAnyWhere = swipeAnyWhere;
	}

	public int getLayerColor() {
		return layerColor;
	}

	public void setLayerColor(int layerColor) {
		this.layerColor = layerColor;
	}
	
	/**
	 * 页面是否被关闭
	 * @return
	 */
    public boolean isSwipeFinished() {
		return swipeFinished;
	}

    private int getScreenWidth(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
    
    
    public void replaceLayer(Activity activity) {             
        mActivity = activity;       
        setClickable(true);
        backgroundLayer = new View(activity);
        backgroundLayer.setBackgroundColor(layerColor);
        final ViewGroup root = (ViewGroup) activity.getWindow().getDecorView();
        contentView = root.getChildAt(0);
        //在Android5.0上，content的高度不再是屏幕高度，而是变成了Activity高度，比屏幕高度低一些，
        //如果this.addView(content),就会使用以前的params，这样content会像root一样比content高出一部分，导致底部空出一部分
        //在装有Android 5.0的Nexus5上，root,SwipeLayout和content的高度分别是1920、1776、1632，144的等差数列……
        //在装有Android4.4.3的HTC One M7上，root,SwipeLayout和content的高度分别相同，都是1920
        //所以我们要做的就是给content一个新的LayoutParams，Match_Parent那种，也就是下面的-1
        ViewGroup.LayoutParams params = contentView.getLayoutParams();
        ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(-1, -1);
        ViewGroup.LayoutParams params3 = new ViewGroup.LayoutParams(-1, -1);
        root.removeView(contentView);
        this.addView(backgroundLayer, params3);
        this.addView(contentView, params2);
        root.addView(this, params);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
    	addVelocityTracker(event);
    	
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:                   
            	lastX = x;
            	lastY = y;
                break;
            case MotionEvent.ACTION_MOVE:                  
                 if(Math.abs(x - lastX) > Math.abs(y - lastY) && x < mSwipeEdgeWidth){
                	 canSwipe = true;
                     
                 }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            	
                canSwipe = false;
                break;
            default:
                break;
        }
/*        if (swipeAnyWhere) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    downX = ev.getX();
                    downY = ev.getY();
                    currentX = downX;
                    currentY = downY;
                    lastX = downX;
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = ev.getX() - downX;
                    float dy = ev.getY() - downY;
                    if ((dy == 0f || Math.abs(dx / dy) > 1) && (dx * dx + dy * dy > touchSlop * touchSlop)) {
                        downX = ev.getX();
                        downY = ev.getY();
                        currentX = downX;
                        currentY = downY;
                        lastX = downX;
                        canSwipe = true;
                        tracker = VelocityTracker.obtain();
                        return true;
                    }
                    break;
            }
        } else*/ 

        return canSwipe? canSwipe : super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canSwipe) {
            addVelocityTracker(event);
            
            int action = event.getAction();
            float x = event.getX();

            switch (action) {
                case MotionEvent.ACTION_DOWN:                   
                    lastX = x;
                    break;
                case MotionEvent.ACTION_MOVE:                  
                    float dx = x - lastX;
                    if (ViewHelper.getX(contentView) + dx < 0) {
                        setContentX(0);
                    } else {
                        setContentX(ViewHelper.getX(contentView) + dx);
                    }
                    lastX = x;
                    
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                	swipeByDistanceX();
                	releaseVelocityTracker();
                    canSwipe = false;
                    break;
                default:
                    break;
            }
        }
        return super.onTouchEvent(event);
    }

    private void swipeByDistanceX(){
    	// 求伪瞬时速度
        mVelocityTracker.computeCurrentVelocity(1000,mMaxVelocity);
        //Logger.debug("XVelocity:"+mVelocityTracker.getXVelocity()+",maxVelocity:"+mMaxVelocity);
        
        if (mVelocityTracker.getXVelocity() > mMaxVelocity/10) {
        	animateFinish(true);
            //animateFromVelocity(mVelocityTracker.getXVelocity());
        } else {
            if (ViewHelper.getX(contentView) > mScreenWidth / 4) {
                animateFinish(false);
            } else {
                animateBack(false);
            }
        }
    }

/*    private void animateFromVelocity(float v) {
        if (v > 0) {
            if (ViewHelper.getX(contentView) < mScreenWidth / 2
                    && v * duration / 1000 + ViewHelper.getX(contentView) < mScreenWidth) {
                animateBack(false);
            } else {
                animateFinish(true);
            }
        } else {
            if (ViewHelper.getX(contentView) > mScreenWidth / 2
                    && v * duration / 1000 + ViewHelper.getX(contentView) > mScreenWidth / 4) {
                animateFinish(false);
            } else {
                animateBack(true);
            }
        }

    }*/
    public void cancelPotentialAnimation() {
        if (animator != null) {
            animator.removeAllListeners();
            animator.cancel();
        }
    }

    private void setContentX(float x) {
        ViewHelper.setX(contentView, x);
        if (backgroundLayer != null) {
        	ViewHelper.setAlpha(backgroundLayer, 1 - x / getWidth());
        }
    }


    /**
     * 弹回，不关闭，因为left是0，所以setX和setTranslationX效果是一样的
     *
     * @param withVel
     */
    private void animateBack(boolean withVel) {
        cancelPotentialAnimation();
        animator = new AnimatorSet();
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(contentView, "x", ViewHelper.getX(contentView), 0);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(backgroundLayer, "alpha", ViewHelper.getAlpha(backgroundLayer), 1);
        ArrayList<Animator> animators = new ArrayList<Animator>();
        animators.add(animatorX);
        animators.add(animatorA);
        if (withVel) {
            animator.setDuration((long) (duration * ViewHelper.getX(contentView) / mScreenWidth));
        } else {
            animator.setDuration(duration);
        }
        animator.playTogether(animators);
        animator.start();
    }

    private void animateFinish(boolean withVel) {
        cancelPotentialAnimation();
        animator = new AnimatorSet();

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(contentView, "x", ViewHelper.getX(contentView), mScreenWidth);
        ObjectAnimator animatorA = ObjectAnimator.ofFloat(backgroundLayer, "alpha", ViewHelper.getAlpha(backgroundLayer), 0);
        ArrayList<Animator> animators = new ArrayList<Animator>();
        animators.add(animatorX);
        animators.add(animatorA);
        if (withVel) {
            animator.setDuration((long) (duration * (mScreenWidth - ViewHelper.getX(contentView)) / mScreenWidth));
        } else {
            animator.setDuration(duration);
        }
        animator.playTogether(animators);

        animator.addListener(new AnimatorListenerAdapter(){
        	 @Override
             public void onAnimationEnd(Animator animation) {
                 if (!mActivity.isFinishing()) {
                     swipeFinished = true;
                     mActivity.finish();
                 }
             }

        });
        animator.start();
    }


}