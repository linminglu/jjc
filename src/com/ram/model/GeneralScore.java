package com.ram.model;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class GeneralScore implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer generalScoreId;

    private Integer userId;//
    private Integer semesterId;//学期
    private Integer programId;//层次
    private Integer tcId;//中心    
    private Integer courseId;//课程    
    private Integer scheduleCourseId;//计划课程
    private BigDecimal assignmentEndScore=new BigDecimal("0");//作业最终成绩    
    private BigDecimal selfTestEndScore=new BigDecimal("0"); //自测最终成绩   
    private BigDecimal examEndScore=new BigDecimal("0");   //考试最终成绩 
    private BigDecimal oralEndScore=new BigDecimal("0");   //口语最终成绩 
    private BigDecimal listenEndScore=new BigDecimal("0");   //听力最终成绩 
    private BigDecimal onlineEndScore=new BigDecimal("0"); //线上作业最终成绩   
    private BigDecimal offlineEndScore=new BigDecimal("0");//线下最终成绩
    private BigDecimal coursewareEndScore=new BigDecimal("0");//课件最终成绩
    private BigDecimal assitCoursewareEndScore=new BigDecimal("0");//辅导课件最终成绩
    private BigDecimal faceTechEndScore=new BigDecimal("0");//面授最终成绩    
    private BigDecimal otherEndScore=new BigDecimal("0");  //其他最终成绩  
    private BigDecimal courseEndScore=new BigDecimal("0");//课程最终成绩
    private BigDecimal sedAddScore=new BigDecimal("0");//第2次调整成绩
    private String isRelease="0";
    private String isCheat="0";
    private String courseStatus;


   
	/**
	 * @return Returns the courseStatus.
	 */
	public String getCourseStatus() {
		return courseStatus;
	}
	/**
	 * @param courseStatus The courseStatus to set.
	 */
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}
    /**
     * @return Returns the isCheat.
     */
    public String getIsCheat() {
        return isCheat;
    }
    /**
     * @param isCheat The isCheat to set.
     */
    public void setIsCheat(String isCheat) {
        this.isCheat = isCheat;
    }
    /**
     * @return Returns the isRelease.
     */
    public String getIsRelease() {
        return isRelease;
    }
    /**
     * @param isRelease The isRelease to set.
     */
    public void setIsRelease(String isRelease) {
        this.isRelease = isRelease;
    }
    /** full constructor */
    public GeneralScore(Integer userId,Integer semesterId,Integer programId,Integer tcId, Integer courseId, Integer scheduleCourseId, BigDecimal assignmentEndScore, BigDecimal selfTestEndScore, BigDecimal examEndScore,BigDecimal oralEndScore,BigDecimal listenEndScore, BigDecimal onlineEndScore, BigDecimal offlineEndScore, BigDecimal coursewareEndScore, BigDecimal assitCoursewareEndScore, BigDecimal faceTechEndScore, BigDecimal otherEndScore, BigDecimal courseEndScore,BigDecimal sedAddScore,String courseStatus,String isRelease) {
        this.userId = userId;
        this.semesterId = semesterId;
        this.programId = programId;
        this.tcId = tcId;
        this.courseId = courseId;
        this.scheduleCourseId = scheduleCourseId;
        this.assignmentEndScore = assignmentEndScore;
        this.selfTestEndScore = selfTestEndScore;
        this.examEndScore = examEndScore;
        this.oralEndScore=oralEndScore;
        this.listenEndScore=listenEndScore;
        this.onlineEndScore = onlineEndScore;
        this.offlineEndScore = offlineEndScore;
        this.coursewareEndScore = coursewareEndScore;
        this.assitCoursewareEndScore = assitCoursewareEndScore;
        this.faceTechEndScore = faceTechEndScore;
        this.otherEndScore = otherEndScore;
        this.courseEndScore = courseEndScore;  
        this.sedAddScore=sedAddScore;
        this.courseStatus=courseStatus;
        this.isRelease=isRelease;
    }

    /** default constructor */
    public GeneralScore() {
    }
    /**
     * @return Returns the sedAddScore.
     */
    public BigDecimal getSedAddScore() {
        return sedAddScore;
    }
    /**
     * @param sedAddScore The sedAddScore to set.
     */
    public void setSedAddScore(BigDecimal sedAddScore) {
        this.sedAddScore = sedAddScore;
    }
    /**
     * @return Returns the assitCoursewareEndScore.
     */
    public BigDecimal getAssitCoursewareEndScore() {
        return assitCoursewareEndScore;
    }
    /**
     * @param assitCoursewareEndScore The assitCoursewareEndScore to set.
     */
    public void setAssitCoursewareEndScore(BigDecimal assitCoursewareEndScore) {
        this.assitCoursewareEndScore = assitCoursewareEndScore;
    }
    /**
     * @return Returns the coursewareEndScore.
     */
    public BigDecimal getCoursewareEndScore() {
        return coursewareEndScore;
    }
    /**
     * @param coursewareEndScore The coursewareEndScore to set.
     */
    public void setCoursewareEndScore(BigDecimal coursewareEndScore) {
        this.coursewareEndScore = coursewareEndScore;
    }
    /**
     * @return Returns the faceTechEndScore.
     */
    public BigDecimal getFaceTechEndScore() {
        return faceTechEndScore;
    }
    /**
     * @param faceTechEndScore The faceTechEndScore to set.
     */
    public void setFaceTechEndScore(BigDecimal faceTechEndScore) {
        this.faceTechEndScore = faceTechEndScore;
    }
    /**
     * @return Returns the listenEndScore.
     */
    public BigDecimal getListenEndScore() {
        return listenEndScore;
    }
    /**
     * @param listenEndScore The listenEndScore to set.
     */
    public void setListenEndScore(BigDecimal listenEndScore) {
        this.listenEndScore = listenEndScore;
    }
   
    
    /**
     * @return Returns the oralEndScore.
     */
    public BigDecimal getOralEndScore() {
        return oralEndScore;
    }
    /**
     * @param oralEndScore The oralEndScore to set.
     */
    public void setOralEndScore(BigDecimal oralEndScore) {
        this.oralEndScore = oralEndScore;
    }
    /**
     * @return Returns the programId.
     */
    public Integer getProgramId() {
        return programId;
    }
    /**
     * @param programId The programId to set.
     */
    public void setProgramId(Integer programId) {
        this.programId = programId;
    }
    /**
     * @return Returns the semesterId.
     */
    public Integer getSemesterId() {
        return semesterId;
    }
    /**
     * @param semesterId The semesterId to set.
     */
    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }
    /**
     * @return Returns the tcId.
     */
    public Integer getTcId() {
        return tcId;
    }
    /**
     * @param tcId The tcId to set.
     */
    public void setTcId(Integer tcId) {
        this.tcId = tcId;
    }
    public Integer getGeneralScoreId() {
        return this.generalScoreId;
    }

    public void setGeneralScoreId(Integer generalScoreId) {
        this.generalScoreId = generalScoreId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCourseId() {
        return this.courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public Integer getScheduleCourseId() {
        return this.scheduleCourseId;
    }

    public void setScheduleCourseId(Integer scheduleCourseId) {
        this.scheduleCourseId = scheduleCourseId;
    }

    public BigDecimal getAssignmentEndScore() {
        return this.assignmentEndScore;
    }

    public void setAssignmentEndScore(BigDecimal assignmentEndScore) {
        this.assignmentEndScore = assignmentEndScore;
    }

    public BigDecimal getSelfTestEndScore() {
        return this.selfTestEndScore;
    }

    public void setSelfTestEndScore(BigDecimal selfTestEndScore) {
        this.selfTestEndScore = selfTestEndScore;
    }

    public BigDecimal getExamEndScore() {
        return this.examEndScore;
    }

    public void setExamEndScore(BigDecimal examEndScore) {
        this.examEndScore = examEndScore;
    }

    public BigDecimal getOnlineEndScore() {
        return this.onlineEndScore;
    }

    public void setOnlineEndScore(BigDecimal onlineEndScore) {
        this.onlineEndScore = onlineEndScore;
    }

    public BigDecimal getOfflineEndScore() {
        return this.offlineEndScore;
    }

    public void setOfflineEndScore(BigDecimal offlineEndScore) {
        this.offlineEndScore = offlineEndScore;
    }

    public BigDecimal getOtherEndScore() {
        return this.otherEndScore;
    }

    public void setOtherEndScore(BigDecimal otherEndScore) {
        this.otherEndScore = otherEndScore;
    }

    public BigDecimal getCourseEndScore() {
        return this.courseEndScore;
    }

    public void setCourseEndScore(BigDecimal courseEndScore) {
        this.courseEndScore = courseEndScore;
    }  

    public String toString() {
        return new ToStringBuilder(this)
            .append("generalScoreId", getGeneralScoreId())
            .toString();
    }

}
