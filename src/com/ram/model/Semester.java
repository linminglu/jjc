package com.ram.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @autsHibernate CodeGenerator */
public class Semester implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer semesterId;

    /** nullable persistent field */
    private String semesterTitle;

    /** nullable persistent field */
    private Integer enrollYear;

    /** nullable persistent field */
    private Integer enrollSeason;

    /** persistent field */
    private Date dateStart;

    /** persistent field */
    private Date dateEnd;

    /** nullable persistent field */
    private String semesterStatus;

    private String isEnrollSemester;
    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** persistent field */
    private Set businessGroups;
    
    private Set examPeriods;

    /** full constructor */
    public Semester(String semesterTitle, Integer enrollYear, Integer enrollSeason, Date dateStart, Date dateEnd, String semesterStatus,String isEnrollSemester, Integer updateUserId, Date updateDateTime, Set businessGroups,Set examPeriods) {
        this.semesterTitle = semesterTitle;
        this.enrollYear = enrollYear;
        this.enrollSeason = enrollSeason;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.semesterStatus = semesterStatus;
        this.isEnrollSemester=isEnrollSemester;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
        this.businessGroups = businessGroups;
        this.examPeriods = examPeriods;
    }

    /** default constructor */
    public Semester() {
    }

    /** minimal constructor */
    public Semester(Date dateStart, Date dateEnd, Set businessGroups) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.businessGroups = businessGroups;
    }

    public Integer getSemesterId() {
        return this.semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getSemesterTitle() {
        return this.semesterTitle;
    }

    public void setSemesterTitle(String semesterTitle) {
        this.semesterTitle = semesterTitle;
    }

    public Integer getEnrollYear() {
        return this.enrollYear;
    }

    public void setEnrollYear(Integer enrollYear) {
        this.enrollYear = enrollYear;
    }

    public Integer getEnrollSeason() {
        return this.enrollSeason;
    }

    public void setEnrollSeason(Integer enrollSeason) {
        this.enrollSeason = enrollSeason;
    }

    public Date getDateStart() {
        return this.dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return this.dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getSemesterStatus() {
        return this.semesterStatus;
    }

    public void setSemesterStatus(String semesterStatus) {
        this.semesterStatus = semesterStatus;
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

    public Set getBusinessGroups() {
        return this.businessGroups;
    }

    public void setBusinessGroups(Set businessGroups) {
        this.businessGroups = businessGroups;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("semesterId", getSemesterId())
            .toString();
    }

	public Set getExamPeriods() {
		return examPeriods;
	}

	public void setExamPeriods(Set examPeriods) {
		this.examPeriods = examPeriods;
	}

	public String getIsEnrollSemester() {
		return isEnrollSemester;
	}

	public void setIsEnrollSemester(String isEnrollSemester) {
		this.isEnrollSemester = isEnrollSemester;
	}

}
