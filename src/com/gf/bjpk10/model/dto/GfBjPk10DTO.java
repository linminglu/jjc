package com.gf.bjpk10.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.game.model.GaBetDetail;
import com.game.model.GaBetSponsor;
import com.game.model.GaBetSponsorDetail;
import com.ram.model.User;


public class GfBjPk10DTO {

	private String sessionNo;
	private BigDecimal totalPoint;
	private BigDecimal winCash;
	private BigDecimal payoff;
	private Date startTime;
	private Date endTime;
	private GaBetSponsor gaSponsor;
	private GaBetSponsorDetail gaBetSponsorDetail;
	private User user;
	public GfBjPk10DTO(){
		
	}
	
	public GfBjPk10DTO(GaBetSponsor gaSponsor,GaBetSponsorDetail gaBetSponsorDetail, User user){
		this.setGaSponsor(gaSponsor);
		this.setGaBetSponsorDetail(gaBetSponsorDetail);
		this.setUser(user);
	}

	public GfBjPk10DTO(BigDecimal totalPoint,
			BigDecimal winCash,BigDecimal payoff){
		this.totalPoint = totalPoint;
		this.winCash = winCash;
		this.payoff = payoff;
	}
	
	public GfBjPk10DTO(String sessionNo,BigDecimal totalPoint,
			BigDecimal winCash,Date startTime,Date endTime,BigDecimal payoff){
		this.sessionNo = sessionNo;
		this.totalPoint = totalPoint;
		this.winCash = winCash;
		this.startTime = startTime;
		this.endTime = endTime;
		this.payoff = payoff;
	}

	public String getSessionNo() {
		return sessionNo;
	}

	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}

	public BigDecimal getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(BigDecimal totalPoint) {
		this.totalPoint = totalPoint;
	}

	public BigDecimal getWinCash() {
		return winCash;
	}

	
	public GaBetSponsor getGaSponsor() {
		return gaSponsor;
	}

	public void setGaSponsor(GaBetSponsor gaSponsor) {
		this.gaSponsor = gaSponsor;
	}

	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}

	public BigDecimal getPayoff() {
		return payoff;
	}

	public void setPayoff(BigDecimal payoff) {
		this.payoff = payoff;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public GaBetSponsorDetail getGaBetSponsorDetail() {
		return gaBetSponsorDetail;
	}

	public void setGaBetSponsorDetail(GaBetSponsorDetail gaBetSponsorDetail) {
		this.gaBetSponsorDetail = gaBetSponsorDetail;
	}

	/**
	 * @return the user
	 */
	public User getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(User user) {
		this.user = user;
	}
}
