package com.xy.bj3.service.impl;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.xy.bj3.service.IBj3Service;

/**
 * 定时更新冷热榜，比开奖结果晚一分钟。
 * @author hpz
 */
public class UpdateBj3GaTrend extends QuartzJobBean{

	private static IBj3Service bj3Service;
	public IBj3Service getBj3Service(){
		return bj3Service;
	}
	
	@SuppressWarnings("static-access")
	public void setBj3Service(IBj3Service bj3Service ){
		this.bj3Service = bj3Service;
	}
	
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_XY_BJ3);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.xy")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			bj3Service.updateGaTrend();
//			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
