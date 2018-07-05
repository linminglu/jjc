package com.ram.web.user.form;



import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.Learner;
import com.ram.model.Manager;
import com.ram.model.Tutor;
import com.ram.model.User;
import com.framework.util.DateTimeUtil;
/**
 * @author lixiaodong 
 */
public class UserInfoForm extends ValidatorForm {
	private Manager manager = new Manager();
	private Tutor tutor = new Tutor();
	private Learner learner = new Learner();
	private User user = new User();
	private Date birthday = DateTimeUtil.getYYYYMMDD_Today(); 
	private FormFile pictrue;
	private String userSystemImage = null;
	
	public Learner getLearner() {
		return learner;
	}
	public void setLearner(Learner learner) {
		this.learner = learner;
	}
	public Manager getManager() {
		return manager;
	}
	public void setManager(Manager manager) {
		this.manager = manager;
	}
	public Tutor getTutor() {
		return tutor;
	}
	public void setTutor(Tutor tutor) {
		this.tutor = tutor;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public void setPictrue(FormFile pictrue){
		this.pictrue = pictrue;
	}
	public FormFile getPictrue(){
		return this.pictrue;
	}
	
	public void setUserSystemImage( String userSystemImage ){
		this.userSystemImage = userSystemImage;
	}
	
	public String getUserSystemImage(){
		return this.userSystemImage;
	}
	
	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		learner = new Learner();
		manager = new Manager();
		tutor = new Tutor();
		user = new User();
		pictrue = null;
	}
}
