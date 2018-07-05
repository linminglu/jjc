package com.game.model.dto;

import java.math.BigDecimal;

import com.game.model.GaHhmfBet;
import com.game.model.GaHhmfBetDetail;
import com.game.model.GaHhmfSession;

public class GaHhmfDTO {
	
	private GaHhmfSession gaHhmfSession = new GaHhmfSession();
	private GaHhmfBet gaHhmfBet = new GaHhmfBet();
	private GaHhmfBetDetail gaHhmfBetDetail = new GaHhmfBetDetail();
	private String loginName;
	private String sessionNo;
	private String totalNum;
	private Integer sessionId;
	
	private String openType;
	private Integer num;
	
	private String betType;
	private Integer betPoints;
	private String openResult;
	private String openStatus;
	private BigDecimal bonus;
	
	
	public GaHhmfDTO(){}
	
	
	public GaHhmfDTO(GaHhmfBet gaHhmfBet,GaHhmfSession gaHhmfSession,String loginName){
		this.gaHhmfBet = gaHhmfBet;
		this.gaHhmfSession = gaHhmfSession;
		this.loginName = loginName;
	}
	
	public GaHhmfDTO(GaHhmfBetDetail gaHhmfBetDetail,GaHhmfSession gaHhmfSession){
		this.gaHhmfBetDetail = gaHhmfBetDetail;
		this.gaHhmfSession = gaHhmfSession;
	}
	
	public GaHhmfDTO(GaHhmfBet gaHhmfBet,GaHhmfSession gaHhmfSession){
		this.gaHhmfBet = gaHhmfBet;
		this.gaHhmfSession = gaHhmfSession;
	}
	public GaHhmfDTO(String openType,Integer num){
		this.openType = openType;
		this.num = num;
	}

	public GaHhmfDTO(Integer sessionId,String sessionNo,String betType,Integer betPoints,String openStatus,BigDecimal bonus){
		this.sessionId = sessionId;
		this.betType = betType;
		this.sessionNo = sessionNo;
		this.betPoints = betPoints;
		this.openStatus = openStatus;
		this.bonus = bonus;
	}
	
	public GaHhmfDTO(Integer sessionId,String openResult,String sessionNo){
		this.sessionId = sessionId;
		this.openResult = openResult;
		this.sessionNo = sessionNo;
	}
	
	public GaHhmfSession getGaHhmfSession() {
		return gaHhmfSession;
	}

	public void setGaHhmfSession(GaHhmfSession gaHhmfSession) {
		this.gaHhmfSession = gaHhmfSession;
	}

	public GaHhmfBet getGaHhmfBet() {
		return gaHhmfBet;
	}

	public void setGaHhmfBet(GaHhmfBet gaHhmfBet) {
		this.gaHhmfBet = gaHhmfBet;
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

	public GaHhmfBetDetail getGaHhmfBetDetail() {
		return gaHhmfBetDetail;
	}

	public void setGaHhmfBetDetail(GaHhmfBetDetail gaHhmfBetDetail) {
		this.gaHhmfBetDetail = gaHhmfBetDetail;
	}

	public String getOpenType() {
		return openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getOpenResult() {
		return openResult;
	}


	public void setOpenResult(String openResult) {
		this.openResult = openResult;
	}

	public String getOpenStatus() {
		return openStatus;
	}

	public void setOpenStatus(String openStatus) {
		this.openStatus = openStatus;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public Integer getBetPoints() {
		return betPoints;
	}

	public void setBetPoints(Integer betPoints) {
		this.betPoints = betPoints;
	}


	public String getBetType() {
		return betType;
	}


	public void setBetType(String betType) {
		this.betType = betType;
	}
		
}
