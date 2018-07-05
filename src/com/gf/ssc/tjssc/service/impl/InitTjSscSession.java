package com.gf.ssc.tjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.ssc.tjssc.service.ITjSscService;


/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitTjSscSession extends QuartzJobBean{

	private static ITjSscService gfTjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ITjSscService getGfTjSscService() {
		return gfTjSscService;
	}
	@SuppressWarnings("static-access")
	public void setGfTjSscService(ITjSscService gfTjSscService) {
		this.gfTjSscService = gfTjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_TJSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfTjSscService.updateInitTodaySession(); // 初始化今天场次
		gfTjSscService.updateInitSession(); // 初始化明天场次
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
