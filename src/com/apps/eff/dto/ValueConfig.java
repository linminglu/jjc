package com.apps.eff.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 配置项
 * 目前用于 充值和打码返水 多档范围比例
 */
public class ValueConfig implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private boolean open;//打开/关闭
	private String execTime;//执行时间
	private Integer max;//最大
	private BigDecimal maxPer;//最大比例
	private Integer min;//最小
	private BigDecimal minPer;//最小比例
	private List<ValueItem> items;//数值项
	
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public String getExecTime() {
		return execTime;
	}
	public void setExecTime(String execTime) {
		this.execTime = execTime;
	}
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}
	public List<ValueItem> getItems() {
		return items;
	}
	public void setItems(List<ValueItem> items) {
		this.items = items;
	}
	public BigDecimal getMaxPer() {
		return maxPer;
	}
	public void setMaxPer(BigDecimal maxPer) {
		this.maxPer = maxPer;
	}
	public BigDecimal getMinPer() {
		return minPer;
	}
	public void setMinPer(BigDecimal minPer) {
		this.minPer = minPer;
	}
	
}
