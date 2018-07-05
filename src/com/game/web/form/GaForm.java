package com.game.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.game.model.GaSessionInfo;

public class GaForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private Integer totalCount;
	private String startIndex;
	private String urlSwitch;
	
	private GaSessionInfo sessionInfo=new GaSessionInfo();
	
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
	public String getUrlSwitch() {
		return urlSwitch;
	}
	public void setUrlSwitch(String urlSwitch) {
		this.urlSwitch = urlSwitch;
	}
	public GaSessionInfo getSessionInfo() {
		return sessionInfo;
	}
	public void setSessionInfo(GaSessionInfo sessionInfo) {
		this.sessionInfo = sessionInfo;
	}	
	
}
