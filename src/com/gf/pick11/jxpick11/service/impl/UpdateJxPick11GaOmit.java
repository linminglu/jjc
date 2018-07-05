package com.gf.pick11.jxpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.pick11.jxpick11.service.IJxPick11Service;

public class UpdateJxPick11GaOmit  extends QuartzJobBean{

	private static IJxPick11Service gfJxPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfJxPick11Service(IJxPick11Service gfJxPick11Service){
		this.gfJxPick11Service = gfJxPick11Service;
	}
	
	public IJxPick11Service getGfJxPick11Service(){
		return this.gfJxPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfJxPick11Service.updateFetchAndOpenOmit();
//			gfJxPick11Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
