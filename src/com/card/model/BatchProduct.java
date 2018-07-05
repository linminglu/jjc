package com.card.model;

import java.io.Serializable;

/**
 * 批次产品关系
 * @author Administrator
 *
 */
public class BatchProduct implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer batchProductId;
	private String protocolCode;
	private String batchCode;
	private Integer productId;
	
	public Integer getBatchProductId() {
		return batchProductId;
	}
	public void setBatchProductId(Integer batchProductId) {
		this.batchProductId = batchProductId;
	}
	public String getProtocolCode() {
		return protocolCode;
	}
	public void setProtocolCode(String protocolCode) {
		this.protocolCode = protocolCode;
	}
	public String getBatchCode() {
		return batchCode;
	}
	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
}