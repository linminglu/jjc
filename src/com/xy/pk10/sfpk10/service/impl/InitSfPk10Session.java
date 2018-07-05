package com.xy.pk10.sfpk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pk10.sfpk10.service.ISfPk10Service;


public class InitSfPk10Session extends QuartzJobBean {
	private static ISfPk10Service sfPk10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISfPk10Service getSfPk10Service() {
		return sfPk10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setSfPk10Service(ISfPk10Service sfPk10Service) {
		this.sfPk10Service = sfPk10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_SFPK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		sfPk10Service.updateInitTodaySession();//当天
		sfPk10Service.updateInitSession();//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
