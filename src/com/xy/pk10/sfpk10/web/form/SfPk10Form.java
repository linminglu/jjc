package com.xy.pk10.sfpk10.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.pk10.sfpk10.model.dto.SfPk10DTO;

public class SfPk10Form  extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private SfPk10DTO sfPk10Dto = new SfPk10DTO();
	
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
	public SfPk10DTO getSfPk10Dto() {
		return sfPk10Dto;
	}
	public void setSfPk10Dto(SfPk10DTO sfPk10Dto) {
		this.sfPk10Dto = sfPk10Dto;
	}
	
}
