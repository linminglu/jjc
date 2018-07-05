package com.xy.hk.sflhc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.hk.sflhc.service.ISflhcService;

/**
 * 每隔两天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitSflhcSession extends QuartzJobBean {

	private static ISflhcService sflhcService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISflhcService getSflhcService() {
		return sflhcService;
	}
	
	@SuppressWarnings("static-access")
	public void setSflhcService(ISflhcService sflhcService) {
		this.sflhcService = sflhcService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_SFLHC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		sflhcService.updateInitSession(0);//当天
		sflhcService.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}