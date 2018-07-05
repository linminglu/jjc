package com.ram.web.permission.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.RolePermissionRl;

public class RolePermissionRlForm extends ValidatorForm{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RolePermissionRl rolePermissionRl=new RolePermissionRl();
	

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
	}

	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return super.validate(arg0, arg1);
	}

	public RolePermissionRl getRolePermissionRl() {
		return rolePermissionRl;
	}

	public void setRolePermissionRl(RolePermissionRl rolePermissionRl) {
		this.rolePermissionRl = rolePermissionRl;
	}
}
