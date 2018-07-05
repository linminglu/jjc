package com.gf.k3.bjk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.gf.k3.bjk3.service.IBjK3Service;

public class UpdateBjK3GaOmit  extends QuartzJobBean{

	private static IBjK3Service gfBjK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfBjK3Service(IBjK3Service gfBjK3Service){
		this.gfBjK3Service = gfBjK3Service;
	}
	
	public IBjK3Service getGfBjK3Service(){
		return this.gfBjK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("bjk3.omit.gf")) return;
		
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfBjK3Service.updateFetchAndOpenOmit();
//			gfBjK3Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
