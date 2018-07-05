package com.gf.k3.jxk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.jxk3.service.IJxK3Service;

public class UpdateJxK3GaTrend extends QuartzJobBean{

	private static IJxK3Service gfJxK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfJxK3Service(IJxK3Service gfJxK3Service){
		this.gfJxK3Service = gfJxK3Service;
	}
	
	public IJxK3Service getGfJxK3Service(){
		return this.gfJxK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("jxk3.trend.gf")) return;
//		
//		log.info("_______[jxk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("jxk3", "_______[jxk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfJxK3Service.updateCountJointBet(null);
//		}
//		log.info("_______[jxk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("jxk3", "_______[jxk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
