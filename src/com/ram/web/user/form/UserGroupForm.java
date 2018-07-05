package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.User;
import com.ram.model.UserGroup;

/**
 * @author lixiaodong 
 */
public class UserGroupForm extends ValidatorForm {

	private UserGroup userGroup=new UserGroup();

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		userGroup=new UserGroup();
	}

}
