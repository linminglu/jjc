package com.gf.k3.hubk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.gf.k3.hubk3.service.IHubK3Service;

public class UpdateHubK3GaOmit  extends QuartzJobBean{

	private static IHubK3Service gfHubK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfHubK3Service(IHubK3Service gfHubK3Service){
		this.gfHubK3Service = gfHubK3Service;
	}
	
	public IHubK3Service getGfHubK3Service(){
		return this.gfHubK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("hubk3.omit.gf")) return;
		
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfHubK3Service.updateFetchAndOpenOmit();
//			gfHubK3Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
