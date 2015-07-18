package com.pwdgame.bean.http;


public class BaseModel {

	public int code;
	public String msg;
	
	/**是否是缓存数据*/
	public boolean isCache;	

	public Object reserve1;
	
	@Override
	public String toString() {
	
		return "BaseModel [code=" + code + ", msg=" + msg
				+ ", isCache=" + isCache + "]";
	}
}
