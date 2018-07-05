package com.gf.ssc.cqssc.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.ssc.cqssc.service.ICqSscService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 */
public class UpdateCqSscGaTrend extends QuartzJobBean{

	private static ICqSscService gfCqSscService;
	public ICqSscService getGfCqSscService(){
		return gfCqSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfCqSscService(ICqSscService gfCqSscService ){
		this.gfCqSscService = gfCqSscService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_CQSSC);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
////			gfCqSscService.updateFetchAndOpenOmit();
////			gfCqSscService.updateDayBetCount();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
