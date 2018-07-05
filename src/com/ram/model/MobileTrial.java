package com.ram.model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
public class MobileTrial implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer mobileTrialId;
	private Integer softwareId;
	private String softwareCode;
	private String userMac;
	private String userIp;
	private Integer launchCount;//使用次数
	private Date lastTime;
	private Date firstTime;
	private String usestatus;//使用状态 3冻结 4删除
	private String vers;
	
	public String getVers() {
		return vers;
	}
	public void setVers(String vers) {
		this.vers = vers;
	}
	
	public Integer getMobileTrialId() {
		return mobileTrialId;
	}
	public void setMobileTrialId(Integer mobileTrialId) {
		this.mobileTrialId = mobileTrialId;
	}
	public Integer getSoftwareId() {
		return softwareId;
	}
	public void setSoftwareId(Integer softwareId) {
		this.softwareId = softwareId;
	}
	public String getSoftwareCode() {
		return softwareCode;
	}
	public void setSoftwareCode(String softwareCode) {
		this.softwareCode = softwareCode;
	}
	public String getUserMac() {
		return userMac;
	}
	public void setUserMac(String userMac) {
		this.userMac = userMac;
	}
	public String getUserIp() {
		return userIp;
	}
	public void setUserIp(String userIp) {
		this.userIp = userIp;
	}
	public Integer getLaunchCount() {
		return launchCount;
	}
	public void setLaunchCount(Integer launchCount) {
		this.launchCount = launchCount;
	}
	public Date getLastTime() {
		return lastTime;
	}
	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}
	public Date getFirstTime() {
		return firstTime;
	}
	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}
	public String getUsestatus() {
		return usestatus;
	}
	public void setUsestatus(String usestatus) {
		this.usestatus = usestatus;
	}
	
	
}