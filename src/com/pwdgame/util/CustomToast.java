package com.pwdgame.util;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pwdgame.library.R;

public class CustomToast {

	public static final int SMILE=0;
	public static final int CRY=1;
	public static final int SUCCESS=2;
	public static final int WARNING=3;
	
    private static Toast mToast;
    private static ImageView imageCodeProject;
    private static int offsetY;
    public static void showToast(Context mContext, String text,int imageRes, int duration) {
    	
    	mToast=getToastInstance(mContext,text,duration);
    	
        LinearLayout toastView = (LinearLayout) mToast.getView();
        
        if(imageRes!=0){
        	//显示图片
           mToast.setGravity(Gravity.CENTER, 0, 0);
    	   
    	   if(imageCodeProject==null||toastView.getChildAt(0)!=imageCodeProject){
	    	   imageCodeProject = new ImageView(mContext);
	    	   imageCodeProject.setImageResource(imageRes);
	    	   toastView.addView(imageCodeProject, 0);
    	   }else{
    		   imageCodeProject.setImageResource(imageRes);
    	   }
        }else{
        	//不显示图片
        	mToast.setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, offsetY);
        	if(imageCodeProject!=null&&toastView.getChildAt(0)==imageCodeProject){
        		toastView.removeView(imageCodeProject);	
        	}        	
        }
        
        mToast.show();
    }

    public static void showToast(Context mContext, int resId, int duration) {
    	if(mContext!=null)
          showToast(mContext, mContext.getResources().getString(resId),0, duration);
    }
    public static void showToast(Context mContext, String text, int duration) {
    	if(mContext!=null)
    	   showToast(mContext, text, 0,duration);
    }
        
    
    /**
     * 显示带图标的TOAST，一般用于简短的提示.
     * 这时又报了一个：This Toast was not created with Toast.makeText()的错误。由此可见，
     * Toast.makeText()生成的Toast可以访问findViewById方式生成的View，
     * 而自己New Toast()的方式生成的Toast只能访问同样new 出来的View对象。原因大概是在.xml中生成的View对象可以被多个Activity引用，
     * Android为了安全起见，就将其上了锁，并且提供唯一的Toast方式，Toast.makeText()来实现吐丝，这一点和单例模式颇有共同之处。
     * @param context
     * @param text
     * @param duration
     * @param type
     */
    public static void showIconToast(Context context,String text,int duration,int type){
    	mToast=getToastInstance(context,text,duration);
    	
    	LayoutInflater inflater=LayoutInflater.from(context);
    	View view=inflater.inflate(R.layout.toast_layout, null);
    	TextView tView=(TextView) view.findViewById(R.id.toast_layout_msg);
    	ImageView iView=(ImageView) view.findViewById(R.id.toast_layout_icon);
    	
    	tView.setText(text);
    	switch(type){
    	case CRY:
    		iView.setImageResource(R.drawable.tips_error);
    		break;
    	case SUCCESS:
    		iView.setImageResource(R.drawable.tips_success);
    		break;
    	case WARNING:
    		iView.setImageResource(R.drawable.tips_warning);
    		break;
    	default:
    		iView.setImageResource(R.drawable.tips_smile);
    		break;
    	}
    	mToast.setView(view);
		// setGravity方法用于设置位置，此处为垂直居中
    	mToast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
    	mToast.setDuration(duration);
    	mToast.show();
    	mToast=null;
    }
    
    private static Toast getToastInstance(Context context,String str,int duration){    	
        if (mToast != null)
            mToast.setText(str);
        else{
            mToast = Toast.makeText(context, str, duration);
            offsetY=mToast.getYOffset();
        }
        return mToast;
    }
    
    public static void cancel(){
    	if(mToast!=null){
    		mToast.cancel();
    	}
    }
}
