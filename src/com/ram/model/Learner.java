package com.ram.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Learner implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** default constructor */
    public Learner() {
    }

    /** identifier field */
    private Integer learnerId;


    /** nullable persistent field */
    private Integer tcId;
    
    /** nullable persistent field */
    private Integer semesterId;
    
    /** nullable persistent field */
    private Integer programId;
    
    /** nullable persistent field */
    private String enrollStatus;
    
    /** nullable persistent field */
    private String studyNumber;

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
   
    /** nullable persistent field */
    private String learnSex;

    /** nullable persistent field */
    private String education;
    private Integer educationId;
    private String program;
    /** nullable persistent field */
    private Integer organizationId;
    private String workplace;
    private String workplaceOffset;
    
    /** nullable persistent field */
    private String workplacePhone;
    
    private String idCode;
    

	private String invoiceTitle;
	
	private String enrollNumber;

	private String cardType;
	private String graduatedSchool;
	
	private Date registeDate;    
	private String provice;
	private String city;
	private String certificate;
	
	private String registerName;
	private String isRegister;
	
	private String auditingExplain;
	private Date auditingDate;
	
	private Integer waveId;
	private Integer scheduleId;
	
	private Date startStudy;
	private Date endStudy;
	
	private String nationality;
	private String business;
	
	private String studyAffirInfo;
	private String systemInfo;
	private String financeInfo;
	
	private String graduateNumber;
    /** nullable persistent field */
    private User user = new User();
    
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getEnrollStatus() {
		return enrollStatus;
	}

	public void setEnrollStatus(String enrollStatus) {
		this.enrollStatus = enrollStatus;
	}

	public String getHomePhone() {
		return homePhone;
	}

	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}

	public Integer getLearnerId() {
		return learnerId;
	}

	public void setLearnerId(Integer learnerId) {
		this.learnerId = learnerId;
	}

	public String getLearnSex() {
		return learnSex;
	}

	public void setLearnSex(String learnSex) {
		this.learnSex = learnSex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMsn() {
		return msn;
	}

	public void setMsn(String msn) {
		this.msn = msn;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Integer getProgramId() {
		return programId;
	}

	public void setProgramId(Integer programId) {
		this.programId = programId;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Integer getSemesterId() {
		return semesterId;
	}

	public void setSemesterId(Integer semesterId) {
		this.semesterId = semesterId;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getWorkplace() {
		return workplace;
	}

	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	public String getWorkplacePhone() {
		return workplacePhone;
	}

	public void setWorkplacePhone(String workplacePhone) {
		this.workplacePhone = workplacePhone;
	}

	public String getIdCode() {
		return idCode;
	}

	public void setIdCode(String idCode) {
		this.idCode = idCode;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public String getEnrollNumber() {
		return this.enrollNumber;
	}

	public void setEnrollNumber(String enrollNumber) {
		this.enrollNumber = enrollNumber;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getGraduatedSchool() {
		return graduatedSchool;
	}

	public void setGraduatedSchool(String graduatedSchool) {
		this.graduatedSchool = graduatedSchool;
	}

	public Date getRegisteDate() {
		return registeDate;
	}

	public void setRegisteDate(Date registeDate) {
		this.registeDate = registeDate;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public Integer getEducationId() {
		return educationId;
	}

	public void setEducationId(Integer educationId) {
		this.educationId = educationId;
	}

	public String getProvice() {
		return provice;
	}

	public void setProvice(String provice) {
		this.provice = provice;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public String getWorkplaceOffset() {
		return workplaceOffset;
	}

	public void setWorkplaceOffset(String workplaceOffset) {
		this.workplaceOffset = workplaceOffset;
	}
	
	public String toString(){
		return new ToStringBuilder(this).append( "learnerId",this.getLearnerId() ).toString();
	}

	public String getIsRegister() {
		return isRegister;
	}

	public void setIsRegister(String isRegister) {
		this.isRegister = isRegister;
	}

	public String getProgram() {
		return program;
	}

	public void setProgram(String program) {
		this.program = program;
	}

	public String getRegisterName() {
		return registerName;
	}

	public void setRegisterName(String registerName) {
		this.registerName = registerName;
	}

	public Date getAuditingDate() {
		return auditingDate;
	}

	public void setAuditingDate(Date auditingDate) {
		this.auditingDate = auditingDate;
	}

	public String getAuditingExplain() {
		return auditingExplain;
	}

	public void setAuditingExplain(String auditingExplain) {
		this.auditingExplain = auditingExplain;
	}

	public Integer getWaveId() {
		return waveId;
	}

	public void setWaveId(Integer waveId) {
		this.waveId = waveId;
	}

	public Integer getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Integer scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Date getEndStudy() {
		return endStudy;
	}

	public void setEndStudy(Date endStudy) {
		this.endStudy = endStudy;
	}

	public Date getStartStudy() {
		return startStudy;
	}

	public void setStartStudy(Date startStudy) {
		this.startStudy = startStudy;
	}

	public String getBusiness() {
		return business;
	}

	public void setBusiness(String business) {
		this.business = business;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getFinanceInfo() {
		return financeInfo;
	}

	public void setFinanceInfo(String financeInfo) {
		this.financeInfo = financeInfo;
	}

	public String getStudyAffirInfo() {
		return studyAffirInfo;
	}

	public void setStudyAffirInfo(String studyAffirInfo) {
		this.studyAffirInfo = studyAffirInfo;
	}

	public String getSystemInfo() {
		return systemInfo;
	}

	public void setSystemInfo(String systemInfo) {
		this.systemInfo = systemInfo;
	}

	public String getGraduateNumber() {
		return graduateNumber;
	}

	public void setGraduateNumber(String graduateNumber) {
		this.graduateNumber = graduateNumber;
	}

	
}
