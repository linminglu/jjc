package com.xy.hk.sflhc;

public class SflhcConstants {


	public final static String common = "cpworld";

	//配置场次及时间间隔
	public final static Integer TIME_OPENING_FREEZE=120;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖
	public final static Integer TIME_INTERVAL=600;//场次间隔秒
	public final static Integer MAX_PART=124;//最大场次
	public final static String START_TIME=" 00:00:00";//开始时间后缀
	public final static String END_TIME=" 23:59:59";//结束时间后缀
	
	public final static String OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String WIN = "1";//1=中奖
	public final static String WIN_NO = "2";//2=未中奖
	public final static String WIN_HE = "3";//3=打和

	public final static Integer MAX_BET_POINT = 100000; //单注投注最大值
	public final static Integer MAX_TOTAL_BET_POINT = 50000; //单期投注最大值五万

	
	

}
