package com.pwdgame.bean;

import android.widget.LinearLayout;


public class ActionBarMenuItem{
	public ActionBarMenuItem(){
		
	}
	public ActionBarMenuItem(int id,String title, Object icon,String subTitle) {
		set(id, title, icon, subTitle);
	}
	public ActionBarMenuItem(int id,String title, ActionBarIcon icon,String subTitle) {
		set(id, title, icon, subTitle);
	}
	public void set(int id,String title, Object icon,String subTitle) {
		this.titleStr=title;
		this.icon=new ActionBarIcon(icon,LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
		this.id=id;
		this.subTitleStr=subTitle;
	}
	
	public void  set(int id,String title, ActionBarIcon icon,String subTitle) {
		this.titleStr=title;
		this.icon=icon;
		this.id=id;
		this.subTitleStr=subTitle;
	}
	
	public int id;		
	public CharSequence titleStr;
	public ActionBarIcon icon;
	public CharSequence subTitleStr;

	
}
