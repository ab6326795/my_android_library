package com.pwdgame.secure;
/**
 *****************************************
 *********深圳市希之光科技有限公司 *********
 *****************************************
 */
// package com.weixin.dodonew.util;

import java.net.URL;
import java.net.URLEncoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @description
 * @author li.wang
 * @date 2014-5-28下午6:11:14
 */
public class Hex2Byte {
	private static String strDefaultKey = "@ddgbg./";
	
	public static String byte2hex(byte[] b) { // 二进制转字符串
		String hs = "";
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs = hs + "0" + stmp;
			else
				hs = hs + stmp;
		}
		return hs;
	}

	public static byte[] hex2byte(String str) { // 字符串转二进制
		if (str == null)
			return null;
		str = str.trim();
		int len = str.length();
		if (len == 0 || len % 2 == 1)
			return null;

		byte[] b = new byte[len / 2];
		try {
			for (int i = 0; i < str.length(); i += 2) {
				b[i / 2] = (byte) Integer.decode("0x" + str.substring(i, i + 2)).intValue();
			}
			return b;
		} catch (Exception e) {
			return null;
		}
	}

	public static byte[] encrypt(byte[] src, byte[] key) { // 加密
		if ((src == null) || (key == null))
			return null;
		try {
			Cipher cipher = Cipher.getInstance("DES");
			// cipher.init(1, new SecretKeySpec(key, "DES"));

			IvParameterSpec zeroIv = new IvParameterSpec(iv);
			cipher.init(1, new SecretKeySpec(key, "DES"),zeroIv);
			return cipher.doFinal(src);
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 如果需要不同平台比如java、ios得到一致的加密结果，必须指定一致的iv序列
	 * 如果不指定iv，各平台将调用自己的默认设置，最终导致各平台的加密结果不一致
	 */
	private static byte[] iv = { 1, 2, 3, 4, 5, 6, 7, 8 };
	/**
	 * 全平台通用des加密函数，实现java、ios平台得到一致密文
	 * 闫勇
	 * 2014-08-13
	 */
    public static String encryptDES(String encryptString)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(strDefaultKey.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes());
        return URLEncoder.encode(Base64.encode(encryptedData),"utf-8");
    }

	public static byte[] decrypt(byte[] src, byte[] key) {  // 解密
		if ((src == null) || (key == null))
			return null;
		try {
			Cipher cipher = Cipher.getInstance("DES");
			cipher.init(2, new SecretKeySpec(key, "DES"));
			return cipher.doFinal(src);
		} catch (Exception e) {
		}
		return null;
	}

	public static void main(String[] args) {
		String des = byte2hex(encrypt("1234567".getBytes(), "12345678".getBytes()));
		System.out.println(des);
		System.out.println(new String(decrypt(hex2byte(des), "12345678".getBytes())));
	}

}
