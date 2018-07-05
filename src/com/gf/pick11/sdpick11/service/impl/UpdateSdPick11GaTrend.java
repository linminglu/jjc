package com.gf.pick11.sdpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.pick11.sdpick11.service.ISdPick11Service;

public class UpdateSdPick11GaTrend extends QuartzJobBean{

	private static ISdPick11Service gfSdPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfSdPick11Service(ISdPick11Service gfSdPick11Service){
		this.gfSdPick11Service = gfSdPick11Service;
	}
	
	public ISdPick11Service getGfSdPick11Service(){
		return this.gfSdPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("sdpick11.trend.gf")) return;
//
//		log.info("_______[gdk10 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("sdpick11", "_______[gdk10 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfSdPick11Service.updateCountJointBet(null);
//		}
//		log.info("_______[gdk10 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("sdpick11", "_______[gdk10 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
