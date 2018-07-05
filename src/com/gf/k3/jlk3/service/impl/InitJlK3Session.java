package com.gf.k3.jlk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.gf.k3.jlk3.service.IJlK3Service;

public class InitJlK3Session extends QuartzJobBean{
	private static IJlK3Service gfJlK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJlK3Service getGfJlK3Service() {
		return gfJlK3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setGfJlK3Service(IJlK3Service gfJlK3Service) {
		this.gfJlK3Service = gfJlK3Service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_JLK3);
		if(!Constants.getTimerOpen(gameCode.substring(2)+".initSession.gf")) return;
		long t1 = System.currentTimeMillis();
		
		Date now = DateTimeUtil.getJavaUtilDateNow();//当前时间
		Date next = DateTimeUtil.getDateTimeOfDays(now, 1);//下一天
		String initNextDay = DateTimeUtil.DateToString(next);
		
		gfJlK3Service.updateInitSession(null);//今天
		gfJlK3Service.updateInitSession(initNextDay);//下一天
		
		GameHelpUtil.log(gameCode,"init session["+(System.currentTimeMillis()-t1)+"ms]");
	}
}
