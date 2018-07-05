package com.apps.model.dto;

import com.ram.model.NewsInformation;

public class NewsDTO {

	private NewsInformation newsInfo=new NewsInformation();
	private String typeTitle;
	
	public NewsDTO(){
		
	}
	public NewsDTO(NewsInformation newsInfo,String typeTitle){
		this.newsInfo = newsInfo;
		this.typeTitle = typeTitle;
	}
	
	public NewsInformation getNewsInfo() {
		return newsInfo;
	}
	public void setNewsInfo(NewsInformation newsInfo) {
		this.newsInfo = newsInfo;
	}
	public String getTypeTitle() {
		return typeTitle;
	}
	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}
	
	
	
	
	
}
