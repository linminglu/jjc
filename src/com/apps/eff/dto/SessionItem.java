package com.apps.eff.dto;

import java.io.Serializable;

/**
 * 期号DTO
 * 
 */
public class SessionItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String gameCode;//代码
	private String sessionNo;//期号
	private String result;//开奖号 逗号分割
	private String time;//开奖时间
	
	public SessionItem(){}
	
	public SessionItem(String result,String time){
		this.result = result;
		this.time = time;
	}
	
	public String getSessionNo() {
		return sessionNo;
	}
	public void setSessionNo(String sessionNo) {
		this.sessionNo = sessionNo;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	
	
}
