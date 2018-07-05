package com.gf.pick11.sdpick11.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.pick11.sdpick11.service.ISdPick11Service;

public class InitSdPick11Session extends QuartzJobBean{
	private static ISdPick11Service gfSdPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ISdPick11Service getGfSdPick11Service() {
		return gfSdPick11Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfSdPick11Service(ISdPick11Service gfSdPick11Service) {
		this.gfSdPick11Service = gfSdPick11Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_SDPICK11);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfSdPick11Service.updateInitSession(null);//今天
		gfSdPick11Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
