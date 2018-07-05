package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class BusinessGroup implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer bizGroupId;

    /** nullable persistent field */
    private String groupName;

    /** persistent field */
    private com.ram.model.LearnerGroup learnerGroup;

    /** persistent field */
    private com.ram.model.TutorCenter tutorCenter;

    /** persistent field */
    private com.ram.model.Program program;

    /** persistent field */
    private com.ram.model.UserGroup userGroup;

    /** persistent field */
    private com.ram.model.Semester semester;
    
    private Integer createUserId;

    public Integer getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(Integer createUserId) {
		this.createUserId = createUserId;
	}

	/** full constructor */
    public BusinessGroup(String groupName, com.ram.model.LearnerGroup learnerGroup, com.ram.model.TutorCenter tutorCenter, com.ram.model.Program program, com.ram.model.UserGroup userGroup, com.ram.model.Semester semester,Integer createUserId) {
        this.groupName = groupName;
        this.learnerGroup = learnerGroup;
        this.tutorCenter = tutorCenter;
        this.program = program;
        this.userGroup = userGroup;
        this.semester = semester;
        this.createUserId=createUserId;
    }

    /** default constructor */
    public BusinessGroup() {
    }

    /** minimal constructor */
    public BusinessGroup(com.ram.model.LearnerGroup learnerGroup, com.ram.model.TutorCenter tutorCenter, com.ram.model.Program program, com.ram.model.UserGroup userGroup, com.ram.model.Semester semester) {
        this.learnerGroup = learnerGroup;
        this.tutorCenter = tutorCenter;
        this.program = program;
        this.userGroup = userGroup;
        this.semester = semester;
    }

    public Integer getBizGroupId() {
        return this.bizGroupId;
    }

    public void setBizGroupId(Integer bizGroupId) {
        this.bizGroupId = bizGroupId;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public com.ram.model.LearnerGroup getLearnerGroup() {
        return this.learnerGroup;
    }

    public void setLearnerGroup(com.ram.model.LearnerGroup learnerGroup) {
        this.learnerGroup = learnerGroup;
    }

    public com.ram.model.TutorCenter getTutorCenter() {
        return this.tutorCenter;
    }

    public void setTutorCenter(com.ram.model.TutorCenter tutorCenter) {
        this.tutorCenter = tutorCenter;
    }

    public com.ram.model.Program getProgram() {
        return this.program;
    }

    public void setProgram(com.ram.model.Program program) {
        this.program = program;
    }

    public com.ram.model.UserGroup getUserGroup() {
        return this.userGroup;
    }

    public void setUserGroup(com.ram.model.UserGroup userGroup) {
        this.userGroup = userGroup;
    }

    public com.ram.model.Semester getSemester() {
        return this.semester;
    }

    public void setSemester(com.ram.model.Semester semester) {
        this.semester = semester;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("bizGroupId", getBizGroupId())
            .toString();
    }

}
