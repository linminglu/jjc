package com.gf.dcb.service.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.gf.dcb.service.IDcbService;

public class FetchDcbOmit  extends QuartzJobBean{
    private static IDcbService service;
    protected final Log log = LogFactory.getLog(getClass());
    
    public static IDcbService getDcbService(){
    	return service;
    }
    
    public void setDcbService(IDcbService service){
    	this.service = service;
    }
    
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {			

//			service.updateFetchAndOpenOmit();
//			service.updateDayBetCount();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}
