package com.gf.dcb.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.Constants;
import com.gf.dcb.model.GfDcbGaSession;
import com.gf.dcb.service.IDcbService;
import com.gf.fivecolor.model.GfFiveGaSession;
import com.framework.util.DateTimeUtil;
import com.framework.util.ParamUtils;
import com.gf.ssc.cqssc.model.GfCqSscGaSession;

/**
 * 定时去官网抓取开奖数据
 * @author hpz
 */
public class FetchDcbResult extends QuartzJobBean{
    private static IDcbService service;
    protected final Log log = LogFactory.getLog(getClass());
    
    public static IDcbService getDcbService(){
    	return service;
    }
    
    public void setDcbService(IDcbService service){
    	this.service = service;
    }
    
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	
    	if(!Constants.getTimerOpen("dcb.fetchResult.gf")) return;

    	log.info("-------双色球 开始 开奖---------"+DateTimeUtil.DateToStringAll(new Date()));
    	List<GfDcbGaSession> list=service.updateAndOpenResult();
    	log.info("-------------------trend-------");
		List<GfDcbGaSession> savelist=new ArrayList<GfDcbGaSession>();
		
		Collections.sort(list,new Comparator<GfDcbGaSession>(){
		    public int compare(GfDcbGaSession o1, GfDcbGaSession o2){  
		       if(o1.getSessionId() < o2.getSessionId()){  
		           return -1;        
		         }  
		      if(o1.getSessionId() ==o2.getSessionId()){  
		          return 0;  
		         }  
		          return 1;
		       }  
		    });

		for(GfDcbGaSession session:list){
			service.updateFetchAndOpenTrendResult();
			log.info("-------------------trend-------");
			String flag = service.updateGfDcbGaSessionOpenResultMethod(session, session.getOpenResult(),null);
			if(ParamUtils.chkString(flag)){
				session.setOpenSuccess(Constants.PUB_STATUS_OPEN);//这些期开奖计算出错
				savelist.add(session);
			}else{
				service.updateDayBetCount(session);
			}
			service.updateFetchAndOpenOmit(session);
		}
		service.updateObjectList(savelist, null);
    	log.info("-------双色球 开始 开奖-----结束----"+DateTimeUtil.DateToStringAll(new Date()));

    }
}
