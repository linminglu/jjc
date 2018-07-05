package com.game.model;
import java.util.Date;
import java.math.BigDecimal;
/**
 * 用户每日数据统计
 * @author admin
 *
 */

public class DailyStatistics{
	private Integer id;
	private Integer userId;
	private Integer agentId;//代理商id
	private String userName;
	private String loginName;
	private BigDecimal totalRecharge;//每日充值
	private BigDecimal totalDrawMoney;//每日提现
	private BigDecimal totalBet;//每日投注
	private BigDecimal totalWin;//每日中奖
	private Date createTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public BigDecimal getTotalRecharge() {
		return totalRecharge;
	}
	public void setTotalRecharge(BigDecimal totalRecharge) {
		this.totalRecharge = totalRecharge;
	}
	public BigDecimal getTotalDrawMoney() {
		return totalDrawMoney;
	}
	public void setTotalDrawMoney(BigDecimal totalDrawMoney) {
		this.totalDrawMoney = totalDrawMoney;
	}
	public BigDecimal getTotalBet() {
		return totalBet;
	}
	public void setTotalBet(BigDecimal totalBet) {
		this.totalBet = totalBet;
	}
	public BigDecimal getTotalWin() {
		return totalWin;
	}
	public void setTotalWin(BigDecimal totalWin) {
		this.totalWin = totalWin;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
