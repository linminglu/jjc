package com.xy.ssc.bjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.ssc.bjssc.service.IBjSscService;


public class InitBjSscSession  extends QuartzJobBean {
	private static IBjSscService bjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjSscService getBjSscService() {
		return bjSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setBjSscService(IBjSscService bjSscService) {
		this.bjSscService = bjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		bjSscService.updateInitSession(0);
		bjSscService.updateInitSession(1);
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
