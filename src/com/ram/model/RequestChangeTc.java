package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RequestChangeTc implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer requestChangeTcId;

    /** nullable persistent field */
    private Integer semesterId;

    /** nullable persistent field */
    private Date requestTime;

    /** nullable persistent field */
    private String requestReason;

    /** nullable persistent field */
    private Integer requestUserId;

    /** nullable persistent field */
    private String requestStatus;

    /** nullable persistent field */
    private Integer firstCheckUserId;

    /** nullable persistent field */
    private Date firstCheckTime;

    /** nullable persistent field */
    private String firstCheckRemark;

    /** nullable persistent field */
    private Integer finalCheckUserId;

    /** nullable persistent field */
    private Date finalCheckTime;

    /** nullable persistent field */
    private String finalCheckRemark;

    /** nullable persistent field */
    private Integer fromTcId;

    /** nullable persistent field */
    private Integer toTcId;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** full constructor */
    public RequestChangeTc(Integer semesterId, Date requestTime, String requestReason, Integer requestUserId, String requestStatus, Integer firstCheckUserId, Date firstCheckTime, String firstCheckRemark, Integer finalCheckUserId, Date finalCheckTime, String finalCheckRemark, Integer fromTcId, Integer toTcId, Integer updateUserId, Date updateDateTime) {
        this.semesterId = semesterId;
        this.requestTime = requestTime;
        this.requestReason = requestReason;
        this.requestUserId = requestUserId;
        this.requestStatus = requestStatus;
        this.firstCheckUserId = firstCheckUserId;
        this.firstCheckTime = firstCheckTime;
        this.firstCheckRemark = firstCheckRemark;
        this.finalCheckUserId = finalCheckUserId;
        this.finalCheckTime = finalCheckTime;
        this.finalCheckRemark = finalCheckRemark;
        this.fromTcId = fromTcId;
        this.toTcId = toTcId;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
    }

    /** default constructor */
    public RequestChangeTc() {
    }

    public Integer getRequestChangeTcId() {
        return this.requestChangeTcId;
    }

    public void setRequestChangeTcId(Integer requestChangeTcId) {
        this.requestChangeTcId = requestChangeTcId;
    }

    public Integer getSemesterId() {
        return this.semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Date getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
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

    public Integer getFirstCheckUserId() {
        return this.firstCheckUserId;
    }

    public void setFirstCheckUserId(Integer firstCheckUserId) {
        this.firstCheckUserId = firstCheckUserId;
    }

    public Date getFirstCheckTime() {
        return this.firstCheckTime;
    }

    public void setFirstCheckTime(Date firstCheckTime) {
        this.firstCheckTime = firstCheckTime;
    }

    public String getFirstCheckRemark() {
        return this.firstCheckRemark;
    }

    public void setFirstCheckRemark(String firstCheckRemark) {
        this.firstCheckRemark = firstCheckRemark;
    }

    public Integer getFinalCheckUserId() {
        return this.finalCheckUserId;
    }

    public void setFinalCheckUserId(Integer finalCheckUserId) {
        this.finalCheckUserId = finalCheckUserId;
    }

    public Date getFinalCheckTime() {
        return this.finalCheckTime;
    }

    public void setFinalCheckTime(Date finalCheckTime) {
        this.finalCheckTime = finalCheckTime;
    }

    public String getFinalCheckRemark() {
        return this.finalCheckRemark;
    }

    public void setFinalCheckRemark(String finalCheckRemark) {
        this.finalCheckRemark = finalCheckRemark;
    }

    public Integer getFromTcId() {
        return this.fromTcId;
    }

    public void setFromTcId(Integer fromTcId) {
        this.fromTcId = fromTcId;
    }

    public Integer getToTcId() {
        return this.toTcId;
    }

    public void setToTcId(Integer toTcId) {
        this.toTcId = toTcId;
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
            .append("requestChangeTcId", getRequestChangeTcId())
            .toString();
    }

}
