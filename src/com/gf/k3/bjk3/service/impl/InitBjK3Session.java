package com.gf.k3.bjk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.k3.bjk3.service.IBjK3Service;

public class InitBjK3Session extends QuartzJobBean{
	private static IBjK3Service gfBjK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IBjK3Service getGfBjK3Service() {
		return gfBjK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfBjK3Service(IBjK3Service gfBjK3Service) {
		this.gfBjK3Service = gfBjK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_BJK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		gfBjK3Service.updateInitSession("");//今天
		gfBjK3Service.updateInitTomorrowSession();//明天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
