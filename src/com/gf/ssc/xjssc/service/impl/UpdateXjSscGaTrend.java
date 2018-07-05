package com.gf.ssc.xjssc.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.ssc.xjssc.service.IXjSscService;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 */
public class UpdateXjSscGaTrend extends QuartzJobBean{

	private static IXjSscService gfXjSscService;
	public IXjSscService getGfXjSscService(){
		return gfXjSscService;
	}
	@SuppressWarnings("static-access")
	public void setGfXjSscService(IXjSscService gfXjSscService ){
		this.gfXjSscService = gfXjSscService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_XJSSC);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
////			gfXjSscService.updateFetchAndOpenOmit();
////			gfXjSscService.updateDayBetCount();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
