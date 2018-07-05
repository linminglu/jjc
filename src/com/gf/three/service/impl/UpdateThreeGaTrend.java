package com.gf.three.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.three.service.IThreeService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 */
public class UpdateThreeGaTrend extends QuartzJobBean{

	private static IThreeService gfThreeService;
	public IThreeService getGfThreeService(){
		return gfThreeService;
	}
	@SuppressWarnings("static-access")
	public void setGfThreeService(IThreeService gfThreeService ){
		this.gfThreeService = gfThreeService;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_THREE);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
////			gfThreeService.updateFetchAndOpenOmit();
////			gfThreeService.updateDayBetCount();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
