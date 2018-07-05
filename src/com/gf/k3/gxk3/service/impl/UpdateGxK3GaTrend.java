package com.gf.k3.gxk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.gxk3.service.IGxK3Service;

public class UpdateGxK3GaTrend extends QuartzJobBean{

	private static IGxK3Service gfGxK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfGxK3Service(IGxK3Service gfGxK3Service){
		this.gfGxK3Service = gfGxK3Service;
	}
	
	public IGxK3Service getGfGxK3Service(){
		return this.gfGxK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("gxk3.trend.gf")) return;
//		
//		log.info("_______[gxk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("gxk3", "_______[gxk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfGxK3Service.updateCountJointBet(null);
//		}
//		log.info("_______[gxk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("gxk3", "_______[gxk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
