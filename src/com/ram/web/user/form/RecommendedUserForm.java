package com.ram.web.user.form;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author cuishiyong
 */
public class RecommendedUserForm extends ValidatorForm {
	
	private String[] userNames;
	private String[] userEmails;
	private Integer recommendUserId;
	private String nameKey;
	
	
	public String getNameKey() {
		return nameKey;
	}
	public void setNameKey(String nameKey) {
		this.nameKey = nameKey;
	}
	public Integer getRecommendUserId() {
		return recommendUserId;
	}
	public void setRecommendUserId(Integer recommendUserId) {
		this.recommendUserId = recommendUserId;
	}
	public String[] getUserEmails() {
		return userEmails;
	}
	public void setUserEmails(String[] userEmails) {
		this.userEmails = userEmails;
	}
	public String[] getUserNames() {
		return userNames;
	}
	public void setUserNames(String[] userNames) {
		this.userNames = userNames;
	}
	
	

}
