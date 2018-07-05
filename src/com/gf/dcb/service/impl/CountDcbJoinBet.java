package com.gf.dcb.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.dcb.service.IDcbService;
import com.framework.util.DateTimeUtil;

/**
 * 计算合买投注值并生成订单。
 * @author admin
 *
 */
public class CountDcbJoinBet extends QuartzJobBean{

	private static IDcbService gfDcbService;
	public static IDcbService getGfDcbService() {
		return gfDcbService;
	}

	public static void setGfDcbService(IDcbService gfDcbService) {
		CountDcbJoinBet.gfDcbService = gfDcbService;
	}

	protected final Log log = LogFactory.getLog(getClass());
	

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("-----双色球开始计算合买-------"+DateTimeUtil.DateToStringAll(new Date()));
			gfDcbService.updateCountJointBet(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		log.info("-----双色球开始计算结束-------"+DateTimeUtil.DateToStringAll(new Date()));
	}

}
