package com.jc.model;

public class JcPlayType {
	
	private Integer pTId;
	/** 比赛局ID*/
	private Integer fId;
	/** 玩法标题*/
	private String title;
	/** 状态：1=有效，0=无效，无效状态下不可投注*/
	private String status;
	/** 开奖结果*/
	private String openResult;
	/** 开奖状态0=未开将 1=已开奖*/
	private String openStatus;
	
	
	public JcPlayType(){
		
	}
	
	public JcPlayType(Integer fId, String title, String status, String openStatus){
		this.fId = fId;
		this.title = title;
		this.status = status;
		this.openStatus = openStatus;
	}

	public Integer getpTId() {
		return pTId;
	}

	public void setpTId(Integer pTId) {
		this.pTId = pTId;
	}

	public Integer getfId() {
		return fId;
	}

	public void setfId(Integer fId) {
		this.fId = fId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getOpenResult() {
		return openResult;
	}

	public void setOpenResult(String openResult) {
		this.openResult = openResult;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
}
