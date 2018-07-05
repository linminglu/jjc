package com.gf.three.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.three.service.IThreeService;

/**
 * 计算合买投注值并生成订单。
 * @author admin
 *
 */
public class CountThreeJoinBet extends QuartzJobBean{

	private static IThreeService gfThreeService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IThreeService getGfThreeService() {
		return gfThreeService;
	}
	@SuppressWarnings("static-access")
	public void setGfThreeService(IThreeService gfThreeService) {
		this.gfThreeService = gfThreeService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//			log.info("------三分彩---计算合买投注-"+DateTimeUtil.DateToStringAll(new Date()));
//			gfThreeService.updateCountJointBet(null); // 计算合买投注
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
