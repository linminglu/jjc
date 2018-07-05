package com.xy.k3.jsk3.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.xy.k3.jsk3.model.dto.JsK3BetDTO;

public class JsK3Form extends ValidatorForm{
	private static final long serialVersionUID = 1L;

	private String startDate;
	private String endDate;
	private String sessionNo;
	private String startIndex;
	private String status;
	private String userName;
	private JsK3BetDTO jsk3Dto = new JsK3BetDTO();
	
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
	public JsK3BetDTO getJsk3Dto() {
		return jsk3Dto;
	}
	public void setJsk3Dto(JsK3BetDTO jsk3Dto) {
		this.jsk3Dto = jsk3Dto;
	}

	
}
