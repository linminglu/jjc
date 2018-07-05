package com.gf.ssc.xjssc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.ssc.xjssc.service.IXjSscService;

/**
 * 计算合买投注值并生成订单。
 *
 */
public class CountXjSscJoinBet extends QuartzJobBean{

	private static IXjSscService gfXjSscService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IXjSscService getGfXjSscService() {
		return gfXjSscService;
	}
	@SuppressWarnings("static-access")
	public void setGfXjSscService(IXjSscService gfXjSscService) {
		this.gfXjSscService = gfXjSscService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
//		
//		if(Constants.getGameTimerOpen().equals("false")) return;
//		
//		try {
//			
//			Date date = DateTimeUtil.getDateFormart(new Date(), "HH:mm:ss");//当前时间
//			Date d1 = DateTimeUtil.stringToDate("01:56:00", "HH:mm:ss");
//			Date d2 = DateTimeUtil.stringToDate("09:59:00", "HH:mm:ss");
//			if(d1.compareTo(date) ==1 || d2.compareTo(date) <1){
//				log.info("---------xjssc -计算合买投注--- 开始--"+DateTimeUtil.DateToStringAll(new Date()));
//				gfXjSscService.updateCountJointBet(null); // 计算合买投注
//				log.info("---------xjssc -计算合买投注--- 结束--"+DateTimeUtil.DateToStringAll(new Date()));
//
//			}
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
