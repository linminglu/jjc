package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class LotterySettingRl implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer rid;
	private Integer lsId;// 设置表id
	private String type;// 奖金类型 0=固定值 1=随机值
	private String title;// 转盘文字
	private BigDecimal fixedMoney;// 固定金额
	private BigDecimal minMoney;// 随机最小值
	private BigDecimal maxMoney;// 随机最大值
	private Integer num;// 数量
	private BigDecimal rechargeMinMoney;// 充值最小值
	private BigDecimal rechargeMaxMoney;// 充值最大值

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public void setLsId(Integer lsId) {
		this.lsId = lsId;
	}

	public Integer getLsId() {
		return lsId;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setFixedMoney(BigDecimal fixedMoney) {
		this.fixedMoney = fixedMoney;
	}

	public BigDecimal getFixedMoney() {
		return fixedMoney;
	}

	public void setMinMoney(BigDecimal minMoney) {
		this.minMoney = minMoney;
	}

	public BigDecimal getMinMoney() {
		return minMoney;
	}

	public void setMaxMoney(BigDecimal maxMoney) {
		this.maxMoney = maxMoney;
	}

	public BigDecimal getMaxMoney() {
		return maxMoney;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getNum() {
		return num;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getRechargeMinMoney() {
		return rechargeMinMoney;
	}

	public void setRechargeMinMoney(BigDecimal rechargeMinMoney) {
		this.rechargeMinMoney = rechargeMinMoney;
	}

	public BigDecimal getRechargeMaxMoney() {
		return rechargeMaxMoney;
	}

	public void setRechargeMaxMoney(BigDecimal rechargeMaxMoney) {
		this.rechargeMaxMoney = rechargeMaxMoney;
	}
}
