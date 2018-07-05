package com.gf.dcb.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.dcb.service.IDcbService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 * @author hpz
 */
public class UpdateDcbGaTrend extends QuartzJobBean{

	private static IDcbService gfDcbService;
	public IDcbService getGfDcbService(){
		return gfDcbService;
	}
	public void setGfDcbService(IDcbService gfDcbService ){
		this.gfDcbService = gfDcbService;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		if(!Constants.getTimerOpen("dcb.trend.gf")) return;
		
//		try{
//			gfDcbService.updateFetchAndOpenTrendResult();
//			
//		}catch(Exception e){
//			
//		}
	}

}
