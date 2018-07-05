package com.apps.model;

import java.io.Serializable;

/**
 * 商家和类型对应表 </br>
 * 
 * @author Mr.zang
 * 
 */
public class SellerTypeRl implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer rid;
	private Integer sid;// 商家id
	private Integer tid;// 类型id

	public SellerTypeRl() {
	}

	public SellerTypeRl( Integer sid, Integer tid) {
		this.sid = sid;
		this.tid = tid;
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

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

}
