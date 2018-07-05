package com.ram.model;

import java.io.Serializable;

/**
 * 桌面软件
 * @author Administrator
 */
public class DeskSoftwareVersion implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer deskSoftwareVersionId;
	private Integer softwareId;
	private String softwareCode;
	private String softwareVersion;
	private String resourceList;
	
	public Integer getDeskSoftwareVersionId() {
		return deskSoftwareVersionId;
	}
	public void setDeskSoftwareVersionId(Integer deskSoftwareVersionId) {
		this.deskSoftwareVersionId = deskSoftwareVersionId;
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
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	public String getResourceList() {
		return resourceList;
	}
	public void setResourceList(String resourceList) {
		this.resourceList = resourceList;
	}
	
	
}