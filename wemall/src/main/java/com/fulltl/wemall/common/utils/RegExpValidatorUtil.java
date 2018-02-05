package com.fulltl.wemall.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证工具类
 *
 */
public class RegExpValidatorUtil {

	/**
	 * 属性文件加载对象
	 */
	private static PropertiesLoader loader = new PropertiesLoader("regularExpression.properties");

	/**
	 * 正则表达式：验证手机号
	 */
	public static final String REGEX_MOBILE = "^1[3|4|5|7|8][0-9]{9}$";
	
	/**
	 * 正则表达式：验证手机号,可为空
	 */
	public static final String REGEX_NULL_OR_MOBILE = "(^$)|(" + REGEX_MOBILE + ")";

	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 正则表达式：验证身份证
	 */
	public static final String REGEX_ID_CARD = "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)|"
												+ "(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$)";
	
	/**
	 * 正则表达式：验证身份证,可为空
	 */
	public static final String REGEX_NULL_OR_ID_CARD = "(^$)|(" + REGEX_ID_CARD + ")";

	/**
	 * 正则表达式：验证URL
	 */
	public static final String REGEX_URL = "http(s)?://([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?";

	/**
	 * 正则表达式：验证IP地址
	 */
	public static final String REGEX_IP_ADDR = "(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)";

	/**
	 * 正则表达式：验证经度。经度整数部分为0-180小数部分为0到11位
	 */
	public static final String REGEX_LNG = "^(((\\d|[1-9]\\d|1[1-7]\\d|0)\\.\\d{0,11})|(\\d|[1-9]\\d|1[1-7]\\d|0{1,3})|180\\.0{0,4}|180)$";

	/**
	 * 正则表达式：验证纬度。纬度整数部分为0-90小数部分为0到11位
	 */
	public static final String REGEX_LAT = "^([0-8]?\\d{1}\\.\\d{0,11}|90\\.0{0,4}|[0-8]?\\d{1}|90)$";
	
	/**
	 * 正则表达式：验证整数或小数的正则表达式。
	 */
	public static final String REGEX_DECIMAL = "^[0-9]+([.]{1}[0-9]+){0,1}$";
	
	/**
	 * 正则表达式：验证整数或小数的正则表达式,可为空
	 */
	public static final String REGEX_NULL_OR_DECIMAL = "(^$)|(" + REGEX_DECIMAL + ")";

	/**
	 * 正则表达式：验证邮箱的正则表达式,可为空
	 */
	public static final String REGEX_NULL_OR_EMAIL =  "(^$)|(" + REGEX_EMAIL + ")";
	
	private static int cn_min = (int) "一".charAt(0); // \u4e00
	private static int cn_max = (int) "龥".charAt(0); // \u9fa5

	/**
	 * 校验手机号
	 * 
	 * @param mobile
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isMobile(String mobile) {
		String regex_mobile = CacheUtils.get("regex_mobile") == null ? "" : CacheUtils.get("regex_mobile").toString();
		if (StringUtils.isBlank(regex_mobile)) {
			// 缓存中没有，取配置文件
			regex_mobile = loader.getProperty("regex_mobile");
			if (StringUtils.isBlank(regex_mobile)) {
				// 配置文件中也没有
				regex_mobile = REGEX_MOBILE;
			}
			CacheUtils.put("regex_mobile", regex_mobile);
		}
		return Pattern.matches(regex_mobile, mobile);
	}

	/**
	 * 校验邮箱
	 * 
	 * @param email
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	/**
	 * 校验身份证
	 * 
	 * @param idCard
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isIDCard(String idCard) {
		return Pattern.matches(REGEX_ID_CARD, idCard);
	}

	/**
	 * 校验URL
	 * 
	 * @param url
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isUrl(String url) {
		return Pattern.matches(REGEX_URL, url);
	}

	/**
	 * 校验IP地址
	 * 
	 * @param ipAddr
	 * @return
	 */
	public static boolean isIPAddr(String ipAddr) {
		return Pattern.matches(REGEX_IP_ADDR, ipAddr);
	}
	
	/**
	 * 校验经度,经度整数部分为0-180小数部分为0到8位
	 * @param ipAddr
	 * @return
	 */
	public static boolean isLNG(String lng) {
		return Pattern.matches(REGEX_LNG, lng);
	}
	
	/**
	 * 校验纬度,纬度整数部分为0-90小数部分为0到8位
	 * @param ipAddr
	 * @return
	 */
	public static boolean isLAT(String lat) {
		return Pattern.matches(REGEX_LAT, lat);
	}
	
	/**
	 * 校验身高或体重
	 * @param ipAddr
	 * @return
	 */
	public static boolean isDECIMAL(String numStr) {
		return Pattern.matches(REGEX_DECIMAL, numStr);
	}

	/**
	 * 判断是否是中文
	 * 
	 * @param c
	 * @return
	 */
	private static final boolean isChinese(char c) {
		int char_int = (int) c;
		if (char_int < cn_min || char_int > cn_max) {
			return false;
		} else {
			return true;
		}
	}

	// 完整的判断中文汉字和符号
	public static boolean hasChinese(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否为乱码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isMessyCode(String str) {
		return !java.nio.charset.Charset.forName("GBK").newEncoder().canEncode(str);
	}

	/**
     * 判断是否含有特殊字符
     *
     * @param str
     * @return true为包含，false为不包含
     */
    public static boolean isSpecialChar(String str) {
        String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.find();
    }
	
	public static void main(String[] args) {
		//System.err.println(Pattern.matches(REGEX_DECIMAL, "320723199510100"));
		//System.err.println(isSpecialChar("32072319\n9510100"));
	}
}