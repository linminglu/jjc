package com.game.service.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.apps.eff.BlackListCacheUtil;

public class Online extends QuartzJobBean {
	
	protected final Log log = LogFactory.getLog(getClass());

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		long DURING_TIME = 120*1000;//毫秒
		int size = 0;
		Map<String, Long> onlineMap = BlackListCacheUtil.getOnlineListU();
		Map<String, Long> mapAll =new HashMap<String, Long>();
		mapAll.putAll(onlineMap);
		
		for (Iterator<String> i = mapAll.keySet().iterator(); i.hasNext(); ) {
			String key = i.next();
			Long time = mapAll.get(key);
			Long dtNow=System.currentTimeMillis();//当前时间
    		if(time==null) time = dtNow;
    		long diff = dtNow-time;// 时间差
    		
    		if(diff<DURING_TIME){//小于2分钟，证明在线
    			size=size+1;
    		}else{
    			BlackListCacheUtil.delOnlineListU(key);
    		}
		}
		BlackListCacheUtil.onlineNumber=size;
	}

}
