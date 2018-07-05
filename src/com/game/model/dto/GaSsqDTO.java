package com.game.model.dto;

import java.math.BigDecimal;

import com.game.model.GaOrder;
import com.game.model.GaSsqBet;
import com.game.model.GaSsqBetDetail;
import com.game.model.GaSsqSession;

public class GaSsqDTO {
	
	private GaSsqSession gaSsqSession = new GaSsqSession();
	private GaSsqBet gaSsqBet = new GaSsqBet();
	private GaSsqBetDetail gaSsqBetDetail = new GaSsqBetDetail();
	private String loginName;
	private String userName;
	private String sessionNo;
	private String totalNum;
	private Integer sessionId;
	private GaOrder gaOrder = new GaOrder();
	private String redBall;
	private String blueBall;
	private BigDecimal totalPoint;
	public GaSsqDTO(){}
	
	
	public GaSsqDTO(GaSsqBet gaSsqBet,GaSsqSession gaSsqSession,String loginName){
		this.gaSsqBet = gaSsqBet;
		this.gaSsqSession = gaSsqSession;
		this.loginName = loginName;
	}
	
	public GaSsqDTO(String  sessionNo,GaSsqBet gaSsqBet){
		this.sessionNo = sessionNo;
		this.gaSsqBet = gaSsqBet;
	}
	
	public GaSsqDTO(GaSsqBetDetail gaSsqBetDetail,GaSsqSession gaSsqSession){
		this.gaSsqBetDetail = gaSsqBetDetail;
		this.gaSsqSession = gaSsqSession;
	}
	
	public GaSsqDTO(GaSsqBet gaSsqBet,GaSsqSession gaSsqSession){
		this.gaSsqBet = gaSsqBet;
		this.gaSsqSession = gaSsqSession;
	}
	
	public GaSsqDTO(GaOrder gaOrder,String loginName,String userName){
		this.gaOrder = gaOrder;
		this.loginName = loginName;
		this.userName = userName;
	}
	
	public GaSsqDTO(String sessionNo,String redBall,String blueBall,BigDecimal totalPoint){
		this.sessionNo = sessionNo;
		this.redBall = redBall;
		this.blueBall = blueBall;
		this.totalPoint = totalPoint;
	}

	public GaSsqSession getGaSsqSession() {
		return gaSsqSession;
	}


	public void setGaSsqSession(GaSsqSession gaSsqSession) {
		this.gaSsqSession = gaSsqSession;
	}


	public GaSsqBet getGaSsqBet() {
		return gaSsqBet;
	}


	public void setGaSsqBet(GaSsqBet gaSsqBet) {
		this.gaSsqBet = gaSsqBet;
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


	public GaSsqBetDetail getGaSsqBetDetail() {
		return gaSsqBetDetail;
	}


	public void setGaSsqBetDetail(GaSsqBetDetail gaSsqBetDetail) {
		this.gaSsqBetDetail = gaSsqBetDetail;
	}


	public GaOrder getGaOrder() {
		return gaOrder;
	}


	public void setGaOrder(GaOrder gaOrder) {
		this.gaOrder = gaOrder;
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
