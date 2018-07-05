package com.jc.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.framework.util.DateTimeUtil;
import com.jc.service.IJcService;


/**
 * 定时计算赔率
 * @author zzw
 *
 */
public class UpdateJingcaiOdds extends QuartzJobBean {
	
	protected final Log log = LogFactory.getLog(getClass());
	private static IJcService jcService;
	

	public static IJcService getJcService() {
		return jcService;
	}

	public void setJcService(IJcService jcService) {
		this.jcService = jcService;
	}


	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		
		try {
			log.info("_______[竞猜定时计算赔率开始]["+DateTimeUtil.getDateTime()+"]___________________");
			
			// 参数1： 计算赔率的基数， 参数2： 平台抽税的税率
			jcService.updateJingcaiOdds();
			
			log.info("_______[竞猜定时计算赔率结束]["+DateTimeUtil.getDateTime()+"]___________________");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("_______[竞猜定时计算赔率出错]["+DateTimeUtil.getDateTime()+"]___________________");
		}
		
	}

}
