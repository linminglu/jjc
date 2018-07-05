package com.game.model;
import java.math.BigDecimal;

   /**
    * GaHhmfBetOption 实体类
    * author:	ch
    * createDate:	2016-03-11 13:32:02
    */ 


public class GaHhmfBetOption{
	private Integer betOptionId;
	private String optionTitle;
	private String optionType;
	private BigDecimal betRate;
	private String sort;
	public void setBetOptionId(Integer betOptionId){
	this.betOptionId=betOptionId;
	}
	public Integer getBetOptionId(){
		return betOptionId;
	}
	public void setOptionTitle(String optionTitle){
	this.optionTitle=optionTitle;
	}
	public String getOptionTitle(){
		return optionTitle;
	}
	public void setOptionType(String optionType){
	this.optionType=optionType;
	}
	public String getOptionType(){
		return optionType;
	}
	public void setBetRate(BigDecimal betRate){
	this.betRate=betRate;
	}
	public BigDecimal getBetRate(){
		return betRate;
	}
	public void setSort(String sort){
	this.sort=sort;
	}
	public String getSort(){
		return sort;
	}
}
