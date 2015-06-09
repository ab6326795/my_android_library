package com.pwdgame.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class XYBottomScrollView extends ScrollView{
	private OnScrollChangeListener mScrollChangeListener;
	
	public XYBottomScrollView(Context context) {
		this(context, null);
	}
	public XYBottomScrollView(Context context,AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public XYBottomScrollView(Context context,AttributeSet attrs,int defStyle) {
		super(context,attrs,defStyle);
	}
	@Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if(mScrollChangeListener!=null)
			mScrollChangeListener.OnScrollChange(l, t, oldl, oldt);
	}
/*	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
		if(scrollY != 0 && null != onScrollToBottom){
			onScrollToBottom.onScrollBottomListener(clampedY);
		}
	}
	*/
	public void setOnScrollListener(OnScrollChangeListener listener){
		mScrollChangeListener = listener;
	}

	public interface OnScrollChangeListener{
		public void OnScrollChange(int l, int t, int oldl, int oldt);
	}
}
