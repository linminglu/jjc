package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.ManagerPositionRl;

/**
 * @author lixiaodong 
 */
public class ManagerPositionForm extends ValidatorForm {
	
	private ManagerPositionRl managerPositionRl = new ManagerPositionRl();
	
	
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		managerPositionRl = new ManagerPositionRl();
	}


	public ManagerPositionRl getManagerPositionRl() {
		return managerPositionRl;
	}


	public void setManagerPositionRl(ManagerPositionRl managerPositionRl) {
		this.managerPositionRl = managerPositionRl;
	}
}
