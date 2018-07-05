package com.gf.k3.bjk3.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.k3.bjk3.service.IBjK3Service;

public class UpdateBjK3GaTrend extends QuartzJobBean{

	private static IBjK3Service gfBjK3Service;
	protected final Log log = LogFactory.getLog(getClass());
	@SuppressWarnings("static-access")
	public void setGfBjK3Service(IBjK3Service gfBjK3Service){
		this.gfBjK3Service = gfBjK3Service;
	}
	
	public IBjK3Service getGfBjK3Service(){
		return this.gfBjK3Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_BJK3);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".trend.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();

//			Date now=new Date();
//			String endTime=DateTimeUtil.DateToString(now);
//			Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//			if(now.getTime()<date.getTime()){
//				gfBjK3Service.updateCountJointBet(null);
//			}
			
//			GameHelpUtil.log(gameCode,"trend count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"trend error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
