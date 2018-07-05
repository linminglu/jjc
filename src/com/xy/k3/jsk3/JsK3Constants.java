package com.xy.k3.jsk3;

/**
 * 游戏模块常量类
 * @author Administrator
 */
public final class JsK3Constants {

	public final static String common = "cpworld";
	
	public final static String JSK3_SESSION_REF_DATE="2017-01-01 00:00:00";
	public final static Integer JSK3_SESSION_NO_REF = 0;//第增加一天就+1

	//配置场次及时间间隔
	public final static Integer JSK3_TIME_OPENING_FREEZE=60;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer JSK3_TIME_INTERVAL=10;//场次间隔分钟
	public final static Integer JSK3_MAX_PART=82;//最大场次
	public final static String JSK3_START_TIME_END=" 00:00:00";//开始时间后缀
	public final static String JSK3_END_TIME_END=" 21:30:00";//开始时间后缀
	public final static String JSK3_START_TIME_S=" 08:30:00";//游戏开始时间点
	public final static String JSK3_START_TIME_STR=" 09:05:00";//游戏开始时间第一期
	public final static Integer JSK3_END_TIME_HOUR=21;//游戏游戏结束小时
	public final static Integer JSK3_END_TIME_MINUTE=50;//游戏结束小时
	
	public final static String JSK3_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String JSK3_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String JSK3_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String JSK3_WIN_INIT = "0";//0=未开奖 1=中奖  2=未中奖
	public final static String JSK3_WIN = "1";//0=未开奖 1=中奖  2=未中奖
	public final static String JSK3_WIN_NOT = "2";//0=未开奖 1=中奖  2=未中奖
	public final static String JSK3_WIN_HE = "3";//0=未开奖 1=中奖  2=未中奖   3=打和
	
	public final static Integer TOTAL_BET_FIVE_THOUSAND = 5000; // 单注投注最大金额5000
	public final static Integer ToTAL_BET_TEN_THOUSAND = 100000; // 单注投注最大金额10000
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。
}
