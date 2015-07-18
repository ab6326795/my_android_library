package com.pwdgame.base;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pwdgame.util.CustomToast;

public abstract class BaseFragment extends android.support.v4.app.Fragment implements OnClickListener{

	public void showToast(String str){
		CustomToast.showToast(getActivity(), str, Toast.LENGTH_SHORT);
	}
	
	public void showToast(int res){
		CustomToast.showToast(getActivity(), res, Toast.LENGTH_SHORT);
	}
	/**
	 * 设置Activity布局，该方法将在onCreate首先调用
	 */
	protected abstract int getConViewResId();
	
	protected abstract void initViews(View view);
	
	protected abstract void initDatas();
	

  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(getConViewResId(), container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
    	initHeaderView(view);
    	initViews(view);
    	initDatas(); 
    }
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
    	super.onActivityCreated(savedInstanceState);
    	
    }
    
	protected void initHeaderView(View view){


	}	
	
	/**
	 * 键盘事件分发
	 * @param keyCode
	 * @param event
	 * @return
	 */
	public boolean onKeyUp(int keyCode,KeyEvent event){
		return false;
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
