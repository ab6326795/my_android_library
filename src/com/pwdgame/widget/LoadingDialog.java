package com.pwdgame.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.pwdgame.library.R;

public class LoadingDialog extends AlertDialog {

    private TextView tips_loading_msg;

    private String message = null;

    public LoadingDialog(Context context) {    	
        this(context,null);
       
    }

    public LoadingDialog(Context context, String message) {
        this(context,0,message);
    }

    public LoadingDialog(Context context, int theme, String message) {
        super(context, theme);
        if(message==null){
        	this.message = getContext().getResources().getString(R.string.msg_load_ing);
        }else{
        	this.message = message;	
        }        
        this.setCancelable(false);
        setDimAmount(0f);
    }

    public void setDimAmount(float dimAmount){
    	WindowManager.LayoutParams layoutParams= this.getWindow().getAttributes();
    	layoutParams.dimAmount=dimAmount;    	
    	this.getWindow().setAttributes(layoutParams);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.loadingdialog_layout);
        tips_loading_msg = (TextView) findViewById(R.id.loadingdialog_msg);
        tips_loading_msg.setText(this.message);
    }

    public void setText(String message) {
        this.message = message;
        tips_loading_msg.setText(this.message);
    }

    public void setText(int resId) {
        setText(getContext().getResources().getString(resId));
    }
    public void setTextColor(int color){
    	tips_loading_msg.setTextColor(color);
    }
    
    public void show(String str){
    	message = str;
    	if(isShowing()){
    		if(tips_loading_msg != null){
    			tips_loading_msg.setText(message);
    		}
    	}else{    		
        	super.show();	
    	}    	
    }

    @Override
    public void show(){
    	message = "";
    	super.show();
    }
}
