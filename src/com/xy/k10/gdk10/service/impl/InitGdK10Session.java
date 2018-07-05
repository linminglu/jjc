package com.xy.k10.gdk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.k10.gdk10.service.IGdK10Service;

public class InitGdK10Session extends QuartzJobBean{
	private static IGdK10Service gdK10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGdK10Service getGdK10Service() {
		return gdK10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGdK10Service(IGdK10Service gdK10Service) {
		this.gdK10Service = gdK10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_GDK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		gdK10Service.updateInitSession();
		gdK10Service.updateTomorrowSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
