package com.xy.pick11.jxpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pick11.jxpick11.service.IJxPick11Service;

public class InitJxPick11Session extends QuartzJobBean{
	private static IJxPick11Service jxPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJxPick11Service getJxPick11Service() {
		return jxPick11Service;
	}
	 
	@SuppressWarnings("static-access")
	public void setJxPick11Service(IJxPick11Service jxPick11Service) {
		this.jxPick11Service = jxPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JXPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		jxPick11Service.updateInitSession();
		jxPick11Service.updateTomorrowSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
