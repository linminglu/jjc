package com.gf.sfpk102.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.sfpk102.service.ISfPk102Service;

public class InitSfPk102Session extends QuartzJobBean{
	private static ISfPk102Service gfSfPk102Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISfPk102Service getGfSfPk102Service() {
		return gfSfPk102Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfSfPk102Service(ISfPk102Service gfSfPk102Service) {
		this.gfSfPk102Service = gfSfPk102Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_SFPK102);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfSfPk102Service.updateInitTodaySession();
		gfSfPk102Service.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
