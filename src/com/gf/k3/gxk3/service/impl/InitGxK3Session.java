package com.gf.k3.gxk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.k3.gxk3.service.IGxK3Service;

public class InitGxK3Session extends QuartzJobBean{
	private static IGxK3Service gfGxK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGxK3Service getGfGxK3Service() {
		return gfGxK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfGxK3Service(IGxK3Service gfGxK3Service) {
		this.gfGxK3Service = gfGxK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_GXK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfGxK3Service.updateInitSession(null);//今天
		gfGxK3Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
