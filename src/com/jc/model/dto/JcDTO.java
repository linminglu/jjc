package com.jc.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.game.model.UserBetCount;
import com.ram.model.User;

public class JcDTO {
	private Integer fId;// 局id
	private String fieldTitle;// 局名称
	
	private Integer pTId;// 玩法id
	private String playTitle;// 玩法名称
	private String openResult;// 开奖结果
	private String openStatus;// 开奖状态0未开奖1已开奖
	
	private Integer optionId;// 投注项id
	private String optionTitle;// 投注项名称
	private BigDecimal rate;// 赔率
	
	public JcDTO(){}

	// 后台开奖查询投注项列表
	public JcDTO(Integer fId, String fieldTitle, Integer pTId, String playTitle,
			String openResult, String openStatus, Integer optionId,
			String optionTitle, BigDecimal rate) {
		super();
		this.fId = fId;
		this.fieldTitle = fieldTitle;
		this.pTId = pTId;
		this.playTitle = playTitle;
		this.openResult = openResult;
		this.openStatus = openStatus;
		this.optionId = optionId;
		this.optionTitle = optionTitle;
		this.rate = rate;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public Integer getpTId() {
		return pTId;
	}

	public void setpTId(Integer pTId) {
		this.pTId = pTId;
	}

	public String getPlayTitle() {
		return playTitle;
	}

	public void setPlayTitle(String playTitle) {
		this.playTitle = playTitle;
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

	public Integer getOptionId() {
		return optionId;
	}

	public void setOptionId(Integer optionId) {
		this.optionId = optionId;
	}

	public String getOptionTitle() {
		return optionTitle;
	}

	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
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
