package com.card;

/**
 * 平台常量类
 * 
 * @author cuisy
 * 
 */
public final class CardConstants {

	/** 订单状态-待付款 **/
	public final static String ORDER_PAY_NOT = "1";
	/** 订单状态-待发货 **/
	public final static String ORDER_PAY_SUCCESS = "2";
	/** 订单状态-待收货 **/
	public final static String ORDER_USDE_SUCCESS = "3";
	/** 订单状态-待评价 **/
	public final static String ORDER_EVAL_SUCCESS = "4";
	/** 订单状态-已完成 **/
	public final static String ORDER_SUCCESS = "5";
	/** 订单状态-关闭 **/
	public final static String ORDER_CLOSE = "6";
	/** 订单状态-退款 **/
	public final static String ORDER_REFUND = "7";
	
	/**礼品卡状态  0=已注销 **/
	public final static String CARD_STATUS_CANCEL = "0";
	/**礼品卡状态 1=未激活 **/
	public final static String CARD_STATUS_NORMAL = "1";
	/**礼品卡状态 2=已激活**/
	public final static String CARD_STATUS_ACTIVED = "2";
}
