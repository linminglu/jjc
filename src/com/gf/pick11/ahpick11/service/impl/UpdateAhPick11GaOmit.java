package com.gf.pick11.ahpick11.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.gf.pick11.ahpick11.service.IAhPick11Service;

public class UpdateAhPick11GaOmit  extends QuartzJobBean{

	private static IAhPick11Service gfAhPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfAhPick11Service(IAhPick11Service gfAhPick11Service){
		this.gfAhPick11Service = gfAhPick11Service;
	}
	
	public IAhPick11Service getGfAhPick11Service(){
		return this.gfAhPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfAhPick11Service.updateFetchAndOpenOmit();
//			gfAhPick11Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
