package com.xy.k10.gxk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.k10.gxk10.service.IGxK10Service;

public class InitGxK10Session extends QuartzJobBean{
	private static IGxK10Service gxK10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGxK10Service getGxK10Service() {
		return gxK10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGxK10Service(IGxK10Service gxK10Service) {
		this.gxK10Service = gxK10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_GXK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		gxK10Service.updateInitSession(0);//当天
		gxK10Service.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
