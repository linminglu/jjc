package com.apps.model.dto;

import com.apps.model.Advertising;

public class AdvertDTO {

	private Advertising advert=new Advertising();
	private String sname;
	private String title;

	public String getSname() {
		return sname;
	}

	public void setSname(String sname) {
		this.sname = sname;
	}

	public Advertising getAdvert() {
		return advert;
	}

	public void setAdvert(Advertising advert) {
		this.advert = advert;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
