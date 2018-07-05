package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.game.service.IGaHhmfService;

/**
 * 初始化场次/每天 24小时
 */
public class InitHhmfSession extends QuartzJobBean {

	private static IGaHhmfService gaHhmfService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaHhmfService getGaHhmfService() {
		return gaHhmfService;
	}

	@SuppressWarnings("static-access")
	public void setGaHhmfService(IGaHhmfService gaHhmfService) {
		this.gaHhmfService = gaHhmfService;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			log.info("_______[hhmf init session]["+DateTimeUtil.getDateTime()+"]___________________");
			
			gaHhmfService.updateInitSession();
			
			log.info("_______[hhmf init session end]["+DateTimeUtil.getDateTime()+"]___________________");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}