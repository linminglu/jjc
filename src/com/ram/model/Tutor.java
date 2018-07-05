package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @autu Hibernate CodeGenerator */
public class Tutor implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer tutorId;

    /** nullable persistent field */
    

    /** nullable persistent field */
    private String address;

    /** nullable persistent field */
    private String postCode;

    /** nullable persistent field */
    private String mobile;

    /** nullable persistent field */
    private String homePhone;

    /** nullable persistent field */
    private String msn;

    /** nullable persistent field */
    private String qq;

    /** nullable persistent field */
    private Date birthday;
    
    private Integer isAppraise;

    /** nullable persistent field */
    private com.ram.model.User user;
    
    private com.ram.model.TutorCenter tutorCenter;
     
    private Set scheduleTutorCourses;
     

    /**
     * @return Returns the scheduleTutorCourses.
     */
    public Set getScheduleTutorCourses() {
        return scheduleTutorCourses;
    }
    /**
     * @param scheduleTutorCourses The scheduleTutorCourses to set.
     */
    public void setScheduleTutorCourses(Set scheduleTutorCourses) {
        this.scheduleTutorCourses = scheduleTutorCourses;
    }
    /** full constructor */
    public Tutor(Integer tcId, String address, String postCode, String mobile, String homePhone, String msn, String qq, Date birthday, Integer isAppraise, com.ram.model.User user,com.ram.model.TutorCenter tutorCenter,Set scheduleTutorCourses) {
        //this.tcId = tcId;
        this.address = address;
        this.postCode = postCode;
        this.mobile = mobile;
        this.homePhone = homePhone;
        this.msn = msn;
        this.qq = qq;
        this.birthday = birthday;
        this.user = user;
        this.tutorCenter=tutorCenter;
        this.scheduleTutorCourses=scheduleTutorCourses;
        this.isAppraise = isAppraise;
    }
    public Tutor(Set scheduleTutorCourses) {      
        this.scheduleTutorCourses=scheduleTutorCourses;
    }

    /** default constructor */
    public Tutor() {
    }
    
    public Tutor(Integer tutorId) {
        this.tutorId=tutorId;
    }
    
    public Integer getTutorId() {
        return this.tutorId;
    }

    public void setTutorId(Integer tutorId) {
        this.tutorId = tutorId;
    }

   

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getHomePhone() {
        return this.homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getMsn() {
        return this.msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getQq() {
        return this.qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public com.ram.model.User getUser() {
        return this.user;
    }

    public void setUser(com.ram.model.User user) {
        this.user = user;
    }

    public Integer getIsAppraise() {
		return isAppraise;
	}
    
	public void setIsAppraise(Integer isAppraise) {
		this.isAppraise = isAppraise;
	}
	
	public String toString() {
        return new ToStringBuilder(this)
            .append("tutorId", getTutorId())
            .toString();
    }

	public com.ram.model.TutorCenter getTutorCenter() {
		return tutorCenter;
	}

	public void setTutorCenter(com.ram.model.TutorCenter tutorCenter) {
		this.tutorCenter = tutorCenter;
	}

}
