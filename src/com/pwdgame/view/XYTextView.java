package com.pwdgame.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

public class XYTextView extends TextView{
/*	static Typeface mTypeface;
	static{
		mTypeface=Typeface.createFromAsset(MyApplication.getAppContext().getAssets(), "fonts/jiankatong.ttf");
	}
	android:lineSpacingMultiplier="1.2"
	*/
    public XYTextView(Context context) { 
    	this(context, null); 
    } 
 
    public XYTextView(Context context, AttributeSet attrs) { 
    	//这里构造方法也很重要，不加这个很多属性不能再XML里面定义
    	this(context, attrs, android.R.attr.textStyle); 
    } 
    
    public XYTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //setLineSpacing(0, 1.2F);
    }

    public final void setTextA(CharSequence str){
    	if(TextUtils.isEmpty(str)){
    		setText("无");
    	}else{
    		setText(str);
    	}
    	
    }
}
