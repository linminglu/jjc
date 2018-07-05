package com.xy.hk.sflhc.model.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.game.model.GaBetDetail;
import com.ram.model.User;

public class SflhcDTO {
    private String sessionNo;
    private Date startTime;
    private Date endTime;
    private BigDecimal totalPoint;
    private BigDecimal winCash;
    private BigDecimal payoff;
	private GaBetDetail gaBetDetail;
	private User user;
    public SflhcDTO(){
    	
    }
    
    public SflhcDTO(BigDecimal totalPoint,
			BigDecimal winCash,BigDecimal payoff){

		this.totalPoint = totalPoint;
		this.winCash = winCash;
		this.payoff = payoff;
	}
    
	public SflhcDTO(String sessionNo,BigDecimal totalPoint,
			BigDecimal winCash,Date startTime,Date endTime,BigDecimal payoff){

		this.sessionNo = sessionNo;
		this.totalPoint = totalPoint;
		this.winCash = winCash;
		this.startTime = startTime;
		this.endTime = endTime;
		this.payoff = payoff;
	}

	public SflhcDTO(GaBetDetail gaBetDetail, User user){
		this.gaBetDetail = gaBetDetail;
		this.user = user;
	}
	/**
	 * @return the sessionNo
	 */
	public String getSessionNo() {
		return sessionNo;
	}
	/**
	 * @param sessionNo the sessionNo to set
	 */
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}
	/**
	 * @return the endTime
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * @param endTime the endTime to set
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * @return the startTime
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * @return the totalPoint
	 */
	public BigDecimal getTotalPoint() {
		return totalPoint;
	}
	/**
	 * @param totalPoint the totalPoint to set
	 */
	public void setTotalPoint(BigDecimal totalPoint) {
		this.totalPoint = totalPoint;
	}
	/**
	 * @return the winCash
	 */
	public BigDecimal getWinCash() {
		return winCash;
	}
	/**
	 * @param winCash the winCash to set
	 */
	public void setWinCash(BigDecimal winCash) {
		this.winCash = winCash;
	}
	public BigDecimal getPayoff() {
		return payoff;
	}
	public void setPayoff(BigDecimal payoff) {
		this.payoff = payoff;
	}
	/**
	 * @return the gaBetDetail
	 */
	public GaBetDetail getGaBetDetail() {
		return gaBetDetail;
	}
	/**
	 * @param gaBetDetail the gaBetDetail to set
	 */
	public void setGaBetDetail(GaBetDetail gaBetDetail) {
		this.gaBetDetail = gaBetDetail;
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
