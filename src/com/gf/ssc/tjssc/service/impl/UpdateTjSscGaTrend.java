package com.gf.ssc.tjssc.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.ssc.tjssc.service.ITjSscService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 */
public class UpdateTjSscGaTrend extends QuartzJobBean{

	private static ITjSscService gfTjSscService;
	public ITjSscService getGfTjSscService(){
		return gfTjSscService;
	}
	@SuppressWarnings("static-access")
	public void setGfTjSscService(ITjSscService gfTjSscService ){
		this.gfTjSscService = gfTjSscService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_TJSSC);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
////			gfTjSscService.updateFetchAndOpenOmit();
////			gfTjSscService.updateDayBetCount();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
