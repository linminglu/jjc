package com.xy.pk10.xyft.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pk10.xyft.service.IXyftService;


public class InitXyftSession extends QuartzJobBean {
	private static IXyftService xyftService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IXyftService getXyftService() {
		return xyftService;
	}
	
	@SuppressWarnings("static-access")
	public void setXyftService(IXyftService xyftService) {
		this.xyftService = xyftService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_XYFT);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		xyftService.updateInitSession(0);//当天
		xyftService.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
