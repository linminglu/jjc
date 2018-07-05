package com.gf.k3.shk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.shk3.service.IShK3Service;

public class UpdateShK3GaTrend extends QuartzJobBean{

	private static IShK3Service gfShK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfShK3Service(IShK3Service gfShK3Service){
		this.gfShK3Service = gfShK3Service;
	}
	
	public IShK3Service getGfShK3Service(){
		return this.gfShK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("shk3.trend.gf")) return;
//		
//		log.info("_______[shk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("shk3", "_______[shk3 fetch trend form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfShK3Service.updateCountJointBet(null);
//		}
//		log.info("_______[shk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
//		GameHelpUtil.log("shk3", "_______[shk3 fetch trend form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
