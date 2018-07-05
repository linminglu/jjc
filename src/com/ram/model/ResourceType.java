package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class ResourceType implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer resourceTypeId;

    /** nullable persistent field */
    private String resourceTypeName;
    
    private String resourceTypeNameEN;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;
    
    private Integer isCourseWare;

    /** persistent field */
    private Set bwResources;

    /** full constructor */
    public ResourceType(String resourceTypeName, String resourceTypeNameEN, Integer updateUserId, Date updateDateTime, Integer isCourseWare, Set bwResources) {
        this.resourceTypeName = resourceTypeName;
        this.resourceTypeNameEN = resourceTypeNameEN;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
        this.isCourseWare = isCourseWare;
        this.bwResources = bwResources;
    }

    /** default constructor */
    public ResourceType() {
    }

    /** minimal constructor */
    public ResourceType(Set bwResources) {
        this.bwResources = bwResources;
    }

    public Integer getResourceTypeId() {
        return this.resourceTypeId;
    }

    public void setResourceTypeId(Integer resourceTypeId) {
        this.resourceTypeId = resourceTypeId;
    }

    public String getResourceTypeName() {
        return this.resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public String getResourceTypeNameEN() {
        return this.resourceTypeNameEN;
    }

    public void setResourceTypeNameEN(String resourceTypeNameEN) {
        this.resourceTypeNameEN = resourceTypeNameEN;
    }

    public Integer getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public Integer getIsCourseWare(){
    	return this.isCourseWare;
    }
    
    public void setIsCourseWare(Integer isCourseWare){
    	this.isCourseWare = isCourseWare;
    }
    
    public Set getBwResources() {
        return this.bwResources;
    }

    public void setBwResources(Set bwResources) {
        this.bwResources = bwResources;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("resourceTypeId", getResourceTypeId())
            .toString();
    }

}
