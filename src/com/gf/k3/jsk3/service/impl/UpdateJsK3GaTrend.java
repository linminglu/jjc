package com.gf.k3.jsk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.jsk3.service.IJsK3Service;

public class UpdateJsK3GaTrend extends QuartzJobBean{

	private static IJsK3Service gfJsK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfJsK3Service(IJsK3Service gfJsK3Service){
		this.gfJsK3Service = gfJsK3Service;
	}
	
	public IJsK3Service getGfJsK3Service(){
		return this.gfJsK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("jsk3.trend.gf")) return;
//		
//		log.info("_______[jsk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("jsk3", "_______[jsk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfJsK3Service.updateCountJointBet(null);
//		}
//		log.info("_______[jsk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("jsk3", "_______[jsk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
