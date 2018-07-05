package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Coupons;

public class CouponsForm extends ValidatorForm{
	private static final long serialVersionUID = 1L;
	private Coupons coupons=new Coupons();
	private String startDate;
	private String endDate;
	private Integer sid;
	private String id;
	
	private Integer totalCount;
	private String startIndex;
	
	private String  validDate;
	private String  type;
	private String  couponstype;
	private String  couponstp;
	
	
	
	public String getCouponstp() {
		couponstp=coupons.getTp();
		return couponstp;
	}
	public void setCouponstp(String couponstp) {
		couponstp=coupons.getTp();
		this.couponstp = couponstp;
	}
	public String getCouponstype() {
		couponstype=coupons.getType();
		return couponstype;
	}
	public void setCouponstype(String couponstype) {
		couponstype=coupons.getType();
		this.couponstype = couponstype;
	}
	public String getValidDate() {
		return validDate;
	}
	public void setValidDate(String validDate) {
		this.validDate = validDate;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public String getStartIndex() {
		return startIndex;
	}
	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}
	public Coupons getCoupons() {
		return coupons;
	}
	public void setCoupons(Coupons coupons) {
		this.coupons = coupons;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public Integer getSid() {
		return sid;
	}
	public void setSid(Integer sid) {
		this.sid = sid;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
}
