package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class CalenderAlert implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer calenderAlertId;

    /** nullable persistent field */
    private String calenderTitle;

    /** nullable persistent field */
    private String calenderContent;

    /** nullable persistent field */
    private Date actionDate;

    /** nullable persistent field */
    private Integer alertAheadDays;

    /** nullable persistent field */
    private String calendarType;

    /** nullable persistent field */
    private String isAlert="0";

    /** nullable persistent field */
    private String isEmailAlert="0";

    /** nullable persistent field */
    private String isOnlineMessageAlert="1";

    /** nullable persistent field */
    private String isMobileMessageAlert="0";

    /** nullable persistent field */
    private Integer createUserId;

    /** nullable persistent field */
    private String createUserName;

    /** nullable persistent field */
    private Date sendDateTime;
    
    private Integer scheduleCourseId;
    
    private  Integer userId;

   

	/** full constructor */
    public CalenderAlert(String calenderTitle, String calenderContent, Date actionDate, Integer alertAheadDays, String calendarType, String isAlert, String isEmailAlert, String isOnlineMessageAlert, String isMobileMessageAlert, Integer createUserId, String createUserName, Date sendDateTime,Integer scheduleCourseId,Integer userId) {
        this.calenderTitle = calenderTitle;
        this.calenderContent = calenderContent;
        this.actionDate = actionDate;
        this.alertAheadDays = alertAheadDays;
        this.calendarType = calendarType;
        this.isAlert = isAlert;
        this.isEmailAlert = isEmailAlert;
        this.isOnlineMessageAlert = isOnlineMessageAlert;
        this.isMobileMessageAlert = isMobileMessageAlert;
        this.createUserId = createUserId;
        this.createUserName = createUserName;
        this.sendDateTime = sendDateTime;
        this.scheduleCourseId=scheduleCourseId;
        this.userId=userId;
    }

    /** default constructor */
    public CalenderAlert() {
    }

    public Integer getCalenderAlertId() {
        return this.calenderAlertId;
    }

    public void setCalenderAlertId(Integer calenderAlertId) {
        this.calenderAlertId = calenderAlertId;
    }

    public String getCalenderTitle() {
        return this.calenderTitle;
    }

    public void setCalenderTitle(String calenderTitle) {
        this.calenderTitle = calenderTitle;
    }

    public String getCalenderContent() {
        return this.calenderContent;
    }

    public void setCalenderContent(String calenderContent) {
        this.calenderContent = calenderContent;
    }

    public Date getActionDate() {
        return this.actionDate;
    }

    public void setActionDate(Date actionDate) {
        this.actionDate = actionDate;
    }

    public Integer getAlertAheadDays() {
        return this.alertAheadDays;
    }

    public void setAlertAheadDays(Integer alertAheadDays) {
        this.alertAheadDays = alertAheadDays;
    }

    public String getCalendarType() {
        return this.calendarType;
    }

    public void setCalendarType(String calendarType) {
        this.calendarType = calendarType;
    }

    public String getIsAlert() {
        return this.isAlert;
    }

    public void setIsAlert(String isAlert) {
        this.isAlert = isAlert;
    }

    public String getIsEmailAlert() {
        return this.isEmailAlert;
    }

    public void setIsEmailAlert(String isEmailAlert) {
        this.isEmailAlert = isEmailAlert;
    }

    public String getIsOnlineMessageAlert() {
        return this.isOnlineMessageAlert;
    }

    public void setIsOnlineMessageAlert(String isOnlineMessageAlert) {
        this.isOnlineMessageAlert = isOnlineMessageAlert;
    }

    public String getIsMobileMessageAlert() {
        return this.isMobileMessageAlert;
    }

    public void setIsMobileMessageAlert(String isMobileMessageAlert) {
        this.isMobileMessageAlert = isMobileMessageAlert;
    }

    public Integer getCreateUserId() {
        return this.createUserId;
    }

    public void setCreateUserId(Integer createUserId) {
        this.createUserId = createUserId;
    }

    public String getCreateUserName() {
        return this.createUserName;
    }

    public void setCreateUserName(String createUserName) {
        this.createUserName = createUserName;
    }

    public Date getSendDateTime() {
        return this.sendDateTime;
    }

    public void setSendDateTime(Date sendDateTime) {
        this.sendDateTime = sendDateTime;
    }
    
  
    public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("calenderAlertId", getCalenderAlertId())
            .toString();
    }

	public Integer getScheduleCourseId() {
		return scheduleCourseId;
	}

	public void setScheduleCourseId(Integer scheduleCourseId) {
		this.scheduleCourseId = scheduleCourseId;
	}

}
