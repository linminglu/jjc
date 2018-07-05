package com.apps.eff.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 值项
 */
public class ValueItem implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer max;//最大
	private Integer min;//最小
	private BigDecimal percent;//比例
	
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
	public BigDecimal getPercent() {
		return percent;
	}
	public void setPercent(BigDecimal percent) {
		this.percent = percent;
	}
	
}
