package com.gf.k3.jxk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.k3.jxk3.service.IJxK3Service;

public class InitJxK3Session extends QuartzJobBean{
	private static IJxK3Service gfJxK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJxK3Service getGfJxK3Service() {
		return gfJxK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfJxK3Service(IJxK3Service gfJxK3Service) {
		this.gfJxK3Service = gfJxK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_JXK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfJxK3Service.updateInitSession(null);//今天
		gfJxK3Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
