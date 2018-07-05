package com.xy.pick11.jxpick11.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xy.pick11.jxpick11.service.IJxPick11Service;

public class UpdateJxPick11GaTrend extends QuartzJobBean{

	private static IJxPick11Service jxPick11Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setJxPick11Service(IJxPick11Service jxPick11Service){
		this.jxPick11Service = jxPick11Service;
	}
	
	public IJxPick11Service getJxPick11Service(){
		return jxPick11Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_JXPICK11);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.xy")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			Date now=new Date();
//			String endTime=DateTimeUtil.DateToString(now);
//			Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//			if(now.getTime()<date.getTime()){
//				GaSessionInfo gaSessionInfo = (GaSessionInfo) CacheUtil.getGameMap().get(Constants.GAME_TYPE_XY_JXPICK11);
//				if(gaSessionInfo != null && Constants.PUB_STATUS_OPEN.equals(gaSessionInfo.getStatus()) && 
//						Constants.PUB_STATUS_OPEN.equals(gaSessionInfo.getBetAvoid())){
//					jxPick11Service.updateGaTrend();
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
