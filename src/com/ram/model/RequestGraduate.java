package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RequestGraduate implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer requestGraduateId;

    /** nullable persistent field */
    private Integer userId;

    /** nullable persistent field */
    private Integer semesterId;

    /** nullable persistent field */
    private String applicationSubmit;

    /** nullable persistent field */
    private String signature;

    /** nullable persistent field */
    private Date submitTime;

    /** nullable persistent field */
    private String resume;

    /** nullable persistent field */
    private String requestStatus;

    /** nullable persistent field */
    private Integer checkUserId;

    /** nullable persistent field */
    private Date checkDate;

    /** nullable persistent field */
    private String isGiveUpThesis;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** full constructor */
    public RequestGraduate(Integer userId, Integer semesterId, String applicationSubmit, String signature, Date submitTime, String resume, String requestStatus, Integer checkUserId, Date checkDate, String isGiveUpThesis, Integer updateUserId, Date updateDateTime) {
        this.userId = userId;
        this.semesterId = semesterId;
        this.applicationSubmit = applicationSubmit;
        this.signature = signature;
        this.submitTime = submitTime;
        this.resume = resume;
        this.requestStatus = requestStatus;
        this.checkUserId = checkUserId;
        this.checkDate = checkDate;
        this.isGiveUpThesis = isGiveUpThesis;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
    }

    /** default constructor */
    public RequestGraduate() {
    }

    public Integer getRequestGraduateId() {
        return this.requestGraduateId;
    }

    public void setRequestGraduateId(Integer requestGraduateId) {
        this.requestGraduateId = requestGraduateId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getSemesterId() {
        return this.semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public String getApplicationSubmit() {
        return this.applicationSubmit;
    }

    public void setApplicationSubmit(String applicationSubmit) {
        this.applicationSubmit = applicationSubmit;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getSubmitTime() {
        return this.submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getResume() {
        return this.resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public String getRequestStatus() {
        return this.requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Integer getCheckUserId() {
        return this.checkUserId;
    }

    public void setCheckUserId(Integer checkUserId) {
        this.checkUserId = checkUserId;
    }

    public Date getCheckDate() {
        return this.checkDate;
    }

    public void setCheckDate(Date checkDate) {
        this.checkDate = checkDate;
    }

    public String getIsGiveUpThesis() {
        return this.isGiveUpThesis;
    }

    public void setIsGiveUpThesis(String isGiveUpThesis) {
        this.isGiveUpThesis = isGiveUpThesis;
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

    public String toString() {
        return new ToStringBuilder(this)
            .append("requestGraduateId", getRequestGraduateId())
            .toString();
    }

}
