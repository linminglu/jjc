package com.ram.web.permission.form;

import org.apache.struts.validator.ValidatorForm;

import com.ram.model.RoleFunctionPermissionRl;

public class RoleFunctionPermissionRlForm extends ValidatorForm {

	private RoleFunctionPermissionRl roleFunctionPermissionRl;

	public RoleFunctionPermissionRl getRoleFunctionPermissionRl() {
		return roleFunctionPermissionRl;
	}

	public void setRoleFunctionPermissionRl(
			RoleFunctionPermissionRl roleFunctionPermissionRl) {
		this.roleFunctionPermissionRl = roleFunctionPermissionRl;
	}
	

}
