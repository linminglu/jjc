package com.jc.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class JcMatch {

	private Integer mId;
	/** 标题*/
	private String title;
	/** 队伍1名称*/
	private String team1Name;
	/** 队伍1LOGO*/
	private String team1Img;
	/** 队伍2名称*/
	private String team2Name;
	/** 队伍2LOGO*/
	private String team2Img;
	/** 副标题*/
	private String subTitle;
	/** 开奖时间*/
	private Date openTime;
	/** 比赛时间*/
	private Date matchTime;
	/** 比赛场次：1=BO1,3=BO3,5=BO5,7=BO7*/
	private Integer boNum;
	/** 红方（主场）*/
	private String red;
	/** 蓝方（客场）*/
	private String blue;
	/** 类型：1=对战，2=单*/
	private String type;
	/** 抽水比例*/
	private String rercentage;
	/** 投注金额*/
	private BigDecimal totalPrice;
	/** 一级分类*/
	private Integer tId1;
	/** 二级分类*/
	private Integer tId2;
	/** 是否推荐：1=是，0=否*/
	private String isRecommend;
	
	
	private List<JcField> fieldList;
	
	

	public Integer getmId() {
		return mId;
	}

	public void setmId(Integer mId) {
		this.mId = mId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTeam1Name() {
		return team1Name;
	}

	public void setTeam1Name(String team1Name) {
		this.team1Name = team1Name;
	}

	public String getTeam1Img() {
		return team1Img;
	}

	public void setTeam1Img(String team1Img) {
		this.team1Img = team1Img;
	}

	public String getTeam2Name() {
		return team2Name;
	}

	public void setTeam2Name(String team2Name) {
		this.team2Name = team2Name;
	}

	public String getTeam2Img() {
		return team2Img;
	}

	public void setTeam2Img(String team2Img) {
		this.team2Img = team2Img;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public Date getMatchTime() {
		return matchTime;
	}

	public void setMatchTime(Date matchTime) {
		this.matchTime = matchTime;
	}

	public Integer getBoNum() {
		return boNum;
	}

	public void setBoNum(Integer boNum) {
		this.boNum = boNum;
	}

	public String getRed() {
		return red;
	}

	public void setRed(String red) {
		this.red = red;
	}

	public String getBlue() {
		return blue;
	}

	public void setBlue(String blue) {
		this.blue = blue;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRercentage() {
		return rercentage;
	}

	public void setRercentage(String rercentage) {
		this.rercentage = rercentage;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Integer gettId1() {
		return tId1;
	}

	public void settId1(Integer tId1) {
		this.tId1 = tId1;
	}

	public Integer gettId2() {
		return tId2;
	}

	public void settId2(Integer tId2) {
		this.tId2 = tId2;
	}

	public List<JcField> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<JcField> fieldList) {
		this.fieldList = fieldList;
	}

	public String getIsRecommend() {
		return isRecommend;
	}

	public void setIsRecommend(String isRecommend) {
		this.isRecommend = isRecommend;
	}
}
