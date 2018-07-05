package com.game.model.dto;

import com.game.model.GaWinCount;
import com.ram.model.User;

public class WinCoDTO {
	private GaWinCount gaWinCount = new GaWinCount();
	private User user = new User();
	
	public WinCoDTO(){
		
	}
	public WinCoDTO(GaWinCount gaWinCount,User user){
		this.gaWinCount = gaWinCount;
		this.user = user;
	}
	public GaWinCount getGaWinCount() {
		return gaWinCount;
	}
	public void setGaWinCount(GaWinCount gaWinCount) {
		this.gaWinCount = gaWinCount;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

}
