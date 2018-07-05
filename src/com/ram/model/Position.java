package com.ram.model;

import java.io.Serializable;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Position implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer positionId;

    /** nullable persistent field */
    private String positionTitle;

    /** persistent field */
    private Set managerPositionRls;

    /** full constructor */
    public Position(String positionTitle, Set managerPositionRls) {
        this.positionTitle = positionTitle;
        this.managerPositionRls = managerPositionRls;
    }

    /** default constructor */
    public Position() {
    }

    /** minimal constructor */
    public Position(Set managerPositionRls) {
        this.managerPositionRls = managerPositionRls;
    }

    public Integer getPositionId() {
        return this.positionId;
    }

    public void setPositionId(Integer positionId) {
        this.positionId = positionId;
    }

    public String getPositionTitle() {
        return this.positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public Set getManagerPositionRls() {
        return this.managerPositionRls;
    }

    public void setManagerPositionRls(Set managerPositionRls) {
        this.managerPositionRls = managerPositionRls;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("positionId", getPositionId())
            .toString();
    }

}
