package com.gf.pick11.shpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.pick11.shpick11.service.IShPick11Service;

public class UpdateShPick11GaTrend extends QuartzJobBean{

	private static IShPick11Service gfShPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfShPick11Service(IShPick11Service gfShPick11Service){
		this.gfShPick11Service = gfShPick11Service;
	}
	
	public IShPick11Service getGfShPick11Service(){
		return this.gfShPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("shpick11.trend.gf")) return;
//
//		log.info("_______[gdk10 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("shpick11", "_______[gdk10 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfShPick11Service.updateCountJointBet(null);
//		}
//		log.info("_______[gdk10 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("shpick11", "_______[gdk10 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
