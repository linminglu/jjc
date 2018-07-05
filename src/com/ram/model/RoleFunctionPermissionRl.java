package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RoleFunctionPermissionRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer roleFunctionPermissionRlId;

    /** persistent field */
    private com.ram.model.RoleFunctionRl roleFunctionRl;

    /** persistent field */
    private com.ram.model.Permission permission;

    /** full constructor */
    public RoleFunctionPermissionRl(Integer roleFunctionPermissionRlId, com.ram.model.RoleFunctionRl roleFunctionRl, com.ram.model.Permission permission) {
        this.roleFunctionPermissionRlId = roleFunctionPermissionRlId;
        this.roleFunctionRl = roleFunctionRl;
        this.permission = permission;
    }

    /** default constructor */
    public RoleFunctionPermissionRl() {
    }

    public Integer getRoleFunctionPermissionRlId() {
        return this.roleFunctionPermissionRlId;
    }

    public void setRoleFunctionPermissionRlId(Integer roleFunctionPermissionRlId) {
        this.roleFunctionPermissionRlId = roleFunctionPermissionRlId;
    }

    public com.ram.model.RoleFunctionRl getRoleFunctionRl() {
        return this.roleFunctionRl;
    }

    public void setRoleFunctionRl(com.ram.model.RoleFunctionRl roleFunctionRl) {
        this.roleFunctionRl = roleFunctionRl;
    }

    public com.ram.model.Permission getPermission() {
        return this.permission;
    }

    public void setPermission(com.ram.model.Permission permission) {
        this.permission = permission;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("roleFunctionPermissionRlId", getRoleFunctionPermissionRlId())
            .toString();
    }

}
