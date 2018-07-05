package com.game.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.apps.eff.GameHelpUtil;
import com.framework.util.DateTimeUtil;
import com.game.service.IGaService;

public class UpdateMemberBack  extends QuartzJobBean {

	private static IGaService gaService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaService getGaService() {
		return gaService;
	}
	
	@SuppressWarnings("static-access")
	public void setGaService(IGaService gaService) {
		this.gaService = gaService;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		boolean timer = Constants.getTimerOpen("game.updateMemberBack");
		if(!timer){
			GameHelpUtil.log("game.updateMemberBack.timer="+timer);
			return;
		}
		
		try {
			Date now = DateTimeUtil.getJavaUtilDateNow();
			long startTiming = System.currentTimeMillis();
			
			String flag = gaService.updateMemberBack(now);
			if(flag.startsWith("success")){
				GameHelpUtil.log("update member back ..... ["+(System.currentTimeMillis()-startTiming)+"ms]");
			}
		} catch (Exception e) {
			GameHelpUtil.log("update member back ERROR ..... ["+e.getMessage()+"]");
			e.printStackTrace();
		}
	}
}
