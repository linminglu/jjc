package com.gf.fivecolor.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.fivecolor.service.IFiveService;

/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitFiveSession extends QuartzJobBean{

	private static IFiveService gfFiveService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IFiveService getGfFiveService() {
		return gfFiveService;
	}
	@SuppressWarnings("static-access")
	public void setGfFiveService(IFiveService gfFiveService) {
		this.gfFiveService = gfFiveService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_FC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfFiveService.updateInitSession(0); // 初始化今天场次
		gfFiveService.updateInitSession(1); // 初始化明天场次
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
