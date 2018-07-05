package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author lixiaodong 
 */
public class TeacherQueryForm extends ValidatorForm {
	
	// 用户名
	private String userName;
	
	// 所在学习中心
	private Integer tcCenter;
	
	public Integer getTcCenter() {
		return tcCenter;
	}


	public void setTcCenter(Integer tcCenter) {
		this.tcCenter = tcCenter;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		userName = null;
		tcCenter = null;
	}
}
