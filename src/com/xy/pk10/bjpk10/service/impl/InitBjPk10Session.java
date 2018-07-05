package com.xy.pk10.bjpk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.pk10.bjpk10.service.IBjPk10Service;

public class InitBjPk10Session extends QuartzJobBean {
	private static IBjPk10Service bjPk10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjPk10Service getBjPk10Service() {
		return bjPk10Service;
	}
	
	@SuppressWarnings("static-access")
	public void setBjPk10Service(IBjPk10Service bjPk10Service) {
		this.bjPk10Service = bjPk10Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJPK10);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		bjPk10Service.updateInitTodaySession("");//当天
		bjPk10Service.updateInitSession();//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
