package com.gf.jsft.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.jsft.service.IJsFtService;

public class InitJsFtSession extends QuartzJobBean{
	private static IJsFtService gfJsFtService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJsFtService getGfJsFtService() {
		return gfJsFtService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfJsFtService(IJsFtService gfJsFtService) {
		this.gfJsFtService = gfJsFtService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_JSFT);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfJsFtService.updateInitTodaySession();
		gfJsFtService.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
