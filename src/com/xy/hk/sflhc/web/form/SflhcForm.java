package com.xy.hk.sflhc.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.hk.sflhc.model.dto.SflhcDTO;

public class SflhcForm extends ValidatorForm{
	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private SflhcDTO sflhcDto = new SflhcDTO();
	
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
	public SflhcDTO getSflhcDto() {
		return sflhcDto;
	}
	public void setSflhcDto(SflhcDTO sflhcDto) {
		this.sflhcDto = sflhcDto;
	}

	
}
