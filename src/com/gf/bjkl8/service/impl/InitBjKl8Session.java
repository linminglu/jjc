package com.gf.bjkl8.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.gf.bjkl8.service.IBjKl8Service;

/**
 * 每天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitBjKl8Session extends QuartzJobBean {

	private static IBjKl8Service gfBjKl8Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjKl8Service getGfBjKl8Service() {
		return gfBjKl8Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfBjKl8Service(IBjKl8Service gfBjKl8Service) {
		this.gfBjKl8Service = gfBjKl8Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		if(!Constants.getTimerOpen("bjkl8.initSession.gf")) return;
		
		try {
			log.info("_______[bjKl8 init session]["+DateTimeUtil.getDateTime()+"]___________________");
			String sessionNo="";
			gfBjKl8Service.updateInitTodaySession(sessionNo);
			gfBjKl8Service.updateInitSession();
			
			log.info("_______[bjKl8 init session end]["+DateTimeUtil.getDateTime()+"]___________________");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}