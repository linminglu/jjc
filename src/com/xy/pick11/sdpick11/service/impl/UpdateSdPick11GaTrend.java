package com.xy.pick11.sdpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xy.pick11.sdpick11.service.ISdPick11Service;

public class UpdateSdPick11GaTrend extends QuartzJobBean{

	private static ISdPick11Service sdPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setSdPick11Service(ISdPick11Service sdPick11Service){
		this.sdPick11Service = sdPick11Service;
	}
	
	public ISdPick11Service getSdPick11Service(){
		return sdPick11Service;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_SDPICK11);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.xy")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			Date now=new Date();
//			String endTime=DateTimeUtil.DateToString(now);
//			Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//			if(now.getTime()<date.getTime()){
//				GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_XY_SDPICK11);
//				if(gaSessionInfo != null && Constants.PUB_STATUS_OPEN.equals(gaSessionInfo.getStatus()) && 
//						Constants.PUB_STATUS_OPEN.equals(gaSessionInfo.getBetAvoid())){
//					sdPick11Service.updateGaTrend();
//				}
//			}
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
