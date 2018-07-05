package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class LearnerAssignment implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer learnerAssignmentId;

    /** nullable persistent field */
    private Integer submitUserId;

    /** nullable persistent field */
    private Integer courseId;

     /** nullable persistent field */
    private String submitAssignmentLink;

    /** nullable persistent field */
    private Integer assignmentSerial;

    /** nullable persistent field */
    private Date updateDateTime;

    /** persistent field */
    private String assignmentStatus;

    /** nullable persistent field */
    private Integer scheduleAssignmentId;

    /** persistent field */
    private Set assignmentFeedbacks;

    /** persistent field */
    private Set assignmentScores;

    /** full constructor */
    public LearnerAssignment(Integer submitUserId, Integer courseId,Integer scheduleCourseId, String submitAssignmentLink, Integer assignmentSerial, Date updateDateTime, String assignmentStatus, Integer scheduleAssignmentId, Set assignmentFeedbacks, Set assignmentScores) {
        this.submitUserId = submitUserId;
        this.courseId = courseId;
        
        this.submitAssignmentLink = submitAssignmentLink;
        this.assignmentSerial = assignmentSerial;
        this.updateDateTime = updateDateTime;
        this.assignmentStatus = assignmentStatus;
        this.scheduleAssignmentId = scheduleAssignmentId;
        this.assignmentFeedbacks = assignmentFeedbacks;
        this.assignmentScores = assignmentScores;
    }

    /** default constructor */
    public LearnerAssignment() {
    }

    /** minimal constructor */
    public LearnerAssignment(String assignmentStatus, Set assignmentFeedbacks, Set assignmentScores) {
        this.assignmentStatus = assignmentStatus;
        this.assignmentFeedbacks = assignmentFeedbacks;
        this.assignmentScores = assignmentScores;
    }

    public Integer getLearnerAssignmentId() {
        return this.learnerAssignmentId;
    }

    public void setLearnerAssignmentId(Integer learnerAssignmentId) {
        this.learnerAssignmentId = learnerAssignmentId;
    }

    public Integer getSubmitUserId() {
        return this.submitUserId;
    }

    public void setSubmitUserId(Integer submitUserId) {
        this.submitUserId = submitUserId;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getSubmitAssignmentLink() {
        return this.submitAssignmentLink;
    }

    public void setSubmitAssignmentLink(String submitAssignmentLink) {
        this.submitAssignmentLink = submitAssignmentLink;
    }

    public Integer getAssignmentSerial() {
        return this.assignmentSerial;
    }

    public void setAssignmentSerial(Integer assignmentSerial) {
        this.assignmentSerial = assignmentSerial;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getAssignmentStatus() {
        return this.assignmentStatus;
    }

    public void setAssignmentStatus(String assignmentStatus) {
        this.assignmentStatus = assignmentStatus;
    }

    public Integer getScheduleAssignmentId() {
        return this.scheduleAssignmentId;
    }

    public void setScheduleAssignmentId(Integer scheduleAssignmentId) {
        this.scheduleAssignmentId = scheduleAssignmentId;
    }

    public Set getAssignmentFeedbacks() {
        return this.assignmentFeedbacks;
    }

    public void setAssignmentFeedbacks(Set assignmentFeedbacks) {
        this.assignmentFeedbacks = assignmentFeedbacks;
    }

    public Set getAssignmentScores() {
        return this.assignmentScores;
    }

    public void setAssignmentScores(Set assignmentScores) {
        this.assignmentScores = assignmentScores;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learnerAssignmentId", getLearnerAssignmentId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof LearnerAssignment) ) return false;
        LearnerAssignment castOther = (LearnerAssignment) other;
        return new EqualsBuilder()
            .append(this.getLearnerAssignmentId(), castOther.getLearnerAssignmentId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getLearnerAssignmentId())
            .toHashCode();
    }

}
