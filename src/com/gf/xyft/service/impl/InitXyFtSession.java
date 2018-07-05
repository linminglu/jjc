package com.gf.xyft.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.xyft.service.IXyFtService;

public class InitXyFtSession extends QuartzJobBean{
	private static IXyFtService gfXyFtService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IXyFtService getGfXyFtService() {
		return gfXyFtService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfXyFtService(IXyFtService gfXyFtService) {
		this.gfXyFtService = gfXyFtService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_XYFT);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfXyFtService.updateInitSession(0);
		gfXyFtService.updateInitSession(1);
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
