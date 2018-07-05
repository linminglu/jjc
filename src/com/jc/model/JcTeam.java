package com.jc.model;

public class JcTeam {

	private Integer Id;
	/** 一级分类*/
	private Integer tId1;
	/** 二级分类*/
	private Integer tId2;
	/** 队伍名称*/
	private String title;
	/** LOGO*/
	private String img;
	/** 说明*/
	private String remarks;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
