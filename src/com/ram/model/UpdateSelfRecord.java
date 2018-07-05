package com.ram.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

public class UpdateSelfRecord implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String studyNumber; //学籍号
	private String userNameZh;  //姓名
    private String startDate; //开始时间
    private String endDate; //结束时间
    private String workUnitName;//描述
    
    public UpdateSelfRecord(){}
    
	public UpdateSelfRecord(String studyNumber, String userNameZh, String startDate, String endDate, String workUnitName) {
		this.studyNumber = studyNumber;
		this.userNameZh = userNameZh;
		this.startDate = startDate;
		this.endDate = endDate;
		this.workUnitName = workUnitName;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStudyNumber() {
		return studyNumber;
	}

	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getUserNameZh() {
		return userNameZh;
	}

	public void setUserNameZh(String userNameZh) {
		this.userNameZh = userNameZh;
	}

	public String getWorkUnitName() {
		return workUnitName;
	}

	public void setWorkUnitName(String workUnitName) {
		this.workUnitName = workUnitName;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("id", getId())
            .toString();
    }
}