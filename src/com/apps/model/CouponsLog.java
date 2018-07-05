package com.apps.model;

import java.io.Serializable;
import java.util.Date;

/**
 * 优惠券记录
 * 
 * @author Mr.zang
 */
public class CouponsLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer couponsId;//
	private Integer uid;//
	private Integer points;// 积分
	private Date createDate;// 数据时间
	private String status = "1";// 状态 1.未使用 2.已使用
	private String code = "";// 兑换码
	private String ip = "";// 

	public CouponsLog() {
	}

	public CouponsLog(Integer couponsId, Integer uid, Integer points,
			Date createDate, String status) {
		this.couponsId = couponsId;
		this.uid = uid;
		this.points = points;
		this.createDate = createDate;
		this.status = status;
	}

	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCouponsId() {
		return couponsId;
	}

	public void setCouponsId(Integer couponsId) {
		this.couponsId = couponsId;
	}

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
