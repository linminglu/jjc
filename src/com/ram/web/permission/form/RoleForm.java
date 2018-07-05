package com.ram.web.permission.form;

import org.apache.struts.validator.ValidatorForm;
import com.ram.model.Role;
public class RoleForm extends ValidatorForm {
	private Role role = new Role();

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}
}
