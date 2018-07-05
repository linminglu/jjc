package com.xy.lucky28.bjlu28.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.xy.lucky28.bjlu28.service.IBjLu28Service;

/**
 * 每天x点开始初始化
 * 目前设定的是每天2点
 */
public class InitBjLu28Session extends QuartzJobBean {

	private static IBjLu28Service bjLu28Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjLu28Service getBjLu28Service() {
		return bjLu28Service;
	}
	
	@SuppressWarnings("static-access")
	public void setBjLu28Service(IBjLu28Service bjLu28Service) {
		this.bjLu28Service = bjLu28Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJLU28);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.xy")) return;
		long t1 = System.currentTimeMillis();
		bjLu28Service.updateInitTodaySession("");
		bjLu28Service.updateInitSession();
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}