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

    public static final String STARTDATE = "20140509";

	public static Date getFromDate(String[] args) throws ParseException {
		return getFirstDayofMonth(args[0]);
	}

	public static Date getEndDate(String[] args) throws ParseException {
		return lastDayOfMonth(args[0]);
	}

	public static Date parseDate(String dateString) throws ParseException {
        return parseDate(dateString, "yyyy/MM/dd");

    }

    public static Date parseDate(String dateString, String format) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
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
		cal.set(Calendar.DAY_OF_MONTH, 20);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getZSEndDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 20);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static Date getJTStartDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 2);
		cal.set(Calendar.DAY_OF_MONTH, 7);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}
	
	public static Date getJTEndDate(String month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Integer.valueOf(month) - 1);
		cal.set(Calendar.DAY_OF_MONTH, 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
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

    public static long getTotalStatisticsDays() throws Exception{
        long start = parseDate(STARTDATE, "yyyyMMdd").getTime();
        long current = System.currentTimeMillis();
       return ( current - start + 1000000)/(3600*24*1000);
    }

    public static void main(String args[]) throws Exception{
        System.out.println(getTotalStatisticsDays());
    }

}
