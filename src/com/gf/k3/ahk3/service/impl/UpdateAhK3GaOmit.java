package com.gf.k3.ahk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.ahk3.service.IAhK3Service;

public class UpdateAhK3GaOmit  extends QuartzJobBean{

	private static IAhK3Service gfAhK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	@SuppressWarnings("static-access")
	public void setGfAhK3Service(IAhK3Service gfAhK3Service){
		this.gfAhK3Service = gfAhK3Service;
	}
	
	public IAhK3Service getGfAhK3Service(){
		return this.gfAhK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("ahk3.omit.gf")) return;
		
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfAhK3Service.updateFetchAndOpenOmit();
//			gfAhK3Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
