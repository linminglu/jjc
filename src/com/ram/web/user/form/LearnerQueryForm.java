package com.ram.web.user.form;

import java.util.Date;

import org.apache.struts.validator.ValidatorForm;

/**
 * @author lixiaodong 
 * 
 */
public class LearnerQueryForm extends ValidatorForm {
	
	// 用户名	private String userName;

	private Date b;
	
	private Date e;
	
	// 所在学习中心
	private Integer tcId;

	private Integer semesterId;

	private Integer programId;
	
	private Integer courseId;

	private String enrollStatus = "0";
	
	//主动被动
	private Integer active;
	
	private String courseStatus;
	
	/**
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	// public void reset(ActionMapping mapping, HttpServletRequest request) {
	// userName = null;
	// TcCenter = null;
	// }
	public Integer getTcId() {
		return this.tcId;
	}

	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getB() {
		return b;
	}

	public void setB(Date b) {
		this.b = b;
	}

	public Date getE() {
		return e;
	}

	public void setE(Date e) {
		this.e = e;
	}

	public String getEnrollStatus() {
		return enrollStatus;
	}

	public void setEnrollStatus(String enrollStatus) {
		this.enrollStatus = enrollStatus;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public Integer getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Integer semesterId) {
		this.semesterId = semesterId;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public String getCourseStatus() {
		return courseStatus;
	}

	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
}
