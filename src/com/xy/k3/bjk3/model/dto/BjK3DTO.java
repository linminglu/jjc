package com.xy.k3.bjk3.model.dto;

import com.game.model.GaBetDetail;
import com.ram.model.User;


public class BjK3DTO {

	private GaBetDetail gaBetDetail;
	private User user;
	
	public BjK3DTO(){
		
	}

	public BjK3DTO(GaBetDetail gaBetDetail, User user){
		this.setGaBetDetail(gaBetDetail);
		this.setUser(user);
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
