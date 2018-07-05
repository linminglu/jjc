package com.card.model.dto;


public class UserCardDTO {
	
	private Boolean canPayByCard;
	
	private Double cardRemainAmount;// 卡可用余额
	private Double payCardAmount;//卡支付金额
	private Double payMoneyAmount;//现金支付金额
	
	private Boolean needPayMoney;
	
	public Boolean getCanPayByCard() {
		return canPayByCard;
	}
	public void setCanPayByCard(Boolean canPayByCard) {
		this.canPayByCard = canPayByCard;
	}
	
	public Double getCardRemainAmount() {
		return cardRemainAmount;
	}
	public void setCardRemainAmount(Double cardRemainAmount) {
		this.cardRemainAmount = cardRemainAmount;
	}
	public Double getPayCardAmount() {
		return payCardAmount;
	}
	public void setPayCardAmount(Double payCardAmount) {
		this.payCardAmount = payCardAmount;
	}
	public Double getPayMoneyAmount() {
		return payMoneyAmount;
	}
	public void setPayMoneyAmount(Double payMoneyAmount) {
		this.payMoneyAmount = payMoneyAmount;
	}
	public Boolean getNeedPayMoney() {
		needPayMoney = (payMoneyAmount == null || payMoneyAmount.compareTo(0.0) == 0)?false:true;
		return needPayMoney;
	}
	public void setNeedPayMoney(Boolean needPayMoney) {
		this.needPayMoney = needPayMoney;
	}
}