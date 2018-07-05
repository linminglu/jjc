package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Program implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer programId;

    /** nullable persistent field */
    private String programName;

    /** nullable persistent field */
    private Integer updateUserId;

    /** nullable persistent field */
    private Date updateDateTime;

    /** persistent field */
    private Set businessGroups;

    /** full constructor */
    public Program(String programName, Integer updateUserId, Date updateDateTime, Set businessGroups) {
        this.programName = programName;
        this.updateUserId = updateUserId;
        this.updateDateTime = updateDateTime;
        this.businessGroups = businessGroups;
    }

    /** default constructor */
    public Program() {
    }

    /** minimal constructor */
    public Program(Set businessGroups) {
        this.businessGroups = businessGroups;
    }

    public Integer getProgramId() {
        return this.programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
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

    public Set getBusinessGroups() {
        return this.businessGroups;
    }

    public void setBusinessGroups(Set businessGroups) {
        this.businessGroups = businessGroups;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("programId", getProgramId())
            .toString();
    }

}
