package com.pwdgame.base;

import android.os.Bundle;

import com.pwdgame.view.SwipeLayout;

public abstract class SwipeChildBaseActivity extends ChildBaseActivity{
	protected SwipeLayout mSwipeLayout;
	protected boolean IsSwipe = true;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {    	
        super.onCreate(savedInstanceState);
        mSwipeLayout = new SwipeLayout(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if(IsSwipe)
           mSwipeLayout.replaceLayer(this);
    }
}
