/*
 * Created on 2005-3-1
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.framework.util;

import java.text.DateFormat;
import java.util.Date;

/**
 * @author sunan
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DateUtils {

  public static final long YEAR = 10000000000l;

  public static final long MONTH = 100000000;

  public static final long DAY = 1000000;

  public static final long HOUR = 10000;

  public static final long MINUTE = 100;

  public static long newDate() {
    long date = 0;
    return date;
  }

  public static long setYear(long date, long year) {
    date = year * YEAR + date % YEAR;
    return date;
  }

  public static long setYear(long date, String year) {
    long thisYear = 0;
    try {
      thisYear = Long.parseLong(year);
    }
    catch (Exception e) {
      return date;
    }

    return setYear(date, thisYear);
  }

  public static long getYear(long date) {

    return date / YEAR;
  }

  public static long setMonth(long date, long month) {
    long year = getYear(date);
    date = date - year * YEAR;
    date = month * MONTH + date % MONTH;

    return year * YEAR + date;
  }

  public static long setMonth(long date, String month) {
    long thisMonth = 0;
    try {
      thisMonth = Long.parseLong(month);
    }
    catch (Exception e) {
      return date;
    }

    return setMonth(date, thisMonth);
  }

  public static long getMonth(long date) {
    long year = getYear(date);
    date = date - year * YEAR;

    return date / MONTH;
  }

  public static long setDay(long date, long day) {
    long year = getYear(date);
    long month = getMonth(date);
    date = (date - year * YEAR) - month * MONTH;
    date = day * DAY + date % DAY;

    return year * YEAR + month * MONTH + date;
  }

  public static long setDay(long date, String day) {
    long thisDay = 0;
    try {
      thisDay = Long.parseLong(day);
    }
    catch (Exception e) {
      return date;
    }

    return setDay(date, thisDay);
  }

  public static long getDay(long date) {
    long year = getYear(date);
    long month = getMonth(date);
    date = date - year * YEAR - month * MONTH;

    return date / DAY;
  }

  public static long setHour(long date, long hour) {
    long thisDate = 0;
    long year = getYear(date);
    long month = getMonth(date);
    long day = getDay(date);

    thisDate = date % HOUR;
    thisDate = year * YEAR + month * MONTH + day * DAY + hour * HOUR
        + thisDate;

    return thisDate;
  }

  public static long setHour(long date, String hour) {
    long thisHour = 0;
    try {
      thisHour = Long.parseLong(hour);
    }
    catch (Exception e) {
      return date;
    }

    return setHour(date, thisHour);

  }

  public static long getHour(long date) {
    long hour = 0;
    hour = date % DAY;
    hour = hour / HOUR;
    return hour;
  }

  public static long setMinute(long date, long minute) {
    long thisDate = 0;
    long year = getYear(date);
    long month = getMonth(date);
    long day = getDay(date);
    long hour = getHour(date);

    thisDate = date % MINUTE;
    thisDate = year * YEAR + month * MONTH + day * DAY + hour * HOUR +
        minute * MINUTE + thisDate;

    return thisDate;
  }

  public static long setMinute(long date, String minute) {
    long thisMinute = 0;
    try {
      thisMinute = Long.parseLong(minute);
    }
    catch (Exception e) {
      return date;
    }

    return setMinute(date, thisMinute);
  }

  public static long getMinute(long date) {
    long minute = 0;

    minute = date % HOUR;
    minute = minute / MINUTE;

    return minute;
  }

  public static long setSecond(long date, long second) {
    long thisDate = 0;
    long year = getYear(date);
    long month = getMonth(date);
    long day = getDay(date);
    long hour = getHour(date);
    long minute = getMinute(date);

    thisDate = year * YEAR + month * MONTH + day * DAY + hour * HOUR +
        minute * MINUTE + second;

    return thisDate;
  }

  public static long setSecond(long date, String second) {
    long thisSecond = 0;
    try {
      thisSecond = Long.parseLong(second);
    }
    catch (Exception e) {
      return date;
    }

    return setSecond(date, thisSecond);
  }

  public static long getSecond(long date) {
    long second = 0;
    second = date % MINUTE;

    return second;
  }

  public static long getNowDate() {
    long date = 0;
    Date nowDate = new Date();
    DateFormat df = DateFormat.getDateInstance();
    String sDate = df.format(nowDate);
    int start = 0;
    int end = 0;
    String year = null;
    String month = null;
    String day = null;
    end = sDate.indexOf("-", start);
    if (end > 0) {
      year = sDate.substring(start, end);
    }
    start = end + 1;
    end = sDate.indexOf("-", start);
    if (end > 0) {
      month = sDate.substring(start, end);
    }
    start = end + 1;
    day = sDate.substring(start);
    /**
     * debug  start
     */


    /**
     * debug  end
     */
    date = setYear(date, year);
    date = setMonth(date, Long.parseLong(month));
    date = setDay(date, day);

    return date;
  }

  public static long getNowDateTime() {
    long date = 0;

    Date nowDate = new Date();
    DateFormat df = DateFormat.getDateTimeInstance();
    String sDate = df.format(nowDate);
    int start = 0;
    int end = 0;
    String year = null;
    String month = null;
    String day = null;
    String hour = null;
    String minute = null;
    String second = null;
    end = sDate.indexOf("-", start);
    if (end > 0) {
      year = sDate.substring(start, end);
    }
    start = end + 1;
    end = sDate.indexOf("-", start);
    if (end > 0) {
      month = sDate.substring(start, end);
    }

    start = end + 1;
    end = sDate.indexOf(" ", start);
    if (end > 0) {
      day = sDate.substring(start, end);
    }

    start = end + 1;
    end = sDate.indexOf(":", start);
    if (end > 0) {
      hour = sDate.substring(start, end);
    }

    start = end + 1;
    end = sDate.indexOf(":", start);
    if (end > 0) {
      minute = sDate.substring(start, end);
    }

    start = end + 1;
    second = sDate.substring(start);

    /**
     * debug  start
     */


    /**
     * debug  end
     */

    date = setYear(date, year);
    date = setMonth(date, month);
    date = setDay(date, day);
    date = setHour(date, hour);
    date = setMinute(date, minute);
    date = setSecond(date, second);
    return date;
  }

  public static String getDate(long date, String separator) {
    String returnDate = null;
    String day = null;
    String month = null;
    String year = null;
    year = Long.toString(getYear(date));
    if (getMonth(date) < 10) {
      month = "0" + Long.toString(getMonth(date));
    }
    else {
      month = Long.toString(getMonth(date));
    }

    if (getDay(date) < 10) {
      day = "0" + Long.toString(getDay(date));
    }
    else {
      day = Long.toString(getDay(date));
    }
    returnDate = year + separator + month + separator + day;

    return returnDate;
  }

  public static String getDate(long date) {
    String returnDate = "";
    String day = null;
    String month = null;
    String year = null;
    year = Long.toString(getYear(date));
    if (getMonth(date) < 10) {
      month = "0" + Long.toString(getMonth(date));
    }
    else {
      month = Long.toString(getMonth(date));
    }

    if (getDay(date) < 10) {
      day = "0" + Long.toString(getDay(date));
    }
    else {
      day = Long.toString(getDay(date));
    }
    returnDate = year + "-" + month + "-" + day;

    return returnDate;
  }

  public static String getDateCn(long date) {
    String returnDate = "";
    String day = null;
    String month = null;
    String year = null;
    String hour = null;
    String minute = null;
    String second = null;

    year = Long.toString(getYear(date));
    if (getMonth(date) < 10) {
      month = "0" + Long.toString(getMonth(date));
    }
    else {
      month = Long.toString(getMonth(date));
    }

    if (getDay(date) < 10) {
      day = "0" + Long.toString(getDay(date));
    }
    else {
      day = Long.toString(getDay(date));
    }

    if(getHour(date) < 10){
      hour = "0" + Long.toString(getHour(date));
    }
    else{
      hour = Long.toString(getHour(date));
    }

    if(getMinute(date) < 10){
      minute = "0" + Long.toString(getMinute(date));
    }
    else{
      minute = Long.toString(getMinute(date));
    }

    if(getSecond(date) < 10){
      second = "0" + Long.toString(getSecond(date));
    }
    else{
      second = Long.toString(getSecond(date));
    }

    returnDate = year + "年" + month + "月" + day + "日" + hour + "点" + minute + "分" + second + "秒";

    return returnDate;

  }

  public static long getDateTime(Date someDate) {
   long date = 0;

   Date nowDate = someDate;
   DateFormat df = DateFormat.getDateTimeInstance();
   String sDate = df.format(nowDate);
   int start = 0;
   int end = 0;
   String year = null;
   String month = null;
   String day = null;
   String hour = null;
   String minute = null;
   String second = null;
   end = sDate.indexOf("-", start);
   if (end > 0) {
     year = sDate.substring(start, end);
   }
   start = end + 1;
   end = sDate.indexOf("-", start);
   if (end > 0) {
     month = sDate.substring(start, end);
   }

   start = end + 1;
   end = sDate.indexOf(" ", start);
   if (end > 0) {
     day = sDate.substring(start, end);
   }

   start = end + 1;
   end = sDate.indexOf(":", start);
   if (end > 0) {
     hour = sDate.substring(start, end);
   }

   start = end + 1;
   end = sDate.indexOf(":", start);
   if (end > 0) {
     minute = sDate.substring(start, end);
   }

   start = end + 1;
   second = sDate.substring(start);

   /**
    * debug  start
    */


   /**
    * debug  end
    */

   date = setYear(date, year);
   date = setMonth(date, month);
   date = setDay(date, day);
   date = setHour(date, hour);
   date = setMinute(date, minute);
   date = setSecond(date, second);
   return date;
 }


 public static long stringToLong(String someDate) {
   long date = 0;

   String sDate = someDate;
   int start = 0;
   int end = 0;
   String year = null;
   String month = null;
   String day = null;
   String hour = null;
   String minute = null;
   String second = null;
   end = sDate.indexOf("-", start);
   if (end > 0) {
     year = sDate.substring(start, end);
   }
   start = end + 1;
   end = sDate.indexOf("-", start);
   if (end > 0) {
     month = sDate.substring(start, end);
   }

   start = end + 1;
   end = sDate.indexOf(" ", start);
   if (end > 0) {
     day = sDate.substring(start, end);
   }

   start = end + 1;
   end = sDate.indexOf(":", start);
   if (end > 0) {
     hour = sDate.substring(start, end);
   }

   start = end + 1;
   end = sDate.indexOf(":", start);
   if (end > 0) {
     minute = sDate.substring(start, end);
   }

   start = end + 1;
   second = sDate.substring(start);

   /**
    * debug  start
    */


   /**
    * debug  end
    */

   date = setYear(date, year);
   date = setMonth(date, month);
   date = setDay(date, day);
   date = setHour(date, hour);
   date = setMinute(date, minute);
   date = setSecond(date, second);
   return date;
 }


}
