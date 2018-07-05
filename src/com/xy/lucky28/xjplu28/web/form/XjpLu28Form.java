package com.xy.lucky28.xjplu28.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.lucky28.xjplu28.model.dto.XjpLu28BetDTO;

public class XjpLu28Form extends ValidatorForm{
	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private XjpLu28BetDTO xjpLu28BetDto = new XjpLu28BetDTO();
	
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
	public XjpLu28BetDTO getXjpLu28BetDto() {
		return xjpLu28BetDto;
	}
	public void setXjpLu28BetDto(XjpLu28BetDTO xjpLu28BetDto) {
		this.xjpLu28BetDto = xjpLu28BetDto;
	}

	
}
