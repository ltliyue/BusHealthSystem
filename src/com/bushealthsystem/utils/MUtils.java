package com.bushealthsystem.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MUtils {

	/**
	 * 比较大小
	 * 
	 * @param smdate
	 * @param bdate
	 * @return
	 * @throws ParseException
	 */
	public static int daysBetween(String smdate, String bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(smdate));

		long time1 = cal.getTimeInMillis();
		cal.setTime(sdf.parse(bdate));
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	public static ArrayList<Integer> getDataFrom16(String data) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		int num = 2;
		int count = data.length() / num;
		for (int i = 0; i < count; i++) {
			arr.add(Integer.parseInt(data.substring(0, num), 16));
			data = data.substring(num);
		}
		return arr;
	}
}
