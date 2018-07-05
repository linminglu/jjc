package com.ram.model;

import java.io.Serializable;
import java.util.Date;

public class RecommendedUserInfo implements Serializable{

	private Integer recommendedUserInfoId;
	private String userName;
	private String userEmail;
	private Integer recommendedUserId;
	private String recommendedUserName;
	private Date recommendedDateTime;
	
	
	public Date getRecommendedDateTime() {
		return recommendedDateTime;
	}
	public void setRecommendedDateTime(Date recommendedDateTime) {
		this.recommendedDateTime = recommendedDateTime;
	}
	public Integer getRecommendedUserId() {
		return recommendedUserId;
	}
	public void setRecommendedUserId(Integer recommendedUserId) {
		this.recommendedUserId = recommendedUserId;
	}
	public Integer getRecommendedUserInfoId() {
		return recommendedUserInfoId;
	}
	public void setRecommendedUserInfoId(Integer recommendedUserInfoId) {
		this.recommendedUserInfoId = recommendedUserInfoId;
	}
	public String getRecommendedUserName() {
		return recommendedUserName;
	}
	public void setRecommendedUserName(String recommendedUserName) {
		this.recommendedUserName = recommendedUserName;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	
}
