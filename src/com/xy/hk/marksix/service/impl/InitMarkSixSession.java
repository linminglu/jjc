package com.xy.hk.marksix.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.hk.marksix.service.IMarkSixService;

/**
 * 每隔两天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitMarkSixSession extends QuartzJobBean {

	private static IMarkSixService markSixService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IMarkSixService getMarkSixService() {
		return markSixService;
	}
	
	@SuppressWarnings("static-access")
	public void setMarkSixService(IMarkSixService markSixService) {
		this.markSixService = markSixService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_MARKSIX);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		markSixService.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}