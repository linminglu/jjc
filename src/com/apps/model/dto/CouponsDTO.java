package com.apps.model.dto;

import com.apps.model.Coupons;
import com.apps.model.CouponsLog;

public class CouponsDTO {

	private Coupons coupons = new Coupons();
	private CouponsLog cl = new CouponsLog();
	private String address;// 地址
	private String title;// 商店名
	private String code;// 商店名

	public CouponsDTO(Coupons coupons) {
		this.coupons = coupons;
	}

	public CouponsDTO() {
	}

	public CouponsDTO(Coupons coupons, String code) {
		this.coupons = coupons;
		this.code = code;
	}

	public CouponsDTO(Coupons coupons, CouponsLog cl) {
		this.coupons = coupons;
		this.cl = cl;
	}

	public CouponsLog getCl() {
		return cl;
	}

	public void setCl(CouponsLog cl) {
		this.cl = cl;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Coupons getCoupons() {
		return coupons;
	}

	public void setCoupons(Coupons coupons) {
		this.coupons = coupons;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
