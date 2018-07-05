package com.ram.model.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户盈亏统计DTO
 * @author Administrator
 *
 */
public class UserMoneyDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public Integer userId;//用户Id
	public String userName;// 用户名
	public BigDecimal totalRechage;// 充值
	public BigDecimal totalDrawMoney;//提现
	public BigDecimal bet; //总投注
	public BigDecimal win;//中奖彩派
	public BigDecimal fanshui;//返水
	public BigDecimal huodong;//活动
	public BigDecimal commission;//佣金
	public BigDecimal totalPayoff; //盈利
	public Integer memberNum;//下线会员
	
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public BigDecimal getTotalRechage() {
		return totalRechage;
	}
	public void setTotalRechage(BigDecimal totalRechage) {
		this.totalRechage = totalRechage;
	}
	public BigDecimal getTotalDrawMoney() {
		return totalDrawMoney;
	}
	public void setTotalDrawMoney(BigDecimal totalDrawMoney) {
		this.totalDrawMoney = totalDrawMoney;
	}
	public BigDecimal getBet() {
		return bet;
	}
	public void setBet(BigDecimal bet) {
		this.bet = bet;
	}
	public BigDecimal getWin() {
		return win;
	}
	public void setWin(BigDecimal win) {
		this.win = win;
	}
	public BigDecimal getFanshui() {
		return fanshui;
	}
	public void setFanshui(BigDecimal fanshui) {
		this.fanshui = fanshui;
	}
	public BigDecimal getHuodong() {
		return huodong;
	}
	public void setHuodong(BigDecimal huodong) {
		this.huodong = huodong;
	}
	public BigDecimal getCommission() {
		return commission;
	}
	public void setCommission(BigDecimal commission) {
		this.commission = commission;
	}
	public BigDecimal getTotalPayoff() {
		return totalPayoff;
	}
	public void setTotalPayoff(BigDecimal totalPayoff) {
		this.totalPayoff = totalPayoff;
	}
	public Integer getMemberNum() {
		return memberNum;
	}
	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

}
