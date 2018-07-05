package com.apps.model;
import java.io.Serializable;
import java.util.Date;

   /**
    * 黑名单
    * author:	ch
    * createDate:	2018-01-04 15:57:06
    */ 


public class BlackList  implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer bid;
	private String type;//type=1 value值是用户uid   type=2  value值是ip
	private String value;
	private Date createTime;//创建时间
	public void setBid(Integer bid){
		this.bid=bid;
	}
	public Integer getBid(){
		return bid;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setValue(String value){
	this.value=value;
	}
	public String getValue(){
		return value;
	}
	public void setCreateTime(Date createTime){
		this.createTime=createTime;
	}
	public Date getCreateTime(){
		return createTime;
	}
}
