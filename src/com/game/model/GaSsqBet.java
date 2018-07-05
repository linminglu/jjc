package com.game.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * GaSsqBet 实体类
    * author:	ch
    * createDate:	2016-03-11 13:33:56
    */ 


public class GaSsqBet{
	private Integer betId;
	private Integer userId;
	private Integer sessionId;
	private Integer totalNum;//投注注数
	private BigDecimal totalPoint;//投注金额
	private BigDecimal winCash;//奖金
	private Date betTime;//投注时间
	private String redBall;//红球
	private String blueBall;//蓝球
	private String redBile;//红色胆球
	private String openStatus;//0是未开奖 1是中奖  2是未中奖
	private String betType;//投注类型  0是单式 1是复式 2是胆拖
	private Integer multiple;//投注倍数 
	public void setBetId(Integer betId){
	this.betId=betId;
	}
	public Integer getBetId(){
		return betId;
	}
	public void setUserId(Integer userId){
	this.userId=userId;
	}
	public Integer getUserId(){
		return userId;
	}
	public void setSessionId(Integer sessionId){
	this.sessionId=sessionId;
	}
	public Integer getSessionId(){
		return sessionId;
	}
	public void setTotalNum(Integer totalNum){
	this.totalNum=totalNum;
	}
	public Integer getTotalNum(){
		return totalNum;
	}
	public void setTotalPoint(BigDecimal totalPoint){
	this.totalPoint=totalPoint;
	}
	public BigDecimal getTotalPoint(){
		return totalPoint;
	}
	public void setWinCash(BigDecimal winCash){
	this.winCash=winCash;
	}
	public BigDecimal getWinCash(){
		return winCash;
	}
	public void setBetTime(Date betTime){
	this.betTime=betTime;
	}
	public Date getBetTime(){
		return betTime;
	}
	public String getRedBall() {
		return redBall;
	}
	public void setRedBall(String redBall) {
		this.redBall = redBall;
	}
	public String getBlueBall() {
		return blueBall;
	}
	public void setBlueBall(String blueBall) {
		this.blueBall = blueBall;
	}
	public String getRedBile() {
		return redBile;
	}
	public void setRedBile(String redBile) {
		this.redBile = redBile;
	}
	public String getOpenStatus() {
		return openStatus;
	}
	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}
	public String getBetType() {
		return betType;
	}
	public void setBetType(String betType) {
		this.betType = betType;
	}
	public Integer getMultiple() {
		return multiple;
	}
	public void setMultiple(Integer multiple) {
		this.multiple = multiple;
	}
	
}
