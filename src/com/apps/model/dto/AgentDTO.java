package com.apps.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;

public class AgentDTO {

	private String cashType;
	private BigDecimal cashMoney;
	private GaBetSponsor gaBetSponsor = new GaBetSponsor();
	private GaBetPart gaBetPart = new GaBetPart();
	private String userName;
	private String gameName;
	private String sponsorName;
	private String gameType;
	private BigDecimal payoff; //盈亏
	private BigDecimal betMoney2;//累计下注
	private Integer betMoney;//累计下注
	private BigDecimal winMoney; // 中奖金额
	private BigDecimal money; //余额
	private Integer number;
	private String userType; //用户类型
	private String sessionNo;//期号
	private Date startDate;
	private Date endDate;
	private String payType;//充值资金类型
	private String betOption;//投注项
	private String title;//
	private String openResult;//开奖数值
	private String winResult;// 开奖结果
	public AgentDTO(){
		
	}
	public AgentDTO(Integer number,String userType,BigDecimal money){
		this.number=number;
		this.userType=userType;
		this.money = money;
	}
	public AgentDTO(String cashType,BigDecimal cashMoney){
		this.cashType=cashType;
		this.cashMoney=cashMoney;
	}
	
	public AgentDTO(GaBetPart gaBetPart,GaBetSponsor gaBetSponsor,String userName,String sponsorName){
		this.gaBetPart = gaBetPart;
		this.gaBetSponsor = gaBetSponsor;
		this.userName = userName;
		this.sponsorName = sponsorName;
	}
	
	public AgentDTO(GaBetPart gaBetPart,GaBetSponsor gaBetSponsor,String userName,String sponsorName,
			String betOption,String title,String winResult, BigDecimal winMoney){
		this.gaBetPart = gaBetPart;
		this.gaBetSponsor = gaBetSponsor;
		this.userName = userName;
		this.sponsorName = sponsorName;
		this.betOption = betOption;
		this.title = title;
		this.winResult = winResult;
		this.winMoney = winMoney;
	}
	
	public AgentDTO(GaBetPart gaBetPart,GaBetSponsor gaBetSponsor,String userName,String sponsorName,
			String betOption,String title,String winResult, BigDecimal winMoney,BigDecimal betMoney){
		this.gaBetPart = gaBetPart;
		this.gaBetSponsor = gaBetSponsor;
		this.userName = userName;
		this.sponsorName = sponsorName;
		this.betOption = betOption;
		this.title = title;
		this.winResult = winResult;
		this.winMoney = winMoney;
		this.betMoney = betMoney!=null?betMoney.intValue():0;
	}
	
	public AgentDTO(String gameType,BigDecimal betMoney2, BigDecimal winMoney,BigDecimal payoff) {
		this.gameType=gameType;
		this.betMoney2=betMoney2;
		this.winMoney=winMoney;
		this.payoff=payoff;
	}
	public AgentDTO(String gameName,String gameType,BigDecimal betMoney2, BigDecimal winMoney,BigDecimal payoff) {
		this.gameName=gameName;
		this.gameType=gameType;
		this.betMoney2=betMoney2;
		this.winMoney=winMoney;
		this.payoff=payoff;
	}
	public AgentDTO(String gameName,String gameType,Integer betMoney, BigDecimal winMoney,BigDecimal payoff) {
		this.gameName=gameName;
		this.gameType=gameType;
		this.betMoney=betMoney;
		this.betMoney2=new BigDecimal(betMoney);
		this.winMoney=winMoney;
		this.payoff=payoff;
	}
	public AgentDTO(String sessionNo,Date startDate,Date endDate,BigDecimal betMoney2,
			BigDecimal winMoney,BigDecimal payoff){
		this.sessionNo = sessionNo;
		this.betMoney2 = betMoney2;
		this.winMoney = winMoney;
		this.startDate = startDate;
		this.endDate = endDate;
		this.payoff = payoff;
	}
	public Integer getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(Integer betMoney) {
		this.betMoney = betMoney;
	}

	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getCashType() {
		return cashType;
	}
	public void setCashType(String cashType) {
		this.cashType = cashType;
	}
	public BigDecimal getCashMoney() {
		return cashMoney;
	}
	public void setCashMoney(BigDecimal cashMoney) {
		this.cashMoney = cashMoney;
	}
	public GaBetSponsor getGaBetSponsor() {
		return gaBetSponsor;
	}
	public void setGaBetSponsor(GaBetSponsor gaBetSponsor) {
		this.gaBetSponsor = gaBetSponsor;
	}
	public GaBetPart getGaBetPart() {
		return gaBetPart;
	}
	public void setGaBetPart(GaBetPart gaBetPart) {
		this.gaBetPart = gaBetPart;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getSponsorName() {
		return sponsorName;
	}
	public void setSponsorName(String sponsorName) {
		this.sponsorName = sponsorName;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getWinResult() {
		return winResult;
	}
	public void setWinResult(String winResult) {
		this.winResult = winResult;
	}
	public BigDecimal getPayoff() {
		return payoff;
	}
	public void setPayoff(BigDecimal payoff) {
		this.payoff = payoff;
	}
	public BigDecimal getBetMoney2() {
		return betMoney2;
	}
	public void setBetMoney2(BigDecimal betMoney2) {
		this.betMoney2 = betMoney2;
	}
	public BigDecimal getWinMoney() {
		return winMoney;
	}
	public void setWinMoney(BigDecimal winMoney) {
		this.winMoney = winMoney;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getBetOption() {
		return betOption;
	}
	public void setBetOption(String betOption) {
		this.betOption = betOption;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getOpenResult() {
		return openResult;
	}
	public void setOpenResult(String openResult) {
		this.openResult = openResult;
	}
	
}
