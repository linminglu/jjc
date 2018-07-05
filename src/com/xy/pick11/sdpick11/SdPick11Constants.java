package com.xy.pick11.sdpick11;

public class SdPick11Constants {

	public final static String SD_PICK11_START_TIME_STR=" 08:25:00";//游戏开始时间第一期
	public final static Integer SD_PICK11_MAX_PART=87;//最大场次
	public final static Integer SD_PICK11_TIME_INTERVAL=10;//场次间隔分钟
	
	public final static Integer SD_PICK11_TIME_OPENING_FREEZE=60;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖

	public final static String SD_PICK11_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String SD_PICK11_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String SD_PICK11_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String SD_PICK11_WIN_INIT = "0";//0=未开奖 1=中奖  2=未中奖
	public final static String SD_PICK11_WIN = "1";//0=未开奖 1=中奖  2=未中奖
	public final static String SD_PICK11_WIN_NOT = "2";//0=未开奖 1=中奖  2=未中奖
	public final static String SD_PICK11_WIN_HE = "3";//3=和
	
	public final static Integer SDPICK11_MAX_BET_POINT = 100000; // 单注投注最大值
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。
	

}
