package com.ram.model;

import java.io.Serializable;
import java.util.Date;

public class OnLineUserRecord implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer onlineUserRecordID;
	private String serverName;
	private Integer userRecordNumber;
	private Date recordTime;
	
	public OnLineUserRecord(){}
	
	public OnLineUserRecord( Integer onlineUserRecordID,String serverName,
							 Integer userRecordNumber,Date recordTime){
		this.onlineUserRecordID = onlineUserRecordID;
		this.serverName = serverName;
		this.userRecordNumber = userRecordNumber;
		this.recordTime = recordTime;
	}
	
	public Integer getOnlineUserRecordID() {
		return onlineUserRecordID;
	}
	public void setOnlineUserRecordID(Integer onlineUserRecordID) {
		this.onlineUserRecordID = onlineUserRecordID;
	}
	
	public Date getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}
	
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	public Integer getUserRecordNumber() {
		return userRecordNumber;
	}
	public void setUserRecordNumber(Integer userRecordNumber) {
		this.userRecordNumber = userRecordNumber;
	}
	
}