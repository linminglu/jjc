package com.xy.bj3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.bj3.service.IBj3Service;

/**
 * 每天x点开始初始化
 * 应该设定为0点初始化，因为游戏0点就开始了
 */
public class InitBj3Session extends QuartzJobBean{

	private static IBj3Service bj3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBj3Service getBj3Service() {
		return bj3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setBj3Service(IBj3Service bj3Service) {
		this.bj3Service = bj3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJ3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		bj3Service.updateInitSession(0);//当天
		bj3Service.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
