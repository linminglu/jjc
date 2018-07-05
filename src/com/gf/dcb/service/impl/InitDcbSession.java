package com.gf.dcb.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.dcb.service.IDcbService;
import com.framework.util.DateTimeUtil;

/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitDcbSession extends QuartzJobBean{

	private static IDcbService gfDcbService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IDcbService getGfDcbService() {
		return gfDcbService;
	}
	
	public void setGfDcbService(IDcbService gfDcbService) {
		this.gfDcbService = gfDcbService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		if(!Constants.getTimerOpen("dcb.initSession.gf")) return;
		
		try {
			gfDcbService.updateInitSession(); // 初始化场次
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
