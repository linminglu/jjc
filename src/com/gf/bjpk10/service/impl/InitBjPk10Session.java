package com.gf.bjpk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.bjpk10.service.IBjPk10Service;

public class InitBjPk10Session extends QuartzJobBean{
	private static IBjPk10Service gfBjPk10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjPk10Service getGfBjPk10Service() {
		return gfBjPk10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfBjPk10Service(IBjPk10Service gfBjPk10Service) {
		this.gfBjPk10Service = gfBjPk10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_BJPK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		String sessionNo="";
		gfBjPk10Service.updateInitTodaySession(sessionNo);
		gfBjPk10Service.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
