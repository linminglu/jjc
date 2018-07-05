package com.xy.pk10.sfpk102.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.pk10.sfpk102.model.dto.SfPk102DTO;

public class SfPk102Form  extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private SfPk102DTO sfPk102Dto = new SfPk102DTO();
	
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
	public SfPk102DTO getSfPk102Dto() {
		return sfPk102Dto;
	}
	public void setSfPk102Dto(SfPk102DTO sfPk102Dto) {
		this.sfPk102Dto = sfPk102Dto;
	}
	
}
