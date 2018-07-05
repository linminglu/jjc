package com.ram.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class McStudyCondition implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer mcStudyCondId;

    /** nullable persistent field */
    private Integer scheduleId;

    /** nullable persistent field */
    private Integer moduleId;

    /** nullable persistent field */
    private Integer courseId;

    /** nullable persistent field */
    private float dependCourseCredit;

    /** nullable persistent field */
    private float dependCourseScore;

    /** nullable persistent field */
    private Integer mcStudyConditionId;

    /** full constructor */
    public McStudyCondition(Integer scheduleId, Integer moduleId, Integer courseId, float dependCourseCredit, float dependCourseScore, Integer mcStudyConditionId) {
        this.scheduleId = scheduleId;
        this.moduleId = moduleId;
        this.courseId = courseId;
        this.dependCourseCredit = dependCourseCredit;
        this.dependCourseScore = dependCourseScore;
        this.mcStudyConditionId = mcStudyConditionId;
    }

    /** default constructor */
    public McStudyCondition() {
    }

    public Integer getMcStudyCondId() {
        return this.mcStudyCondId;
    }

    public void setMcStudyCondId(Integer mcStudyCondId) {
        this.mcStudyCondId = mcStudyCondId;
    }

    public Integer getScheduleId() {
        return this.scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Integer getModuleId() {
        return this.moduleId;
    }

    public void setModuleId(Integer moduleId) {
        this.moduleId = moduleId;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public float getDependCourseCredit() {
        return this.dependCourseCredit;
    }

    public void setDependCourseCredit(float dependCourseCredit) {
        this.dependCourseCredit = dependCourseCredit;
    }

    public float getDependCourseScore() {
        return this.dependCourseScore;
    }

    public void setDependCourseScore(float dependCourseScore) {
        this.dependCourseScore = dependCourseScore;
    }

    public Integer getMcStudyConditionId() {
        return this.mcStudyConditionId;
    }

    public void setMcStudyConditionId(Integer mcStudyConditionId) {
        this.mcStudyConditionId = mcStudyConditionId;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("mcStudyCondId", getMcStudyCondId())
            .toString();
    }

}
