package com.ram.web.user.form;

import org.apache.struts.validator.ValidatorForm;

import com.ram.model.DeskSoftwareVersion;

public class DataBaseForm extends ValidatorForm {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String keyword;
	private String mysqls;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getMysqls() {
		return mysqls;
	}
	public void setMysqls(String mysqls) {
		this.mysqls = mysqls;
	}
	
	
	
}
