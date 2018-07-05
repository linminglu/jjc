package com.framework.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.apps.Constants;

/**
 * DateTime Util.
 * 
 * @version 0.3.5
 * @author Xiaobo Liu
 */
public class DateTimeUtil {
	static Calendar cld;

	/**
	 * Return current datetime string.
	 * 
	 * @return current datetime, pattern: "yyyy-MM-dd HH:mm:ss".
	 */
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * Return current datetime string.
	 * 
	 * @param pattern
	 *            format pattern
	 * @return current datetime
	 */
	public static String getDateTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * 取当前日期，返回日期类型
	 * 
	 * @return
	 */
	public static Date getNowSQLDate() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentDate);
		// ParsePosition pos = new ParsePosition(0);
		Date currentTime = null;
		try {
			currentTime = formatter.parse(dateString);
		} catch (Exception e) {
		}
		return currentTime;
	}

	public static java.util.Date getJavaUtilDateNow() {
		return getNowSQLDate();
	}
	
	public static java.util.Date getNow() {
		return getNowSQLDate();
	}

	public static java.util.Date getYMDHMNow() {
		Date currentDate = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String dateString = formatter.format(currentDate);
		Date currentTime = null;
		try {
			currentTime = formatter.parse(dateString);
		} catch (Exception e) {
		}
		return currentTime;
	}

	public static java.util.Date getYYYYMMDD_Today() {
		return getNowDate();
	}

	public static java.sql.Date getJavaSQLDateNow() {
		return new java.sql.Date(System.currentTimeMillis());
	}

	/**
	 * 取当前日期，返回日期类型
	 * 
	 * @return
	 */

	/**
	 * 获得YYYY-MM-DD格式的日期
	 * 
	 * 传入的参数是java.util.Date格式 此方法有问题，返回的格式不是YYYY-MM-DD类型的日期数据
	 */
	public static Date getDateFormart_YYYY_MM_DD(Date date) {
		// /---return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = formatter.format(date);
		Date currentTime = null;
		try {
			currentTime = formatter.parse(dateString);
		} catch (Exception e) {
		}
		return currentTime;
	}

	public static String DoFormatDate(java.util.Date dt_in,
			boolean ifShowTimePart) {
		if (ifShowTimePart)
			return (new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")).format(dt_in);
		else
			return (new SimpleDateFormat("yyyy-MM-dd")).format(dt_in);
	}

	/**
	 * 得到YYYY-MM-DD格式的当前日期
	 * 
	 * @return
	 */
	public static Date getNowDate() {
		Date currentDate = new Date();
		return getDateFormart_YYYY_MM_DD(currentDate);
	}

	/**
	 * Return short format datetime string.
	 * 
	 * @param date
	 *            java.util.Date
	 * @return short format datetime
	 */
	public static String shortFmt(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
		return sdf.format(date);
	}

	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *            datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
	 * @return java.util.Date
	 */
	public static Date parse(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
			date = new Date(System.currentTimeMillis());
		}
		return date;
	}
	
	public static Date parse(String param,String format) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
			date = new Date(System.currentTimeMillis());
		}
		return date;
	}
	
	/**
	 * 得到两个日期相差的小时数
	 */
	public static int subTime(String time1, String time2) {
		int result = 0;
		// "1979-02-20 08:00:00.000"
		Timestamp d1 = Timestamp.valueOf(time1);
		Timestamp d2 = Timestamp.valueOf(time2);
		result = (int) ((d2.getTime() - d1.getTime()) / 1000 / 60 / 60);
		return result;
	}

	/**
	 * 取得本月第一天的日期
	 * 
	 * @param DQRQ
	 * @return
	 */
	public static String getCurMonthFirstDay(String DQRQ) {
		String Year = DQRQ.substring(0, 4);
		String Month = DQRQ.substring(5, 7);
		String Day = "01";
		String strReturn = Year + "-" + Month + "-" + Day;
		return strReturn;
	}

	/**
	 * 取得本月第一天的日期
	 * 
	 * @param DQRQ
	 * @return
	 */

	public static String getMonthFirstDay() {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Calendar cpcalendar = (Calendar) calendar.clone();
		cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
		return df.format(new Date(cpcalendar.getTimeInMillis()));
	}

	/**
	 * 取得本月最后一天的日期
	 * 
	 * @param DQRQ
	 * @return
	 */
	public static String getMonthEndDay() {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Calendar cpcalendar1 = (Calendar) calendar.clone();
		cpcalendar1.set(Calendar.DAY_OF_MONTH, 1);
		cpcalendar1.add(Calendar.MONTH, 1);
		cpcalendar1.add(Calendar.DATE, -1);

		return df.format(new Date(cpcalendar1.getTimeInMillis()));
	}

	/**
	 * 取得当前月的最大天数
	 * 
	 * 
	 * @return
	 */
	public static int getDaysOfMonth() {
		return getDaysOfMonth(getRightYear(), getRightMonth());
	}

	/**
	 * 取得一个月的最大天数
	 * 
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfMonth(int year, int month) {
		return (int) ((toLongTime(month == 12 ? (year + 1) : year,
				month == 12 ? 1 : (month + 1), 1) - toLongTime(year, month, 1)) / (24 * 60 * 60 * 1000));
	}

	/**
	 * 取得下一个月的最大天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfNextMonth(int year, int month) {
		year = month == 12 ? year + 1 : year;
		month = month == 12 ? 1 : month + 1;
		return getDaysOfMonth(year, month);
	}

	/**
	 * 取得上个月的最大天数
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static int getDaysOfProMonth(int year, int month) {
		year = month == 1 ? year - 1 : year;
		month = month == 1 ? 12 : month - 1;
		return getDaysOfMonth(year, month);
	}

	/**
	 * 取上月第一天
	 * 
	 * 
	 * @param dqrq
	 *            Description of Parameter
	 * @return String
	 * @since 2003-0416
	 */
	public static String getFirstDayOfPreMonth(String dqrq) {
		String strDQRQ = dqrq;
		String strYear = "";
		String strMonth = "";
		String strDay = "";
		if (strDQRQ == null) {
			return "";
		}
		if (strDQRQ.length() == 8) {
			strYear = strDQRQ.substring(0, 4);
			strMonth = strDQRQ.substring(4, 6);
			strDay = strDQRQ.substring(6, 8);
		}
		if (strDQRQ.length() == 10) {
			strYear = strDQRQ.substring(0, 4);
			strMonth = strDQRQ.substring(5, 7);
			strDay = strDQRQ.substring(8, 10);
		}

		int iMonth = DataTypeUtil.toInt(strMonth);
		int iYear = DataTypeUtil.toInt(strYear);
		if (iMonth == 1) {
			iYear = iYear - 1;
			iMonth = 12;
		} else if (iMonth > 1) {
			iMonth = iMonth - 1;
		} else {
			return "";
		}
		if (iMonth < 10) {
			strMonth = "0" + iMonth;
		} else {
			strMonth = "" + iMonth;
		}
		strDay = "01";
		if (strDQRQ.length() == 8) {
			return iYear + strMonth + strDay;
		} else if (strDQRQ.length() == 10) {
			return iYear + "-" + strMonth + "-" + strDay;
		} else {
			return "";
		}
	}

	/**
	 * 取得当前的年 以整数形式返回， 例如：1999
	 * 
	 * @return 返回当前年
	 */
	public static int getRightYear() {
		return getRightYear((Date) null);
	}

	/**
	 * 根据Date对象取得 年份 整数形式
	 * 
	 * @param date
	 *            java.util.Date 对象
	 * @return 返回 java.util.Date 的年份
	 */
	public static int getRightYear(Date date) {
		Calendar rightYear = Calendar.getInstance();
		if (date != null) {
			rightYear.setTime(date);
		}
		return rightYear.get(Calendar.YEAR);
	}

	/**
	 * Gets the RightMonth attribute of the DateUtil class
	 * 
	 * @return The RightMonth value
	 */
	public static int getRightMonth() {
		return getRightMonth((Date) null);
	}

	/**
	 * Gets the RightMonth attribute of the DateUtil class
	 * 
	 * @param date
	 *            Description of Parameter
	 * @return The RightMonth value
	 */
	public static int getRightMonth(Date date) {
		Calendar rightMonth = Calendar.getInstance();
		if (date != null) {
			rightMonth.setTime(date);
		}
		return rightMonth.get(Calendar.MONTH) + 1;
	}

	/**
	 * Gets the RightDay attribute of the DateUtil class
	 * 
	 * @param date
	 *            Description of Parameter
	 * @return The RightMonth value
	 */
	public static int getRightDay(Date date) {
		Calendar rightMonth = Calendar.getInstance();
		if (date != null) {
			rightMonth.setTime(date);
		}
		return rightMonth.get(Calendar.DATE);
	}
	
	/**
	 * 从给定的 year,mongth,day 得到时间的long值表示
	 * 
	 * milliseconds after January 1, 1970 00:00:00 GMT).
	 * 
	 * @param year
	 *            年
	 * 
	 * @param month
	 *            月
	 * 
	 * @param day
	 *            日
	 * 
	 * @return 给定的 year,mongth,day 得到时间的long值表示
	 */
	public static long toLongTime(int year, int month, int day) {
		return toDate(year, month, day).getTime();
	}

	/**
	 * 从年月日得到一个Date对象
	 * 
	 * @param year
	 *            年
	 * 
	 * @param month
	 *            月
	 * 
	 * @param day
	 *            日
	 * 
	 * @return 得到的Date对象
	 */
	public static java.util.Date toDate(int year, int month, int day) {
		if (cld == null) {
			cld = new GregorianCalendar();
		}
		cld.clear();
		cld.set(Calendar.YEAR, year);
		cld.set(Calendar.MONTH, month - 1);
		cld.set(Calendar.DAY_OF_MONTH, day);
		return cld.getTime();
		// .getTime();
	}

	/**
	 * 得到一个随机的字符串是(从时间取得)
	 * 
	 * @return String
	 */
	public static String GetUniqueID() {
		SimpleDateFormat dateform = new SimpleDateFormat("yyyyMMddHHmmhhSSS");// notice
																				// here

		GregorianCalendar calendar = new GregorianCalendar();
		String s = dateform.format(calendar.getTime());
		// String str = new Double(Math.random()).toString();
		// s = s + str.substring(2, str.length());
		return s;
	}

	public static Date getDateTimeOfDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return new Date(cal.getTimeInMillis());
	}
	
	public static void main(String[] args) {
		Date d = DateTimeUtil.getDateTimeOfDays(DateTimeUtil.parse(DateTimeUtil.DateToString(new Date())+" "+Constants.GAME_XJPICK11_BEGIN_TIME),1);
		System.out.println(DateTimeUtil.DateToStringAll(d));
	}

	public static Date StringToDate(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}

	public static Date StringToDate(String param, String format) {
		Date date = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			date = sdf.parse(param);
		} catch (ParseException ex) {
			return null;
		}
		return date;
	}

	/**
	 * String to Date 时间格式转换
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDateMul(String strDate) {
		if (strDate == null || strDate.length() < 6) {
			throw new IllegalArgumentException("illeage date format");
		}
		String fmt = "yyyy-MM-dd HH:mm:ss";
		if (strDate.length() == 10) {
			fmt = "yyyy-MM-dd";
		} else if (strDate.length() == 8) {
			if (strDate.indexOf("-") > 0) {
				fmt = "yy-MM-dd";
			} else {
				fmt = "yyyyMMdd";
			}
		} else if (strDate.length() == 6) {
			fmt = "yyMMdd";
		}
		SimpleDateFormat formatter = new SimpleDateFormat(fmt);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	public static String DateToString(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String TOStringNull(String cont) {
		if (ParamUtils.chkString(cont))
			return cont;
		return "";
	}

	public static String DateToStringAll(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String DateToStringMMddHHmm(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
		return sdf.format(date);
	}

	public static String DateToStringHHMM(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		return sdf.format(date);
	}

	public static String DateToStringHHMMSS(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		return sdf.format(date);
	}

	public static String DateToStringHHmmss(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		return sdf.format(date);
	}

	public static java.util.Date getCurrentDate() {
		return new java.util.Date(System.currentTimeMillis());
	}

	public static String getYyyyMMddStr(java.util.Date dt_in) {
		return (new SimpleDateFormat("yyyyMMdd")).format(dt_in);
	}

	public static String getYyMMddStr(java.util.Date dt_in) {
		return (new SimpleDateFormat("yyMMdd")).format(dt_in);
	}
	
	public static String getYyyy(java.util.Date dt_in) {
		return (new SimpleDateFormat("yyyy")).format(dt_in);
	}
	public static String getYy(java.util.Date dt_in) {
		return (new SimpleDateFormat("yy")).format(dt_in);
	}

	public static String getMMDD_str(Date date) {
		return (new SimpleDateFormat("MM/dd")).format(date);
	}

	public static String getYyyyMMStr(java.util.Date dt_in) {
		return (new SimpleDateFormat("yyyyMM")).format(dt_in);
	}

	public static Date getCurrentDayStart() {
		return parse(DateToString(getNowDate()) + " 00:00:00");
	}

	public static Date getCurrentDayend() {
		return parse(DateToString(getNowDate()) + " 23:59:59");
	}

	/**
	 * 使用用户格式格式化日期
	 * 
	 * @param date
	 *            日期
	 * @param pattern
	 *            日期格式
	 * @author LLB
	 */
	public static String format(Date date, String pattern) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat(pattern);
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	public static String format(Date date) {
		String returnValue = "";
		if (date != null) {
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	public static String shortFsm(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(date);
	}

	/**
	 * 计算两个时间相差的秒数
	 * 
	 * @param startTime
	 * @param endTime
	 * @param format
	 *            时间格式
	 * @return 秒
	 * @author zang
	 */
	public static long dateDiffToSec(String startTime, String endTime,
			String format) {
		// 按照传入的格式生成一个simpledateformate对象
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long sec = 0;
		try {
			// 获得两个时间的毫秒时间差异
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			sec = diff / ns;// 计算差多少秒
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sec;
	}

	public static long dateDiffToSec(Date startTime, Date endTime) {
		long ns = 1000;// 一秒钟的毫秒数
		long diff;
		long sec = 0;
		try {
			// 获得两个时间的毫秒时间差异
			diff = endTime.getTime() - startTime.getTime();
			sec = diff / ns;// 计算差多少秒
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sec;
	}

	
	/**
	 * 计算时间相差秒数的最后时间
	 * @param date
	 * @param second 把日期往后增加SECOND 秒.整数往后推,负数往前移动
	 * @return
	 */
	public static Date dateDiffSec(Date date, int second) {
		Calendar c = new GregorianCalendar();
		c.setTime(date);// 设置参数时间
		c.add(Calendar.SECOND, second);// 把日期往后增加SECOND 秒.整数往后推,负数往前移动
		date = c.getTime();
		return date;
	}

	// 把yyyymmdd转成yyyy-MM-dd格式

	public static String format_YYYY_MM_DD(String str) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
		String sfstr = "";
		try {
			if (ParamUtils.chkString(str)) {
				sfstr = sf2.format(sf1.parse(str));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sfstr;
	}

	// 把yyyy-MM-dd转成yyyymmdd格式

	public static String format_YYYYMMDD(String str) {
		SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
		String sfstr = "";
		try {
			if (ParamUtils.chkString(str)) {
				sfstr = sf1.format(sf2.parse(str));
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return sfstr;
	}

	public static String dateToString(Date date, String format) {
		SimpleDateFormat sf1 = new SimpleDateFormat(format);
		String sfstr = null;
		try {
			sfstr = sf1.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sfstr;

	}

	public static Date stringToDate(String str, String format) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(str);
		} catch (ParseException ex) {
		}
		return date;

	}

	public static int getHours(Date date) {
		Calendar rightYear = Calendar.getInstance();
		if (date != null) {
			rightYear.setTime(date);
		}
		return rightYear.get(Calendar.HOUR_OF_DAY);
	}

	public static int getMinute(Date date) {
		Calendar rightYear = Calendar.getInstance();
		if (date != null) {
			rightYear.setTime(date);
		}
		return rightYear.get(Calendar.MINUTE);
	}

	/**
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuringStyleCommon(long msec) {
		if (msec <= 0)
			return "00:00:00";
		long hours = (msec % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (msec % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (msec % (1000 * 60)) / 1000;
		StringBuffer sb = new StringBuffer();
		sb.append((hours > 0 ? (hours < 10 ? "0" + hours : hours) : "00") + ":");
		sb.append((minutes > 0 ? (minutes < 10 ? "0" + minutes : minutes)
				: "00") + ":");
		sb.append(seconds > 0 ? (seconds < 10 ? "0" + seconds : seconds) : "00");
		return sb.toString();
	}

	/**
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuringStyleAll(long msec) {
		if (msec <= 0)
			return "00:00:00:00";
		long days = msec / (1000 * 60 * 60 * 24);
		long hours = (msec % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (msec % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (msec % (1000 * 60)) / 1000;
		StringBuffer sb = new StringBuffer();
		sb.append(days > 0 ? (days < 10 ? "0" + days : days) : "00");
		sb.append(":");
		sb.append((hours > 0 ? (hours < 10 ? "0" + hours : hours) : "00") + ":");
		sb.append((minutes > 0 ? (minutes < 10 ? "0" + minutes : minutes)
				: "00") + ":");
		sb.append(seconds > 0 ? (seconds < 10 ? "0" + seconds : seconds) : "00");
		return sb.toString();
	}
	
	/**
	 * @param 要转换的毫秒数
	 * @return 该毫秒数转换为 * 天 * hours * minutes * seconds 后的格式
	 * @author fy.zhang
	 */
	public static String formatDuringStyleAll2(long msec) {
		if (msec <= 0)
			return "00:00:00:00";
		long days = msec / (1000 * 60 * 60 * 24);
		long hours = (msec % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
		long minutes = (msec % (1000 * 60 * 60)) / (1000 * 60);
		long seconds = (msec % (1000 * 60)) / 1000;
		StringBuffer sb = new StringBuffer();
		sb.append(days > 0 ? days + "天" : "");
		sb.append(":");
		sb.append((hours > 0 ? (hours < 10 ? "0" + hours : hours) : "00") + ":");
		sb.append((minutes > 0 ? (minutes < 10 ? "0" + minutes : minutes)
				: "00") + ":");
		sb.append(seconds > 0 ? (seconds < 10 ? "0" + seconds : seconds) : "00");
		return sb.toString();
	}

	/**
	 * 得到几天后的时间
	 * 
	 * @param d
	 * @param day
	 * @param format
	 * @return
	 */
	public static Date getDateAfter(Date d, int day, String format) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
		return getDateFormart(now.getTime(), format);
	}

	/**
	 * 传入的参数是java.util.Date格式
	 */
	public static Date getDateFormart(Date date, String format) {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateString = formatter.format(date);
		Date currentTime = null;
		try {
			currentTime = formatter.parse(dateString);
		} catch (Exception e) {
		}
		return currentTime;
	}

	/**
	 * 取当前日期，返回日期类型
	 * 
	 * @return
	 */

	public static Date getDateTimeOfMinutes(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getDateTimeOfSeconds(Date date, int seconds) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, seconds);
		return new Date(cal.getTimeInMillis());
	}

	public static Date getDateBefore(Date d, int day) {
		Calendar now = Calendar.getInstance();
		now.setTime(d);
		now.set(Calendar.DATE, now.get(Calendar.DATE) - day);
		return now.getTime();
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间  
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			smdate = sdf.parse(sdf.format(smdate));
			bdate = sdf.parse(sdf.format(bdate));
		} catch (Exception e) {
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/**
	 * 字符串的日期格式的计算
	 */
	public static int daysBetween(String smdate, String bdate) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(smdate));
			cal2.setTime(sdf.parse(bdate));
		} catch (Exception e) {
			e.printStackTrace();
		}

		long time1 = cal.getTimeInMillis();

		long time2 = cal2.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 获取这种格式的日期字符串 yyMMdd 2017-05-03--->170503
	 * 
	 * @param date
	 * @return
	 */
	public static String DateToStringYY(Date date) {
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		return sdf.format(date);
	}
	/**
	 * 获取本周一
	 * @param date
	 * @return
	 */
    public static Date getThisWeekMonday(Date date) {  
        Calendar cal = Calendar.getInstance();  
        cal.setTime(date);  
        // 获得当前日期是一个星期的第几天  
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);  
        if (1 == dayWeek) {  
            cal.add(Calendar.DAY_OF_MONTH, -1);  
        }  
        // 设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一  
        cal.setFirstDayOfWeek(Calendar.MONDAY);  
        // 获得当前日期是一个星期的第几天  
        int day = cal.get(Calendar.DAY_OF_WEEK);  
        // 根据日历的规则，给当前日期减去星期几与一个星期第一天的差值  
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);  
        return cal.getTime();  
    }  
    
    /**
     * 两个日期是否为同一天
     * @param d1
     * @param d2
     * @return
     */
    public static boolean checkSameDay(Date d1,Date d2){
    	if(d1!=null && d2!=null){
    		return DateToString(d1).equals(DateToString(d2))?true:false;
    	}
    	return false;
    }
    
    /**
	 * 取得今天开始时间-String类型
	 * @return
	 */
	public static String getCurrentDayStartStr() {
		return DateToString(getNowDate()) + " 00:00:00";
	}
	
	/**
	 * 取得今天结束时间 -String类型
	 * @return
	 */
	public static String getCurrentDayendStr() {
		return DateToString(getNowDate()) + " 23:59:59";
	}
}
