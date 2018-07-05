package com.game.model;

/**
 * 投注订单关联表
 * @author admin
 *
 */
public class BetOrderRl {
	private Integer rid;
	private Integer betId;
	private Integer orderId;
	
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	public Integer getBetId() {
		return betId;
	}
	public void setBetId(Integer betId) {
		this.betId = betId;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}		
	
}
