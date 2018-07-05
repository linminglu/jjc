package com.ram.model;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class LearnerGroupMember implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer learnerGroupMemebersId;

    /** nullable persistent field */
    private String isGroupChairman;
    
    private Integer userId;
    

    /** persistent field */
    private com.ram.model.LearnerGroup learnerGroup;

    /** full constructor */
    public LearnerGroupMember(String isGroupChairman,Integer userId, com.ram.model.LearnerGroup learnerGroup) {
        this.isGroupChairman = isGroupChairman;
        this.userId=userId;
        this.learnerGroup = learnerGroup;
    }

    /** default constructor */
    public LearnerGroupMember() {
    }

    /** minimal constructor */
    public LearnerGroupMember(com.ram.model.LearnerGroup learnerGroup) {
        this.learnerGroup = learnerGroup;
    }

    public Integer getLearnerGroupMemebersId() {
        return this.learnerGroupMemebersId;
    }

    public void setLearnerGroupMemebersId(Integer learnerGroupMemebersId) {
        this.learnerGroupMemebersId = learnerGroupMemebersId;
    }

    public String getIsGroupChairman() {
        return this.isGroupChairman;
    }

    public void setIsGroupChairman(String isGroupChairman) {
        this.isGroupChairman = isGroupChairman;
    }

    public com.ram.model.LearnerGroup getLearnerGroup() {
        return this.learnerGroup;
    }

    public void setLearnerGroup(com.ram.model.LearnerGroup learnerGroup) {
        this.learnerGroup = learnerGroup;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("learnerGroupMemebersId", getLearnerGroupMemebersId())
            .toString();
    }

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

}
