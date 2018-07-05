/**
 * @(#)QueryForm.java 1.0 06/08/29
 */

package com.ram.model;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * 修定后的毕业信息
 * @author Lu Congyu
 */
public class UpdateRequestGraduate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String studyNumber; //学籍号
	private String userNameZh;  //姓名
	private String zsbh;		//毕业证书编号
	private String graduateSchool; //毕业学校
	private String gender;		//性别
	private String tcName;		//学习中心名称
	private String specialty;	//专业层次
	private String regDate;		//注册日期
	private String cardType;	//证件类型
	private String idcard; 		//证件编号
	private String birthday;	//生日
	private String nativeplace; //籍贯
	private String nation;		//民族
	private String politystation; //政治面貌
	private String homePhone;	//家庭电话
	private String workPhone;	//办公电话
	private String workplace;	//公司
	private String userEmail;	//电子邮件
	private String mobile;		//手机号
	private String postCode;	//邮编
	private String address;		//邮政地址
	private String resume;		//自我鉴定
	private String submitTime; 	//毕业申请日期
	private String everusingname; //曾用名  
	


	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getCardType() {
		return cardType;
	}
	
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	
	public String getEverusingname() {
		return everusingname;
	}
	
	public void setEverusingname(String everusingname) {
		this.everusingname = everusingname;
	}
	
	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public String getGraduateSchool() {
		return graduateSchool;
	}
	
	public void setGraduateSchool(String graduateSchool) {
		this.graduateSchool = graduateSchool;
	}
	
	public String getHomePhone() {
		return homePhone;
	}
	
	public void setHomePhone(String homePhone) {
		this.homePhone = homePhone;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getIdcard() {
		return idcard;
	}
	
	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}
	
	public String getMobile() {
		return mobile;
	}
	
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getNation() {
		return nation;
	}
	
	public void setNation(String nation) {
		this.nation = nation;
	}
	
	public String getNativeplace() {
		return nativeplace;
	}
	
	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}
	
	public String getPolitystation() {
		return politystation;
	}
	
	public void setPolitystation(String politystation) {
		this.politystation = politystation;
	}
	
	public String getPostCode() {
		return postCode;
	}
	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}
	
	public String getRegDate() {
		return regDate;
	}
	
	public void setRegDate(String regDate) {
		this.regDate = regDate;
	}
	
	public String getResume() {
		return resume;
	}
	
	public void setResume(String resume) {
		this.resume = resume;
	}
	
	public String getSpecialty() {
		return specialty;
	}
	
	public void setSpecialty(String specialty) {
		this.specialty = specialty;
	}
	
	public String getStudyNumber() {
		return studyNumber;
	}
	
	public void setStudyNumber(String studyNumber) {
		this.studyNumber = studyNumber;
	}
	
	public String getSubmitTime() {
		return submitTime;
	}
	
	public void setSubmitTime(String submitTime) {
		this.submitTime = submitTime;
	}
	
	public String getTcName() {
		return tcName;
	}
	
	public void setTcName(String tcName) {
		this.tcName = tcName;
	}
	
	public String getUserEmail() {
		return userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	
	public String getUserNameZh() {
		return userNameZh;
	}
	
	public void setUserNameZh(String userNameZh) {
		this.userNameZh = userNameZh;
	}
	
	public String getWorkPhone() {
		return workPhone;
	}
	
	public void setWorkPhone(String workPhone) {
		this.workPhone = workPhone;
	}
	
	public String getWorkplace() {
		return workplace;
	}
	
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}
	
	public String getZsbh() {
		return zsbh;
	}
	
	public void setZsbh(String zsbh) {
		this.zsbh = zsbh;
	}

    public String toString() {
        return new ToStringBuilder(this)
            .append("Id", getId())
            .toString();
    }
}