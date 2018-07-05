package com.xy.pick11.gdpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pick11.gdpick11.service.IGdPick11Service;

public class InitGdPick11Session extends QuartzJobBean{
	private static IGdPick11Service gdPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGdPick11Service getGdPick11Service() {
		return gdPick11Service;
	}
	 
	@SuppressWarnings("static-access")
	public void setGdPick11Service(IGdPick11Service gdPick11Service) {
		this.gdPick11Service = gdPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_GDPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		gdPick11Service.updateInitSession();
		gdPick11Service.updateTomorrowSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
