package com.gf.fivecolor.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.fivecolor.service.IFiveService;

/**
 * 计算合买投注值并生成订单。
 *
 */
public class CountFiveJoinBet extends QuartzJobBean{

	private static IFiveService gfFiveService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IFiveService getGfFiveService() {
		return gfFiveService;
	}
	@SuppressWarnings("static-access")
	public void setGfFiveService(IFiveService gfFiveService) {
		this.gfFiveService = gfFiveService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//			log.info("---------五分彩 -计算合买投注--- 开始--"+DateTimeUtil.DateToStringAll(new Date()));
//			gfFiveService.updateCountJointBet(null); // 计算合买投注
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
