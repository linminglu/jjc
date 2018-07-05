package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class StudentBehaveInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer studentBehaveInfoId;

    /** nullable persistent field */
    private Integer learnerId;

    /** nullable persistent field */
    private Date behaveDate;

    /** nullable persistent field */
    private String behaveDesp;

    /** nullable persistent field */
    private Integer updateUserId;

    /** full constructor */
    public StudentBehaveInfo(Integer learnerId, Date behaveDate, String behaveDesp, Integer updateUserId) {
        this.learnerId = learnerId;
        this.behaveDate = behaveDate;
        this.behaveDesp = behaveDesp;
        this.updateUserId = updateUserId;
    }

    /** default constructor */
    public StudentBehaveInfo() {
    }

    public Integer getStudentBehaveInfoId() {
        return this.studentBehaveInfoId;
    }

    public void setStudentBehaveInfoId(Integer studentBehaveInfoId) {
        this.studentBehaveInfoId = studentBehaveInfoId;
    }

    public Integer getLearnerId() {
        return this.learnerId;
    }

    public void setLearnerId(Integer learnerId) {
        this.learnerId = learnerId;
    }

    public Date getBehaveDate() {
        return this.behaveDate;
    }

    public void setBehaveDate(Date behaveDate) {
        this.behaveDate = behaveDate;
    }

    public String getBehaveDesp() {
        return this.behaveDesp;
    }

    public void setBehaveDesp(String behaveDesp) {
        this.behaveDesp = behaveDesp;
    }

    public Integer getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("studentBehaveInfoId", getStudentBehaveInfoId())
            .toString();
    }

}
