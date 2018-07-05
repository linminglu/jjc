package com.xy.ssc.cqssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.ssc.cqssc.service.ICqSscService;


public class InitCqSscSession  extends QuartzJobBean {
	private static ICqSscService cqSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ICqSscService getCqSscService() {
		return cqSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setCqSscService(ICqSscService cqSscService) {
		this.cqSscService = cqSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_CQSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		cqSscService.updateInitTodaySession();
		cqSscService.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
