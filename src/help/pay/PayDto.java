package help.pay;

import java.math.BigDecimal;

/**
 * bxspay支付参数封装类
 * 
 * @author Mr.zang
 * 
 */
public class PayDto {
	private String payGateway;// 支付网关类型
	private String payMode = "1";// 支付模式 1=固定金额支付 2=非固定金额支付
	private String orderNo;// 订单号
	private String orderPrice;// 订单金额
	private String orderStamp;// 时间戳
	private String callback;// 异步回调
	private String pageUrl;// 同步回调
	private String returnUrl;// 同步回调页面
	private String title;// 标题
	private String scene;// 使用场景1=pc 2=app 3=wap 4=wechat 5=alipay

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 支付网关类型
	 * 
	 * 
	 */
	public String getPayGateway() {
		return payGateway;
	}

	/**
	 * 支付网关类型
	 * 
	 * @param payGateway
	 */
	public void setPayGateway(String payGateway) {
		this.payGateway = payGateway;
	}

	public String getScene() {
		return scene;
	}

	public void setScene(String scene) {
		this.scene = scene;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getPageUrl() {
		return pageUrl;
	}

	public void setPageUrl(String pageUrl) {
		this.pageUrl = pageUrl;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}

	public String getOrderStamp() {
		return orderStamp;
	}

	public void setOrderStamp(String orderStamp) {
		this.orderStamp = orderStamp;
	}

}
