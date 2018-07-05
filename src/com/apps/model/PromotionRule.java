package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 促销规则
 * 
 * @author Mr.zang
 * 
 */
public class PromotionRule implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private Integer promotionId;// 促销id
	// 下面的促销规则是，满50减5
	private BigDecimal fullPrice;// 满足价格
	private BigDecimal minusPrice;// 减的价格

	public PromotionRule() {
	}

	
	public PromotionRule(Integer promotionId, BigDecimal fullPrice,
			BigDecimal minusPrice) {
		this.promotionId = promotionId;
		this.fullPrice = fullPrice;
		this.minusPrice = minusPrice;
	}


	public Integer getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Integer promotionId) {
		this.promotionId = promotionId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getFullPrice() {
		return fullPrice;
	}

	public void setFullPrice(BigDecimal fullPrice) {
		this.fullPrice = fullPrice;
	}

	public BigDecimal getMinusPrice() {
		return minusPrice;
	}

	public void setMinusPrice(BigDecimal minusPrice) {
		this.minusPrice = minusPrice;
	}

}
