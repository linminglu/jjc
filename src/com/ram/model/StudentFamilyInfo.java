package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class StudentFamilyInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer studentFamilyInfoId;

    /** nullable persistent field */
    private Integer learnerId;

    /** nullable persistent field */
    private String name;

    /** nullable persistent field */
    private String relation;

    /** nullable persistent field */
    private String workUnit;

    /** nullable persistent field */
    private String position;

    /** full constructor */
    public StudentFamilyInfo(Integer learnerId, String name, String relation, String workUnit, String position) {
        this.learnerId = learnerId;
        this.name = name;
        this.relation = relation;
        this.workUnit = workUnit;
        this.position = position;
    }

    /** default constructor */
    public StudentFamilyInfo() {
    }

    public Integer getStudentFamilyInfoId() {
        return this.studentFamilyInfoId;
    }

    public void setStudentFamilyInfoId(Integer studentFamilyInfoId) {
        this.studentFamilyInfoId = studentFamilyInfoId;
    }

    public Integer getLearnerId() {
        return this.learnerId;
    }

    public void setLearnerId(Integer learnerId) {
        this.learnerId = learnerId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return this.relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getWorkUnit() {
        return this.workUnit;
    }

    public void setWorkUnit(String workUnit) {
        this.workUnit = workUnit;
    }

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("studentFamilyInfoId", getStudentFamilyInfoId())
            .toString();
    }

}
