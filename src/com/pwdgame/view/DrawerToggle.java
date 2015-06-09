package com.pwdgame.view;

import java.lang.reflect.Method;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pwdgame.library.R;


public class DrawerToggle extends android.support.v4.app.ActionBarDrawerToggle {

    private static final String TAG = DrawerToggle.class.getName();

    protected ActionBarActivity mActivity;
    protected DrawerLayout mDrawerLayout;

    protected int mOpenDrawerContentDescRes;
    protected int mCloseDrawerContentDescRes;
    protected DrawerArrowDrawable mDrawerImage;
    protected boolean animateEnabled;

    //是否已经设置了自定义的指示器
    protected boolean isSetIndoctor;
    
    public DrawerToggle(ActionBarActivity activity, DrawerLayout drawerLayout, int drawerImageRes, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, drawerImageRes, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    public DrawerToggle(ActionBarActivity activity, DrawerLayout drawerLayout, DrawerArrowDrawable drawerImage, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
        super(activity, drawerLayout, R.drawable.ic_drawer, openDrawerContentDescRes, closeDrawerContentDescRes);
        mActivity = activity;
        mDrawerLayout = drawerLayout;
        mOpenDrawerContentDescRes = openDrawerContentDescRes;
        mCloseDrawerContentDescRes = closeDrawerContentDescRes;
        mDrawerImage = drawerImage;
        animateEnabled = true;
    }

    @Override
    public void syncState() {
        if (mDrawerImage == null) {
            super.syncState();
            return;
        }
        if (animateEnabled) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerImage.setProgress(1.f);
            } else {
                mDrawerImage.setProgress(0.f);
            }
        }
        setActionBarUpIndicator();
        setActionBarDescription();
    }

    @Override
    public void setDrawerIndicatorEnabled(boolean enable) {
        if (mDrawerImage == null) {
            super.setDrawerIndicatorEnabled(enable);
            return;
        }
        setActionBarUpIndicator();
        setActionBarDescription();
    }

    @Override
    public boolean isDrawerIndicatorEnabled() {
        if (mDrawerImage == null) {
            return super.isDrawerIndicatorEnabled();
        }
        return true;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (mDrawerImage == null) {
            super.onConfigurationChanged(newConfig);
            return;
        }
        syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        if (mDrawerImage == null) {
            super.onDrawerSlide(drawerView, slideOffset);
            return;
        }
        if (animateEnabled) {
            mDrawerImage.setVerticalMirror(!mDrawerLayout.isDrawerOpen(GravityCompat.START));
            mDrawerImage.setProgress(slideOffset);
        }
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        if (mDrawerImage == null) {
            super.onDrawerOpened(drawerView);
            return;
        }
        if (animateEnabled) {
            mDrawerImage.setProgress(1.f);
        }
        setActionBarDescription();
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        if (mDrawerImage == null) {
            super.onDrawerClosed(drawerView);
            return;
        }
        if (animateEnabled) {
            mDrawerImage.setProgress(0.f);
        }
        setActionBarDescription();
    }

    /**
     * 设置指示器显示的ImageView
     * @param iv
     */
    public void setIndoctorDisplay(ImageView iv){
    	iv.setImageDrawable(mDrawerImage);
    	isSetIndoctor=true;
    }
    
    /**
     * 如果指示器未曾设置，则默认设置到标题栏上
     */
    protected void setActionBarUpIndicator() {
    	if(isSetIndoctor)
    		return;
        if (mActivity != null) {
            try {
                Method setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator",
                    Drawable.class);
                setHomeAsUpIndicator.invoke(mActivity.getSupportActionBar(), mDrawerImage);
                return;
            } catch (Exception e) {
                Log.e(TAG, "setActionBarUpIndicator error", e);
            }

            final View home = mActivity.findViewById(android.R.id.home);
            if (home == null) {
                return;
            }

            final ViewGroup parent = (ViewGroup) home.getParent();
            final int childCount = parent.getChildCount();
            if (childCount != 2) {
                return;
            }

            final View first = parent.getChildAt(0);
            final View second = parent.getChildAt(1);
            final View up = first.getId() == android.R.id.home ? second : first;

            if (up instanceof ImageView) {
                ImageView upV = (ImageView) up;
                upV.setImageDrawable(mDrawerImage);
            }
        }
    }

    protected void setActionBarDescription() {
        if (mActivity != null && mActivity.getSupportActionBar() != null) {
            try {
                Method setHomeActionContentDescription = ActionBar.class.getDeclaredMethod(
                    "setHomeActionContentDescription", Integer.TYPE);
                setHomeActionContentDescription.invoke(mActivity.getSupportActionBar(),
                    mDrawerLayout.isDrawerOpen(GravityCompat.START) ? mOpenDrawerContentDescRes : mCloseDrawerContentDescRes);
                
                mActivity.getSupportActionBar().setSubtitle(mActivity.getSupportActionBar().getSubtitle());
 
            } catch (Exception e) {
                Log.e(TAG, "setActionBarUpIndicator", e);
            }
        }
    }

    public void setAnimateEnabled(boolean enabled) {
        this.animateEnabled = enabled;
    }

    public boolean isAnimateEnabled() {
        return this.animateEnabled;
    }

}
