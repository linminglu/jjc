package com.ram.model;

import java.io.Serializable;
import java.util.Date;
import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class GraduateStudentInfo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer graduateStudentInfoId;

    /** nullable persistent field */
    private Integer userId;

    /** nullable persistent field */
    private String password;

    /** nullable persistent field */
    private String userEmail;

    /** nullable persistent field */
    private String userNameZh;

    /** nullable persistent field */
    private String userNameEn;

    /** nullable persistent field */
    private Date birthday;

    /** nullable persistent field */
    private String specialityName;

    /** nullable persistent field */
    private String learnSex;

    /** nullable persistent field */
    private String education;

    /** nullable persistent field */
    private String workplace;

    /** nullable persistent field */
    private String tcCenterName;

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
    private String loginName;

    /** nullable persistent field */
    private Date graduatedate;

    /** nullable persistent field */
    private String idcardNumber;

    /** nullable persistent field */
    private String tccenterProvince;

    /** nullable persistent field */
    private String graduateSchoolCode;

    /** nullable persistent field */
    private String graduateSchoolName;

    /** nullable persistent field */
    private String diplomaNumber;

    /** nullable persistent field */
    private String politystation;

    /** nullable persistent field */
    private String nativeplace;

    /** nullable persistent field */
    private String nation;

    /** nullable persistent field */
    private String everusingname;
    
    private String programName;
    
    private String semesterName;
    
    private byte[] graduateImage; //毕业照片
    

    /** full constructor */
    public GraduateStudentInfo(Integer userId, String password, String userEmail, String userNameZh, String userNameEn, Date birthday, String specialityName, String learnSex, String education, String workplace, String tcCenterName, Integer semesterId, Integer programId, String enrollStatus, String studyNumber, String address, String postCode, String mobile, String homePhone, String msn, String qq, String loginName, Date graduatedate, String idcardNumber, String tccenterProvince, String graduateSchoolCode, String graduateSchoolName, String diplomaNumber, String politystation, String nativeplace, String nation, String everusingname,byte[] graduateImage) {
        this.userId = userId;
        this.password = password;
        this.userEmail = userEmail;
        this.userNameZh = userNameZh;
        this.userNameEn = userNameEn;
        this.birthday = birthday;
        this.specialityName = specialityName;
        this.learnSex = learnSex;
        this.education = education;
        this.workplace = workplace;
        this.tcCenterName = tcCenterName;
        this.semesterId = semesterId;
        this.programId = programId;
        this.enrollStatus = enrollStatus;
        this.studyNumber = studyNumber;
        this.address = address;
        this.postCode = postCode;
        this.mobile = mobile;
        this.homePhone = homePhone;
        this.msn = msn;
        this.qq = qq;
        this.loginName = loginName;
        this.graduatedate = graduatedate;
        this.idcardNumber = idcardNumber;
        this.tccenterProvince = tccenterProvince;
        this.graduateSchoolCode = graduateSchoolCode;
        this.graduateSchoolName = graduateSchoolName;
        this.diplomaNumber = diplomaNumber;
        this.politystation = politystation;
        this.nativeplace = nativeplace;
        this.nation = nation;
        this.everusingname = everusingname;
        this.graduateImage = graduateImage;
       
    }

    /** default constructor */
    public GraduateStudentInfo() {
    }

    public Integer getGraduateStudentInfoId() {
        return this.graduateStudentInfoId;
    }

    public void setGraduateStudentInfoId(Integer graduateStudentInfoId) {
        this.graduateStudentInfoId = graduateStudentInfoId;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserEmail() {
        return this.userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNameZh() {
        return this.userNameZh;
    }

    public void setUserNameZh(String userNameZh) {
        this.userNameZh = userNameZh;
    }

    public String getUserNameEn() {
        return this.userNameEn;
    }

    public void setUserNameEn(String userNameEn) {
        this.userNameEn = userNameEn;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    
    public String getSpecialityName() {
		return specialityName;
	}

	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}

	public String getLearnSex() {
        return this.learnSex;
    }

    public void setLearnSex(String learnSex) {
        this.learnSex = learnSex;
    }

    public String getEducation() {
        return this.education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getWorkplace() {
        return this.workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }
    
    public String getTcCenterName() {
		return tcCenterName;
	}

	public void setTcCenterName(String tcCenterName) {
		this.tcCenterName = tcCenterName;
	}

	public Integer getSemesterId() {
        return this.semesterId;
    }

    public void setSemesterId(Integer semesterId) {
        this.semesterId = semesterId;
    }

    public Integer getProgramId() {
        return this.programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getEnrollStatus() {
        return this.enrollStatus;
    }

    public void setEnrollStatus(String enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public String getStudyNumber() {
        return this.studyNumber;
    }

    public void setStudyNumber(String studyNumber) {
        this.studyNumber = studyNumber;
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

    public String getLoginName() {
        return this.loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getGraduatedate() {
        return this.graduatedate;
    }

    public void setGraduatedate(Date graduatedate) {
        this.graduatedate = graduatedate;
    }

    public String getIdcardNumber() {
        return this.idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getTccenterProvince() {
        return this.tccenterProvince;
    }

    public void setTccenterProvince(String tccenterProvince) {
        this.tccenterProvince = tccenterProvince;
    }

    public String getGraduateSchoolCode() {
        return this.graduateSchoolCode;
    }

    public void setGraduateSchoolCode(String graduateSchoolCode) {
        this.graduateSchoolCode = graduateSchoolCode;
    }

    public String getGraduateSchoolName() {
        return this.graduateSchoolName;
    }

    public void setGraduateSchoolName(String graduateSchoolName) {
        this.graduateSchoolName = graduateSchoolName;
    }

    public String getDiplomaNumber() {
        return this.diplomaNumber;
    }

    public void setDiplomaNumber(String diplomaNumber) {
        this.diplomaNumber = diplomaNumber;
    }

    public String getPolitystation() {
        return this.politystation;
    }

    public void setPolitystation(String politystation) {
        this.politystation = politystation;
    }

    public String getNativeplace() {
        return this.nativeplace;
    }

    public void setNativeplace(String nativeplace) {
        this.nativeplace = nativeplace;
    }

    public String getNation() {
        return this.nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getEverusingname() {
        return this.everusingname;
    }

    public void setEverusingname(String everusingname) {
        this.everusingname = everusingname;
    }

    public String getProgramName() {
		return programName;
	}

	public void setProgramName(String programName) {
		this.programName = programName;
	}

	public String getSemesterName() {
		return semesterName;
	}

	public void setSemesterName(String semesterName) {
		this.semesterName = semesterName;
	}

	public String toString() {
        return new ToStringBuilder(this)
            .append("graduateStudentInfoId", getGraduateStudentInfoId())
            .toString();
    }

	public byte[] getGraduateImage() {
		return graduateImage;
	}

	public void setGraduateImage(byte[] graduateImage) {
		this.graduateImage = graduateImage;
	}


	
}
