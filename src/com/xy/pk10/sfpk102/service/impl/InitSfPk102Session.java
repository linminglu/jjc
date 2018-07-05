package com.xy.pk10.sfpk102.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pk10.sfpk102.service.ISfPk102Service;


public class InitSfPk102Session extends QuartzJobBean {
	private static ISfPk102Service sfPk102Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISfPk102Service getSfPk10Service() {
		return sfPk102Service;
	}
	
	@SuppressWarnings("static-access")
	public void setSfPk102Service(ISfPk102Service sfPk102Service) {
		this.sfPk102Service = sfPk102Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_SFPK102);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		sfPk102Service.updateInitSession(0);//当天
		sfPk102Service.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
