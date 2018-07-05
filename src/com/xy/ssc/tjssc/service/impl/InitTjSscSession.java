package com.xy.ssc.tjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.ssc.tjssc.service.ITjSscService;


public class InitTjSscSession  extends QuartzJobBean {
	private static ITjSscService tjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ITjSscService getTjSscService() {
		return tjSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setTjSscService(ITjSscService tjSscService) {
		this.tjSscService = tjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_TJSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		tjSscService.updateInitTodaySession();
		tjSscService.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
