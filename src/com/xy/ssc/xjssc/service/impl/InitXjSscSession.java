package com.xy.ssc.xjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.ssc.xjssc.service.IXjSscService;


public class InitXjSscSession  extends QuartzJobBean {
	private static IXjSscService xjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IXjSscService getXjSscService() {
		return xjSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setXjSscService(IXjSscService xjSscService) {
		this.xjSscService = xjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_XJSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		xjSscService.updateInitTodaySession();
		xjSscService.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
