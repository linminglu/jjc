package com.xy.k10.cqk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xy.k10.cqk10.service.ICqK10Service;

public class UpdateCqK10GaTrend extends QuartzJobBean{

	private static ICqK10Service cqK10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setCqK10Service(ICqK10Service cqK10Service){
		this.cqK10Service = cqK10Service;
	}
	
	public ICqK10Service getCqK10Service(){
		return cqK10Service;
	}
	
	protected void executeInternal(JobExecutionContext arg0)throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_CQK10);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.xy")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_XY_CQK10);
//			if(gaSessionInfo != null && Constants.PUB_STATUS_OPEN.equals(gaSessionInfo.getStatus()) && 
//					Constants.PUB_STATUS_OPEN.equals(gaSessionInfo.getBetAvoid())){
//				cqK10Service.updateGaTrend();
//			}
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
