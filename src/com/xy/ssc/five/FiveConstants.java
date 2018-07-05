package com.xy.ssc.five;

public class FiveConstants {
	public final static String FIVE_START_TIME_STR=" 00:00:00";//游戏开始时间第一期

	public final static Integer FIVE_MAX_PART=288;//最大场次
	public final static Integer FIVE_TIME_INTERVAL=300;//场次间隔秒300

	public final static Integer FIVE_TIME_OPENING_FREEZE = 60;//[秒]开奖中冻结期，即可不允许在投注，等官网开奖

	public final static String FIVE_OPEN_STATUS_INIT = "0";//0=未开奖 1=开奖中  2=已开奖
	public final static String FIVE_OPEN_STATUS_OPENING = "1";//0=未开奖 1=开奖中  2=已开奖
	public final static String FIVE_OPEN_STATUS_OPENED = "2";//0=未开奖 1=开奖中  2=已开奖
	
	public final static String FIVE_PLAY_TYPE_TWO_FACE = "0";//0=两面盘 1=1-10名  2=冠亚军和
	public final static String FIVE_PLAY_TYPE_ONE_TO_TEN = "1";//0=两面盘 1=1-10名  2=冠亚军和
	public final static String FIVE_PLAY_TYPE_SUM = "2";//0=两面盘 1=1-10名  2=冠亚军和
	
	public final static String FIVE_WIN_INIT = "0";//0=未开奖 1=中奖  2=未中奖
	public final static String FIVE_WIN = "1";//0=未开奖 1=中奖  2=未中奖
	public final static String FIVE_WIN_NOT = "2";//0=未开奖 1=中奖  2=未中奖
	public final static String FIVE_WIN_HE = "3";//0=未开奖 1=中奖  2=未中奖   3=打和
	
	public final static Integer SINGLE_NOTE_MAX_BET_POINT = 100000; //单注投注最大值1万。
	public final static Integer TOTAL_MAX_BET_POINT=50000; //单期投注总值最大为5万。

}
