package com.apps.model;

public class PayConfig implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer payId; //id
	private String par; //商户号
	private String code; //终端id，商户标识
	private String payKey; //密钥
	private String type; //支付方式
	/**
	 * @return the payId
	 */
	public Integer getPayId() {
		return payId;
	}
	/**
	 * @param payId the payId to set
	 */
	public void setPayId(Integer payId) {
		this.payId = payId;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the par
	 */
	public String getPar() {
		return par;
	}
	/**
	 * @param par the par to set
	 */
	public void setPar(String par) {
		this.par = par;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return the payKey
	 */
	public String getPayKey() {
		return payKey;
	}
	/**
	 * @param payKey the payKey to set
	 */
	public void setPayKey(String payKey) {
		this.payKey = payKey;
	}
}
