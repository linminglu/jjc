package com.ram.model;

   /**
    * NewsType 实体类
    * author:	ch
    * createDate:	2017-07-19 19:36:19
    */ 


public class NewsType{
	private Integer tid;
	private String title;//名称
	private String status;//状态
	private Integer sort;//排序
	public void setTid(Integer tid){
	this.tid=tid;
	}
	public Integer getTid(){
		return tid;
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
	public void setSort(Integer sort){
	this.sort=sort;
	}
	public Integer getSort(){
		return sort;
	}
}
