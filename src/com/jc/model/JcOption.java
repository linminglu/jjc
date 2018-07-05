package com.jc.model;

import java.math.BigDecimal;

public class JcOption {
	
	private Integer oId;
	/** 玩法ID*/
	private Integer pTId;
	/** 投注标题*/
	private String title;
	/** 赔率*/
	private BigDecimal rate;
	/** 比赛局ID*/
	private Integer fId;

	
	public JcOption(){
		
	}
	
	public JcOption(Integer pTId, String title, BigDecimal rate, Integer fId){
		this.pTId = pTId;
		this.title = title;
		this.rate = rate;
		this.fId = fId;
	}
	
	
	public Integer getoId() {
		return oId;
	}

	public void setoId(Integer oId) {
		this.oId = oId;
	}

	public Integer getpTId() {
		return pTId;
	}

	public void setpTId(Integer pTId) {
		this.pTId = pTId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getfId() {
		return fId;
	}

	public void setfId(Integer fId) {
		this.fId = fId;
	}
}
