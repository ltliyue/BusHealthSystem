package com.bushealthsystem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTools {
	public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

	private TimeTools() {
		throw new AssertionError();
	}

	public static String getStringDate() {
		Date currentTime = new Date();
		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	public static String getStringDate2() {
		Date currentTime = new Date();
		// SimpleDateFormat formatter = new
		// SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * long time to string
	 * 
	 * @param timeInMillis
	 * @param dateFormat
	 * @return
	 */
	public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
		return dateFormat.format(new Date(timeInMillis));
	}

	/**
	 * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @param timeInMillis
	 * @return
	 */
	public static String getTime(long timeInMillis) {
		return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static long getCurrentTimeInLong() {
		return System.currentTimeMillis();
	}

	/**
	 * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString() {
		return getTime(getCurrentTimeInLong());
	}

	/**
	 * get current time in milliseconds
	 * 
	 * @return
	 */
	public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
		return getTime(getCurrentTimeInLong(), dateFormat);

	}

	/**
	 * 拼接时间字符串
	 */
	public static String getStringTime(String inTime) {
		String outTime = "";
		outTime = inTime + " 08:00:00";
		return outTime;
	}

	/**
	 * 将字符串数据转化为毫秒数
	 */
	public static long getStringTimeInLong(String dateTime) {
		Calendar c = Calendar.getInstance();
		long timeInMillis = 0;
		try {
			c.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateTime));
			timeInMillis = c.getTimeInMillis();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		System.out.println("时间转化后的毫秒数为：" + c.getTimeInMillis());
		return timeInMillis;
	}

	/**
	 * 将字符串毫秒数转化为格式化的日期yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrentData(String getdata) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long now_long_1000 = Long.parseLong(getdata);
		return df.format(new Date(now_long_1000 * 1000));
	}
}
