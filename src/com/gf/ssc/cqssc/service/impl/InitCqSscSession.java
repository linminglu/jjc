package com.gf.ssc.cqssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.ssc.cqssc.service.ICqSscService;


/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitCqSscSession extends QuartzJobBean{

	private static ICqSscService gfCqSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ICqSscService getGfCqSscService() {
		return gfCqSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfCqSscService(ICqSscService gfCqSscService) {
		this.gfCqSscService = gfCqSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_CQSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfCqSscService.updateInitTodaySession(); // 初始化今天场次
		gfCqSscService.updateInitSession(); // 初始化明天场次
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
