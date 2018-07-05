package com.xy.pk10.jsft.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.xy.pk10.jsft.service.IJsftService;


public class InitJsftSession extends QuartzJobBean {
	private static IJsftService jsftService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJsftService getJsftService() {
		return jsftService;
	}
	
	@SuppressWarnings("static-access")
	public void setJsftService(IJsftService jsftService) {
		this.jsftService = jsftService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JSFT);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		jsftService.updateInitSession(0);//当天
		jsftService.updateInitSession(1);//明天
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
