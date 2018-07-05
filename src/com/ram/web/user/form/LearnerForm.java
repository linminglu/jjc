package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Learner;
import com.ram.model.User;
import com.framework.util.DateTimeUtil;

/**
 * @author lixiaodong 
 */
public class LearnerForm extends ValidatorForm {
	
	private Learner learner = new Learner();
	private User user = new User();
	private java.util.Date birthday = null;
	private Integer learnerId;
	private Integer semesterId;
	private String programName;
	private String specialityName;
	private String semesterName;
	private String tcCenterName;
	private FormFile picture;

	public LearnerForm(){}
	
	public LearnerForm(Learner learner, User user, Integer learnerId, Integer semesterId, String programName, String specialityName, String semesterName, String tcCenterName){
		this.learner = learner;
		this.user = user;
		this.learnerId = learnerId;
		this.semesterId = semesterId;
		this.programName = programName;
		this.specialityName = specialityName;
		this.semesterName = semesterName;
		this.tcCenterName = tcCenterName;
	}
	
	public String getTcCenterName() {
		return tcCenterName;
	}

	public void setTcCenterName(String tcCenterName) {
		this.tcCenterName = tcCenterName;
	}

	public Integer getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(Integer learnerId) {
		this.learnerId = learnerId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public java.util.Date getBirthday() {
		return birthday;
	}

	public void setBirthday(java.util.Date birthday) {
		this.birthday = birthday;
	}

	public Integer getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Integer semesterId) {
		this.semesterId = semesterId;
	}
	
	public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getSemesterName() {
		return semesterName;
	}

	public void setSemesterName(String semesterName) {
		this.semesterName = semesterName;
	}

	public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
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
		learner = new Learner();
		user = new User();
		learnerId = null;
	}
}
