package com.xy.k3.bjk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.k3.bjk3.service.IBjK3Service;

/**
 * 每天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitBjK3Session extends QuartzJobBean {

	private static IBjK3Service bjK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjK3Service getBjK3Service() {
		return bjK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setBjK3Service(IBjK3Service bjK3Service) {
		this.bjK3Service = bjK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		bjK3Service.updateInitSession("");//今天的
		bjK3Service.updateInitTomorrowSession();//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
