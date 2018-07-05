package com.gf.k3.jxk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.gf.k3.jxk3.service.IJxK3Service;

public class UpdateJxK3GaOmit  extends QuartzJobBean{

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
		
//		if(!Constants.getTimerOpen("jxk3.omit.gf")) return;
		
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfJxK3Service.updateFetchAndOpenOmit();
//			gfJxK3Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
