package com.framework.util;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Format{
	
	public static String getDateString(Date date, String format){
		if(date == null) return null;
		if(format == null || format.equals(""))format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	
	public static String getNumberString(double db, String format){
		if(format == null || format.equals(""))format = "######0.00";
		DecimalFormat df = new DecimalFormat(format);
		return df.format(db);
	}
	
	public static String getNumberString(int db, String format){
		if(format == null || format.equals(""))format = "######0.00";
		DecimalFormat df = new DecimalFormat(format);
		return df.format(db);
	}
	
	public static String getNumberString(float db, String format){
		if(format == null || format.equals(""))format = "######0.00";
		DecimalFormat df = new DecimalFormat(format);
		return df.format(db);
	}
	
	public static String random_6() {
		double a = Math.random() * 900000 + 100000;
		a = Math.ceil(a);
		String randomNum = "";
		randomNum += new Double(a).intValue();
		return randomNum;
	}
}