package com.gf.k3.ahk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.ahk3.service.IAhK3Service;

public class UpdateAhK3GaTrend extends QuartzJobBean{

	private static IAhK3Service gfAhK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setGfAhK3Service(IAhK3Service gfAhK3Service){
		this.gfAhK3Service = gfAhK3Service;
	}
	
	public IAhK3Service getGfAhK3Service(){
		return gfAhK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		if(!Constants.getTimerOpen("ahk3.trend.gf")) return;
//		
//		log.info("_______[ahk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("ahk3", "_______[ahk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfAhK3Service.updateCountJointBet(null);
//		}
//		log.info("_______[ahk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("ahk3", "_______[ahk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
