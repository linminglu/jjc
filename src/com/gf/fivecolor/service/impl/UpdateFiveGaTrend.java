package com.gf.fivecolor.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.fivecolor.service.IFiveService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 */
public class UpdateFiveGaTrend extends QuartzJobBean{

	private static IFiveService gfFiveService;
	public IFiveService getGfFiveService(){
		return gfFiveService;
	}
	@SuppressWarnings("static-access")
	public void setGfFiveService(IFiveService gfFiveService ){
		this.gfFiveService = gfFiveService;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_FC);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
////			gfFiveService.updateFetchAndOpenOmit();
////			gfFiveService.updateDayBetCount();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
