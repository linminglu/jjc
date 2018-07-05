package com.gf.bjkl8.model;
import java.util.Date;
import java.math.BigDecimal;

   /**
    * BjLu28GaBet 实体类  暂时不用
    * author:	ch
    * createDate:	2017-03-23 18:32:46
    */ 


public class GfBjKl8GaBet{
	private Integer betId;
	private Integer userId;//用户id
	private Integer sessionId;//
	private Integer totalNum;
	private BigDecimal totalPoint;
	private BigDecimal winCash;
	private Date betTime;
	private Integer bigSmaOddDual; //大小单双
	private Integer combination; // 组合
	private Integer extreme; // 极值
	private Integer code; // 特码
	private Integer color; // 波色
	private Integer leopard; // 豹子
	private String sessionNo;
	
	public Integer getBigSmaOddDual() {
		return bigSmaOddDual;
	}
	public void setBigSmaOddDual(Integer bigSmaOddDual) {
		this.bigSmaOddDual = bigSmaOddDual;
	}
	public Integer getCombination() {
		return combination;
	}
	public void setCombination(Integer combination) {
		this.combination = combination;
	}
	public Integer getExtreme() {
		return extreme;
	}
	public void setExtreme(Integer extreme) {
		this.extreme = extreme;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Integer getColor() {
		return color;
	}
	public void setColor(Integer color) {
		this.color = color;
	}
	public Integer getLeopard() {
		return leopard;
	}
	public void setLeopard(Integer leopard) {
		this.leopard = leopard;
	}
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
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
}
