package com.gf.pick11.shpick11.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.pick11.shpick11.service.IShPick11Service;

public class InitShPick11Session extends QuartzJobBean{
	private static IShPick11Service gfShPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IShPick11Service getGfShPick11Service() {
		return gfShPick11Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfShPick11Service(IShPick11Service gfShPick11Service) {
		this.gfShPick11Service = gfShPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_SHPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfShPick11Service.updateInitSession(null);//今天
		gfShPick11Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
