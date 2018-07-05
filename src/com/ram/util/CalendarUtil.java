package com.ram.util;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarUtil {

	public final static int[] DAYSINMONTH = { 31, 28, 31, 30, 31, 30, 31, 31,
			30, 31, 30, 31 };

	public String[] monthNames = null;

	protected DateFormatSymbols symbols = null;

	protected Calendar cal = Calendar.getInstance();
	

	protected int today = cal.get(Calendar.DAY_OF_MONTH);

	protected int month = cal.get(Calendar.MONTH);

	protected int curMonth=cal.get(Calendar.MONTH) + 1;
	
	protected int year = cal.get(Calendar.YEAR);

	public CalendarUtil() {
		symbols = new DateFormatSymbols();
		monthNames = symbols.getMonths();
	
	}

	public void setMonth(int monthArg) {
		this.month = monthArg;
	}

	public void setYear(int yearArg) {
		this.year = yearArg;
	}

	public int getYear(){
		return year;
	}
	public void setMonth(String monthArg) {
		try {
			int month = Integer.parseInt(monthArg);
			setMonth(month);
		} catch (Exception exc) {
		}
	}

	public void setYear(String yearArg) {
		try {
			int year = Integer.parseInt(yearArg);
			setYear(year);
		} catch (Exception exc) {
		}
	}





	/**
	 * 得到当前日期
	 * @return
	 */
	public String getCurrentDate(){
		String tmp=this.year + "-" + this.curMonth + "-" + this.today;
		return tmp;
	}
	
	public String getCurrentMonth_Chinese(){
		String tmp=this.year + "年" + String.valueOf(this.month +1) + "月";
		return tmp;
	}
	

	
	
	
	/**
	 * 得到指定today的当前日期
	 * @param today
	 * @return
	 */
	public String getThisDate(int today){
		int tMonth=this.month;
		String tmpMonth="";
		tMonth++;
		if(tMonth<10){
			tmpMonth="0" +tMonth;
		}else{
			tmpMonth=""+tMonth;
		}
		String tmpDay="";
		if(today<10){
			tmpDay="0" + today;
		}else{
			tmpDay=""+today;
		}
		
		String tmp=this.year + "-" + tmpMonth + "-" + tmpDay;
		return tmp;
				
		
	}
	public int getStartCell() {
		Calendar beginOfMonth = Calendar.getInstance();
		beginOfMonth.set(year, month, 0);
		return beginOfMonth.get(Calendar.DAY_OF_WEEK);
	}

	public int getEndCell() {
		cal.set(year, month, 1);
		int endCell = DAYSINMONTH[month] + getStartCell() - 1;
		if (month == Calendar.FEBRUARY
				&& ((GregorianCalendar) cal).isLeapYear(year)) {
			endCell++;
		}
		return endCell;
	}

	public void update() {
		cal.set(this.year, this.month, 1);
	}

	public String getDayName(int day, boolean longFormat) {
		if (longFormat)
			return symbols.getWeekdays()[day];
		return symbols.getShortWeekdays()[day] + "日";
	}
	
	public String getWeekDayName(int day) {
		String weekDayName="";
		switch (day){
		case 1:weekDayName= "日";break;
		case 2:weekDayName= "一";break;
		case 3:weekDayName= "二";break;
		case 4:weekDayName= "三";break;
		case 5:weekDayName= "四";break;
		case 6:weekDayName= "五";break;
		case 7:weekDayName= "六";break;
		}
		return weekDayName;
		
	}
	
	public String getMonthName() {
		return monthNames[cal.get(Calendar.MONTH)];
	}	
	public String getMonthName(int month) {
		return monthNames[cal.get(month)];
	}		
	public int getToday() {
		return today;
	}	
	
	public int getMonth(){
		return month;
	}
}
