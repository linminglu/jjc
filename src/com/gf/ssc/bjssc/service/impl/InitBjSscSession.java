package com.gf.ssc.bjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.ssc.bjssc.service.IBjSscService;


/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitBjSscSession extends QuartzJobBean{

	private static IBjSscService gfBjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjSscService getGfBjSscService() {
		return gfBjSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfBjSscService(IBjSscService gfBjSscService) {
		this.gfBjSscService = gfBjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_BJSSC);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfBjSscService.updateInitSession(0); // 初始化今天场次
		gfBjSscService.updateInitSession(1); // 初始化明天场次
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
