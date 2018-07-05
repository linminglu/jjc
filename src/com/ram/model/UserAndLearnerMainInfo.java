package com.ram.model;

import java.io.Serializable;


/** @author Hibernate CodeGenerator */
public class UserAndLearnerMainInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer userId ;

    /** nullable persistent field */
    private String loginName;

    /** nullable persistent field */
    private String userNameZh;
    
    /** identifier field */
    private Integer learnerId ;
    
    /** nullable persistent field */
    private Integer tcId ;
        
    /** nullable persistent field */
    private String studyNumber;

    /** full constructor */
    public UserAndLearnerMainInfo(Integer userId,String loginName, String userNameZh,Integer learnerId,Integer tcId,String studyNumber ) {
        this.userId = userId;
    	this.loginName = loginName;
        this.userNameZh = userNameZh;
        this.learnerId = learnerId;
        this.tcId = tcId;
        this.studyNumber = studyNumber;
    }

	public Integer getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(Integer learnerId) {
		this.learnerId = learnerId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getStudyNumber() {
		return studyNumber;
	}

	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}

	public Integer getTcId() {
		return tcId;
	}

	public void setTcId(Integer tcId) {
		this.tcId = tcId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserNameZh() {
		return userNameZh;
	}

	public void setUserNameZh(String userNameZh) {
		this.userNameZh = userNameZh;
	}
    
}

