package com.apps.web.form;

import org.apache.struts.upload.FormFile;
import org.apache.struts.validator.ValidatorForm;

import com.apps.model.Seller;

public class SellerForm extends ValidatorForm {

	private static final long serialVersionUID = 1L;

	private Seller seller = new Seller();
	private String startDate;
	private String endDate;
	private Integer totalCount;
	// private Integer tid;
	private FormFile file;
	private String type;// 1.电商模块2.订餐模块3.团购模块
	// private Integer tid1;
	// private Integer tid2;

	private Integer columnId;// 首页栏目id
	private Integer tid;// 二级栏目id
	private Integer tid3;// 三级栏目id

	private String title;
	private Integer cid;
	private Integer busaid;
	private Integer ccid;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getColumnId() {
		return columnId;
	}

	public void setColumnId(Integer columnId) {
		this.columnId = columnId;
	}

	public Integer getTid3() {
		return tid3;
	}

	public void setTid3(Integer tid3) {
		this.tid3 = tid3;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getTid() {
		return tid;
	}

	public void setTid(Integer tid) {
		this.tid = tid;
	}

	public FormFile getFile() {
		return file;
	}

	public void setFile(FormFile file) {
		this.file = file;
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

	public Integer getCid() {
		return cid;
	}

	public void setCid(Integer cid) {
		this.cid = cid;
	}

	public Integer getBusaid() {
		return busaid;
	}

	public void setBusaid(Integer busaid) {
		this.busaid = busaid;
	}

	public Integer getCcid() {
		return ccid;
	}

	public void setCcid(Integer ccid) {
		this.ccid = ccid;
	}

}
