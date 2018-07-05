package com.ram.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Role implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer roleId;

    /** nullable persistent field */
    private String roleName;

    /** nullable persistent field */
    private String status;

    private Set userRoleRls=new TreeSet();
    /** persistent field */
    private Set roleFunctionRls=new TreeSet();

    /** persistent field */
    private Set rolePermissionRls=new TreeSet();

    /** full constructor */
    public Role(Integer roleId, String roleName, String status,Set userRoleRls, Set roleFunctionRls, Set rolePermissionRls) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.status = status;
        this.userRoleRls=userRoleRls;
        this.roleFunctionRls = roleFunctionRls;
        this.rolePermissionRls = rolePermissionRls;
    }

    /** default constructor */
    public Role() {
    }

    /** minimal constructor */
    public Role(Integer roleId,Set userRoleRls, Set roleFunctionRls, Set rolePermissionRls) {
        this.roleId = roleId;
        this.userRoleRls=userRoleRls;
        this.roleFunctionRls = roleFunctionRls;
        this.rolePermissionRls = rolePermissionRls;
    }

    public Integer getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return this.roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Set getRoleFunctionRls() {
        return this.roleFunctionRls;
    }

    public void setRoleFunctionRls(Set roleFunctionRls) {
        this.roleFunctionRls = roleFunctionRls;
    }

    public Set getRolePermissionRls() {
        return this.rolePermissionRls;
    }

    public void setRolePermissionRls(Set rolePermissionRls) {
        this.rolePermissionRls = rolePermissionRls;
    }

    
    
    public Set getUserRoleRls() {
		return userRoleRls;
	}

	public void setUserRoleRls(Set userRoleRls) {
		this.userRoleRls = userRoleRls;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("roleId", getRoleId())
            .toString();
    }

}
