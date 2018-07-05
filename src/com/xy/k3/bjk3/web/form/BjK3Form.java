package com.xy.k3.bjk3.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.k3.bjk3.model.dto.BjK3BetDTO;

public class BjK3Form extends ValidatorForm{
	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private BjK3BetDTO bjk3betDto = new BjK3BetDTO();
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
	public BjK3BetDTO getBjk3betDto() {
		return bjk3betDto;
	}
	public void setBjk3betDto(BjK3BetDTO bjk3betDto) {
		this.bjk3betDto = bjk3betDto;
	}

	
}
