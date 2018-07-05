package com.gf.dcb.model.dto;

import com.game.model.GaBetPart;
import com.game.model.GaBetSponsor;
import com.ram.model.User;

/**
 * 北京三分彩类
 * @author hpz
 */
public class GfDcbDTO {

	private GaBetSponsor gaBetSponsor = new GaBetSponsor();
	private GaBetPart gaBetPart = new GaBetPart();
	private User user = new User();
	
	
	public GfDcbDTO(){
		
	}
	public GfDcbDTO(GaBetPart gaBetPart,User user){
		this.gaBetPart = gaBetPart;
		this.user = user;
	}
	public GfDcbDTO(GaBetSponsor gaBetSponsor,User user){
		this.gaBetSponsor = gaBetSponsor;
		this.user = user;
	}

	public GaBetPart getGaBetPart() {
		return gaBetPart;
	}
	public void setGaBetPart(GaBetPart gaBetPart) {
		this.gaBetPart = gaBetPart;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public GaBetSponsor getGaBetSponsor() {
		return gaBetSponsor;
	}
	public void setGaBetSponsor(GaBetSponsor gaBetSponsor) {
		this.gaBetSponsor = gaBetSponsor;
	}
	
}
