package com.xy.pk10.bjpk10.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.pk10.bjpk10.model.dto.BjPk10DTO;

public class BjPk10Form  extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private BjPk10DTO bjPk10Dto = new BjPk10DTO();
	
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
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BjPk10DTO getBjPk10Dto() {
		return bjPk10Dto;
	}
	public void setBjPk10Dto(BjPk10DTO bjPk10Dto) {
		this.bjPk10Dto = bjPk10Dto;
	}
	
	
}
