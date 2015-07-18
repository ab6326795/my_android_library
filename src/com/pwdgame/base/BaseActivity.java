package com.pwdgame.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pwdgame.app.ActivityStackManager;
import com.pwdgame.library.R;
import com.pwdgame.util.CustomToast;
import com.pwdgame.widget.LoadingDialog;
/**
 * 不含标题栏的Activity 基类，所有activity必须从它派生
 * @author xieyuan 20150625
 *
 */
public abstract class BaseActivity extends ActionBarActivity{
	//是否显示切换动画
	public boolean bAnimation=false;
	
	protected BaseActivity mInstance;	

	protected LoadingDialog mLoadingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);			
		setConView();
		// header init
		initHeaderView();
		initBaseData();
		// centent init 
		initViews();
		initDatas();
	}
	
	/**
	 * 设置Activity布局，该方法将在onCreate首先调用
	 */
	protected abstract void setConView();
	
	protected abstract void initViews();
	
	protected abstract void initDatas();
	
	private void initBaseData(){
		//添加动画效果
		if(bAnimation){
			if(getParent()!=null){				
				getParent().overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
			}
			else {
				overridePendingTransition(R.anim.activity_right_in, R.anim.activity_left_out);
			}
		}		
		mInstance=this;

		mLoadingDialog = new LoadingDialog(this);
		// 添加Activity到堆栈
		ActivityStackManager.getInstance().addActivity(this);
	}
	
	
	public void showProgressBar() {
		mLoadingDialog.show();
	}
	public void showProgressBar(String string) {
		mLoadingDialog.show(string);
	}
	
	public boolean isShowProgressBar(){
		return mLoadingDialog.isShowing();
	}
	
	public void dismissProgressBar() {
		if(mLoadingDialog != null && mLoadingDialog.isShowing()){
			mLoadingDialog.dismiss();
		}	
		
	}
	
	protected void initHeaderView(){


	}	

	public void showToast(String str){
		CustomToast.showToast(this, str, Toast.LENGTH_SHORT);
	}
	

	public void showToast(int res){
		CustomToast.showToast(this, res, Toast.LENGTH_SHORT);
	}
	

	@Override
	public void finish() {		
		super.finish();

		//添加动画效果
		if(bAnimation){
			if(getParent()!=null){				
				getParent().overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
			}
			else {
				overridePendingTransition(R.anim.activity_left_in, R.anim.activity_right_out);
			}
		}
	}
	
	@Override
	public boolean onKeyUp(int keyCode,KeyEvent event){
		if(KeyEvent.KEYCODE_BACK == keyCode){
			if(mLoadingDialog != null && mLoadingDialog.isShowing()){
				dismissProgressBar();
				return true;
			}
		}
		return super.onKeyUp(keyCode, event);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	}
		
	@Override
	protected void onDestroy(){
		super.onDestroy();
		// 结束Activity&从堆栈中移除
		ActivityStackManager.getInstance().finishActivity(this);
	}
	
}
