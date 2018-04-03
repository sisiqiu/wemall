package com.fulltl.wemall.modules.wx.core.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.fulltl.wemall.common.config.Global;

/**
 * 请求校验工具类
 */
public class SignUtil {
	// 与接口配置信息中的Token要一致
	private static String token = Global.getConfig("weixin.token");

	/**
	 * 通过token(已被确定)、timestamp、和nonce解析出signature(微信加密签名),
	 * 将解析出的signature与传入的signature比较，若相同，则返回true；否则返回false。
	 * 
	 * @param signature
	 *            微信加密签名
	 * @param timestamp
	 *            时间戳
	 * @param nonce
	 *            随机数
	 * @return boolean类型，若返回true，则微信验证成功；若返回false，则微信验证失败。
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) {
		// 从请求中（也就是微信服务器传过来的）拿到的token, timestamp, nonce
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;

		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			// 将字节数组转成字符串
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}

	// 将加密后的字节数组变成字符串
	private static String byteToStr(byte[] byteArray) {
		String strDigest = "";
		for (int i = 0; i < byteArray.length; i++) {
			strDigest += byteToHexStr(byteArray[i]);
		}
		return strDigest;
	}

	private static String byteToHexStr(byte mByte) {
		char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] tempArr = new char[2];
		tempArr[0] = Digit[(mByte >>> 4) & 0X0F];
		tempArr[1] = Digit[mByte & 0X0F];

		String s = new String(tempArr);
		return s;
	}

	// 用于字典排序
	public static void sort(String a[]) {
		for (int i = 0; i < a.length - 1; i++) {
			for (int j = i + 1; j < a.length; j++) {
				if (a[j].compareTo(a[i]) < 0) {
					String temp = a[i];
					a[i] = a[j];
					a[j] = temp;
				}
			}
		}
	}
}