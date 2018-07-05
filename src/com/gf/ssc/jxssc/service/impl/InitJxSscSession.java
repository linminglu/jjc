package com.gf.ssc.jxssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.ssc.jxssc.service.IJxSscService;


/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitJxSscSession extends QuartzJobBean{

	private static IJxSscService jxsscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJxSscService getJxSscService() {
		return jxsscService;
	}
	
	public void setJxSscService(IJxSscService jxsscService) {
		this.jxsscService = jxsscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		if(!Constants.getTimerOpen("jxssc.initSession.gf")) return;
		
		try {
			log.info("------开始初始化jxssc");
			jxsscService.updateInitSession(0); // 初始化今天场次
			jxsscService.updateInitSession(1); // 初始化明天场次
			log.info("------结束初始化jxssc");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
