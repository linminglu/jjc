package com.game.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgroups.tests.perf.Data;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.game.service.IGaService;

public class ClearData extends QuartzJobBean {

	private static IGaService gaService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaService getGaService() {
		return gaService;
	}
	
	@SuppressWarnings("static-access")
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		if(!Constants.getTimerOpen("game.clearData")) return;
			
//		Date date = DateTimeUtil.getDateTimeOfDays(new Date(), -30);
//		gaService.updateMarkSixGaSession(date);
//		gaService.updateGdK10GaSession(date);
//		gaService.updateBjLu28GaSession(date);
//		gaService.updateBjPk10GaSession(date);
//		gaService.updateBj3GaSession(date);
//		gaService.updateCqSscGaSession(date);
//		gaService.updateGdK10GaSession(date);
//		gaService.updateGdPick11GaSession(date);
//		gaService.updateGxK10GaSession(date);
//		gaService.updateJsK3GaSession(date);
//		gaService.updatePokerGaSession(date);
//		gaService.updateTjSscGaSession(date);
//		gaService.updateXjSscGaSession(date);
//		gaService.updateXjpLu28GaSession(date);
		log.info("---------------------------> update---------1");

//		gaService.updateGaBetDetail(date);
		log.info("---------------------------> update-------------3");
		
	}

}
