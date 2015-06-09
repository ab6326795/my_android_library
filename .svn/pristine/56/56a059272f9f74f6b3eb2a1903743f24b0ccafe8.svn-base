package com.pwdgame.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class XYMenuInflater<T> {

	private Context mContext;
	private List<T> list=new ArrayList<T>();		
	
	public XYMenuInflater(Context mContext){
		this.mContext=mContext;
	}
	
	public List<T> inflater(int res,Class<T> c,Menu menu) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException, InstantiationException{
		
		MenuInflater inflater=new MenuInflater(mContext);		
		inflater.inflate(res, menu);
		for(int i=0,size=menu.size();i<size;i++){
			T t=c.newInstance();
			Class<?> class1= t.getClass();
			
			MenuItem item=menu.getItem(i);
			int id=item.getItemId();
			Drawable drawable=item.getIcon();
			CharSequence title=item.getTitle();
			
			Field drawableField=class1.getField("drawable");
			drawableField.setAccessible(true);
			drawableField.set(t, drawable);
			
			Field idField=class1.getField("id");
			idField.setAccessible(true);
			idField.set(t, id);
			
			Field titleField=class1.getField("titleStr");
			titleField.setAccessible(true);
			titleField.set(t, title);
			
			list.add(t);
		}
		//清除菜单
		menu.clear();
		
		return list;
	}

}
