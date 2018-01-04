package com.fulltl.wemall.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateFormatUtils;

/**
 * 时间处理工具类
 * @author ldk
 *
 */
public class TimeUtil {
	
	/**
	 * 日期转换成字符串,字符串类型："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateTimeToStr(Date date) {
		if(date == null) return null;
		return DateFormatUtils.format(date, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 日期转换成字符串,字符串类型："yyyy-MM-dd"
	 * 
	 * @param date
	 * @return str
	 */
	public static String DateToStr(Date date) {
		return DateFormatUtils.format(date, "yyyy-MM-dd");
	}

	/**
	 * 字符串转换成日期,字符串类型："yyyy-MM-dd HH:mm:ss"
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDateTime(String str) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换成日期,字符串类型："yyyy-MM-dd"
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate(String str) {
		if (str == null || str.trim().equals("")) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换成日期,字符串类型："HH:mm:ss"
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate3(String str) {
		if (str == null || str.trim().equals("")) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 字符串转换成日期,字符串类型："yyyy/MM/dd"
	 * 
	 * @param str
	 * @return date
	 */
	public static Date StrToDate2(String str) {
		if (str == null || str.trim().equals("")) {
			return null;
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 获取当月月初日期字符串。
	 * 
	 * @return 当月月初日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String beginMonth() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();

		calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), 1, 0, 0, 0);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取当月月末日期字符串。
	 * 
	 * @return 当月月初日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String endMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 根据传入的yyyy-MM字符串或yyyy-MM-dd字符串，获取当月开始日期
	 * 
	 * @param string
	 * @return
	 */
	public static String beginMonthByDate(String string) {
		String[] strs = string.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]) - 1, 1, 0, 0, 0);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 根据传入的yyyy-MM字符串或yyyy-MM-dd字符串，获取当月结束日期
	 * 
	 * @param string
	 * @return
	 */
	public static String endMonthByDate(String string) {
		String[] strs = string.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(strs[0]), Integer.parseInt(strs[1]) - 1, 1, 0, 0, 0);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取下月月初时间字符串。
	 * 
	 * @return 下月月初时间字符串。格式"yyyy-MM-dd HH:mm:ss"。
	 */
	public static String nextMonth() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		if (c.get(Calendar.MONTH) != 11) {
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, 1, 0, 0, 0);
		} else {
			calendar.set(c.get(Calendar.YEAR) + 1, 0, 1, 0, 0, 0);
		}
		return DateFormatUtils.format(calendar, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取当天0点时间字符串。
	 * 
	 * @return 当月月初时间字符串。格式"yyyy-MM-dd HH:mm:ss"。
	 */
	public static String beginDay() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();

		calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取上月月初时间字符串。
	 * 
	 * @return 上月月初时间字符串。格式"yyyy-MM-dd HH:mm:ss"。
	 */
	public static String lastMonth() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		if (c.get(Calendar.MONTH) != 0) {
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1, 1, 0, 0, 0);
		} else {
			calendar.set(c.get(Calendar.YEAR) - 1, 11, 1, 0, 0, 0);
		}

		return DateFormatUtils.format(calendar, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取前一天时间字符串。
	 * 
	 * @return 前一天时间字符串。格式"yyyy-MM-dd"。
	 */
	public static String lastDay() {
		Date d = new Date();
		Date lastDay = new Date(d.getTime() - 24 * 60 * 60 * 1000);
		return DateFormatUtils.format(lastDay, "yyyy-MM-dd");
	}

	/**
	 * 获取两月前的月初时间字符串
	 * 
	 * @return 两月前的月初时间字符串。格式"yyyy-MM-dd HH:mm:ss"。
	 */
	public static String twoMonthAgo() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		if (c.get(Calendar.MONTH) == 0) {
			calendar.set(c.get(Calendar.YEAR) - 1, 10, 1, 0, 0, 0);
		} else if (c.get(Calendar.MONTH) == 1) {
			calendar.set(c.get(Calendar.YEAR) - 1, 11, 1, 0, 0, 0);
		} else {
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 2, 1, 0, 0, 0);
		}

		return DateFormatUtils.format(calendar, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 获取本月时间字符串
	 * 
	 * @return 本月时间字符串。格式"yyyyMM"。
	 */
	public static String curMonthStr() {
		return DateFormatUtils.format(new Date(), "yyyyMM");
	}

	/**
	 * 获取上月时间字符串
	 * 
	 * @return 上月时间字符串。格式"yyyyMM"。
	 */
	public static String lastMonthStr() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		if (c.get(Calendar.MONTH) == 0) {
			calendar.set(c.get(Calendar.YEAR) - 1, 11, 1, 0, 0, 0);
		} else {
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1, 1, 0, 0, 0);
		}
		return DateFormatUtils.format(calendar, "yyyyMM");
	}

	/**
	 * 获取本周开始日期字符串
	 * 
	 * @return 本周开始日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String beginWeek() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			calendar.add(Calendar.DAY_OF_WEEK, -6);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取本周结束日期字符串
	 * 
	 * @return 本周结束日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String endWeek() {
		Calendar calendar = Calendar.getInstance();
		if (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.add(Calendar.DAY_OF_WEEK, 6);
		}
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 根据传入日期获取传入日期所在周的开始日期字符串
	 * 
	 * @param date
	 *            传入日期
	 * @return 传入日期所在周的开始日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String beginWeekBy(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			calendar.add(Calendar.DAY_OF_WEEK, -6);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取传入日期所在周的结束日期字符串
	 * 
	 * @param date
	 *            传入日期
	 * @return 传入日期所在周的结束日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String endWeekBy(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.add(Calendar.DAY_OF_WEEK, 6);
		}
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 根据传入日期获取传入日期所在周的开始日期字符串
	 * 
	 * @param date
	 *            传入日期
	 * @return 传入日期所在周的开始日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String beginWeekBy(String date) {
		SimpleDateFormat sdf = null;
		if (date.contains("/")) {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			return beginWeekBy(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取传入日期所在周的结束日期字符串
	 * 
	 * @param date
	 *            传入日期
	 * @return 传入日期所在周的结束日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String endWeekBy(String date) {
		SimpleDateFormat sdf = null;
		if (date.contains("/")) {
			sdf = new SimpleDateFormat("yyyy/MM/dd");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			return endWeekBy(sdf.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取上周开始日期字符串
	 * 
	 * @return 上周开始日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String beginLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(calendar.getTimeInMillis() - 7 * 24 * 60 * 60 * 1000);
		if (calendar.get(Calendar.DAY_OF_WEEK) == 1) {
			calendar.add(Calendar.DAY_OF_WEEK, -6);
		}
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取上周结束日期字符串
	 * 
	 * @return 上周结束日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String endLastWeek() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(calendar.getTimeInMillis() - 7 * 24 * 60 * 60 * 1000);
		if (calendar.get(Calendar.DAY_OF_WEEK) != 1) {
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
			calendar.add(Calendar.DAY_OF_WEEK, 6);
		}
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取上月开始日期字符串
	 * 
	 * @return 上月开始日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String beginLastMonth() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		if (c.get(Calendar.MONTH) != 0) {
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1, 1, 0, 0, 0);
		} else {
			calendar.set(c.get(Calendar.YEAR) - 1, 11, 1, 0, 0, 0);
		}
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获取上月束日期字符串
	 * 
	 * @return 上月结束日期字符串。格式"yyyy-MM-dd"。
	 */
	public static String endLastMonth() {
		Calendar c = Calendar.getInstance();
		Calendar calendar = Calendar.getInstance();
		if (c.get(Calendar.MONTH) != 0) {
			calendar.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH) - 1, 1, 0, 0, 0);
		} else {
			calendar.set(c.get(Calendar.YEAR) - 1, 11, 1, 0, 0, 0);
		}
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获得明天的字符串，字符串格式"yyyy-MM-dd"
	 * 
	 * @return
	 */
	public static String nextDay() {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + 1);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获得传入日期第二天的字符串，字符串格式"yyyy-MM-dd"
	 * 
	 * @return
	 */
	public static String nextDay(Date date) {
		// TODO Auto-generated method stub
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int day = calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE, day + 1);
		return DateFormatUtils.format(calendar, "yyyy-MM-dd");
	}

	/**
	 * 获得传入日期是周几，字符串格式"yyyy-MM-dd"
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static int getzjDay(String pTime) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		c.setTime(format.parse(pTime));
		int dayForWeek = 0;
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			dayForWeek = 7;
		} else {
			dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
		}
		return dayForWeek;
	}
	
	/**
	 * 获得传入日期是周几，字符串格式"yyyy-MM-dd"
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static String getZjDate(String substring) throws ParseException {
		String zjDate = "";
		int c =  TimeUtil.getzjDay(substring);
		switch (c){
			case 1 : zjDate = "周一";break;
			case 2 : zjDate = "周二";break;
			case 3 : zjDate = "周三";break;
			case 4 : zjDate = "周四";break;
			case 5 : zjDate = "周五";break;
			case 6 : zjDate = "周六";break;
			case 7 : zjDate = "周日";break;
			default:break;
		}
		return zjDate;
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(beginMonthByDate("2017-2"));
		System.out.println(endMonthByDate("2017-03"));
		System.out.println(getZjDate("2017-7-30"));
		System.out.println(secondToStr(111111111111l));
	}
	/**
	 * 计算耗时多久
	 * @param millisecond
	 * @return
	 */
	public static String secondToStr(Long millisecond){
		String str = "";
		long day = millisecond /  86400000;
		long hour = (millisecond % 86400000) / 3600000 ;
		long minute = (millisecond %  86400000 % 3600000) / 60000;
		long second = (millisecond %  86400000 % 3600000 )% 60000;
		if(day > 0){
			str = String.valueOf(day) + "天";
		}
		if(hour > 0){
			str += String.valueOf(hour) + "小时";
		}
		if(minute > 0){
			str += String.valueOf(minute) + "分钟";
		}
		if(second > 0){
			str += String.valueOf(second).substring(0,2) + "秒";
		}
		return str;
	}
}
