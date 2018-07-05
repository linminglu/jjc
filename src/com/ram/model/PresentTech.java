package com.ram.model;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/** @author Hibernate CodeGenerator */
public class PresentTech implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer presentTechId;

    /** nullable persistent field */
    private String presentTechTitle;

    /** nullable persistent field */
    private String presentTechDesc;
    
    private Integer courseId;
    private Integer semesterId;

    /** persistent field */
    private Set tutorialSchedules;

    
    public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}


	public Integer getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Integer semesterId) {
		this.semesterId = semesterId;
	}

	/** full constructor */
    public PresentTech(Integer presentTechId,Integer scheduleId,Integer scheduleCourseId, String presentTechTitle, String presentTechDesc, Set tutorialSchedules) {
        this.presentTechId = presentTechId;
//        this.scheduleId = scheduleId;
//        this.scheduleCourseId = scheduleCourseId;
        this.presentTechTitle = presentTechTitle;
        this.presentTechDesc = presentTechDesc;
        this.tutorialSchedules = tutorialSchedules;
       
    }

    /** default constructor */
    public PresentTech() {
    }

    /** minimal constructor */
    public PresentTech(Set tutorialSchedules) {
        this.tutorialSchedules = tutorialSchedules;
        
    }

   

    public String toString() {
        return new ToStringBuilder(this)
            .append("presentTechId", getPresentTechId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof PresentTech) ) return false;
        PresentTech castOther = (PresentTech) other;
        return new EqualsBuilder()
            .append(this.getPresentTechId(), castOther.getPresentTechId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getPresentTechId())
            .toHashCode();
    }

	public String getPresentTechDesc() {
		return presentTechDesc;
	}

	public void setPresentTechDesc(String presentTechDesc) {
		this.presentTechDesc = presentTechDesc;
	}

	public Integer getPresentTechId() {
		return presentTechId;
	}

	public void setPresentTechId(Integer presentTechId) {
		this.presentTechId = presentTechId;
	}

	public String getPresentTechTitle() {
		return presentTechTitle;
	}

	public void setPresentTechTitle(String presentTechTitle) {
		this.presentTechTitle = presentTechTitle;
	}

	public Set getTutorialSchedules() {
		return tutorialSchedules;
	}

	public void setTutorialSchedules(Set tutorialSchedules) {
		this.tutorialSchedules = tutorialSchedules;
	}



}
