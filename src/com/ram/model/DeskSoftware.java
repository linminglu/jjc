package com.ram.model;

import java.io.Serializable;

/**
 * 桌面软件
 * @author Administrator
 */
public class DeskSoftware implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer deskSoftwareId;
	private Integer softwareId;
	private String snHead;
	private String softwareCode;
	private String title;
	private String latestVersion;
	private String updateExec;
	private Integer trialCount=0;//试用次数
	private Integer userDays;
	private String resourceList;
	private String resourceListAll;
	private String resourceList3;
	private String istrial;//是否试用版
	private String isshow;//默认显示
	private Integer sellprice;//价格
	
	public DeskSoftware(){
		
	}
	
	public DeskSoftware(Integer deskSoftwareId,String softwareCode,String title,Integer sellprice){
		this.deskSoftwareId = deskSoftwareId;
		this.softwareCode = softwareCode;
		this.title = title;
		this.sellprice=sellprice;
	}
	
	public String getIsshow() {
		return isshow;
	}

	public void setIsshow(String isshow) {
		this.isshow = isshow;
	}

	public Integer getSoftwareId() {
		return softwareId;
	}
	public void setSoftwareId(Integer softwareId) {
		this.softwareId = softwareId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLatestVersion() {
		return latestVersion;
	}
	public void setLatestVersion(String latestVersion) {
		this.latestVersion = latestVersion;
	}
	public String getUpdateExec() {
		return updateExec;
	}
	public void setUpdateExec(String updateExec) {
		this.updateExec = updateExec;
	}
	public Integer getDeskSoftwareId() {
		return deskSoftwareId;
	}
	public void setDeskSoftwareId(Integer deskSoftwareId) {
		this.deskSoftwareId = deskSoftwareId;
	}
	public Integer getUserDays() {
		return userDays;
	}
	public void setUserDays(Integer userDays) {
		this.userDays = userDays;
	}
	public String getSoftwareCode() {
		return softwareCode;
	}
	public void setSoftwareCode(String softwareCode) {
		this.softwareCode = softwareCode;
	}
	public String getResourceList() {
		return resourceList;
	}
	public void setResourceList(String resourceList) {
		this.resourceList = resourceList;
	}
	public String getResourceListAll() {
		return resourceListAll;
	}
	public void setResourceListAll(String resourceListAll) {
		this.resourceListAll = resourceListAll;
	}
	public Integer getTrialCount() {
		return trialCount;
	}
	public void setTrialCount(Integer trialCount) {
		this.trialCount = trialCount;
	}
	public String getSnHead() {
		return snHead;
	}
	public void setSnHead(String snHead) {
		this.snHead = snHead;
	}

	public String getIstrial() {
		return istrial;
	}

	public void setIstrial(String istrial) {
		this.istrial = istrial;
	}

	public Integer getSellprice() {
		return sellprice;
	}

	public void setSellprice(Integer sellprice) {
		this.sellprice = sellprice;
	}

	public String getResourceList3() {
		return resourceList3;
	}

	public void setResourceList3(String resourceList3) {
		this.resourceList3 = resourceList3;
	}
	
}