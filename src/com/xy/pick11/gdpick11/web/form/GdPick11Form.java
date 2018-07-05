package com.xy.pick11.gdpick11.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.pick11.gdpick11.model.dto.GdPick11DTO;

public class GdPick11Form extends ValidatorForm{

	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private GdPick11DTO gdpick11Dto = new GdPick11DTO();
	
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
	public GdPick11DTO getGdpick11Dto() {
		return gdpick11Dto;
	}
	public void setGdpick11Dto(GdPick11DTO gdpick11Dto) {
		this.gdpick11Dto = gdpick11Dto;
	}
	
}
