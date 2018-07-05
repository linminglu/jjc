package com.game.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.game.model.UserBetCount;
import com.ram.model.User;

public class GaDTO {
	private Integer totalPoint;//投注总数量
	private BigDecimal winCash;//中奖总钱数
	private BigDecimal paperMoney;
	private Integer optionType;
	private String title;
	private String agentName;
	private BigDecimal totalRecharge;//总投注
	private BigDecimal totalDrawMoney;//总退款
	private BigDecimal totalBet;//总投注
	private BigDecimal totalWin;//总中奖
	private Date createTime;//统计时间
	private User user = new User();
	private Integer userId;
	private String userName;
	private String cashType;
	private Integer number;
	private Integer betMoney;
	private BigDecimal betMoneyBig;
	private Integer agentId;
	private String isBetBack;// 每个用户打码返水开关
	private UserBetCount userBetCount = new UserBetCount();
	
	private BigDecimal oneMoney;//第一个结果  中奖的金额
	private BigDecimal twoMoney;//第二个结果  中奖的金额
	private BigDecimal threeMoney;//第三个结果  中奖的金额
	private BigDecimal fourMoney;//第四个结果  中奖的金额
	private BigDecimal fiveMoney;//第五个结果  中奖的金额
	
	private BigDecimal countMoney;//盈亏（结算金额） 后台盈亏=下注-中奖-返水-活动-佣金

	public GaDTO(){
		
	}
	public GaDTO(BigDecimal countMoney,Integer userId){
		this.countMoney = countMoney;
		this.userId = userId;
	}
	
	public GaDTO(String isBetBack,UserBetCount userBetCount){
		this.isBetBack = isBetBack;
		this.userBetCount = userBetCount;
	}
	
	public GaDTO(BigDecimal oneMoney,BigDecimal twoMoney,BigDecimal threeMoney,BigDecimal fourMoney,BigDecimal fiveMoney){
		this.oneMoney=oneMoney;
		this.twoMoney=twoMoney;
		this.threeMoney=threeMoney;
		this.fourMoney=fourMoney;
		this.fiveMoney=fiveMoney;
	}
	
	public GaDTO(Integer optionType,String title){
		this.optionType=optionType;
		this.title=title;
	}
	
	public GaDTO(User user,Integer betMoney){
		this.user=user;
		this.betMoney=betMoney;
	}
	
	public GaDTO(Integer userId,Integer betMoney,Integer agentId){
		this.userId=userId;
		this.betMoney=betMoney;
		this.agentId=agentId;
	}
	
	public GaDTO(Integer userId,BigDecimal betMoneyBig,Integer agentId){
		this.userId=userId;
		this.betMoneyBig=betMoneyBig;
		this.agentId=agentId;
	}
	
	public GaDTO(Integer userId,String userName,Integer number){
		this.userId=userId;
		this.userName=userName;
		this.number = number;
	}
	public GaDTO(Integer userId,String userName,Integer number,BigDecimal winCash,BigDecimal totalBet,BigDecimal paperMoney){
		this.userId=userId;
		this.userName=userName;
		this.number = number;
		this.winCash = winCash;
		this.totalBet = totalBet;
		this.paperMoney = paperMoney;
	}
	public GaDTO(Integer userId,BigDecimal paperMoney,String cashType){
		this.userId=userId;
		this.paperMoney=paperMoney;
		this.cashType = cashType;
	}
	public GaDTO(Integer totalPoint,BigDecimal winCash,BigDecimal paperMoney){
		this.totalPoint=totalPoint;
		this.winCash=winCash;
		this.paperMoney=paperMoney;
	}
	public GaDTO(BigDecimal totalRecharge,BigDecimal totalDrawMoney,BigDecimal totalBet,BigDecimal totalWin,
			Date createTime,User user){
		this.totalRecharge=totalRecharge;
		this.totalDrawMoney=totalDrawMoney;
		this.totalBet=totalBet;
		this.totalWin=totalWin;
		this.createTime =createTime;
		this.user = user;
	}
	public GaDTO(BigDecimal totalRecharge,BigDecimal totalDrawMoney,BigDecimal totalBet,BigDecimal totalWin,
			Date createTime,Integer userId,String userName){
		this.totalRecharge=totalRecharge;
		this.totalDrawMoney=totalDrawMoney;
		this.totalBet=totalBet;
		this.totalWin=totalWin;
		this.createTime =createTime;
		this.userId = userId;
		this.userName = userName;
	}
	
	public GaDTO(User user,Integer userId,Integer betMoney){
		this.userId=userId;
		this.betMoney=betMoney;
		this.user = user;
	}
	
	public Integer getTotalPoint() {
		return totalPoint;
	}
	public void setTotalPoint(Integer totalPoint) {
		this.totalPoint = totalPoint;
	}
	public BigDecimal getWinCash() {
		return winCash;
	}
	public String getIsBetBack() {
		return isBetBack;
	}

	public void setIsBetBack(String isBetBack) {
		this.isBetBack = isBetBack;
	}

	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}
	public BigDecimal getPaperMoney() {
		return paperMoney;
	}
	public void setPaperMoney(BigDecimal paperMoney) {
		this.paperMoney = paperMoney;
	}
	public Integer getOptionType() {
		return optionType;
	}
	public void setOptionType(Integer optionType) {
		this.optionType = optionType;
	}
	public BigDecimal getOneMoney() {
		return oneMoney;
	}
	public void setOneMoney(BigDecimal oneMoney) {
		this.oneMoney = oneMoney;
	}
	public BigDecimal getTwoMoney() {
		return twoMoney;
	}
	public void setTwoMoney(BigDecimal twoMoney) {
		this.twoMoney = twoMoney;
	}
	public BigDecimal getThreeMoney() {
		return threeMoney;
	}
	public void setThreeMoney(BigDecimal threeMoney) {
		this.threeMoney = threeMoney;
	}
	public BigDecimal getFourMoney() {
		return fourMoney;
	}
	public void setFourMoney(BigDecimal fourMoney) {
		this.fourMoney = fourMoney;
	}
	public BigDecimal getFiveMoney() {
		return fiveMoney;
	}
	public void setFiveMoney(BigDecimal fiveMoney) {
		this.fiveMoney = fiveMoney;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public String getAgentName() {
		return agentName;
	}
	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
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
	public String getCashType() {
		return cashType;
	}
	public void setCashType(String cashType) {
		this.cashType = cashType;
	}
	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public Integer getBetMoney() {
		return betMoney;
	}
	public void setBetMoney(Integer betMoney) {
		this.betMoney = betMoney;
	}
	public UserBetCount getUserBetCount() {
		return userBetCount;
	}
	public void setUserBetCount(UserBetCount userBetCount) {
		this.userBetCount = userBetCount;
	}
	public Integer getAgentId() {
		return agentId;
	}
	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public BigDecimal getCountMoney() {
		return countMoney;
	}

	public void setCountMoney(BigDecimal countMoney) {
		this.countMoney = countMoney;
	}
	public BigDecimal getBetMoneyBig() {
		return betMoneyBig;
	}
	public void setBetMoneyBig(BigDecimal betMoneyBig) {
		this.betMoneyBig = betMoneyBig;
	}
	
}
