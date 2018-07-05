package com.xy.lucky28.xjplu28.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.lucky28.xjplu28.service.IXjpLu28Service;

/**
 * 每天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitXjpLu28Session extends QuartzJobBean {

	private static IXjpLu28Service xjpLu28Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IXjpLu28Service getXjpLu28Service() {
		return xjpLu28Service;
	}

	@SuppressWarnings("static-access")
	public void setXjpLu28Service(IXjpLu28Service xjpLu28Service) {
		this.xjpLu28Service = xjpLu28Service;
	}


	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_XJPLU28);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		xjpLu28Service.updateInitSession("");
		xjpLu28Service.updateInitTomorrowSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}