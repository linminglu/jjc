package com.game.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 投注发起表
 */
public class GaBetSponsor {
	private Integer jointId;
	private Integer userId; //发起人Id
	private String orderNum;//编号（订单号）
	private Integer sessionId;//
	private Date betTime;//投注时间
	private BigDecimal money;//订单总方案金额
	private BigDecimal schedule; //进度
	private Integer num; //总份额
	private Integer betNum; //总注数
	private Integer restNum; //剩余份额
	private Integer guaranteedNum;  //保底份数
	private String winResult;// 状态0=未满员 1=无效 2=未开奖 3=开奖中  4=中奖 5=不中奖
	private String isGuarantee; // 是否保底
	private BigDecimal winCash;//中奖金额
	private String betFlag;//是否生效
	private String gameType;//游戏类型 14=双色球
	private String gameName;//游戏名字
	private String sessionNo;//期号
	private Integer multiple; // 购买倍数
	private String betType; //投注类型 0=代购 1=合买
	private String isAddNo; //是否追号
	private String isWinStop; //是否中奖后停止
	private String batchNum;//批次号，主要用来看是否属于同一追号
	private String isPrivacy;//隐私 0=完全公开 1=截止后公开 2=仅单人可看 3=完全保密
	private String openResult; //开奖号码
	private Date openTime;//开奖时间
	private BigDecimal winPoint;//中奖积分
	private BigDecimal guaranteedMoney;//保底金额
	private BigDecimal preMoney;//每份金额
	
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
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	public String getGameType() {
		return gameType;
	}
	public void setGameType(String gameType) {
		this.gameType = gameType;
	}
	public Integer getJointId() {
		return jointId;
	}
	public void setJointId(Integer jointId) {
		this.jointId = jointId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public BigDecimal getSchedule() {
		return schedule;
	}
	public void setSchedule(BigDecimal schedule) {
		this.schedule = schedule;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getRestNum() {
		return restNum;
	}
	public void setRestNum(Integer restNum) {
		this.restNum = restNum;
	}
	public Integer getGuaranteedNum() {
		return guaranteedNum;
	}
	public void setGuaranteedNum(Integer guaranteedNum) {
		this.guaranteedNum = guaranteedNum;
	}
	public String getIsGuarantee() {
		return isGuarantee;
	}
	public void setIsGuarantee(String isGuarantee) {
		this.isGuarantee = isGuarantee;
	}
	public Integer getMultiple() {
		return multiple;
	}
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}
	/**
	 * @return the betNum
	 */
	public Integer getBetNum() {
		return betNum;
	}
	/**
	 * @param betNum the betNum to set
	 */
	public void setBetNum(Integer betNum) {
		this.betNum = betNum;
	}
	public String getBetType() {
		return betType;
	}
	public void setBetType(String betType) {
		this.betType = betType;
	}
	public String getIsAddNo() {
		return isAddNo;
	}
	public void setIsAddNo(String isAddNo) {
		this.isAddNo = isAddNo;
	}
	public String getIsWinStop() {
		return isWinStop;
	}
	public void setIsWinStop(String isWinStop) {
		this.isWinStop = isWinStop;
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
	public String getOpenResult() {
		return openResult;
	}
	public void setOpenResult(String openResult) {
		this.openResult = openResult;
	}
	public Date getOpenTime() {
		return openTime;
	}
	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}
	public BigDecimal getWinPoint() {
		return winPoint;
	}
	public void setWinPoint(BigDecimal winPoint) {
		this.winPoint = winPoint;
	}
	public BigDecimal getGuaranteedMoney() {
		return guaranteedMoney;
	}
	public void setGuaranteedMoney(BigDecimal guaranteedMoney) {
		this.guaranteedMoney = guaranteedMoney;
	}
	public BigDecimal getPreMoney() {
		return preMoney;
	}
	public void setPreMoney(BigDecimal preMoney) {
		this.preMoney = preMoney;
	}
}
