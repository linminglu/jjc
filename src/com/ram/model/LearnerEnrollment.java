package com.ram.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class LearnerEnrollment implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer learnerEnrollmentId;

    /** nullable persistent field */
    private Integer userId;

    /** nullable persistent field */
    private Integer scheduleId;

    /** nullable persistent field */
    private Integer scheduleModuleId;    

    /** nullable persistent field */
    private Integer courseId;

    /** nullable persistent field */
    private Integer scheduleCourseId;

    /** nullable persistent field */
    private String courseStatus;

    /** nullable persistent field */
    private Integer selSemesterId;

    /** nullable persistent field */
    private Integer curSemesterId;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** nullable persistent field */
    private String isCourseExamine;

    /** nullable persistent field */
    private BigDecimal courseScore;
    
    /** nullable persistent field */
    private Date bookDate;
    
    /** nullable persistent field */
    private String bookStatus;

    private String mailStatus;
    
    private String oldStatusEnrolltype;
    
    private Integer tutorId;
    
    private Integer agoTutorId;
    
    private String reEnrollFlag;
    
    public String getReEnrollFlag() {
		return reEnrollFlag;
	}

	public void setReEnrollFlag(String reEnrollFlag) {
		this.reEnrollFlag = reEnrollFlag;
	}

	/** full constructor */
    public LearnerEnrollment(Integer userId, Integer scheduleId, Integer scheduleModuleId, Integer courseId, Integer scheduleCourseId, String courseStatus, Integer selSemesterId, Integer curSemesterId, Integer updateUserId, Date updateDateTime, String isCourseExamine, BigDecimal courseScore, Date bookDate, String bookStatus,String mailStatus,String oldStatusEnrolltype,Integer tutorId,Integer agoTutorId,String reEnrollFlag) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.scheduleModuleId = scheduleModuleId;
        this.courseId = courseId;
        this.scheduleCourseId = scheduleCourseId;
        this.courseStatus = courseStatus;
        this.selSemesterId = selSemesterId;
        this.curSemesterId = curSemesterId;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
        this.isCourseExamine = isCourseExamine;
        this.courseScore = courseScore;
        this.bookDate = bookDate;
        this.bookStatus = bookStatus;
        this.mailStatus = mailStatus;
        this.oldStatusEnrolltype=oldStatusEnrolltype;
        this.tutorId = tutorId;
        this.agoTutorId = agoTutorId;
        this.reEnrollFlag = reEnrollFlag;
    }

    /** default constructor */
    public LearnerEnrollment() {
    }

    public Integer getLearnerEnrollmentId() {
        return this.learnerEnrollmentId;
    }

    public void setLearnerEnrollmentId(Integer learnerEnrollmentId) {
        this.learnerEnrollmentId = learnerEnrollmentId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getScheduleModuleId() {
		return scheduleModuleId;
	}

	public void setScheduleModuleId(Integer scheduleModuleId) {
		this.scheduleModuleId = scheduleModuleId;
	}

	public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getScheduleCourseId() {
        return this.scheduleCourseId;
    }

    public void setScheduleCourseId(Integer scheduleCourseId) {
        this.scheduleCourseId = scheduleCourseId;
    }

    public String getCourseStatus() {
        return this.courseStatus;
    }

    public void setCourseStatus(String courseStatus) {
        this.courseStatus = courseStatus;
    }

    public Integer getSelSemesterId() {
        return this.selSemesterId;
    }

    public void setSelSemesterId(Integer selSemesterId) {
        this.selSemesterId = selSemesterId;
    }

    public Integer getCurSemesterId() {
        return this.curSemesterId;
    }

    public void setCurSemesterId(Integer curSemesterId) {
        this.curSemesterId = curSemesterId;
    }

    public Integer getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getIsCourseExamine() {
        return this.isCourseExamine;
    }

    public void setIsCourseExamine(String isCourseExamine) {
        this.isCourseExamine = isCourseExamine;
    }

    public BigDecimal getCourseScore() {
        return this.courseScore;
    }

    public void setCourseScore(BigDecimal courseScore) {
        this.courseScore = courseScore;
    }
    public Date getBookDate() {
		return bookDate;
	}

	public void setBookDate(Date bookDate) {
		this.bookDate = bookDate;
	}

	public String getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("learnerEnrollmentId", getLearnerEnrollmentId())
            .toString();
    }

	public String getOldStatusEnrolltype() {
		return oldStatusEnrolltype;
	}

	public void setOldStatusEnrolltype(String oldStatusEnrolltype) {
		this.oldStatusEnrolltype = oldStatusEnrolltype;
	}

	public Integer getTutorId() {
		return tutorId;
	}

	public void setTutorId(Integer tutorId) {
		this.tutorId = tutorId;
	}

	public String getMailStatus() {
		return mailStatus;
	}

	public void setMailStatus(String mailStatus) {
		this.mailStatus = mailStatus;
	}

	public Integer getAgoTutorId() {
		return agoTutorId;
	}

	public void setAgoTutorId(Integer agoTutorId) {
		this.agoTutorId = agoTutorId;
	}
	
	

}
