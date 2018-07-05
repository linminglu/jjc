package com.ram.web.user.form;

import org.apache.struts.validator.ValidatorForm;

import com.ram.model.DeskSoftwareVersion;

public class DeskGenTestForm extends ValidatorForm {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String code;
	private String keyword;
	private String sntype;
	private String sntaobao;
	private String status;
	private String sendTime;
	private String sendTime2;
	private String email;
	private String orderbys;
	private Integer count;
	private String remarks;
	private String sellprice;
	private String version;
	
	private DeskSoftwareVersion dsv = new DeskSoftwareVersion();
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getSntype() {
		return sntype;
	}
	public void setSntype(String sntype) {
		this.sntype = sntype;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSendTime2() {
		return sendTime2;
	}
	public void setSendTime2(String sendTime2) {
		this.sendTime2 = sendTime2;
	}
	public String getOrderbys() {
		return orderbys;
	}
	public void setOrderbys(String orderbys) {
		this.orderbys = orderbys;
	}
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public String getSntaobao() {
		return sntaobao;
	}
	public void setSntaobao(String sntaobao) {
		this.sntaobao = sntaobao;
	}
	
	public String getSellprice() {
		return sellprice;
	}
	public void setSellprice(String sellprice) {
		this.sellprice = sellprice;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public DeskSoftwareVersion getDsv() {
		return dsv;
	}
	public void setDsv(DeskSoftwareVersion dsv) {
		this.dsv = dsv;
	}
	
}
