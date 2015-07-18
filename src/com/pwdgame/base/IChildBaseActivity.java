package com.pwdgame.base;

import android.view.View;

public interface IChildBaseActivity {


	public enum TitlePosition{
		LEFT(0),
		CENTER(1);
		
		private int value;
		private TitlePosition(int value){
			this.value=value;
		}
		public int getValue(){
			return this.value;
		}
	}
	
	public enum TitleBarMenuType{
		TEXT(0),
		ICON(1);
		
		private int value;
		private TitleBarMenuType(int value){
			this.value=value;
		}
		public int getValue(){
			return this.value;
		}
	}

	public class TitleBarMenuItem{
		public TitleBarMenuItem(int menuResId, int menuId,
				TitleBarMenuType type) {
			this.menuResId = menuResId;
			this.menuId = menuId;
			this.type = type;
		}
		public int menuResId;
		public int menuId;
		public TitleBarMenuType type;		
	}
	
	public abstract void addMenuItem(TitleBarMenuItem item);

	public abstract void addMenuItem(int menuResId, int menuId,
			TitleBarMenuType type);

	/* (non-Javadoc)
	 * @see com.sirendaou.ui.proxy.I#setHeaderTitle(java.lang.String)
	 */
	public abstract void setHeaderTitle(String str, TitlePosition position);

	/* (non-Javadoc)
	 * @see com.sirendaou.ui.proxy.I#setHeaderTitle(int)
	 */
	public abstract void setHeaderTitle(int res, TitlePosition position);

	public abstract void setHeaderTitle(int res);

	public abstract void setHeaderBackVisibility(int visibility);
	
	/* (non-Javadoc)
	 * @see com.sirendaou.ui.proxy.I#setHeaderBackground(int)
	 */
	public abstract void setHeaderBackground(int resId);

	/* (non-Javadoc)
	 * @see com.sirendaou.ui.proxy.I#setHeaderVisibile(int)
	 */
	public abstract void setHeaderVisibile(int visibility);

	public abstract View getContentView();

	public abstract int getBackViewId();


}