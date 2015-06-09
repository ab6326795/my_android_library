package com.pwdgame.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 适配器基类
 * 
 * @author admin
 * 
 * @param <T>
 */
public abstract class XYBaseAdapter<T> extends android.widget.BaseAdapter {

	protected List<T> arrayList;
	protected Context mContext;
	protected LayoutInflater inflater;
	

	
	public XYBaseAdapter(Context mContext) {
		this(mContext, null);
	}
	public XYBaseAdapter(Context mContext,List<T> arrayList) {
		this.mContext = mContext;
		inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.arrayList=arrayList;
	}
	@Override
	public int getCount() {
		return (arrayList == null) ? 0 : arrayList.size();
	}

	@Override
	public Object getItem(int arg0) {

		return (arrayList == null) ? null : arrayList.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 返回View 给 ListView等 Item，由子类实现
	 */
	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);

	public void put(T t){
		if(this.arrayList!=null){
			this.arrayList.add(t);			
		}
	}
	public void put(List<T> t){
		if(this.arrayList!=null){
			this.arrayList.addAll(t);			
		}
	}
	/**
	 * 设置数据源
	 * 
	 * @param arrayList
	 */
	public void setArrayList(List<T> arrayList) {
		this.arrayList = arrayList;
		notifyDataSetChanged();
	}
		

	public void setArrayList(T[] arrayList) {
		this.arrayList = new ArrayList<T>();
		for (T t : arrayList) {
			this.arrayList.add(t);
		}
		notifyDataSetChanged();
	}
	public void setArrayList(T[] arrayList,List<T> headerValue){
		this.arrayList = new ArrayList<T>();
		for (T t : headerValue) {
			this.arrayList.add(t);
		}
		if(arrayList!=null)
		for (T t : arrayList) {
			this.arrayList.add(t);
		}
		notifyDataSetChanged();
	}
	/**
	 * 返回数据源对象
	 * 
	 * @return
	 */
	public List<T> getArrayList() {
		return arrayList;
	}

	public void removeAll(){
		if(arrayList!=null){
			arrayList.clear();
			arrayList=null;
		}
		notifyDataSetChanged();
	}
	
	public void desotry(){
		
	}

}
