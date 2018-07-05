package com.gf.k3.hubk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.k3.hubk3.service.IHubK3Service;

public class InitHubK3Session extends QuartzJobBean{
	private static IHubK3Service gfHubK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IHubK3Service getGfHubK3Service() {
		return gfHubK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfHubK3Service(IHubK3Service gfHubK3Service) {
		this.gfHubK3Service = gfHubK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_HUBK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfHubK3Service.updateInitSession(null);//今天
		gfHubK3Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
