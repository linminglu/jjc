package com.ram.model.dto;

import java.math.BigDecimal;

import com.apps.model.UserTradeDetail;
import com.game.model.GaBetDetail;
import com.ram.model.User;

public class UserDTO {
	private User user = new User();
	private String userName;
	private UserTradeDetail userTradeDetail;
	private GaBetDetail gaBetDetail;

	private Integer userId;
	private BigDecimal money;
	private String id;
	private String title;
	
	public UserDTO() {
	}

	public UserDTO(Integer userId,BigDecimal money) {
		this.userId = userId;
		this.money = money;
	}

	
	public UserDTO(User user, UserTradeDetail userTradeDetail) {
		this.user = user;
		this.userTradeDetail = userTradeDetail;
	}
	
	public UserDTO(User user, String userName) {
		this.user = user;
		this.userName = userName;
	}
	
	public UserDTO(User user, GaBetDetail gaBetDetail){
		this.user = user;
		this.gaBetDetail = gaBetDetail;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the userTradeDetail
	 */
	public UserTradeDetail getUserTradeDetail() {
		return userTradeDetail;
	}

	/**
	 * @param userTradeDetail the userTradeDetail to set
	 */
	public void setUserTradeDetail(UserTradeDetail userTradeDetail) {
		this.userTradeDetail = userTradeDetail;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	

}
