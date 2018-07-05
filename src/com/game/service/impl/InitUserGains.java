package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.game.service.IGaService;

public class InitUserGains  extends QuartzJobBean {

	private static IGaService gaService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaService getGaService() {
		return gaService;
	}
	
	@SuppressWarnings("static-access")
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		boolean timer = Constants.getTimerOpen("game.initUserGains");
		GameHelpUtil.log("game.initUserGains=",timer,0);
		
		if(!timer) return;
		
		try {
			long sTing = System.currentTimeMillis();
			gaService.updateUserGains();
			//--gaService.updateUserDayWinAndBet();//更新用户每日投注金额，每日中奖金额
			GameHelpUtil.log("init user gains","...",sTing);
		} catch (Exception e) {
			GameHelpUtil.log("init user gains ERROR ..... ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
}