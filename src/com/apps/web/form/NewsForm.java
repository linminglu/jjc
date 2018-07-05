package com.apps.web.form;

import org.apache.struts.validator.ValidatorForm;

import com.ram.model.NewsInformation;

public class NewsForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String title;
	private Integer tid;
	private NewsInformation newsInfo=new NewsInformation();
	
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public NewsInformation getNewsInfo() {
		return newsInfo;
	}
	public void setNewsInfo(NewsInformation newsInfo) {
		this.newsInfo = newsInfo;
	}
	public Integer getTid() {
		return tid;
	}
	public void setTid(Integer tid) {
		this.tid = tid;
	}

}
