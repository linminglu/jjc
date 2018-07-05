package com.card.model.dto;

import java.math.BigDecimal;

import com.card.model.Card;
import com.card.model.CardItem;
import com.card.model.CardItemOrder;
import com.card.model.CardRechargeItem;
import com.card.model.CardRechargeOrder;
import com.ram.model.User;

public class CardDTO {
	
	private Card card;
	private User createUser;
	private CardRechargeItem cardRechargeItem;
	private CardRechargeOrder cardRechargeOrder;
	private CardItem cardItem;
	private CardItemOrder cardItemOrder;
	
	public CardDTO(CardRechargeItem cardRechargeItem, CardRechargeOrder cardRechargeOrder, User createUser) {
		this.cardRechargeItem = cardRechargeItem;
		this.cardRechargeOrder = cardRechargeOrder;
		this.createUser = createUser;
	}
	
	public CardDTO(CardItem cardItem, CardItemOrder cardItemOrder, User createUser,Card card) {
		this.cardItem = cardItem;
		this.cardItemOrder = cardItemOrder;
		this.createUser = createUser;
		this.card = card;
	}
	
	public CardDTO(CardItem cardItem, CardItemOrder cardItemOrder){
		this.cardItem=cardItem;
		this.cardItemOrder=cardItemOrder;
	}
	
	public CardDTO(CardRechargeOrder cardRechargeOrder, User createUser){
		this.cardRechargeOrder=cardRechargeOrder;
		this.createUser=createUser;
	}
	
	
	public CardDTO() {
		// default
		super();
	}
	public CardDTO(Card card, User createUser) {
		super();
		this.card = card;
		this.createUser = createUser;
	}
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public User getCreateUser() {
		return createUser;
	}
	public void setCreateUser(User createUser) {
		this.createUser = createUser;
	}

	public CardRechargeItem getCardRechargeItem() {
		return cardRechargeItem;
	}

	public void setCardRechargeItem(CardRechargeItem cardRechargeItem) {
		this.cardRechargeItem = cardRechargeItem;
	}

	public CardRechargeOrder getCardRechargeOrder() {
		return cardRechargeOrder;
	}

	public void setCardRechargeOrder(CardRechargeOrder cardRechargeOrder) {
		this.cardRechargeOrder = cardRechargeOrder;
	}

	public CardItem getCardItem() {
		return cardItem;
	}

	public void setCardItem(CardItem cardItem) {
		this.cardItem = cardItem;
	}

	public CardItemOrder getCardItemOrder() {
		return cardItemOrder;
	}

	public void setCardItemOrder(CardItemOrder cardItemOrder) {
		this.cardItemOrder = cardItemOrder;
	}

	
}