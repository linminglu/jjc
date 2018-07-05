package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.BusinessGroup;
import com.ram.model.Learner;
import com.ram.model.LearnerGroup;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.Tutor;
import com.ram.model.TutorCenter;
import com.ram.model.User;
import com.ram.model.UserGroup;

public class UserExtendInfoForm extends ValidatorForm{
	
	private static final long serialVersionUID = 1L;
	
	private String userName = null;
	private String userNameZH = null;
	private String email = null;
	private String userType = null;
	
	private Integer userExtendInfoId;
	private Integer userId;
	private Integer learnerId;
	private String age;
	private String workYear;
	private String salary;
	private Integer englishStudyYear;
	private String isAttendTrain;
	private String infoResource;
	
	private User user = null;
	private Learner learner = null;
	
	public UserExtendInfoForm( Integer userExtendInfoId,Integer userId,Integer learnerId,String age,
							   String workYear,String salary,Integer englishStudyYear,String isAttendTrain,
							   String infoResource,String userName,String userNameZH,String email,
							   String userType,User user,Learner learner  ){
		this.userExtendInfoId = userExtendInfoId;
		this.userId = userId;
		this.learnerId = learnerId;
		this.age = age;
		this.workYear = workYear;
		this.salary = salary;
		this.englishStudyYear = englishStudyYear;
		this.isAttendTrain = isAttendTrain;
		this.infoResource = infoResource;
		this.userName = userName;
		this.userNameZH = userNameZH;
		this.email = email;
		this.userType = userType;
		this.user = user;
		this.learner = learner;
	}
	
	public UserExtendInfoForm(){}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public Integer getEnglishStudyYear() {
		return englishStudyYear;
	}

	public void setEnglishStudyYear(Integer englishStudyYear) {
		this.englishStudyYear = englishStudyYear;
	}

	public String getInfoResource() {
		return infoResource;
	}

	public void setInfoResource(String infoResource) {
		this.infoResource = infoResource;
	}

	public String getIsAttendTrain() {
		return isAttendTrain;
	}

	public void setIsAttendTrain(String isAttendTrain) {
		this.isAttendTrain = isAttendTrain;
	}

	public Learner getLearner() {
		return learner;
	}

	public void setLearner(Learner learner) {
		this.learner = learner;
	}

	public Integer getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(Integer learnerId) {
		this.learnerId = learnerId;
	}

	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getUserExtendInfoId() {
		return userExtendInfoId;
	}

	public void setUserExtendInfoId(Integer userExtendInfoId) {
		this.userExtendInfoId = userExtendInfoId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getWorkYear() {
		return workYear;
	}

	public void setWorkYear(String workYear) {
		this.workYear = workYear;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNameZH() {
		return userNameZH;
	}

	public void setUserNameZH(String userNameZH) {
		this.userNameZH = userNameZH;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}
	
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		user = new User();
		learner= new Learner();
	}
}