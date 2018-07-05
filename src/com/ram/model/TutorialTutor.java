package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TutorialTutor implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer columnTutorialTutorId;

    /** nullable persistent field */
    private Integer tutorUserId;

    /** nullable persistent field */
    private String tutorName;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** persistent field */
    private com.ram.model.TutorialSchedule tutorialSchedule;

    /** full constructor */
    public TutorialTutor(Integer tutorUserId, String tutorName, Integer updateUserId, Date updateDateTime, com.ram.model.TutorialSchedule tutorialSchedule) {
        this.tutorUserId = tutorUserId;
        this.tutorName = tutorName;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
        this.tutorialSchedule = tutorialSchedule;
    }

    /** default constructor */
    public TutorialTutor() {
    }

    /** minimal constructor */
    public TutorialTutor(com.ram.model.TutorialSchedule tutorialSchedule) {
        this.tutorialSchedule = tutorialSchedule;
    }

    public Integer getColumnTutorialTutorId() {
        return this.columnTutorialTutorId;
    }

    public void setColumnTutorialTutorId(Integer columnTutorialTutorId) {
        this.columnTutorialTutorId = columnTutorialTutorId;
    }

    public Integer getTutorUserId() {
        return this.tutorUserId;
    }

    public void setTutorUserId(Integer tutorUserId) {
        this.tutorUserId = tutorUserId;
    }

    public String getTutorName() {
        return this.tutorName;
    }

    public void setTutorName(String tutorName) {
        this.tutorName = tutorName;
    }

    public Integer getUpdateUserId() {
        return this.updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Date getUpdateDateTime() {
        return this.updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public com.ram.model.TutorialSchedule getTutorialSchedule() {
        return this.tutorialSchedule;
    }

    public void setTutorialSchedule(com.ram.model.TutorialSchedule tutorialSchedule) {
        this.tutorialSchedule = tutorialSchedule;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("columnTutorialTutorId", getColumnTutorialTutorId())
            .toString();
    }

}
