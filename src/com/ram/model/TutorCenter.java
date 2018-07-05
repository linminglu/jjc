package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class TutorCenter implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer tcId;

    /** nullable persistent field */
    private String tcTitle;

    /** nullable persistent field */
    private String tcCode;

    /** nullable persistent field */
    private String tcAddress;

    /** nullable persistent field */
    private String tcPostCode;

    /** nullable persistent field */
    private String tcTelephone;

    /** nullable persistent field */
    private String tcFax;

    /** nullable persistent field */
    private String tcEmail;

    private String nation;

    /** nullable persistent field */
    private Integer parentTcId;
    /** nullable persistent field */
    private String haveSubTc;
    private Integer tcLevel;
    
    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    private Set businessGroups;
    private Set tutors;
    private Set learnerGroups;
    public Set getLearnerGroups() {
		return learnerGroups;
	}

	public void setLearnerGroups(Set learnerGroups) {
		this.learnerGroups = learnerGroups;
	}

	public Set getTutors() {
		return tutors;
	}

	public void setTutors(Set tutors) {
		this.tutors = tutors;
	}
	

	/** full constructor */
    public TutorCenter(String tcTitle, String tcCode, String tcAddress, String tcPostCode, String tcTelephone, String tcFax, String tcEmail,  Integer parentTcId,String haveSubTc,Integer tcLevel, Integer updateUserId, Date updateDateTime, String status) {
        this.tcTitle = tcTitle;
        this.tcCode = tcCode;
        this.tcAddress = tcAddress;
        this.tcPostCode = tcPostCode;
        this.tcTelephone = tcTelephone;
        this.tcFax = tcFax;
        this.tcEmail = tcEmail;
        this.parentTcId = parentTcId;
        this.haveSubTc=haveSubTc;
        this.tcLevel=tcLevel;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
       
    }

    /** default constructor */
    public TutorCenter() {
    }

    public Integer getTcId() {
        return this.tcId;
    }

    public void setTcId(Integer tcId) {
        this.tcId = tcId;
    }

    public String getTcTitle() {
        return this.tcTitle;
    }

    public void setTcTitle(String tcTitle) {
        this.tcTitle = tcTitle;
    }

    public String getTcCode() {
        return this.tcCode;
    }

    public void setTcCode(String tcCode) {
        this.tcCode = tcCode;
    }

    public String getTcAddress() {
        return this.tcAddress;
    }

    public void setTcAddress(String tcAddress) {
        this.tcAddress = tcAddress;
    }

    public String getTcPostCode() {
        return this.tcPostCode;
    }

    public void setTcPostCode(String tcPostCode) {
        this.tcPostCode = tcPostCode;
    }

    public String getTcTelephone() {
        return this.tcTelephone;
    }

    public void setTcTelephone(String tcTelephone) {
        this.tcTelephone = tcTelephone;
    }

    public String getTcFax() {
        return this.tcFax;
    }

    public void setTcFax(String tcFax) {
        this.tcFax = tcFax;
    }

    public String getTcEmail() {
        return this.tcEmail;
    }

    public void setTcEmail(String tcEmail) {
        this.tcEmail = tcEmail;
    }



    public Integer getParentTcId() {
        return this.parentTcId;
    }

    public void setParentTcId(Integer parentTcId) {
        this.parentTcId = parentTcId;
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

	public String getHaveSubTc() {
		return haveSubTc;
	}

	public void setHaveSubTc(String haveSubTc) {
		this.haveSubTc = haveSubTc;
	}

	public Integer getTcLevel() {
		return tcLevel;
	}

	public void setTcLevel(Integer tcLevel) {
		this.tcLevel = tcLevel;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("tcId", getTcId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof TutorCenter) ) return false;
        TutorCenter castOther = (TutorCenter) other;
        return new EqualsBuilder()
            .append(this.getTcId(), castOther.getTcId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getTcId())
            .toHashCode();
    }
    public Set getBusinessGroups() {
		return businessGroups;
	}

	public void setBusinessGroups(Set businessGroups) {
		this.businessGroups = businessGroups;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

}
