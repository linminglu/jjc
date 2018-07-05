package com.xy.ssc.five.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.ssc.five.service.IFiveService;


public class InitFiveSession  extends QuartzJobBean {
	private static IFiveService fiveService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IFiveService getFiveService() {
		return fiveService;
	}
	
	@SuppressWarnings("static-access")
	public void setFiveService(IFiveService fiveService) {
		this.fiveService = fiveService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_FC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		fiveService.updateInitSession(0);
		fiveService.updateInitSession(1);
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
