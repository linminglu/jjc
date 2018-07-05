package com.cash.service;

import java.util.Date;

import com.framework.service.IService;

/**
 * 结算服务类
 * @author cuisy
 */
public interface ICheckoutService extends IService {
	
	/**
	 * 定时结算用户订单
	 * @param checkoutDate 结算截止日期 yyyy-MM-dd 23:59:59
	 * @return 1=ok,0=fail
	 */
	public String updateUserOrderCheckoutByTimerAuto(Date checkoutDate);

}
