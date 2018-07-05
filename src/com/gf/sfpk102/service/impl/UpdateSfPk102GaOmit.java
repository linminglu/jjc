package com.gf.sfpk102.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.sfpk102.service.ISfPk102Service;

public class UpdateSfPk102GaOmit  extends QuartzJobBean{

	private static ISfPk102Service gfSfPk102Service;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setGfSfPk102Service(ISfPk102Service gfSfPk102Service){
		this.gfSfPk102Service = gfSfPk102Service;
	}
	
	public ISfPk102Service getGfSfPk102Service(){
		return gfSfPk102Service;
	}
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_SFPK102);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".omit.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			Date now=new Date();
//			String endTime=DateTimeUtil.DateToString(now);
//			Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//			if(now.getTime()<date.getTime()){
//				gfSfPk102Service.updateFetchAndOpenOmit();
//				gfSfPk102Service.updateDayBetCount();
//			}
//			
//			GameHelpUtil.log(gameCode,"omit count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"omit error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
