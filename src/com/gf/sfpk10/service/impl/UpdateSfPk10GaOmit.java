package com.gf.sfpk10.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.sfpk10.service.ISfPk10Service;

public class UpdateSfPk10GaOmit  extends QuartzJobBean{

	private static ISfPk10Service gfSfPk10Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setGfSfPk10Service(ISfPk10Service gfSfPk10Service){
		this.gfSfPk10Service = gfSfPk10Service;
	}
	
	public ISfPk10Service getGfSfPk10Service(){
		return gfSfPk10Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_SFPK10);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".omit.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			Date now=new Date();
//			String endTime=DateTimeUtil.DateToString(now);
//			Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//			if(now.getTime()<date.getTime()){
//				gfSfPk10Service.updateFetchAndOpenOmit();
//				gfSfPk10Service.updateDayBetCount();
//			}
//			
//			GameHelpUtil.log(gameCode,"omit count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"omit error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
