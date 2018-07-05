package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/** @author Hibernate CodeGenerator */
public class TutorialSchedule implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
	private Integer tutorialScheduleId;

	/** nullable persistent field */
	private String tutorialSubject;

	/** nullable persistent field */
	private String tutorialDate;

	/** nullable persistent field */
	private String tutorialDay;

	/** nullable persistent field */
	private String tutorialTime;

   private com.ram.model.PresentTech presentTech;

	/** persistent field */
	// private com.ram.model.Course course;

	/** full constructor */
	public TutorialSchedule(Integer tutorialScheduleId,
			String tutorialSubject, String tutorialDate, String tutorialDay,
			String tutorialTime,com.ram.model.PresentTech presentTech) {
		
		this.tutorialScheduleId = tutorialScheduleId;
        this.tutorialSubject = tutorialSubject;
		this.tutorialDate = tutorialDate;
		this.tutorialDay = tutorialDay;
		this.tutorialTime = tutorialTime;
		this.presentTech = presentTech;
	}

	/** default constructor */
	public TutorialSchedule() {
	}


	public com.ram.model.PresentTech getPresentTech() {
		return presentTech;
	}

	public void setPresentTech(com.ram.model.PresentTech presentTech) {
		this.presentTech = presentTech;
	}

	public String getTutorialDate() {
		return tutorialDate;
	}

	public void setTutorialDate(String tutorialDate) {
		this.tutorialDate = tutorialDate;
	}

	public String getTutorialDay() {
		return tutorialDay;
	}

	public void setTutorialDay(String tutorialDay) {
		this.tutorialDay = tutorialDay;
	}

	public Integer getTutorialScheduleId() {
		return tutorialScheduleId;
	}

	public void setTutorialScheduleId(Integer tutorialScheduleId) {
		this.tutorialScheduleId = tutorialScheduleId;
	}

	public String getTutorialSubject() {
		return tutorialSubject;
	}

	public void setTutorialSubject(String tutorialSubject) {
		this.tutorialSubject = tutorialSubject;
	}

	public String getTutorialTime() {
		return tutorialTime;
	}

	public void setTutorialTime(String tutorialTime) {
		this.tutorialTime = tutorialTime;
	}

	public String toString() {
		return new ToStringBuilder(this).append("tutorialScheduleId",
				getTutorialScheduleId()).toString();
	}

}
