package com.card.web.form;

import org.apache.struts.action.ActionForm;

import com.card.model.Card;

public class UserCardForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Card card = new Card();
	
	private String validateSn;
	
	public Card getCard() {
		return card;
	}
	public void setCard(Card card) {
		this.card = card;
	}
	public String getValidateSn() {
		return validateSn;
	}
	public void setValidateSn(String validateSn) {
		this.validateSn = validateSn;
	}
}