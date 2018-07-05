package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Speciality implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer specialityId;

    /** nullable persistent field */
    private String specialityTitle;

 

   

    /** full constructor */
    public Speciality(Integer specialityId, String specialityTitle) {
        this.specialityId = specialityId;
        this.specialityTitle = specialityTitle;
      
       
    }

    /** default constructor */
    public Speciality() {
    }

    /** minimal constructor */
    public Speciality(Integer specialityId) {
        this.specialityId = specialityId;      
    }

    public Integer getSpecialityId() {
        return this.specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
    }

    public String getSpecialityTitle() {
        return this.specialityTitle;
    }

    public void setSpecialityTitle(String specialityTitle) {
        this.specialityTitle = specialityTitle;
    }




    public String toString() {
        return new ToStringBuilder(this)
            .append("specialityId", getSpecialityId())
            .toString();
    }

}
