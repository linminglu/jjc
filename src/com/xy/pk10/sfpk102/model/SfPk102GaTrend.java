package com.xy.pk10.sfpk102.model;

   /**
    * BjPk10GaTrend 实体类
    * author:	ch
    * createDate:	2017-03-23 18:34:04
    */ 


public class SfPk102GaTrend{
	private Integer trendId;
	private String trendTitle;
	private Integer trendCount;
	public void setTrendId(Integer trendId){
	this.trendId=trendId;
	}
	public Integer getTrendId(){
		return trendId;
	}
	public void setTrendTitle(String trendTitle){
	this.trendTitle=trendTitle;
	}
	public String getTrendTitle(){
		return trendTitle;
	}
	public void setTrendCount(Integer trendCount){
	this.trendCount=trendCount;
	}
	public Integer getTrendCount(){
		return trendCount;
	}
}
