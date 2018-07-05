package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.game.service.IGaSsqService;

/**
 * 定时抓去官网开奖数据
 */
public class FetchSsqResult extends QuartzJobBean {

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
//			log.info("_______[ssq fetch session]["+DateTimeUtil.getDateTime()+"]___________________");
			
//			gaSsqService.updateFetchAndOpenResult();
			
//			log.info("_______[ssq fetch session end]["+DateTimeUtil.getDateTime()+"]___________________");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}