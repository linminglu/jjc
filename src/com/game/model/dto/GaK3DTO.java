package com.game.model.dto;

import java.math.BigDecimal;

import com.game.model.GaK3Bet;
import com.game.model.GaK3BetDetail;
import com.game.model.GaK3Session;
import com.game.model.GaOrder;

public class GaK3DTO {
	
	private GaK3Session gaK3Session = new GaK3Session();
	private GaK3Bet gaK3Bet = new GaK3Bet();
	private GaK3BetDetail gaK3BetDetail = new GaK3BetDetail();
	private String loginName;
	private String userName;
	private String sessionNo;
	private String totalNum;
	private Integer sessionId;
	private GaOrder gaOrder = new GaOrder();
	private String betValue;
	private BigDecimal totalPoint;
	
	public GaK3DTO(){}
	
	
	public GaK3DTO(GaK3Bet gaK3Bet,GaK3Session gaK3Session,String loginName){
		this.gaK3Bet = gaK3Bet;
		this.gaK3Session = gaK3Session;
		this.loginName = loginName;
	}
	
	public GaK3DTO(GaK3BetDetail gaK3BetDetail,GaK3Session gaK3Session){
		this.gaK3BetDetail = gaK3BetDetail;
		this.gaK3Session = gaK3Session;
	}
	
	public GaK3DTO(GaK3Bet gaK3Bet,GaK3Session gaK3Session){
		this.gaK3Bet = gaK3Bet;
		this.gaK3Session = gaK3Session;
	}
	
	public GaK3DTO(String sessionNo,GaK3Bet gaK3Bet){
		this.sessionNo = sessionNo;
		this.gaK3Bet = gaK3Bet;
	}
	
	public GaK3DTO(GaOrder gaOrder,String loginName,String userName){
		this.gaOrder = gaOrder;
		this.loginName = loginName;
		this.userName = userName;
	}
	
	public GaK3DTO(String sessionNo,String betValue,BigDecimal totalPoint){
		this.sessionNo = sessionNo;
		this.betValue = betValue;
		this.totalPoint = totalPoint;
	}

	public GaK3Session getGaK3Session() {
		return gaK3Session;
	}


	public void setGaK3Session(GaK3Session gaK3Session) {
		this.gaK3Session = gaK3Session;
	}


	public GaK3Bet getGaK3Bet() {
		return gaK3Bet;
	}


	public void setGaK3Bet(GaK3Bet gaK3Bet) {
		this.gaK3Bet = gaK3Bet;
	}


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getSessionNo() {
		return sessionNo;
	}


	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}


	public String getTotalNum() {
		return totalNum;
	}


	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}


	public Integer getSessionId() {
		return sessionId;
	}


	public void setSessionId(Integer sessionId) {
		this.sessionId = sessionId;
	}


	public GaK3BetDetail getGaK3BetDetail() {
		return gaK3BetDetail;
	}


	public void setGaK3BetDetail(GaK3BetDetail gaK3BetDetail) {
		this.gaK3BetDetail = gaK3BetDetail;
	}


	public GaOrder getGaOrder() {
		return gaOrder;
	}


	public void setGaOrder(GaOrder gaOrder) {
		this.gaOrder = gaOrder;
	}


	public String getBetValue() {
		return betValue;
	}


	public void setBetValue(String betValue) {
		this.betValue = betValue;
	}


	public BigDecimal getTotalPoint() {
		return totalPoint;
	}


	public void setTotalPoint(BigDecimal totalPoint) {
		this.totalPoint = totalPoint;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
	
}
