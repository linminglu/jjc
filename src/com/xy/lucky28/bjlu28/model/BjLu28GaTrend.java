package com.xy.lucky28.bjlu28.model;

   /**
    * BjLu28GaTrend 实体类    冷热排行表
    * author:	ch
    * createDate:	2017-03-23 18:32:36
    */ 


public class BjLu28GaTrend{
	private Integer trendId;
	private String trendTitle;//排行名字 
	private Integer trendCount;//累计期数
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
