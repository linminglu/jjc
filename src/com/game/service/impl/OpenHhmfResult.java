package com.game.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.game.service.IGaHhmfService;

/**
 * 定时15分钟去开奖
 * 由于需要抓取官网数据有，延迟，所以开奖实际是是取到数据由另一个定时器FetchK10Result去做开奖处理
 */
public class OpenHhmfResult extends QuartzJobBean {

	private static IGaHhmfService gaHhmfService;
	protected final Log log = LogFactory.getLog(getClass());
	
	public static IGaHhmfService getGaHhmfService() {
		return gaHhmfService;
	}

	@SuppressWarnings("static-access")
	public void setGaHhmfService(IGaHhmfService gaHhmfService) {
		this.gaHhmfService = gaHhmfService;
	}

	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
//			log.info("_______[hhmf check session]["+DateTimeUtil.getDateTime()+"]___________________");
//			
//			gaHhmfService.updateCheckSession();
//			
//			log.info("_______[hhmf check session end]["+DateTimeUtil.getDateTime()+"]___________________");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}