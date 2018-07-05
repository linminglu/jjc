package com.apps.model;
import java.math.BigDecimal;

   /**
    * SysOption 实体类
    * author:	yangxiaojun
    * createDate:	2015-10-30 21:21:15
    */ 


public class SysOption{
	private Integer optionId;
	private String optionType;
	private String optionTitle;
	private BigDecimal points;
	private String unitType;//单位类型 1=元，2=积分
	private String content;
	public void setOptionId(Integer optionId){
	this.optionId=optionId;
	}
	public Integer getOptionId(){
		return optionId;
	}
	public void setOptionType(String optionType){
	this.optionType=optionType;
	}
	public String getOptionType(){
		return optionType;
	}
	public void setOptionTitle(String optionTitle){
	this.optionTitle=optionTitle;
	}
	public String getOptionTitle(){
		return optionTitle;
	}
	public void setPoints(BigDecimal points){
	this.points=points;
	}
	public BigDecimal getPoints(){
		return points;
	}
	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
