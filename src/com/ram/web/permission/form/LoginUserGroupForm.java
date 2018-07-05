package com.ram.web.permission.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.User;
import com.ram.model.UserGroup;

/**
 * @author hulei 
 */
public class LoginUserGroupForm extends ValidatorForm {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private User user=new User();
	private UserGroup userGroup=new UserGroup();

	
	
	public User getUser() {
		return user;
	}



	public void setUser(User user) {
		this.user = user;
	}



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
		this.user=null;
		this.userGroup=null;
	}
}
