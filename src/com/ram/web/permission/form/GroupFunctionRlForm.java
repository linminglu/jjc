package com.ram.web.permission.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.GroupFunctionRl;

public class GroupFunctionRlForm extends ValidatorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GroupFunctionRl groupFunctionRl = new GroupFunctionRl();

	private Integer groupId;

	private Integer functionId;

	public Integer getFunctionId() {
		return functionId;
	}

	public void setFunctionId(Integer functionId) {
		this.functionId = functionId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		super.reset(arg0, arg1);
	}

	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
		// TODO Auto-generated method stub
		return super.validate(arg0, arg1);
	}

	public GroupFunctionRl getGroupFunctionRl() {
		return groupFunctionRl;
	}

	public void setGroupFunctionRl(GroupFunctionRl groupFunctionRl) {
		this.groupFunctionRl = groupFunctionRl;
	}
}
