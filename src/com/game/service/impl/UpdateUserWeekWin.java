package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.game.service.IGaService;

public class UpdateUserWeekWin   extends QuartzJobBean {

	private static IGaService gaService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaService getGaService() {
		return gaService;
	}
	
	@SuppressWarnings("static-access")
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		try {
//			log.info("_______updateUserWeekWin["+DateTimeUtil.getDateTime()+"]___________________");
//			
//			gaService.updateUserWeekWin();
//						
//			log.info("_______updateUserWeekWin["+DateTimeUtil.getDateTime()+"]___________________");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
