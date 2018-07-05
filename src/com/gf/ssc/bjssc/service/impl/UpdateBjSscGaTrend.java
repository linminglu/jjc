package com.gf.ssc.bjssc.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.ssc.bjssc.service.IBjSscService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 */
public class UpdateBjSscGaTrend extends QuartzJobBean{

	private static IBjSscService gfBjSscService;
	public IBjSscService getGfBjSscService(){
		return gfBjSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfBjSscService(IBjSscService gfBjSscService ){
		this.gfBjSscService = gfBjSscService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_BJSSC);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
////			gfBjSscService.updateFetchAndOpenOmit();
////			gfBjSscService.updateDayBetCount();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
