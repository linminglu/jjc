package com.xy.lucky28.bjlu28.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.lucky28.bjlu28.model.dto.BjLu28BetDTO;

public class BjLu28Form extends ValidatorForm{
	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private BjLu28BetDTO bjlu28Dto = new BjLu28BetDTO();
	
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
	public BjLu28BetDTO getBjlu28Dto() {
		return bjlu28Dto;
	}
	public void setBjlu28Dto(BjLu28BetDTO bjlu28Dto) {
		this.bjlu28Dto = bjlu28Dto;
	}

}
