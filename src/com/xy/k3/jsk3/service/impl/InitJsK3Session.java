package com.xy.k3.jsk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.k3.jsk3.service.IJsK3Service;

/**
 * 每天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitJsK3Session extends QuartzJobBean {

	private static IJsK3Service jsK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJsK3Service getJsK3Service() {
		return jsK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setJsK3Service(IJsK3Service jsK3Service) {
		this.jsK3Service = jsK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JSK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		jsK3Service.updateInitSession();//今天的
		jsK3Service.updateInitTomorrowSession();//明天的
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}