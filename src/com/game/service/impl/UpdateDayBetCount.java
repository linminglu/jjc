package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.game.service.IGaService;

public class UpdateDayBetCount   extends QuartzJobBean {

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
		boolean timer = Constants.getTimerOpen("game.updateDayBetCount");
		GameHelpUtil.log("game.updateDayBetCount=",timer,0);
		
		if(!timer) return;
		
		try {
			long sTing = System.currentTimeMillis();
			gaService.updateDayBetCount(null);//每日投注派彩盈亏统计
			GameHelpUtil.log("day bet END","...",sTing);
			
			sTing = System.currentTimeMillis();
			gaService.updateUserBetCount(null);//每日会员投注统计
			GameHelpUtil.log("user bet END","...",sTing);
		} catch (Exception e) {
			GameHelpUtil.log("day bet ERROR ..... ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
}
