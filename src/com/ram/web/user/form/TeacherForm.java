package com.ram.web.user.form;



import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Tutor;
import com.ram.model.User;
import com.framework.util.DateTimeUtil;

/**
 * @author lixiaodong 
 */
public class TeacherForm extends ValidatorForm {
	
	private Tutor teacher = new Tutor();
	private User user = new User();
	private java.util.Date birthday =null;
	private String userGroup = null;
	private String rol = null;
	private Integer teacherId = null;
	private String tcCenterName = null;
	private Integer tcId = null;
	private FormFile picture;

	public String getTcCenterName() {
		return tcCenterName;
	}

	public void setTcCenterName(String tcCenterName) {
		this.tcCenterName = tcCenterName;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public Tutor getTeacher() {
		return teacher;
	}

	public void setTeacher(Tutor teacher) {
		this.teacher = teacher;
	}

	public java.util.Date getBirthday() {
		return birthday;
	}

	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}
	
	public Integer getTcId() {
		return tcId;
	}

	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}
	
	public void setPicture(FormFile picture){
		this.picture = picture;
	}
	
	public FormFile getPicture(){
		return this.picture;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		teacher = new Tutor();
		teacherId = null;
	}
}
