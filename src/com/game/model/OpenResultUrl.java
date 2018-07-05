package com.game.model;

   /**
    * OpenResultUrl 实体类  游戏开奖网址配置表
    * author:	ch
    * createDate:	2017-11-02 09:12:03
    */ 


public class OpenResultUrl{
	private Integer urlId;
	private String url;//开奖网址
	private String type;//网址来源   1=开彩网(或者找28)   2=彩票控
	private String gameType;//游戏类型
	private String gameName;//游戏名称
	public void setUrlId(Integer urlId){
	this.urlId=urlId;
	}
	public Integer getUrlId(){
		return urlId;
	}
	public void setUrl(String url){
	this.url=url;
	}
	public String getUrl(){
		return url;
	}
	public void setType(String type){
	this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setGameType(String gameType){
	this.gameType=gameType;
	}
	public String getGameType(){
		return gameType;
	}
	public void setGameName(String gameName){
	this.gameName=gameName;
	}
	public String getGameName(){
		return gameName;
	}
}
