package com.ram.model;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class UserGroup implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer userGroupId;

    /** nullable persistent field */
    private String userGroupName;

    /** nullable persistent field */
    private String userGroupDesc;

    /** persistent field */
    private Set businessGroups;

    /** persistent field */
    private Set groupFunctionRls;

    /** persistent field */
    private Set userGroupRls;

    /** full constructor */
    public UserGroup(Integer userGroupId,String userGroupName, String userGroupDesc, Set businessGroups,Set groupFunctionRls,Set userGroupRls) {
        this.userGroupId=userGroupId;
    	this.userGroupName = userGroupName;
        this.userGroupDesc = userGroupDesc;
        this.businessGroups = businessGroups;
        this.groupFunctionRls=groupFunctionRls;
        this.userGroupRls=userGroupRls;
    }

    /** default constructor */
    public UserGroup() {
    }

    /** minimal constructor */
    public UserGroup(Integer userGroupId,Set businessGroups,Set groupFunctionRls,Set userGroupRls) {
    	this.userGroupId=userGroupId;
    	this.businessGroups = businessGroups;
    	this.groupFunctionRls=groupFunctionRls;
    	this.userGroupRls=userGroupRls;
    }

    public Integer getUserGroupId() {
        return this.userGroupId;
    }

    public void setUserGroupId(Integer userGroupId) {
        this.userGroupId = userGroupId;
    }

    public String getUserGroupName() {
        return this.userGroupName;
    }

    public void setUserGroupName(String userGroupName) {
        this.userGroupName = userGroupName;
    }

    public String getUserGroupDesc() {
        return this.userGroupDesc;
    }

    public void setUserGroupDesc(String userGroupDesc) {
        this.userGroupDesc = userGroupDesc;
    }

    public Set getBusinessGroups() {
        return this.businessGroups;
    }

    public void setBusinessGroups(Set businessGroups) {
        this.businessGroups = businessGroups;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userGroupId", getUserGroupId())
            .toString();
    }

	public Set getGroupFunctionRls() {
		return groupFunctionRls;
	}

	public void setGroupFunctionRls(Set groupFunctionRls) {
		this.groupFunctionRls = groupFunctionRls;
	}

	public Set getUserGroupRls() {
		return userGroupRls;
	}

	public void setUserGroupRls(Set userGroupRls) {
		this.userGroupRls = userGroupRls;
	}

}
