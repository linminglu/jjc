package com.xy.poker.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.CacheUtil;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.game.model.GaSessionInfo;
import com.xy.poker.service.IPokerService;

/**
 * 每天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitPokerSession extends QuartzJobBean {

	private static IPokerService pokerService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IPokerService getPokerService() {
		return pokerService;
	}
	
	@SuppressWarnings("static-access")
	public void setPokerService(IPokerService pokerService) {
		this.pokerService = pokerService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_POKER);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		pokerService.updateInitTodaySession();
		pokerService.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}