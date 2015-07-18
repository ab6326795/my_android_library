package com.pwdgame.base;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 包含标题栏的Activity 基类，所有包含标题栏的activity必须从它派生
 * @author xieyuan 20150625
 *
 */
public abstract class ChildBaseActivity extends BaseActivity implements IChildBaseActivity,OnClickListener{

	
	protected IChildBaseActivity mChildBaseViewImpl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
    
	@Override
	protected void initHeaderView(){
		super.initHeaderView();
		mChildBaseViewImpl=createChildBaseViewImpl();
		/*mChildBaseViewImpl.createHeaderView(getWindow().getDecorView());
		mChildBaseViewImpl.addEventListener(this);*/
	}	
	
	protected abstract IChildBaseActivity createChildBaseViewImpl();

	@Override
	public void addMenuItem(TitleBarMenuItem item) {
		mChildBaseViewImpl.addMenuItem(item);
	}

	@Override
	public void addMenuItem(int menuResId, int menuId, TitleBarMenuType type) {
		mChildBaseViewImpl.addMenuItem(menuResId, menuId, type);
	}

	@Override
	public void setHeaderTitle(String str, TitlePosition position) {
		mChildBaseViewImpl.setHeaderTitle(str, position);
	}

	@Override
	public void setHeaderTitle(int res, TitlePosition position) {
		mChildBaseViewImpl.setHeaderTitle(res, position);
	}

	@Override
	public void setHeaderTitle(int res) {
		mChildBaseViewImpl.setHeaderTitle(res);
	}

	@Override
	public void setHeaderBackground(int resId) {
		mChildBaseViewImpl.setHeaderBackground(resId);
	}

	@Override
	public void setHeaderVisibile(int visibility) {
		mChildBaseViewImpl.setHeaderVisibile(visibility);
	}
	
	@Override
	public void setHeaderBackVisibility(int visibility){
		mChildBaseViewImpl.setHeaderBackVisibility(visibility);
	}
	
	@Override
	public View getContentView() {
		
		return mChildBaseViewImpl.getContentView();
	}


	@Override
	public void onClick(View view) {
		int id = view.getId();
		if(id == mChildBaseViewImpl.getBackViewId()){
			finish();
		}
		
	}


}
