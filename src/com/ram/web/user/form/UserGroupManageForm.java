package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.UserGroupRl;

/**
 * @author lixiaodong 
 */
public class UserGroupManageForm extends ValidatorForm {
	
	private UserGroupRl userGroupRl = new UserGroupRl();
	
	
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		userGroupRl = new UserGroupRl();
	}


	public UserGroupRl getUserGroupRl() {
		return userGroupRl;
	}


	public void setUserGroupRl(UserGroupRl userGroupRl) {
		this.userGroupRl = userGroupRl;
	}

}

