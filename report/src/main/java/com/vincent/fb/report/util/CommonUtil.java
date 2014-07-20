package com.vincent.fb.report.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class CommonUtil {
	public static Date getFromDate(String[] args) throws ParseException {
		return getFirstDayofMonth(args[0]);
	}

	public static Date getEndDate(String[] args) throws ParseException {
		return lastDayOfMonth(args[0]);
	}

	public static Date parseDate(String dateString) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			return sf.parse(dateString);
		} catch (ParseException e) {
			System.out.println("日期格式错误" + dateString);
			throw e;
		}
	}
	
	public static String formatDate(Date date) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		try {
			return sf.format(date);
		} catch (Exception e) {
			throw e;
		}
	}

	public static Date lastDayOfMonth(String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.roll(Calendar.DAY_OF_MONTH, -1);
		return cal.getTime();
	}

	public static Date getFirstDayofMonth(String month) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}
	
	public static Date getZSStartDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 2);
		cal.set(Calendar.DAY_OF_MONTH, 21);
		return cal.getTime();
	}
	
	public static Date getZSEndDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 20);
		return cal.getTime();
	}
	
	public static Date getJTStartDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 2);
		cal.set(Calendar.DAY_OF_MONTH, 8);
		return cal.getTime();
	}
	
	public static Date getJTEndDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 7);
		return cal.getTime();
	}
	
	public static String getValue(String key) throws IOException{
		InputStream in = new BufferedInputStream(new FileInputStream("D:/fbconfig/config.properties"));
		Properties p = new Properties();
		p.load(in);
		return new String(p.getProperty(key).getBytes("ISO-8859-1"),"gbk");
	}
	
	public static boolean compareMonth(Date date, String month){
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		int i = instance.get(Calendar.MONTH);
		int year = instance.get(Calendar.YEAR);
		if(year != 2014){
			return false;
		}
		return i == (Integer.valueOf(month) + 1) || i == (Integer.valueOf(month));
	}
	

}
