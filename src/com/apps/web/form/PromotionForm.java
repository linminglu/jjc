package com.apps.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Promotion;
import com.apps.model.Seller;

public class PromotionForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private Promotion promotion = new Promotion();

	private Seller seller = new Seller();
	private Integer sid;
	private String startDate;
	private String endDate;
	private Integer totalCount;
	private String startIndex;
	private FormFile file;

	public Promotion getPromotion() {
		return promotion;
	}

	public void setPromotion(Promotion promotion) {
		this.promotion = promotion;
	}

	public String getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(String startIndex) {
		this.startIndex = startIndex;
	}

	public Integer getSid() {
		return sid;
	}

	public void setSid(Integer sid) {
		this.sid = sid;
	}

	public Seller getSeller() {
		return seller;
	}

	public void setSeller(Seller seller) {
		this.seller = seller;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
	}

}
