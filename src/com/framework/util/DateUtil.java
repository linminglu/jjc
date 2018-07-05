package com.framework.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;

import com.framework.Constants;


/**
 * Date Utility Class
 * This is used to convert Strings to Dates and Timestamps
 *
 * <p>
 * <a href="DateUtil.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *  Modified by <a href="mailto:dan@getrolling.com">Dan Kibler </a> 
 *   to correct time pattern. Minutes should be mm not MM
 * 	(MM is month). 
 * @version $Revision: 1.7 $ $Date: 2005/05/04 04:57:41 $
 */
public class DateUtil {
    //~ Static fields/initializers =============================================

    private static Log log = LogFactory.getLog(DateUtil.class);
    private static String defaultDatePattern = null;
    private static String timePattern = "HH:mm";

    //~ Methods ================================================================

    /**
     * Return default datePattern (MM/dd/yyyy)
     * @return a string representing the date pattern on the UI
     */
    public static synchronized String getDatePattern() {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
                .getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "yyyy-MM-dd";
        }
        
        return defaultDatePattern;
    }
    
    public static synchronized String getDatePattern2() {
        Locale locale = LocaleContextHolder.getLocale();
        try {
            defaultDatePattern = ResourceBundle.getBundle(Constants.BUNDLE_KEY, locale)
                .getString("date.format");
        } catch (MissingResourceException mse) {
            defaultDatePattern = "yyMMddhhmmss";
        }
        
        return defaultDatePattern;
    }

    /**
     * This method attempts to convert an Oracle-formatted date
     * in the form dd-MMM-yyyy to mm/dd/yyyy.
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(getDatePattern());
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @see java.text.SimpleDateFormat
     * @throws ParseException
     */
    public static final Date convertStringToDate(String aMask, String strDate)
      throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '"
                      + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * This method returns the current date time in the format:
     * MM/dd/yyyy HH:MM a
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * This method returns the current date in the format: MM/dd/yyyy
     * 
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(getDatePattern());

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * This method generates a string representation of a date's date/time
     * in the format you specify on input
     *
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * 
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * This method generates a string representation of a date based
     * on the System Property 'dateFormat'
     * in the format you specify on input
     * 
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(getDatePattern(), aDate);
    }
    
    public static final String convertDateToString2(Date aDate) {
        return getDateTime(getDatePattern2(), aDate);
    }

    /**
     * This method converts a String to a date using the datePattern
     * 
     * @param strDate the date to convert (in format MM/dd/yyyy)
     * @return a date object
     * 
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
      throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + getDatePattern());
            }

            aDate = convertStringToDate(getDatePattern(), strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate
                      + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                                     pe.getErrorOffset());
                    
        }

        return aDate;
    }
    /**
     * String to Date 时间格式转换
     * @param strDate
     * @return
     */
    public static Date strToDate(String strDate) {
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
    
    /**
     *Date to String 
     * @param dateDate
     * @return
     */
    public static String dateToStr(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    
    /**
     * 某个时间加上天数得到新的时间
     * @param date
     * @param day
     * @return
     */
    public static java.util.Date incDate(java.util.Date date, int day) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, day);
        return cal.getTime();
    }
    
    /**
     * 某个时间减去天数得到新的时间
     * @param date
     * @param day
     * @return
     */
    
    public static java.util.Date suDate(java.util.Date date, int day) {
         Calendar cal = new GregorianCalendar();
         cal.setTime(date);
         cal.add(Calendar.DAY_OF_YEAR,-day);
         return cal.getTime();
     }
    
    /**
     * 
     * @param dateDate
     * @return
     */
    public static String dateToStrLong(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    
    
    /**
     * 
     * @param dateDate
     * @return
     */
    public static String dateToStrShort(java.util.Date dateDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(dateDate);
        return dateString;
    }
    
    /**
     *date to date 
     * @param date
     * @return
     */
    public static Date date2Date(Date date) {       
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(date);
        ParsePosition pos = new ParsePosition(0);
        Date date2 = formatter.parse(dateString, pos);
        return date2;
    }
    
    public static Date date2DateLower(Date date) {       
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        ParsePosition pos = new ParsePosition(0);
        Date date2 = formatter.parse(dateString, pos);
        return date2;
    }
    
    
    /**
     * 对两个日期类型的变量进行比较，切记：不包括时间比较
     * @param date1
     * @param date2
     * @return
     */
    
    public static int compareTwoDate(java.util.Date date1, java.util.Date date2){
    	if(date1.compareTo(date2) == 0){
    		return 0;
    	}
    	else if(date1.compareTo(date2) < 0){
    		if(date1.toString().equals(date2.toString())){
    			return 0;
    		}
    		else{
    			return -1;
    		}
    	}
    	else{
    		if(date1.toString().equals(date2.toString())){
    			return 0;
    		}
    		else{
    			return 1;
    		}
    	}
    }
    /**
	 * 在一个时间上加next天，返回yyyy-MM-dd
	 */
	 public static String getNextSomeDay(Date date, int next) {	     	    
	        try { 
	            long diffTime = date.getTime() + next * 24 * 60 * 60 * 1000;	           
	            Date nextDate = new Date(diffTime);	            
	            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
	            String str=sdf.format(nextDate);
	            return str;
	        } catch (Exception e) {
	            e.printStackTrace();
	            return null;
	        }
	    }
	    /**
		 * 在一个时间上减去next天，返回yyyy-MM-dd
		 */
		 public static String getBeforeSomeDay(Date date, int next) {	     	    
		        try { 
		            long diffTime = date.getTime() - next * 24 * 60 * 60 * 1000;	           
		            Date nextDate = new Date(diffTime);	            
		            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		            String str=sdf.format(nextDate);
		            return str;
		        } catch (Exception e) {
		            e.printStackTrace();
		            return null;
		        }
		    }	
		    /**
		     *Date to String 
		     * @param dateDate
		     * @return
		     */
		    public static String dateToStrHHMM(java.util.Date dateDate) {
		        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		        String dateString = formatter.format(dateDate);
		        return dateString;
		    }
		    
}
