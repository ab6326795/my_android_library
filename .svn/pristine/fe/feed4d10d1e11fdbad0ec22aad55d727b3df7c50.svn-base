package com.pwdgame.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;

public class XYViewPagerForScrollView extends ViewPager{

	private GestureDetector mDetector;
	
	public XYViewPagerForScrollView(Context context) {
		this(context, null);
	}
    public XYViewPagerForScrollView(Context context, AttributeSet attrs) {  
        super(context, attrs);  
        init();
    }  
  
    public void init(){
    	mDetector=new GestureDetector(new YScrollDetector());
    }
    int preX=0;
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {      	
    	if(mDetector.onTouchEvent(ev)){
    		//这句话的作用 告诉父view，我的单击事件我自行处理，不要阻碍我。
           getParent().requestDisallowInterceptTouchEvent(true); 

    	}else {
    		getParent().requestDisallowInterceptTouchEvent(false);
		}
        return super.dispatchTouchEvent(ev);  
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent ev){
        
    	return super.onTouchEvent(ev);
    }
    
    class YScrollDetector extends SimpleOnGestureListener {  
        @Override  
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {  
		   if(distanceY!=0&&distanceX!=0){  
		             
		   }  
            if(Math.abs(distanceY) < Math.abs(distanceX)) {  
                return true;  
            }  
            return false;  
        }  
    }  
}
