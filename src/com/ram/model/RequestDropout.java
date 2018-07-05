package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RequestDropout implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer requestDropoutId;

    /** nullable persistent field */
    private Integer semesterId;

    /** nullable persistent field */
    private Date requestDate;

    /** nullable persistent field */
    private String requestReason;

    /** nullable persistent field */
    private Integer requestUserId;

    /** nullable persistent field */
    private String requestStatus="2";

    /** nullable persistent field */
    private Integer checkUserId;

    /** nullable persistent field */
    private Date approveDate;

    /** nullable persistent field */
    private String requestRemark;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;
    
    private String finalRequestRemark;
    
    private Date finalCheckDate;
    
    private Integer finalUserId;

    /** full constructor */
    public RequestDropout(Integer semesterId, Date requestDate, String requestReason, Integer requestUserId, String requestStatus, Integer checkUserId, Date approveDate, String requestRemark, Integer updateUserId, Date updateDateTime, String finalRequestRemark, Date finalCheckDate, Integer finalUserId) {
        this.semesterId = semesterId;
        this.requestDate = requestDate;
        this.requestReason = requestReason;
        this.requestUserId = requestUserId;
        this.requestStatus = requestStatus;
        this.checkUserId = checkUserId;
        this.approveDate = approveDate;
        this.requestRemark = requestRemark;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
        this.finalRequestRemark = finalRequestRemark;
        this.finalCheckDate = finalCheckDate;
        this.finalUserId = finalUserId;
    }

    /** default constructor */
    public RequestDropout() {
    }

    public Integer getRequestDropoutId() {
        return this.requestDropoutId;
    }

    public void setRequestDropoutId(Integer requestDropoutId) {
        this.requestDropoutId = requestDropoutId;
    }

    public Integer getSemesterId() {
        return this.semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Date getRequestDate() {
        return this.requestDate;
    }

    public void setRequestDate(Date requestDate) {
        this.requestDate = requestDate;
    }

    public String getRequestReason() {
        return this.requestReason;
    }

    public void setRequestReason(String requestReason) {
        this.requestReason = requestReason;
    }

    public Integer getRequestUserId() {
        return this.requestUserId;
    }

    public void setRequestUserId(Integer requestUserId) {
        this.requestUserId = requestUserId;
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

    public Date getApproveDate() {
        return this.approveDate;
    }

    public void setApproveDate(Date approveDate) {
        this.approveDate = approveDate;
    }

    public String getRequestRemark() {
        return this.requestRemark;
    }

    public void setRequestRemark(String requestRemark) {
        this.requestRemark = requestRemark;
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

	public String getFinalRequestRemark() {
		return finalRequestRemark;
	}

	public void setFinalRequestRemark(String finalRequestRemark) {
		this.finalRequestRemark = finalRequestRemark;
	}

    public Date getFinalCheckDate() {
		return finalCheckDate;
	}

	public void setFinalCheckDate(Date finalCheckDate) {
		this.finalCheckDate = finalCheckDate;
	}

	public Integer getFinalUserId() {
		return finalUserId;
	}

	public void setFinalUserId(Integer finalUserId) {
		this.finalUserId = finalUserId;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("requestDropoutId", getRequestDropoutId())
            .toString();
    }

}
