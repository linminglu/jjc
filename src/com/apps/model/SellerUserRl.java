package com.apps.model;

import java.io.Serializable;

/**
 * 商家和管理用户对应表 </br>
 * 
 * @author Mr.zang
 * 
 */
public class SellerUserRl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer rid;
	private Integer sid;// 商家id
	private Integer uid;// 用户id
	private String type;// 1.普通员工 2.派单人员 3.送货人员  4.管理员
	private String userName;// 姓名

	public SellerUserRl() {
	}

	public SellerUserRl(Integer sid, Integer uid,String type,String userName) {
		this.sid = sid;
		this.uid = uid;
		this.type = type;
		this.userName = userName;
	}
	
	public SellerUserRl(Integer sid, Integer uid) {
		this.sid = sid;
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

}
