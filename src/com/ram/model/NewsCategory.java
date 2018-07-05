package com.ram.model;

   /**
    * 
    * 新闻资讯类别表：网站公告  帮助中心   焦点新闻  单场竞技  数字彩  高频彩
    * NewsCategory 实体类   
    * author:	ch
    * createDate:	2017-07-25 09:35:08
    */ 


public class NewsCategory{
	private Integer cid;
	private String title;
	private String status;
	private String type; //0= 网站公告  1=帮助中心   2=焦点新闻  3=单场竞技  4=数字彩  5=高频彩   6=充值指南  7=提款帮助 8=常见问题  9=网站帮助
	private Integer sort;
	public void setCid(Integer cid){
	this.cid=cid;
	}
	public Integer getCid(){
		return cid;
	}
	public void setTitle(String title){
	this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setStatus(String status){
	this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setSort(Integer sort){
	this.sort=sort;
	}
	public Integer getSort(){
		return sort;
	}
}
