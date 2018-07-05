package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class UserRoleRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer userRoleRlId;

    /** persistent field */
    private com.ram.model.User user;

    /** persistent field */
    private com.ram.model.Role role;

    /** full constructor */
    public UserRoleRl(Integer userRoleRlId, com.ram.model.User user, com.ram.model.Role role) {
        this.userRoleRlId = userRoleRlId;
        this.user = user;
        this.role = role;
    }

    /** default constructor */
    public UserRoleRl() {
    }

    public Integer getUserRoleRlId() {
        return this.userRoleRlId;
    }

    public void setUserRoleRlId(Integer userRoleRlId) {
        this.userRoleRlId = userRoleRlId;
    }

    public com.ram.model.User getUser() {
        return this.user;
    }

    public void setUser(com.ram.model.User user) {
        this.user = user;
    }

    public com.ram.model.Role getRole() {
        return this.role;
    }

    public void setRole(com.ram.model.Role role) {
        this.role = role;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("userRoleRlId", getUserRoleRlId())
            .toString();
    }

}
