package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RolePermissionRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer rolePermissionRlId;

    /** persistent field */
    private com.ram.model.Permission permission;

    /** persistent field */
    private com.ram.model.Role role;

    /** full constructor */
    public RolePermissionRl(Integer rolePermissionRlId, com.ram.model.Permission permission, com.ram.model.Role role) {
        this.rolePermissionRlId = rolePermissionRlId;
        this.permission = permission;
        this.role = role;
    }

    /** default constructor */
    public RolePermissionRl() {
    }

    public Integer getRolePermissionRlId() {
        return this.rolePermissionRlId;
    }

    public void setRolePermissionRlId(Integer rolePermissionRlId) {
        this.rolePermissionRlId = rolePermissionRlId;
    }

    public com.ram.model.Permission getPermission() {
        return this.permission;
    }

    public void setPermission(com.ram.model.Permission permission) {
        this.permission = permission;
    }

    public com.ram.model.Role getRole() {
        return this.role;
    }

    public void setRole(com.ram.model.Role role) {
        this.role = role;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("rolePermissionRlId", getRolePermissionRlId())
            .toString();
    }

}
