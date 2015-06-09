package com.pwdgame.view;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.pwdgame.library.R;
import com.pwdgame.tasks.XYTimer;
import com.pwdgame.tasks.XYTimerTask;

public class XYRelativeLayoutForViewPager extends RelativeLayout{
	private final static int DOT_WHITE_RESID=R.drawable.guide_dot_white;
	private final static int DOT_BLACK_RESID=R.drawable.guide_dot_black;
	
	//定时切换时间
	private static final int DEF_SWITCH_TIME=4000;
	
	private XYTimer mTimer;
	private XYTimerTask mSwitchTimerTask;
	
	private LinearLayout mDotLinearLayout;
	private int dotWidth;
	private int dotMarginRight;		
	
	private boolean mAutoSwtich;
	private int interval;
	
	public XYRelativeLayoutForViewPager(Context context) {
        this(context,null);
    }

    public XYRelativeLayoutForViewPager(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XYRelativeLayoutForViewPager(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);     
        
        parseAttributes(context.obtainStyledAttributes(attrs,R.styleable.ReletiveLayoutForViewPager));
       
    }
	
	private void parseAttributes(TypedArray a){			
		int margin=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotLinearMargin, 0);
		int marginTop=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotLinearMarginTop,0);
		int marginBottom=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotLinearMarginBottom,0);
		int marginLeft=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotLinearMarginLeft,0);
		int marginRight=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotLinearMarginRight,0);	
		int dotCount=a.getInteger(R.styleable.ReletiveLayoutForViewPager_dotCount, 0);
		dotWidth=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotWidth,
				  TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
		dotMarginRight=(int) a.getDimension(R.styleable.ReletiveLayoutForViewPager_dotMarginRight,
				TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getResources().getDisplayMetrics()));
		mAutoSwtich=a.getBoolean(R.styleable.ReletiveLayoutForViewPager_viewpager_autoswitch, true);
		interval=a.getInteger(R.styleable.ReletiveLayoutForViewPager_viewpager_interval, DEF_SWITCH_TIME);
		
		a.recycle();				
		
		mDotLinearLayout=new LinearLayout(getContext());
	    RelativeLayout.LayoutParams mLayoutParams= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT,
	    		RelativeLayout.LayoutParams.WRAP_CONTENT);

	    mLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	    if(margin!=0){
	    	mDotLinearLayout.setPadding(margin, margin, margin, margin);
	    }else{
	    	mDotLinearLayout.setPadding(marginLeft, marginTop, marginRight, marginBottom);
	    }
	    mDotLinearLayout.setLayoutParams(mLayoutParams);
	    mDotLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
	    mDotLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);
	    changeDot(dotCount);

	}  

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	@Override
	protected void onAttachedToWindow(){
		int size=getChildCount();
		for(int i=0;i<size;i++){
			View child=getChildAt(i);
			if(child instanceof ViewPager){	
				ViewPager mViewPager=(ViewPager)child;
				PagerAdapter mAdapter=mViewPager.getAdapter();
				if(mAdapter!=null){
					int viewCount=mAdapter.getCount();				
					changeDot(viewCount);
					mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		    	    
					if(mAutoSwtich){
						mTimer=new XYTimer();
						mSwitchTimerTask=new SwitchTimerTask(mViewPager);
						mTimer.schedule(mSwitchTimerTask, interval, interval);
						
					}
				}
			}
		}

		addView(mDotLinearLayout);
		super.onAttachedToWindow();		
	}
	
	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		if(mTimer!=null){
			mTimer.cancel();
			mTimer=null;
		}
		removeView(mDotLinearLayout);		
	}
	
	/**
	 * 更改添加小圆点
	 * @param size
	 */
	public void changeDot(int size){		
		mDotLinearLayout.removeAllViews();
	    //一个不加点，大于1个才加点
	    if(size<=1)
	    	return;
		for(int i=0;i<size;i++){
			//生成点
			ImageView dot=new ImageView(getContext());			
			LinearLayout.LayoutParams dotLayoutParams=new LinearLayout.LayoutParams
					(new ViewGroup.LayoutParams(dotWidth,dotWidth));
			dotLayoutParams.setMargins(0, 0, dotMarginRight, 0);
			dot.setLayoutParams(dotLayoutParams);		
			dot.setScaleType(ScaleType.FIT_XY);
			if(i==0)
				dot.setImageResource(DOT_BLACK_RESID);
			else
			    dot.setImageResource(DOT_WHITE_RESID);
			
			mDotLinearLayout.addView(dot);	
		}
	}
	
	/**
	 * 移除所有圆点
	 */
	public void removeAllPoint(){
		if(mDotLinearLayout!=null)
			mDotLinearLayout.removeAllViews();
	}
	
	/**
	 * ViewPager状态更改
	 * @author Administrator
	 *
	 */
	class MyOnPageChangeListener implements ViewPager.OnPageChangeListener{

		@Override
		public void onPageSelected(int position) {
			int len=mDotLinearLayout.getChildCount();
			if(len<=0)
				return;
			//选中点			
			int selectPosition=position%len;
			for(int i=0;i<len;i++){
				if(i==selectPosition)
					((ImageView)mDotLinearLayout.getChildAt(i)).setImageResource(DOT_BLACK_RESID);
				else
				   ((ImageView)mDotLinearLayout.getChildAt(i)).setImageResource(DOT_WHITE_RESID);
			}
			//每次选项更改重新设置时间
			mSwitchTimerTask.resetInterval();
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	/**
	 * 定时器任务
	 * @author Administrator
	 *
	 */
	private class SwitchTimerTask extends XYTimerTask{
		private ViewPager viewPager;		
		
		public SwitchTimerTask(ViewPager viewPager){
			this.viewPager=viewPager;
		}
		
		@Override
		public void run() {
			this.viewPager.post(new Runnable() {
				
				@Override
				public void run() {
					PagerAdapter adapter=viewPager.getAdapter();
					if(adapter!=null){
						int count=adapter.getCount();
						if(count>0)
					       viewPager.setCurrentItem((viewPager.getCurrentItem()+1)%count);
					}
				}
			});
			
		}
		
	};
}
