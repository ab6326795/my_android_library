package com.pwdgame.util;

import java.util.Random;

public class XYTextUtils {

	/**
	 * 随机生成len个汉字
	 * @param len
	 * @return
	 */
	public static String randomChineseString(int len){
		StringBuilder builder=new StringBuilder();
		for(int i=0;i<len;i++){
		   char ss= (char)(0x4e00+(int)(Math.random()*(0x9fa5-0x4e00+1)));
		   builder.append(ss);
		}
		return builder.toString();
	}
	
	/**
	 * 随机生成len个数字
	 * @param len
	 * @return
	 */
	public static String randomNumber(int len){
		StringBuilder builder=new StringBuilder();		
		for(int i=0;i<len;i++){
			builder.append((int)(Math.random()*10));
		}
		return builder.toString();
	}
}
