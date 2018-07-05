package com.gf.ssc.jxssc.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.ssc.jxssc.service.IJxSscService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 * @author hpz
 */
public class UpdateJxSscGaTrend extends QuartzJobBean{

	private static IJxSscService jxsscService;
	public IJxSscService getJxSscService(){
		return jxsscService;
	}
	public void setJxSscService(IJxSscService jxsscService ){
		this.jxsscService = jxsscService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		if(!Constants.getTimerOpen("jxssc.trend.gf")) return;
		
		try{
//			jxsscService.updateFetchAndOpenOmit();
//			jxsscService.updateDayBetCount();

		}catch(Exception e){
			
		}
	}

}
