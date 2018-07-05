package com.xy.pick11.sdpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pick11.sdpick11.service.ISdPick11Service;

public class InitSdPick11Session extends QuartzJobBean{
	private static ISdPick11Service sdPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISdPick11Service getSdPick11Service() {
		return sdPick11Service;
	}
	 
	@SuppressWarnings("static-access")
	public void setSdPick11Service(ISdPick11Service sdPick11Service) {
		this.sdPick11Service = sdPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_SDPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		sdPick11Service.updateInitSession();
		sdPick11Service.updateTomorrowSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
