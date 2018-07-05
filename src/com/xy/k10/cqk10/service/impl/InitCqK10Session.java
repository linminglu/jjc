package com.xy.k10.cqk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.k10.cqk10.service.ICqK10Service;

public class InitCqK10Session extends QuartzJobBean{
	private static ICqK10Service cqK10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ICqK10Service getCqK10Service() {
		return cqK10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setCqK10Service(ICqK10Service cqK10Service) {
		this.cqK10Service = cqK10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_CQK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		cqK10Service.updateInitSession(0);//当天
		cqK10Service.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
