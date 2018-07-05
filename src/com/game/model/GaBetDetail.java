package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

public class GaBetDetail{
	private Integer betDetailId;
	private Integer userId;//用户id
	private Integer betId;//玩法ID,用于计算赔率
	private Integer betOptionId;//投注项id ?
	private Integer sessionId;//比赛局ID
	private Date betTime;//投注时间
	private BigDecimal betMoney2;//投注金额(dto)
	private Integer betMoney;//投注金额
	private String winResult;//0未开奖  1中奖  2未中奖
	private BigDecimal winCash;//中奖金额
	private String betFlag;//暂时不用
	
	private String gameType;//一级类型Id
	//
	private String gameName;//一级名称
	private String playName;//比赛名称
	private String sessionNo;//暂不用
	private String betName;//玩法名称
	private BigDecimal betRate;//赔率 ?
	private String optionTitle;//具体投注项名字 例如：总和大，龙，总和单 ?
	private String room;//比赛局名称
	private BigDecimal payoff;//收益
	private BigDecimal paperMoney;//使用红包金额
	private String optionIds;//组合投注项的中文名字。?
	private String openResult; //开奖结果。
	private String betType; //投注类型 0=代购 1=合买
	private String orderNum; // 订单编号
	private String isAddNo; //是否追号 ?
	private String batchNum;//批次号，主要用来看是否属于同一追号
	private String isPrivacy;//隐私 0=完全公开 1=截止后公开 2=仅单人可看 3=完全保密
	
	private BigDecimal oneMoney;//第一个结果  中奖的金额
	private BigDecimal twoMoney;//第二个结果  中奖的金额
	private BigDecimal threeMoney;//第三个结果  中奖的金额
	private BigDecimal fourMoney;//第四个结果  中奖的金额
	private BigDecimal fiveMoney;//第五个结果  中奖的金额
	
	
	private String type;// 用户类型 哪种类型的用户进行了投注
	private String loginName;//用户名

	
	public GaBetDetail(){}
	
	// 竞猜记录
	public GaBetDetail(Integer betDetailId, Integer betOptionId, Integer sessionId, Date betTime,
			Integer betMoney, String winResult, BigDecimal winCash, String playName, String room,
			String optionTitle){
		this.betDetailId=betDetailId;
		this.betOptionId=betOptionId;
		this.sessionId=sessionId;
		this.betTime=betTime;
		this.betMoney=betMoney;
		this.winResult=winResult;
		this.winCash=winCash;
		this.playName=playName;
		this.room=room;
		this.optionTitle=optionTitle;
	}
	
	public String getOptionIds() {
		return optionIds;
	}
	public void setOptionIds(String optionIds) {
		this.optionIds = optionIds;
	}
	private String  countStatus;//统计状态 1已统计 0未统计
	public void setBetDetailId(Integer betDetailId){
	this.betDetailId=betDetailId;
	}
	public Integer getBetDetailId(){
		return betDetailId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setBetId(Integer betId){
	this.betId=betId;
	}
	public Integer getBetId(){
		return betId;
	}
	public void setBetOptionId(Integer betOptionId){
	this.betOptionId=betOptionId;
	}
	public Integer getBetOptionId(){
		return betOptionId;
	}
	public void setSessionId(Integer sessionId){
	this.sessionId=sessionId;
	}
	public Integer getSessionId(){
		return sessionId;
	}
	public void setBetTime(Date betTime){
	this.betTime=betTime;
	}
	public Date getBetTime(){
		return betTime;
	}
	public void setBetMoney(Integer betMoney){
	this.betMoney=betMoney;
	}
	public Integer getBetMoney(){
		return betMoney;
	}
	public void setWinResult(String winResult){
	this.winResult=winResult;
	}
	public String getWinResult(){
		return winResult;
	}
	public void setWinCash(BigDecimal winCash){
	this.winCash=winCash;
	}
	public BigDecimal getWinCash(){
		return winCash;
	}
	public void setBetFlag(String betFlag){
	this.betFlag=betFlag;
	}
	public String getBetFlag(){
		return betFlag;
	}
	public String getGameName() {
		return gameName;
	}
	public void setGameName(String gameName) {
		this.gameName = gameName;
	}
	public String getLoginName() {
		return loginName;
	}
	public BigDecimal getFiveMoney() {
		return fiveMoney;
	}
	public void setFiveMoney(BigDecimal fiveMoney) {
		this.fiveMoney = fiveMoney;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPlayName() {
		return playName;
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
	public void setPlayName(String playName) {
		this.playName = playName;
	}
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	public String getBetName() {
		return betName;
	}
	public void setBetName(String betName) {
		this.betName = betName;
	}
	public BigDecimal getBetRate() {
		return betRate;
	}
	public void setBetRate(BigDecimal betRate) {
		this.betRate = betRate;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public BigDecimal getPaperMoney() {
		return paperMoney;
	}
	public void setPaperMoney(BigDecimal paperMoney) {
		this.paperMoney = paperMoney;
	}
	public BigDecimal getPayoff() {
		return payoff;
	}
	public void setPayoff(BigDecimal payoff) {
		this.payoff = payoff;
	}
	public String getCountStatus() {
		return countStatus;
	}
	public void setCountStatus(String countStatus) {
		this.countStatus = countStatus;
	}
	/**
	 * @return the openResult
	 */
	public String getOpenResult() {
		return openResult;
	}
	/**
	 * @param openResult the openResult to set
	 */
	public void setOpenResult(String openResult) {
		this.openResult = openResult;
	}
	public BigDecimal getBetMoney2() {
		return betMoney2;
	}
	public void setBetMoney2(BigDecimal betMoney2) {
		this.betMoney2 = betMoney2;
	}
	public String getBetType() {
		return betType;
	}
	public void setBetType(String betType) {
		this.betType = betType;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public String getIsAddNo() {
		return isAddNo;
	}
	public void setIsAddNo(String isAddNo) {
		this.isAddNo = isAddNo;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getBatchNum() {
		return batchNum;
	}
	public void setBatchNum(String batchNum) {
		this.batchNum = batchNum;
	}
	public String getIsPrivacy() {
		return isPrivacy;
	}
	public void setIsPrivacy(String isPrivacy) {
		this.isPrivacy = isPrivacy;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
