package com.fulltl.wemall.modules.wx.core.utils;

import java.util.Map;

import com.google.common.collect.Maps;
import com.fulltl.wemall.common.security.Cryptos;
import com.fulltl.wemall.common.security.Digests;

/**
 * 用于生成微信支付的签名的工具类
 * @author Administrator
 *
 */
public class WeixinTradeSignature {

	/**
	 * 根据商户平台设置的密钥key使用MD5签名方式获取签名
	 * @param key key为商户平台设置的密钥key
	 * @param map 参数列表
	 * @return
	 */
	public static String getSignStrByMD5(String key, Map<String, String> map) {
		//注：key为商户平台设置的密钥key
		String stringSignTemp = getStrAfterSort(map) + "&key=" + key;
		//注：MD5签名方式
		String sign = byteToStr(Digests.md5(stringSignTemp.getBytes())).toUpperCase();
		return sign;
	}
	
	/**
	 * 根据商户平台设置的密钥key使用HMAC-SHA256签名方式获取签名
	 * @param key key为商户平台设置的密钥key
	 * @param map 参数列表
	 * @return
	 */
	public static String getSignStrByHMACSHA256(String key, Map<String, String> map) {
		//注：key为商户平台设置的密钥key
		String stringSignTemp = getStrAfterSort(map) + "&key=" + key;
		//注：HMAC-SHA256签名方式
		String sign = byteToStr(Cryptos.hmacSha256(stringSignTemp.getBytes(), key.getBytes())).toUpperCase();
		return sign;
	}
	
	/**
	 * 根据商户平台设置的密钥key使用MD5签名方式验证签名
	 * @param key key为商户平台设置的密钥key
	 * @param map 参数列表
	 * @return
	 */
	public static boolean checkSignStrByMD5(String key, Map<String, String> map) {
		String signFromWX = map.get("sign");
		map.remove("sign");
		//注：key为商户平台设置的密钥key
		String stringSignTemp = getStrAfterSort(map) + "&key=" + key;
		//注：MD5签名方式
		String sign = byteToStr(Digests.md5(stringSignTemp.getBytes())).toUpperCase();
		return sign.equals(signFromWX);
	}
	
	/**
	 * 根据商户平台设置的密钥key使用HMAC-SHA256签名方式验证签名
	 * @param key key为商户平台设置的密钥key
	 * @param map 参数列表
	 * @return
	 */
	public static boolean checkSignStrByHMACSHA256(String key, Map<String, String> map) {
		String signFromWX = map.get("sign");
		map.remove("sign");
		//注：key为商户平台设置的密钥key
		String stringSignTemp = getStrAfterSort(map) + "&key=" + key;
		//注：HMAC-SHA256签名方式
		String sign = byteToStr(Cryptos.hmacSha256(stringSignTemp.getBytes(), key.getBytes())).toUpperCase();
		return sign.equals(signFromWX);
	}
	
	/**
	 * 对参数按照key=value的格式，并按照参数名ASCII字典序排序，获取结果字符串。
	 * @param map 参数列表
	 * @return
	 */
	public static String getStrAfterSort(Map<String, String> map) {
		StringBuilder strBuilder = new StringBuilder();
		String[] keyArr = map.keySet().toArray(new String[]{});
		sort(keyArr);
		for(String key : keyArr) {
			strBuilder.append(key).append("=").append(map.get(key)).append("&");
		}
		if(strBuilder.length() > 0) {
			return strBuilder.substring(0, strBuilder.length()-1);
		} else {
			return strBuilder.toString();
		}
	}
	
	/**
	 * 用于字典排序
	 * @param a
	 */
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
	
	/**
	 * 将加密后的字节数组变成字符串
	 * @param byteArray
	 * @return
	 */
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
	
	public static void main(String[] args) {
		String key = "192006250b4c09247ec02edce69f6a2d";
		Map<String, String> map = Maps.newHashMap();
		map.put("device_info", "1000");
		map.put("mch_id", "10000100");
		map.put("body", "test");
		map.put("nonce_str", "ibuaiVcKdpRxkhJA");
		map.put("appid", "wxd930ea5d5a258f4f");
		String signMD5 = WeixinTradeSignature.getSignStrByMD5(key, map);
		System.err.println(signMD5);
		String signHMACSSHA256 = WeixinTradeSignature.getSignStrByHMACSHA256(key, map);
		System.err.println(signHMACSSHA256);
		map.put("sign", signMD5);
		System.err.println(WeixinTradeSignature.checkSignStrByMD5(key, map));
		map.put("sign", signHMACSSHA256);
		System.err.println(WeixinTradeSignature.checkSignStrByHMACSHA256(key, map));
	}
}
