package com.pwdgame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.pwdgame.util.Utility;

public class XYBaseReleativeLayout extends RelativeLayout{
	
	public XYBaseReleativeLayout(Context context) {
        super(context);
    }

    public XYBaseReleativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XYBaseReleativeLayout(Context context, AttributeSet attrs,
        int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	//关闭键盘
    	Utility.closeBoard(getContext(), this);
    	return super.onInterceptTouchEvent(event);
    }
}
