package com.gf.three.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.gf.three.service.IThreeService;


/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitThreeSession extends QuartzJobBean{

	private static IThreeService gfThreeService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IThreeService getGfThreeService() {
		return gfThreeService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfThreeService(IThreeService gfThreeService) {
		this.gfThreeService = gfThreeService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_THREE);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		gfThreeService.updateInitSession(0); // 初始化今天场次
		gfThreeService.updateInitSession(1); // 初始化明天场次
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
