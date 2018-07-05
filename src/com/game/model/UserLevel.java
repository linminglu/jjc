package com.game.model;

import java.math.BigDecimal;

/**
 * 用户等级
 * @author dell-8c350w1
 *
 */
public class UserLevel {
	private Integer id;
	private String levelName;//等级的名称
	private BigDecimal point;//成长积分
	private BigDecimal award;//跳级奖励
	private String level;//等级
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public BigDecimal getPoint() {
		return point;
	}
	public void setPoint(BigDecimal point) {
		this.point = point;
	}
	public BigDecimal getAward() {
		return award;
	}
	public void setAward(BigDecimal award) {
		this.award = award;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
}
