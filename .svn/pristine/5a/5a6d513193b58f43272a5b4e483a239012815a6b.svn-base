package com.pwdgame.widget;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.Transformation;

public class ExpandAnimation extends Animation {
    private final int mStartHeight;
    private final int mDeltaHeight;
    private View mView;
    
    public ExpandAnimation(View mView,int startHeight, int endHeight) {
    	this.mView=mView;
        mStartHeight = startHeight;
        mDeltaHeight = endHeight - startHeight;      
    }

    @Override
    protected void applyTransformation(float interpolatedTime,
        Transformation t) {
        android.view.ViewGroup.LayoutParams lp = mView.getLayoutParams();
        lp.height = (int) (mStartHeight + mDeltaHeight * interpolatedTime);
        mView.setLayoutParams(lp);
    }

    @Override
    public boolean willChangeBounds() {
        // TODO Auto-generated method stub
        return true;
    }
}
