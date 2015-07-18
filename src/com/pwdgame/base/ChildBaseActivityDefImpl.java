package com.pwdgame.base;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.pwdgame.library.R;
import com.pwdgame.util.Utility;
//import com.yizhan.utils.ScreenUtils;
/**
 * 将标题栏共性抽取出来，提供一个实现。方便在activity、fragment多处复用
 * @author xieyuan 2015.06.26
 *
 */
public class ChildBaseActivityDefImpl implements IChildBaseActivity {
	public View mContainerView;
	
	public ViewGroup mHeaderRootLinear;
	/**
	 * 标题栏左侧退出
	 */
	public TextView mBackTextView;
	
	/**
	 * 标题
	 */
	public TextView mTitleTextView;	
	
	/**
	 * 标题上的菜单
	 */
	public LinearLayout mMenuLinear;
	

	
	public ChildBaseActivityDefImpl(Activity activity){
		this.mActivity=activity;
	}	
	

	public View createHeaderView(View view){

		    this.mContainerView=view;
			this.mHeaderRootLinear=(ViewGroup) view.findViewById(R.id.header_titlebar_root_linear);						
			this.mBackTextView=(TextView)view.findViewById(R.id.header_titlebar_back_tv);
			this.mTitleTextView=(TextView)view.findViewById(R.id.header_titlebar_title_tv);
			this.mMenuLinear=(LinearLayout)view.findViewById(R.id.header_titlebar_menu_linear);			


			return this.mContainerView;			
	}	
	
	public void addEventListener(OnClickListener listener){
		if(mBackTextView!=null){
			mBackTextView.setOnClickListener(listener);
		}
		if(mMenuLinear!=null){
			for(int i=0,size=mMenuLinear.getChildCount();i<size;i++){
				mMenuLinear.getChildAt(i).setOnClickListener(listener);
			}
		}
		mOnClickListener = listener;
	}

	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#addMenuItem(com.yizhan.base.XYChildBaseActivityImpl.TitleBarMenuItem)
	 */
	@Override
	public void addMenuItem(TitleBarMenuItem item){
		int padding = Utility.dipToPx(mActivity, 15);
		
		switch(item.type.getValue()){
		case 0:
			TextView mTextView = new TextView(mActivity);
		    mTextView.setText(item.menuResId);
		    mTextView.setId(item.menuId);
		    
		    int id = mHeaderRootLinear.getChildAt(0).getId();
/*		    if(id == R.id.header_titlebar_blue_rl){
		    	mTextView.setTextColor(mActivity.getResources().getColor(R.color.white));
		    }else{
		    	mTextView.setTextColor(mActivity.getResources().getColor(R.color.main_card_head_short_bg));
		    }
		    */
		    mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);		    
		    mTextView.setPadding(padding, 0, padding, 0);
		    mTextView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		    mTextView.setOnClickListener(mOnClickListener);
		    mMenuLinear.addView(mTextView);
			break;
		case 1:
			ImageView mImageView = new ImageView(mActivity);		 
		    mImageView.setId(item.menuId);
		    mImageView.setImageResource(item.menuResId);
		    mImageView.setOnClickListener(mOnClickListener);	    
		    mImageView.setPadding(padding, 0, padding, 0);
		    mMenuLinear.addView(mImageView);
			break;
		}
	}

	@Override
	public void setHeaderBackVisibility(int visibility) {
		if(mBackTextView != null){
			mBackTextView.setVisibility(visibility);
		}
	}
	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#addMenuItem(int, int, com.yizhan.base.XYChildBaseActivityImpl.TitleBarMenuType)
	 */
	@Override
	public void addMenuItem(int menuResId, int menuId,TitleBarMenuType type){
		addMenuItem(new TitleBarMenuItem(menuResId, menuId, type));
	}
	

	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#setHeaderTitle(java.lang.String, com.yizhan.base.XYChildBaseActivityImpl.TitlePosition)
	 */
	@Override
	public void setHeaderTitle(String str,TitlePosition position){
		if(mTitleTextView == null)
			return;
		switch(position.getValue()){
		case 0:
			mTitleViewId = mBackTextView.getId();
		    mTitleTextView.setVisibility(View.GONE);
		    mBackTextView.setText(str);
			break;
		case 1:
			mTitleViewId = mTitleTextView.getId();
		    mBackTextView.setText("");
		    mTitleTextView.setText(str);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#setHeaderTitle(int, com.yizhan.base.XYChildBaseActivityImpl.TitlePosition)
	 */
	@Override
	public void setHeaderTitle(int res,TitlePosition position){
		setHeaderTitle(mActivity.getString(res), position);
	}
	
	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#setHeaderTitle(int)
	 */
	@Override
	public void setHeaderTitle(int res){
		setHeaderTitle(mActivity.getString(res), TitlePosition.CENTER);
	}
	

	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#setHeaderBackground(int)
	 */
	@Override
	public void setHeaderBackground(int resId){
		if(mHeaderRootLinear!=null)
			mHeaderRootLinear.setBackgroundResource(resId);
	}

	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#setHeaderVisibile(int)
	 */
	@Override
	public void setHeaderVisibile(int visibility){
		mHeaderRootLinear.setVisibility(visibility);
	}
	

	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#getContentView()
	 */
	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return mContainerView;
	}
	
	/* (non-Javadoc)
	 * @see com.yizhan.base.IXYChildBaseActivity#getTitleViewId()
	 */
	public int getTitleViewId() {
		// TODO Auto-generated method stub
		return mTitleViewId;
	}
	
	public int getBackViewId(){
		if(mBackTextView == null)
			return 0;
		return mBackTextView.getId();
	}
	
	protected Activity mActivity;
	
	protected int mTitleViewId;
	
	private OnClickListener mOnClickListener;

}
