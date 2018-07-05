package com.gf.ssc.tjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.ssc.tjssc.service.ITjSscService;

/**
 * 计算合买投注值并生成订单。
 * @author admin
 *
 */
public class CountTjSscJoinBet extends QuartzJobBean{

	private static ITjSscService gfTjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static ITjSscService getGfTjSscService() {
		return gfTjSscService;
	}
	
	@SuppressWarnings("static-access")
	public void setGfTjSscService(ITjSscService gfTjSscService) {
		this.gfTjSscService = gfTjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		if(Constants.getGameTimerOpen().equals("false")) return;
//		
//		try {
//			
//			Date date = DateTimeUtil.getDateFormart(new Date(), "HH:mm:ss");//当前时间
//			Date d1 = DateTimeUtil.stringToDate("01:56:00", "HH:mm:ss");
//			Date d2 = DateTimeUtil.stringToDate("09:59:00", "HH:mm:ss");
//			if(d1.compareTo(date) ==1 || d2.compareTo(date) <1){
//				log.info("---------tjssc -计算合买投注--- 开始--"+DateTimeUtil.DateToStringAll(new Date()));
//				gfTjSscService.updateCountJointBet(null); // 计算合买投注
//				log.info("---------tjssc -计算合买投注--- 结束--"+DateTimeUtil.DateToStringAll(new Date()));
//
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
