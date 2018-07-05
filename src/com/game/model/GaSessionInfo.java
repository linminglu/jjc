package com.game.model;

import java.io.Serializable;
import java.util.Date;

   /**
    * GaSessionInfo 实体类
    * author:	ch
    * createDate:	2017-04-12 10:00:36
    */ 


public class GaSessionInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer infoId;
	private String gameTitle;//游戏名字
	private String gameType;//游戏类型  
	private String gameCode;// 代码
	private String gameSet; //设置
	private String latestSessionNo;//最新未开奖期号
	private String openSessionNo;//最新已开奖期号
	private String openResult;//最新开奖结果
	private Date endTime;//时间
	private String img;//图片
	private Integer sort; //给彩票排序
	private String status; //状态。默认为1
	private String des; // 描述
	private String exp;//每日开奖期数说明
	private String betAvoid; //是否能投注
	private String isShowHistoryOpen;// 是否显示历史开奖
	private String isShow;// 是否在后台显示
	
	private String kaicaiUrl;
	private String caipiaoUrl;
	private String urlSwitch;
	
	private String playCate;//彩种类型  1=官方   2=信用
	
	public void setInfoId(Integer infoId){
		this.infoId=infoId;
	}
	public Integer getInfoId(){
		return infoId;
	}
	public void setGameTitle(String gameTitle){
	this.gameTitle=gameTitle;
	}
	public String getGameTitle(){
		return gameTitle;
	}
	public void setGameType(String gameType){
	this.gameType=gameType;
	}
	public String getGameType(){
		return gameType;
	}
	public void setLatestSessionNo(String latestSessionNo){
	this.latestSessionNo=latestSessionNo;
	}
	public String getLatestSessionNo(){
		return latestSessionNo;
	}
	public void setOpenSessionNo(String openSessionNo){
	this.openSessionNo=openSessionNo;
	}
	public String getOpenSessionNo(){
		return openSessionNo;
	}
	public void setOpenResult(String openResult){
	this.openResult=openResult;
	}
	public String getOpenResult(){
		return openResult;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * @return the sort
	 */
	public Integer getSort() {
		return sort;
	}
	/**
	 * @param sort the sort to set
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public String getIsShowHistoryOpen() {
		return isShowHistoryOpen;
	}
	public void setIsShowHistoryOpen(String isShowHistoryOpen) {
		this.isShowHistoryOpen = isShowHistoryOpen;
	}
	public String getIsShow() {
		return isShow;
	}
	public void setIsShow(String isShow) {
		this.isShow = isShow;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getDes() {
		return des;
	}
	public void setDes(String des) {
		this.des = des;
	}
	public String getExp() {
		return exp;
	}
	public void setExp(String exp) {
		this.exp = exp;
	}
	public String getGameCode() {
		return gameCode;
	}
	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}
	public String getGameSet() {
		return gameSet;
	}
	public void setGameSet(String gameSet) {
		this.gameSet = gameSet;
	}
	public String getBetAvoid() {
		return betAvoid;
	}
	public String getKaicaiUrl() {
		return kaicaiUrl;
	}
	public void setKaicaiUrl(String kaicaiUrl) {
		this.kaicaiUrl = kaicaiUrl;
	}
	public String getCaipiaoUrl() {
		return caipiaoUrl;
	}
	public void setCaipiaoUrl(String caipiaoUrl) {
		this.caipiaoUrl = caipiaoUrl;
	}
	public String getUrlSwitch() {
		return urlSwitch;
	}
	public void setUrlSwitch(String urlSwitch) {
		this.urlSwitch = urlSwitch;
	}
	public void setBetAvoid(String betAvoid) {
		this.betAvoid = betAvoid;
	}
	public String getPlayCate() {
		return playCate;
	}
	public void setPlayCate(String playCate) {
		this.playCate = playCate;
	}
	
}
