package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.game.service.IGaService;

public class UpdateDailyStatistics   extends QuartzJobBean {

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
		boolean timer = Constants.getTimerOpen("game.updateDailyStatistics");
		GameHelpUtil.log("game.updateDailyStatistics=",timer,0);
		
		if(!timer) return;
		
		try {
			long sTing = System.currentTimeMillis();
			//if(Constants.getGameInitAndOpenSwitch().equals(Constants.PUB_STATUS_OPEN)){		
				gaService.updateDailyStatistics(null);
			//}
			GameHelpUtil.log("Daily Statistics END","...",sTing);
		} catch (Exception e) {
			e.printStackTrace();
			GameHelpUtil.log("Daily Statistics ERROR ..... ["+e.getMessage()+"]");
		}
	}
}
