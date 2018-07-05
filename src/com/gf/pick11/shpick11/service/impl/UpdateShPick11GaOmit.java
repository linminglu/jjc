package com.gf.pick11.shpick11.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.gf.pick11.shpick11.service.IShPick11Service;

public class UpdateShPick11GaOmit  extends QuartzJobBean{

	private static IShPick11Service gfShPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	public void setGfShPick11Service(IShPick11Service gfShPick11Service){
		this.gfShPick11Service = gfShPick11Service;
	}
	
	public IShPick11Service getGfShPick11Service(){
		return this.gfShPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
//		log.info("_______[gdk10 fetch omit form session]["+DateTimeUtil.getDateTime()+"]___________________");
//		Date now=new Date();
//		String endTime=DateTimeUtil.DateToString(now);
//		Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//		if(now.getTime()<date.getTime()){
//			gfShPick11Service.updateFetchAndOpenOmit();
//			gfShPick11Service.updateDayBetCount();
//		}
//		log.info("_______[gdk10 fetch omit form session end]["+DateTimeUtil.getDateTime()+"]___________________");
	}

}
