package com.xy.lucky28.xjplu28;

public class XjpLu28Constants {
	public final static String common = "cpworld";
	
	public final static String LUCKY28_SESSION_REF_DATE="2017-01-01 00:00:00";
	public final static Integer LUCKY28_SESSION_NO_REF = 0;//第增加一天就+1

	//配置场次及时间间隔
	public final static Integer LUCKY28_TIME_OPENING_FREEZE=30;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer LUCKY28_TIME_INTERVAL=2;//场次间隔分钟
	public final static Integer LUCKY28_MAX_FIRST=180;//最大场次
	public final static Integer LUCKY28_MAX_SECOND=660;//最大场次
	public final static String LUCKY28_START_TIME_END=" 00:00:00";//开始时间后缀
	public final static String LUCKY28_END_TIME_END=" 21:30:00";//开始时间后缀
	public final static String LUCKY28_START_TIME_S=" 08:00:00";//游戏开始时间点
	public final static String LUCKY28_START_TIME_STR=" 08:02:00";//游戏开始时间第一期
	public final static Integer LUCKY28_END_TIME_HOUR=21;//游戏游戏结束小时
	public final static Integer LUCKY28_END_TIME_MINUTE=50;//游戏结束小时
	
	
	public final static String LUCKY28_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String LUCKY28_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String LUCKY28_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static Integer SINGLE_NOTE_MAX_BET_POINT = 100000; //单注投注最大值1万。
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。

}
