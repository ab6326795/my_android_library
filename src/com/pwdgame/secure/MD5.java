package com.pwdgame.secure;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class MD5 {
	private static byte[] hexBase = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
/*	
	
	public static String encryptToHex(String s) throws UnsupportedEncodingException {
		//拼接密钥
		s=strDefaultKey+s;
		if (s == null)
			return "";
		byte buff[] = s.getBytes("utf-8");
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(buff);
			byte result[] = messagedigest.digest();
			return byte2Hex(result);
		} catch (Exception e) {
			return "";
		}
	}*/
	
/*	public static void main(String[] args){
		//[domainId=1&time=1422502267594&username=1234561||dodonew_wx#Pay]
        try {
			System.out.println(encryptToHex("domainId=1&time=1422502267594&username=1234561||","dodonew_wx#Pay"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * MD5后以16进制返回
	 * @param s 需要加密的字符串
	 * @param key 密钥
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptToHex(String s,String key) throws UnsupportedEncodingException {
		//拼接密钥
		s=s+key;
		if (s == null)
			return "";
		byte buff[] = s.getBytes("utf-8");
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(buff);
			byte result[] = messagedigest.digest();
			return byte2Hex(result);
		} catch (Exception e) {
			return "";
		}
	}
	
	/**
	 * MD5后以String返回
	 * @param s
	 * @param key
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encryptToString(String s,String key) throws UnsupportedEncodingException {
		//拼接密钥
		s=key+s;
		if (s == null)
			return "";
		byte buff[] = s.getBytes("utf-8");
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			messagedigest.update(buff);
			byte result[] = messagedigest.digest();
			return new String(result);
		} catch (Exception e) {
			return "";
		}
	}
	public static String fileHash(String fileName) {
		try {
			byte buff[] = new byte[4096];
			MessageDigest messagedigest = MessageDigest.getInstance("MD5");
			FileInputStream fis = new FileInputStream(fileName);
			int len = 0;
			while ((len = fis.read(buff)) > 0) {
				messagedigest.update(buff, 0, len);
			}
			fis.close();
			byte result[] = messagedigest.digest();
			return byte2Hex(result);
		} catch (IOException ei) {
			System.out.println(MD5.class.getName() + ": " + ei.getMessage());
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	public static String byte2Hex(byte b[]) {
		if (b == null)
			return "";
		StringBuffer tmp = new StringBuffer();
		int len = b.length;
		for (int i = 0; i < len; i++) {
			tmp.append((char) hexBase[(b[i] & 0xF0) >> 4]);
			tmp.append((char) hexBase[b[i] & 0x0F]);
		}
		while (tmp.length() < 16)
			tmp.append("00");

		return tmp.toString();
	}
}
