package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class LearnerGroup implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer learnerGroupId;

    /** persistent field */
    private String learnerGroupTitle;


    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private String updateUserLoginName;

    /** persistent field */
    private Date updateDateTime;

    /** persistent field */
//    private Classes classes=new Classes();
    private Semester semester;
    private Program program;
    private TutorCenter tutorCenter;
//    private Course course;
    
//    /** persistent field */
//    private Set learnerGroupMembers;
//
//    /** full constructor */
//    public LearnerGroup(String learnerGroupTitle,
//    		Semester semester,
//    		Program program,
//    		TutorCenter tutorCenter,
//    		Course course, 
//    		Integer updateUserId, 
//    		String updateUserLoginName, 
//    		Date updateDateTime, 
//    		Classes classes, 
//    		Set learnerGroupMembers) {
//        this.learnerGroupTitle = learnerGroupTitle;
//        this.semester=semester;
//        this.program=program;
//        this.tutorCenter=tutorCenter;
//        this.course=course;
//        this.updateUserId = updateUserId;
//        this.updateUserLoginName = updateUserLoginName;
//        this.updateDateTime = updateDateTime;
//        this.classes = classes;
//        this.learnerGroupMembers = learnerGroupMembers;
//    }
//
//    /** default constructor */
//    public LearnerGroup() {
//    }
//
//    /** minimal constructor */
//    public LearnerGroup(String learnerGroupTitle, Date updateDateTime,Semester semester,Program program,TutorCenter tutorCenter,Course course ,Classes classes, Set learnerGroupMembers) {
//        this.semester=semester;
//        this.program=program;
//        this.tutorCenter=tutorCenter;
//        this.course=course;
//    	this.learnerGroupTitle = learnerGroupTitle;
//        this.updateDateTime = updateDateTime;
//        this.classes = classes;
//        this.learnerGroupMembers = learnerGroupMembers;
//    }

    public Integer getLearnerGroupId() {
        return this.learnerGroupId;
    }

    public void setLearnerGroupId(Integer learnerGroupId) {
        this.learnerGroupId = learnerGroupId;
    }

    public String getLearnerGroupTitle() {
        return this.learnerGroupTitle;
    }

    public void setLearnerGroupTitle(String learnerGroupTitle) {
        this.learnerGroupTitle = learnerGroupTitle;
    }

    public Integer getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateUserLoginName() {
        return this.updateUserLoginName;
    }

    public void setUpdateUserLoginName(String updateUserLoginName) {
        this.updateUserLoginName = updateUserLoginName;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }



//    public Set getLearnerGroupMembers() {
//        return this.learnerGroupMembers;
//    }
//
//    public void setLearnerGroupMembers(Set learnerGroupMembers) {
//        this.learnerGroupMembers = learnerGroupMembers;
//    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learnerGroupId", getLearnerGroupId())
            .toString();
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











}
