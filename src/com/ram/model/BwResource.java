package com.ram.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;

public class BwResource implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer resourceId;
	private String resourceTitleZh;
	private String resourceTitleEn;
	private String resourceDesc;
	private Integer courseId;
	private Integer unitId;
	private String resourceLinkAddress;
	private Integer accessTimes;
	private Integer updateUserId;
	private String deleteStatus;
	private Date updateDateTime;
	private ResourceType resourceType=new ResourceType();
	private Integer allowed;
	private Set scheduleResources=new HashSet();
	public Integer getAccessTimes() {
		return accessTimes;
	}
	public void setAccessTimes(Integer accessTimes) {
		this.accessTimes = accessTimes;
	}
	public Integer getCourseId() {
		return courseId;
	}
	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}
	public String getDeleteStatus() {
		return deleteStatus;
	}
	public void setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
	}
	public String getResourceDesc() {
		return resourceDesc;
	}
	public void setResourceDesc(String resourceDesc) {
		this.resourceDesc = resourceDesc;
	}
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public String getResourceLinkAddress() {
		return resourceLinkAddress;
	}
	public void setResourceLinkAddress(String resourceLinkAddress) {
		this.resourceLinkAddress = resourceLinkAddress;
	}
	public String getResourceTitleEn() {
		return resourceTitleEn;
	}
	public void setResourceTitleEn(String resourceTitleEn) {
		this.resourceTitleEn = resourceTitleEn;
	}
	public String getResourceTitleZh() {
		return resourceTitleZh;
	}
	public void setResourceTitleZh(String resourceTitleZh) {
		this.resourceTitleZh = resourceTitleZh;
	}
	public ResourceType getResourceType() {
		return resourceType;
	}
	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}
	public Set getScheduleResources() {
		return scheduleResources;
	}
	public void setScheduleResources(Set scheduleResources) {
		this.scheduleResources = scheduleResources;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public Date getUpdateDateTime() {
		return updateDateTime;
	}
	public void setUpdateDateTime(Date updateDateTime) {
		this.updateDateTime = updateDateTime;
	}
	public Integer getUpdateUserId() {
		return updateUserId;
	}
	public void setUpdateUserId(Integer updateUserId) {
		this.updateUserId = updateUserId;
		
		
	}
	
	public void setAllowed(Integer allowed){
		this.allowed = allowed;
	}
	
	public Integer getAllowed(){
		return this.allowed;
	}
}
