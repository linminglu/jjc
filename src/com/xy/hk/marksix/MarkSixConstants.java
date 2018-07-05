package com.xy.hk.marksix;

public class MarkSixConstants {


	public final static String common = "cpworld";
		
	public final static String MARKSIX_SESSION_REF_DATE="2017-01-01 00:00:00";
	public final static Integer MARKSIX_SESSION_NO_REF = 0;//第增加一天就+1

	//配置场次及时间间隔
	public final static Integer MARKSIX_TIME_OPENING_FREEZE=300;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer MARKSIX_TIME_INTERVAL=10;//场次间隔分钟
	public final static Integer MARKSIX_MAX_PART=88;//最大场次
	public final static String MARKSIX_START_TIME_END=" 00:00:00";//开始时间后缀
	public final static String MARKSIX_END_TIME_END=" 21:30:00";//开始时间后缀
	public final static String MARKSIX_START_TIME_S=" 08:22:00";//游戏开始时间点
	public final static String MARKSIX_START_TIME_STR=" 09:05:00";//游戏开始时间第一期
	public final static Integer MARKSIX_END_TIME_HOUR=21;//游戏游戏结束小时
	public final static Integer MARKSIX_END_TIME_MINUTE=50;//游戏结束小时
	
	
	public final static String MARKSIX_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String MARKSIX_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String MARKSIX_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String MARKSIX_WIN = "1";//1=中奖
	public final static String MARKSIX_WIN_NO = "2";//2=未中奖
	public final static String MARKSIX_WIN_HE = "3";//3=打和

	
//	public final static long MARKSIX_BET_END_INTERVAL = 1*60*60;//投注截止时间到开奖时间间隔1小时。
	
	public final static Integer MARKSIX_MAX_BET_POINT = 100000; //单注投注最大值
	public final static Integer MARKSIX_MAX_TOTAL_BET_POINT = 50000; //单期投注最大值五万

	
	

}
