package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.game.service.IGaK3Service;
import com.game.service.IGaSsqService;

/**
 * 初始化场次
 */
public class InitSsqSession extends QuartzJobBean {

	private static IGaSsqService gaSsqService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaSsqService getGaSsqService() {
		return gaSsqService;
	}
	
	@SuppressWarnings("static-access")
	public void setGaSsqService(IGaSsqService gaSsqService) {
		this.gaSsqService = gaSsqService;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("_______[ssq init session]["+DateTimeUtil.getDateTime()+"]___________________");
			
//			gaSsqService.updateInitSession();
			
			log.info("_______[ssq init session end]["+DateTimeUtil.getDateTime()+"]___________________");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}