package com.gf.pick11.jxpick11.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.pick11.jxpick11.service.IJxPick11Service;

public class InitJxPick11Session extends QuartzJobBean{
	private static IJxPick11Service gfJxPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJxPick11Service getGfJxPick11Service() {
		return gfJxPick11Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfJxPick11Service(IJxPick11Service gfJxPick11Service) {
		this.gfJxPick11Service = gfJxPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_JXPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfJxPick11Service.updateInitSession(null);//今天
		gfJxPick11Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
