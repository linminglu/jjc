package com.ram.model;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class RoleFunctionRl implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer roleFunctionRlId;

    /** persistent field */
    private com.ram.model.Function function;

    /** persistent field */
    private com.ram.model.Role role;

    /** persistent field */
    private Set roleFunctionPermissionRls=new TreeSet();

    /** full constructor */
    public RoleFunctionRl(Integer roleFunctionRlId, com.ram.model.Function function, com.ram.model.Role role, Set roleFunctionPermissionRls) {
        this.roleFunctionRlId = roleFunctionRlId;
        this.function = function;
        this.role = role;
        this.roleFunctionPermissionRls = roleFunctionPermissionRls;
    }

    /** default constructor */
    public RoleFunctionRl() {
    }

    public Integer getRoleFunctionRlId() {
        return this.roleFunctionRlId;
    }

    public void setRoleFunctionRlId(Integer roleFunctionRlId) {
        this.roleFunctionRlId = roleFunctionRlId;
    }

    public com.ram.model.Function getFunction() {
        return this.function;
    }

    public void setFunction(com.ram.model.Function function) {
        this.function = function;
    }

    public com.ram.model.Role getRole() {
        return this.role;
    }

    public void setRole(com.ram.model.Role role) {
        this.role = role;
    }

    public Set getRoleFunctionPermissionRls() {
        return this.roleFunctionPermissionRls;
    }

    public void setRoleFunctionPermissionRls(Set roleFunctionPermissionRls) {
        this.roleFunctionPermissionRls = roleFunctionPermissionRls;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("roleFunctionRlId", getRoleFunctionRlId())
            .toString();
    }

}
