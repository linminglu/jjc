package com.gf.sfpk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.sfpk10.service.ISfPk10Service;

public class InitSfPk10Session extends QuartzJobBean{
	private static ISfPk10Service gfSfPk10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISfPk10Service getGfSfPk10Service() {
		return gfSfPk10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfSfPk10Service(ISfPk10Service gfSfPk10Service) {
		this.gfSfPk10Service = gfSfPk10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_SFPK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfSfPk10Service.updateInitTodaySession();
		gfSfPk10Service.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
