package com.game.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.game.model.GaHhmfBetOption;

public class GaHhmfForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private Integer totalCount;
	private String startIndex;
	private String startTime;//开始时间
	private String endTime;//结束时间
	private String sessionNoStart;//开始期号
	private String sessionNoEnd;//结束期号
	private String loginName;
	private GaHhmfBetOption gaHhmfBetOption = new GaHhmfBetOption();
	
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getSessionNoStart() {
		return sessionNoStart;
	}
	public void setSessionNoStart(String sessionNoStart) {
		this.sessionNoStart = sessionNoStart;
	}
	public String getSessionNoEnd() {
		return sessionNoEnd;
	}
	public void setSessionNoEnd(String sessionNoEnd) {
		this.sessionNoEnd = sessionNoEnd;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public GaHhmfBetOption getGaHhmfBetOption() {
		return gaHhmfBetOption;
	}
	public void setGaHhmfBetOption(GaHhmfBetOption gaHhmfBetOption) {
		this.gaHhmfBetOption = gaHhmfBetOption;
	}
	
	

}
