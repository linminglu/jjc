package com.card.web.form;

import java.math.BigDecimal;

import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;

import com.card.model.Card;
import com.card.model.CardGeneLog;

import com.card.model.CardItem;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;

import com.ram.model.User;

public class CardForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Card card = new Card();
	private CardGeneLog cardGeneLog = new CardGeneLog();
	private CardGeneLog cardGeneLogForQuery;
	
	private User user=new User();
	private String startDate;
	private String endDate;
	private FormFile file;
	private CardItem cardItem=new CardItem();
	private String userpoints;
	private String remarks;

	private CardRechargeItem  cardRechargeItem= new CardRechargeItem();
	private Integer itemId;//套餐Id
	private String title;//套餐标题
	private BigDecimal parValue;//面额
	private BigDecimal price;//售价
	private CardRechargeOrder cardRechargeOrder = new CardRechargeOrder();
	private String userName;
	private String cardCode;//卡号
	private String orderNum;//订单号
	private String imgUrl;
	private String payStatus;
	private String reason;
	private String type;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	private Integer[] cardIds;
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public CardGeneLog getCardGeneLog() {
		return cardGeneLog;
	}
	public void setCardGeneLog(CardGeneLog cardGeneLog) {
		this.cardGeneLog = cardGeneLog;
	}
	public CardGeneLog getCardGeneLogForQuery() {
		if(cardGeneLogForQuery == null){
			cardGeneLogForQuery = new CardGeneLog();
		}
		return cardGeneLogForQuery;
	}
	public void setCardGeneLogForQuery(CardGeneLog cardGeneLogForQuery) {
		this.cardGeneLogForQuery = cardGeneLogForQuery;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer[] getCardIds() {
		return cardIds;
	}
	public void setCardIds(Integer[] cardIds) {
		this.cardIds = cardIds;
	}


	public CardItem getCardItem() {
		return cardItem;
	}
	public void setCardItem(CardItem cardItem) {
		this.cardItem = cardItem;
	}

	public CardRechargeItem getCardRechargeItem() {
		return cardRechargeItem;
	}
	public void setCardRechargeItem(CardRechargeItem cardRechargeItem) {
		this.cardRechargeItem = cardRechargeItem;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getParValue() {
		return parValue;
	}
	public void setParValue(BigDecimal parValue) {
		this.parValue = parValue;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public CardRechargeOrder getCardRechargeOrder() {
		return cardRechargeOrder;
	}
	public void setCardRechargeOrder(CardRechargeOrder cardRechargeOrder) {
		this.cardRechargeOrder = cardRechargeOrder;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getCardCode() {
		return cardCode;
	}
	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	public FormFile getFile() {
		return file;
	}
	public void setFile(FormFile file) {
		this.file = file;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public String getPayStatus() {
		return payStatus;
	}
	public void setPayStatus(String payStatus) {
		this.payStatus = payStatus;
	}
	public String getUserpoints() {
		return userpoints;
	}
	public void setUserpoints(String userpoints) {
		this.userpoints = userpoints;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}