package com.gf.ssc.jxssc.service.impl;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.framework.util.DateTimeUtil;
import com.gf.ssc.jxssc.service.IJxSscService;

/**
 * 计算合买投注值并生成订单。
 * @author admin
 *
 */
public class CountJxSscJoinBet extends QuartzJobBean{

	private static IJxSscService service;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IJxSscService getJxSscService() {
		return service;
	}
	
	public void setJxSscService(IJxSscService service) {
		this.service = service;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		
		if(Constants.getGameTimerOpen().equals("false")) return;
		
		try {
			
			log.info("---------jxssc -计算合买投注--- 开始--"+DateTimeUtil.DateToStringAll(new Date()));
			service.updateCountJointBet(null); // 计算合买投注
			log.info("---------jxssc -计算合买投注--- 结束--"+DateTimeUtil.DateToStringAll(new Date()));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
