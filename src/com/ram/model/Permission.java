	package com.ram.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Permission implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer permissionId;

    /** nullable persistent field */
    private String permissionTitle;

    /** nullable persistent field */
    private String permissionValue;

    private Set roleFunctionPermissionRls=new TreeSet();
    private Set rolePermissionRls=new TreeSet();

    /** persistent field */
   



	/** full constructor */
    public Permission(Integer permissionId, String permissionTitle, String permissionValue,Set roleFunctionPermissionRls,Set rolePermissionRls ) {
        this.permissionId = permissionId;
        this.permissionTitle = permissionTitle;
        this.permissionValue = permissionValue;
        this.roleFunctionPermissionRls=roleFunctionPermissionRls;
        this.rolePermissionRls=rolePermissionRls;
    }

    /** default constructor */
    public Permission(){
     }

    /** minimal constructor */
    public Permission(Integer permissionId,Set roleFunctionPermissionRls,Set rolePermissionRls){
       this.permissionId=permissionId;
       this.rolePermissionRls=rolePermissionRls;
       this.roleFunctionPermissionRls=roleFunctionPermissionRls;
    }

    public Integer getPermissionId() {
        return this.permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionTitle() {
        return this.permissionTitle;
    }

    public void setPermissionTitle(String permissionTitle) {
        this.permissionTitle = permissionTitle;
    }

    public String getPermissionValue() {
        return this.permissionValue;
    }

    public void setPermissionValue(String permissionValue) {
        this.permissionValue = permissionValue;
    }


    public Set getRoleFunctionPermissionRls() {
		return roleFunctionPermissionRls;
	}

	public void setRoleFunctionPermissionRls(Set roleFunctionPermissionRls) {
		this.roleFunctionPermissionRls = roleFunctionPermissionRls;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("permissionId", getPermissionId())
            .toString();
    }

	public Set getRolePermissionRls() {
		return rolePermissionRls;
	}

	public void setRolePermissionRls(Set rolePermissionRls) {
		this.rolePermissionRls = rolePermissionRls;
	}

}
