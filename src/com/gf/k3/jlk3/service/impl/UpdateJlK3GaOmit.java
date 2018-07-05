package com.gf.k3.jlk3.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.gf.k3.jlk3.service.IJlK3Service;

public class UpdateJlK3GaOmit  extends QuartzJobBean{

	private static IJlK3Service gfJlK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfJlK3Service(IJlK3Service gfJlK3Service){
		this.gfJlK3Service = gfJlK3Service;
	}
	
	public IJlK3Service getGfJlK3Service(){
		return this.gfJlK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
//		if(!Constants.getTimerOpen("jlk3.omit.gf")) return;
		
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfJlK3Service.updateFetchAndOpenOmit();
//			gfJlK3Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
