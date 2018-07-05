package com.ram.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 用户测试程序前后执行性能的工具类
 * @author Administrator
 *
 */
public class TimeCostUtil {
	
	protected static final Log log = LogFactory.getLog(TimeCostUtil.class);
	
	private static long begin_time=0;
	private static long end_time=0;
	
	public TimeCostUtil(){
		TimeCostUtil.begin_time=System.currentTimeMillis();
	}
	
	public static void init(){
		TimeCostUtil.begin_time=System.currentTimeMillis();
	}
	public static void init(String where){
		TimeCostUtil.begin_time=System.currentTimeMillis();
		log.fatal(where);
	}	
	
	public static void complete(){
		long costTime=0;
		TimeCostUtil.end_time=System.currentTimeMillis();
		costTime=end_time - begin_time;
		log.fatal("耗时:" + String.valueOf(costTime) +"毫秒");
		TimeCostUtil.begin_time=System.currentTimeMillis();
	}
	
	public static void complete(String where){
		long costTime=0;
		TimeCostUtil.end_time=System.currentTimeMillis();
		costTime=end_time - begin_time;
		log.fatal(where + " 耗时:" + String.valueOf(costTime) + "毫秒");
		TimeCostUtil.begin_time=System.currentTimeMillis();

	}
	
	
}
