package com.ram.web.user.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.ram.model.BusinessGroup;
import com.ram.model.LearnerGroup;
import com.ram.model.Program;
import com.ram.model.Semester;
import com.ram.model.TutorCenter;
import com.ram.model.UserGroup;

public class BusinessGroupForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	
	
	private int businessGroupId=0;
	private UserGroup userGroup=new UserGroup();


	private Program program=new Program();


	private TutorCenter tutorCenter=new TutorCenter();

	private LearnerGroup learnerGroup=new LearnerGroup();

	private Semester semester=new Semester();

	private BusinessGroup businessGroup=new BusinessGroup();
	
	private String searchUserGroupId = null;	
	private String searchProgramId = null;
	private String searchTcId = null;
	private	String searchSemesterId = null; 


	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getSearchProgramId() {
		return searchProgramId;
	}

	public void setSearchProgramId(String searchProgramId) {
		this.searchProgramId = searchProgramId;
	}

	public String getSearchSemesterId() {
		return searchSemesterId;
	}

	public void setSearchSemesterId(String searchSemesterId) {
		this.searchSemesterId = searchSemesterId;
	}

	public String getSearchTcId() {
		return searchTcId;
	}

	public void setSearchTcId(String searchTcId) {
		this.searchTcId = searchTcId;
	}

	public String getSearchUserGroupId() {
		return searchUserGroupId;
	}

	public void setSearchUserGroupId(String searchUserGroupId) {
		this.searchUserGroupId = searchUserGroupId;
	}

	public BusinessGroup getBusinessGroup() {
		return businessGroup;
	}

	public void setBusinessGroup(BusinessGroup businessGroup) {
		this.businessGroup = businessGroup;
	}


	public LearnerGroup getLearnerGroup() {
		return learnerGroup;
	}

	public void setLearnerGroup(LearnerGroup learnerGroup) {
		this.learnerGroup = learnerGroup;
	}


	public Program getProgram() {
		return program;
	}

	public void setProgram(Program program) {
		this.program = program;
	}

	public Semester getSemester() {
		return semester;
	}

	public void setSemester(Semester semester) {
		this.semester = semester;
	}

	public TutorCenter getTutorCenter() {
		return tutorCenter;
	}

	public void setTutorCenter(TutorCenter tutorCenter) {
		this.tutorCenter = tutorCenter;
	}

	public UserGroup getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroup userGroup) {
		this.userGroup = userGroup;
	}


	public void reset(ActionMapping arg0, HttpServletRequest arg1) {
		
		
		this.learnerGroup=new LearnerGroup();
		this.program=new Program();
		this.semester=new Semester();
		this.tutorCenter=new TutorCenter();
		this.userGroup=new UserGroup();
		this.businessGroup=new BusinessGroup();
		this.businessGroup.setSemester(semester);
		this.businessGroup.setProgram(program);
		this.businessGroup.setTutorCenter(tutorCenter);
		this.businessGroup.setUserGroup(userGroup);
		this.businessGroup.setLearnerGroup(learnerGroup);
		this.businessGroupId=0;
	}

	public int getBusinessGroupId() {
		return businessGroupId;
	}

	public void setBusinessGroupId(int businessGroupId) {
		this.businessGroupId = businessGroupId;
	}
	
	

}
