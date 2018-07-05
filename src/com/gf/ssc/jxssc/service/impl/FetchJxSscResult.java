package com.gf.ssc.jxssc.service.impl;

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
import com.framework.util.DateTimeUtil;
import com.framework.util.ManageFile;
import com.framework.util.ParamUtils;
import com.gf.ssc.jxssc.model.GfJxSscGaSession;
import com.gf.ssc.jxssc.service.IJxSscService;



/**
 * 定时去官网抓取开奖数据
 * @author hpz
 */
public class FetchJxSscResult extends QuartzJobBean{
    private static IJxSscService jxSscService;
    protected final Log log = LogFactory.getLog(getClass());
    
    public static IJxSscService getJxSscService(){
    	return jxSscService;
    }
    
    public void setJxSscService(IJxSscService jxSscService){
    	this.jxSscService = jxSscService;
    }
    
    protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
    	
    	if(!Constants.getTimerOpen("jxssc.fetchResult.gf")) return;
    	
    	log.info("-------江西时时彩 ---开始开奖---"+DateTimeUtil.DateToStringAll(new Date()));
		List<GfJxSscGaSession> list=jxSscService.updateAndOpenResult();

		Collections.sort(list,new Comparator<GfJxSscGaSession>(){
		    public int compare(GfJxSscGaSession o1, GfJxSscGaSession o2){  
		       if(o1.getSessionId() < o2.getSessionId()){  
		           return -1;        
		         }  
		      if(o1.getSessionId() ==o2.getSessionId()){  
		          return 0;  
		         }  
		          return 1;
		       }  
		    });  

		List<GfJxSscGaSession> savelist=new ArrayList<GfJxSscGaSession>();
		for(GfJxSscGaSession session:list){
			//ManageFile.writeTextToFile("[sessionNo]"+session.getSessionNo()+"\n", Constants.getWebRootPath()+"/gamelogo/jxsscjilu.txt", true);
//			List<Object> para = new ArrayList<Object>();


			jxSscService.updateFetchAndOpenTrendResult(session);

			String flag = jxSscService.updateJxSscSessionOpenResultMethod(session, session.getOpenResult(),null);
			if(ParamUtils.chkString(flag)){
				session.setOpenSuccess(Constants.PUB_STATUS_OPEN);//这些期开奖计算出错
				savelist.add(session);
			}else{
				jxSscService.updateDayBetCount(session);
			}
			jxSscService.updateFetchAndOpenOmit(session);
		}
		jxSscService.updateObjectList(savelist, null);
    	log.info("-------江西时时彩 ---开奖结束---"+DateTimeUtil.DateToStringAll(new Date()));

    }
}
