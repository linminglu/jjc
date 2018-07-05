package com.ram.model;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class Manager implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** identifier field */
    private Integer managerId;

    /** nullable persistent field */
    private Integer tcId;

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

    /** persistent field */
    private com.ram.model.User user;

    /** full constructor */
    public Manager(Integer tcId, String address, String postCode, String mobile, String homePhone, String msn, String qq, Date birthday, com.ram.model.User user) {
        this.tcId = tcId;
        this.address = address;
        this.postCode = postCode;
        this.mobile = mobile;
        this.homePhone = homePhone;
        this.msn = msn;
        this.qq = qq;
        this.birthday = birthday;
        this.user = user;
    }

    /** default constructor */
    public Manager() {
    }

    /** minimal constructor */
    public Manager(com.ram.model.User user) {
        this.user = user;
    }

    public Integer getManagerId() {
        return this.managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getTcId() {
        return this.tcId;
    }

    public void setTcId(Integer tcId) {
        this.tcId = tcId;
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
    
    public String toString() {
        return new ToStringBuilder(this)
            .append("managerId", getManagerId())
            .toString();
    }

}
