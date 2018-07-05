package com.apps.model;

   /**
    * City 实体类
    * author:	ch
    * createDate:	2017-04-14 20:03:45
    */ 


public class City{
	private Integer cid;
	private String title;
	private String type;
	private Integer parentId;
	private String sort;
	private String pinyin;
	private String code;
	private String status;
	private String isMunicipality;

	public void setTitle(String title){
	this.title=title;
	}
	public String getTitle(){
		return title;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}

	public void setSort(String sort){
	this.sort=sort;
	}
	public String getSort(){
		return sort;
	}
	public void setPinyin(String pinyin){
	this.pinyin=pinyin;
	}
	public String getPinyin(){
		return pinyin;
	}
	public void setCode(String code){
	this.code=code;
	}
	public String getCode(){
		return code;
	}
	public void setStatus(String status){
	this.status=status;
	}
	public String getStatus(){
		return status;
	}
	public void setIsMunicipality(String isMunicipality){
	this.isMunicipality=isMunicipality;
	}
	public String getIsMunicipality(){
		return isMunicipality;
	}
	public Integer getCid() {
		return cid;
	}
	public void setCid(Integer cid) {
		this.cid = cid;
	}
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
}
