package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分类
 * 
 * @author Mr.zang
 * 
 */
public class Param implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer paramId;
	private String title;// 标题
	private String value;// 值
	private String status;// 状态
	private String sort;// 排序
	private String type;
	private String code;//代码
	private String readonly;//代码表是否可编辑
	private BigDecimal minRecharge;//充值范围
	private BigDecimal maxRecharge;//充值范围
	private BigDecimal maxPresent;//最大赠送金额
	private String beizhu;
	public Param() {
	}

	public BigDecimal getMinRecharge() {
		return minRecharge;
	}

	public void setMinRecharge(BigDecimal minRecharge) {
		this.minRecharge = minRecharge;
	}

	public BigDecimal getMaxRecharge() {
		return maxRecharge;
	}

	public void setMaxRecharge(BigDecimal maxRecharge) {
		this.maxRecharge = maxRecharge;
	}

	public BigDecimal getMaxPresent() {
		return maxPresent;
	}

	public void setMaxPresent(BigDecimal maxPresent) {
		this.maxPresent = maxPresent;
	}

	public String getReadonly() {
		return readonly;
	}

	public void setReadonly(String readonly) {
		this.readonly = readonly;
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public Integer getParamId() {
		return paramId;
	}

	public void setParamId(Integer paramId) {
		this.paramId = paramId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getBeizhu() {
		return beizhu;
	}
	public void setBeizhu(String beizhu) {
		this.beizhu = beizhu;
	}

}
