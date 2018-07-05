package com.apps.model;

import java.util.Date;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 转盘&红包设置
 */
public class LotterySetting implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer lsId;
	private Date startTime;// 抽奖开始时间
	private Date endTime;// 抽奖结束时间
	private String type;// 类型 1=抽奖 2=红包 3=打码返水
	private String status;// 开关 1=开 0=关
	private BigDecimal money;// 随身金额

	public Integer getLsId() {
		return lsId;
	}

	public void setLsId(Integer lsId) {
		this.lsId = lsId;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getMoney() {
		return money;
	}
}
