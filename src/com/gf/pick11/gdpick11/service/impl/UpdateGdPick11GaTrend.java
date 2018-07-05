package com.gf.pick11.gdpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.pick11.gdpick11.service.IGdPick11Service;

public class UpdateGdPick11GaTrend extends QuartzJobBean{

	private static IGdPick11Service gfGdPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfGdPick11Service(IGdPick11Service gfGdPick11Service){
		this.gfGdPick11Service = gfGdPick11Service;
	}
	
	public IGdPick11Service getGfGdPick11Service(){
		return this.gfGdPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("gdpick11.trend.gf")) return;
//
//		log.info("_______[gdk10 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("gdpick11", "_______[gdk10 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfGdPick11Service.updateCountJointBet(null);
//		}
//		log.info("_______[gdk10 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("gdpick11", "_______[gdk10 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
