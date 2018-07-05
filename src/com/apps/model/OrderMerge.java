package com.apps.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 合并订单类 <br/>
 * 有的订单是属于合并订单提交给第三方支付平台的
 * 
 * @author Mr.zang
 * 
 */
public class OrderMerge implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;// 合并订单id
	private Integer userId;// 用户id
	private String title;// 订单说明
	private Date createDate;// 创建日期
	private String tradeNo;// 交易号

	private BigDecimal totalPrice;// 总价
	private BigDecimal freightPrices;// 运费，这个运费是值所有订单相加的
	private Integer num;// 购买数量
	private String payType;// 支付方式 1.支付宝 2.银联 3.货到付款
	private String status;// 状态 1.待付款 2.待发货 3.待收货 4.待评价  5.已完成 6.关闭 7.退款

	public OrderMerge() {
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public BigDecimal getFreightPrices() {
		return freightPrices;
	}

	public void setFreightPrices(BigDecimal freightPrices) {
		this.freightPrices = freightPrices;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public BigDecimal getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

}
