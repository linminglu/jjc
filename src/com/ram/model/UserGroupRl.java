package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class UserGroupRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer userGroupRlId;

    /** persistent field */
    private com.ram.model.User user;

    /** persistent field */
    private com.ram.model.UserGroup userGroup;

    /** full constructor */
    public UserGroupRl(Integer userGroupRlId, com.ram.model.User user, com.ram.model.UserGroup userGroup) {
        this.userGroupRlId = userGroupRlId;
        this.user = user;
        this.userGroup = userGroup;
    }

    /** default constructor */
    public UserGroupRl() {
    }

    public Integer getUserGroupRlId() {
        return this.userGroupRlId;
    }

    public void setUserGroupRlId(Integer userGroupRlId) {
        this.userGroupRlId = userGroupRlId;
    }

    public com.ram.model.User getUser() {
        return this.user;
    }

    public void setUser(com.ram.model.User user) {
        this.user = user;
    }

    public com.ram.model.UserGroup getUserGroup() {
        return this.userGroup;
    }

    public void setUserGroup(com.ram.model.UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userGroupRlId", getUserGroupRlId())
            .toString();
    }

}
