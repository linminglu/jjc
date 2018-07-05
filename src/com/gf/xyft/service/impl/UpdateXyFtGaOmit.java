package com.gf.xyft.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.xyft.service.IXyFtService;

public class UpdateXyFtGaOmit  extends QuartzJobBean{

	private static IXyFtService gfXyFtService;
	protected final Log log = LogFactory.getLog(getClass());
	
	@SuppressWarnings("static-access")
	public void setGfXyFtService(IXyFtService gfXyFtService){
		this.gfXyFtService = gfXyFtService;
	}
	
	public IXyFtService getGfXyFtService(){
		return gfXyFtService;
	}
	
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		String gameCode = Constants.getGameCodeOfGameType(Constants.GAME_TYPE_GF_XYFT);
//		if(!Constants.getTimerOpen(gameCode.substring(2)+".omit.gf")) return;
//		try {
//			long t1 = System.currentTimeMillis();
//			
//			Date now=new Date();
//			String endTime=DateTimeUtil.DateToString(now);
//			Date date=DateTimeUtil.stringToDate(endTime+" 23:05:00", "yyyy-MM-dd HH:mm:ss");			
//			if(now.getTime()<date.getTime()){
//				gfXyFtService.updateFetchAndOpenOmit();
//				gfXyFtService.updateDayBetCount();
//			}
//			
//			GameHelpUtil.log(gameCode,"omit count["+(System.currentTimeMillis()-t1)+"ms]");
//		} catch (Exception e) {
//			GameHelpUtil.log(gameCode,"omit error:"+e.getMessage());
//			//e.printStackTrace();
//		}
	}

}
