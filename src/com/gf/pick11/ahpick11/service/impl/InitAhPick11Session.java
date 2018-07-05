package com.gf.pick11.ahpick11.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.pick11.ahpick11.service.IAhPick11Service;

public class InitAhPick11Session extends QuartzJobBean{
	private static IAhPick11Service gfAhPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IAhPick11Service getGfAhPick11Service() {
		return gfAhPick11Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfAhPick11Service(IAhPick11Service gfAhPick11Service) {
		this.gfAhPick11Service = gfAhPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_AHPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfAhPick11Service.updateInitSession(null);//今天
		gfAhPick11Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
