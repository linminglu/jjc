package com.apps.web.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.apps.service.ISellerService;
import com.cash.service.ICheckoutService;
import com.framework.util.DateTimeUtil;
import com.framework.web.action.BaseDispatchAction;

/**
 * 定时器管理
 * 
 * @author zang
 * 
 */

public class TimerManager extends BaseDispatchAction {
//	private final ISellerService sellerService = (ISellerService) getService("sellerService");
//	private final ICheckoutService checkoutService = (ICheckoutService) getService("checkoutService");
	public TimerManager() {
	}

	/**
	 * 定时器，在每天固定时间内执行，24小时制
	 * 
	 * @param hh
	 *            时
	 * @param mm
	 *            分
	 * @param ss
	 *            秒
	 */
	public TimerManager(int hh, int mm, int ss) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hh); // 控制时
		calendar.set(Calendar.MINUTE, mm); // 控制分
		calendar.set(Calendar.SECOND, ss); // 控制秒

		Date time = calendar.getTime(); // 得出执行任务的时间,此处为今天的00：00：00

		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				//1.把已经失效的促销信息关闭
				String nowDate = DateTimeUtil.getDateTime();
//				sellerService.updateSellerMinusStatus(nowDate);
				log.info(">>>>>更新商家促销信息状态["+nowDate+"]");
				
				//结算当前时间之前的所有已完成&&未结算的订单
				//Date checkoutDate = DateTimeUtil.parse(DateTimeUtil.DoFormatDate(DateTimeUtil.getJavaUtilDateNow(), false)+" 23:59:59");
				Date checkoutDate = DateTimeUtil.getJavaUtilDateNow();
//				checkoutService.updateUserOrderCheckoutByTimerAuto(checkoutDate);
				log.info(">>>>>更新商家与个人用户结算状态["+checkoutDate+"]");
			}
		}, time, 1000 * 60 * 60* 24);// 这里设定将延时每天固定执行 
	}
}
